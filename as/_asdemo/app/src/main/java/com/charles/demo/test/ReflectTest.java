package com.charles.demo.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class Member {
    Member() {}

    Member(int a) {}

    Member(String a) {}

    private int test(int a) {return a + 1;}

    private int m = 100;
}

class ReflectTest {
    static void job() throws Exception {
        //Class类实例化三种方法
//        Member member = new Member();
//        Class<?> clazz = member.getClass();
        Class<?> clazz = Member.class;
//        Class<?> clazz = Class.forName("com.charles.mytest.test.Member");

        Object obj = clazz.getDeclaredConstructor().newInstance();
//        Object obj=clazz.getDeclaredConstructor(String.class).newInstance("abc");
//        Object obj=clazz.getDeclaredConstructor(int.class).newInstance(123);
//        Log.d("William", "ReflectTest-job: " + obj);

//        Constructor<?>[]constructors=clazz.getDeclaredConstructors();
//        for(Constructor<?> cons:constructors){LogUtil.d("ReflectTest-job: " +cons);}
//        Constructor<?>con =clazz.getDeclaredConstructor(int.class);
        Method method = clazz.getDeclaredMethod("test", int.class);
        Field field = clazz.getDeclaredField("m");
        field.setAccessible(true);//取消封装???
        field.set(obj, 156);
        method.setAccessible(true);
    }
}
