package org.beanio.stream.xls;

import java.util.Objects;
import java.util.Properties;
import org.beanio.BeanioInput;
import org.beanio.BeanioOutput;
import org.beanio.internal.config.StreamConfig;
import org.beanio.internal.parser.MarshallingContext;
import org.beanio.internal.parser.StreamFormatSupport;
import org.beanio.internal.parser.UnmarshallingContext;
import org.beanio.stream.RecordReader;
import org.beanio.stream.RecordWriter;

public class ExcelStreamFormat extends StreamFormatSupport {

  public ExcelStreamFormat(StreamConfig config) {
    super();
    this.setName(config.getName());
    this.setRecordParserFactory(new ExcelRecordParserFactory(getProperties(config)));
  }

  private Properties getProperties(StreamConfig config) {
    return Objects.isNull(config.getParserFactory())
        ? new Properties()
        : config.getParserFactory().getProperties();
  }

  @Override
  public RecordReader createRecordReader(BeanioInput in) {
    return ((ExcelRecordParserFactory) getRecordParserFactory()).createReader(in.getInputStream());
  }

  @Override
  public RecordWriter createRecordWriter(BeanioOutput out) {
    return ((ExcelRecordParserFactory) getRecordParserFactory())
        .createWriter(out.getOutputStream());
  }

  @Override
  public UnmarshallingContext createUnmarshallingContext() {
    return new ExcelUnmarshallingContext();
  }

  @Override
  public MarshallingContext createMarshallingContext(boolean streaming) {
    return new ExcelMarshallingContext();
  }
}
