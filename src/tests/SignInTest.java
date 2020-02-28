package tests;

import classes.SignIn;
import framework.TestSetup;
import framework.WebPage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import webobjects.MainApplicationPage;
import webobjects.SignIn.EnterEmailPage;

public class SignInTest extends TestSetup {
    private WebPage nextPage;

    @Test (testName = "Happy path using test data")
    @Parameters({"login_email", "login_account"})
    public void TestSignIn01(String login_email, String login_account) {
        SignIn signIn = new SignIn(webDriver, applicationURL);
        nextPage = signIn.loginNirmataAccount(login_email,login_account);
        Assert.assertTrue(nextPage instanceof MainApplicationPage,"SignIn failed");
//        nextPage.forceLogout(applicationURL);
    }

    @Test (testName = "Verify if user cannot login with empty email")
    public void TestSignIn02() {
        nextPage = new SignIn(webDriver, applicationURL).enterEmail("");
        Assert.assertTrue(nextPage instanceof EnterEmailPage,"Empty email has been accepted!");
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
        nextPage.forceLogout(applicationURL);
    }


}
