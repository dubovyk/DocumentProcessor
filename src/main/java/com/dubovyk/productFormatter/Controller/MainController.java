package com.dubovyk.productFormatter.Controller;

import com.dubovyk.productFormatter.Models.GlobalModel;
import com.dubovyk.productFormatter.Processor.FrontendEntry;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
@SuppressWarnings("Duplicates")
public class MainController implements Initializable{
    public TextField problemCol;
    private FrontendEntry mainWorker;

    public TextField inputFilePath;
    public ComboBox actionSelector;
    public TextField outputFilePath;

    public void initialize(URL url, ResourceBundle rb){
        actionSelector.getItems().add("Skecher`s files");
        actionSelector.getItems().add("Elverys file");
        actionSelector.getItems().add("Top Part file");
        actionSelector.getItems().add("Louis Copeland file");
        actionSelector.getItems().add("Tiger`s file");
        actionSelector.getItems().add("CompuB file");
        actionSelector.getItems().add("Direct convert to csv");
        actionSelector.getItems().add("Remove extra commas from csv file");
        mainWorker = new FrontendEntry();
    }

    @FXML
    public void handleExit(ActionEvent event){
        Platform.exit();
    }

    @FXML
    public void handleHelp(ActionEvent event){

    }

    @FXML
    public void handleOperationSelect(ActionEvent event){
        GlobalModel.getInstance().setOperationName(actionSelector.getValue().toString());
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
    public void handleCompuB(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/layouts/compub.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root, 650, 300);
            stage.initOwner(inputFilePath.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("CompuB file formatter");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleLoad(ActionEvent event){
        FileChooser.ExtensionFilter allNameExtensionFilter = new FileChooser.ExtensionFilter("All files" ,"*.*");
        FileChooser.ExtensionFilter csvNameExtensionFilter = new FileChooser.ExtensionFilter("CSV files" ,"*.csv");
        FileChooser.ExtensionFilter textNameExtensionFilter = new FileChooser.ExtensionFilter("Text files", "*.txt");
        FileChooser.ExtensionFilter excelNameExtensionFilter = new FileChooser.ExtensionFilter("MS Excel spreadsheets", "*.xlsx");
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(allNameExtensionFilter);
        chooser.getExtensionFilters().add(textNameExtensionFilter);
        chooser.getExtensionFilters().add(csvNameExtensionFilter);
        chooser.getExtensionFilters().add(excelNameExtensionFilter);
        File source = chooser.showOpenDialog(new Stage());
        if (source != null) {
            GlobalModel.getInstance().setFileInputPath(source.getAbsolutePath());
            inputFilePath.setText(source.getAbsolutePath());
        }
    }

    @FXML
    public void handleProcess(ActionEvent event){
        if(inputFilePath.getText() != null){
            GlobalModel.getInstance().setFileInputPath(inputFilePath.getText());
        }
        if(outputFilePath.getText() != null){
            GlobalModel.getInstance().setFileOutputPath(outputFilePath.getText());
        }
        if(actionSelector.getValue() != null){
            GlobalModel.getInstance().setOperationName(actionSelector.getValue().toString());
        }

        if(actionSelector.getValue().equals("Remove extra commas from csv file") && problemCol.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Failed");
            alert.setContentText("Enter problem column number.");
            alert.showAndWait();
            return;
        }
        if(GlobalModel.getInstance().getFileInputPath().isEmpty() ||
                GlobalModel.getInstance().getFileOutputPath().isEmpty() ||
                GlobalModel.getInstance().getOperationName() == null){
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
        int problemColNum = 0;
        try {
            problemColNum = Integer.parseInt(problemCol.getText());
        } catch (Exception ex){

        }
        if(mainWorker.processFile(GlobalModel.getInstance().getFileInputPath(),
                GlobalModel.getInstance().getFileOutputPath(),
                GlobalModel.getInstance().getOperationName(),
                problemColNum)){
            alert.setContentText("Processing completed successfully.");
            alert.setHeaderText("Finished");
            alert.setTitle("Information");
            alert.showAndWait();
        }
    }
}
