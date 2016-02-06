package md.builder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

public class Builder {

    ErrorHandler handler;
    HashMap<Class<?>, Parser> parser = new HashMap<>();
    HashMap<String, Object> objects = new HashMap<>();

    public Builder(ErrorHandler handler) {
        this.handler = handler;

        parser.put(String.class, toParse -> toParse);
        parser.put(Character.class, toParse -> toParse.charAt(0));
        parser.put(char.class, toParse -> toParse.charAt(0));
        parser.put(Byte.class, Byte::parseByte);
        parser.put(byte.class, Byte::parseByte);
        parser.put(Integer.class, Integer::parseInt);
        parser.put(int.class, Integer::parseInt);
        parser.put(Short.class, Short::parseShort);
        parser.put(short.class, Short::parseShort);
        parser.put(Float.class, Float::parseFloat);
        parser.put(float.class, Float::parseFloat);
        parser.put(Double.class, Double::parseDouble);
        parser.put(double.class, Double::parseDouble);
        parser.put(Long.class, Long::parseLong);
        parser.put(long.class, Long::parseLong);
    }

    public void setStaticField(String classpath, String propertyName, String value) {
        try {
            Field field = Class.forName(classpath).getField(propertyName);
            field.set(null, getValue(field, value));
        } catch (Exception e) {
            handler.unableToSetStaticField(classpath, propertyName, value, e);
        }
    }

    public void setField(String objectName, String fieldName, String value) {
        if (!objects.containsKey(objectName)) {
            return;
        }
        try {
            Object object = objects.get(objectName);
            Field field = object.getClass().getField(fieldName);
            field.set(object, getValue(field, value));
        } catch (Exception e) {
            handler.unableToSetField(objectName, fieldName, value, e);
        }
    }

    private Object getValue(Field field, String value) {
        return getValue(field.getType(), value);
    }

    private Object getValue(Class<?> type, String value) {
        if (objects.containsKey(value))
            return objects.get(value);
        else if (parser.containsKey(type))
            return parser.get(type).parse(value);
        else
            throw new UnknownValueException();
    }


    public void callMethod(String objectName, String methodName, String... parameters) {
        if (!objects.containsKey(objectName)) {
            return;
        }
        Object object = objects.get(objectName);
        try {
            Optional<Method> result = findMethod(object.getClass(), methodName, parameters);
            if (result.isPresent()) {
                Method method = result.get();
                method.invoke(object, buildParameters(method.getParameterTypes(), parameters));
            } else {
                handler.noMethodFound(objectName, methodName, parameters);
            }
        } catch (Exception e) {
            handler.unableToCallMethod(objectName, methodName, parameters, e);
        }
    }

    public void callStaticMethod(String classpath, String methodName, String... parameters) {
        try {
            Optional<Method> result = findMethod(Class.forName(classpath), methodName, parameters);
            if (result.isPresent()) {
                Method method = result.get();
                method.invoke(null, buildParameters(method.getParameterTypes(), parameters));
            } else {
                handler.noStaticMethodFound(classpath, methodName, parameters);
            }
        } catch (Exception e) {
            handler.unableToCallStaticMethod(classpath, methodName, parameters, e);
        }
    }

    private Optional<Method> findMethod(Class<?> objectType, String methodName, String[] parameters) {
        return Stream.of(objectType.getMethods())
                .filter(method -> method.getName().equals(methodName))
                .filter(method -> method.getParameterCount() == parameters.length)
                .filter(method ->
                        {
                            Class<?>[] parameterTypes = method.getParameterTypes();
                            for (int i = 0; i < parameters.length; i++) {
                                if (!(parser.containsKey(parameterTypes[i]) || objects.containsKey(parameters[i]))) {
                                    return false;
                                }
                            }
                            return true;
                        }
                ).findFirst();
    }

    public void constructObject(String classpath, String name, String... args) {
        try {
            Class<?> toWorkOn = Class.forName(classpath);
            Optional<Constructor<?>> result = Stream.of(toWorkOn.getConstructors())
                    .filter((con) -> con.getParameterCount() == args.length)
                    .filter((con) ->
                            {
                                Class<?>[] parameterTypes = con.getParameterTypes();
                                for (int i = 0; i < args.length; i++) {
                                    if (!(parser.containsKey(parameterTypes[i]) || objects.containsKey(args[i]))) {
                                        return false;
                                    }
                                }
                                return true;
                            }
                    ).findFirst();
            if (result.isPresent()) {
                Constructor<?> constructor = result.get();
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                objects.put(name, constructor.newInstance(buildParameters(parameterTypes, args)));
            } else {
                handler.noConstructorFound(classpath, name, args);
            }
        } catch (Exception e) {
            handler.unableToConstructObject(classpath, name, args, e);
        }
    }

    private Object[] buildParameters(Class<?>[] parameterTypes, String[] args) {
        Object[] results = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            String arg = args[i];
            Class<?> aClass = parameterTypes[i];
            results[i] = getValue(aClass, arg);
        }
        return results;
    }

    private class UnknownValueException extends RuntimeException {
    }
}
