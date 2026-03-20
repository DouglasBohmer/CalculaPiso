package PisoAsso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.swing.JOptionPane;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

public class EstoqueScraper {

    private static final String PESQUISA_URL = "https://redeasso.areacentral.com.br/401/?pg=associado_catalogos_produtos&ordenacao=nome-asc";
    private static final String LOGIN_URL = "https://redeasso.areacentral.com.br/401/?pg=associado_catalogos_produtos&ordenacao=nome-asc";
    private static final String CHROME_PROFILE_PATH = System.getProperty("user.home") + "\\selenium\\ChromeProfile"; 
    public static final String COOKIE_PATH = "\\\\Usuario-pc\\arquivos compartilhados\\Calcula Piso\\PisoAsso\\Extras\\cookie.txt";
    
    public static String estoque = "0";
    public static String status = "N/D";
    public static String nome = "";
    public static String valor = "0,00"; 
    public static String multiplo = "0";
    public static boolean produtoNaoEncontrado = false;
    
    private static final String[] CODIGOS_TESTE = {"2176596", "3989", "2177133"};
    private static WebDriver driverLogin = null;

    // =========================================================================================
    // MÉTODOS PARA O FLUXO AUTOMATIZADO DE LOGIN (ESTRATÉGIA ESPIÃO CEGO V2)
    // =========================================================================================

