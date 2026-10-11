package api_rest_tests;

import controllers.AuthenticationController;
import dto.TokenDto;
import dto.User;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.PropertiesReader;
import static utils.PropertiesReader.*;

import static utils.UserFactory.*;

public class RegistrationLoginTests extends AuthenticationController {
    @Test
    public void registrationPositiveTest(){
        User user = positiveRegisterUser();
        Assert.assertEquals(registrationLogin(user, REGISTRATION_URL).getStatusCode(), 200);
    }

    @Test
    public void loginPositiveTest(){
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


}
