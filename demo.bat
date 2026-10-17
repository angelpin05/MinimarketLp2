@echo off
cd /d "%~dp0"
java -jar dist\minimarket-lp2.jar --spring.profiles.active=demo
pause
