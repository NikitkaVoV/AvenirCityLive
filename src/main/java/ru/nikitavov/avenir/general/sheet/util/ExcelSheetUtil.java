package ru.nikitavov.avenir.general.sheet.util;

import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExcelSheetUtil {

    public XSSFRow getRow(XSSFSheet sheet, int numberOfRow) {
        return sheet.getRow(numberOfRow - 1);
    }

    public List<XSSFRow> getRows(XSSFSheet sheet, int startNumberOfRow) {
        return getRows(sheet, startNumberOfRow, sheet.getPhysicalNumberOfRows());
    }

    public List<XSSFRow> getRows(XSSFSheet sheet, int startNumberOfRow, int endNumberOfRow) {
        if (!checkValidRows(sheet, startNumberOfRow, endNumberOfRow)) {
            return Collections.emptyList();
        }

        ArrayList<XSSFRow> rows = new ArrayList<>(endNumberOfRow - startNumberOfRow);
        for (int i = startNumberOfRow - 1; i < endNumberOfRow; i++) {
            rows.add(sheet.getRow(i));
        }

        return rows;
    }

    public boolean checkValidRows(XSSFSheet sheet, int startNumberOfRow, int endNumberOfRow) {
        int numberOfRows = sheet.getPhysicalNumberOfRows();
        if (numberOfRows < endNumberOfRow - startNumberOfRow) return false;

        return startNumberOfRow <= numberOfRows && numberOfRows >= endNumberOfRow;
    }

    public XSSFCell getCell(XSSFRow row, int numberOfCell) {
        return row.getCell(numberOfCell - 1);
    }

    public List<XSSFCell> getCells(XSSFRow row, int startNumberOfCell, int endNumberOfCell) {

        ArrayList<XSSFCell> cells = new ArrayList<>(endNumberOfCell - startNumberOfCell);
        for (int i = startNumberOfCell; i <= endNumberOfCell; i++) {
            cells.add(getCell(row, i));
        }

        return cells;
    }

}
