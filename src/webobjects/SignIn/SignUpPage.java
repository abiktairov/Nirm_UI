package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignInPage;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class SignUpPage extends SignInPage {
    private String by_name = "//input[@id='name']";
    private String by_business_email = "//input[@id='email']";
    private String by_company_name = "//input[@id='company']";
    private String by_phone_number = "//input[@id='phone']";
    private String by_signup_btn = "//button[@id='btnSignupEmail']";
    private String by_accept_btn = "//div[contains(@class,'modal-content')]//button[contains(text(),'Accept and Proceed')]";
    private String by_alreadyhave_link = "//a[contains(text(),'Already have an account? Sign in to Nirmata now')]";
    private String by_confirmation_text = "//*[contains(text(),'Your account has been created!')]";


    public SignUpPage(WebDriver webDriver) {
        super(webDriver);
        assertTrue(waitAppear(by_login_title, text_signUp), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public SignUpPage enterName(String name) {
        updateElement(by_name, name);
        return this;
    }

    public SignUpPage enterBusinessEmail(String email) {
        updateElement(by_business_email, email);
        return this;
    }

    public SignUpPage enterCompanyName(String name) {
        updateElement(by_company_name, name);
        return this;
    }

    public SignUpPage enterPhoneNumber(String number) {
        updateElement(by_phone_number, number);
        return this;
    }

    public SignUpPage clickSignUpBtn() {
        clickElement(by_signup_btn);
        return this;
    }

    public SignUpPage clickAlreadyHaveLink() {
        clickElement(by_alreadyhave_link);
        return this;
    }

    public SignUpPage clickAcceptAndProceed() {
        clickElement(by_accept_btn);
        return this;
    }

    @Override
    public SignUpPage assertThat(boolean expectation, String message) {
        if (expectation) assertTrue(waitDisappear(by_signup_btn) && waitAppear(by_confirmation_text), message);
        else             assertFalse(waitDisappear(by_signup_btn) && waitAppear(by_confirmation_text), message);
        return this;
    }

}
