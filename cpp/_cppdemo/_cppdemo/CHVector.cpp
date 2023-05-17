#include "CHVector.h"
#include <iostream>

void CHVector::test()
{
	using namespace std;

	// ==声明定义
	vector<int> v1{ 1,2,3 };
	v1.assign(4, 5);
	v1.emplace_back(9);
	v1.push_back(10);
	v1.push_back(11);
	v1.push_back(12);
	v1.pop_back();
	v1.insert(v1.begin()+3, 10);
	printf("%u,%u\n", v1.size(), v1.capacity());

	// ==访问
	cout << "index 0: " << v1.at(0) << endl;
	cout << "vectorV1:" << endl;
	std::vector<int>::iterator it;
	for (it = v1.begin(); it != v1.end(); it++)
		cout << *it << endl;

	vector<int> v2;
	cout << "vectorV2:" << endl;
	v2.assign(v1.begin(), v1.end());
	for (int val : v2) cout << val << endl;
}

void printVector(std::vector<int>& v)
{
	using namespace std;
	for (vector<int>::iterator it = v.begin(); it != v.end(); it++)
	{
		cout << *it << " ";
	}
	cout << endl;
}

void CHVector::test2()
{
	using namespace std;
	vector<int>v1;
	for (int i = 0; i < 10; i++)
	{
		v1.push_back(i);
	}
	printVector(v1);
	//判断是否为空
	if (v1.empty())
	{
		cout << "v1为空" << endl;
	}
	else
	{
		cout <<v1[0]<< v1[1]<<v1.at(2);
		cout << "v1不为空" << endl;
		cout << "v1容量为:" << v1.capacity() << endl;
		cout << "v1的大小：" << v1.size() << endl;
	}
	v1.resize(15, 100);//利用重载版本，指定过长时，默认用0填充新位置; 也可以指定默认填充值,
	printVector(v1);

	v1.resize(5);//短了，超出部分会删除
	printVector(v1);
}


