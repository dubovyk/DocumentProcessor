package com.dubovyk.productFormatter.Models;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class GlobalModel {
    private String fileInputPath, fileOutputPath;
    private String operationName;

    private static GlobalModel ourInstance = new GlobalModel();

    public static GlobalModel getInstance() {
        return ourInstance;
    }

    private GlobalModel() {
    }

    public String getFileInputPath() {
        return fileInputPath;
    }

    public void setFileInputPath(String fileInputPath) {
        this.fileInputPath = fileInputPath;
    }

    public String getFileOutputPath() {
        return fileOutputPath;
    }

    public void setFileOutputPath(String fileOutputPath) {
        this.fileOutputPath = fileOutputPath;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}
