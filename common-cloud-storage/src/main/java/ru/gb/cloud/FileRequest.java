package ru.gb.cloud;

public class FileRequest extends Message {
    private String filename;

    public FileRequest(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

}
