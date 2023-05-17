#pragma once

#include <iostream>
#include <Windows.h>
#include <thread>
#include <mutex>

#include <future>

class CHThread
{
public:
	void test();
	static void t1(); //静态下test才能够引用?
	static void t2();
	static void t3();
	static void t4();
private:
	static int cnt;  //静态类成员必须在类外面定义
	static std::mutex mtx;
    void waitfor();
};

//namespace CONSTANT {
//	static int cnt = 20; //int cnt; //error 头文件不能定义普通变量, 否则编译可以但是链接报错
//	static std::mutex m;
//}
