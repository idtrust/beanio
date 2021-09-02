package org.beanio.stream.xls;

import java.util.Objects;
import java.util.Properties;
import org.beanio.internal.config.StreamConfig;
import org.beanio.internal.parser.MarshallingContext;
import org.beanio.internal.parser.StreamFormatSupport;
import org.beanio.internal.parser.UnmarshallingContext;

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
  public UnmarshallingContext createUnmarshallingContext() {
    return new ExcelUnmarshallingContext();
  }

  @Override
  public MarshallingContext createMarshallingContext(boolean streaming) {
    return new ExcelMarshallingContext();
  }
}
