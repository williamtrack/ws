package com.charles.demo.test;


//工厂模式、反射机制、泛型
interface IMessage {
    void send();
}

class AMessage implements IMessage {
    @Override
    public void send() {
    }
}

class BMessage implements IMessage {
    @Override
    public void send() {
    }
}

class ReflectFactory {
    private ReflectFactory() {}

    static <T> T getInstance(String className) {
        T instance = null;
        try {
            instance = (T) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    static void job() {
        IMessage msg = ReflectFactory.getInstance("com.charles.mytest.test.AMessage");
//        IMessage msg =new FactoryA().produce();
        msg.send();
    }
}


//第二种工厂模式：抽象工厂模式
interface IFactory {
    IMessage produce();
}
//或者
abstract class Factory{
    abstract IMessage produce();
}

class FactoryA implements IFactory {
    @Override
    public IMessage produce() {
        return new AMessage();
    }
}

class FactoryB implements IFactory {
    @Override
    public IMessage produce() {
        return new BMessage();
    }
}
