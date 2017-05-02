package com.dubovyk.productFormatter.Processor;

import com.dubovyk.productFormatter.FileFormatters.*;
import com.dubovyk.productFormatter.FileProcessor.CSVWorker;
import javafx.scene.control.Alert;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class FrontendEntry {
    private CSVWorker worker;
    private TopPartFormatter topPartFormatter;
    private SkechersFormatter skechersFormatter;
    private ElverysFormatter elverysFormatter;
    private CopelandFormatter copelandFormatter;
    private TigerFormatter tigerFormatter;
    private DirectConvertFormatter directConvertFormatter;
    private XlsxToCSVFormatter xlsxToCSVFormatter;
    private ExtraCommaReplacer extraCommaReplacer;

    public FrontendEntry(){
        worker = new CSVWorker();
        xlsxToCSVFormatter = new XlsxToCSVFormatter();
        directConvertFormatter = new DirectConvertFormatter();
        tigerFormatter = new TigerFormatter();
        copelandFormatter = new CopelandFormatter();
        topPartFormatter = new TopPartFormatter();
        elverysFormatter = new ElverysFormatter();
        extraCommaReplacer = new ExtraCommaReplacer();
        skechersFormatter = new SkechersFormatter();
    }

    @SuppressWarnings("Duplicates")
    public boolean processFile(String inputName, String outputName, String fileType, int problemCol){
        File input = new File(inputName);
        File out = new File(outputName);

        if (!input.exists()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Input file doesn`t exist. Operation cancelled");
            alert.setHeaderText("Failed");
            alert.showAndWait();
            return false;
        }


        if (fileType.equals("Skecher`s files")){
            if (!FilenameUtils.getExtension(input.getName()).equals("csv") ||
                    !FilenameUtils.getExtension(out.getName()).equals("csv")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Wrong filetype. Must be in *.csv");
                alert.setHeaderText("Failed");
                alert.showAndWait();
                return false;
            }
            List<String[]> data = worker.loadData(inputName);
            List<String[]> res = skechersFormatter.proceed(data);
            worker.writeToFile(outputName, res);
            System.out.println(worker.getString(res));
        } else if (fileType.equals("Elverys file")){
            if (!FilenameUtils.getExtension(input.getName()).equals("txt") ||
                    !FilenameUtils.getExtension(out.getName()).equals("csv")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                System.out.println(FilenameUtils.getExtension(input.getName()) + "  " + FilenameUtils.getExtension(out.getName()));
                alert.setContentText("Wrong filetype.");
                alert.setHeaderText("Failed");
                alert.showAndWait();
                return false;
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader(new File(inputName)));
                List<String> lines = new ArrayList<String>();
                String line = null;
                while ((line = reader.readLine()) != null){
                    lines.add(line);
                }
                reader.close();
                List<String[]> data =  elverysFormatter.proceed(lines);
                worker.writeToFile(outputName, data, false);
            } catch (IOException ex){
                ex.printStackTrace();
            }
        } else if (fileType.equals("Top Part file")){
            if (!FilenameUtils.getExtension(input.getName()).equals("xlsx") ||
                    !FilenameUtils.getExtension(out.getName()).equals("csv")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Wrong filetype.");
                alert.setHeaderText("Failed");
                alert.showAndWait();
                return false;
            }
            try {
                topPartFormatter.process(inputName, outputName);
            } catch (IOException ex){
                ex.printStackTrace();
                return false;
            }
        } else if (fileType.equals("Louis Copeland file")){
            if (!FilenameUtils.getExtension(input.getName()).equals("csv") ||
                    !FilenameUtils.getExtension(out.getName()).equals("csv")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Wrong filetype. Must be in *.csv");
                alert.setHeaderText("Failed");
                alert.showAndWait();
                return false;
            }
            List<String[]> data = worker.loadData(inputName);
            List<String[]> res = copelandFormatter.proceed(data);
            worker.writeToFile(outputName, res);
            System.out.println(worker.getString(res));
        } else if (fileType.equals("Tiger`s file")){
            if (!FilenameUtils.getExtension(input.getName()).equals("xlsx") ||
                    !FilenameUtils.getExtension(out.getName()).equals("csv")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Wrong filetype.");
                alert.setHeaderText("Failed");
                alert.showAndWait();
                return false;
            }
            try {
                tigerFormatter.process(inputName, outputName);
            } catch (IOException ex){
                ex.printStackTrace();
                return false;
            }
        } else if (fileType.equals("Direct convert to csv")){
            if (!(FilenameUtils.getExtension(input.getName()).equals("txt") ||
                    FilenameUtils.getExtension(input.getName()).equals("xlsx") ||
                    FilenameUtils.getExtension(input.getName()).equals("xls"))
                    || !FilenameUtils.getExtension(out.getName()).equals("csv")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Wrong filetype. Must be in *.txt or *.xlsx.");
                alert.setHeaderText("Failed");
                alert.showAndWait();
                return false;
            }
            List<String[]> res;
            if (FilenameUtils.getExtension(input.getName()).equals("txt")) {
                List<String[]> data = worker.loadData(inputName);
                res = directConvertFormatter.proceed(data);
            } else {
                try {
                    if (FilenameUtils.getExtension(input.getName()).equals("xls")){
                        res = xlsxToCSVFormatter.processXls(inputName);
                        worker.writeToFile(outputName, res, false);
                    } else{
                        res = xlsxToCSVFormatter.process(inputName);
                        worker.writeToFile(outputName, res, false);
                    }
                } catch (IOException ex){
                    ex.printStackTrace();
                    return false;
                }
            }
        } else if (fileType.equals("Remove extra commas from csv file")){
            if (!FilenameUtils.getExtension(input.getName()).toLowerCase().equals("csv") ||
                    !FilenameUtils.getExtension(out.getName()).toLowerCase().equals("csv")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Wrong filetype. Must be in *.csv");
                alert.setHeaderText("Failed");
                alert.showAndWait();
                return false;
            }
            extraCommaReplacer.process(inputName, outputName, problemCol);
        }
        return true;
    }
}
