package com.charles.demo.test;

class MyData {
    static byte Xff = (byte) 0xff;

    static byte[] createData() {//{1,0,1,0, 1,0,1,0, ...1,0,1,0}
        int num = 768 * 1024 * 4;
        byte[] data = new byte[num];
        for (int i = 0; i < num; i++) {
            if (i % 4 == 0) data[i] = Xff;
//            else if (i % 4 == 1) data[i] = (byte) 0b01111111;
            else if (i % 4 == 1) data[i] = (byte) 0b10000000;
            else if (i % 4 == 2) data[i] = 0;
            else data[i] = Xff;
        }
        return data;
    }
}
