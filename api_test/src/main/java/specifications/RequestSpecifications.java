package specifications;


import static helpers.RequestHelper.getAuthToken;

import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class RequestSpecifications {
    static RequestSpecification requestSpecification;
    static RequestSpecification requestSpecificationBasicAuth;

    public static RequestSpecification requestSpecificationSetup() {
        PrintStream log;

        if(requestSpecification == null){
            try {
                log = new PrintStream(new FileOutputStream("Logs.log"));
                String token = getAuthToken();
                RequestSpecBuilder builder = new RequestSpecBuilder();
                builder.addHeader("Authorization", "Bearer " + token);
                builder.addFilter(RequestLoggingFilter.logRequestTo(log));
                builder.addFilter(ResponseLoggingFilter.logResponseTo(log));
                requestSpecification = builder.build();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return requestSpecification;
    }

    public static RequestSpecification requestSpecificationSetupBasicAuth() {
        PrintStream log;
        final PreemptiveBasicAuthScheme preemptiveBasicAuthScheme = new PreemptiveBasicAuthScheme();

        if(requestSpecificationBasicAuth == null){
            try {
                log = new PrintStream(new FileOutputStream("Logs.log"));
                RequestSpecBuilder builder = new RequestSpecBuilder();
                builder.addFilter(RequestLoggingFilter.logRequestTo(log));
                builder.addFilter(ResponseLoggingFilter.logResponseTo(log));
                preemptiveBasicAuthScheme.setUserName("testuser");
                preemptiveBasicAuthScheme.setPassword("testpass");
                builder.setAuth(preemptiveBasicAuthScheme);
                requestSpecificationBasicAuth = builder.build();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return requestSpecificationBasicAuth;
    }


}
