package ru.gb.cloud;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {

    private static final int PORT = 8189;

    public void run() {
        // Пул потоков для обработки подключений клиентов
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // Пул потоков для обработки сетевых сообщений
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // Создание настроек сервера
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup) // указание пулов потоков для работы сервера
                    .channel(NioServerSocketChannel.class) // указание канала для подключения новых клиентов
                    .childHandler(new ChannelInitializer<SocketChannel>() { //
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception { // настройка конвейера для каждого подключившегося клиента
                            ch.pipeline().addLast(
                                    new ObjectEncoder(),
                                    new ObjectDecoder(2 * 1024 * 1024, ClassResolvers.cacheDisabled(null)),
                                    new ServerHandler()
                            );
                        }
                    });
            ChannelFuture f = b.bind(PORT).sync(); // запуск прослушивания порта для подключения клиентов
            log.debug("Server started...");
            f.channel().closeFuture().sync(); // ожидание завершения работы сервера
        } catch (Exception e) {
            log.error("Server exception. Error:", e);
        } finally {
            workerGroup.shutdownGracefully(); // закрытие пула
            bossGroup.shutdownGracefully(); // закрытие пула
        }
    }

    public static void main(String[] args) {
        new Server().run();
    }
}
