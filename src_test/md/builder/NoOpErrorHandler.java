package md.builder;

public class NoOpErrorHandler implements ErrorHandler {
    @Override
    public void unableToSetField(String objectName, String fieldName, String value, Exception ex) {

    }

    @Override
    public void unableToSetStaticField(String classpath, String fieldName, String value, Exception ex) {
    }

    @Override
    public void unableToCallMethod(String objectName, String methodName, String[] parameters, Exception ex) {

    }

    @Override
    public void unableToCallStaticMethod(String classpath, String methodName, String[] parameters, Exception e) {

    }

    @Override
    public void noMethodFound(String objectName, String methodName, String[] parameters) {

    }

    @Override
    public void noStaticMethodFound(String classpath, String methodName, String[] parameters) {

    }

    @Override
    public void noConstructorFound(String classpath, String name, String[] parameters) {

    }

    @Override
    public void unableToConstructObject(String classpath, String name, String[] parameters, Exception e) {

    }
}
