package ru.gb.cloud;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileMessage extends Message {
    private String filename;
    private byte[] buffer;
    private int partNumber;
    private int partCount;

    public FileMessage(Path path) throws IOException {
        filename = path.getFileName().toString();
        buffer = Files.readAllBytes(path);
    }

    public FileMessage(String filename, int partNumber, int partCount, byte[] buffer) {
        this.filename = filename;
        this.partNumber = partNumber;
        this.partCount = partCount;
        this.buffer = buffer;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public String getFilename() {
        return filename;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public int getPartCount() {
        return partCount;
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }
}
