#include "CHCmdHandler.h"
#include <Windows.h>
#include <stdio.h>
#include <tchar.h>
#include <string>

#define EXCEPTIION_STATE_CHECK \
    if (!m_bInit) return E_NOTIMPL

#define SYSTEM_PAUSE system("pause")

void OnCommandEvent(const CHCmdParam * pParam, HRESULT hResultCode, char* szResult);

int testCHCmdHandler()
{
    CHCmdParam cmdParam;
    CCmdHandler cmdHandler;
    HRESULT cmdResult = S_OK;

    ZeroMemory(&cmdParam, sizeof(cmdParam));
    cmdParam.iSize = sizeof(CHCmdParam);
    // 这里测试d目录下，命令格式为 cmd.exe /C + 命令
    //TCHAR *szCmd = _T("cmd.exe /C dir D:\\&& echo S_OK || echo E_FAIL");
    //TCHAR cmd[] = TEXT("cmd.exe /C dir D:\\&& echo S_OK || echo E_FAIL");
    TCHAR cmd[] = _T("cmd.exe /C dir D:\\&& echo S_OK || echo E_FAIL");
    TCHAR* szCmd = cmd;
    _tcscpy_s(cmdParam.szCommand, szCmd);
    cmdParam.OnCmdEvent = OnCommandEvent;
    cmdParam.iTimeOut = 3000;

    cmdResult = cmdHandler.Initalize();
    if (cmdResult != S_OK)
    {
        printf("cmd handler 初始化失败\n");
        SYSTEM_PAUSE;
        return 0;
    }
    cmdResult = cmdHandler.HandleCommand(&cmdParam);
    if (cmdResult != S_OK)
    {
        printf("cmd handler 执行命令接口调用失败\n");
        cmdHandler.Finish();
        SYSTEM_PAUSE;
        return 0;
    }
    system("pause");
    return 0;
}

CCmdHandler::CCmdHandler()
    : m_bInit(FALSE)
    , m_dwErrorCode(0)
    , m_hPipeRead(NULL)
    , m_hPipeWrite(NULL)
{
    ZeroMemory(m_szReadBuffer, sizeof(m_szReadBuffer));
    ZeroMemory(m_szWriteBuffer, sizeof(m_szWriteBuffer));
    ZeroMemory(&m_CommandParam, sizeof(m_CommandParam));
}
CCmdHandler::~CCmdHandler()
{
}

