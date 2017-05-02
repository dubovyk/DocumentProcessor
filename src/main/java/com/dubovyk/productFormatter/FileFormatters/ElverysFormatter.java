package com.dubovyk.productFormatter.FileFormatters;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class ElverysFormatter {
    public List<String[]> proceed(List<String> input){
        List<String[]> result = new ArrayList<String[]>();
        String[] header = input.get(6).split("\\|");
        for(int i = 0; i < header.length; i++){
            header[i] = header[i].trim();
        }
        header = new String[]{header[1], header[2], header[3], header[4]};
        result.add(header);
        for(int i = 8; i < input.size(); i++){
            String[] currentLine = input.get(i).split("\\|");
            if(currentLine.length == 5) {
                for (int j = 0; i < currentLine.length; i++) {
                    currentLine[j] = currentLine[j].trim();
                }
                currentLine[2] = currentLine[2].replaceAll("( ){2,}", "");
                System.out.println(currentLine[2]);
                currentLine = new String[]{currentLine[1], currentLine[2], currentLine[3], currentLine[4]};
                result.add(currentLine);
            }
        }
        return result;
    }
}
