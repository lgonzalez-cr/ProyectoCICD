package tests;

import helpers.APIResourcesHelper;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import specifications.ResponseSpecifications;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static specifications.RequestSpecifications.requestSpecificationSetup;

public class PostTests extends BaseTest {

    @Test(description = "This test aims to create a post")
    public void createPostTestValid() {
        given().spec(requestSpecificationSetup())
        .body(testDataBuilder.createPostPayload("Title", "Content"))
        .when().post(APIResourcesHelper.CreatePostAPI.getResource())
        .then().spec(ResponseSpecifications.validateResponse(200, "Post created", "message"));
    }

    @Test(description = "This test aims to get all posts")
    public void retrieveAllPostsTest() {
        response = given().spec(requestSpecificationSetup())
                .when().get(APIResourcesHelper.GetPostsAPI.getResource());

        response.then().statusCode(200);
        assertThat(response.asString(), matchesJsonSchemaInClasspath("getPosts.schema.json"));
    }

    @Test(description = "This test aims to get posts by id", groups = "requiresPostId")
    public void retrievePostByIdTest() {
        response = given().spec(requestSpecificationSetup())
                .pathParam("postId", postId)
                .when().get(APIResourcesHelper.GetPostAPI.getResource());

        response.then().statusCode(200);
        assertThat(response.asString(), matchesJsonSchemaInClasspath("getPost.schema.json"));
        assertThat(response.path("data.id"), Matchers.equalTo(postId));
    }

    @Test(description = "This test aims to update a post", groups = "requiresPostId")
    public void updatePostTest() {
        given().spec(requestSpecificationSetup())
                .pathParam("postId", postId)
                .body(testDataBuilder.createPostPayload("New title", "New content"))
                .when().put(APIResourcesHelper.UpdatePostAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(200, "Post updated", "message"));
    }

    @Test(description = "This test aims to update a post", groups = "requiresPostId")
    public void deletePostTest() {
        given().spec(requestSpecificationSetup())
                .pathParam("postId", postId)
                .when().delete(APIResourcesHelper.DeletePostAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(200, "Post deleted", "message"));
    }

    /************************************************* INVALID TESTS ******************************************************************************/

    @Test(description = "This test aims to try to create a post without auth")
    public void createPostMissingAuthTest(){
        given()
        .body(testDataBuilder.createPostPayload("Title", "Content"))
        .when().post(APIResourcesHelper.CreatePostAPI.getResource())
        .then().spec(ResponseSpecifications.validateResponse(401, "Please login first", "message"));
    }

    @Test(description = "This test aims to try to create a post without title and content")
    public void createPostMissingFieldsTest(){
        given().spec(requestSpecificationSetup())
        .body(testDataBuilder.createPostPayload("", ""))
        .when().post(APIResourcesHelper.CreatePostAPI.getResource())
        .then().spec(ResponseSpecifications.validateResponse(406, "Invalid form", "message"));
    }

    @Test(description = "This test aims to get all posts without auth")
    public void retrieveAllPostsMissingAuthTest() {
        given()
        .when().get(APIResourcesHelper.GetPostsAPI.getResource())
        .then().spec(ResponseSpecifications.validateResponse(401, "Please login first", "message"));
    }

    @Test(description = "This test aims to get a post which doesn't exist")
    public void retrieveNotExistingPostByIdTest() {
        given().spec(requestSpecificationSetup())
        .pathParam("postId", 9999)
        .when().get(APIResourcesHelper.GetPostAPI.getResource())
        .then().spec(ResponseSpecifications.validateResponse(404, "Post not found", "Message"));
    }

    @Test(description = "This test aims to update a post using null data", groups = "requiresPostId")
    public void updatePostWithInvalidDataTest() {
        given().spec(requestSpecificationSetup())
                .pathParam("postId", postId)
                .body(testDataBuilder.createPostPayload(null, null))
                .when().put(APIResourcesHelper.UpdatePostAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(406, "Invalid form", "message"));
    }

    @Test(description = "This test aims to  try to delete a post when its id is invalid", groups = "requiresPostId")
    public void deletePostInvalidIdTest() {
        given().spec(requestSpecificationSetup())
                .pathParam("postId", "v2")
                .when().delete(APIResourcesHelper.DeletePostAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(404, "Invalid parameter", "message"));
    }
}
