package enums;

public class Params {
    public static final String PARAM_TEXT = "text";
    public static final String PARAM_OPTIONS = "options";
    public static final String PARAM_LANG = "lang";

    public enum Language {
        RU("ru"),
        EN("en"),
        UK("uk");

        private final String language;

        Language(String language) {
            this.language = language;
        }
    }

    public enum ErrorCodes {
        ERROR_UNKNOWN_WORD("1"),
        ERROR_REPEAT_WORD("2"),
        ERROR_CAPITALIZATION("3"),
        ERROR_TOO_MANY_ERRORS("4");

        private final String code;

        ErrorCodes(String code) {
            this.code = code;
        }
    }
}
