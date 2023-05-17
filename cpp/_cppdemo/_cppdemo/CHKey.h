#pragma once
#include <conio.h>
#include <Windows.h>
#include <iostream>

class CHKey
{
public:
    void test() {
        char ch;
        while (1) {
            if (_kbhit()) {//如果有按键按下，则_kbhit()函数返回真
                ch = _getch();//使用_getch()函数获取按下的键值
                std::cout << ch;
                if (ch == 27) {
                    break;
                }//当按下ESC时循环，ESC键的键值时27.
            }
            Sleep(500);
        }
    }
};

