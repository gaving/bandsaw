package net.brokentrain.bandsaw.log4j;

public interface Log4jItem {

    public static final int LEVEL = 0;
    public static final int CATEGORY = 1;
    public static final int MESSAGE = 2;
    public static final int LINE_NUMBER = 3;
    public static final int DATE = 4;
    public static final int NDC = 5;
    public static final int THROWABLE = 6;
    public static final int UNKNOWN = 99;

    public static final String LABEL_LEVEL = "Level";
    public static final String LABEL_CATEGORY = "Category";
    public static final String LABEL_MESSAGE = "Message";
    public static final String LABEL_LINE_NUMBER = "Line";
    public static final String LABEL_DATE = "Date";
    public static final String LABEL_NDC = "NDC";
    public static final String LABEL_THROWABLE = "Throwable";
    public static final String LABEL_UNKNOWN = "Unknown";

    public String getText();
}
