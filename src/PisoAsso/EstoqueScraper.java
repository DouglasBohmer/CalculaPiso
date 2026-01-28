package PisoAsso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements; // Import novo adicionado
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

public class EstoqueScraper {

    private static final String PESQUISA_URL = "https://redeasso.areacentral.com.br/401/?pg=associado_catalogos_produtos&ordenacao=nome-asc";
    private static final String LOGIN_URL = "https://redeasso.areacentral.com.br/401/?pg=associado_catalogos_produtos&ordenacao=nome-asc";
    private static final String CHROME_PROFILE_PATH = "C:\\selenium\\ChromeProfile"; 
    private static final String COOKIE_PATH = "\\\\Usuario-pc\\arquivos compartilhados\\Calcula Piso\\PisoAsso\\Extras\\cookie.txt";
    
    // Valores padrão
    public static String estoque = "0";
    public static String status = "N/D";
    public static String nome = "";
    public static String valor = "0,00"; 
    public static boolean produtoNaoEncontrado = false;
    
    private static final String[] CODIGOS_TESTE = {"2176596", "3989", "2177133"};

    public static int buscarEstoque(String codigo) throws IOException {
        Path path = Paths.get(COOKIE_PATH);
        if (!Files.exists(path)) {
            Files.createFile(path);
            return 0;
        }

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
                    Element estoqueLabel = doc.selectFirst("b:matchesOwn(^Estoque:)");
                    estoque = (estoqueLabel != null) ? estoqueLabel.nextSibling().toString().trim().replace(",", ".") : "0";

                    Element statusElement = doc.selectFirst("span.marcador[data-codigo='']");
                    status = (statusElement != null) ? statusElement.text().trim() : "N/D";
                    
                    Element nomeElement = doc.selectFirst("div.product-name > div:first-child");
                    nome = (nomeElement != null) ? nomeElement.text().trim().replaceAll("- 1xM2", "").trim() : "Sem Nome";
                    
                    // --- NOVA LÓGICA DE PREÇO (Início) ---
                    valor = "0,00"; // Reseta o valor
                    
                    // Busca qualquer elemento que contenha texto "R$" diretamente nele
                    Elements precosEncontrados = doc.getElementsContainingOwnText("R$");
                    
                    if (precosEncontrados != null) {
                        for (Element el : precosEncontrados) {
                            String texto = el.text().trim();
                            // Verifica se o texto tem formato de dinheiro (contém números e vírgula)
                            // Exemplo: "R$ 32,29" ou "Por: R$ 32,29"
                            if (texto.matches(".*\\d+,\\d{2}.*")) {
                                valor = texto.replace("R$", "").trim();
                                break; // Para no primeiro preço válido encontrado
                            }
                        }
                    }
                    // --- NOVA LÓGICA DE PREÇO (Fim) ---
                    
                    return 1;
                } else {
                    return 2;
                }

            } catch (IOException e) {
                System.err.println("Erro cookie: " + cookie + " - " + e.getMessage());
            }
        }
        return 0;
    }
    
    // --- LÓGICA DO NOVO LOGIN (REMOTE DEBUGGING) ---

    private static String getChromePath() {
        String[] possiblePaths = {
            "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
            "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe"
        };
        for (String p : possiblePaths) {
            if (new File(p).exists()) return p;
        }
        return null;
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
            File driverFile = new File(driverPathToUse);

            if (driverFile.exists()) {
                System.setProperty("webdriver.chrome.driver", driverPathToUse);
                return true;
            } else {
                mostrarErroDriver(majorVersion, driverPathToUse);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
            try { Desktop.getDesktop().browse(new URI("https://googlechromelabs.github.io/chrome-for-testing/")); } 
            catch (Exception ex) {}
        });
        panel.add(linkButton);
        JOptionPane.showMessageDialog(null, panel, "Erro Driver", JOptionPane.ERROR_MESSAGE);
    }
    
    // MÉTODO NOVO: Verifica se a porta 9222 está aberta em no máximo 1 segundo
    private static boolean isChromeDebugOpen() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("127.0.0.1", 9222), 1000); // 1000ms = 1 segundo de timeout
            return true;
        } catch (IOException e) {
            return false;
        }
    }
 	
    public static void realizarLoginESalvarCookie() {
        String chromePath = getChromePath();
        if (chromePath == null) {
            JOptionPane.showMessageDialog(null, "Google Chrome não encontrado no local padrão.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!configurarDriver(chromePath)) return;
        
        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");

        boolean tentarConexao = true;

        while (tentarConexao) {
            try {
                new File(CHROME_PROFILE_PATH).mkdirs();
                String cmd = "\"" + chromePath + "\" --remote-debugging-port=9222 --user-data-dir=\"" + CHROME_PROFILE_PATH + "\" \"" + LOGIN_URL + "\"";
                Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao tentar abrir o Chrome: " + e.getMessage());
                return;
            }

            JOptionPane pane = new JOptionPane(
                "O Chrome foi aberto (Perfil Robô).\n\n" +
                "1. Faça o LOGIN manualmente e resolva o CAPTCHA.\n" +
                "2. MANTENHA O CHROME ABERTO (Não feche a janela!).\n" +
                "3. Volte aqui e clique em SIM.",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION
            );
            
            JDialog dialog = pane.createDialog("Aguardando Login Manual");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true); 
            
            Object selectedValue = pane.getValue();
            int opcao = JOptionPane.CLOSED_OPTION;
            if (selectedValue instanceof Integer) {
                opcao = (Integer) selectedValue;
            }

            if (opcao != JOptionPane.YES_OPTION) {
                return; 
            }

            // --- MELHORIA: VERIFICAÇÃO RÁPIDA DE PORTA ---
            if (!isChromeDebugOpen()) {
                // Se a porta estiver fechada, nem tenta abrir o Selenium para não travar
                int retry = JOptionPane.showConfirmDialog(null, 
                    "Não consegui encontrar o Chrome aberto!\n\n" +
                    "Parece que ele foi fechado. O sistema precisa que a janela\n" +
                    "continue aberta para copiar o acesso.\n\n" +
                    "Deseja abrir o navegador novamente?", 
                    "Chrome Fechado", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.ERROR_MESSAGE);
                
                if (retry != JOptionPane.YES_OPTION) {
                    tentarConexao = false; 
                }
                continue; // Volta para o início do loop
            }
            
            // Se passou daqui, o Chrome está aberto e respondendo!

            WebDriver driver = null;
            try {
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");

                driver = new ChromeDriver(options);
                
                try {
                    String phpsessid = driver.manage().getCookieNamed("PHPSESSID").getValue();
                    
                    if (phpsessid != null && !phpsessid.isEmpty()) {
                        salvarCookieEmArquivo(phpsessid);
                       // JOptionPane.showMessageDialog(null, "Sucesso! Cookie capturado e salvo.", "Concluído", JOptionPane.INFORMATION_MESSAGE);
                        
                        try { driver.close(); } catch (Exception ex) {}
                        
                        tentarConexao = false;
                    } else {
                        JOptionPane.showMessageDialog(null, "Cookie 'PHPSESSID' não encontrado.\nVerifique se você realmente logou no site.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Erro ao ler cookie. Talvez o login não tenha completado.", "Erro", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                // Caso raro de erro mesmo com a porta aberta
                JOptionPane.showMessageDialog(null, "Erro técnico ao conectar: " + e.getMessage());
                tentarConexao = false;
            } finally {
                if (driver != null) {
                    try { driver.quit(); } catch (Exception ex) {}
                }
            }
        }
    }
    
    private static void salvarCookieEmArquivo(String cookieValue) {
        try (FileWriter fw = new FileWriter(COOKIE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.newLine();
            bw.write(cookieValue);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar arquivo: " + e.getMessage());
        }
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
        }
        return false;
    }
}