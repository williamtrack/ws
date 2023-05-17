#include "pch.h"
#include "CHCmdHandler.h"
#include <Windows.h>
#include <stdio.h>
#include <tchar.h>
#include <iostream>

#include <fstream>
#include <sstream>
#include <direct.h>


void testCreateProcess(TCHAR cmd[]) {
	SECURITY_ATTRIBUTES sa;
	sa.nLength = sizeof(SECURITY_ATTRIBUTES);
	sa.lpSecurityDescriptor = NULL;
	sa.bInheritHandle = TRUE;
	HANDLE hReadPipe, hWritePipe;
	BOOL run_pipe = CreatePipe(&hReadPipe, &hWritePipe, &sa, 0);

	PROCESS_INFORMATION pi;
	STARTUPINFO si;
	ZeroMemory(&pi, sizeof(PROCESS_INFORMATION));
	ZeroMemory(&si, sizeof(STARTUPINFO));
	si.cb = sizeof(STARTUPINFO);
	si.dwFlags |= STARTF_USESTDHANDLES;
	si.hStdInput = NULL;
	si.hStdError = hWritePipe;
	si.hStdOutput = hWritePipe;

	DWORD flags = CREATE_NO_WINDOW;

	//cmd:命令  执行可能需要 "cmd.exe /c ping baidu.com"  需要/c才能执行 执行完命令行马上关闭/ k 执行完命令行不关闭
	BOOL ret = CreateProcess(
		NULL,   // No module name (use command line)
		cmd, //(LPWSTR)cmd.c_str(),     // Command line
		NULL,           // Process handle not inheritable
		NULL,           // Thread handle not inheritable
		TRUE,            // Set handle inheritance to FALSE
		flags,          // No creation flags
		NULL,           // Use parent's environment block
		NULL,           // Use parent's starting directory 
		&si,            // Pointer to STARTUPINFO structure
		&pi
	);
	if (ret)
	{
		while (true)
		{
			DWORD ExitCode = 0;
			//判断进程是否执行结束
			GetExitCodeProcess(pi.hProcess, &ExitCode);
			if (ExitCode != STILL_ACTIVE)break;
		}

		//WaitForSingleObject(pi.hProcess, INFINITE);
		CloseHandle(pi.hProcess);
		CloseHandle(pi.hThread);
		return;
	}
	return;
}