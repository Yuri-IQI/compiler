@echo off
set FILE_PATH=%~dp1
set PROGRAM_NAME=%~n1

if "%FILE_PATH:~-1%"=="\" set FILE_PATH=%FILE_PATH:~0,-1%

if "%FILE_PATH%"=="" set FILE_PATH=.

C:\masm32\bin\ml.exe /c /coff /IC:\masm32\include /Fo"%FILE_PATH%\%PROGRAM_NAME%.obj" "%FILE_PATH%\%PROGRAM_NAME%.asm"

C:\masm32\bin\link.exe /SUBSYSTEM:CONSOLE /MACHINE:X86 /LIBPATH:C:\masm32\lib /OUT:"%FILE_PATH%\%PROGRAM_NAME%.exe" "%FILE_PATH%\%PROGRAM_NAME%.obj" kernel32.lib

"%FILE_PATH%\%PROGRAM_NAME%.exe"

:fim
echo.
echo Processo finalizado.