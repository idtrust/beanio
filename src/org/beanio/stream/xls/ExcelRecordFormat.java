package org.beanio.stream.xls;

import org.beanio.internal.parser.RecordFormat;
import org.beanio.internal.parser.UnmarshallingContext;

public class ExcelRecordFormat implements RecordFormat {

  private final Integer maxCells;

  public ExcelRecordFormat(Integer maxCells) {
    this.maxCells = maxCells;
  }

  @Override
  public boolean matches(UnmarshallingContext context) {
    return ((ExcelUnmarshallingContext) context).getTotalCells() >= maxCells;
  }

  @Override
  public void validate(UnmarshallingContext context) {}
}
