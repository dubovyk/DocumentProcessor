package com.dubovyk.productFormatter.FileFormatters;

import com.dubovyk.productFormatter.FileProcessor.CSVWorker;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
@SuppressWarnings("Duplicates")
public class XlsxToCSVFormatter {
    public List<String[]> process(String fileName) throws IOException {
        List<String[]> result = new ArrayList<String[]>();
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName));
        InputStream is = new FileInputStream(new File(fileName));
        Workbook workbook = StreamingReader.builder()
                .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(is);

        int fCell = 0;
        int lCell = 0;
        int fRow = 0;
        int lRow = 0;
        int curRow = 0;
        for (Sheet sheet : workbook) {
            if (sheet != null) {
                for (Row row : sheet) {
                    if (row != null) {
                        String[] rowArray = new String[row.getLastCellNum()];
                        int iCell = 0;
                        for (Cell cell : row) {
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
                                        try{
                                            int n = ((Double)(cell.getNumericCellValue())).intValue();
                                            if (cell.getNumericCellValue() == n){
                                                rowArray[iCell] = Integer.valueOf(n).toString();
                                            } else {
                                                rowArray[iCell] = Double.valueOf(cell.getNumericCellValue()).toString();
                                            }
                                        } catch (Exception ex){
                                            ex.printStackTrace();
                                        }
                                        break;

                                    case XSSFCell.CELL_TYPE_STRING:
                                        rowArray[iCell] = cell.getStringCellValue().replaceAll("\"", "").replaceAll("\"", "");
                                        break;
                                    default:
                                        rowArray[iCell] = "";
                                }
                            }
                            iCell++;
                        }
                        for(int i = 0; i < rowArray.length; i++) {
                            if (rowArray[i] != null) {
                                rowArray[i] = rowArray[i].replaceAll("^\"", "").replaceAll("\"$", "");
                            }
                        }
                        System.out.println(String.join(",", rowArray));
                        result.add(rowArray);
                    }
                    curRow++;
                }
            }
        }
        bis.close();
        return result;
    }

    public List<String[]> processXls(String fileName) throws IOException {
        List<String[]> result = new ArrayList<String[]>();
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(fileName));
        workbook.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);

        HSSFSheet sheet = workbook.getSheetAt(0);
        for(int rowIndex = sheet.getFirstRowNum(); rowIndex <= sheet.getLastRowNum(); rowIndex++)
        {
            List<String> rowdata = new ArrayList<String>();
            Cell cell=null;
            Row row = null;

            int previousCell = -1;
            int currentCell = 0;
            row = sheet.getRow(rowIndex);
            for(int colIndex=row.getFirstCellNum(); colIndex < row.getLastCellNum(); colIndex++)
            {
                cell = row.getCell(colIndex);
                currentCell = cell.getColumnIndex();
                switch (cell.getCellType()) {
                    case XSSFCell.CELL_TYPE_BLANK:
                        rowdata.add(cell.getStringCellValue());
                        break;

                    case XSSFCell.CELL_TYPE_BOOLEAN:
                        rowdata.add(cell.getStringCellValue());
                        break;

                    case XSSFCell.CELL_TYPE_ERROR:
                        rowdata.add(cell.getStringCellValue());
                        break;

                    case XSSFCell.CELL_TYPE_FORMULA:
                        rowdata.add(cell.getStringCellValue());
                        break;

                    case XSSFCell.CELL_TYPE_NUMERIC:
                        try{
                            int n = ((Double)(cell.getNumericCellValue())).intValue();
                            if (cell.getNumericCellValue() == n){
                                rowdata.add((Integer.valueOf(n).toString()));
                            } else {
                                rowdata.add(Double.valueOf(cell.getNumericCellValue()).toString());
                            }
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }
                        break;

                    case XSSFCell.CELL_TYPE_STRING:
                        rowdata.add(cell.getStringCellValue());
                        break;
                    default:
                        rowdata.add("");
                 /* Cell processing starts here*/
            }
        }
            String[] data = new String[rowdata.size()];
            for(int i = 0; i < rowdata.size(); i++){
                data[i] = rowdata.get(i);
            }
            System.out.println();
            result.add(data);
    }
        return result;
    }
}

