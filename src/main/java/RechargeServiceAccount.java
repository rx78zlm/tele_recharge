import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author zhangleimin
 * @package PACKAGE_NAME
 * @date 16-9-25
 */
public class RechargeServiceAccount {

    public static void main(String[] args) {
        String url = "http://service.sh.189.cn/service/biz/pay/fenZhang";
        try {
            final WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setRedirectEnabled(true);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.setJavaScriptTimeout(30000);
            HtmlPage chargePage = webClient.getPage(url);
            HtmlInput account = chargePage.getHtmlElementById("phone"); // 分账序号
            HtmlDivision payMethodDiv = chargePage.getHtmlElementById("1188");  // 电信充值卡
            HtmlDivision buttonCharge = chargePage.querySelector(".btn.f18.btn_long");
            account.setValueAttribute("66863524994");
            payMethodDiv.click();
            HtmlPage payPage = buttonCharge.click();
            HtmlInput cardPwd = payPage.getHtmlElementById("1188_pwd"); // 电信充值卡密码
            cardPwd.setValueAttribute("210514848848975853");
            HtmlDivision payDiv = payPage.querySelector(".btn.f18.btn_long.mt20");
            HtmlPage resultPage = payDiv.click();
            HtmlDivision resultDiv = resultPage.querySelector(".i_content");
            System.out.println(resultDiv.asText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
