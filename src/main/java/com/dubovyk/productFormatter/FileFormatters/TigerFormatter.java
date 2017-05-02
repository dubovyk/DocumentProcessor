package com.dubovyk.productFormatter.FileFormatters;

import com.dubovyk.productFormatter.FileProcessor.CSVWorker;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
@SuppressWarnings("Duplicates")
public class TigerFormatter {
    private Map<Integer, String> itemCategories;
    public TigerFormatter(){
        itemCategories = new HashMap<Integer, String>();
        itemCategories.put(0, "THE ADMINISTRATION");
        itemCategories.put(1, "HOUSE HOUSE");
        itemCategories.put(2, "HOUSE GLASSES");
        itemCategories.put(3, "HOUSE PARTY");
        itemCategories.put(4, "HOUSE LEISURE");
        itemCategories.put(5, "HOUSE GADGET");
        itemCategories.put(6, "HOUSE HOBBY");
        itemCategories.put(7, "HOUSE KITCHEN");
        itemCategories.put(8, "HOUSE OFFICE");
        itemCategories.put(9, "HOUSE TO PLAY");
        itemCategories.put(10, "HOUSE MEDIA");
        itemCategories.put(11, "HOUSE CARE");
        itemCategories.put(12, "HOUSE internship");
        itemCategories.put(13, "HOUSE DECORATIVE");
        itemCategories.put(14, "HOUSE SEASON");
        itemCategories.put(15, "HOUSE PLAYING");
        itemCategories.put(16, "HOUSE EAT");
        itemCategories.put(17, "HOUSE SPORT");
        itemCategories.put(18, "HOUSE TOJ");
        itemCategories.put(19, "JULEARTIK .");
        itemCategories.put(20, "ADMINIST");
        itemCategories.put(21, "BOOKSTORE");
        itemCategories.put(22, "INTERNAL");
        itemCategories.put(23, "KAMI");
        itemCategories.put(24, "PALLER");
        itemCategories.put(25, "Jest & SKEAMT");
        itemCategories.put(99, "PROBLEM");
    }

    public void process(String fileName, String outputName) throws IOException {
        List<String[]> result = new ArrayList<String[]>();
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName));
        File f = new File(outputName);
        if (f.exists()){
            f.delete();
        }
        InputStream is = new FileInputStream(new File(fileName));
        Workbook workbook = StreamingReader.builder()
                .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(is);

        Cell cell = null;
        int sheets = workbook.getNumberOfSheets();
        int fCell = 0;
        int lCell = 0;
        int fRow = 0;
        int lRow = 0;
        for (int iSheet = 0; iSheet < sheets; iSheet++) {
            Sheet sheet = workbook.getSheetAt(iSheet);
            if (sheet != null) {
                fRow = 0;
                int iRow = 0;
                lRow = 4;
                for (Row row : sheet) {
                    if (row != null) {
                        String[] rowArray = new String[5];
                        fCell = 0;
                        lCell = 5;
                        for (int iCell = fCell; iCell < lCell; iCell++) {
                            cell = row.getCell(iCell);
                            if (cell != null) {
                                switch (cell.getCellType()) {
                                    case XSSFCell.CELL_TYPE_BLANK:
                                        rowArray[iCell] = "";
                                        break;

                                    case XSSFCell.CELL_TYPE_BOOLEAN:
                                        rowArray[iCell] = (cell.getBooleanCellValue()) ? "True" : "False";
                                        break;

                                    case XSSFCell.CELL_TYPE_ERROR:
                                        rowArray[iCell] = String.valueOf((char) cell.getErrorCellValue());
                                        break;

                                    case XSSFCell.CELL_TYPE_FORMULA:
                                        try {
                                            rowArray[iCell] = ((Double)cell.getNumericCellValue()).toString().replace(',', '.');
                                        } catch (Exception ex){
                                            rowArray[iCell] = cell.getStringCellValue().replace(',', '.');
                                        }
                                        break;

                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        rowArray[iCell] = ((Double) cell.getNumericCellValue()).toString().replace(',', '.');
                                        break;

                                    case XSSFCell.CELL_TYPE_STRING:
                                        rowArray[iCell] = cell.getStringCellValue().replace(',', '.');
                                        break;
                                    default:
                                        rowArray[iCell] = "";
                                }
                            }
                        }
                        if (iRow > 0) {
                            rowArray[0] = ((Integer)Double.valueOf(rowArray[0]).intValue()).toString();
                            rowArray[2] = itemCategories.get(Double.valueOf(rowArray[2]).intValue());
                            rowArray[1] = rowArray[1].replaceAll("\"", "");
                        }
                        System.out.println(rowArray[1]);
                        result.add(rowArray);
                        iRow++;
                    }
                }
            }
        }
        bis.close();
        CSVWorker worker = new CSVWorker();
        worker.writeToFile(outputName, result, false);
    }
}
