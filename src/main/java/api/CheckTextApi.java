package api;

import beans.YandexSpellerAnswer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import api.request_options.Params;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CheckTextApi {

    //    Resource URL
    public static final String YANDEX_SPELLER_CHECK_TEXT_URL =
            "https://speller.yandex.net/services/spellservice.json/checkText";

    //    Check text method parameters to send in request
    private final HashMap<String, String> params = new HashMap<>();

    //    Request builder method
    public static CheckTextApiBuilder with() {
        return new CheckTextApiBuilder(new CheckTextApi());
    }

    //    Request builder pattern
    public static class CheckTextApiBuilder {
        CheckTextApi checkText;

        private CheckTextApiBuilder(CheckTextApi checkText) {
            this.checkText = checkText;
        }

        public CheckTextApiBuilder text(String text) {
            checkText.params.put(Params.PARAM_TEXT, text);
            return this;
        }

        public CheckTextApiBuilder lang(String lang) {
            checkText.params.put(Params.PARAM_LANG, lang);
            return this;
        }

        public CheckTextApiBuilder options(String options) {
            checkText.params.put(Params.PARAM_OPTIONS, options);
            return this;
        }

        //    Method get call with request and response console logging
        public Response callApi() {
            return RestAssured.with()
                    .queryParams(checkText.params)
                    .log().all()
                    .get(YANDEX_SPELLER_CHECK_TEXT_URL)
                    .prettyPeek();
        }
    }

        //    Get answers from response JSON to Object
    public static List<YandexSpellerAnswer> getYandexSpellerAnswers(Response response) {
        return new Gson().fromJson(response.asString().trim(), new TypeToken<List<YandexSpellerAnswer>>() {
        }.getType());
    }

    //    Create request with base config parameters
    public static RequestSpecification baseRequestConfig() {
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .log(LogDetail.ALL)
                .addQueryParam("requestID", new Random().nextLong())
                .setBaseUri(YANDEX_SPELLER_CHECK_TEXT_URL)
                .build();
    }

    //    Create response specifications with common asserts
    public static ResponseSpecification baseResponse() {
        return new ResponseSpecBuilder()
//                .expectContentType(ContentType.JSON)
//                .expectHeader("Access-Control-Allow-Origin", "*")
                .expectResponseTime(Matchers.lessThan(20000L))
                .build();
    }

    public static ResponseSpecification successResponse() {
        return baseResponse()
                .contentType(ContentType.JSON)
                .header("Access-Control-Allow-Origin", "*")
                .statusCode(HttpStatus.SC_OK);
    }

    public static ResponseSpecification badRequestResponse() {
        return baseResponse()
                .header("Connection", "Close")
                .header("Content-Length", "0")
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
