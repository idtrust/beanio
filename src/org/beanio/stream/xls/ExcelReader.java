package org.beanio.stream.xls;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.IntStream;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.beanio.stream.RecordIOException;
import org.beanio.stream.RecordReader;

public class ExcelReader implements RecordReader {

  private static final SimpleDateFormat DATE_FORMAT =
      new SimpleDateFormat("yyyy-MM-dd", LocaleUtil.getUserLocale());

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
    if (row == null) {
      return null;
    }
    Object result = null;
    int lastCellNum = getLastCellNum(row);

    if (lastCellNum >= 0) {
      final String[] cells = new String[lastCellNum];
      IntStream.range(0, cells.length)
          .forEach(
              i -> {
                Cell value = row.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cells[i] = CONVERTERS.get(value.getCellTypeEnum()).apply(value);
              });
      result = cells;
    }
    return result;
  }

  private int getLastCellNum(Row row) {
    for (int i = row.getLastCellNum(); i > 0; i--) {
      Cell cell = row.getCell(i - 1);
      String val = CONVERTERS.get(cell.getCellTypeEnum()).apply(cell);
      if (!val.trim().isEmpty()) {
        return i;
      }
    }

    // se todas as celulas estiverem vazias, não é um registro valido e deve ser ignorado
    return -1;
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
    CONVERTERS.put(
        CellType.NUMERIC,
        cell -> {

          // Se for um valor de data valida, retorna a data em formato ISO
          if (HSSFDateUtil.isCellDateFormatted(cell)) {
            DATE_FORMAT.setTimeZone(LocaleUtil.getUserTimeZone());
            return DATE_FORMAT.format(cell.getDateCellValue());
          }

          return NumberToTextConverter.toText(cell.getNumericCellValue());
        });
    CONVERTERS.put(CellType.BOOLEAN, cell -> String.valueOf(cell.getBooleanCellValue()));
    CONVERTERS.put(CellType.FORMULA, Cell::getStringCellValue);
    CONVERTERS.put(CellType.STRING, Cell::getStringCellValue);
    CONVERTERS.put(CellType.BLANK, Cell::getStringCellValue);
    CONVERTERS.put(CellType._NONE, cell -> null);
    CONVERTERS.put(CellType.ERROR, cell -> null);
  }
}
