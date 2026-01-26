package PisoAsso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class EstoqueScraper {

    private static final String PESQUISA_URL = "https://redeasso.areacentral.com.br/401/?pg=associado_catalogos_produtos&ordenacao=nome-asc";
    private static final String COOKIE_PATH = "\\\\Usuario-pc\\arquivos compartilhados\\Calcula Piso\\PisoAsso\\Extras\\cookie.txt";
    public static String estoque = "";
    public static String status = "";
    public static String metragem = "";
    public static String nome = "";
    public static String valor = "";
    public static boolean produtoNaoEncontrado = false;
    
    private static final String[] CODIGOS_TESTE = {"2176596", "3989", "2177133"};

    public static int buscarEstoque(String codigo) throws IOException {
        Path path = Paths.get(COOKIE_PATH);
        if (!Files.exists(path)) {
            Files.createFile(path);
            return 0;
        }

        List<String> linhas = Files.readAllLines(path);
        if (linhas.isEmpty()) {
            return 0;
        }

        int inicio = Math.max(0, linhas.size() - 10);
        List<String> ultimas = linhas.subList(inicio, linhas.size());

        for (int i = ultimas.size() - 1; i >= 0; i--) {
            String cookie = ultimas.get(i).trim();
            if (cookie.isEmpty()) continue;

            try {
                Document doc = Jsoup.connect(PESQUISA_URL)
                        .cookie("PHPSESSID", cookie)
                        .data("filtrar", "#")
                        .data("salvar-filtro", "0")
                        .data("salvar-conteudo", "0")
                        .data("salvar-ordenacao", "0")
                        .data("salvar-paginacao", "0")
                        .data("limit-filtro", "960")
                        .data("limpar-filtros", "0")
                        .data("limpar-ordenacao", "0")
                        .data("limpar-paginacao", "0")
                        .data("filtro[]", "PF.REFERENCIA")
                        .data("operador[]", "IGUAL")
                        .data("valor_filtro1[]", codigo)
                        .post();

                Element multiploLabel = doc.selectFirst("b:matchesOwn(^Múltiplo:)");

                if (multiploLabel != null) {
                    Element estoqueLabel = doc.selectFirst("b:matchesOwn(^Estoque:)");
                    
                    if (estoqueLabel != null) {
                        estoque = estoqueLabel.nextSibling().toString().trim().replace(",", ".");
                    } else {
                        estoque = "0,00";
                    }

                    Element statusElement = doc.selectFirst("span.marcador[data-codigo='']");
                    if (statusElement != null) {
                        status = statusElement.text().trim();
                    } else {
                        status = "N/D";
                    }
                    
                    // Capturar o nome do produto
                    Element nomeElement = doc.selectFirst("div.product-name > div:first-child");
                    if (nomeElement != null) {
                    	String nomeCompleto = nomeElement.text().trim();
                        nome = nomeCompleto.replaceAll("- 1xM2", "").trim();
                    } else {
                        nome = "";
                    }
                    
                    // Capturar o valor (preço)
                    Element valorElement = doc.selectFirst("span[data-toggle='popover'][data-trigger='hover']");
                    if (valorElement != null) {
                        String valorTexto = valorElement.text().trim();
                        valor = valorTexto.replace("R$", "").trim();
                    } else {
                        valor = "";
                    }
                    
                    return 1; // Produto encontrado
                } else {
                    return 2; // Produto fora de linha
                }

            } catch (IOException e) {
                System.err.println("Erro ao tentar conectar com cookie: " + cookie + " - " + e.getMessage());
            }
        }
        return 0; // Não conseguiu conectar com nenhum cookie
    }
    
    private static boolean configurarChromeDriver() {
        String driverFolderPath = "\\\\USUARIO-PC\\arquivos compartilhados\\Calcula Piso\\PisoAsso\\Extras\\WebDriver\\";
        String driverPath109 = driverFolderPath + "chromedriver109.exe";

        try {
            String command = "wmic datafile where name=\"C:\\\\Program Files\\\\Google\\\\Chrome\\\\Application\\\\chrome.exe\" get Version /value";
            Process process = Runtime.getRuntime().exec(command);

            Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\\A");
            String output = scanner.hasNext() ? scanner.next() : "";
            process.waitFor();
            scanner.close();

            if (output.trim().isEmpty()) {
                 command = "wmic datafile where name=\"C:\\\\Program Files (x86)\\\\Google\\\\Chrome\\\\Application\\\\chrome.exe\" get Version /value";
                 process = Runtime.getRuntime().exec(command);
                 scanner = new Scanner(process.getInputStream()).useDelimiter("\\A");
                 output = scanner.hasNext() ? scanner.next() : "";
                 process.waitFor();
                 scanner.close();
            }

            if (output.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Não foi possível detectar a versão do Google Chrome.", "Erro de Detecção", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            String majorVersion = "";
            String[] lines = output.split("\n");
            for (String line : lines) {
                if (line.startsWith("Version=")) {
                    String fullVersion = line.substring("Version=".length()).trim();
                    if (!fullVersion.isEmpty()) {
                       majorVersion = fullVersion.split("\\.")[0];
                    }
                    break;
                }
            }

            if (majorVersion.isEmpty()){
                 JOptionPane.showMessageDialog(null, "Não foi possível extrair o número da versão do Chrome.", "Erro de Análise", JOptionPane.ERROR_MESSAGE);
                 return false;
            }

            String driverPathToUse;

            if (majorVersion.equals("109")) {
                driverPathToUse = driverPath109;
                System.out.println("Modo de compatibilidade ativado para Chrome v109.");
            } else {
                System.out.println("Versão moderna do Chrome detectada: v" + majorVersion);
                String dynamicDriverName = "chromedriver" + majorVersion + ".exe";
                driverPathToUse = driverFolderPath + dynamicDriverName;
                System.out.println("Procurando por driver em: " + driverPathToUse);
            }

            File driverFile = new File(driverPathToUse);
            if (driverFile.exists()) {
                System.setProperty("webdriver.chrome.driver", driverPathToUse);
                return true;
            } else {
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                panel.add(new JLabel("Driver não encontrado para a sua versão do Chrome (" + majorVersion + ")."));
                panel.add(new JLabel("Era esperado em: " + driverPathToUse));
                panel.add(new JLabel(" ")); 
                panel.add(new JLabel("Por favor, baixe o driver correto e coloque-o na pasta compartilhada."));
                panel.add(new JLabel(" "));

                JButton linkButton = new JButton("Clique aqui para baixar o driver (site oficial)");
                final String url = "https://googlechromelabs.github.io/chrome-for-testing/";
                
                linkButton.setForeground(Color.BLUE.darker());
                linkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                linkButton.setBorderPainted(false);
                linkButton.setOpaque(false);
                linkButton.setContentAreaFilled(false);
                linkButton.setToolTipText(url);

                linkButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            Desktop.getDesktop().browse(new URI(url));
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Não foi possível abrir o link: " + ex.getMessage());
                        }
                    }
                });

                panel.add(linkButton);
                JOptionPane.showMessageDialog(null, panel, "Driver Incompatível", JOptionPane.ERROR_MESSAGE);
                return false;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro na detecção da versão: " + e.getMessage(), "Erro Inesperado", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
 	
    public static void realizarLoginESalvarCookie() {
        if (!configurarChromeDriver()) {
            return;
        }
        
        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");

        WebDriver driver = null;
        try {
            // Configurações para tentar evitar a detecção do Selenium
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            options.setExperimentalOption("useAutomationExtension", false);
            options.addArguments("--disable-blink-features=AutomationControlled");
            
            driver = new ChromeDriver(options);
            driver.get(PESQUISA_URL);

            // Tempo de espera aumentado para 600 segundos (10 minutos)
            WebDriverWait wait = new WebDriverWait(driver, 600);

            // O Robô apenas preenche os dados para facilitar
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("USR_APELIDO"))).sendKeys("jaragua.vilson");
            driver.findElement(By.id("USR_SENHA")).sendKeys("ctcjaragua2002");
            
            // --- MODO MANUAL ---
            // O código agora PARA AQUI e espera você fazer o resto.
            // 1. Resolva o Captcha/Cloudflare.
            // 2. Clique no botão Entrar manualmente.
            System.out.println("Dados preenchidos. Aguardando login manual do usuário (resolva o captcha)...");
            
            // O sistema fica vigiando se o login deu certo (procurando o botão de filtro da tela interna)
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.btn.btn-success.filtrar-dados")));

            String phpsessid = driver.manage().getCookieNamed("PHPSESSID").getValue();
            salvarCookieEmArquivo(phpsessid);
            System.out.println("Login detectado! Cookie salvo com sucesso!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null, 
                "Tempo limite excedido ou erro de navegador.\nPor favor, realize o login manualmente dentro do prazo.\nErro: " + e.getMessage(), 
                "Erro de Automação", 
                JOptionPane.ERROR_MESSAGE
            );
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
    
    private static void salvarCookieEmArquivo(String cookieValue) {
        try (FileWriter fw = new FileWriter(COOKIE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.newLine();
            bw.write(cookieValue);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao salvar o novo cookie no arquivo: " + e.getMessage(), 
                "Erro de Arquivo", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static boolean validarCookie() throws IOException {
        Path path = Paths.get(COOKIE_PATH);
        if (!Files.exists(path)) {
            Files.createFile(path);
            return false;
        }

        List<String> linhas = Files.readAllLines(path);
        if (linhas.isEmpty()) {
            return false;
        }

        int inicio = Math.max(0, linhas.size() - 10);
        List<String> ultimas = linhas.subList(inicio, linhas.size());

        for (int i = ultimas.size() - 1; i >= 0; i--) {
            String cookie = ultimas.get(i).trim();
            if (cookie.isEmpty()) continue;

            for (String codigoTeste : CODIGOS_TESTE) {
                try {
                    Document doc = Jsoup.connect(PESQUISA_URL)
                            .cookie("PHPSESSID", cookie)
                            .data("filtrar", "#")
                            .data("salvar-filtro", "0")
                            .data("salvar-conteudo", "0")
                            .data("salvar-ordenacao", "0")
                            .data("salvar-paginacao", "0")
                            .data("limit-filtro", "960")
                            .data("limpar-filtros", "0")
                            .data("limpar-ordenacao", "0")
                            .data("limpar-paginacao", "0")
                            .data("filtro[]", "PF.REFERENCIA")
                            .data("operador[]", "IGUAL")
                            .data("valor_filtro1[]", codigoTeste)
                            .post();

                    Element multiploLabel = doc.selectFirst("b:matchesOwn(^Múltiplo:)");
                    
                    if (multiploLabel != null) {
                        return true; // Cookie válido!
                    }
                    
                } catch (IOException e) {
                    System.err.println("Erro ao validar com código " + codigoTeste + ": " + e.getMessage());
                }
            }
        }
        
        return false; // Nenhum cookie funcionou
    }
}