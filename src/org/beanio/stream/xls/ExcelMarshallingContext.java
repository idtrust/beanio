package org.beanio.stream.xls;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.beanio.internal.parser.MarshallingContext;

public class ExcelMarshallingContext extends MarshallingContext {

  private final Map<Integer, String> records = new ConcurrentHashMap<>();

  public void insertCellValue(int cellNumber, String text) {
    records.put(cellNumber, text);
  }

  @Override
  public void clear() {
    super.clear();
    this.records.clear();
  }

  @Override
  protected Object getRecordObject() {
    int maxField = records.keySet().stream().max(Comparator.naturalOrder()).orElse(0);
    String[] record = new String[maxField + 1];
    records.forEach((k, v) -> record[k] = v);
    return record;
  }
}
