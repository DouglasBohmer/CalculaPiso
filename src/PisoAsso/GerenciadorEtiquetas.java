package PisoAsso;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.awt.print.*;
import java.awt.*;

import javax.print.PrintService;
import javax.swing.JOptionPane;

public class GerenciadorEtiquetas {

    // Caminho do arquivo JSON da fila
    private static final String ARQUIVO_JSON = "\\\\Usuario-pc\\arquivos compartilhados\\Calcula Piso\\PisoAsso\\Extras\\fila_etiquetas.json";
    
    // --- CONFIGURAÇÕES DE MEDIDA (Ajuste fino aqui) ---
    private static final int MARGEM_ESQUERDA = 20; // Aumentei um pouco para garantir que não corte a esquerda
    private static final int MARGEM_TOPO_ETIQUETA_1 = 40; // Aumentei para 40 para não cortar o topo do nome
    private static final int MARGEM_TOPO_ETIQUETA_2 = 250; // Posição Y onde começa a segunda etiqueta
    private static final int LARGURA_MAX_TEXTO = 240; // Largura segura para quebra de linha

    // --- ESTRUTURA DOS DADOS ---
    public static class DadosEtiqueta {
        public String nome, asso, ctc, m2cx, pcscx, pei, preco;
        public int quantidade;

        public DadosEtiqueta(String nome, String asso, String ctc, String m2cx, String pcscx, String pei, String preco, int quantidade) {
            this.nome = nome; this.asso = asso; this.ctc = ctc; 
            this.m2cx = m2cx; this.pcscx = pcscx; this.pei = pei; 
            this.preco = preco; this.quantidade = quantidade;
        }
    }

    // --- 1. ADICIONAR NA FILA ---
    public static void adicionarNaFila(String nome, String asso, String ctc, String m2cx, String pcscx, String pei, String preco) {
        String qtdStr = JOptionPane.showInputDialog(null, "Quantas etiquetas para: \n" + nome, "1");
        if (qtdStr == null) return; 
        
        int qtd = 1;
        try { qtd = Integer.parseInt(qtdStr); } catch (Exception e) {}

        try {
            String jsonLine = String.format(
                "{\"nome\":\"%s\", \"asso\":\"%s\", \"ctc\":\"%s\", \"m2cx\":\"%s\", \"pcscx\":\"%s\", \"pei\":\"%s\", \"preco\":\"%s\", \"qtd\":\"%d\"}",
                limparTexto(nome), asso, ctc, m2cx, pcscx, pei, preco, qtd
            );

            FileWriter fw = new FileWriter(ARQUIVO_JSON, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(jsonLine);
            bw.newLine();
            bw.close();
            
            JOptionPane.showMessageDialog(null, "Adicionado à fila! Total na fila: " + contarFila());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar: " + e.getMessage());
        }
    }

    // --- 2. CONTAR ITENS ---
    public static int contarFila() {
        try {
            Path path = Paths.get(ARQUIVO_JSON);
            if (!Files.exists(path)) return 0;
            List<String> linhas = Files.readAllLines(path);
            int total = 0;
            for (String linha : linhas) { if (linha.trim().length() > 0) total++; }
            return total;
        } catch (Exception e) { return 0; }
    }

    // --- 3. LER O JSON ---
    public static List<DadosEtiqueta> lerFila() {
        List<DadosEtiqueta> lista = new ArrayList<>();
        try {
            Path path = Paths.get(ARQUIVO_JSON);
            if (!Files.exists(path)) return lista;

            List<String> linhas = Files.readAllLines(path);
            for (String linha : linhas) {
                if (linha.trim().isEmpty()) continue;
                
                String nome = extrairValor(linha, "nome");
                String asso = extrairValor(linha, "asso");
                String ctc = extrairValor(linha, "ctc");
                String m2cx = extrairValor(linha, "m2cx");
                String pcscx = extrairValor(linha, "pcscx");
                String pei = extrairValor(linha, "pei");
                String preco = extrairValor(linha, "preco");
                String qtdStr = extrairValor(linha, "qtd");
                
                int qtd = 1;
                try { qtd = Integer.parseInt(qtdStr); } catch (Exception e) {}
                
                for (int i = 0; i < qtd; i++) {
                    lista.add(new DadosEtiqueta(nome, asso, ctc, m2cx, pcscx, pei, preco, 1));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    private static String limparTexto(String texto) {
        if (texto == null) return "";
        return texto.replace("\"", "").replace("{", "").replace("}", "").replace("\\", "");
    }

    private static String extrairValor(String json, String chave) {
        String key = "\"" + chave + "\":\"";
        int start = json.indexOf(key);
        if (start == -1) return "";
        start += key.length();
        int end = json.indexOf("\"", start);
        if (end == -1) return "";
        return json.substring(start, end);
    }

    // --- 4. IMPRIMIR ---
    public static void imprimirFilaArgox() {
        List<DadosEtiqueta> fila = lerFila();
        
        if (fila.isEmpty()) {
            JOptionPane.showMessageDialog(null, "A fila está vazia!");
            return;
        }

        PrintService[] services = PrinterJob.lookupPrintServices();
        PrintService argoxService = null;
        for (PrintService service : services) {
            String name = service.getName().toUpperCase();
            if (name.contains("ARGOX") || name.contains("ZEBRA") || name.contains("ELGIN")) {
                argoxService = service;
                break;
            }
        }

        if (argoxService == null) {
            JOptionPane.showMessageDialog(null, "Impressora Argox não encontrada!");
            return;
        }

        PrinterJob job = PrinterJob.getPrinterJob();
        try { job.setPrintService(argoxService); } catch (PrinterException e) { return; }

        PageFormat pf = job.defaultPage();
        Paper paper = pf.getPaper();
        
        // Tamanho da folha: 100mm x 150mm (aprox 283 x 425 pontos em 72dpi)
        // Se ainda der erro de tamanho, tente inverter para (425, 283) dependendo da orientação do driver
        paper.setSize(283, 426); // Coloquei 426 para garantir que cubra o sensor de gap
        paper.setImageableArea(0, 0, 283, 426); 
        pf.setPaper(paper);

        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
                int indexItem1 = pageIndex * 2;
                int indexItem2 = indexItem1 + 1;

                if (indexItem1 >= fila.size()) return NO_SUCH_PAGE;

                Graphics2D g2d = (Graphics2D) g;
                g2d.translate(pf.getImageableX(), pf.getImageableY());

                // FONTE ÚNICA: SansSerif, Negrito, 14
                g2d.setFont(new Font("SansSerif", Font.BOLD, 14));

                // 1ª ETIQUETA (Topo)
                desenharEtiqueta(g2d, fila.get(indexItem1), MARGEM_TOPO_ETIQUETA_1);

                // 2ª ETIQUETA (Baixo)
                if (indexItem2 < fila.size()) {
                    desenharEtiqueta(g2d, fila.get(indexItem2), MARGEM_TOPO_ETIQUETA_2);
                }
                
                // Linha de Corte (apenas visual para referência, não sai se o ribbon acabar rs)
                g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5.0f}, 0.0f));
                g2d.drawLine(0, 213, 283, 213); // Metade exata

                return PAGE_EXISTS;
            }
        }, pf);

