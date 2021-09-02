package org.beanio.stream.xls;

import org.beanio.internal.parser.FieldFormat;
import org.beanio.internal.parser.MarshallingContext;
import org.beanio.internal.parser.UnmarshallingContext;

public class ExcelFieldFormat implements FieldFormat {

  private final int cellNumber;
  private int maxLength;

  public ExcelFieldFormat(int cellNumber) {
    this.cellNumber = cellNumber;
  }

  public void setMaxLength(int maxLength) {
    this.maxLength = maxLength;
  }

  @Override
  public String extract(UnmarshallingContext context, boolean reportErrors) {
    return ((ExcelUnmarshallingContext) context).getValueAt(cellNumber);
  }

  @Override
  public boolean insertValue(MarshallingContext context, Object value) {
    return false;
  }

  @Override
  public void insertField(MarshallingContext context, String text) {
    ((ExcelMarshallingContext) context).insertCellValue(this.cellNumber, text);
  }

  @Override
  public int getSize() {
    return Math.max(0, this.maxLength);
  }

  @Override
  public boolean isNillable() {
    return false;
  }

  @Override
  public boolean isLazy() {
    return false;
  }
}
