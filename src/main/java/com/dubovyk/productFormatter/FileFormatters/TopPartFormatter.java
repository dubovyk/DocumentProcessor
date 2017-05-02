package com.dubovyk.productFormatter.FileFormatters;

import com.dubovyk.productFormatter.FileProcessor.CSVWorker;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.monitorjbl.xlsx.StreamingReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class TopPartFormatter {
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

        int counter = 0;
        for (Sheet sheet : workbook){
            int fCell, lCell;
            Cell cell;
            for (Row row : sheet) {
                if(counter >= 4){
                    String[] rowArray = new String[18];
                    fCell = 0;
                    lCell = 18;
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
                                    rowArray[iCell] = cell.getStringCellValue();
                                    break;

                                case XSSFCell.CELL_TYPE_NUMERIC:
                                    try {
                                        rowArray[iCell] = ((Double)cell.getNumericCellValue()).toString().replace(',', '.');
                                    } catch (Exception ex){
                                        rowArray[iCell] = cell.getStringCellValue().replace(',', '.');
                                    }
                                    break;

                                case XSSFCell.CELL_TYPE_STRING:
                                    rowArray[iCell] = cell.getStringCellValue();
                                    break;
                                default:
                                    rowArray[iCell] = "";
                            }
                        }
                    }
                    for(int i = 1; i < rowArray.length; i++) {
                        if (rowArray[i] != null) {
                            rowArray[i] = rowArray[i].replace("\"", "");
                        }
                    }
                    result.add(rowArray);
                }
                counter++;
            }
        }

        System.out.println(counter);
        bis.close();
        /*BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputName)));
        for(String[] row : result) {
            writer.write(String.join(",", row));
            writer.write('\n');
            writer.flush();
        }
        writer.close();*/
        CSVWorker worker = new CSVWorker();
        worker.writeToFile(outputName, result);
    }
}
