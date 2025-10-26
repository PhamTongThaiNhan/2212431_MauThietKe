@echo off
chcp 65001 >nul
if not exist ThucThi\CoffeeShopManager.jar (
  echo JAR not found. Building...
  call build.bat
)
echo Running CoffeeShopManager...
java -Dfile.encoding=UTF-8 -jar ThucThi\CoffeeShopManager.jar
pause
