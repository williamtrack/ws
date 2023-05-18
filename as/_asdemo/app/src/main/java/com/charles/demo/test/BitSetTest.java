package com.charles.demo.test;

import android.util.Log;

import com.charles.util.CountUtil;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

class BitSetTest {
    static void test() {
        Random random = new Random();
        int random_number = 10000000;
        int all_number = 10000000;

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < random_number; i++) {
            int randomResult = random.nextInt(random_number);
            list.add(randomResult);
        }
//        System.out.println("产生的随机数有");
        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i));
        }
        BitSet bitSet = new BitSet(all_number);
        for (int i = 0; i < random_number; i++) {
            bitSet.set(list.get(i));
        }

        Log.d("William", "BitSetTest-test: " + "不在上述随机数中有" + (all_number - bitSet.cardinality()));
//        for (int i = 0; i < all_number; i++) {
//            if (!bitSet.get(i)) {
//                System.out.println(i);
//            }
//        }
    }


    byte Xff = (byte) 0xff;
    byte B0 = 0b0;
    byte B1 = 0b1;

    int width = 768;
    int height = 1024;

    BitSet bitSet = new BitSet(100);
    byte[] bmp_array = MyData.createData();
    byte[] change_array = new byte[height * (width * 3 + 16) / 8];


    //按特定行，更改change_array的值
    void change(short[] address_array, byte mode) {
        CountUtil.intervalTime(false);
        int change_index = 0;
        for (short address : address_array) {//按行数处理
            for (int i = 0; i < 6; i++) {
                change_array[change_index] |= (mode & 0b00111111) >> (5 - i) << (7 - i);
            }
            change_array[change_index] |= (address & 0b111100000000) >> 8;
            change_array[change_index + 1] |= address & 0b11111111;

            int cut_index = address * (width * 4);//原始数组起始索引
            changeRGB111(bmp_array, change_array, cut_index, change_index);//处理一行数据
            change_index = change_index + (width * 3 + 16) / 8;//下一行结果数组起始索引
        }
        CountUtil.intervalTime(true);
        Log.d("William", "BitSetTest-change: " + change_array[0]);
        Log.d("William", "BitSetTest-change: " + change_array[1]);
        Log.d("William", "BitSetTest-change: " + change_array[2]);
        Log.d("William", "BitSetTest-change: " + change_array[3]);
        Log.d("William", "BitSetTest-change: " + change_array[4]);
        Log.d("William", "BitSetTest-change: ~" + change_array[296000]);
        Log.d("William", "BitSetTest-change: =" + change_array[296001]);
        Log.d("William", "BitSetTest-change: ==" + change_array[296002]);
        Log.d("William", "BitSetTest-change: ===" + change_array[296003]);
    }


    private void changeRGB111(byte[] cut_array, byte[] change_array, int cut_index, int change_index) {
        for (int i = change_index, j = cut_index; i < change_index + (width * 3) / 8; i = i + 3, j = j + 32) {
            change_array[2 + i] |= (byte) (((cut_array[j] & 0xFF) > 127 ? 1 : 0) << 7);
            change_array[2 + i] |= (byte) (((cut_array[j + 1] & 0xFF) > 127 ? 1 : 0) << 6);
            change_array[2 + i] |= (byte) (((cut_array[j + 2] & 0xFF) > 127 ? 1 : 0) << 5);
            change_array[2 + i] |= (byte) (((cut_array[j + 4] & 0xFF) > 127 ? 1 : 0) << 4);
            change_array[2 + i] |= (byte) (((cut_array[j + 5] & 0xFF) > 127 ? 1 : 0) << 3);
            change_array[2 + i] |= (byte) (((cut_array[j + 6] & 0xFF) > 127 ? 1 : 0) << 2);
            change_array[2 + i] |= (byte) (((cut_array[j + 8] & 0xFF) > 127 ? 1 : 0) << 1);
            change_array[2 + i] |= (byte) (((cut_array[j + 9] & 0xFF) > 127 ? 1 : 0));

            change_array[2 + i + 1] |= (byte) (((cut_array[j + 10] & 0xFF) > 127 ? 1 : 0) << 7);
            change_array[2 + i + 1] |= (byte) (((cut_array[j + 12] & 0xFF) > 127 ? 1 : 0) << 6);
            change_array[2 + i + 1] |= (byte) (((cut_array[j + 13] & 0xFF) > 127 ? 1 : 0) << 5);
            change_array[2 + i + 1] |= (byte) (((cut_array[j + 14] & 0xFF) > 127 ? 1 : 0) << 4);
            change_array[2 + i + 1] |= (byte) (((cut_array[j + 16] & 0xFF) > 127 ? 1 : 0) << 3);
            change_array[2 + i + 1] |= (byte) (((cut_array[j + 17] & 0xFF) > 127 ? 1 : 0) << 2);
            change_array[2 + i + 1] |= (byte) (((cut_array[j + 18] & 0xFF) > 127 ? 1 : 0) << 1);
            change_array[2 + i + 1] |= (byte) (((cut_array[j + 20] & 0xFF) > 127 ? 1 : 0));

            change_array[2 + i + 2] |= (byte) (((cut_array[j + 21] & 0xFF) > 127 ? 1 : 0) << 7);
            change_array[2 + i + 2] |= (byte) (((cut_array[j + 22] & 0xFF) > 127 ? 1 : 0) << 6);
            change_array[2 + i + 2] |= (byte) (((cut_array[j + 24] & 0xFF) > 127 ? 1 : 0) << 5);
            change_array[2 + i + 2] |= (byte) (((cut_array[j + 25] & 0xFF) > 127 ? 1 : 0) << 4);
            change_array[2 + i + 2] |= (byte) (((cut_array[j + 26] & 0xFF) > 127 ? 1 : 0) << 3);
            change_array[2 + i + 2] |= (byte) (((cut_array[j + 28] & 0xFF) > 127 ? 1 : 0) << 2);
            change_array[2 + i + 2] |= (byte) (((cut_array[j + 29] & 0xFF) > 127 ? 1 : 0) << 1);
            change_array[2 + i + 2] |= (byte) (((cut_array[j + 30] & 0xFF) > 127 ? 1 : 0));
        }
    }



    //根据中心点，获取行数
    short[] createPaintRowNumber(int cutN, int pointY) {
        short[] row_number = new short[cutN];
        //判断上下边界
        if (pointY < cutN / 2) pointY = cutN / 2;
        else if (pointY > height - cutN / 2) pointY = height - cutN / 2 - cutN % 2;
        //找到对应dataArray的下标
        int first = pointY - cutN / 2;
        for (int i = 0; i < cutN; i++) {
            row_number[i] = (short) (first + i);
        }
        return row_number;
    }


}
