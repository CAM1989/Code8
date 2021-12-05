package ru.gb.cloud;

public class FileDelete extends Message{
private String filename;

    public FileDelete(String filename){
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
