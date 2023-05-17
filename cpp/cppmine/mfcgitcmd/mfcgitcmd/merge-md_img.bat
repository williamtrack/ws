@echo off
d:
cd  D:/data/md/md_img

echo.
echo =====: git commit
git checkout dev
SET today=%date:~8,2%%date:~0,2%%date:~3,2%
rem SET year=%date:~6,4%
rem SET month=%date:~0,2%
rem SET day=%date:~3,2%
rem SET tt=%time:~0,8%
rem SET Today=%year%%month%%day%_%time:~0,2%%time:~3,2%%time:~6,2%
rem ECHO %Today%
rem ECHO %date%
rem ECHO %time%
git add .
git commit -am "%today%"
git remote rm origin
git remote add origin git@github.com:williamtrack/md_img.git

echo.
echo =====: git checkout master
git checkout master
echo.
echo =====: git pull origin master
git pull origin master

echo.
echo =====: git checkout dev
git checkout dev
echo.
echo =====: git merge master
for /f "delims=" %%i in ('git merge master') do set value_1=%%i
echo %value_1%
(echo %value_1%)| findstr /i "conflict"
if ERRORLEVEL 1 (
	echo.
	echo =====: git checkout master
	git checkout master
) else (
	msg * "conflict"
	pause
	exit
)

echo.
echo =====: git merge dev
for /f "delims=" %%i in ('git merge dev') do set value_1=%%i
echo %value_1%
(echo %value_1%)| findstr /i "conflict"
if ERRORLEVEL 1 (
	echo.
	echo =====: git push origin master
	git push origin master
	echo.
	echo =====: git checkout dev
	git checkout dev
) else (
	msg * "conflict"
	pause
	exit
)
pause
exit