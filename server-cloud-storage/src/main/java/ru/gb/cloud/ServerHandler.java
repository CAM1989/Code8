package ru.gb.cloud;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final static String serverDir = "server-cloud-storage/Server-DATA";
    private static Path serverPath = Paths.get(serverDir);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
        ctx.writeAndFlush(new ListResponse(serverPath));
        ctx.writeAndFlush(new PathResponse(serverDir));
        log.debug("Client connected...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("Message: {}", msg);
        if (msg instanceof FileRequest) {
            FileRequest fileRequest = (FileRequest) msg;
            FileMessage fileMessage = new FileMessage(serverPath.resolve(fileRequest.getFilename()));
            ctx.writeAndFlush(fileMessage);
        }
        if (msg instanceof FileMessage) {
            FileMessage message = (FileMessage) msg;
            String filename = message.getFilename();
            Path path = Paths.get(serverDir + filename);
            boolean append = true;
            if (message.getPartNumber() == 1) {
                append = false;
            }
            log.debug("Message part: {} / {}", message.getPartNumber(),message.getPartCount());
            FileOutputStream fileOutputStream = new FileOutputStream(String.valueOf(path),append);
            fileOutputStream.write(message.getBuffer());
            fileOutputStream.close();
            ctx.writeAndFlush(new ListResponse(serverPath));
        }
        if (msg instanceof FileDelete) {
            FileDelete fileDelete = (FileDelete) msg;
            Path path = serverPath.resolve(fileDelete.getFilename());
            File file = path.toFile();
            delete(file);
            ctx.writeAndFlush(new ListResponse(serverPath));
        }
        if (msg instanceof FileRequest) {
            ctx.writeAndFlush(new ListResponse(serverPath));
        }
        if (msg instanceof PathRequestUp) {
            if (serverPath.getParent() != null) {
                serverPath = serverPath.getParent();
                ctx.writeAndFlush(new PathResponse(serverPath.toString()));
                ctx.writeAndFlush(new ListResponse(serverPath));
            }
        }
        if (msg instanceof PathRequestIn) {
            PathRequestIn pathRequestIn = (PathRequestIn) msg;
            Path newPath = serverPath.resolve(pathRequestIn.getDir());
            if (Files.isDirectory(newPath)) {
                serverPath = newPath;
                ctx.writeAndFlush(new PathResponse(serverPath.toString()));
                ctx.writeAndFlush(new ListResponse(serverPath));
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Exception ServerHandler: ", cause);
        ctx.close();
    }

    private void delete(File file) {
        File[] list = file.listFiles();
        if (list != null) {
            for (File f : list) {
                delete(f);
            }
        }
        file.delete();
    }
}
