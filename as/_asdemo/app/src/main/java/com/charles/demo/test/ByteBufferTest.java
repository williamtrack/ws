package com.charles.demo.test;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//  Created by Charles on 2020/11/16.
//  mail: zingon@163.com
public class ByteBufferTest {
    static String path= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"data.txt";
    private static final int SIZE = 1024;
    public static void doJob(){
        try {
            // 获取通道，该通道允许写操作
            FileChannel fc = new FileOutputStream(path).getChannel();
            //fc.position(20);
            // 将字节数组包装到缓冲区中
            ByteBuffer byteBuffer=ByteBuffer.wrap("apple".getBytes());
            fc.write(byteBuffer);
            // 关闭通道
            fc.close();

            // 随机读写文件流创建的管道
            fc = new RandomAccessFile(path, "rw").getChannel();
            // fc.position()计算从文件的开始到当前位置之间的字节数
            Log.d("William", "此通道文件位置"+fc.position());
            // 设置此通道的文件位置,fc.size()此通道的文件的当前大小,该条语句执行后，通道位置处于文件的末尾
            fc.position(20);
            // 在文件末尾写入字节
            fc.write(ByteBuffer.wrap("Apple".getBytes()));
            fc.close();

            // 用通道读取文件
            fc = new FileInputStream(path).getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(SIZE);
            // 将文件内容读到指定的缓冲区中
            fc.read(buffer);
            buffer.flip();// 此行语句一定要有
            while (buffer.hasRemaining()) {
                Log.d("William", "ByteBUfferTest: "+(char)buffer.get());
            }
            fc.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void doJob2(){
        ByteBuffer buffer=ByteBuffer.allocate(30);
        Log.d("will","--------Test reset----------");
        buffer.clear();
        buffer.position(5);
        buffer.mark();
        buffer.position(10);
        Log.d("will","before reset:" + buffer);
        buffer.reset();
        Log.d("will","after reset:" + buffer);

        Log.d("will","--------Test rewind--------");
        buffer.clear();
        buffer.position(10);
        buffer.limit(15);
        Log.d("will","before rewind:" + buffer);
        buffer.rewind();
        Log.d("will","before rewind:" + buffer);

        Log.d("will","--------Test compact--------");
        buffer.clear();
        buffer.put("abcd".getBytes());
        Log.d("will","before compact:" + buffer);
        Log.d("will",new String(buffer.array()));
        buffer.flip();
        Log.d("will","after flip:" + buffer);
        Log.d("will", String.valueOf((char) buffer.get()));
        Log.d("will", String.valueOf((char) buffer.get()));
        Log.d("will", String.valueOf((char) buffer.get()));
        Log.d("will","after three gets:" + buffer);
        Log.d("will","\t" + new String(buffer.array()));
        buffer.compact();
        Log.d("will","after compact:" + buffer);
        Log.d("will","\t" + new String(buffer.array()));

        Log.d("will","------Test get-------------");
        buffer = ByteBuffer.allocate(32);
        buffer.put((byte) 'a').put((byte) 'b').put((byte) 'c').put((byte) 'd')
                .put((byte) 'e').put((byte) 'f');
        Log.d("will","before flip()" + buffer);
        // 转换为读取模式
        buffer.flip();
        Log.d("will","before get():" + buffer);
        Log.d("will", String.valueOf((char) buffer.get()));
        Log.d("will","after get():" + buffer);
        // get(index)不影响position的值
        Log.d("will", String.valueOf((char) buffer.get(2)));
        Log.d("will","after get(index):" + buffer);
        byte[] dst = new byte[10];
        buffer.get(dst, 0, 2);
        Log.d("will","after get(dst, 0, 2):" + buffer);
        Log.d("will","\t dst:" + new String(dst));
        Log.d("will","buffer now is:" + buffer);
        Log.d("will","\t" + new String(buffer.array()));

        Log.d("will","--------Test put-------");
        ByteBuffer bb = ByteBuffer.allocate(32);
        Log.d("will","before put(byte):" + bb);
        Log.d("will","after put(byte):" + bb.put((byte) 'z'));
        Log.d("will","\t" + bb.put(2, (byte) 'c'));
        // put(2,(byte) 'c')不改变position的位置
        Log.d("will","after put(2,(byte) 'c'):" + bb);
        Log.d("will","\t" + new String(bb.array()));
        // 这里的buffer是 abcdef[pos=3 lim=6 cap=32]
        bb.put(buffer);
        Log.d("will","after put(buffer):" + bb);
        Log.d("will","\t" + new String(bb.array()));

    }
}
