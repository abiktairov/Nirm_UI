package tests;

import framework.NirmataMailer;
import framework.TestSetup;
import framework.WebPage;
import org.testng.Assert;
import org.testng.annotations.*;
import webobjects.SignIn.*;

import java.util.Date;

public class SignInTestAll extends TestSetup {
    private WebPage nextPage; // = signIn.getNextPage();
    private Date timeStamp; // = new Date();

    @BeforeClass
    public void beforeClass() {
    }

    @BeforeMethod
    public void beforeMethod() {
        timeStamp = new Date();
        nextPage = runApplication();
    }

    @AfterMethod
    public void aftereMethod() {
        nextPage = forceLogout();
        deleteAllCookies();
    }


    @Test (alwaysRun = true, testName = "Verify if user cannot login with empty, unregistered or wrong-format email and error message appears")
    @Parameters({"not_registered_user", "wrong_format_email"})
    public void test101(String not_registered_user, String wrong_format_email) {
        nextPage = ((EnterEmailPage) nextPage).enterEmail("").clickSignInBtn();
        Assert.assertTrue(nextPage instanceof EnterEmailPage, "Empty email has been accepted!");
        Assert.assertFalse(((EnterEmailPage) nextPage).getErrorMessage().isEmpty(), "There's no error message!");
        Assert.assertTrue(((EnterEmailPage) nextPage).getErrorMessage().contains("Please enter a value for this field"), "Error message text is not correct!");

        nextPage = ((EnterEmailPage) nextPage).enterEmail(not_registered_user).clickSignInBtn();
        Assert.assertTrue(nextPage instanceof EnterEmailPage,"Not registered email has been accepted!");
        Assert.assertFalse(((EnterEmailPage) nextPage).getErrorMessage().isEmpty(),"There's no error message!");
        Assert.assertTrue(((EnterEmailPage) nextPage).getErrorMessage().contains("We could not find an account with that email."),"Error message text is not correct!");

        nextPage = ((EnterEmailPage) nextPage).enterEmail(wrong_format_email).clickSignInBtn();
        Assert.assertTrue(nextPage instanceof EnterEmailPage,"Wrong formatted email has been accepted!");
        Assert.assertFalse(((EnterEmailPage) nextPage).getErrorMessage().isEmpty(),"There's no error message!");
        Assert.assertTrue(((EnterEmailPage) nextPage).getErrorMessage().contains("Invalid email address"),"Error message text is not correct!");
}

    @Test (alwaysRun = true, testName = "Verify if 'Remember me' checkbox is worked")
    @Parameters({"single_account_user"})
    public void test102(String single_account_user) {
        nextPage = ((EnterEmailPage) nextPage).enterEmail(single_account_user).clickRememberMe().clickSignInBtn();
        Assert.assertFalse(nextPage instanceof EnterEmailPage, "Email has not been accepted, test is stopped.");
        nextPage = forceLogout();
        Assert.assertEquals(single_account_user, ((EnterEmailPage) nextPage).getEmail(), "Email has not been remembered.");
    }

    @Test (alwaysRun = true, testName = "Verify if user can use 'Sign Up for Nirmata account' link")
    public void test103() {
        nextPage = ((EnterEmailPage) nextPage).clickSignUpLink();
        Assert.assertFalse(nextPage instanceof EnterEmailPage, "'Sign up for Nirmata account' link does not work!");
        Assert.assertTrue(nextPage instanceof SignUpPage, "'Sign up for Nirmata account' link does not lead to Sign Up web page!");
    }

    @Test (alwaysRun = true, testName = "Verify if user cannot login with invalid verification code")
    @Parameters({"multiple_accounts_user", "fake_verification_code"})
    public void test201(String multiple_accounts_user, String fake_verification_code) {
        nextPage = ((EnterEmailPage) nextPage).enterEmail(multiple_accounts_user).clickSignInBtn();
        nextPage = ((VerifyIdentityPage) nextPage).enterVerificationCode(fake_verification_code).clickSignInBtn();
        Assert.assertTrue(nextPage instanceof VerifyIdentityPage,"Fake verification code has been accepted!");
    }

    @Test (alwaysRun = true, testName = "Verify if user can resend verification code after first attempt failed")
    @Parameters({"multiple_accounts_user", "fake_verification_code"})
    public void test202(String multiple_accounts_user, String fake_verification_code) {
        nextPage = ((EnterEmailPage) nextPage).enterEmail(multiple_accounts_user).clickSignInBtn();
        nextPage = ((VerifyIdentityPage) nextPage).enterVerificationCode(fake_verification_code).clickSignInBtn();
        Assert.assertTrue(((VerifyIdentityPage) nextPage).clickResendCodeLink(), "There was no confirmation for sending code!");
        // replace for forced version!
        nextPage = ((VerifyIdentityPage) nextPage).
                enterVerificationCode(new NirmataMailer(multiple_accounts_user).
                        getAccessCode(timeStamp, 60, 5)).clickSignInBtn();
        Assert.assertFalse(nextPage instanceof VerifyIdentityPage,"Resent code has not been accepted!");
    }

