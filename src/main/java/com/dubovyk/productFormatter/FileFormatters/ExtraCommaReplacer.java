package com.dubovyk.productFormatter.FileFormatters;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class ExtraCommaReplacer {
    public List<String[]> process(String inputPath, String outputPath, int problemCol){
        List<String[]> res = new ArrayList<>();
        int trueSize;
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(new File(outputPath)));
            BufferedReader sr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputPath))));
            String s = sr.readLine();
            trueSize = s.split(",").length;
            sr.close();
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        List<String> badTemp = new ArrayList<>();
        int a[] = new int[]{0};
        try (Stream<String> stream = Files.lines(Paths.get(inputPath), Charset.forName("Cp1252"))) {
            stream.forEach((s -> {
                String[] splited = s.split(",");
                try {
                    StringBuilder builder = new StringBuilder();
                    if(a[0] == 1){
                        String[] newSplited = new String[splited.length];
                        newSplited[0] = badTemp.get(0);
                        badTemp.remove(0);
                        a[0] = 0;
                        for(int i = 1; i < splited.length; i++){
                            newSplited[i] = splited[i];
                        }
                        String[] done = processLine(newSplited, problemCol, trueSize);
                        for(int i = 0; i < done.length; i++){
                            builder.append(done[i]);
                            builder.append(",");
                        }
                        String result = builder.toString();
                        System.out.println(builder.toString());
                        writer.write(result);
                        writer.newLine();
                        writer.flush();
                    } else if(splited.length == 1){
                        System.out.println(splited[0]);
                        badTemp.add(splited[0]);
                        a[0] = 1;
                    } else {
                        String[] done = processLine(splited, problemCol, trueSize);
                        for(int i = 0; i < done.length; i++){
                            builder.append(done[i]);
                            builder.append(",");
                        }
                        String result = builder.toString();
                        writer.write(result);
                        writer.newLine();
                        writer.flush();
                    }

                } catch (IOException ex){
                    ex.printStackTrace();
                    return;
                }
            }));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private String[] processLine(String[] line, int problemCol, int trueSize){
        if(line.length == trueSize){
            return  line;
        }
        String temp[] = new String[trueSize];
        int goodI = 0;
        int badI = 0;
        int passedCommas = 0;
        for(; badI < problemCol; goodI++, badI++){
            temp[goodI] = line[badI];
            passedCommas++;

        }
        int wrongCommas = line.length - 1 - passedCommas - (trueSize - problemCol) + 1;
        temp[goodI] = "";
        for(int j = 0; j <= wrongCommas; j++){
            temp[goodI] += line[badI];
            badI++;
        }
        goodI++;
        for(; badI < line.length; badI++, goodI++){
            temp[goodI] = line[badI];
        }
        return temp;
    }
}
