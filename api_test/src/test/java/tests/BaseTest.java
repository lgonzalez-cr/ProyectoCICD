package tests;

import helpers.RequestHelper;
import helpers.TestDataBuilder;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static helpers.RequestHelper.createRandomCommentAndGetId;

public class BaseTest {
    TestDataBuilder testDataBuilder = new TestDataBuilder();
    Response response;
    protected int postId;
    protected int commentId;

    @Parameters("host")
    @BeforeSuite
    public void setup(@Optional("https://api-coffee-testing.herokuapp.com") String host) throws FileNotFoundException {
        System.out.println(String.format("Test Host: %s", host));
        RestAssured.baseURI = host;
        new PrintStream(new FileOutputStream("Logs.log"));
    }

    @BeforeMethod(onlyForGroups = "requiresPostId")
    void createPost(){
        postId = RequestHelper.createRandomPostAndGetId();
    }

    @BeforeMethod(onlyForGroups = "requiresPostIdAndCommentId")
    void createPostAndComment(){
        postId = RequestHelper.createRandomPostAndGetId();
        commentId = createRandomCommentAndGetId(postId);
    }

    @AfterClass
    void deleteAllPosts(){
        RequestHelper.deleteAllPostsForUser();
    }
}
