@echo off
setlocal
cd /d C:\Programs\Eclipse\DeltaSoft2

start /B eclipse.exe -data %SDE_HOME%\DeltaSoft2\workspace %1 %2 %3 %4 %5 -vmargs -Xms2G -Xmx6G -XX:PermSize=256M -XX:MaxPermSize=512M

