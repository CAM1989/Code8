package ru.gb.cloud;

public class PathResponse extends Message {
    private String path;

    public PathResponse(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
