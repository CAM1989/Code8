package ru.gb.cloud;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Network {
    private static Socket socket;
    private static ObjectEncoderOutputStream out;
    private static ObjectDecoderInputStream in;

    public static void start() {
        try {
            socket = new Socket("localhost", 8189);
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
            in = new ObjectDecoderInputStream(socket.getInputStream());
        } catch (IOException e) {
            log.error("",e);
        }
    }

    public static void stop() {
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            log.error("",e);
        }
    }

    public static void sendMsg(Message msg) {
        try {
            out.writeObject(msg);
        } catch (IOException e) {
            log.error("",e);
        }
    }

    public static Message readObject() throws ClassNotFoundException, IOException {
        Object obj = in.readObject();
        return (Message) obj;
    }
}
