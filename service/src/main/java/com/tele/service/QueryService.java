package com.tele.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.google.common.base.Strings;
import com.tele.model.QueryResponse;
import com.tele.model.Response;
import com.tele.utils.StreamUtil;
import com.tele.utils.ValidCodeUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author zhangleimin
 * @package com.tele.service
 * @date 16-9-30
 */
@Log4j
public class QueryService extends BaseService {

    public static final String URL = "http://service.sh.189.cn/service/card/cardBalance";

    public Response<QueryResponse> queryBalance(String cardNo) {
        WebClient webClient = null;
        Response<QueryResponse> response = new Response<>();
        try {
            webClient = createWebClient();
            HtmlPage queryPage = webClient.getPage(URL);
            HtmlInput inputCard = queryPage.getHtmlElementById("cardNum");
            HtmlInput inputCheck = queryPage.getHtmlElementById("check");
            HtmlDivision div = queryPage.querySelector(".operate-btns.marT30");
            HtmlAnchor button = div.querySelector(".sub-btn");
            inputCard.setValueAttribute(cardNo);
            HtmlImage image = queryPage.getHtmlElementById("randNum");
            image.saveAs(new File("d:\\tmpcode.jpg"));
            String code = ValidCodeUtil.decrypt();
            inputCheck.setValueAttribute(code);
            HtmlPage queryResult = button.click();
            HtmlDivision errDiv = queryResult.getHtmlElementById("failCode");
            if (!Strings.isNullOrEmpty(errDiv.asText().trim())) {
                response.setErrMsg(errDiv.asText().trim());
                response.setSuccess(false);
            } else {
                response.setData(convert(queryResult));
                response.setSuccess(true);
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setErrMsg("查询失败,【" + e + "】");
            log.error(e);
        } finally {
            StreamUtil.close(webClient);
        }
        return response;
    }

    private QueryResponse convert(HtmlPage queryResult) {
        HtmlTableDataCell requestUser = queryResult.getHtmlElementById("TrequestUser");
        HtmlTableDataCell cardNumber = queryResult.getHtmlElementById("TcardNumber");
        HtmlTableDataCell cardPublisher = queryResult.getHtmlElementById("TcardPublisher");
        HtmlTableDataCell cardStatus = queryResult.getHtmlElementById("Tcardstatus");
        HtmlTableDataCell expirationTime = queryResult.getHtmlElementById("TexpirationTime");
        HtmlTableDataCell prolongDays = queryResult.getHtmlElementById("Tprolongdays");
        HtmlTableDataCell cardValue = queryResult.getHtmlElementById("Tcardvalue");
        HtmlTableDataCell rechargeTime = queryResult.getHtmlElementById("TrechargeTime");
        HtmlTableDataCell callingNumber = queryResult.getHtmlElementById("TcallingNumber");
        HtmlTableDataCell destinationAccount = queryResult.getHtmlElementById("TdestinationAccount");
        HtmlTableDataCell destinationAttr = queryResult.getHtmlElementById("TdestinationAttr");
        QueryResponse response = new QueryResponse();
        response.setRequestUser(requestUser.getTextContent().trim());
        response.setCardNumber(cardNumber.getTextContent().trim());
        response.setCardPublisher(cardPublisher.getTextContent().trim());
        response.setCardStatus(cardStatus.getTextContent().trim());
        response.setExpirationTime(expirationTime.getTextContent().trim());
        response.setProlongDays(prolongDays.getTextContent().trim());
        response.setCardValue(cardValue.getTextContent().trim());
        response.setRechargeTime(rechargeTime.getTextContent().trim());
        response.setCallingNumber(callingNumber.getTextContent().trim());
        response.setDestinationAccount(destinationAccount.getTextContent().trim());
        response.setDestinationAttr(destinationAttr.getTextContent().trim());
        return response;
    }

    public static void main(String[] args) {
        QueryService queryService = new QueryService();
        Response<QueryResponse> response = queryService.queryBalance("1111111111111");
        System.out.println(response);
    }
}
