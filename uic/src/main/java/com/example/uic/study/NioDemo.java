/*
 * SocketNio.java
 * Copyright 2020 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.example.uic.study;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Function: 初学 NIO
 * @author 未闻
 * @date 2020/6/20
 */
public class NioDemo {
    private static final int PORT = 8086;

    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) throws Exception {
        List<SocketChannel> clientList = new LinkedList<>();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(PORT));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        while (true) {
            System.out.println("wait to be connected.");
            SocketChannel client = serverSocketChannel.accept();
            if (null != client) {
                // 对应的连接也设置为非阻塞
                client.configureBlocking(false);
                clientList.add(client);
                System.out.println("Accept a client: " + client.socket().getPort());
            }
            Iterator<SocketChannel> iterator = clientList.iterator();
            while (iterator.hasNext()) {
                SocketChannel socketChannel = iterator.next();
                boolean res = handleConnect(socketChannel);
                if (res) {
                    iterator.remove();
                }
            }
            Thread.sleep(5000);
        }
    }

    private static boolean handleConnect(SocketChannel client) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        System.out.println("handle client message.");
        try {
            // 不会阻塞
            int res = client.read(byteBuffer);
            if (res > 0) {
                // do something.
                System.out.println("read something.");
                client.close();
                return true;
            }
        } catch (IOException ignore) {
            return true;
        }
        return false;
    }
}
