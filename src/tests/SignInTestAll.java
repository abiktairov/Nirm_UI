package tests;

import framework.NirmataMailer;
import framework.TestData;
import framework.TestSetup;
import org.testng.Assert;
import org.testng.annotations.*;
import webobjects.MainApplicationPage;
import webobjects.SignIn.*;

import java.util.Date;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class SignInTestAll extends TestSetup {
//    private WebPage nextPage; // = signIn.getNextPage();
    private Date timeStamp;
    private String not_registered_user;
    private String wrong_format_email;
    private String single_account_user;
    private String multiple_accounts_user;
    private String fake_verification_code;
    private String wrong_password;
    private String user_account;

    @BeforeClass
    public void beforeClass() {
        nextPage = forceLogout();
    }

    @BeforeMethod
    public void beforeMethod() {
        timeStamp = new Date();
    }

    @Parameters({"not_registered_user", "wrong_format_email", "single_account_user", "multiple_accounts_user", "fake_verification_code", "wrong_password", "user_account"})
    public SignInTestAll(String not_registered_user, String wrong_format_email, String single_account_user, String multiple_accounts_user, String fake_verification_code, String wrong_password, String user_account) {
        this.not_registered_user = not_registered_user;
        this.wrong_format_email = wrong_format_email;
        this.single_account_user = single_account_user;
        this.multiple_accounts_user = multiple_accounts_user;
        this.fake_verification_code = fake_verification_code;
        this.wrong_password = wrong_password;
        this.user_account = user_account;
    }

    @Test (alwaysRun = true, description = "Verify if empty email is not accepted and error message appears.")
    public void ifEmptyEmail() {
        nextPage = runApplication();
        nextPage = ((EnterEmailPage) nextPage).enterEmail("")
                .clickSignInBtn()
                .assertThat(false, "Empty email has been accepted!");
        assertFalse(((EnterEmailPage) nextPage).getErrorMessage().isEmpty(), "There's no error message!");
        assertTrue(((EnterEmailPage) nextPage).getErrorMessage().contains("Please enter a value for this field"), "Error message text is not correct!");
    }

    @Test (alwaysRun = true, description = "Verify if unregistered email is not accepted and error message appears.")
    public void ifUnregisteredEmail() {
        nextPage = runApplication();
        nextPage = ((EnterEmailPage) nextPage)
                .enterEmail(not_registered_user)
                .clickSignInBtn()
                .assertThat(false, "Unregistered user email has been accepted!");
        assertFalse(((EnterEmailPage) nextPage).getErrorMessage().isEmpty(), "There's no error message!");
        assertTrue(((EnterEmailPage) nextPage).getErrorMessage().contains("We could not find an account with that email."), "Error message text is not correct!");
    }

    @Test (alwaysRun = true, description = "Verify if invalid email is not accepted and error message appears.")
    public void ifInvalidEmail() {
        nextPage = runApplication();
        nextPage = ((EnterEmailPage) nextPage)
                .enterEmail(wrong_format_email)
                .clickSignInBtn()
                .assertThat(false, "Invalid user email has been accepted!");
        assertFalse(((EnterEmailPage) nextPage).getErrorMessage().isEmpty(),"There's no error message!");
        assertTrue(((EnterEmailPage) nextPage).getErrorMessage().contains("Invalid email address"),"Error message text is not correct!");
}

    @Test (alwaysRun = true, description = "Verify if 'Remember me' checkbox is worked and valid email is accepted.")
    public void ifRememberMe() {
        nextPage = runApplication();
        nextPage = ((EnterEmailPage) nextPage)
                .enterEmail(single_account_user)
                .clickRememberMe()
                .clickSignInBtn()
                .assertThat();
        nextPage = forceLogout();
        Assert.assertEquals(single_account_user, ((EnterEmailPage) nextPage).getEmail(), "Email has not been remembered.");
    }

    @Test (alwaysRun = true, description = "Verify if user can use 'Sign Up for Nirmata account' link.")
    public void ifUserCanSignUp() {
        nextPage = runApplication();
        nextPage = ((EnterEmailPage) nextPage)
                .clickSignUpLink()
                .assertThat();
        assertTrue(nextPage instanceof SignUpPage, "'Sign up for Nirmata account' link does not lead to Sign Up web page!");
    }

    @Test (priority = 1, alwaysRun = true, description = "Verify if user cannot login with invalid verification code.")
    public void ifInvalidVerification() {
        nextPage = runApplication();
        nextPage = ((EnterEmailPage) nextPage)
                .enterEmail(multiple_accounts_user)
                .clickSignInBtn()
                .assertThat();
        nextPage = ((VerifyIdentityPage) nextPage)
                .enterVerificationCode(fake_verification_code)
                .clickSignInBtn()
                .assertThat(false, "Fake verification code has been accepted!");
    }

    @Test (priority = 1, dependsOnMethods = {"ifInvalidVerification"}, description = "Verify if user can resend verification code.")
    public void ifResendVerification() {
        assertTrue(((VerifyIdentityPage) nextPage).clickResendCodeLink(), "There was no confirmation for sending code!");
    }

    @Test (priority = 1, dependsOnMethods = {"ifResendVerification"}, description = "Verify if valid verification code is accepted.")
    public void ifVerificationAccepted() {
        nextPage = ((VerifyIdentityPage) nextPage)
                .enterVerificationCode(new NirmataMailer(multiple_accounts_user).getAccessCode(timeStamp, 60, 10))
                .clickSignInBtn()
                .assertThat();
    }

    @Test (priority = 2, alwaysRun = true, description = "Verify if user can start with a different email address from 'Select an Account' page.")
    public void ifUseDifferentEmail() {
        nextPage = runApplication();
        nextPage = ((EnterEmailPage) nextPage)
                .enterEmail(multiple_accounts_user)
                .clickSignInBtn()
                .assertThat();
        if (nextPage instanceof VerifyIdentityPage)
            nextPage = ((VerifyIdentityPage) nextPage)
                    .enterVerificationCode(new NirmataMailer(multiple_accounts_user).getAccessCode(timeStamp, 60, 10))
                    .clickSignInBtn()
                    .assertThat();
        nextPage = ((SelectAccountPage) nextPage)
                .clickUseDifferentEmailLink()
                .assertThat();
        assertTrue(nextPage instanceof EnterEmailPage,"'Use a different email address' does not lead to 'Sign In to Nirmata' web page!");
    }

    @Test (priority = 2, alwaysRun = true, description = "Verify if user can select account from 'Select an Account' page.")
    public void ifSelectAccount() {
        nextPage = runApplication();
        nextPage = ((EnterEmailPage) nextPage)
                .enterEmail(multiple_accounts_user)
                .clickSignInBtn()
                .assertThat();
        if (nextPage instanceof VerifyIdentityPage)
            nextPage = ((VerifyIdentityPage) nextPage)
                    .enterVerificationCode(new NirmataMailer(multiple_accounts_user).getAccessCode(timeStamp, 60, 10))
                    .clickSignInBtn()
                    .assertThat();
        nextPage = ((SelectAccountPage) nextPage)
                .selectAccount(user_account)
                .assertThat();
    }

    @Test (priority = 3, alwaysRun = true, description = "Verify if empty password is not accepted and error message appears.")
    public void ifEmptyPassword() {
        nextPage = runApplication();
        nextPage = ((EnterEmailPage) nextPage)
                .enterEmail(single_account_user)
                .clickSignInBtn()
                .assertThat();
        nextPage = ((EnterPasswordPage) nextPage)
                .enterPassword("")
                .clickSignInBtn()
                .assertThat(false, "Empty password has been accepted!");
        assertFalse(((EnterPasswordPage) nextPage).getErrorMessage().isEmpty(), "There's no error message!");
        assertTrue(((EnterPasswordPage) nextPage).getErrorMessage().contains("Invalid password. Please try again."), "Error message text is not correct!");
    }

    @Test (priority = 3, dependsOnMethods = {"ifEmptyPassword"}, description = "Verify if invalid password is not accepted and error message appears.")
    public void ifInvalidPassword() {
        nextPage = ((EnterPasswordPage) nextPage)
                .enterPassword(wrong_password)
                .clickSignInBtn()
                .assertThat(false, "Inalid password has been accepted!");
        assertFalse(((EnterPasswordPage) nextPage).getErrorMessage().isEmpty(), "There's no error message!");
        assertTrue(((EnterPasswordPage) nextPage).getErrorMessage().contains("Invalid password. Please try again."), "Error message text is not correct!");
    }

    @Test (priority = 4, dependsOnMethods = {"ifEmptyPassword"}, description = "Verify if user can run password resetting.")
    public void ifPasswordReset() {
        nextPage = ((EnterPasswordPage) nextPage)
                .clickForgotYourPasswordLink()
                .assertThat();
        assertTrue(nextPage instanceof ResetPasswordPage,"'Forgot your password' does not lead to 'Reset your password' web page!");
    }

    @Test (priority = 5, description = "Verify if valid password is accepted.")
    public void ifPasswordAccepted() {
        nextPage = runApplication();
        nextPage = ((EnterEmailPage) nextPage)
                .enterEmail(single_account_user)
                .clickSignInBtn()
                .assertThat();
        nextPage = ((EnterPasswordPage) nextPage)
                .enterPassword(TestData.getUser("user_password", single_account_user))
                .clickSignInBtn()
                .assertThat();
        assertTrue(nextPage instanceof MainApplicationPage,"Main Application page is not loaded, looks like password is not accepted!");
    }

}
