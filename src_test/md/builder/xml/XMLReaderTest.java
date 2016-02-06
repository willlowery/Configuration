package md.builder.xml;

import md.builder.Builder;
import md.builder.NoOpErrorHandler;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

import static md.builder.xml.AStaticProperties.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class XMLReaderTest {


    @Test
    public void testStaticPropertiesAreSet() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        new XMLReader(new Builder(new NoOpErrorHandler()), this.getClass().getResourceAsStream("test.xml")).execute();

        assertThat(TO_SET_STRING, is("A String Value"));
        assertThat(TO_SET_CHARACTER, is('A'));
        assertThat(TO_SET_CHARACTER_P, is('B'));
        assertThat(TO_SET_BYTE, is(new Byte("1")));
        assertThat(TO_SET_BYTE_P, is(new Byte("2")));
        assertThat(TO_SET_SHORT, is(new Short("3")));
        assertThat(TO_SET_SHORT_P, is(new Short("4")));
        assertThat(TO_SET_INTEGER, is(new Integer("5")));
        assertThat(TO_SET_INTEGER_P, is(6));
        assertThat(TO_SET_LONG, is(new Long("7")));
        assertThat(TO_SET_LONG_P, is(8L));
        assertThat(TO_SET_FLOAT, is(new Float("8.1")));
        assertThat(TO_SET_FLOAT_P, is(8.2F));
        assertThat(TO_SET_DOUBLE, is(new Double("8.3")));
        assertThat(TO_SET_DOUBLE_P, is(8.4));

        assertThat(BStaticProperties.A_OBJECT.getaString(), is("A String Value"));
        assertThat(BStaticProperties.A_OBJECT.getbString(), is("Second Arg"));
        assertThat(BStaticProperties.A_OBJECT_WITH_SETTERS.getValue(), is("Value"));
        assertThat(BStaticProperties.A_OBJECT_WITH_SETTERS.getAnotherValue(), is("Another Value"));
        assertThat(BStaticProperties.A_OBJECT_WITH_SETTERS.publicField, is("A String Value"));
        assertThat(BStaticProperties.getPrivateValue(), is("A String Value"));

    }


}