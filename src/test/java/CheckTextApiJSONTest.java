import api.CheckTextApi;
import api.request_options.Params;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static api.CheckTextApi.*;
import static api.request_options.Options.*;

public class CheckTextApiJSONTest {

    @Test
    public void simpleCallWithCorrectWord() {
        RestAssured
                .given()
                .params(Params.PARAM_TEXT, "Correct word")
                .params(Params.PARAM_LANG, Params.Language.EN)
                .params(Params.PARAM_OPTIONS, calcOptions(FIND_REPEAT_WORDS))
                .log().everything()

                .when()
                .get(CheckTextApi.YANDEX_SPELLER_CHECK_TEXT_URL)
                .prettyPeek()

                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .header("Access-Control-Allow-Origin", "*")
                .time(Matchers.lessThan(2L), TimeUnit.SECONDS);
    }

    @Test
    public void correctEnWordTest(){
        RestAssured
                .given()
                .params(Params.PARAM_TEXT, "correct word")
                .log().all()

                .when()
                .get(CheckTextApi.YANDEX_SPELLER_CHECK_TEXT_URL)
                .prettyPeek()

                .then()
                .contentType(ContentType.JSON)
                .content(Matchers.equalTo("[]"));
    }

    @Test
    public void wrongEnWordTest(){
        RestAssured
                .given(baseRequestConfig())
                .params(Params.PARAM_TEXT, "korrect")

                .when()
                .get()
                .prettyPeek()

                .then()
                .spec(successResponse())
                .content(Matchers.containsString("\"s\":[\"correct\",\"korrekt\",\"correctly\"]"));
    }

    @Test
    public void correctRuWordTest(){
        RestAssured
                .given(baseRequestConfig())
                .params(Params.PARAM_TEXT, "проверить")

                .when()
                .get()
                .prettyPeek()

                .then()
                .spec(successResponse())
                .content(Matchers.equalTo("[]"));
    }

    @Test
    public void wrongRuWordTest(){
        RestAssured
                .given(baseRequestConfig())
                .params(Params.PARAM_TEXT, "праверить")

                .when()
                .get()
                .prettyPeek()

                .then()
                .spec(successResponse())
                .content(Matchers.containsString("\"s\":[\"проверить\",\"проверит\",\"прверить\"]"));
    }

    @Test
    public void correctEnPlusRuWordTest(){
        RestAssured
                .given(baseRequestConfig())
                .params(Params.PARAM_TEXT, "проверить text")

                .when()
                .get()
                .prettyPeek()

                .then()
                .spec(successResponse())
                .content(Matchers.equalTo("[]"));
    }

    @Test
    public void wrongRuCorrectEnWordTest(){
        RestAssured
                .given(baseRequestConfig())
                .params(Params.PARAM_TEXT, "праверить text")
                .log().all()

                .when()
                .get()
                .prettyPeek()

                .then()
                .spec(successResponse())
                .content(Matchers.containsString("проверить"));
    }

    @Test
    public void wrongEnPlusRuWordTest(){
        RestAssured
                .given(baseRequestConfig())
                .params(Params.PARAM_TEXT, "проверить teext")

                .when()
                .get()
                .prettyPeek()

                .then()
                .spec(successResponse())
                .content(Matchers.containsString("s\":[\"text\",\"texet\",\"etext\"]"));
    }

    @Test
    public void ruWordInEnTest(){
        RestAssured
                .given(baseRequestConfig())
                .params(Params.PARAM_TEXT, "ghjdthrf")

                .when()
                .get()
                .prettyPeek()

                .then()
                .spec(successResponse())
                .content(Matchers.containsString("проверка"));
    }

    @Test
    public void enWordInRuTest(){
        RestAssured
                .given(baseRequestConfig())
                .params(Params.PARAM_TEXT, "сщккусе")

                .when()
                .get()
                .prettyPeek()

                .then()
                .spec(successResponse())
                .content(Matchers.containsString("correct"));
    }

    @Test
    public void ignoreDigitsWithoutOptionsTest(){
        RestAssured
                .given(baseRequestConfig())
                .params(Params.PARAM_TEXT, "word123")

                .when()
                .get()
                .prettyPeek()

                .then()
                .spec(successResponse())
                .content(Matchers.containsString("word 123"));
    }

    @Test
    public void ignoreDigitsWithOptionsTest(){
        RestAssured
                .given(baseRequestConfig())
                .params(Params.PARAM_TEXT, "проверить text")
                .params(Params.PARAM_OPTIONS, calcOptions(IGNORE_DIGITS))

                .when()
                .get()
                .prettyPeek()

                .then()
                .spec(successResponse())
                .content(Matchers.equalTo("[]"));
    }

    @Test
    public void badRequestTest(){
        RestAssured
                .given(baseRequestConfig())
                .param(Params.PARAM_TEXT, "some text")
                .header("some header", "wrong header")
                .body("wrong body")

                .when()
                .get()
                .prettyPeek()

                .then()
                .spec(badRequestResponse());

    }

}
