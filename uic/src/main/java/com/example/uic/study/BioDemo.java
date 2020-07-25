/*
 * SocketBio.java
 * Copyright 2020 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.example.uic.study;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Function: BIO 示例代码
 * @author 未闻
 * @date 2020/6/21
 */
public class BioDemo {
    private static final int PORT = 8086;

    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(PORT));

        // 死循环 监听是否有新的连接
        while (true) {
            System.out.println("wait to be connected.");
            // 会阻塞
            SocketChannel client = serverSocketChannel.accept();
            System.out.println("Accept a client: " + client.socket().getPort());
            // 每次有新的连接来就需要new一个线程去处理请求
            Thread clientHandler = new Thread(() -> handleConnect(client));
            clientHandler.start();
        }
    }

    private static void handleConnect(SocketChannel client) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        System.out.println("handle client message.");
        try {
            // 会阻塞
            client.read(byteBuffer);
            System.out.println("read something.");
            // do something.
            client.close();
        } catch (IOException ignore) {

        }
        System.out.println("handle client message finished.");
    }
}
