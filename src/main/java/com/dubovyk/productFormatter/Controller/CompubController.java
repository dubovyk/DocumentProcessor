package com.dubovyk.productFormatter.Controller;

import com.dubovyk.productFormatter.FileFormatters.CompuBFormatter;
import com.dubovyk.productFormatter.Models.GlobalModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
@SuppressWarnings("Duplicates")
public class CompubController implements Initializable{
    public TextField masterFilePath;
    public TextField serialsFilePath;
    public TextField stocksFilePath;
    public TextField outputFilePath;
    private CompuBFormatter compubFormatter;

    public void initialize(URL url, ResourceBundle rb){
        compubFormatter = new CompuBFormatter();
    }

    @FXML
    public void handleExit(ActionEvent event){
        Platform.exit();
    }

    @FXML
    public void handleHelp(ActionEvent event){
    }

    @FXML
    public void handleLoadMaster(ActionEvent event){
        FileChooser.ExtensionFilter allNameExtensionFilter = new FileChooser.ExtensionFilter("All files" ,"*.*");
        FileChooser.ExtensionFilter textNameExtensionFilter = new FileChooser.ExtensionFilter("Text files", "*.txt");
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(allNameExtensionFilter);
        chooser.getExtensionFilters().add(textNameExtensionFilter);
        File source = chooser.showOpenDialog(new Stage());
        if (source != null) {
            masterFilePath.setText(source.getAbsolutePath());
        }
    }

    @FXML
    public void handleLoadSerials(ActionEvent event){
        FileChooser.ExtensionFilter allNameExtensionFilter = new FileChooser.ExtensionFilter("All files" ,"*.*");
        FileChooser.ExtensionFilter textNameExtensionFilter = new FileChooser.ExtensionFilter("Text files", "*.txt");
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(allNameExtensionFilter);
        chooser.getExtensionFilters().add(textNameExtensionFilter);
        File source = chooser.showOpenDialog(new Stage());
        if (source != null) {
            serialsFilePath.setText(source.getAbsolutePath());
        }
    }

    @FXML
    public void handleLoadStocks(ActionEvent event){
        FileChooser.ExtensionFilter allNameExtensionFilter = new FileChooser.ExtensionFilter("All files" ,"*.*");
        FileChooser.ExtensionFilter textNameExtensionFilter = new FileChooser.ExtensionFilter("Text files", "*.txt");
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(allNameExtensionFilter);
        chooser.getExtensionFilters().add(textNameExtensionFilter);
        File source = chooser.showOpenDialog(new Stage());
        if (source != null) {
            stocksFilePath.setText(source.getAbsolutePath());
        }
    }

    @FXML
    public void handleSave(ActionEvent event){
        FileChooser.ExtensionFilter csvNameExtensionFilter = new FileChooser.ExtensionFilter("CSV files" ,"*.csv");
        FileChooser.ExtensionFilter excelNameExtensionFilter = new FileChooser.ExtensionFilter("MS Excel spreadsheets", "*.xlsx");
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(csvNameExtensionFilter);
        chooser.getExtensionFilters().add(excelNameExtensionFilter);
        File source = chooser.showSaveDialog(new Stage());
        if (source != null) {
            GlobalModel.getInstance().setFileOutputPath(source.getAbsolutePath());
            outputFilePath.setText(source.getAbsolutePath());
        }
    }

    @FXML
    public void handleProcess(ActionEvent event){
        if(masterFilePath.getText().isEmpty() ||
                serialsFilePath.getText().isEmpty() ||
                stocksFilePath.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Failed");
            alert.setContentText("All fields are required");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Started processing file.\nPlease wait.");
        alert.setHeaderText("Started");
        alert.setTitle("Information");
        alert.showAndWait();
        if(compubFormatter.process(masterFilePath.getText(),
                serialsFilePath.getText(),
                stocksFilePath.getText(),
                outputFilePath.getText())){
            alert.setContentText("Processing completed successfully.");
            alert.setHeaderText("Finished");
            alert.setTitle("Information");
            alert.showAndWait();
        }
    }
}
