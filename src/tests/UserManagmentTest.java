package tests;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.junit.Assume.assumeTrue;
import static org.testng.Assert.*;

import com.jayway.jsonpath.JsonPath;
import framework.NirmataMailer;
import framework.TestData;
import io.restassured.response.Response;

import framework.TestSetup;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import webobjects.SignIn.EnterEmailPage;
import webobjects.SignIn.ResetPasswordPage;
import webobjects.SignIn.SetPasswordPage;
import webobjects.SignIn.SignUpPage;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserManagmentTest extends TestSetup {
    private Date timeStamp;
    private String multiple_accounts_user;
    private String user_account;
    private String new_tenant_user;

    @BeforeClass
    public void beforeClass() { nextPage = forceLogout(); }

    @BeforeMethod
    public void beforeMethod() { timeStamp = new Date(); }

    @Parameters({"multiple_accounts_user", "user_account", "new_tenant_user"})
    public UserManagmentTest(String multiple_accounts_user, String user_account, String new_tenant_user) {
        this.multiple_accounts_user = multiple_accounts_user;
        this.user_account = user_account;
        this.new_tenant_user = new_tenant_user;
    }

    @Test(alwaysRun = true, description = "Verify if the Test can be run in this environment.")
    public void ifTestCanBeRun() {
        // this test class limited for execution in devtest2 environment only. Remove it when you are ready.
        if (!applicationURL.contains("devtest2"))
            throw new SkipException("The test class is skipped because it's not a devtest2 environment");
    }

    @Test(dependsOnMethods = {"ifTestCanBeRun"}, description = "Verify if new Tenant account can be created.")
    public void ifSignUp() {
        nextPage = runApplication();
        nextPage = ((EnterEmailPage) nextPage)
                .clickSignUpLink()
                .assertThat();
        nextPage = ((SignUpPage) nextPage)
                .enterName(TestData.getUser("user_name", new_tenant_user))
                .enterBusinessEmail(new_tenant_user)
                .enterCompanyName(TestData.getUser("company_name", new_tenant_user))
                .enterPhoneNumber(TestData.getUser("user_phone", new_tenant_user))
                .clickSignUpBtn()
                .clickAcceptAndProceed()
                .assertThat(true, "New tenant account has not been created!");
    }

    @Test (dependsOnMethods = {"ifSignUp"}, description = "Verify if new tenant account can be activated.")
    public void ifAccountActivated() {
        String activationLink = new NirmataMailer(new_tenant_user).getActivationLink(applicationURL, timeStamp, 60, 5);
        assertFalse(activationLink.isEmpty(), "Password reset link is invalid!");
        webDriver.get(activationLink);
        nextPage = ((SetPasswordPage) nextPage)
                .enterPassword(TestData.getUser("user_password", new_tenant_user))
                .enterPasswordConfirmation(TestData.getUser("user_password", new_tenant_user))
                .clicResetPasswordBtn().assertPasswordConfirmation(true, "Confirmation message has not appeared.")
                .clicSignInBtn()
                .assertThat();
    }

    @Test (dependsOnMethods = {"ifAccountActivated"}, description = "Verify if new tenant can sign in.")
    public void ifSigninNewTenant() {
        nextPage = signInNirmata(multiple_accounts_user, user_account, TestData.getUser("user_password", new_tenant_user));
        nextPage = forceLogout();
    }

    @Test(dependsOnMethods = {"ifSigninNewTenant"}, description = "Verify if the Tenant can be deleted using API call.")
    public void ifTenantCanBeDeleted() {
        Response response;
        String url_get_tenants = applicationURL + "users/api/Tenant";
        String url_post_delete = applicationURL + "security/api/accounts/delete";

        // we need to get sysadmin cookies to continue working with API
        nextPage = signInNirmata(multiple_accounts_user, user_account, TestData.getUser("user_password", multiple_accounts_user));
        //    private WebPage nextPage;
        String cookie = nextPage.getHttpCookies();

         // get tenants list
        response = given().header("Cookie", cookie).get(url_get_tenants);
        assertEquals(response.getStatusCode(), 200, "Getting of tenants list is failed.");

        // locate aimed tenant
        String output = response.getBody().print();
        String json_pattern = "$.[?(@.ownerEmail=='" + new_tenant_user + "')]['id']";
        List<String> tenantMap = JsonPath.read(output, json_pattern);
        assertEquals(tenantMap.size(), 1, "Cannot retrieve unique tenant id for " + new_tenant_user + ". Test stopped.");
        String tenantId = tenantMap.get(0);

        // delete the tenant
        String body = "{\"id\": \"" + tenantId + "\",\"forceDelete\": true}";
        response = given().header("Cookie", cookie).body(body).post(url_post_delete);
        assertEquals(response.getStatusCode(), 200, "Tenant has not been deleted, API call status code is - " + response.getStatusCode());
    }






//    @Test(alwaysRun = true, description = "Get list of all tenants with IDs")
//    @Parameters({"multiple_accounts_user"})
//    public void test102(String multiple_accounts_user) {
//        String uri = "https://devtest2.nirmata.io/users/api/Tenant";
//        String email = "";
//        String password = "";
//        String api_key = "";
//
//        Response response = given().header("Authorization", api_key).get(uri);
//        String output = response.getBody().print();
//
//        List<Map> result = JsonPath.read(output, "$[*]['id','ownerEmail','createdOn']");
//
//        int j = 0;
//        for (int i=0; i < result.size(); i++) {
//            if (result.get(i).get("ownerEmail").toString().contains("@mail.ru") &&
//            new Date (Long.parseLong(result.get(i).get("createdOn").toString())).getTime() > new Date(new Date().getTime()-24*60*60*1000).getTime()) {
//                deleteTenant(result.get(i).get("id").toString(), api_key);
//            }
//        }
//
////        JsonPath.read(output, "$.[?(@.ownerEmail=='and14eve20200303051913@mail.ru')]['id','ownerEmail']");

//    }

//    private void deleteTenant(String id, String api_key) {
//        String uri = "https://devtest2.nirmata.io/security/api/accounts/delete";
//        String email = "";
//        String password = "";
//        String cookie0 = "";
//
//        String body = "{\"id\": \"" + id + "\",\"forceDelete\": true}";
//
//        Response response = given().contentType("application/json").accept("application/json").header("Cookie", cookie0).body(body).post(uri);
//
//    }

//    @Test
//    @Parameters({"login_email", "login_account"})
//    public void TestSignIn(String login_email, String login_account) {
//        SignIn signIn = new SignIn(webDriver, applicationURL);
//        nextPage = signIn.loginNirmataAccount(login_email, login_account);
//        Assert.assertTrue(nextPage.isClass("MainAppSuper"),"SignIn failed");
//    }
//
//    @Test (dependsOnMethods = {"TestSignIn"})
//    public void AccessAppMainPage() {
//        // check all version of starter page (before main application page)
//        if (nextPage.isClass("GetStartedPage")) {
//            ((GetStartedPage) nextPage).isLoaded();
//            ((GetStartedPage) nextPage).skipSetup();
//            nextPage = ((GetStartedPage) nextPage).nextWebPage();
//        }
////        else if () {}
//        Assert.assertTrue(nextPage.isClass("MainApplicationPage"));
//    }
//
//    @Test (dependsOnMethods = {"AccessAppMainPage"})
//    @Parameters({"adduser_email"})
//    public void AddUser(String adduser_email) {
//        nextPage = ((MainMenuPage) nextPage).selectMenuItem("Identity & Access");
//        Assert.assertTrue(nextPage.isClass("Users"));
//    }
}
