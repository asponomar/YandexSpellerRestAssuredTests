import api.CheckTextApi;
import enums.Options;
import enums.Params;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static enums.Options.*;

public class CheckTextApiJSONTests {

    @Test
    public void simpleCallWithCorrectWord() {
        RestAssured
                .given()
                .params(Params.PARAM_TEXT, "Correct word")
                .params(Params.PARAM_LANG, Params.Language.EN)
                .params(Params.PARAM_OPTIONS, calcOptions(FIND_REPEAT_WORDS))
                .log().everything()
                .get(CheckTextApi.YANDEX_SPELLER_CHECKTEXT_URL)
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
                .get(CheckTextApi.YANDEX_SPELLER_CHECKTEXT_URL)
                .prettyPeek()
                .then()
                .contentType(ContentType.JSON)
                .content(Matchers.equalTo("[]"));
    }

    @Test
    public void wrongEnWordTest(){
        RestAssured
                .given()
                .params(Params.PARAM_TEXT, "korrect")
                .log().all()
                .get(CheckTextApi.YANDEX_SPELLER_CHECKTEXT_URL)
                .prettyPeek()
                .then()
                .content(Matchers.containsString("\"s\":[\"correct\",\"korrekt\",\"correctly\"]"));
    }

    @Test
    public void correctRuWordTest(){
        RestAssured
                .given()
                .params(Params.PARAM_TEXT, "проверить")
                .log().all()
                .get(CheckTextApi.YANDEX_SPELLER_CHECKTEXT_URL)
                .prettyPeek()
                .then()
                .contentType(ContentType.JSON)
                .content(Matchers.equalTo("[]"));
    }

    @Test
    public void wrongRuWordTest(){
        RestAssured
                .given()
                .params(Params.PARAM_TEXT, "праверить")
                .log().all()
                .get(CheckTextApi.YANDEX_SPELLER_CHECKTEXT_URL)
                .prettyPeek()
                .then()
                .content(Matchers.containsString("\"s\":[\"проверить\",\"проверит\",\"прверить\"]"));
    }

    @Test
    public void correctEnPlusRuWordTest(){
        RestAssured
                .given()
                .params(Params.PARAM_TEXT, "проверить text")
                .log().all()
                .get(CheckTextApi.YANDEX_SPELLER_CHECKTEXT_URL)
                .prettyPeek()
                .then()
                .contentType(ContentType.JSON)
                .content(Matchers.equalTo("[]"));
    }

    @Test
    public void wrongRuCorrectEnWordTest(){
        RestAssured
                .given()
                .params(Params.PARAM_TEXT, "праверить text")
                .log().all()
                .get(CheckTextApi.YANDEX_SPELLER_CHECKTEXT_URL)
                .prettyPeek()
                .then()
                .contentType(ContentType.JSON)
                .content(Matchers.containsString("проверить"));
    }

    @Test
    public void wrongEnPlusRuWordTest(){
        RestAssured
                .given()
                .params(Params.PARAM_TEXT, "проверить teext")
                .log().all()
                .get(CheckTextApi.YANDEX_SPELLER_CHECKTEXT_URL)
                .prettyPeek()
                .then()
                .contentType(ContentType.JSON)
                .content(Matchers.containsString("s\":[\"text\",\"texet\",\"etext\"]"));
    }

    @Test
    public void ruWordInEnTest(){
        RestAssured
                .given()
                .params(Params.PARAM_TEXT, "ghjdthrf")
                .log().all()
                .get(CheckTextApi.YANDEX_SPELLER_CHECKTEXT_URL)
                .prettyPeek()
                .then()
                .contentType(ContentType.JSON)
                .content(Matchers.containsString("проверка"));
    }

    @Test
    public void enWordInRuTest(){
        RestAssured
                .given()
                .params(Params.PARAM_TEXT, "сщккусе")
                .log().all()
                .get(CheckTextApi.YANDEX_SPELLER_CHECKTEXT_URL)
                .prettyPeek()
                .then()
                .contentType(ContentType.JSON)
                .content(Matchers.containsString("correct"));
    }

    @Test
    public void ignoreDigitsWithoutOptionsTest(){
        RestAssured
                .given()
                .params(Params.PARAM_TEXT, "word123")
                .log().all()
                .get(CheckTextApi.YANDEX_SPELLER_CHECKTEXT_URL)
                .prettyPeek()
                .then()
                .contentType(ContentType.JSON)
                .content(Matchers.containsString("word 123"));
    }

    @Test
    public void ignoreDigitsWithOptionsTest(){
        RestAssured
                .given()
                .params(Params.PARAM_TEXT, "проверить text")
                .params(Params.PARAM_OPTIONS, calcOptions(IGNORE_DIGITS))
                .log().all()
                .get(CheckTextApi.YANDEX_SPELLER_CHECKTEXT_URL)
                .prettyPeek()
                .then()
                .contentType(ContentType.JSON)
                .content(Matchers.equalTo("[]"));
    }

}
