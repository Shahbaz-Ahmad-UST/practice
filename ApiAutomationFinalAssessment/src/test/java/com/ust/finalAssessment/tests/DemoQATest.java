package com.ust.finalAssessment.tests;
import com.ust.finalAssessment.api.client.AuthClient;
import com.ust.finalAssessment.api.client.BookClient;
import com.ust.finalAssessment.model.Book;
import com.ust.finalAssessment.report.ExtentTestListener;
import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.List;

import static com.ust.finalAssessment.config.ApiConfiguration.getRequired;
import static com.ust.finalAssessment.factory.ResponseSpecFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;


@Epic("final Assessment Journeys")
@Feature("Full Getting Book List")
@Owner("Shahbaz Ahmad")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(ExtentTestListener.class)
public class DemoQATest {
    AuthClient authClient = new AuthClient();
    BookClient bookClient = new BookClient();
    private final String username = getRequired("USER_NAME");
    private final String password = getRequired("USER_PASSWORD");
    private static String token;
    private final  String id = Double.toString(Math.random() * 1000001);


    @Test
    @Order(0)
    @DisplayName("create a user with user name and password")
    @Story("user create - generate token - get list of book")
    @Severity(SeverityLevel.CRITICAL)
    void create_user_with_Credentials()
    {
        Response response =authClient.create_User(username+id,password)
                .then()
                .spec(authResponse)
                .extract()
                .response();
        JsonPath jsonPath= response.jsonPath();
        assertThat(jsonPath.getString("username"),equalTo(username+id));
        assertThat(jsonPath.getList("books").size(),equalTo(0));
    }


    @Test
    @Order(1)
    @DisplayName("Generate token")
    @Story("user create - generate token - get list of book")
    @Severity(SeverityLevel.CRITICAL)
    void generate_Token()
    {
        Response response =authClient.login_generate_Token(username,password)
                .then()
                .spec(jsonResponse)
                .extract()
                .response();

        JsonPath jsonPath= response.jsonPath();

        token = jsonPath.getString("token");

        assertThat(jsonPath.getString("status"),equalTo("Success"));
        assertThat(jsonPath.getString("result"),equalTo("User authorized successfully."));
    }


    @Test
    @Order(2)
    @DisplayName("Get list of Book by using token")
    @Story("user create - generate token - get list of book")
    @Severity(SeverityLevel.NORMAL)
    void getList_Of_Book()
    {
        Response response =bookClient.getListOfBook(token)
                .then()
                .spec(jsonResponse)
                .extract()
                .response();

        JsonPath jsonPath= response.jsonPath();

        List<Book> list = jsonPath.getList("books", Book.class);

        assertThat(list.size(),greaterThanOrEqualTo(0));

        for (Book book : list) {
            System.out.println("---------------------------------------------");
            System.out.println("Isbn "+book.isbn());
            System.out.println("Title "+book.title());
            System.out.println("Subtitle "+book.subTitle());
            System.out.println("Author "+book.publish_date());
            System.out.println("Publisher "+ book.publisher());
            System.out.println("Pages "+book.pages());
            System.out.println("Description "+book.description());
            System.out.println("website "+book.website());
            System.out.println("---------------------------------------------");
        }
    }
}
