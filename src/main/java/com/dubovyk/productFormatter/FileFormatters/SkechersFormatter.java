package com.dubovyk.productFormatter.FileFormatters;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class SkechersFormatter implements IFormatter {
    public List<String[]> proceed(List<String[]> data_in){
        List<String[]> out = new ArrayList<String[]>();
        out.add(new String[]{"BARCODE", "Product Name"});
        for(int i = 1; i < data_in.size(); i++){
            out.add(new String[]{data_in.get(i)[0], "PRODUCT " + ((Integer)i).toString()});
        }
        return out;
    }
}
