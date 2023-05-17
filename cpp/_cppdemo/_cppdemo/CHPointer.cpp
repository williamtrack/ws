#include "CHPointer.h"

#include <memory>
#include <iostream>


class Bar { public: ~Bar() { std::cout << "destroying"; } };



void CHPointer::test()
{
	////declare
	////way 1
	//auto p1 = std::make_shared<char>(5); //定义计数+1
	//std::cout << p1.use_count() << std::endl;

	////way 2
	//char* sp1 = new char[3];
	//memset(sp1, 'a', sizeof(char) * 3);


	//std::shared_ptr<char> sp2(p1); //被引用计数+1
	//std::cout << sp2.use_count() << std::endl;
	//sp2.get()[2] = 'b';
	//printf("%c\n", sp2.get()[2]);
	//std::shared_ptr<char> sp3(p1); //被引用计数+1
	//std::cout << sp2.use_count() << std::endl;

	//swap reset
	auto sp1 = std::make_shared<Bar>(); //+1
	std::shared_ptr<Bar> sp2;
	auto sp3 = sp1; //+1
	//auto sp3(sp1);
	std::cout << "initially, sp1.count = " << sp1.use_count() << ",sp2.count = " << sp2.use_count() << ",sp3.count = " << sp3.use_count() << std::endl;
	sp1.swap(sp2);
	std::cout << "after swapping, sp1.count = " << sp1.use_count() << ",sp2.count = " << sp2.use_count() << ",sp3.count = " << sp3.use_count() << std::endl;
	//交换之后，sp3的计数不受影响。而sp1归零，sp2的计数变成了sp1原来的计数。且bar在swap前后都没有析构
	sp3.reset();
	std::cout << "after reset, sp1.count = " << sp1.use_count() << ",sp2.count = " << sp2.use_count() << ",sp3.count = " << sp3.use_count() << std::endl;
	//sp2计数减一 sp3的计数变为0
	std::cin.get();
}

void CHPointer::test2()
{
    std::unique_ptr<int> up1(new int(11));   // 无法复制的unique_ptr
    //unique_ptr<int> up2 = up1;        // err, 不能通过编译
    std::cout << *up1 << std::endl;   // 11

    std::unique_ptr<int> up3 = std::move(up1);    // 现在p3是数据的唯一的unique_ptr

    std::cout << *up3 << std::endl;   // 11
    //std::cout << *up1 << std::endl;   // err, 运行时错误
    up3.reset();            // 显式释放内存
    up1.reset();            // 不会导致运行时错误
    //std::cout << *up3 << std::endl;   // err, 运行时错误

    std::unique_ptr<int> up4(new int(22));   // 无法复制的unique_ptr
    up4.reset(new int(44)); //"绑定"动态对象
    std::cout << *up4 << std::endl; // 44

    up4 = nullptr;//显式销毁所指对象，同时智能指针变为空指针。与up4.reset()等价

    std::unique_ptr<int> up5(new int(55));
    int* p = up5.release(); //只是释放控制权，不会释放内存
    std::cout << *p << std::endl;
    //cout << *up5 << endl; // err, 运行时错误
    delete p; //释放堆区资源
}
