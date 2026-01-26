; --- Bloqueia teclado e mouse ---
BlockInput, On
BlockInput, MouseMove

Run, chrome.exe "https://redeasso.areacentral.com.br/401/?pg=associado_catalogos_produtos&ordenacao=nome-asc"
Sleep, 5000

Send, ^w   ; CTRL+W fecha a aba
Sleep, 500

; --- Libera teclado e mouse ---
BlockInput, MouseMoveOff
BlockInput, Off

Run, chrome.exe

MsgBox, Computador liberado!
