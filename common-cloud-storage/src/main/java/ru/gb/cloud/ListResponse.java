package ru.gb.cloud;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ListResponse extends Message {

    private List<String> list;

    public ListResponse(Path path) throws IOException {
        list = Files.list(path).map(p -> p.getFileName().toString()).collect(Collectors.toList());
    }

    public List<String> getList() {
        return list;
    }

}
