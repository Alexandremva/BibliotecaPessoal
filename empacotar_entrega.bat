@echo off
setlocal

rem ============================================================
rem  Empacota o projeto Biblioteca Pessoal para entrega.
rem  1) Remove .gradle, build e app\build
rem  2) Gera C:\Users\alexa\BibliotecaPessoal_Entrega.zip
rem     (sem incluir este proprio .bat)
rem ============================================================

rem Garante que o script rode a partir da raiz do projeto
pushd "%~dp0"

set "DESTINO=C:\Users\alexa\BibliotecaPessoal_Entrega.zip"
set "SCRIPT=%~nx0"

echo.
echo [1/2] Limpando pastas temporarias...
for %%P in (".gradle" "build" "app\build") do (
    if exist "%%~P\" (
        rmdir /s /q "%%~P"
        if exist "%%~P\" (
            echo   ERRO: nao foi possivel remover "%%~P".
            echo   Feche o Android Studio e execute novamente.
            popd
            pause
            exit /b 1
        )
        echo   Removida: %%~P
    ) else (
        echo   Nao existe: %%~P
    )
)

echo.
echo [2/2] Compactando projeto em "%DESTINO%"...
if exist "%DESTINO%" del /f /q "%DESTINO%"

powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference = 'Stop'; Get-ChildItem -LiteralPath '.' -Force | Where-Object { $_.Name -ne $env:SCRIPT } | Compress-Archive -DestinationPath $env:DESTINO -CompressionLevel Optimal -Force"

if errorlevel 1 (
    echo.
    echo ERRO: falha ao gerar o arquivo ZIP.
    popd
    pause
    exit /b 1
)

echo.
echo Concluido! Arquivo gerado: %DESTINO%
popd
pause
endlocal
