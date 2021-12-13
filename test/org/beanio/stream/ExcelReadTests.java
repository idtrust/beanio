package org.beanio.stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.beanio.BeanReader;
import org.beanio.BeanioInput;
import org.beanio.StreamFactory;
import org.beanio.builder.FieldBuilder;
import org.beanio.builder.RecordBuilder;
import org.beanio.builder.StreamBuilder;
import org.beanio.internal.parser.Stream;
import org.beanio.internal.util.TypeHandlerFactory;
import org.beanio.stream.xls.ExcelParserFactory;
import org.beanio.stream.xls.ExcelRecordParserFactory;
import org.junit.Assert;
import org.junit.Test;

public class ExcelReadTests {

  @Test
  public void shouldReadSuccessfully() throws IOException {
    StreamFactory sf = StreamFactory.newInstance();
    sf.load(new File("test/org/beanio/stream/xls-config.xml"));
    InputStream is = new FileInputStream("test/org/beanio/stream/test-reading.xlsx");
    BeanReader reader =
        sf.createReader("xls-test", BeanioInput.ofInputStream(is), Locale.getDefault());
    assertReader(reader);
  }

  @Test
  public void shouldReadWithStreamBuilder() throws FileNotFoundException {
    ExcelParserFactory f = new ExcelParserFactory();
    f.setTypeHandlerFactory(new TypeHandlerFactory());
    f.setClassLoader(getClass().getClassLoader());
    StreamBuilder sb =
        new StreamBuilder("xls-test", "xls")
            .parser(new ExcelRecordParserFactory())
            .addRecord(
                new RecordBuilder("header", HashMap.class)
                    .minOccurs(1)
                    .maxOccurs(1)
                    .addField(new FieldBuilder("header1").at(0).type(String.class))
                    .addField(new FieldBuilder("header2").at(1).type(String.class))
                    .addField(new FieldBuilder("header3").at(2).type(String.class)))
            .addRecord(
                new RecordBuilder("employee", HashMap.class)
                    .addField(new FieldBuilder("name").at(0).type(String.class))
                    .addField(new FieldBuilder("age").at(1).type(Integer.class))
                    .addField(new FieldBuilder("income").at(2).type(String.class)));

    Stream s = f.createStream(sb.build());
    BeanReader reader =
        s.createBeanReader(
            BeanioInput.ofInputStream(
                new FileInputStream("test/org/beanio/stream/test-reading.xlsx")),
            Locale.getDefault());

    assertReader(reader);
  }

  @Test
  public void shouldReadDoubleAsString() throws FileNotFoundException {
    ExcelParserFactory f = new ExcelParserFactory();
    f.setTypeHandlerFactory(new TypeHandlerFactory());
    f.setClassLoader(getClass().getClassLoader());
    StreamBuilder sb =
        new StreamBuilder("xls-test", "xls")
            .parser(new ExcelRecordParserFactory())
            .addRecord(
                new RecordBuilder("header", HashMap.class)
                    .minOccurs(1)
                    .maxOccurs(1)
                    .addField(new FieldBuilder("header1").at(0).type(String.class))
                    .addField(new FieldBuilder("header2").at(1).type(String.class))
                    .addField(new FieldBuilder("header3").at(2).type(String.class)))
            .addRecord(
                new RecordBuilder("employee", HashMap.class)
                    .addField(new FieldBuilder("name").at(0).type(String.class))
                    .addField(new FieldBuilder("age").at(1).type(Integer.class))
                    .addField(new FieldBuilder("income").at(2).type(String.class)));

    Stream s = f.createStream(sb.build());
    BeanReader reader =
        s.createBeanReader(
            BeanioInput.ofInputStream(
                new FileInputStream("test/org/beanio/stream/test-reading-double.xlsx")),
            Locale.getDefault());

    reader.skip(1);

    Map map = (Map) reader.read();
    assertTrue(map instanceof Map);
    assertEquals("ricardo", map.get("name"));
    assertEquals(35, map.get("age"));
    assertEquals("145000000000000000", map.get("income"));
  }


  private void assertReader(BeanReader reader) {
    Object map = reader.read();
    assertTrue(map instanceof Map);
    assertEquals("NOME", ((Map) map).get("header1"));
    assertEquals("IDADE", ((Map) map).get("header2"));
    assertEquals("SALARIO", ((Map) map).get("header3"));

    map = reader.read();
    assertTrue(map instanceof Map);
    assertEquals("ricardo", ((Map) map).get("name"));
    assertEquals(33, ((Map) map).get("age"));
    assertEquals("123.45", ((Map) map).get("income"));

    map = reader.read();
    assertTrue(map instanceof Map);
    assertEquals("rodrigo", ((Map) map).get("name"));
    assertEquals(35, ((Map) map).get("age"));
    assertEquals("456.67", ((Map) map).get("income"));

    map = reader.read();
    Assert.assertNull(map);
  }
}
