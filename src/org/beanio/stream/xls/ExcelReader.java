package org.beanio.stream.xls;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.beanio.stream.RecordIOException;
import org.beanio.stream.RecordReader;
import org.beanio.stream.xls.util.ConverterUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.stream.IntStream;

public class ExcelReader implements RecordReader {

    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  private transient int lineNumber;
  private final InputStream inputStream;
  private final ExcelParserConfiguration config;
  private final Sheet sheet;

  public ExcelReader(InputStream in, ExcelParserConfiguration config) throws IOException {
    this.inputStream = in;
    this.config = config;
    this.sheet = getSheet();
  }

  private Sheet getSheet() throws IOException {
    XSSFWorkbook wb = new XSSFWorkbook(inputStream);
    Sheet sheet = getSheetFromWorkbook(wb);
    Objects.requireNonNull(sheet, String.format("sheet [%s] not found", config.getSheetName()));
    return sheet;
  }

  private Sheet getSheetFromWorkbook(XSSFWorkbook wb) {
    Sheet sheet;
    if (config.getSheetName().equals("<FIRST>")) {
      sheet = wb.iterator().next();
    } else {
      sheet =
          wb.getSheet(Objects.requireNonNull(config.getSheetName(), "sheetName must be provided"));
    }
    return sheet;
  }

  @Override
  public Object read() throws RecordIOException {
    Row row = sheet.getRow(lineNumber++);
    if (row == null) {
      return null;
    }
    Object result = null;
    int lastCellNum = ConverterUtils.getRowLastCellNum(row);

    if (lastCellNum >= 0) {
      final String[] cells = new String[lastCellNum];
      IntStream.range(0, cells.length)
          .forEach(
              i -> {
                Cell value = row.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (value.getCellTypeEnum().equals(CellType.NUMERIC) && DateUtil.isCellDateFormatted(value)) {
                    cells[i] = FORMAT.format(value.getDateCellValue());
                } else {
                    cells[i] = ConverterUtils.getConverterFor(value.getCellTypeEnum()).apply(value);
                }
              });
      result = cells;
    }
    return result;
  }

  @Override
  public void close() throws IOException {
    this.inputStream.close();
  }

  @Override
  public int getRecordLineNumber() {
    return lineNumber;
  }

  @Override
  public String getRecordText() {
    return "";
  }


}
