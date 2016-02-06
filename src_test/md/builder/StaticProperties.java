package md.builder;

public class StaticProperties {

    public static Properties PROPERTIES;

    private static boolean noArgsCalled;
    private static String STRING_VAL;
    private static int INT_VAL;

    public static void noArgs() {
        noArgsCalled = true;
    }

    public static boolean noArgsCalled() {
        return noArgsCalled;
    }

    public static void setValue(String value) {
        STRING_VAL = value;
    }

    public static void setInt(int value) {
        INT_VAL = value;
    }

    public static int getInt() {
        return INT_VAL;
    }

    public static String getValue() {
        return STRING_VAL;
    }

}
