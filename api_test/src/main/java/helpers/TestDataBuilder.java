package helpers;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Comment;
import model.Post;


public class TestDataBuilder {
    public Post createPostPayload(String title, String content) {
        return new Post(title, content);
    }

    public Comment createCommentPayload(String commentName, String content) {
        return new Comment(commentName, content);
    }

    public static String getJsonPath(Response response, String key) {
        JsonPath jsonPath = new JsonPath(response.asString());
        return jsonPath.get(key);
    }

    public static int getJsonPathForInteger(Response response, String key) {
        JsonPath jsonPath = new JsonPath(response.asString());
        return jsonPath.getInt(key);
    }

}
