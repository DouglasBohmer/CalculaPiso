; --- Bloqueia teclado e mouse ---
BlockInput, On
BlockInput, MouseMove

Run, chrome.exe "https://redeasso.areacentral.com.br/401/?pg=associado_catalogos_produtos&ordenacao=nome-asc"
Sleep, 5000

Send, {Tab 5}{Enter}
Sleep, 10000

Send, ^+i
Sleep, 5000

Send, {Tab 2}
Sleep, 1000
Send, PHPSESSID{Enter}
Sleep, 500

Send, {Tab 18}
Sleep, 500

Send, +{F10}
Sleep, 500

Send, {Up 2}
Sleep, 500

Send, {Enter}
Sleep, 500

Send, ^c
Sleep, 500

; ---- Salva o cookie sempre no caminho de rede absoluto ----
FileAppend, %Clipboard%`n, \\Usuario-pc\arquivos compartilhados\Calcula Piso\PisoAsso\Extras\cookie.txt
Sleep, 500

Send, ^w
Sleep, 500

; --- Libera teclado e mouse ---
BlockInput, MouseMoveOff
BlockInput, Off

Run, msedge.exe "http://192.168.0.100/index.html?_1737743744254"

Sleep, 5000

MsgBox, COMPUTADOR LIBERADO PARA USO!
