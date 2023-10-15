package ru.nikitavov.avenir.general.sheet.util;

import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Component
public class ExcelBookUtil {

    public Optional<XSSFWorkbook> loadBook(File file) {
        XSSFWorkbook book;
        try {
            book = (XSSFWorkbook) WorkbookFactory.create(file);
            return Optional.of(book);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<XSSFWorkbook> loadBook(InputStream inputStream) {
        XSSFWorkbook book;
        try {
            book = (XSSFWorkbook) WorkbookFactory.create(inputStream);
            return Optional.of(book);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
