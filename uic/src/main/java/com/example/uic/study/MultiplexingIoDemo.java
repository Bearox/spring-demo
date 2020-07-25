/*
 * SocketMultiplexingIo.java
 * Copyright 2020 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.example.uic.study;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Function: 多路复用器 示例代码
 * @author 未闻
 * @date 2020/7/7
 */
public class MultiplexingIoDemo {
    private static Selector ACCEPT_SELECTOR = null;
    private volatile static Selector READ_SELECTOR = null;
    private static final int PORT = 8086;
    /** 0表示一直阻塞 **/
    private static final int WAIT_TIME = 500;

    private static final int BUFFER_SIZE = 64 * 4096;

    private static void initServer() {
        try {
            final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);

            ACCEPT_SELECTOR = Selector.open();
            READ_SELECTOR = Selector.open();

            serverSocketChannel.register(ACCEPT_SELECTOR, SelectionKey.OP_ACCEPT);
        } catch (Exception ignore) {

        }
    }

    private static void handleRead(SelectionKey key) {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        int read;
        try {
            while (true) {
                read = client.read(buffer);
                if (read > 0) {
                    System.out.println("read something.");
                    // do something
                    buffer.clear();
                } else if (read == 0) {
                    break;
                } else {
                    client.close();
                    break;
                }
            }
        } catch (Exception ignore) {

        }
    }

    private static void handleAccept(SelectionKey selectionKey) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel client = serverSocketChannel.accept();
            client.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            client.register(READ_SELECTOR, SelectionKey.OP_READ, buffer);
            System.out.println("Accept a client: " + client.socket().getPort());
        } catch (Exception ignore) {

        }
    }

    private static void readHandler() {
        try {
            while (true) {
                while (READ_SELECTOR.select(WAIT_TIME) > 0) {
                    Set<SelectionKey> selectionKeys = READ_SELECTOR.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if (selectionKey.isReadable()) {
                            handleRead(selectionKey);
                        }
                    }
                }
            }
        } catch (Exception ignore) {

        }
    }

    public static void main(String[] args) {
        initServer();
        new Thread(MultiplexingIoDemo::readHandler).start();
        try {
            while (true) {
                while (ACCEPT_SELECTOR.select(WAIT_TIME) > 0) {
                    Set<SelectionKey> selectionKeys = ACCEPT_SELECTOR.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if (selectionKey.isAcceptable()) {
                            handleAccept(selectionKey);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("done!");
    }
}
