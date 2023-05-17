#include "CHRegex.h"
#include <iostream>
#include <regex>

void CHRegex::test()
{
	using namespace std;
	//cout << regex_match("123", regex("\\d*")) << endl;

	string str = "Hello_2018_2020";
	smatch result;
	regex pattern("(.{5})_(\\d{4})"); //匹配5个任意单字符 + 下划线 + 4个数字
	regex pattern2("_(\\d{4})"); //匹配5个任意单字符 + 下划线 + 4个数字

	//判断是否一样
	//if (regex_match(str, result, pattern))
	//{
	//	cout << "match num:" << result.size() << endl;
	//	for (int i = 0; i < result.size(); i++) {
	//		cout << result[i] << endl;
	//		//cout << result.str(i) << endl; //结果显示形式2
	//	}
	//	//完整匹配结果，Hello_2018
	//	//第一组匹配的数据，Hello  //?
	//	//第二组匹配的数据，2018   //?
	//}

	//单次寻找
	//if (regex_search(str, result, pattern))
	//{
	//	cout << "search num:" << result.size() << endl;
	//	for (int i = 0; i < result.size(); i++) {
	//		cout << result[i] << endl;
	//	}
	//}

	//多次寻找
	for (sregex_iterator it(str.begin(), str.end(), pattern2), end_it; it != end_it; ++it) { //end_it是一个空迭代器
		cout << it->str() << endl;
	}
}
