Run, chrome.exe "https://redeasso.areacentral.com.br/401/?pg=associado_catalogos_produtos&ordenacao=nome-asc"
Sleep, 2000  ; espera 5 segundos para carregar o site (ajuste se precisar)

; Exemplo: se o botão "Login" fica selecionável com TAB + Enter
Send, {Tab}{Tab}{Tab}{Tab}{Tab}{Enter}

; espera completar e fecha o navegador
Sleep, 8000
Send, ^w   ; CTRL+W fecha a aba
