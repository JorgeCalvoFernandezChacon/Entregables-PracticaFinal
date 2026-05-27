@echo off
echo Compilando proyecto...
set SDK_PATH="javafx-sdk-26.0.1\lib"
set FILES=src\**\*.java

:: Usar un comando de PowerShell para obtener todos los archivos .java y compilar
powershell -Command "$files = Get-ChildItem -Recurse -Filter *.java | Where-Object { $_.FullName -notlike '*MainTest.java*' } | ForEach-Object { $_.FullName }; & javac --module-path %SDK_PATH% --add-modules javafx.controls,javafx.fxml -d bin $files"

if %ERRORLEVEL% neq 0 (
    echo Error durante la compilacion.
    pause
    exit /b %ERRORLEVEL%
)

echo Ejecutando Juego de Mazmorras...
java --module-path %SDK_PATH% --add-modules javafx.controls,javafx.fxml -cp bin ui.MainApp
pause
