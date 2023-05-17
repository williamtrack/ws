
// mfcgitcmdDlg.cpp : implementation file
//

#include "pch.h"
#include "framework.h"
#include "mfcgitcmd.h"
#include "mfcgitcmdDlg.h"
#include "afxdialogex.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CmfcgitcmdDlg dialog



CmfcgitcmdDlg::CmfcgitcmdDlg(CWnd* pParent /*=nullptr*/)
	: CDialogEx(IDD_MFCGITCMD_DIALOG, pParent)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CmfcgitcmdDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CmfcgitcmdDlg, CDialogEx)
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_BN_CLICKED(IDOK, &CmfcgitcmdDlg::OnBnClickedOk)
	ON_BN_CLICKED(IDC_BUTTON1, &CmfcgitcmdDlg::OnBnClickedButton1)
	ON_BN_CLICKED(IDC_BUTTON2, &CmfcgitcmdDlg::OnBnClickedButton2)
	ON_BN_CLICKED(IDC_BUTTON3, &CmfcgitcmdDlg::OnBnClickedButton3)
	ON_BN_CLICKED(IDC_BUTTON4, &CmfcgitcmdDlg::OnBnClickedButton4)
	ON_BN_CLICKED(IDC_BUTTON5, &CmfcgitcmdDlg::OnBnClickedButton5)
END_MESSAGE_MAP()


// CmfcgitcmdDlg message handlers
BOOL CmfcgitcmdDlg::OnInitDialog()
{
	CDialogEx::OnInitDialog();

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon


	// TODO: Add extra initialization here

	return TRUE;  // return TRUE  unless you set the focus to a control
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CmfcgitcmdDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialogEx::OnPaint();
	}
}

// The system calls this function to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CmfcgitcmdDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

#include "CHCmdHandler.h"

void CmfcgitcmdDlg::OnBnClickedOk()
{
	// TODO: Add your control notification handler code here
	CDialogEx::OnOK();
}

std::string cm = "cmd.exe /c start D:/data/ws/cpp/cppmine/mfcgitcmd/mfcgitcmd/";
TCHAR cmd[100];
//TCHAR cmd[] = _T("cmd.exe /c start D:/data/ws/merge/merge-md.bat");

void CmfcgitcmdDlg::OnBnClickedButton1()
{
	//MessageBeep(1); //±¨¾¯Éù
	std::string var = "merge-md.bat";
	_stprintf_s(cmd, 100, _T("%S"), (cm+var).c_str());//%S¿í×Ö·û
	testCreateProcess(cmd);
	CDialogEx::OnOK();
}


void CmfcgitcmdDlg::OnBnClickedButton2()
{
	std::string var = "merge-ws.bat";
	_stprintf_s(cmd, 100, _T("%S"), (cm + var).c_str());
	testCreateProcess(cmd);
	CDialogEx::OnOK();
}


void CmfcgitcmdDlg::OnBnClickedButton3()
{
	std::string var = "merge-md_img.bat";
	_stprintf_s(cmd, 100, _T("%S"), (cm + var).c_str());
	testCreateProcess(cmd);
	CDialogEx::OnOK();
}


void CmfcgitcmdDlg::OnBnClickedButton4()
{
}


void CmfcgitcmdDlg::OnBnClickedButton5()
{
	std::string var = "merge-gitfile.bat";
	_stprintf_s(cmd, 100, _T("%S"), (cm + var).c_str());
	testCreateProcess(cmd);
	CDialogEx::OnOK();
}
