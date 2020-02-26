package tests;

import classes.SignIn;
import framework.TestSetup;
import framework.WebPage;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import webobjects.MainApplicationPage;

public class SignInTest extends TestSetup {
    private WebPage nextPage;

    @Test (testName = "Happy path using test data")
    @Parameters({"login_email", "login_account"})
    public void TestSignIn(String login_email, String login_account) {

        nextPage = new SignIn(webDriver, applicationURL).loginNirmataAccount(login_email,login_account);
        Assert.assertTrue(nextPage instanceof MainApplicationPage,"SignIn failed");
    }

//    @Test (testName = "Verify if ")
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
//        nextPage = ((MainApplicationPage) nextPage).selectMenuItem("Identity & Access");
//        Assert.assertTrue(nextPage.isClass("Users"));
//    }


}
