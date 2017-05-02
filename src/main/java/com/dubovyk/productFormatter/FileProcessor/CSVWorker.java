package com.dubovyk.productFormatter.FileProcessor;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class CSVWorker {

    public List<String[]> loadDataTabs(String filename){
        List<String[]> res = new ArrayList<String[]>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-16"));
            String line = null;
            while ((line = reader.readLine()) != null){
                res.add(line.split("\t"));
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return res;
    }

    public List<String[]> loadData(String filename, char delimiter){
        try {
            CSVReader reader = new CSVReader(new FileReader(filename), delimiter);
            List<String[]> data = (List<String[]>) reader.readAll();
            return data;
        } catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public List<String[]> loadData(String filename){
        return loadData(filename, ',');
    }

    public void writeToFile(String fileName, List<String[]> data){
        writeToFile(fileName, data, ',');
    }

    public void writeToFile(String fileName, List<String[]> data, boolean noQuotes){
        writeToFile(fileName, data, ',', noQuotes);
    }

    public void writeToFile(String fileName, List<String[]> data, char delimiter, boolean noQuotes){
        try {
            CSVWriter writer;
            if(noQuotes) {
                writer = new CSVWriter(new FileWriter(fileName), delimiter, CSVWriter.NO_QUOTE_CHARACTER);
            }
            else {
                writer = new CSVWriter(new FileWriter(fileName), delimiter);
            }
            writer.writeAll(data, false);
            writer.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void writeToFile(String fileName, List<String[]> data, char delimiter){
        writeToFile(fileName, data, delimiter, true);
    }

    public String getString(List<String[]> data){
        return getString(data, ',');
    }

    public String getString(List<String[]> data, char delimiter){
        StringBuilder res = new StringBuilder();
        for(String[] strings : data){
            for(int i = 0; i < strings.length; i++){
                res.append(strings[i]);
                if(i < strings.length - 1){
                    res.append(delimiter);
                }
            }
            res.append('\n');
        }
        return res.toString();
    }
}
