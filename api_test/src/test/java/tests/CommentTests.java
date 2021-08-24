package tests;

import helpers.APIResourcesHelper;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import specifications.ResponseSpecifications;

import static helpers.RequestHelper.createRandomCommentAndGetId;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static specifications.RequestSpecifications.requestSpecificationSetupBasicAuth;

public class CommentTests extends BaseTest {

    @Test(description = "This test aims to create a comment", groups = "requiresPostId")
    public void createPostTestValid() {
        given().spec(requestSpecificationSetupBasicAuth())
                .pathParam("postId", postId)
                .body(testDataBuilder.createCommentPayload("CommentName", "Content"))
                .when().post(APIResourcesHelper.CreateCommentAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(200, "Comment created", "message"));
    }

    @Test(description = "This test aims to retrieve all comments that belong to a post", groups = "requiresPostId")
    public void getAllCommentsForPost() {
        createRandomCommentAndGetId(postId);
        createRandomCommentAndGetId(postId);

        response = given().spec(requestSpecificationSetupBasicAuth())
                .pathParam("postId", postId)
                .when().get(APIResourcesHelper.GetCommentsAPI.getResource());

        response.then().statusCode(200);
        assertThat(response.asString(), matchesJsonSchemaInClasspath("getComments.schema.json"));
    }

    @Test(description = "This test aims to retrieve one comment that belongs to a post", groups = "requiresPostIdAndCommentId")
    public void getCommentForPost() {
        response = given().spec(requestSpecificationSetupBasicAuth())
                .pathParam("postId", postId)
                .pathParam("commentId", commentId)
                .when().get(APIResourcesHelper.GetCommentAPI.getResource());

        response.then().statusCode(200);

        assertThat(response.asString(), matchesJsonSchemaInClasspath("getComment.schema.json"));
        assertThat(response.path("data.id"), Matchers.equalTo(commentId));
    }

    @Test(description = "This test aims to update a comment", groups = "requiresPostIdAndCommentId")
    public void updateCommentTest() {
        given().spec(requestSpecificationSetupBasicAuth())
                .pathParam("postId", postId)
                .pathParam("commentId", commentId)
                .body(testDataBuilder.createCommentPayload("New Comment Name", "New Comment content"))
                .when().put(APIResourcesHelper.UpdateCommentAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(200, "Comment updated", "message"));
    }

    @Test(description = "This test aims to update a post", groups = "requiresPostIdAndCommentId")
    public void deleteCommentTest() {
        given().spec(requestSpecificationSetupBasicAuth())
                .pathParam("postId", postId)
                .pathParam("commentId", commentId)
                .when().delete(APIResourcesHelper.DeleteCommentAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(200, "Comment deleted", "message"));
    }

    /************************************************* INVALID TESTS ******************************************************************************/

    @Test(description = "This test aims to try to create a comment when auth is missing", groups = "requiresPostId")
    public void createCommentMissingAuthValid() {
        given().pathParam("postId", postId)
                .body(testDataBuilder.createCommentPayload("CommentName", "Content"))
                .when().post(APIResourcesHelper.CreateCommentAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(401, "Please login first", "message"));
    }

    @Test(description = "This test aims to try to create a comment when postId is blank", groups = "requiresPostId")
    public void createCommentMissingPostIdValid() {
        given().spec(requestSpecificationSetupBasicAuth())
                .pathParam("postId", " ")
                .body(testDataBuilder.createCommentPayload("CommentName", "Content"))
                .when().post(APIResourcesHelper.CreateCommentAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(406, "PostID is missing", "message"));
    }

    @Test(description = "This test aims to retrieve all comments when auth is missing", groups = "requiresPostId")
    public void getAllCommentsForMissingAuth() {
        given().pathParam("postId", postId)
                .when().get(APIResourcesHelper.GetCommentsAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(401, "Please login first", "message"));
    }

    @Test(description = "This test aims to retrieve all comments that belongs to an invalid postId")
    public void getAllCommentsForInvalidPost() {
        given().spec(requestSpecificationSetupBasicAuth())
                .pathParam("postId", "756756756756756756")
                .when().get(APIResourcesHelper.GetCommentsAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(406, "Could not get comments", "Message"));
    }

    @Test(description = "This test aims to retrieve a comment that does not exist", groups = "requiresPostIdAndCommentId")
    public void getNotExistingCommentForPostTest() {
        given().spec(requestSpecificationSetupBasicAuth())
                .pathParam("postId", postId)
                .pathParam("commentId", 99999)
                .when().get(APIResourcesHelper.GetCommentAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(404, "Comment not found", "Message"));
    }

    @Test(description = "This test aims to retrieve a comment when the id is invalid", groups = "requiresPostIdAndCommentId")
    public void getInvalidCommentForPostTest() {
        given().spec(requestSpecificationSetupBasicAuth())
                .pathParam("postId", postId)
                .pathParam("commentId", "X")
                .when().get(APIResourcesHelper.GetCommentAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(404, "Invalid parameter", "Message"));
    }

    @Test(description = "This test aims to update a comment with missing auth", groups = "requiresPostIdAndCommentId")
    public void updateCommentWithMissingAuthTest() {
        given().pathParam("postId", postId)
                .pathParam("commentId", commentId)
                .body(testDataBuilder.createCommentPayload("New Comment Name", "Test"))
                .when().put(APIResourcesHelper.UpdateCommentAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(401, "Please login first", "message"));
    }

    @Test(description = "This test aims to update a comment with null data", groups = "requiresPostId")
    public void updatePostWithInvalidDataTest() {
        given().spec(requestSpecificationSetupBasicAuth())
                .pathParam("postId", postId)
                .pathParam("commentId", commentId)
                .body(testDataBuilder.createPostPayload(null, null))
                .when().put(APIResourcesHelper.UpdateCommentAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(406, "Invalid form", "message"));
    }

    @Test(description = "This test aims to delete a comment when the auth is missing", groups = "requiresPostIdAndCommentId")
    public void deleteCommentWithMissingAuthTest() {
        given().pathParam("postId", postId)
                .pathParam("commentId", commentId)
                .when().delete(APIResourcesHelper.DeleteCommentAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(401, "Please login first", "message"));
    }

    @Test(description = "This test aims to try to delete a comment when the commentId is invalid", groups = "requiresPostIdAndCommentId")
    public void deleteCommentForInvalidCommentIdTest() {
        given().spec(requestSpecificationSetupBasicAuth())
                .pathParam("postId", postId)
                .pathParam("commentId", "E")
                .when().delete(APIResourcesHelper.DeleteCommentAPI.getResource())
                .then().spec(ResponseSpecifications.validateResponse(404, "Invalid parameter", "message"));
    }
}
