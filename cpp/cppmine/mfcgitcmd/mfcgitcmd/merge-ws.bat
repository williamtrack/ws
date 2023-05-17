@echo off
cd /d D:/data/ws

echo.
echo =====: git commit
git checkout dev
SET today=%date:~8,2%%date:~0,2%%date:~3,2%
git add .
git commit -am "%today%"
git remote rm origin
git remote add origin git@github.com:williamtrack/ws.git

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
echo on
for /f "delims=" %%i in ('git merge master') do set value_1=%%i
echo %value_1%
echo %value_1% | findstr /i "conflict"
echo off
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
echo %value_1% | findstr /i "conflict"
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