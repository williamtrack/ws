#include "CHMap.h"
#include <map>
#include <string>
#include <iostream>

void CHMap::test()
{
	using namespace std;
	map<int, int> list1;
	map<int, string> list2 = { {1,"java"},{2,"c++"},{3,"python"} };
	map<int, string> list3 = { pair<int,string>(1,"java"),{2,"c++"},{3,"python"} };
	
	//添加元素
	//map中key的值是唯一的，如果插入一个已经存在的key值会导致原先值的覆盖
	list1.insert(pair<int, int>(0, 15));
	list1.insert({ 1,13 });
	list1.insert({ 2,200 });
	list1.insert({ 3,300 });
	list1.insert({ 4,400 });


	//删除元素
	list1.erase(1);

	//遍历,迭代器
	for (auto iter = list1.begin(); iter != list1.end(); ++iter) {
		cout << iter->first << "  " << iter->second << endl;
	}

	//查找元素和赋值 3种方法
	cout << list1[2]<<endl;
	cout << list1.at(3)<<endl;
	map<int, int>::iterator it = list1.find(4); //find返回迭代器
	if (it != list1.end()) cout << it->first << ' ' << it->second << endl;

	//其他用法
	map<string, int> map_1;
	map_1.clear();                //清除所有元素
	map_1.insert({ "a",10 });
	map_1.empty();                //如果为空返回1，负责返回0
	map_1.size();                 //返回容器的元素个数
	map_1.max_size();             //返回容器可以容纳的最大元素
	map_1.end();				  //一般用于迭代器判断是否找不到


	////添加
	////第一种：用insert函数插入pair数据：
	//map<int, string> my_map;
	//my_map.insert(pair<int, string>(1, "first"));
	//my_map.insert(pair<int, string>(2, "second"));

	////第二种：用insert函数插入value_type数据：
	//my_map.insert(map<int, string>::value_type(1, "first"));
	//my_map.insert(map<int, string>::value_type(2, "second"));

	//map<int, string>::iterator it;           //迭代器遍历
	//for (it = my_map.begin(); it != my_map.end(); it++)
	//	cout << it->first << it->second << endl;
}
