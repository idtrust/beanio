package org.beanio.stream.xls;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.IntStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.beanio.stream.RecordIOException;
import org.beanio.stream.RecordReader;

public class ExcelReader implements RecordReader {

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
    Objects.requireNonNull(
        sheet, MessageFormat.format("sheet [%s] not found", config.getSheetName()));
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
    Object result = null;
    if (!Objects.isNull(row)) {
      final String[] cells = new String[row.getLastCellNum()];
      IntStream.range(0, cells.length)
          .forEach(
              i -> {
                Cell value = row.getCell(i);
                cells[i] = CONVERTERS.get(value.getCellTypeEnum()).apply(value);
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

  private static final Map<CellType, Function<Cell, String>> CONVERTERS;

  static {
    CONVERTERS = new ConcurrentHashMap<>();
    CONVERTERS.put(CellType.NUMERIC, cell -> String.valueOf(cell.getNumericCellValue()));
    CONVERTERS.put(CellType.BOOLEAN, cell -> String.valueOf(cell.getBooleanCellValue()));
    CONVERTERS.put(CellType.FORMULA, Cell::getStringCellValue);
    CONVERTERS.put(CellType.STRING, Cell::getStringCellValue);
    CONVERTERS.put(CellType.BLANK, Cell::getStringCellValue);
    CONVERTERS.put(CellType._NONE, cell -> null);
    CONVERTERS.put(CellType.ERROR, cell -> null);
  }
}
