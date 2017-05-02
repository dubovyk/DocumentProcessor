package com.dubovyk.productFormatter.FileFormatters;

import com.dubovyk.productFormatter.FileProcessor.CSVWorker;

import java.util.*;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class CompuBFormatter {
    public boolean process(String inputMain, String inputSerials, String inputStocks, String outputPath){
        List<String[]> result = new ArrayList<String[]>();
        CSVWorker worker = new CSVWorker();
        List<String[]> main = worker.loadDataTabs(inputMain);
        List<String[]> serials = worker.loadDataTabs(inputSerials);
        List<String[]> stocks = worker.loadDataTabs(inputStocks);

        List<String[]> goodSerials = new ArrayList<String[]>();
        for(String[] row : serials){
            if(row.length >= 2){
                goodSerials.add(row);
            }
        }
        serials = goodSerials;

        //System.out.println(main.size() + "  " + serials.size() + "  " + stocks.size());
        //System.out.println(String.join(" - ", main.get(2)));

        Map<String, String> barcodeData = getBarcodesFromItemNo(main);
        Map<String, String> makersData = getManufacturesFromItemNo(main);
        Map<String, String> descData = getItemsDescFromItemNo(serials);
        Map<String, String> stocksData = getStocksData(stocks);

        for(int i = 0; i < serials.size(); i++) {
            String[] row = serials.get(i);
            if(row.length < 2){
                continue;
            }
            if (i > 0) {
                row = new String[]{row[0], row[1], row[2], "1", row[3]};
            }
            else {
                row = new String[]{row[0], row[1], row[2], "In Stock", row[3]};
            }
            serials.set(i, row);
        }

        for(int i = 0; i < main.size(); i++){
            String[] row = main.get(i);
            if(row.length < 2){
                continue;
            }
            //System.out.println(i + " " + row.length + " " + String.join(" - ", row));
            String[] temp = new String[30];
            for(int j = 0; j < row.length; j++){
                temp[j] = row[j];
            }
            String[] newRow = new String[]{temp[0], temp[1], temp[2], temp[3], temp[4], temp[5], temp[6], temp[18]};
            if(i == 0){
                temp[3] = "In stock";
            } else {
                temp[3] = "0";
            }
            main.set(i, newRow);
        }

        for(int i = 0; i < serials.size(); i++){
            String[] row = serials.get(i);
            if(row.length < 2){
                continue;
            }
            String[] newRow = new String[7];
            newRow[0] = row[0];
            newRow[1] = row[1];
            newRow[2] = row[2];
            newRow[3] = row[3];
            newRow[4] = row[4];
            newRow[5] = barcodeData.get(row[1]);
            newRow[6] = makersData.get(row[1]);
            serials.set(i, newRow);
        }

        for(int i = 0; i < main.size(); i++){
            String[] row = main.get(i);
            if(row.length < 2){
                continue;
            }
            String[] newRow = new String[row.length + 1];
            for(int j = 0; j < row.length; j++){
                newRow[j] = row[j];
            }
            if(descData.keySet().contains(row[1])){
                newRow[row.length] = descData.get(row[1]);
            } else {
                newRow[row.length] = "";
            }
            main.set(i, newRow);
        }

        List<String[]> newMain = new ArrayList<String[]>();
        newMain.add(main.get(0));
        for(int i = 1; i < main.size(); i++){
            String[] row = main.get(i);
            if(row.length < 2){
                continue;
            }
            //System.out.println(row.length);
            if(row[8].isEmpty()){
                newMain.add(row);
            }
        }

        for(int i = 1; i < newMain.size(); i++){
            String[] row = newMain.get(i);
            if(row.length < 2){
                continue;
            }
            if(stocksData.keySet().contains(row[1])){
                row[3] = stocksData.get(row[1]);
            } else {
                row[3] = "0";
            }
            newMain.set(i, row);
        }

        System.out.println(newMain.size() + " " + serials.size() + " " + newMain.size() + serials.size());

        for(int i = 1; i < serials.size(); i++){
            newMain.add(serials.get(i));
        }

        for(int i = 0; i < newMain.size(); i++){
            String[] row = newMain.get(i);

            for(int k = 0; k < row.length; k++){
                if (row[k] != null){
                    row[k] = row[k].replaceAll(",", ".");
                }
            }

            if(row[1] != null) {
                row[1] = row[1].replaceAll("\"\" ", "").replaceAll("\"", "");
            }
            if(row[2] != null) {
                row[2] = row[2].replaceAll("\"", "");
            }
            if (row[3] != null) {
                row[3] = row[3].replaceAll("\"", "");
            }
            if(row[4] != null) {
                row[4] = row[4].replaceAll("\"", "");
            }
            if(row[5] != null) {
                row[5] = row[5].replaceAll("'", "").replaceAll("\"", "");
            }
            if(row[6]!= null) {
                row[6] = row[6].replaceAll("\"", "");
            }
            row = new String[]{row[1], row[2], row[3], row[4],
                    row[5], row[6]};
            //row[1] = "\"" + row[1] + "\"";
            newMain.set(i, row);
            if(row[0].equals("MNNQ2B/A")){
                System.out.println(String.join(" -- ", row));
            }
        }
        newMain.set(0, new String[]{"", "Item Description", "In Stock", "", "Item Group", "Manufacturer"});
        worker.writeToFile(outputPath, newMain);

        return true;
    }

    private Map<String, String> getBarcodesFromItemNo(List<String[]> master){
        Map<String, String> res = new HashMap<String, String>();
        for(int i = 1; i < master.size(); i++){
            String[] row = master.get(i);
            if(row.length < 2){
                continue;
            }
            res.put(row[1], row[5]);
        }
        return res;
    }

    private Map<String, String> getManufacturesFromItemNo(List<String[]> master){
        Map<String, String> res = new HashMap<String, String>();
        for(String[] row: master){
            if(row.length < 2){
                continue;
            }
            res.put(row[1], row[6]);
        }
        return res;
    }

    private Map<String, String> getItemsDescFromItemNo(List<String[]> serials){
        Map<String, String> res = new HashMap<String, String>();
        for(String[] row: serials){
            if(row.length < 2){
                continue;
            }
            res.put(row[1], row[2]);
        }
        return res;
    }

    private Map<String, String> getStocksData(List<String[]> stocks){
        Map<String, String> res = new HashMap<String, String>();
        for(String[] row: stocks){
            if(row.length < 2){
                continue;
            }
            res.put(row[0], row[6]);
        }
        return res;
    }
}
