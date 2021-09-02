package org.beanio.stream.xls;

import java.io.IOException;
import java.util.Objects;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.beanio.stream.RecordWriter;

public class ExcelWriter implements RecordWriter {

  private final ExcelWriterContext context;
  private transient int rowNumber;

  public ExcelWriter(ExcelWriterContext context, ExcelParserConfiguration configuration) {
    this.context = context;
    context.createSheet(
        Objects.requireNonNull(configuration.getSheetName(), "Workbook sheet name is required"));
  }

  @Override
  public void write(Object record) throws IOException {
    Row row = context.getSheet().createRow(rowNumber++);
    int cellnum = 0;
    for (String item : ((String[]) record)) {
      Cell cell = row.createCell(cellnum++);
      cell.setCellValue(item);
    }
  }

  @Override
  public void flush() throws IOException {
    this.context.write();
  }

  @Override
  public void close() throws IOException {
    this.context.close();
  }
}
