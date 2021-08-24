package specifications;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;

public class ResponseSpecifications {
    public static ResponseSpecification validateResponse(int responseCode, String message, String bodyMessageVarName) {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(responseCode);
        builder.expectBody(bodyMessageVarName, Matchers.hasToString(message));
        builder.expectContentType("application/json");
        return builder.build();
    }
}
