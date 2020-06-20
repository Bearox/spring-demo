/*
 * ReflectTest.java
 * Copyright 2020 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.example.uic.study;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Function: 反射测试
 * @author 未闻
 * @date 2020/6/3
 */
public class ReflectTest {
    public static void main(String[] args)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?>[]          methodParameterTypes = new Class<?> [] {String.class, String.class};
        Object[]            parameters = new Object[] {"hello", "bearox"};
        InvokeObjService invokeObjService = new InvokeObjImpl();
        Class<?> clazz = invokeObjService.getClass();
        Method[] methods = clazz.getMethods();
        System.out.println("以下输出InvokeObj类的方法：");
        for (Method method : methods) {
            System.out.println(method);
        }
        Method method1 = clazz.getMethod("show", methodParameterTypes);
        method1.invoke(new InvokeObjImpl(), parameters);
    }

}
