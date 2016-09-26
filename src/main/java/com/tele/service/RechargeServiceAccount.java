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
import com.tele.utils.StreamUtil;
import com.tele.utils.XLSUtil;
import lombok.extern.log4j.Log4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangleimin
 * @package PACKAGE_NAME
 * @date 16-9-25
 */
@Log4j
public class RechargeServiceAccount {

    public static final String URL = "http://service.sh.189.cn/service/biz/pay/fenZhang";

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

    public List<ChargeResponse> recharge(List<ChargeRequest> chargeRequestList) {
        WebClient webClient = createWebClient();
        List<ChargeResponse> responseList = Lists.newArrayList();
        int count = 0;
        for (ChargeRequest chargeRequest : chargeRequestList) {
            ChargeResponse response = new ChargeResponse();
            response.setAccountNo(chargeRequest.getAccountNo());
            response.setCardNo(chargeRequest.getCardNo());
            response.setCardPwd(chargeRequest.getCardPwd());
            response.setMessage(recharge(webClient, chargeRequest));
            responseList.add(response);
            count++;
            if (count % 10 == 0) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    log.error(e);
                }
            }
        }
        StreamUtil.close(webClient);
        return responseList;
    }

    public WebClient createWebClient() {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.setJavaScriptTimeout(30000);
        return webClient;
    }

    public void recharge(String srcFile, String msgFile) {
        XLSUtil xlsUtil = new XLSUtil();
        List<ChargeRequest> requestList = xlsUtil.read(srcFile);
        List<ChargeResponse> responseList = recharge(requestList);
        boolean result = xlsUtil.write(responseList, msgFile);
        log.info("执行结果：" + result);
    }

}
