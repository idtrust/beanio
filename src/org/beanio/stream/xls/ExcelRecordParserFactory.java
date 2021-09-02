package org.beanio.stream.xls;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;
import org.beanio.stream.RecordMarshaller;
import org.beanio.stream.RecordParserFactory;
import org.beanio.stream.RecordReader;
import org.beanio.stream.RecordUnmarshaller;
import org.beanio.stream.RecordWriter;

public class ExcelRecordParserFactory extends ExcelParserConfiguration
    implements RecordParserFactory {

  private static final String FIRST_SHEET = "<FIRST>";

  public ExcelRecordParserFactory() {
    this(new Properties());
  }

  public ExcelRecordParserFactory(Properties props) {
    parseProps(props);
  }

  private void parseProps(Properties props) {
    setSheetName(props == null ? FIRST_SHEET : props.getProperty("sheetName", FIRST_SHEET));
  }

  @Override
  public void init() throws IllegalArgumentException {}

  @Override
  public RecordReader createReader(Reader in) throws IllegalArgumentException {
    try {
      return new ExcelReader(in, this);
    } catch (IOException e) {
      throw new IllegalArgumentException("could not read file: " + e.getMessage(), e);
    }
  }

  @Override
  public RecordWriter createWriter(Writer out) throws IllegalArgumentException {
    return new ExcelWriter(new ExcelWriterContext(out), this);
  }

  @Override
  public RecordMarshaller createMarshaller() throws IllegalArgumentException {
    throw new IllegalArgumentException("marshaller is not supported");
  }

  @Override
  public RecordUnmarshaller createUnmarshaller() throws IllegalArgumentException {
    throw new IllegalArgumentException("unmarshaller is not supported");
  }
}
