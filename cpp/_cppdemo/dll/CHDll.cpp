#include "CHDll.h"

void CHDll::test()
{
	printf("This is a dllTest class");
}

void CHDll::setID(int id)
{
	ID = id;
}

int CHDll::getID()
{
	printf("The id is %d.\n", ID);
	return ID;
}

extern "C" void dlltest(char* str) {
	printf("The str is %s.\n", str);
}