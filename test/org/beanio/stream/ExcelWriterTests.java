package org.beanio.stream;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.beanio.stream.xls.util.RawOutputStreamWriterAdapter;
import org.junit.Test;

public class ExcelWriterTests {

  @Test
  public void shouldWriteExcelFile() throws IOException {

    StreamFactory sf = StreamFactory.newInstance();
    sf.load(new File("test/org/beanio/stream/xls-config.xml"));
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    BeanWriter writer = sf.createWriter("xls-test", new RawOutputStreamWriterAdapter(baos));
    try {
      writer.write("header", new HashMap<String, Object>());
      writer.write("employee", createEmployee("ricardo", 33, 123.45));
      writer.write("employee", createEmployee("rodrigo", 35, 456.67));
    } finally {
      writer.flush();
      writer.close();
    }

    Sheet xlsSheet = getXlsSheetFromFile(baos.toByteArray(), "sheet1");

    assertEquals(2, xlsSheet.getLastRowNum());

    // header row
    Row row = xlsSheet.getRow(0);
    assertEquals("NOME", row.getCell(0).getStringCellValue());
    assertEquals("IDADE", row.getCell(1).getStringCellValue());
    assertEquals("SALARIO", row.getCell(2).getStringCellValue());

    // first row
    row = xlsSheet.getRow(1);
    assertEquals("ricardo", row.getCell(0).getStringCellValue());
    assertEquals("33", row.getCell(1).getStringCellValue());
    assertEquals("123.45", row.getCell(2).getStringCellValue());

    // second row
    row = xlsSheet.getRow(2);
    assertEquals("rodrigo", row.getCell(0).getStringCellValue());
    assertEquals("35", row.getCell(1).getStringCellValue());
    assertEquals("456.67", row.getCell(2).getStringCellValue());
  }

  private Sheet getXlsSheetFromFile(byte[] bytes, String sheetName) throws IOException {
    XSSFWorkbook wb = new XSSFWorkbook(new ByteArrayInputStream(bytes));
    return wb.getSheet(sheetName);
  }

  private Object createEmployee(String name, int age, double income) {
    return new HashMap<String, Object>() {
      {
        put("name", name);
        put("age", age);
        put("income", income);
      }
    };
  }
}
