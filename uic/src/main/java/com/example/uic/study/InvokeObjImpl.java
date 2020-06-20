/*
 * InvokeObjImpl.java
 * Copyright 2020 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.example.uic.study;

/**
 * Function: 反射测试对象
 * @author 未闻
 * @date 2020/6/3
 */
public class InvokeObjImpl implements InvokeObjService {
    public void show (String name1, String name2) {
        System.out.println("Success called show method: " + name1 + " " + name2);
    }
}
