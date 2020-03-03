package tests;

import classes.SignIn;
import framework.TestSetup;
import framework.WebPage;
import org.testng.Assert;
import org.testng.annotations.*;
import webobjects.MainApplicationPage;
import webobjects.SignIn.EnterEmailPage;
import webobjects.SignIn.SelectAccountPage;
import webobjects.SignIn.SignUpPage;
import webobjects.SignIn.VerifyIdentityPage;

import java.util.Date;

public class SignInTest extends TestSetup {
    private WebPage nextPage;
    private SignIn signIn;
    private Date testStartTime;

    @BeforeMethod
    public void StartSignIn() {
        signIn = new SignIn(webDriver, applicationURL);
        nextPage = signIn.getNextPage();
        testStartTime = new Date();
    }

    @Test (testName = "Happy path using test data, user with multiple accounts")
    @Parameters({"multiple_accounts_user", "user_account"})
    public void TestSignIn001(String multiple_accounts_user, String user_account) {
        nextPage = signIn.loginNirmataAccount(multiple_accounts_user,user_account);
        Assert.assertTrue(nextPage instanceof MainApplicationPage,"SignIn failed");
    }

    @Test (testName = "Happy path using test data, user with single account")
    @Parameters({"single_account_user"})
    public void TestSignIn002(String single_account_user) {
        nextPage = signIn.loginNirmataAccount(single_account_user,"");
        Assert.assertTrue(nextPage instanceof MainApplicationPage,"SignIn failed");
    }

//
//    @Test (dependsOnMethods = {"AccessAppMainPage"})
//    @Parameters({"adduser_email"})
//    public void AddUser(String adduser_email) {
//        nextPage = ((MainApplicationPage) nextPage).selectMenuItem("Identity & Access");
//        Assert.assertTrue(nextPage.isClass("Users"));
//    }

    @AfterMethod
    public void ApplicationLogout() {
        forceLogout();
    }


}
