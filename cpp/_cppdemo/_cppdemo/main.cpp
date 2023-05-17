// ConsoleApplication.cpp : 此文件包含 "main" 函数。程序执行将在此处开始并结束。
//

#define _CRT_SECURE_NO_WARNINGS

//#pragma comment( linker, "/subsystem:\"windows\" /entry:\"mainCRTStartup\"")
//设置入口非console时添加 properties-linker-system-subsystem

#include "test.h"
#include "CHTime.h"
#include "CHThread.h"
#include "CHVector.h"
#include "CHPointer.h"
#include "CHTemplate.h"
#include "CHMap.h"
#include "CHRegex.h"
#include "CHKey.h"
#include "CHShare.h"
#include "CHCmdHandler.h"

#include <iostream>

static int static_value = 10; //静态全局变量，只在该文件中有效
extern int extern_value; //至少至多在一个文件中定义(所以不能在头文件中定义), 可以多次声明
extern Test test_ins;
extern Test* test_prt;


int main()
{
    //system("chcp 936");
    //system("chcp 65001");
    //SetConsoleOutputCP(936);
    CONSOLE_FONT_INFOEX info = { 0 }; // 以下设置字体来支持中文显示。
    info.cbSize = sizeof(info);
    info.dwFontSize.Y = 16; // leave X as zero
    info.FontWeight = FW_NORMAL;
    wcscpy(info.FaceName, L"Consolas");
    SetCurrentConsoleFontEx(GetStdHandle(STD_OUTPUT_HANDLE), NULL, &info);

    std::cout << "=== Hello World! 你好===\a\n";


    //test_prt->getValue();
    //test_ins.getValue();
    //std::cout << static_value << std::endl;
    global_test();

    ////file test
    //testFile();


    ////cmd test
    ////testCHCmdHandler();
    //testCreateProcess();

    //definetest();
    //inputtest();
    //lamdatest();
    //pointertest();
    //arraytest();
    //dlllibtest();

    ////=== thread test
    //CHThread th;
    //th.test();

    //=== vector test
    //CHVector chvector;
    //chvector.test2();

    ////=== shared_ptr test
    //CHPointer chPointer;
    //chPointer.test();

    ////=== template
    //CHTemplate chtemplate;
    //chtemplate.test();

    ////=== map test
    //CHMap chmap;
    //chmap.test();

    ////=== regex
    //CHRegex chregex;
    //chregex.test();

    ////=== 捕获键盘
    //CHKey chkey;
    //chkey.test();

    ////=== memory shared
    //CHShare chshare;
    //chshare.test();
    ////chshare.fileTest();

    ////time test
    //char* current_datetime = (char*)malloc(18);
    //memset(current_datetime, 0, 18);
    //getCurrentDateTime(current_datetime);
    //std::cout << current_datetime << std::endl;

    //throw "this is an error!";
    std::cout << "\n=== This is end! ===\n";
    system("pause");
}

//=== input test
void inputtest() {
    int sum = 0, value = 0;
    while (std::cin >> value) {
        sum += value;
    } //input ctrl+z to end
    std::cout << sum << std::endl;
}

//=== lambda test
template <typename T, typename _Fn> T lamdatt(T _First, T _Last, _Fn _Func) {
    return _Func(_First, _Last);
}
void lamdatest() {
    int test = 100;
    auto fun = [test](int a, int b) {if (a < test)return a + b; else return a - b; };
    auto add = [](int a, int b) { return a + b; };
    std::cout << add(10, 20);
    std::cout << lamdatt(10, 20, [](int a, int b) {return a + b; });
}

//#define test
void definetest() {
#define defA
#define defB

#ifdef defA
#ifdef defB
    std::cout << "defB" << std::endl;
#else
    std::cout << "no defB" << std::endl;
#endif
#else
    std::cout << "no defA" << std::endl;
#endif
}

//point test
void pointertest() {
    int a = 100;
    int* b = &a;
    int** p = &b;
    std::cout << *b << std::endl;
    std::cout << p << std::endl;
    p++; //地址加1表示增加一个存储单元; b++ 地址偏移4byte(int); p++ 偏移8byte(64内存)
    std::cout << b << std::endl;
    std::cout << *b << std::endl;
    std::cout << p << std::endl;
}

//array test
void arraytest() {
    uint8_t* p = 0;
    uint8_t a[10] = { 'a','b','c','a','b','c' };
    p = a;
    p[4] = '\0';
    std::cout << p << std::endl;
    std::cout << p[1] << std::endl;
}

//dll lib test; 外部dll引入测试; 外部lib引入
#pragma comment(lib,"../x64/release/dll.lib")  //引入lib库, 也可设置properties-linker-input
#include "../dll/CHDll.h" //引入头文件, 或直接引入声明
//extern "C" __declspec(dllimport) void dlltest(char* str); //直接引入声明
void dlllibtest() {
    //外部dll引入测试
    char a[64] = "abc";
    dlltest(a);
    CHDll chdll;
    chdll.setID(234);
    chdll.getID();

    //===============
    //外部lib引入测试, 多个lib函数有同名则引用第一个,和主工程同名则使用主工程
    CHTime chtime;
    chtime.pt("test");
    std::cout << chtime.getTImeMicros() << std::endl;
    //std::this_thread::sleep_for(std::chrono::microseconds(1000));//4ms误差
    std::cout << chtime.getTImeMicros() << std::endl;

}