HRESULT CCmdHandler::Initalize()
{
    // 初始化，创建匿名管道
    if (m_bInit) return S_OK;
    m_bInit = TRUE;
    ZeroMemory(m_szReadBuffer, sizeof(m_szReadBuffer));
    ZeroMemory(&m_saOutPipe, sizeof(m_saOutPipe));
    m_saOutPipe.nLength = sizeof(SECURITY_ATTRIBUTES);
    m_saOutPipe.lpSecurityDescriptor = NULL;
    m_saOutPipe.bInheritHandle = TRUE;
    ZeroMemory(&m_startupInfo, sizeof(STARTUPINFO));
    ZeroMemory(&m_processInfo, sizeof(PROCESS_INFORMATION));
    if (!CreatePipe(&m_hPipeRead, &m_hPipeWrite, &m_saOutPipe, PIPE_BUFFER_SIZE))
    {
        m_dwErrorCode = GetLastError();
        return E_FAIL;
    }
    return S_OK;
}
HRESULT CCmdHandler::Finish()
{
    EXCEPTIION_STATE_CHECK;
    if (m_hPipeRead)
    {
        CloseHandle(m_hPipeRead);
        m_hPipeRead = NULL;
    }
    if (m_hPipeWrite)
    {
        CloseHandle(m_hPipeWrite);
        m_hPipeWrite = NULL;
    }
    return S_OK;
}
HRESULT CCmdHandler::HandleCommand(CHCmdParam* pCommmandParam)
{
    EXCEPTIION_STATE_CHECK;
    if (!pCommmandParam || pCommmandParam->iSize != sizeof(CHCmdParam))
        return E_INVALIDARG;
    if (_tcslen(pCommmandParam->szCommand) <= 0)
        return E_UNEXPECTED;
    memset(&m_CommandParam, 0, sizeof(m_CommandParam));
    m_CommandParam = *pCommmandParam;
    return ExecuteCmdWait();
}
HRESULT CCmdHandler::ExecuteCmdWait()
{
    EXCEPTIION_STATE_CHECK;
    HRESULT hResult = E_FAIL;
    DWORD dwReadLen = 0;
    DWORD dwStdLen = 0;
    m_startupInfo.cb = sizeof(STARTUPINFO);
    m_startupInfo.dwFlags = STARTF_USESTDHANDLES | STARTF_USESHOWWINDOW;
    m_startupInfo.hStdOutput = m_hPipeWrite;
    m_startupInfo.hStdError = m_hPipeWrite;
    m_startupInfo.wShowWindow = SW_HIDE;
    DWORD dTimeOut = m_CommandParam.iTimeOut >= 3000 ? m_CommandParam.iTimeOut : 5000;
    do
    {
        if (!CreateProcess(NULL, m_CommandParam.szCommand,
            NULL, NULL, TRUE, CREATE_NEW_CONSOLE, NULL, NULL,
            &m_startupInfo, &m_processInfo))
        {
            m_dwErrorCode = GetLastError();
            hResult = E_FAIL;
            break;
        }
        if (WAIT_TIMEOUT == WaitForSingleObject(m_processInfo.hProcess, dTimeOut))
        {
            m_dwErrorCode = GetLastError();
            hResult = CO_E_SERVER_START_TIMEOUT;
            if (m_CommandParam.OnCmdEvent)
                m_CommandParam.OnCmdEvent(&m_CommandParam, CO_E_SERVER_START_TIMEOUT, NULL);
            break;
        }
        // 预览管道中数据的内容
        if (!PeekNamedPipe(m_hPipeRead, NULL, 0, NULL, &dwReadLen, NULL)
            || dwReadLen <= 0)
        {
            m_dwErrorCode = GetLastError();
            hResult = E_FAIL;
            break;
        }
        else
        {
            ZeroMemory(m_szPipeOut, sizeof(m_szPipeOut));
            // 读取管道中的数据
            if (ReadFile(m_hPipeRead, m_szPipeOut, dwReadLen, &dwStdLen, NULL))
            {
                hResult = S_OK;
                if (m_CommandParam.OnCmdEvent)
                    m_CommandParam.OnCmdEvent(&m_CommandParam, S_OK, m_szPipeOut);
                break;
            }
            else
            {
                m_dwErrorCode = GetLastError();
                break;
            }
        }
    } while (0);
    if (m_processInfo.hThread)
    {
        CloseHandle(m_processInfo.hThread);
        m_processInfo.hThread = NULL;
    }
    if (m_processInfo.hProcess)
    {
        CloseHandle(m_processInfo.hProcess);
        m_processInfo.hProcess = NULL;
    }
    return hResult;
}

void OnCommandEvent(const CHCmdParam* pParam, HRESULT hResultCode, char* szResult)
{
    if (!szResult || !szResult[0]) return;
    if (!pParam || hResultCode != S_OK) return;
    printf("============== 回调 ==============\n");
    std::string echo_data(szResult);
    std::string s_ok("S_OK");
    std::string::size_type pos = echo_data.find(s_ok);
    if (pos != std::string::npos)
        printf("命令执行成功\n");
    else
        printf("命令执行失败\n");
    printf("执行返回的结构:\n");
    printf("========================================\n");
    printf("%s\n", szResult);
}




//=====others=====

