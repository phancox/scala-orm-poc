@rem ==========================================================================
@rem Script to build a debug version of the Java run-time classses.
@rem e.g., "BuildJDK 1.7.0_05"
@rem ==========================================================================

@echo off
setlocal
set VERSION=%1
set JDK=jdk%VERSION%
set JAVA_HOME=C:\Program Files\Java\%JDK%
set BUILD_HOME=C:\tmp\%JDK%
set PATH=%JAVA_HOME%\bin;%PATH%

rem goto :SKIPUNPACK
rd /s %BUILD_HOME%
md %BUILD_HOME%\src
cd /d %BUILD_HOME%\src
wzunzip -d "%JAVA_HOME%\src.zip"
md ..\build
cd ..\build
wzunzip -d "%JAVA_HOME%\jre\lib\rt.jar"

:SKIPUNPACK

cd /d %BUILD_HOME%\src
C:\Programs\cygwin\bin\find.exe ( -path "./*sun*" -prune -o -path "./org/*" -prune ) -type f -o -name "*.java" > files.txt
set BOOTCLASSPATH=%JAVA_HOME%\jre\lib\rt.jar;%JAVA_HOME%\jre\lib\jce.jar;%JAVA_HOME%\jre\lib\jsse.jar;%JAVA_HOME%\jre\lib\resources.jar;%JAVA_HOME%\jre\lib\charsets.jar;%JAVA_HOME%\jre\lib\deploy.jar
javac -verbose -nowarn -g -d ..\build -J-Xmx4G -bootclasspath "%BOOTCLASSPATH%" -classpath "%BUILD_HOME%\src" -sourcepath "%BUILD_HOME%\src" @files.txt

cd ..\build
jar cvf ..\rt-debug-%VERSION%.jar *