        try {
            job.print();
            JOptionPane.showMessageDialog(null, "Enviado para Argox!");
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, "Erro na impressão: " + ex.getMessage());
        }
    }

 // Função de desenho atualizada
    private static void desenharEtiqueta(Graphics2D g2d, DadosEtiqueta d, int yInicial) {
        int y = yInicial;
        int x = MARGEM_ESQUERDA;
        
        // Fontes
        Font fontePadrao = new Font("SansSerif", Font.BOLD, 14);
        Font fontePreco = new Font("SansSerif", Font.BOLD, 18); // Fonte maior para o preço

        g2d.setFont(fontePadrao);

        // 1. Nome do Produto (Com quebra de linha)
        List<String> linhasNome = quebrarTexto(d.nome, 26); 
        for (String linha : linhasNome) {
            g2d.drawString(linha, x, y);
            y += 16; 
        }
        
        y += 5;
        g2d.drawLine(x, y, 260, y); // Linha separadora
        y += 20;

        // 2. Dados Técnicos
        g2d.drawString("Cód Asso: " + d.asso + "    Cód Loja: " + d.ctc, x, y);
        y += 20;

        g2d.drawString("Cx: " + d.m2cx + "m²   Pçs/Cx: " + d.pcscx, x, y);
        y += 20;

        // 3. PEI
        g2d.drawString("PEI: " + d.pei, x, y);
        y += 30; // Espaço um pouco maior antes do preço

        // 4. Preço (Centralizado e Maior)
        g2d.setFont(fontePreco);
        String textoPreco = "    " + d.preco;
        
        // Lógica de centralização
        FontMetrics fm = g2d.getFontMetrics();
        int larguraTexto = fm.stringWidth(textoPreco);
        int larguraPagina = 283; // Largura total configurada no paper.setSize
        int xCentralizado = (larguraPagina - larguraTexto) / 2;

        g2d.drawString(textoPreco, xCentralizado, y);
    }

    // Helper para quebra de linha
    private static List<String> quebrarTexto(String texto, int maxChars) {
        List<String> linhas = new ArrayList<>();
        if (texto == null) return linhas;
        
        String[] palavras = texto.split(" ");
        StringBuilder linhaAtual = new StringBuilder();

        for (String palavra : palavras) {
            if (linhaAtual.length() + palavra.length() + 1 <= maxChars) {
                if (linhaAtual.length() > 0) linhaAtual.append(" ");
                linhaAtual.append(palavra);
            } else {
                linhas.add(linhaAtual.toString());
                linhaAtual = new StringBuilder(palavra);
            }
        }
        if (linhaAtual.length() > 0) {
            linhas.add(linhaAtual.toString());
        }
        return linhas;
    }
}