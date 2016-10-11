package com.tele.service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.collect.Lists;
import com.tele.model.ChargeRequest;
import com.tele.model.ChargeResponse;
import com.tele.model.QueryResponse;
import com.tele.model.Response;
import com.tele.utils.StreamUtil;
import com.tele.utils.XLSUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangleimin
 * @package PACKAGE_NAME
 * @date 16-9-25
 */
@Log4j
public class RechargeServiceAccount extends BaseService {

    public static final String URL = "http://service.sh.189.cn/service/biz/pay/fenZhang";

    private QueryService queryService = new QueryService();

    public String recharge(WebClient webClient, ChargeRequest chargeRequest) {
        String result = "";
        try {
            HtmlPage chargePage = webClient.getPage(URL);
            HtmlInput account = chargePage.getHtmlElementById("phone"); // 分账序号
            HtmlDivision payMethodDiv = chargePage.getHtmlElementById("1188");  // 电信充值卡
            HtmlDivision buttonCharge = chargePage.querySelector(".btn.f18.btn_long");
            account.setValueAttribute(chargeRequest.getAccountNo());
            payMethodDiv.click();
            HtmlPage payPage = buttonCharge.click();
            HtmlInput cardPwd = payPage.getHtmlElementById("1188_pwd"); // 电信充值卡密码
            cardPwd.setValueAttribute(chargeRequest.getCardPwd());
            HtmlDivision payDiv = payPage.querySelector(".btn.f18.btn_long.mt20");
            HtmlPage resultPage = payDiv.click();
            HtmlDivision resultDiv = resultPage.querySelector(".i_content");
            result = resultDiv.asText();
        } catch (Exception e) {
            result = "system error";
            log.error(String.format("分账序号[%s], 充值异常", chargeRequest.getAccountNo()), e);
        }
        return result;
    }

    /**
     * 批量充值
     * @param chargeRequestList 充值请求
     * @return  充值结果
     */
    public List<ChargeResponse> rechargeBatch(List<ChargeRequest> chargeRequestList) {
        WebClient webClient = createWebClient();
        List<ChargeResponse> responseList = Lists.newArrayList();
        for (ChargeRequest chargeRequest : chargeRequestList) {
            responseList.add(rechargeOnce(webClient, chargeRequest));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.error(e);
            }
        }
        StreamUtil.close(webClient);
        return responseList;
    }

    public Response<String> recharge(String srcFile, String msgFile) {
        XLSUtil xlsUtil = new XLSUtil();
        Response<String> response = new Response<>();
        Response<List<ChargeRequest>> readResp = xlsUtil.read(srcFile);
        if (!readResp.isSuccess()) {
            response.setSuccess(false);
            response.setErrMsg(readResp.getErrMsg());
            return response;
        }
        List<ChargeRequest> requestList = readResp.getData();
        List<ChargeResponse> responseList = rechargeBatch(requestList);
        boolean result = xlsUtil.write(responseList, msgFile);
        log.info("执行结果：" + result);
        response.setSuccess(result);
        return response;
    }

    public Response<List<ChargeRequest>> readFile(String srcFile) {
        XLSUtil xlsUtil = new XLSUtil();
        return xlsUtil.read(srcFile);
    }

    /**
     * 单次充值
     * @param webClient 连接
     * @param chargeRequest 充值请求
     * @return  充值结果
     */
    public ChargeResponse rechargeOnce(WebClient webClient, ChargeRequest chargeRequest) {
        ChargeResponse response = new ChargeResponse();
        response.setSuccess(false);
        response.setAccountNo(chargeRequest.getAccountNo());
        response.setCardNo(chargeRequest.getCardNo());
        response.setCardPwd(chargeRequest.getCardPwd());
        int times = 0;
        while (!response.isSuccess() && times < 3) {
            String respStr = recharge(webClient, chargeRequest);
            response.setMessage(respStr);
            response.setSuccess(StringUtils.contains(respStr, "支付成功"));
            times++;
        }
        times = 0;
        if (!response.isSuccess()) {
            // 充值失败，查询卡信息
            Response<QueryResponse> queryBalance = new Response<>();
            while (!queryBalance.isSuccess() && times < 3) {
                queryBalance = queryService.queryBalance(chargeRequest.getCardNo());
                times++;
            }
            response.setQueryResponse(queryBalance.getData());
        }
        return response;
    }
}
