package com.dubovyk.productFormatter.FileFormatters;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class CopelandFormatter implements IFormatter {
    public List<String[]> proceed (List<String[]> input){
        List<String[]> result = new ArrayList<String[]>();
        for(String[] row : input){
            String[] new_row = new String[8];
            new_row[0] = row[16];
            new_row[1] = row[17];
            new_row[2] = row[18];
            new_row[3] = row[19];
            new_row[4] = row[20];
            new_row[5] = row[21] + " " + row[22] + " " + row[23] + " " + row[24] + " " + row[25];
            new_row[6] = row[26];
            new_row[7] = row[27];
            result.add(new_row);
        }
        return result;
    }
}
