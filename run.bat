@echo off
chcp 65001 >nul
title CoffeeShopManager - SQLite Version

:: Check if JAR exists
if not exist ThucThi\CoffeeShopManager.jar (
    echo Building project...
    call build.bat
)

:: Check SQLite JDBC driver
if not exist Source\lib\sqlite-jdbc-3.50.3.0.jar (
    echo [ERROR] SQLite JDBC not found!
    echo Please download sqlite-jdbc-3.50.3.0.jar and place it in the folder: Source\lib\
    echo Download link: https://github.com/xerial/sqlite-jdbc/releases/latest
    pause
    exit /b
)

echo Running CoffeeShopManager...
echo -------------------------------------
java -Dfile.encoding=UTF-8 -cp "ThucThi\CoffeeShopManager.jar;Source\lib\sqlite-jdbc-3.50.3.0.jar" coffee.main.Main
echo -------------------------------------
pause
