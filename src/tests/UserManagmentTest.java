package tests;

import framework.TestSetup;
import framework.WebPage;

public class UserManagmentTest extends TestSetup {
    private WebPage nextPage;

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
