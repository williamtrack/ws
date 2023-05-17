#pragma once

#include "tchar.h"

class Test {
public:
    int value;
    void getValue();
};
extern int extern_value; //不能在头文件中定义
extern Test test_ins;
extern Test* test_prt;

void testFile();


void inputtest();
void lamdatest();
void definetest();
void pointertest();
void arraytest();
void dlllibtest();

void global_test();


