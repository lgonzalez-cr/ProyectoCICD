package helpers;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Comment;
import model.Post;
import model.User;
import specifications.RequestSpecifications;
import specifications.ResponseSpecifications;

import java.util.List;

import static helpers.TestDataBuilder.*;
import static io.restassured.RestAssured.given;
import static specifications.RequestSpecifications.requestSpecificationSetup;
import static specifications.RequestSpecifications.requestSpecificationSetupBasicAuth;

public class RequestHelper {

    public static String getAuthToken () {
        User testUser = new User(
                "Luis",
                "password@password.com",
                "password");

        Response response = given().body(testUser).post(APIResourcesHelper.LoginAPI.getResource());

        return getJsonPath(response, "token.access_token");
    }

    public static int createRandomPostAndGetId () {
        Response response = given().spec(RequestSpecifications.requestSpecificationSetup())
                .body(new Post("Test", "test"))
                .when().post(APIResourcesHelper.CreatePostAPI.getResource());
        return getJsonPathForInteger(response, "id");
    }

    public static int createRandomCommentAndGetId (int postId) {
        Response response = given().spec(requestSpecificationSetupBasicAuth())
                .pathParam("postId", postId)
                .body(new Comment("Test", "test"))
                .when().post(APIResourcesHelper.CreateCommentAPI.getResource());
        return getJsonPathForInteger(response, "id");
    }

    public static void deleteAllPostsForUser () {
        Response response = given().spec(requestSpecificationSetup())
                 .when().get(APIResourcesHelper.valueOf("GetPostsAPI").getResource());

        int numberOfPosts = getJsonPathForInteger(response, "results[0].meta.total");
        for (int i = 0; i < numberOfPosts; i++) {
            int idToDelete = getJsonPathForInteger(response,"results[0].data["+i+"].id");

            if(idToDelete > 0)
                given().spec(requestSpecificationSetup())
                    .pathParam("postId", idToDelete)
                    .when().delete(APIResourcesHelper.valueOf("UpdatePostAPI").getResource())
                    .then().spec(ResponseSpecifications.validateResponse(200, "Post deleted", "message"));
        }
    }
}
