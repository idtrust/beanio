package org.beanio.stream.xls;

import org.beanio.internal.parser.UnmarshallingContext;

public class ExcelUnmarshallingContext extends UnmarshallingContext {

  private String[] values;

  public int getTotalCells() {
    return values.length;
  }

  public String getValueAt(int index) {
    return index < values.length ? values[index] : "";
  }

  @Override
  public void setRecordValue(Object value) {
    this.values = (String[]) value;
  }
}
