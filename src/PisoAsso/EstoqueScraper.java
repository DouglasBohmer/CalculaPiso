package PisoAsso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

public class EstoqueScraper {

    private static final String PESQUISA_URL = "https://redeasso.areacentral.com.br/401/?pg=associado_catalogos_produtos&ordenacao=nome-asc";
    private static final String LOGIN_URL = "https://redeasso.areacentral.com.br/401/?pg=associado_catalogos_produtos&ordenacao=nome-asc";
    private static final String CHROME_PROFILE_PATH = "C:\\selenium\\ChromeProfile"; 
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
    // MÉTODOS PARA O FLUXO AUTOMATIZADO DE LOGIN (ANTI-CAPTCHA)
    // =========================================================================================

    public static void abrirNavegadorApenas() {
        String chromePath = getChromePath();
        if (chromePath == null) {
            JOptionPane.showMessageDialog(null, "Google Chrome não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!configurarDriver(chromePath)) return;
        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");

        try {
            new File(CHROME_PROFILE_PATH).mkdirs();
            // COMANDO ORIGINAL DE ABERTURA - Sem chamar a atenção do Captcha
            String cmd = "\"" + chromePath + "\" --remote-debugging-port=9222 --user-data-dir=\"" + CHROME_PROFILE_PATH + "\" \"" + LOGIN_URL + "\"";
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao abrir Chrome: " + e.getMessage());
        }
    }

    // NOVA TÁTICA: Espiona silenciosamente se a tela do Captcha (Cloudflare) já sumiu 
    // antes de injetar o Selenium, garantindo que você não seja bloqueado como robô.
    private static boolean isSeguroParaConectarSelenium() {
        try {
            URL url = new URL("http://127.0.0.1:9222/json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(1000);
            con.setReadTimeout(1000);
            Scanner scanner = new Scanner(con.getInputStream());
            String json = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
            
            String lowerJson = json.toLowerCase();
            // Se encontrar vestígios do Captcha na tela, retorna falso e espera.
            if (lowerJson.contains("just a moment") || 
                lowerJson.contains("um momento") || 
                lowerJson.contains("attention required") || 
                lowerJson.contains("atenção") ||
                lowerJson.contains("cloudflare")) {
                return false; 
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void tentaCapturarCookieBackground() {
        if (!isChromeDebugOpen()) return; 

        // Se o driver AINDA NÃO FOI CONECTADO
        if (driverLogin == null) {
            // Se a tela for o Captcha, nem tenta conectar para não dar erro de "Não sou humano"
            if (!isSeguroParaConectarSelenium()) {
                return; 
            }
            // Tela limpa! Conecta o Selenium
            try {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
                driverLogin = new ChromeDriver(options);
            } catch (Exception e) {
                return; 
            }
        }

        // Se já está conectado com sucesso, tenta pegar o cookie
        try {
            String phpsessid = null;
            try {
                phpsessid = driverLogin.manage().getCookieNamed("PHPSESSID").getValue();
            } catch (Exception e) {
                return;
            }

            if (phpsessid != null && !phpsessid.isEmpty()) {
                // Testa o cookie rapidinho no site para confirmar se o login foi feito
                if (validarCookieString(phpsessid)) {
                    salvarCookieEmArquivo(phpsessid);
                }
            }
        } catch (Exception e) {}
    }

    public static void fecharChrome() {
        if (driverLogin != null) {
            try { 
                driverLogin.close(); // Fecha apenas a guia controlada
                driverLogin.quit();  // Desconecta e limpa memória
            } catch (Exception e) {}
            driverLogin = null;
        }
        try {
            // Garante que o executável fantasma não fique na memória
            Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
        } catch (IOException e) {}
    }

    // =========================================================================================
    // MÉTODOS DE BUSCA E VALIDAÇÃO
    // =========================================================================================

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

    private static boolean configurarDriver(String chromePath) {
        String driverFolderPath = "\\\\USUARIO-PC\\arquivos compartilhados\\Calcula Piso\\PisoAsso\\Extras\\WebDriver\\";
        String driverPath109 = driverFolderPath + "chromedriver109.exe";
        try {
            String command = "wmic datafile where name=\"" + chromePath.replace("\\", "\\\\") + "\" get Version /value";
            Process process = Runtime.getRuntime().exec(command);
            Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\\A");
            String output = scanner.hasNext() ? scanner.next() : "";
            String majorVersion = "";
            if (!output.trim().isEmpty()) {
                String[] lines = output.split("\n");
                for (String line : lines) {
                    if (line.startsWith("Version=")) {
                        majorVersion = line.substring("Version=".length()).trim().split("\\.")[0];
                        break;
                    }
                }
            }
            String driverPathToUse = majorVersion.equals("109") ? driverPath109 : driverFolderPath + "chromedriver" + majorVersion + ".exe";
            if (new File(driverPathToUse).exists()) {
                System.setProperty("webdriver.chrome.driver", driverPathToUse);
                return true;
            } else {
                mostrarErroDriver(majorVersion, driverPathToUse);
                return false;
            }
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    private static void mostrarErroDriver(String version, String path) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Driver não encontrado para Chrome v" + version));
        panel.add(new JLabel("Esperado: " + path));
        JButton linkButton = new JButton("Baixar Driver");
        linkButton.setForeground(Color.BLUE.darker());
        linkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkButton.addActionListener(e -> {
            try { Desktop.getDesktop().browse(new URI("https://googlechromelabs.github.io/chrome-for-testing/")); } catch (Exception ex) {}
        });
        panel.add(linkButton);
        JOptionPane.showMessageDialog(null, panel, "Erro Driver", JOptionPane.ERROR_MESSAGE);
    }
}