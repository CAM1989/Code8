package ru.gb.cloud;

import java.nio.file.Path;

public class PathRequestIn extends Message {
    private String dir;

    public PathRequestIn(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }
}
