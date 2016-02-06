package md.builder;

public interface ErrorHandler {
    void unableToSetField(String objectName, String fieldName, String value, Exception ex);

    void unableToSetStaticField(String classpath, String fieldName, String value, Exception ex);

    void unableToCallMethod(String objectName, String methodName, String[] parameters, Exception ex);

    void unableToCallStaticMethod(String classpath, String methodName, String[] parameters, Exception e);

    void noMethodFound(String objectName, String methodName, String[] parameters);

    void noStaticMethodFound(String classpath, String methodName, String[] parameters);

    void noConstructorFound(String classpath, String name, String[] parameters);

    void unableToConstructObject(String classpath, String name, String[] parameters, Exception e);
}
