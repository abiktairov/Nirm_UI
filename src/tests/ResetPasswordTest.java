package tests;

import classes.SignIn;
import framework.NirmataMailer;
import framework.TestData;
import framework.TestSetup;
import framework.WebPage;
import org.testng.Assert;
import org.testng.annotations.*;
import webobjects.MainApplicationPage;
import webobjects.SignIn.*;

import java.util.Date;

public class ResetPasswordTest extends TestSetup {
    private WebPage nextPage; // = signIn.getNextPage();
    private Date timeStamp; // = new Date();
    private String passwordResetLink = "";

    @BeforeClass
    public void beforeClass() {
    }

    @BeforeMethod
    public void beforeMethod() {
        timeStamp = new Date();
//        nextPage = runApplication();
    }

    @AfterMethod
    public void aftereMethod() {
//        nextPage = forceLogout();
//        deleteAllCookies();
    }

    @Test (alwaysRun = true, testName = "Verify if 'Reset your password' page' is accessible")
    @Parameters({"single_account_user"})
    public void test101(String single_account_user) {
        nextPage = runApplication();
        nextPage = assertEmail(single_account_user);
        Assert.assertTrue(isEnterPasswordPage(),"'Enter password' page has not been loaded', test is stopped.");
        nextPage = ((EnterPasswordPage) nextPage).clickForgotYourPasswordLink();
        Assert.assertTrue(isResetPasswordPage(),"'Reset your password' page has not been loaded', test is stopped.");
    }

    @Test (dependsOnMethods = {"test101"}, testName = "Verify if link to reset password can be obtained")
    @Parameters({"multiple_accounts_user"})
    public void test102(String multiple_accounts_user) {
        nextPage = ((ResetPasswordPage) nextPage).enterEmail(multiple_accounts_user).clickResetPasswordBtn();
        Assert.assertTrue (((ResetPasswordPage) nextPage).isResetConfirmationAppeared(), "Something is wrong - no reset confirmation!");
        passwordResetLink = new NirmataMailer(multiple_accounts_user).getPasswordResetLink(timeStamp, 60, 5);
        Assert.assertFalse(passwordResetLink.isEmpty(), "Password reset link is invalid!");
    }

    @Test (dependsOnMethods = {"test102"}, testName = "Verify if user can select account to reset password")
    @Parameters({"multiple_accounts_user", "user_account"})
    public void test103(String multiple_accounts_user, String user_account) {
        webDriver.get(passwordResetLink);
        nextPage = new SelectAccountResetPage(webDriver);
        Assert.assertTrue(isSelectAccountResetPage(),"'Select Account for Reset' page has not been loaded for multi-account user!");
        nextPage = ((SelectAccountResetPage) nextPage).selectAccount(user_account);
        Assert.assertTrue(isSetPasswordPage(),"'Set new password' page has not been loaded for multi-account user!");
    }

    @Test (dependsOnMethods = {"test103"}, testName = "Verify if user cannot set empty password")
    public void test201() {
        nextPage = ((SetPasswordPage) nextPage).enterPassword("").enterPasswordConfirmation("").clicResetPasswordBtn();
        Assert.assertFalse(((SetPasswordPage) nextPage).isSetPasswordConfirmationAppeared(), "Empty password has been accepted!");
        Assert.assertFalse(((SetPasswordPage) nextPage).getPasswordErrorMessage().isEmpty(), "There's no error message for empty password!");
        Assert.assertFalse(((SetPasswordPage) nextPage).getConfirmationErrorMessage().isEmpty(), "There's no error message for empty confirmation!");
        Assert.assertTrue(((SetPasswordPage) nextPage).getPasswordErrorMessage().contains("Please enter a value for this field"), "Error message text is not correct!");
        Assert.assertTrue(((SetPasswordPage) nextPage).getConfirmationErrorMessage().contains("Please enter a value for this field"), "Error message text is not correct!");
    }

    @Test (dependsOnMethods = {"test103"}, testName = "Verify if user cannot set valid password with invalid confirmation")
    @Parameters({"multiple_accounts_user"})
    public void test202(String multiple_accounts_user) {
        String newValidPassword = TestData.getUser("user_password", multiple_accounts_user) + "qqq";
        String newInvalidPassword = TestData.getUser("user_password", multiple_accounts_user) + "aaa";
        nextPage = ((SetPasswordPage) nextPage).enterPassword(newValidPassword).enterPasswordConfirmation(newInvalidPassword).clicResetPasswordBtn();
        Assert.assertFalse(((SetPasswordPage) nextPage).isSetPasswordConfirmationAppeared(), "Password with invalid confirmation has been accepted!");
        Assert.assertFalse(((SetPasswordPage) nextPage).getConfirmationErrorMessage().isEmpty(), "There's no error message for unmatched passwords!");
        Assert.assertTrue(((SetPasswordPage) nextPage).getConfirmationErrorMessage().contains("The passwords do not match"), "Error message text is not correct!");
    }

