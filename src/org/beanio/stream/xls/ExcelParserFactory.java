package org.beanio.stream.xls;

import java.util.Comparator;
import java.util.stream.Stream;
import org.beanio.internal.compiler.ParserFactorySupport;
import org.beanio.internal.config.FieldConfig;
import org.beanio.internal.config.RecordConfig;
import org.beanio.internal.config.StreamConfig;
import org.beanio.internal.parser.FieldFormat;
import org.beanio.internal.parser.RecordFormat;
import org.beanio.internal.parser.StreamFormat;
import org.beanio.stream.RecordParserFactory;

public class ExcelParserFactory extends ParserFactorySupport {

  @Override
  protected StreamFormat createStreamFormat(StreamConfig config) {
    return new ExcelStreamFormat(config);
  }

  @Override
  protected RecordFormat createRecordFormat(RecordConfig config) {
    Integer maxCell = getFieldPositions(config).max(Comparator.naturalOrder()).orElse(0);
    return new ExcelRecordFormat(maxCell + 1);
  }

  private Stream<Integer> getFieldPositions(RecordConfig config) {
    return config.getChildren().stream()
        .filter(p -> p instanceof FieldConfig)
        .map(p -> ((FieldConfig) p).getPosition());
  }

  @Override
  protected FieldFormat createFieldFormat(FieldConfig config, Class<?> type) {
    ExcelFieldFormat f = new ExcelFieldFormat(config.getPosition());
    f.setMaxLength(config.getMaxLength());
    return f;
  }

  @Override
  protected RecordParserFactory getDefaultRecordParserFactory() {
    return new ExcelRecordParserFactory();
  }
}
