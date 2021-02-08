package api.request_options;

public enum Options {
    IGNORE_DIGITS(2),
    IGNORE_URLS(4),
    FIND_REPEAT_WORDS(8),
    IGNORE_CAPITALIZATION(512);

    private int code;

    Options(int code) {
        this.code = code;
    }

    public static String calcOptions(Options... options) {
        int sum = 0;
        for (Options element : options) {
            sum += element.code;
        }
        return String.valueOf(sum);
    }


}
