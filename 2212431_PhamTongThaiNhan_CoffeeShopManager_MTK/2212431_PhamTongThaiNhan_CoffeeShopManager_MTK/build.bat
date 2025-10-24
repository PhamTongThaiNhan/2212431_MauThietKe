\
@echo off
setlocal ENABLEDELAYEDEXPANSION
set SRC=Source
set OUT=out
set JAR=ThucThi\CoffeeShopManager.jar

if exist "%OUT%" rmdir /s /q "%OUT%"
mkdir "%OUT%"

echo Collecting sources...
dir /b /s "%SRC%\*.java" > sources.txt

echo Compiling...
javac -encoding UTF-8 -d "%OUT%" @sources.txt
if errorlevel 1 (
    echo Compile failed.
    pause
    exit /b 1
)

echo Creating jar...
if not exist ThucThi mkdir ThucThi
jar cfe "%JAR%" coffee.main.Main -C "%OUT%" .
if errorlevel 1 (
    echo JAR creation failed.
    pause
    exit /b 1
)

echo Done. Created "%JAR%"
echo Run with: run.bat
endlocal