    @Test (dependsOnMethods = {"test103"}, testName = "Verify ...   any other combinations of passwords ...")
    @Parameters({"multiple_accounts_user"})
    public void test203(String multiple_accounts_user) {
        // place for additional password validations
    }

    @Test (dependsOnMethods = {"test103"}, testName = "Verify if user can reset password with valid password / confirmation combination")
    @Parameters({"multiple_accounts_user"})
    public void test301(String multiple_accounts_user) {
        String newValidPassword = TestData.getUser("user_password", multiple_accounts_user) + "qqq";
        nextPage = ((SetPasswordPage) nextPage).enterPassword(newValidPassword).enterPasswordConfirmation(newValidPassword).clicResetPasswordBtn();
        Assert.assertTrue(((SetPasswordPage) nextPage).isSetPasswordConfirmationAppeared(), "Valid password / confirmation combination has not been accepted!");
        nextPage = ((SetPasswordPage) nextPage).clicSignInBtn();
        Assert.assertTrue(isEnterEmailPage(),"'Sign in to Nirmata' button doesn't lead to login page!");
    }

    @Test (dependsOnMethods = {"test301"}, testName = "Verify if user can sign in using new password")
    @Parameters({"multiple_accounts_user", "user_account"})
    public void test302(String multiple_accounts_user, String user_account) {
        String newValidPassword = TestData.getUser("user_password", multiple_accounts_user) + "qqq";
        nextPage = isEnterEmailPage() ? nextPage : runApplication();
        nextPage = new SignIn(webDriver, applicationURL).loginNirmataAccount(multiple_accounts_user, user_account, newValidPassword);
        Assert.assertTrue(isMainApplicationPage(),"Sign in with new password has been failed!");
    }

    @Test (dependsOnMethods = {"test302"}, testName = "Set password back to default")
    @Parameters({"single_account_user", "multiple_accounts_user", "user_account"})
    public void test303(String single_account_user, String multiple_accounts_user, String user_account) {
        nextPage = forceLogout();
        nextPage = assertEmail(single_account_user);
        Assert.assertTrue(isEnterPasswordPage(),"'Enter password' page has not been loaded', test is stopped.");
        nextPage = ((EnterPasswordPage) nextPage).clickForgotYourPasswordLink();
        Assert.assertTrue(isResetPasswordPage(),"'Reset your password' page has not been loaded', test is stopped.");
        nextPage = ((ResetPasswordPage) nextPage).enterEmail(multiple_accounts_user).clickResetPasswordBtn();
        Assert.assertTrue (((ResetPasswordPage) nextPage).isResetConfirmationAppeared(), "Something is wrong - no reset confirmation!");
        passwordResetLink = new NirmataMailer(multiple_accounts_user).getPasswordResetLink(timeStamp, 60, 5);
        Assert.assertFalse(passwordResetLink.isEmpty(), "Password reset link is invalid!");

        webDriver.get(passwordResetLink);
        nextPage = new SelectAccountResetPage(webDriver);
        Assert.assertTrue(isSelectAccountResetPage(),"'Select Account for Reset' page has not been loaded for multi-account user!");
        nextPage = ((SelectAccountResetPage) nextPage).selectAccount(user_account);
        Assert.assertTrue(isSetPasswordPage(),"'Set new password' page has not been loaded for multi-account user!");

        String newValidPassword = TestData.getUser("user_password", multiple_accounts_user);
        nextPage = ((SetPasswordPage) nextPage).enterPassword(newValidPassword).enterPasswordConfirmation(newValidPassword).clicResetPasswordBtn();
        Assert.assertTrue(((SetPasswordPage) nextPage).isSetPasswordConfirmationAppeared(), "Valid password / confirmation combination has not been accepted!");
        nextPage = ((SetPasswordPage) nextPage).clicSignInBtn();
        Assert.assertTrue(isEnterEmailPage(),"'Sign in to Nirmata' button doesn't lead to login page!");

        nextPage = isEnterEmailPage() ? nextPage : runApplication();
        nextPage = new SignIn(webDriver, applicationURL).loginNirmataAccount(multiple_accounts_user, user_account);
        Assert.assertTrue(isMainApplicationPage(),"Sign in with restored password has been failed!");
    }


    private boolean isMainApplicationPage() {
        return (nextPage instanceof MainApplicationPage);
    }

    private boolean isEnterEmailPage() {
        return (nextPage instanceof EnterEmailPage);
    }

    private boolean isEnterPasswordPage() {
        return (nextPage instanceof EnterPasswordPage);
    }

    private boolean isResetPasswordPage() {
        return (nextPage instanceof ResetPasswordPage);
    }

    private boolean isSelectAccountResetPage() {
        return (nextPage instanceof SelectAccountResetPage);
    }

    private boolean isSetPasswordPage() {
        return (nextPage instanceof SetPasswordPage);
    }

    private WebPage assertEmail (String email) {
        nextPage = ((EnterEmailPage) nextPage).enterEmail(email).clickSignInBtn();
        Assert.assertFalse(isEnterEmailPage(), "Email has not been accepted, test is stopped.");
        return nextPage;
    }

}
