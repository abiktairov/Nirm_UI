package webobjects.SignIn;

import framework.WebPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import webobjects.SignInPage;

public class SignUpPage extends SignInPage {
    private String by_name = "//input[@id='name']";
    private String by_business_email = "//input[@id='email']";
    private String by_company_name = "//input[@id='company']";
    private String by_phone_number = "//input[@id='phone']";
    private String by_signup_btn = "//button[@id='btnSignupEmail']";
    private String by_alreadyhave_link = "//a[contains(text(),'Already have an account? Sign in to Nirmata now')]";

    public SignUpPage(WebDriver webDriver) {
        super(webDriver);
        Assert.assertTrue(waitAppear(by_login_title, text_signUp), "Timeout of pageObject " + this.getClass().getName() + " loading.");
    }

    public SignUpPage enterName(String name) {
        getElement(by_name).clear();
        getElement(by_name).sendKeys(name);
        return this;
    }

    public SignUpPage enterBusinessEmail(String email) {
        getElement(by_business_email).clear();
        getElement(by_business_email).sendKeys(email);
        return this;
    }

    public SignUpPage enterCompanyName(String name) {
        getElement(by_company_name).clear();
        getElement(by_company_name).sendKeys(name);
        return this;
    }

    public SignUpPage enterPhoneNumber(String number) {
        getElement(by_phone_number).clear();
        getElement(by_phone_number).sendKeys(number);
        return this;
    }

    public WebPage clickSignUpBtn() {
        getElement(by_signup_btn).click();
        if (waitDisappear(by_login_title, text_signUp)) return dispatchClass();
        else return this;
    }

    public WebPage clickAlreadyHaveLink() {
        getElement(by_alreadyhave_link).click();
        if (waitDisappear(by_login_title, text_signUp)) return dispatchClass();
        else return this;
    }

}
