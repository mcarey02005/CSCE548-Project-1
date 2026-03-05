param(
    [string]$BaseUrl = "http://localhost:8080",
    [string]$JarPath = "taskmanager-service/target/taskmanager-service-1.0-SNAPSHOT.jar",
    [int]$StartupWaitSeconds = 8
)

$ErrorActionPreference = "Stop"

function Resolve-BrowserPath {
    $candidates = @(
        "C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe",
        "C:\Program Files\Google\Chrome\Application\chrome.exe",
        "C:\Program Files (x86)\Google\Chrome\Application\chrome.exe"
    )

    foreach ($candidate in $candidates) {
        if (Test-Path $candidate) {
            return $candidate
        }
    }

    throw "No supported browser found. Install Microsoft Edge or Google Chrome."
}

function Save-Screenshot {
    param(
        [string]$BrowserPath,
        [string]$OutputPath,
        [string]$Url,
        [string]$Size,
        [int]$BudgetMs
    )

    $commonArgs = @(
        "--disable-gpu",
        "--hide-scrollbars",
        "--no-first-run",
        "--no-default-browser-check",
        "--window-size=$Size",
        "--virtual-time-budget=$BudgetMs",
        "--screenshot=$OutputPath",
        $Url
    )

    & $BrowserPath "--headless=new" @commonArgs | Out-Null
    if ($LASTEXITCODE -ne 0) {
        & $BrowserPath "--headless" @commonArgs | Out-Null
        if ($LASTEXITCODE -ne 0) {
            throw "Screenshot command failed for $Url"
        }
    }
}

$browserPath = Resolve-BrowserPath
$jarAbsolute = if ([System.IO.Path]::IsPathRooted($JarPath)) {
    $JarPath
} else {
    Join-Path $PSScriptRoot $JarPath
}

if (-not (Test-Path $jarAbsolute)) {
    throw "Service jar not found at: $jarAbsolute"
}

$outputDir = Join-Path $PSScriptRoot "screenshots\Project3"
New-Item -ItemType Directory -Force -Path $outputDir | Out-Null

$service = Start-Process -FilePath "java" -ArgumentList "-jar `"$jarAbsolute`"" -PassThru

try {
    Start-Sleep -Seconds $StartupWaitSeconds

    $shots = @(
        @{
            File = "project3-main-demo.png"
            Url = "$BaseUrl/?autorun=1"
            Size = "1750,2050"
            BudgetMs = 18000
        },
        @{
            File = "project3-users-categories.png"
            Url = "$BaseUrl/?autorun=1&panel=users"
            Size = "1600,1300"
            BudgetMs = 18000
        },
        @{
            File = "project3-tasks-log.png"
            Url = "$BaseUrl/?autorun=1&panel=tasks"
            Size = "1600,1500"
            BudgetMs = 18000
        }
    )

    foreach ($shot in $shots) {
        $outputPath = Join-Path $outputDir $shot.File
        Save-Screenshot -BrowserPath $browserPath -OutputPath $outputPath -Url $shot.Url -Size $shot.Size -BudgetMs $shot.BudgetMs
        Write-Host "Saved: $outputPath"
    }
}
finally {
    $javaProcesses = Get-CimInstance Win32_Process -Filter "Name = 'java.exe'" | Where-Object {
        $_.CommandLine -and $_.CommandLine.Contains($jarAbsolute)
    }

    foreach ($proc in $javaProcesses) {
        Stop-Process -Id $proc.ProcessId -Force -ErrorAction SilentlyContinue
    }

    if ($service -and -not $service.HasExited) {
        Stop-Process -Id $service.Id -Force -ErrorAction SilentlyContinue
    }
}
