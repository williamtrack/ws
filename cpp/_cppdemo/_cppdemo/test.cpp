#include "test.h"
#include <iostream>
#include <Windows.h>

static int static_value = 1000;


Test test_ins; //定义时不能写extern。//定义类只有一种写法
Test* test_prt = &test_ins; //定义可以赋值nullprt; 第二种写法: extern Test* test_prt = &test_ins;
int extern_value; //默认定义0; 第二种写法: extern int extern_value = 100; 

void Test::getValue() {
    value = 100;
    std::cout << value << std::endl;
}

void global_test() {
    //std::cout << GetSystemMetrics(SM_CXSCREEN) << std::endl;
    //std::cout << GetSystemMetrics(SM_CYSCREEN) << std::endl;
    std::cout << "extern_value " << extern_value << std::endl;
    std::cout << "static_value " << static_value << std::endl;
}
