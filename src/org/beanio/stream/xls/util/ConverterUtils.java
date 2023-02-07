package org.beanio.stream.xls.util;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.util.LocaleUtil;

public class ConverterUtils {

  private static final SimpleDateFormat DATE_FORMAT =
      new SimpleDateFormat("yyyy-MM-dd", LocaleUtil.getUserLocale());

  private static final Map<CellType, Function<Cell, String>> CONVERTERS;

  /**
   * Obtém o índice da última celula preenchida de uma linha
   * @param row
   * @return
   */
  public static int getRowLastCellNum(Row row) {
    for (int i = row.getLastCellNum(); i > 0; i--) {
      Cell cell = row.getCell(i - 1);
      if (cell != null) {
        String val = ConverterUtils.getConverterFor(cell.getCellTypeEnum()).apply(cell);
        if (val != null && !val.trim().isEmpty()) {
          return i;
        }
      }
    }

    // se todas as celulas estiverem vazias, não é um registro valido e deve ser ignorado
    return -1;
  }

  public static Function<Cell, String> getConverterFor(CellType type) {
    return CONVERTERS.get(type);
  }

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