    @Test (alwaysRun = true, testName = "Verify if user can start with a different email address from 'Select an Account' page")
    @Parameters({"multiple_accounts_user"})
    public void test301(String multiple_accounts_user) {
        nextPage = assertEmail(multiple_accounts_user);
        if (isVerifyIdentityPage())
            nextPage = assertVerificationCode(new NirmataMailer(multiple_accounts_user).getAccessCode(timeStamp, 60, 5));
        Assert.assertTrue(isSelectAccountPage(),"'Select Account' page has not been loaded', test is stopped.");
        nextPage = ((SelectAccountPage) nextPage).clickUseDifferentEmailLink();
        Assert.assertFalse(isSelectAccountPage(),"'Use a different email address' link does not work!");
        Assert.assertTrue(isEnterEmailPage(),"'Use a different email address' does not lead to 'Sign In to Nirmata' web page!");
    }

    @Test (alwaysRun = true, testName = "Verify if user cannot login with empty or invalid password and error message appears")
    @Parameters({"single_account_user", "wrong_password"})
    public void test401(String single_account_user, String wrong_password) {
        nextPage = assertEmail(single_account_user);
        Assert.assertTrue(isEnterPasswordPage(),"'Enter password' page has not been loaded', test is stopped.");
        nextPage = ((EnterPasswordPage) nextPage).enterPassword("").clickSignInBtn();
        Assert.assertTrue(isEnterPasswordPage(), "Empty password has been accepted!");
        Assert.assertFalse(((EnterPasswordPage) nextPage).getErrorMessage().isEmpty(), "There's no error message!");
        Assert.assertTrue(((EnterPasswordPage) nextPage).getErrorMessage().contains("Invalid password. Please try again."), "Error message text is not correct!");

        nextPage = ((EnterPasswordPage) nextPage).enterPassword(wrong_password).clickSignInBtn();
        Assert.assertTrue(isEnterPasswordPage(), "Inalid password has been accepted!");
        Assert.assertFalse(((EnterPasswordPage) nextPage).getErrorMessage().isEmpty(), "There's no error message!");
        Assert.assertTrue(((EnterPasswordPage) nextPage).getErrorMessage().contains("Invalid password. Please try again."), "Error message text is not correct!");
    }

    @Test (alwaysRun = true, testName = "Verify if user can run password recovering")
    @Parameters({"single_account_user"})
    public void test402(String single_account_user) {
        nextPage = assertEmail(single_account_user);
        Assert.assertTrue(isEnterPasswordPage(),"'Enter password' page has not been loaded', test is stopped.");
        nextPage = ((EnterPasswordPage) nextPage).clickForgotYourPasswordLink();
        Assert.assertFalse(isEnterPasswordPage(),"'Forgot your password' link does not work!");
        Assert.assertTrue(isResetPasswordPage(),"'Forgot your password' does not lead to 'Reset your password' web page!");
    }

    @Test (alwaysRun = true, testName = "Verify if user can recover his password")
    @Parameters({"single_account_user"})
    public void test501(String single_account_user) {
        nextPage = assertEmail(single_account_user);
        Assert.assertTrue(isEnterPasswordPage(),"'Enter password' page has not been loaded', test is stopped.");
        nextPage = ((EnterPasswordPage) nextPage).clickForgotYourPasswordLink();
        Assert.assertTrue(isResetPasswordPage(),"'Reset your password' page has not been loaded', test is stopped.");
        nextPage = ((ResetPasswordPage) nextPage).enterEmail(single_account_user).clickResetPasswordBtn();
        Assert.assertTrue (((ResetPasswordPage) nextPage).isResetConfirmationAppeared(), "Something is wrong - no reset confirmation!");
        String passwordResetLink = new NirmataMailer(single_account_user).getPasswordResetLink(timeStamp, 60, 5);

    }

    private boolean isEnterEmailPage() {
        return (nextPage instanceof EnterEmailPage);
    }

    private boolean isVerifyIdentityPage() {
        return (nextPage instanceof VerifyIdentityPage);
    }

    private boolean isSelectAccountPage() {
        return (nextPage instanceof SelectAccountPage);
    }

    private boolean isEnterPasswordPage() {
        return (nextPage instanceof EnterPasswordPage);
    }

    private boolean isResetPasswordPage() {
        return (nextPage instanceof ResetPasswordPage);
    }

    private WebPage assertEmail (String email) {
        nextPage = ((EnterEmailPage) nextPage).enterEmail(email).clickSignInBtn();
        Assert.assertFalse(isEnterEmailPage(), "Email has not been accepted, test is stopped.");
        return nextPage;
    }

    private WebPage assertVerificationCode (String code) {
        nextPage = ((VerifyIdentityPage) nextPage).enterVerificationCode(code).clickSignInBtn();
        Assert.assertFalse(isVerifyIdentityPage(), "Verification code has not been accepted, test is stopped.");
        return nextPage;
    }

    private WebPage assertPassword (String password) {
        nextPage = ((EnterPasswordPage) nextPage).enterPassword(password).clickSignInBtn();
        Assert.assertFalse(isEnterPasswordPage(), "Verification code has not been accepted, test is stopped.");
        return nextPage;
    }

}
