package api;

import enums.Params;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;

public class CheckTextApi {

    //    Resource URL
    public static final String YANDEX_SPELLER_CHECKTEXT_URL =
            "https://speller.yandex.net/services/spellservice.json/checkText";

    //    Check text method parameters to send in request
    private final HashMap<String, String> params = new HashMap<>();

    //    Request builder method
    public static CheckTextApiBuilder with() {
        return new CheckTextApiBuilder(new CheckTextApi());
    }

    //    Request bilder pattern
    public static class CheckTextApiBuilder {
        CheckTextApi checktext;

        private CheckTextApiBuilder(CheckTextApi checktext) {
            this.checktext = checktext;
        }

        public CheckTextApiBuilder text(String text) {
            checktext.params.put(Params.PARAM_TEXT, text);
            return this;
        }

        public CheckTextApiBuilder lang(String lang) {
            checktext.params.put(Params.PARAM_LANG, lang);
            return this;
        }

        public CheckTextApiBuilder optons(String options) {
            checktext.params.put(Params.PARAM_OPTIONS, options);
            return this;
        }

        //    Method get call with request and response console logging
        public Response callApi() {
            return RestAssured.with()
                    .queryParams(checktext.params)
                    .log().all()
                    .get(YANDEX_SPELLER_CHECKTEXT_URL)
                    .prettyPeek();
        }
    }
}