void testShellExecute() {
    //WinExec("Notepad.exe C:/Users/William/Desktop/merge.bat", SW_SHOW);
    //ShellExecute(NULL, "open", "notepad.exe", NULL, NULL, SW_SHOWNORMAL);
    //ShellExecute(NULL, "open", "notepad.exe", "C:/Users/William/Desktop/test.txt", NULL, SW_SHOWNORMAL);
    //Unicode版本
    //ShellExecute(NULL, TEXT("open"),TEXT( "notepad.exe"), TEXT("C:/Users/William/Desktop/test.txt"), NULL, SW_SHOWNORMAL);
    //ShellExecute(NULL, TEXT("open"), TEXT("C:/Users/William/AppData/Local/Google/Chrome/Application/chrome.exe"), TEXT("www.baidu.com"), NULL, SW_SHOWNORMAL);
    //ShellExecute(NULL, TEXT("open"), TEXT("C:/Program Files/Git/cmd/git.exe"), TEXT("git status"), NULL, SW_SHOWNORMAL);

    LPCWSTR strCmd4 = TEXT("/k git status");
    ShellExecute(NULL, TEXT("open"), TEXT("cmd.exe"), strCmd4, NULL, SW_SHOW);
}

void testCreateProcess() {
    TCHAR cmd[] = TEXT("cmd.exe /c cd /D d:\\ && dir");
    BOOL run_pipe;
    PROCESS_INFORMATION pi;
    STARTUPINFO si;
    BOOL ret = FALSE;
    DWORD flags = CREATE_NO_WINDOW;

    char pBuffer[1000];
    SECURITY_ATTRIBUTES sa;
    sa.nLength = sizeof(SECURITY_ATTRIBUTES);
    sa.lpSecurityDescriptor = NULL;
    sa.bInheritHandle = TRUE;
    HANDLE hReadPipe, hWritePipe;
    run_pipe = CreatePipe(&hReadPipe, &hWritePipe, &sa, 0);
    printf("run_pipe=%d\n", run_pipe);

    ZeroMemory(&pi, sizeof(PROCESS_INFORMATION));
    ZeroMemory(&si, sizeof(STARTUPINFO));
    si.cb = sizeof(STARTUPINFO);
    si.dwFlags |= STARTF_USESTDHANDLES;
    si.hStdInput = NULL;
    si.hStdError = hWritePipe;
    si.hStdOutput = hWritePipe;

    // Start the child process. 
    //cmd:命令  执行可能需要 "cmd.exe /c ping baidu.com"  需要/c才能执行 执行完命令行马上关闭/ k 执行完命令行不关闭
    ret = CreateProcess(
        NULL,   // No module name (use command line)
        cmd, //(LPWSTR)cmd.c_str(),     // Command line
        NULL,           // Process handle not inheritable
        NULL,           // Thread handle not inheritable
        TRUE,            // Set handle inheritance to FALSE
        flags,          // No creation flags
        NULL,           // Use parent's environment block
        NULL,           // Use parent's starting directory 
        &si,            // Pointer to STARTUPINFO structure
        &pi           // Pointer to PROCESS_INFORMATION structure
    );
    if (ret)
    {
        while (true)
        {
            DWORD ExitCode = 0;
            //判断进程是否执行结束
            GetExitCodeProcess(pi.hProcess, &ExitCode);
            if (ExitCode == STILL_ACTIVE) //正在运行
            {
                DWORD RSize = 0;
                BOOL run_s = 0;
                run_s = ReadFile(hReadPipe, pBuffer, 500, &RSize, NULL);
                pBuffer[RSize] = '\0';
                printf("return:%d,%d,%s\n", run_s, RSize, pBuffer);
            }
            else //结束
            {
                printf("finish...\n");
                break;
            }
        }

        //WaitForSingleObject(pi.hProcess, INFINITE);
        printf("执行成功....\n");
        CloseHandle(pi.hProcess);
        CloseHandle(pi.hThread);
        return;
    }
    printf("执行失败....\n");
    return;
}

int test_popen() {
    const char* cmd = "cmd.exe /c cd /d d:\ && dir";
    char MsgBuff[1024];
    int MsgLen = 1020;
    FILE* fp;
    if (cmd == NULL)
    {
        return -1;
    }
    if ((fp = _popen(cmd, "r")) == NULL)
    {
        return -2;
    }
    else
    {
        memset(MsgBuff, 0, MsgLen);
        //读取命令执行过程中的输出
        while (fgets(MsgBuff, MsgLen, fp) != NULL)
        {
            printf("%s", MsgBuff);
        }
        //关闭执行的进程
        if (_pclose(fp) == -1)
        {
            return -3;
        }
    }
    return 0;
}
