package md.builder.xml;

public class BStaticProperties {
    public static AObject A_OBJECT;
    public static AObjectWithSetters A_OBJECT_WITH_SETTERS;

    private static String privateValue;

    public static void setPrivateValue(String text) {
        privateValue = text;
    }

    public static String getPrivateValue() {
        return privateValue;
    }
}
