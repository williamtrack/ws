#include "CHShare.h"
#include <windows.h>
#include <iostream>

void CHShare::test()
{

#define  BUF_SIZE 256
#define  FILE_MAP_NAME  L"Global//001"
	HANDLE hMapFile = NULL;
	hMapFile = CreateFileMapping(INVALID_HANDLE_VALUE, //创建一个物理文件无关的内存映射
		NULL,
		PAGE_READWRITE,
		0, /*一定为0 否则磁盘空间不足*/
		BUF_SIZE, /*不能为0*/
		FILE_MAP_NAME /*Global/" or "Local/" 其他都不能带有/ */
	);
	if (hMapFile == NULL)
	{
		DWORD dwErr = GetLastError();
		return;
	}
	void* pMapBuf = NULL;
	//文件中的数据映射到进程的虚拟内存中, 指向文件视图的指针
	if ((pMapBuf = MapViewOfFile(hMapFile, FILE_MAP_ALL_ACCESS, 0, 0, BUF_SIZE)) == NULL)
	{
		DWORD dwErr = GetLastError();
		return;
	}
	memset(pMapBuf, 0, 100);
	strcpy((char*)pMapBuf, "create file text");
	UnmapViewOfFile(pMapBuf);
	//CloseHandle(hMapFile);//如果此时关闭句柄映射,则其他线程无法使用



	//The Second Process :
	HANDLE hMapFile2 = NULL;
	if ((hMapFile2 = OpenFileMapping(
		FILE_MAP_ALL_ACCESS,
		FALSE,
		//NULL,
		FILE_MAP_NAME
	)) == NULL)
	{
		DWORD dwErr = GetLastError();
		return;
	}
	void* pMapBuf2 = NULL;
	if ((pMapBuf2 = MapViewOfFile(hMapFile2, FILE_MAP_ALL_ACCESS, 0, 0, BUF_SIZE)) == NULL)
	{
		DWORD dwErr = GetLastError();
		return;
	}
	//strcpy((char*)pMapBuf2, "open file text");
	std::cout << (char*)pMapBuf2 << std::endl;

	UnmapViewOfFile(pMapBuf2);//关闭映射
	CloseHandle(hMapFile2);//关闭内存映射文件的句柄
	CloseHandle(hMapFile);
}

#include <stdio.h>
int CHShare::fileTest()
{

	//将第1个文件打开，第2个文件打开，将文件1的内容读到第2个文件中
#define BUF_SIZE 256
		HANDLE hIn, hOut;
		DWORD size_in, size_out;
		CHAR buffer[BUF_SIZE];
		hIn = CreateFile(L"d://test.txt",
			GENERIC_READ,
			FILE_SHARE_READ,
			NULL,
			OPEN_EXISTING,
			FILE_ATTRIBUTE_NORMAL,
			NULL);
		if (hIn == INVALID_HANDLE_VALUE)
		{
			printf("不能打开文件。错误代码：%d\n", GetLastError());
			return 1;
		}

		hOut = CreateFile(L"d://test2.txt",
			GENERIC_WRITE,
			0,
			NULL,
			CREATE_ALWAYS,
			FILE_ATTRIBUTE_NORMAL,
			NULL);
		if (hOut == INVALID_HANDLE_VALUE)
		{
			printf("不能打开文件。错误代码：%d\n", GetLastError());
			return 2;
		}

		while (ReadFile(hIn, buffer, BUF_SIZE, &size_in, NULL) && size_in)
		{
			WriteFile(hOut, buffer, size_in, &size_out, NULL);
			if (size_in != size_out)
			{
				printf("产生严重错误！\n错误代码：%d\n", GetLastError());
				return 3;
			}
		}

		CloseHandle(hIn);
		CloseHandle(hOut);
		return 0;
}
