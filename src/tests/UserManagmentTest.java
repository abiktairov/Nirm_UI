package tests;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

import classes.SignIn;
import framework.NirmataMailer;
import framework.TestData;
import io.restassured.response.Response;

import framework.TestSetup;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Date;

public class UserManagmentTest extends TestSetup {
//    private WebPage nextPage;
    private Date timeStamp;
    private String multiple_accounts_user;
    private String user_account;

    @BeforeClass
    public void beforeClass() {
        nextPage = forceLogout();
    }

    @BeforeMethod
    public void beforeMethod() { timeStamp = new Date(); }

    @Parameters({"multiple_accounts_user", "user_account"})
    public UserManagmentTest(String multiple_accounts_user, String user_account) {
        this.multiple_accounts_user = multiple_accounts_user;
        this.user_account = user_account;
    }

    @Test(alwaysRun = true, description = "Verify if the user can sign in as Nirmata Administrator")
    public void test101() {
        nextPage = new SignIn(webDriver, applicationURL)
                .loginNirmataAccount(multiple_accounts_user, user_account, TestData.getUser("user_password", multiple_accounts_user));

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
