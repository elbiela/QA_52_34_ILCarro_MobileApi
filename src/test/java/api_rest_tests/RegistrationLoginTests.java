package api_rest_tests;

import controllers.AuthenticationController;
import data_providers.UserDataProvider;
import dto.TokenDto;
import dto.User;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static utils.PropertiesReader.*;

import static utils.UserFactory.*;

public class RegistrationLoginTests extends AuthenticationController {
    @Test
    public void registrationPositiveTest() {
        User user = positiveRegisterUser();
        Assert.assertEquals(registrationLogin(user, REGISTRATION_URL).getStatusCode(), 200);
    }

    @Test
    public void registrationNegativeEmptyAllFieldsTest() {
        User user = positiveRegisterUser();
        user.setUsername("");
        user.setPassword("");
        user.setFirstName("");
        user.setLastName("");

        Assert.assertEquals(registrationLogin(user, REGISTRATION_URL).getStatusCode(), 400);
    }

    @Test
    public void registrationNegativeEmptyEmailTest() {
        User user = positiveRegisterUser();
        user.setUsername("");

        Assert.assertEquals(registrationLogin(user, REGISTRATION_URL).getStatusCode(), 400);
    }

    @Test
    public void registrationNegativeEmptyPasswordTest() {
        User user = positiveRegisterUser();
        user.setPassword("");

        Assert.assertEquals(registrationLogin(user, REGISTRATION_URL).getStatusCode(), 400);
    }

    @Test
    public void registrationNegativeEmptyFirstNameTest() {
        User user = positiveRegisterUser();
        user.setFirstName("");

        Assert.assertEquals(registrationLogin(user, REGISTRATION_URL).getStatusCode(), 400);
    }

    @Test
    public void registrationNegativeEmptyLastNameTest() {
        User user = positiveRegisterUser();
        user.setLastName("");

        Assert.assertEquals(registrationLogin(user, REGISTRATION_URL).getStatusCode(), 400);
    }

    @Test(dataProvider = "dataProviderInvalidPasswordOrEmail", dataProviderClass = UserDataProvider.class)
    public void registrationNegativeInvalidEmailOrPasswordTest(User user) {
        Assert.assertEquals(registrationLogin(user, REGISTRATION_URL).getStatusCode(), 400);
    }

    @Test
    public void registrationNegativeRegisteredUserTest(){
        User user = positiveRegisterUser();

        Assert.assertEquals(registrationLogin(user, REGISTRATION_URL).getStatusCode(), 200);
        Assert.assertEquals(registrationLogin(user, REGISTRATION_URL).getStatusCode(), 400);
    }

    @Test
    public void registrationNegativeWrongFormatTest() {
        User user = positiveRegisterUser();
        Assert.assertEquals(registrationLoginWithTextContentType(user, REGISTRATION_URL).getStatusCode(), 500);
    }

    @Test
    public void loginPositiveTest() {
        User user = User.builder()
                .username(getProperty("base.properties", "email"))
                .password(getProperty("base.properties", "password"))
                .build();
        Response response = registrationLogin(user, LOGIN_URL);
        System.out.println(response.getBody().asString());
        TokenDto tokenDto = response.getBody().as(TokenDto.class);
        System.out.println(tokenDto.getAccessToken());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void loginAfterRegistrationTest() {
        User registeredUser = positiveRegisterUser();

        Assert.assertEquals(registrationLogin(registeredUser, REGISTRATION_URL).getStatusCode(), 200);

        User loginUser = User.builder()
                .username(registeredUser.getUsername())
                .password(registeredUser.getPassword())
                .build();

        Assert.assertEquals(registrationLogin(loginUser, LOGIN_URL).getStatusCode(), 200);
    }

    @Test
    public void loginNegativeEmptyAllFieldsTest() {
        User user = User.builder()
                .username("")
                .password("")
                .build();
        Assert.assertEquals(registrationLogin(user, LOGIN_URL).getStatusCode(), 401);
    }

    @Test
    public void loginNegativeEmptyEmailTest() {
        User user = User.builder()
                .username("")
                .password(getProperty("base.properties", "password"))
                .build();
        Assert.assertEquals(registrationLogin(user, LOGIN_URL).getStatusCode(), 401);
    }

    @Test
    public void loginNegativeEmptyPasswordTest() {
        User user = User.builder()
                .username(getProperty("base.properties", "email"))
                .password("")
                .build();
        Assert.assertEquals(registrationLogin(user, LOGIN_URL).getStatusCode(), 401);
    }

    @Test
    public void loginNegativeUnregisteredUserTest(){
        User user = positiveRegisterUser();
        Assert.assertEquals(registrationLogin(user, LOGIN_URL).getStatusCode(), 401);
    }

    @Test
    public void loginNegativeWrongPasswordTest() {
        User user = positiveLoginUser();
        user.setPassword(getProperty("base.properties", "wrongPassword"));
        Assert.assertEquals(registrationLogin(user, LOGIN_URL).getStatusCode(), 401);
    }

    @Test
    public void loginNegativeWrongFormatTest() {
        User user = positiveLoginUser();
        Assert.assertEquals(registrationLoginWithTextContentType(user, REGISTRATION_URL).getStatusCode(), 500);
    }

}