    public static void abrirNavegadorApenas() {
        String chromePath = getChromePath();
        if (chromePath == null) {
            JOptionPane.showMessageDialog(null, "Google Chrome não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // [MARCAÇÃO] Configuração manual de driver removida (Uso do Selenium Manager ativo)
        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");

        try {
            new File(CHROME_PROFILE_PATH).mkdirs();
            String cmd = "\"" + chromePath + "\" --remote-debugging-port=9222 --user-data-dir=\"" + CHROME_PROFILE_PATH + "\" \"" + LOGIN_URL + "\"";
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao abrir Chrome: " + e.getMessage());
        }
    }

    // NOVA VERSÃO: Procura por parciais sem acento para evitar erros de codificação do Windows
    public static boolean isLoginConcluidoPeloTitulo() {
        try {
            // Executa o tasklist
            Process process = Runtime.getRuntime().exec("tasklist /V /FI \"IMAGENAME eq chrome.exe\" /FO LIST");
            Scanner scanner = new Scanner(process.getInputStream());
            
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().toLowerCase();
                
                if (linha.contains("por produtos") || linha.contains("catalogo")) {
                    if (!linha.contains("central") && !linha.contains("login") && !linha.contains("acesso")) {
                        scanner.close();
                        return true; 
                    }
                }
            }
            scanner.close();
        } catch (Exception e) {
            // Silencioso
        }
        return false;
    }

    public static boolean capturarCookiePosLogin() {
        if (!isChromeDebugOpen()) return false;
        
        try {
            if (driverLogin == null) {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
                // [MARCAÇÃO] Instância com download automatizado pelo Selenium Manager
                driverLogin = new ChromeDriver(options);
            }

            String phpsessid = null;
            try {
                phpsessid = driverLogin.manage().getCookieNamed("PHPSESSID").getValue();
            } catch (Exception e) {
                return false;
            }

            if (phpsessid != null && !phpsessid.isEmpty()) {
                if (validarCookieString(phpsessid)) {
                    salvarCookieEmArquivo(phpsessid);
                    return true;
                }
            }
        } catch (Exception e) {
            // Silencioso
        }
        return false;
    }

    public static void fecharChrome() {
        if (driverLogin != null) {
            try { 
                driverLogin.close(); 
                driverLogin.quit();  
            } catch (Exception e) {}
            driverLogin = null;
        }
        try {
            Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
        } catch (IOException e) {}
    }


    public static boolean validarCookieString(String cookie) {
        for (String codigoTeste : CODIGOS_TESTE) {
            try {
                Document doc = Jsoup.connect(PESQUISA_URL)
                        .cookie("PHPSESSID", cookie)
                        .data("filtrar", "#") 
                        .data("filtro[]", "PF.REFERENCIA")
                        .data("operador[]", "IGUAL")
                        .data("valor_filtro1[]", codigoTeste)
                        .post();
                if (doc.selectFirst("b:matchesOwn(^Múltiplo:)") != null) {
                    return true; 
                }
            } catch (IOException e) { }
        }
        return false;
    }

    public static int buscarEstoque(String codigo) throws IOException {
        Path path = Paths.get(COOKIE_PATH);
        if (!Files.exists(path)) { Files.createFile(path); return 0; }

        List<String> linhas = Files.readAllLines(path);
        if (linhas.isEmpty()) return 0;

        int inicio = Math.max(0, linhas.size() - 10);
        List<String> ultimas = linhas.subList(inicio, linhas.size());

        for (int i = ultimas.size() - 1; i >= 0; i--) {
            String cookie = ultimas.get(i).trim();
            if (cookie.isEmpty()) continue;

            try {
                Document doc = Jsoup.connect(PESQUISA_URL)
                        .cookie("PHPSESSID", cookie)
                        .data("filtrar", "#")
                        .data("limit-filtro", "960")
                        .data("filtro[]", "PF.REFERENCIA")
                        .data("operador[]", "IGUAL")
                        .data("valor_filtro1[]", codigo)
                        .post();

                Element multiploLabel = doc.selectFirst("b:matchesOwn(^Múltiplo:)");

                if (multiploLabel != null) {
                    multiplo = multiploLabel.nextSibling().toString().trim().replace(",", ".");
                    Element estoqueLabel = doc.selectFirst("b:matchesOwn(^Estoque:)");
                    estoque = (estoqueLabel != null) ? estoqueLabel.nextSibling().toString().trim().replace(",", ".") : "0";
                    Element statusElement = doc.selectFirst("span.marcador[data-codigo='']");
                    status = (statusElement != null) ? statusElement.text().trim() : "N/D";
                    Element nomeElement = doc.selectFirst("div.product-name > div:first-child");
                    nome = (nomeElement != null) ? nomeElement.text().trim().replaceAll("- 1xM2", "").trim() : "Sem Nome";
                    Element valorElement = doc.selectFirst("span[data-toggle='popover'][data-trigger='hover']");
                    valor = (valorElement != null) ? valorElement.text().trim().replace("R$", "").trim() : "0,00";
                    return 1;
                } else {
                    return 2;
                }
            } catch (IOException e) { }
        }
        return 0;
    }

    private static void salvarCookieEmArquivo(String cookieValue) {
        try {
            Path path = Paths.get(COOKIE_PATH);
            if (Files.exists(path)) {
                List<String> lines = Files.readAllLines(path);
                if (!lines.isEmpty()) {
                    String lastCookie = lines.get(lines.size() - 1).trim();
                    if (lastCookie.equals(cookieValue)) return; 
                }
            }
            
            try (FileWriter fw = new FileWriter(COOKIE_PATH, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.newLine();
                bw.write(cookieValue);
            }
        } catch (IOException e) { }
    }

    public static boolean validarCookie() throws IOException {
        Path path = Paths.get(COOKIE_PATH);
        if (!Files.exists(path)) return false;

        List<String> linhas = Files.readAllLines(path);
        if (linhas.isEmpty()) return false;

        int inicio = Math.max(0, linhas.size() - 5);
        List<String> ultimas = linhas.subList(inicio, linhas.size());

        for (int i = ultimas.size() - 1; i >= 0; i--) {
            String cookie = ultimas.get(i).trim();
            if (cookie.isEmpty()) continue;

            if (validarCookieString(cookie)) {
                return true;
            }
        }
        return false;
    }
    
    private static String getChromePath() {
        String[] possiblePaths = {"C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe"};
        for (String p : possiblePaths) if (new File(p).exists()) return p;
        return null;
    }

    private static boolean isChromeDebugOpen() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("127.0.0.1", 9222), 1000); 
            return true;
        } catch (IOException e) { return false; }
    }
}