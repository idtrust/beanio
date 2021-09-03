package org.beanio.stream.xls;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriterContext {

  private final XSSFWorkbook workbook = new XSSFWorkbook();
  private final OutputStream outputStream;
  private XSSFSheet sheet;

  public ExcelWriterContext(OutputStream outputStream) {
    this.outputStream = outputStream;
  }

  public XSSFSheet getSheet() {
    return sheet;
  }

  public void createSheet(String name) {
    this.sheet = this.workbook.createSheet(name);
  }

  public OutputStream getOutputStream() {
    return outputStream;
  }

  public void write() throws IOException {
    workbook.write(outputStream);
  }

  public void close() throws IOException {
    workbook.close();
    outputStream.close();
  }
}
