@echo off
set dir=%CD%
cd src\main\java\es\unizar\unoforall
set escritorioDir=%CD%
rmdir /S /Q model
cd %dir%
cd ..\..\Backend\Proyecto\src\es\unizar\unoforall
xcopy /E /I "%CD%\model" "%escritorioDir%\model"
pause
