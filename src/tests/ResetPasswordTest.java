package tests;

import classes.SignIn;
import framework.NirmataMailer;
import framework.TestData;
import framework.TestSetup;
import org.testng.Assert;
import org.testng.annotations.*;
import webobjects.SignIn.*;

import java.util.Date;

public class ResetPasswordTest extends TestSetup {
//    private WebPage nextPage;
    private Date timeStamp;
    private String passwordResetLink = "";
    private String single_account_user;
    private String multiple_accounts_user;
    private String user_account;

    @BeforeClass
    public void beforeClass() {
        nextPage = forceLogout();
    }

    @BeforeMethod
    public void beforeMethod() {
        timeStamp = new Date();
    }

    @Parameters({"single_account_user", "multiple_accounts_user", "user_account"})
    public ResetPasswordTest(String single_account_user, String multiple_accounts_user, String user_account) {
        this.single_account_user = single_account_user;
        this.multiple_accounts_user = multiple_accounts_user;
        this.user_account = user_account;
    }

    @Test (alwaysRun = true, description = "Verify if 'Reset your password' page' is accessible.")
    public void ifResetPageAccessable() {
        nextPage = runApplication();
        nextPage = ((EnterEmailPage) nextPage)
                .enterEmail(single_account_user)
                .clickSignInBtn()
                .assertThat();
        nextPage = ((EnterPasswordPage) nextPage)
                .clickForgotYourPasswordLink()
                .assertThat();
    }

    @Test (dependsOnMethods = {"ifResetPageAccessable"}, description = "Verify if link to reset password can be obtained.")
    public void ifLinkAvailable() {
        nextPage = ((ResetPasswordPage) nextPage)
                .enterEmail(multiple_accounts_user)
                .clickResetPasswordBtn()
                .assertThat();
        passwordResetLink = new NirmataMailer(multiple_accounts_user).getPasswordResetLink(timeStamp, 60, 5);
        Assert.assertFalse(passwordResetLink.isEmpty(), "Password reset link is invalid!");
    }

    @Test (dependsOnMethods = {"ifLinkAvailable"}, description = "Verify if user can select account to reset password.")
    public void ifSelectAccount() {
        webDriver.get(passwordResetLink);
        nextPage = new SelectAccountResetPage(webDriver);
        nextPage = ((SelectAccountResetPage) nextPage)
                .selectAccount(user_account)
                .assertThat();
    }

    @Test (dependsOnMethods = {"ifSelectAccount"}, description = "Verify if user cannot set empty password.")
    public void ifSetEmptyPassword() {
        nextPage = ((SetPasswordPage) nextPage)
                .enterPassword("")
                .enterPasswordConfirmation("")
                .clicResetPasswordBtn()
                .assertPasswordConfirmation(false, "Empty password has been accepted!");
        Assert.assertFalse(((SetPasswordPage) nextPage).getPasswordErrorMessage().isEmpty(), "There's no error message for empty password!");
        Assert.assertFalse(((SetPasswordPage) nextPage).getConfirmationErrorMessage().isEmpty(), "There's no error message for empty confirmation!");
        Assert.assertTrue(((SetPasswordPage) nextPage).getPasswordErrorMessage().contains("Please enter a value for this field"), "Error message text is not correct!");
        Assert.assertTrue(((SetPasswordPage) nextPage).getConfirmationErrorMessage().contains("Please enter a value for this field"), "Error message text is not correct!");
    }

    @Test (dependsOnMethods = {"ifSelectAccount"}, description = "Verify if user cannot set valid password with invalid confirmation.")
    public void ifPasswordNotMatch() {
        String newValidPassword = TestData.getUser("user_password", multiple_accounts_user) + "qqq";
        String newInvalidPassword = TestData.getUser("user_password", multiple_accounts_user) + "aaa";
        nextPage = ((SetPasswordPage) nextPage)
                .enterPassword(newValidPassword)
                .enterPasswordConfirmation(newInvalidPassword)
                .clicResetPasswordBtn()
                .assertPasswordConfirmation(false, "Password not matched with confirmation has been accepted!");
        Assert.assertFalse(((SetPasswordPage) nextPage).getConfirmationErrorMessage().isEmpty(), "There's no error message for unmatched passwords!");
        Assert.assertTrue(((SetPasswordPage) nextPage).getConfirmationErrorMessage().contains("The passwords do not match"), "Error message text is not correct!");
    }

    @Test (dependsOnMethods = {"ifSelectAccount"}, description = "Verify ...   any other combinations of passwords ...")
    public void ifSomethingElse() {
        // place for additional password validations
    }

    @Test (dependsOnMethods = {"ifSelectAccount"}, description = "Verify if user can reset password with valid password / confirmation combination.")
    public void ifReserSuccess() {
        String newValidPassword = TestData.getUser("user_password", multiple_accounts_user) + "qqq";
        nextPage = ((SetPasswordPage) nextPage)
                .enterPassword(newValidPassword)
                .enterPasswordConfirmation(newValidPassword)
                .clicResetPasswordBtn().assertPasswordConfirmation(true, "Confirmation message has not appeared.")
                .clicSignInBtn()
                .assertThat();
    }

    @Test (dependsOnMethods = {"ifReserSuccess"}, description = "Verify if user can sign in using new password.")
    public void ifSigninNewPassword() {
        String newValidPassword = TestData.getUser("user_password", multiple_accounts_user) + "qqq";
//        nextPage = runApplication();
        nextPage = new SignIn(webDriver, applicationURL).loginNirmataAccount(multiple_accounts_user, user_account, newValidPassword);
//        Assert.assertTrue(isMainApplicationPage(),"Sign in with new password has been failed!");
    }

    @Test (dependsOnMethods = {"ifSigninNewPassword"}, description = "Set password back to default.")
    public void ifSetPasswordBack() {
        nextPage = forceLogout();
        nextPage = ((EnterEmailPage) nextPage)
                .enterEmail(multiple_accounts_user)
                .clickSignInBtn()
                .assertThat();
        nextPage = ((EnterPasswordPage) nextPage)
                .clickForgotYourPasswordLink()
                .assertThat();
        nextPage = ((ResetPasswordPage) nextPage)
                .enterEmail(multiple_accounts_user)
                .clickResetPasswordBtn()
                .assertThat();
        passwordResetLink = new NirmataMailer(multiple_accounts_user).getPasswordResetLink(timeStamp, 60, 5);
        Assert.assertFalse(passwordResetLink.isEmpty(), "Password reset link is invalid!");
        webDriver.get(passwordResetLink);
        nextPage = new SelectAccountResetPage(webDriver);
        nextPage = ((SelectAccountResetPage) nextPage)
                .selectAccount(user_account)
                .assertThat();
        String newValidPassword = TestData.getUser("user_password", multiple_accounts_user);
        nextPage = ((SetPasswordPage) nextPage)
                .enterPassword(newValidPassword)
                .enterPasswordConfirmation(newValidPassword)
                .clicResetPasswordBtn()
                .assertPasswordConfirmation(true, "Confirmation message has not appeared.");
    }
}
