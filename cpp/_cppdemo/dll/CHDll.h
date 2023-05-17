#pragma once
#define DllExport __declspec(dllexport)

#include <iostream>
class DllExport CHDll
{
	int ID;
public:
	void test();
	void setID(int id);
	int getID();
};

extern "C" DllExport void dlltest(char* str);