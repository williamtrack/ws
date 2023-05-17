// boost.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

//#define _SILENCE_CXX17_ITERATOR_BASE_CLASS_DEPRECATION_WARNING //旧版boost c++17适配问题

#include <iostream>
#include "boost/circular_buffer.hpp"

#include <numeric>
#include <assert.h> //只有debug模式才有效



void test();
int main()
{
    std::byte a{ 0b1001 };
    //test();
    std::cout << "Hello World!\n";
    std::cout << std::to_integer<int>(a);
}

void test() {
    std::unique_ptr<boost::circular_buffer<std::byte>> bf = nullptr;

    // 创建一个容量为3的循环缓冲区
    boost::circular_buffer<int> cb(3);

    // 插入一些元素到循环缓冲区
    cb.push_back(1);
    cb.push_back(2);

    // 断言
    assert(cb[0] == 1);
    assert(cb[1] == 2);
    assert(!cb.full());
    assert(cb.size() == 2);
    assert(cb.capacity() == 3);

    // 再插入其它元素
    cb.push_back(3);
    cb.push_back(4);

    // 求和
    int sum = std::accumulate(cb.begin(), cb.end(), 0);

    // 断言
    assert(cb[0] == 2);
    assert(cb[1] == 3);
    assert(cb[2] == 4);
    assert(*cb.begin() == 2);
    assert(cb.front() == 2);
    assert(cb.back() == 4);
    assert(sum == 9);
    assert(cb.full());
    assert(cb.size() == 3);
    assert(cb.capacity() == 3);
}