package md.builder;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class BuilderTest {

    public static String TO_SET_STRING;
    public static Character TO_SET_CHARACTER;
    public static char TO_SET_CHARACTER_P;
    public static Byte TO_SET_BYTE;
    public static byte TO_SET_BYTE_P;
    public static Short TO_SET_SHORT;
    public static short TO_SET_SHORT_P;
    public static Integer TO_SET_INTEGER;
    public static int TO_SET_INTEGER_P;
    public static Long TO_SET_LONG;
    public static long TO_SET_LONG_P;
    public static Float TO_SET_FLOAT;
    public static float TO_SET_FLOAT_P;
    public static Double TO_SET_DOUBLE;
    public static double TO_SET_DOUBLE_P;
    public static Date TO_NOT_SET_DATE;

    @Test
    public void given_parsable_input_expects_static_fields_set() {
        String classpath = "md.builder.BuilderTest";

        builder.setStaticField(classpath, "TO_SET_STRING", "SET");
        builder.setStaticField(classpath, "TO_SET_CHARACTER", "A");
        builder.setStaticField(classpath, "TO_SET_CHARACTER_P", "A");
        builder.setStaticField(classpath, "TO_SET_BYTE", "0");
        builder.setStaticField(classpath, "TO_SET_BYTE_P", "0");
        builder.setStaticField(classpath, "TO_SET_SHORT", "1");
        builder.setStaticField(classpath, "TO_SET_SHORT_P", "1");
        builder.setStaticField(classpath, "TO_SET_INTEGER", "2");
        builder.setStaticField(classpath, "TO_SET_INTEGER_P", "2");
        builder.setStaticField(classpath, "TO_SET_LONG", "3");
        builder.setStaticField(classpath, "TO_SET_LONG_P", "3");
        builder.setStaticField(classpath, "TO_SET_FLOAT", "4.5");
        builder.setStaticField(classpath, "TO_SET_FLOAT_P", "4.5");
        builder.setStaticField(classpath, "TO_SET_DOUBLE", "5.5");
        builder.setStaticField(classpath, "TO_SET_DOUBLE_P", "5.5");

        assertThat(TO_SET_STRING, is("SET"));
        assertThat(TO_SET_CHARACTER, is('A'));
        assertThat(TO_SET_CHARACTER_P, is('A'));
        assertThat(TO_SET_BYTE, is((byte) 0));
        assertThat(TO_SET_BYTE_P, is((byte) 0));
        assertThat(TO_SET_SHORT, is((short) 1));
        assertThat(TO_SET_SHORT_P, is((short) 1));
        assertThat(TO_SET_INTEGER, is(2));
        assertThat(TO_SET_INTEGER_P, is(2));
        assertThat(TO_SET_LONG, is(3L));
        assertThat(TO_SET_LONG_P, is(3L));
        assertThat(TO_SET_FLOAT, is(4.5F));
        assertThat(TO_SET_FLOAT_P, is(4.5F));
        assertThat(TO_SET_DOUBLE, is(5.5D));
        assertThat(TO_SET_DOUBLE_P, is(5.5D));
    }

    @Test
    public void test_bad_input_does_not_stop_run() {
        builder.setStaticField("Not a valid class path", "TO_SET_STRING", "SET");
        builder.setStaticField("md.builder.BuilderTest", "TO_SET_INTEGER", "not an int");
        builder.setStaticField("md.builder.BuilderTest", "TO_NOT_SET_DATE", "2015-10-10");
    }

    public static Object OBJECT_TO_SET;

    @Test
    public void test_object_construction() {
        builder.constructObject("java.lang.Object", "newObject");
        builder.setStaticField("md.builder.BuilderTest", "OBJECT_TO_SET", "newObject");

        builder.constructObject("java.lang.String", "newString", "value");
        builder.setStaticField("md.builder.BuilderTest", "TO_SET_STRING", "newString");

        assertNotNull(OBJECT_TO_SET);
        assertThat(TO_SET_STRING, is("value"));
    }

    @Test
    public void test_static_method_calls_expects_values() {
        builder.callStaticMethod("md.builder.StaticProperties", "noArgs");
        builder.callStaticMethod("md.builder.StaticProperties", "setValue", "String");
        builder.callStaticMethod("md.builder.StaticProperties", "setInt", "1");

        assertThat(StaticProperties.noArgsCalled(), is(true));
        assertThat(StaticProperties.getValue(), is("String"));
        assertThat(StaticProperties.getInt(), is(1));
    }

    @Test
    public void test() {
        builder.constructObject("md.builder.Properties", "$myProps");
        builder.setField("$myProps", "stringValue", "value");
        builder.callMethod("$myProps", "setStringSetter", "value");
        builder.setStaticField("md.builder.StaticProperties", "PROPERTIES", "$myProps");

        assertThat(StaticProperties.PROPERTIES.stringValue, is("value"));
        assertThat(StaticProperties.PROPERTIES.getStringSetter(), is("value"));

    }

    @Before
    public void setup() {
        builder = new Builder(new NoOpErrorHandler());
    }

    Builder builder;
}

