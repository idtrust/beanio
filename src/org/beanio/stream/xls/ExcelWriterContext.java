package org.beanio.stream.xls;

import java.io.IOException;
import java.io.Writer;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.beanio.stream.xls.util.WriterOutputStream;

public class ExcelWriterContext {

  private final XSSFWorkbook workbook = new XSSFWorkbook();
  private final Writer writer;
  private XSSFSheet sheet;

  public ExcelWriterContext(Writer writer) {
    this.writer = writer;
  }

  public XSSFSheet getSheet() {
    return sheet;
  }

  public void createSheet(String name) {
    this.sheet = this.workbook.createSheet(name);
  }

  public Writer getWriter() {
    return writer;
  }

  public void write() throws IOException {
    workbook.write(new WriterOutputStream(writer));
  }

  public void close() throws IOException {
    workbook.close();
    writer.close();
  }
}
