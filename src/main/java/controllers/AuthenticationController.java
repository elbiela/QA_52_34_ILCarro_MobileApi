package controllers;

import dto.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.BaseApi;

import static io.restassured.RestAssured.given;

public class AuthenticationController implements BaseApi {
    public Response registrationLogin(User user, String url) {
        return given()
                .body(user)
                .contentType(ContentType.JSON)
                .when()
                .post(BASE_URL+url)
                .andReturn();
    }

    public Response registrationLoginWithTextContentType(User user,String url) {
        return given()
                .body(url)
                .contentType(ContentType.TEXT)
                .when()
                .post(BASE_URL + REGISTRATION_URL)
                .andReturn();
    }
}
