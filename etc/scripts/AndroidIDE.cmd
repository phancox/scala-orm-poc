@echo off
setlocal
cd /d C:\Programs\EclipseAndroid\eclipse

start /B eclipse.exe -data %SDE_HOME%\Android\workspace %1 %2 %3 %4 %5 -vmargs -Xms2G -Xmx4G -XX:PermSize=256M -XX:MaxPermSize=512M

