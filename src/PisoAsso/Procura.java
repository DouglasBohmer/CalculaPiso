package PisoAsso;

import java.awt.EventQueue;

	//Conexão banco de dados;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.AbstractButton;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputFilter.Status;
import java.io.UnsupportedEncodingException;
import java.awt.event.KeyAdapter;
import javax.swing.JFormattedTextField;
import javax.swing.border.LineBorder;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JTextArea;
import java.math.BigDecimal; 
import java.math.RoundingMode; 

import java.awt.print.PrinterJob;
import java.awt.print.PrinterException;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import javax.print.PrintService;

public class Procura extends JFrame {

	private JPanel contentPane;
		
	public static String CaminhoImagem = "\\\\USUARIO-PC\\arquivos compartilhados\\Calcula Piso\\PisoAsso\\Extras\\Imagens"; //PC Loja
	public static String CaminhoBD = "jdbc:mysql://192.168.0.157:3306/calcula_piso"; //PC Loja
	
	private JTextField Pesquisa_Cod_Piso;
	private JTextField Mostra_Cod_Asso;
	private JTextField Mostra_Cod_CTC;
	private JTextField Mostra_Larg;
	private JTextField Mostra_Alt;
	private JTextField Mostra_Rejunte;
	private JTextField Mostra_Peca_CX;
	private JTextField Mostra_M_Peca;
	private JTextField Mostra_M_CX;
	private JTextField Mostra_Nome_Piso;
	private JTextField Pesquisa_M_Cliente;
	private JTextField Pesquisa_CX_Cliente;
	private JTextField Mostra_CX_Exato;
	private JTextField Mostra_CX_Arredondado;
	private JTextField Mostra_QTD_Pedir;
	private JTextField Mostra_KG_Argamassa;
	private JTextField Mostra_KG_Rejunte;
	private JTextField Mostra_Sacos_Argamassa;
	private JTextField Mostra_Sacos_Rejunte;
	private JTextField Mostra_Tipo_Piso;
	private JTextField Mostra_Pei;
	private JTextField Mostra_Retificado;
	
	private double altura;
	private double largura;
	private double area_pc;
	private int pecas_cx;
	private double m_caixas;
	private double cx_exatas;
	private double m_cliente;
	private double cx_cliente;
	private double m_pedir;
	DecimalFormat m_caixas_arredond = new DecimalFormat("#,##0.00");
	private double m_cx;
	private double rejunte;
	private double rejunte_kg;
	private double profund = 9;
	private double coeficiente = 1.8;
	private double rejunte_total;
	private double argamassa;
	private double saco_argamassa;
	private int caixas_arredondadas;
	
	private String link_foto;
	private String link_site;
	private String link_ambiente;
	private String texto_label="Quantidade de caixa para X M²";
	
	private String link_final_zerado = "";
	private String link_final_curto = "https://redeasso.areacentral.com.br/401/?";
	private JTextField txtltimaAtt;
	private JButton BT_Whats;
	private JFormattedTextField FTF_Valor;
	private JFormattedTextField FTF_Telefone;
	private JTextField BTEstoque_Asso;
	private JTextField Status_Asso;
	private JTextArea Mostra_Local_Uso;
	
	private String cod_asso_banco ="";
	private boolean cookieValidado = false;
	private JLabel lblStatus;
	
	private JDialog dialogEspera;
	private Timer timerVerificacao;
	private long lastModifiedTime;
	private boolean loginConcluido = false; // VARIÁVEL ADICIONADA AQUI
	
	double estoque_cor;
	String status_cor;
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		
		UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");   
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Procura frame = new Procura();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
										
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Procura() {
		setTitle("Calcula Piso");		
		setIconImage(Toolkit.getDefaultToolkit().getImage(CaminhoImagem+"\\Logo Casa dos Tubos 50_page-0001.jpg"));
		setFont(new Font("Arial", Font.PLAIN, 12));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1045, 730);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton Botao_Cadastro = new JButton("");
		Botao_Cadastro.setIcon(new ImageIcon(CaminhoImagem+"\\+.png"));
		Botao_Cadastro.setBounds(889, 618, 60, 35);
		Botao_Cadastro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Cadastro TelaCad = new Cadastro();
				TelaCad.setLocationRelativeTo(null);
				TelaCad.setVisible(true);
				dispose();
			}
		});
		
		JButton Cadastrados = new JButton("");
		Cadastrados.setIcon(new ImageIcon(CaminhoImagem+"\\Cadastrado.png"));
		Cadastrados.setBounds(959, 618, 60, 35);
		Cadastrados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Pisos_Cadastrados TelaPisoCad = new Pisos_Cadastrados();
				TelaPisoCad.setLocationRelativeTo(null);
				TelaPisoCad.setVisible(true);
				dispose();
			}
		});
		
		JButton BT_Voltar = new JButton("");
		BT_Voltar.setEnabled(true);
		BT_Voltar.setToolTipText("Refazer Login");
		BT_Voltar.setIcon(new ImageIcon(CaminhoImagem+"\\Recarregar.png"));
		BT_Voltar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        iniciarRotinaDeLogin();
		    }
		});

		BT_Voltar.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		BT_Voltar.setBounds(819, 618, 60, 35);
		contentPane.add(BT_Voltar);
		
		txtltimaAtt = new JTextField();
		txtltimaAtt.setBackground(new Color(240, 240, 240));
		txtltimaAtt.setText("Última att: 17/02/26");
		txtltimaAtt.setHorizontalAlignment(SwingConstants.CENTER);
		txtltimaAtt.setFont(new Font("Arial", Font.PLAIN, 8));
		txtltimaAtt.setEditable(false);
		txtltimaAtt.setColumns(10);
		txtltimaAtt.setBounds(819, 664, 200, 16);
		txtltimaAtt.setFocusable(true); 
		contentPane.add(txtltimaAtt);
		Cadastrados.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		contentPane.add(Cadastrados);
		Botao_Cadastro.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		contentPane.add(Botao_Cadastro);
		
		JLabel lblNewLabel_1 = new JLabel("Código Piso");
		lblNewLabel_1.setBounds(22, 47, 114, 25);
		lblNewLabel_1.setBackground(new Color(0, 0, 255));
		lblNewLabel_1.setForeground(new Color(0, 0, 0));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1);
		
		Pesquisa_Cod_Piso = new JTextField();
		Pesquisa_Cod_Piso.setBounds(146, 44, 199, 28);
		Pesquisa_Cod_Piso.setForeground(new Color(255, 0, 0));
		Pesquisa_Cod_Piso.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				Pesquisa_Cod_Piso.setText("");
			}
		});
		Pesquisa_Cod_Piso.setText("0");
		Pesquisa_Cod_Piso.setHorizontalAlignment(SwingConstants.CENTER);
		Pesquisa_Cod_Piso.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 16));
		contentPane.add(Pesquisa_Cod_Piso);
		Pesquisa_Cod_Piso.setColumns(10);
		
		JLabel lblNewLabel_1_1 = new JLabel("Cód CTC");
		lblNewLabel_1_1.setBounds(559, 143, 140, 20);
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setForeground(Color.BLACK);
		lblNewLabel_1_1.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("Cód Asso");
		lblNewLabel_1_2.setBounds(399, 143, 140, 20);
		lblNewLabel_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2.setForeground(Color.BLACK);
		lblNewLabel_1_2.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_2);
		
		Mostra_Cod_Asso = new JTextField();
		Mostra_Cod_Asso.setBounds(399, 174, 140, 20);
		Mostra_Cod_Asso.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_Cod_Asso.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_Cod_Asso.setEditable(false);
		Mostra_Cod_Asso.setColumns(10);
		contentPane.add(Mostra_Cod_Asso);
		
		Mostra_Cod_CTC = new JTextField();
		Mostra_Cod_CTC.setBounds(559, 174, 140, 20);
		Mostra_Cod_CTC.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_Cod_CTC.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_Cod_CTC.setEditable(false);
		Mostra_Cod_CTC.setColumns(10);
		contentPane.add(Mostra_Cod_CTC);
				
		JButton Botao_Pesquisar = new JButton("Pesquisar");
		Botao_Pesquisar.setIcon(new ImageIcon(CaminhoImagem+"\\Pesquisar.png"));
		Botao_Pesquisar.setBounds(22, 159, 160, 35);
		getRootPane().setDefaultButton(Botao_Pesquisar);
		
		Botao_Pesquisar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					JOptionPane.showMessageDialog(null, "Teste");
				}
			}
		});
		Botao_Pesquisar.setMnemonic(KeyEvent.VK_S);

		Botao_Pesquisar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {

		        if (VerificaCodigo(Pesquisa_Cod_Piso.getText())) {
		            JOptionPane.showMessageDialog(null, "Campo de pesquisa do código está vazio! Digite algo para pesquisar");
		            return;
		        }
		        if (VerificaValor(Pesquisa_M_Cliente.getText(), Pesquisa_CX_Cliente.getText())) {
		            JOptionPane.showMessageDialog(null, "Os campos de quantidade de caixas ou a metragem do piso são obrigatórios!");
		            return;
		        }

		        try {
		            boolean cookieOk = EstoqueScraper.validarCookie();
		            
		            if (cookieOk) {
		            	cookieValidado = true;
		            	realizarPesquisa(); 
		            } else {
		            	iniciarRotinaDeLogin();
		            }
		            
		        } catch (IOException ex) {
		             iniciarRotinaDeLogin();
		        }
		    }
		});
		
		Botao_Pesquisar.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		contentPane.add(Botao_Pesquisar);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Largura (cm)");
		lblNewLabel_1_1_1.setBounds(71, 268, 110, 20);
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1.setForeground(Color.BLACK);
		lblNewLabel_1_1_1.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Altura (cm)");
		lblNewLabel_1_1_1_1.setBounds(195, 268, 110, 20);
		lblNewLabel_1_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_1.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_1.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1_1_1);
		
		Mostra_Larg = new JTextField();
		Mostra_Larg.setBounds(71, 297, 110, 20);
		Mostra_Larg.setForeground(new Color(255, 0, 0));
		Mostra_Larg.setBackground(new Color(240, 240, 240));
		Mostra_Larg.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_Larg.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_Larg.setEditable(false);
		Mostra_Larg.setColumns(10);
		contentPane.add(Mostra_Larg);
		
		Mostra_Alt = new JTextField();
		Mostra_Alt.setBounds(195, 297, 110, 20);
		Mostra_Alt.setForeground(new Color(255, 0, 0));
		Mostra_Alt.setBackground(new Color(240, 240, 240));
		Mostra_Alt.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_Alt.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_Alt.setEditable(false);
		Mostra_Alt.setColumns(10);
		contentPane.add(Mostra_Alt);
		
		JLabel lblNewLabel_1_1_1_2 = new JLabel("Rejunte (mm)");
		lblNewLabel_1_1_1_2.setBounds(71, 390, 110, 20);
		lblNewLabel_1_1_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_2.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_2.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1_1_2);
		
		JLabel lblNewLabel_1_1_1_3 = new JLabel("Peças p/ CX");
		lblNewLabel_1_1_1_3.setBounds(71, 328, 110, 20);
		lblNewLabel_1_1_1_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_3.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_3.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1_1_3);
		
		JLabel lblNewLabel_1_1_1_4 = new JLabel("M² p/ Peça");
		lblNewLabel_1_1_1_4.setBounds(195, 390, 110, 20);
		lblNewLabel_1_1_1_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_4.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_4.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1_1_4);
		
		JLabel lblNewLabel_1_1_1_5 = new JLabel("M² p/ CX");
		lblNewLabel_1_1_1_5.setBounds(195, 328, 110, 20);
		lblNewLabel_1_1_1_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_5.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_5.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1_1_5);
		
		Mostra_Rejunte = new JTextField();
		Mostra_Rejunte.setBounds(71, 421, 110, 20);
		Mostra_Rejunte.setForeground(new Color(255, 0, 0));
		Mostra_Rejunte.setBackground(new Color(240, 240, 240));
		Mostra_Rejunte.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_Rejunte.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_Rejunte.setEditable(false);
		Mostra_Rejunte.setColumns(10);
		contentPane.add(Mostra_Rejunte);
		
		Mostra_Peca_CX = new JTextField();
		Mostra_Peca_CX.setBounds(71, 359, 110, 20);
		Mostra_Peca_CX.setForeground(new Color(255, 0, 0));
		Mostra_Peca_CX.setBackground(new Color(240, 240, 240));
		Mostra_Peca_CX.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_Peca_CX.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_Peca_CX.setEditable(false);
		Mostra_Peca_CX.setColumns(10);
		contentPane.add(Mostra_Peca_CX);
		
		Mostra_M_Peca = new JTextField();
		Mostra_M_Peca.setBounds(195, 421, 110, 20);
		Mostra_M_Peca.setBackground(new Color(240, 240, 240));
		Mostra_M_Peca.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_M_Peca.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_M_Peca.setEditable(false);
		Mostra_M_Peca.setColumns(10);
		contentPane.add(Mostra_M_Peca);
		
		Mostra_M_CX = new JTextField();
		Mostra_M_CX.setBounds(195, 359, 110, 20);
		Mostra_M_CX.setForeground(new Color(255, 0, 0));
		Mostra_M_CX.setBackground(new Color(240, 240, 240));
		Mostra_M_CX.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_M_CX.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_M_CX.setEditable(false);
		Mostra_M_CX.setColumns(10);
		contentPane.add(Mostra_M_CX);
		
		Mostra_Nome_Piso = new JTextField();
		Mostra_Nome_Piso.setBounds(399, 44, 620, 28);
		Mostra_Nome_Piso.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_Nome_Piso.setFont(new Font("Cambria", Font.PLAIN, 18));
		Mostra_Nome_Piso.setEditable(false);
		Mostra_Nome_Piso.setColumns(10);
		contentPane.add(Mostra_Nome_Piso);
		
		Pesquisa_M_Cliente = new JTextField();
		Pesquisa_M_Cliente.setBounds(146, 82, 199, 28);
		Pesquisa_M_Cliente.setForeground(new Color(255, 0, 0));
		Pesquisa_M_Cliente.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				Pesquisa_CX_Cliente.setText("0");
				Pesquisa_M_Cliente.setText("");
			}
		});
		Pesquisa_M_Cliente.setToolTipText("");
		Pesquisa_M_Cliente.setText("0");
		Pesquisa_M_Cliente.setHorizontalAlignment(SwingConstants.CENTER);
		Pesquisa_M_Cliente.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 16));
		Pesquisa_M_Cliente.setColumns(10);
		contentPane.add(Pesquisa_M_Cliente);
		
		Pesquisa_CX_Cliente = new JTextField();
		Pesquisa_CX_Cliente.setBounds(146, 121, 199, 28);
		Pesquisa_CX_Cliente.setForeground(new Color(255, 0, 0));
		Pesquisa_CX_Cliente.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				Pesquisa_M_Cliente.setText("0");
				Pesquisa_CX_Cliente.setText("");
			}
		});
		Pesquisa_CX_Cliente.setText("0");
		Pesquisa_CX_Cliente.setHorizontalAlignment(SwingConstants.CENTER);
		Pesquisa_CX_Cliente.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 16));
		Pesquisa_CX_Cliente.setColumns(10);
		contentPane.add(Pesquisa_CX_Cliente);
		
		JLabel lblNewLabel_1_3 = new JLabel("M² Cliente");
		lblNewLabel_1_3.setBounds(22, 82, 114, 25);
		lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3.setForeground(Color.BLACK);
		lblNewLabel_1_3.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_3);
		
		JLabel lblNewLabel_1_4 = new JLabel("Caixas Cliente");
		lblNewLabel_1_4.setBounds(22, 122, 114, 25);
		lblNewLabel_1_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_4.setForeground(Color.BLACK);
		lblNewLabel_1_4.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_4);
		
		JButton Botao_Reset = new JButton("Apagar");
		Botao_Reset.setIcon(new ImageIcon(CaminhoImagem+"\\Lixo.png"));
		Botao_Reset.setBounds(185, 159, 160, 35);
		Botao_Reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Pesquisa_Cod_Piso.setText("0");
				Pesquisa_CX_Cliente.setText("0");
				Pesquisa_M_Cliente.setText("0");
				apagar();
			}
		});
		Botao_Reset.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		contentPane.add(Botao_Reset);
		
		JLabel lblNewLabel_1_1_1_6 = new JLabel("Informações Técnicas");
		lblNewLabel_1_1_1_6.setBounds(22, 232, 323, 35);
		lblNewLabel_1_1_1_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_6.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_6.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
		contentPane.add(lblNewLabel_1_1_1_6);
		
		JLabel lblNewLabel_1_1_1_6_1 = new JLabel("QTD Aproximada de Materiais");
		lblNewLabel_1_1_1_6_1.setBounds(399, 282, 620, 20);
		lblNewLabel_1_1_1_6_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_6_1.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_6_1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
		contentPane.add(lblNewLabel_1_1_1_6_1);
		
		JLabel lblNewLabel_1_1_1_8_1 = new JLabel("Qtd de caixa para M² pedida");
		lblNewLabel_1_1_1_8_1.setBounds(399, 313, 290, 23);
		lblNewLabel_1_1_1_8_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_8_1.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_8_1.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1_1_8_1);
		
		Mostra_CX_Exato = new JTextField();
		Mostra_CX_Exato.setBounds(869, 356, 0, 0);
		Mostra_CX_Exato.setEnabled(false);
		Mostra_CX_Exato.setBackground(new Color(240, 240, 240));
		Mostra_CX_Exato.setForeground(new Color(0, 0, 0));
		Mostra_CX_Exato.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_CX_Exato.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_CX_Exato.setEditable(false);
		Mostra_CX_Exato.setColumns(10);
		contentPane.add(Mostra_CX_Exato);
		
		Mostra_CX_Arredondado = new JTextField();
		Mostra_CX_Arredondado.setBounds(399, 345, 290, 20);
		Mostra_CX_Arredondado.setForeground(new Color(255, 0, 0));
		Mostra_CX_Arredondado.setBackground(new Color(240, 240, 240));
		Mostra_CX_Arredondado.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_CX_Arredondado.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_CX_Arredondado.setEditable(false);
		Mostra_CX_Arredondado.setColumns(10);
		contentPane.add(Mostra_CX_Arredondado);
		
		JLabel lblNewLabel_1_1_1_8_1_1 = new JLabel("Qtd de M² digitar no cupom");
		lblNewLabel_1_1_1_8_1_1.setBounds(729, 313, 290, 20);
		lblNewLabel_1_1_1_8_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_8_1_1.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_8_1_1.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1_1_8_1_1);
		
		Mostra_QTD_Pedir = new JTextField();
		Mostra_QTD_Pedir.setBounds(729, 345, 290, 20);
		Mostra_QTD_Pedir.setForeground(new Color(255, 0, 0));
		Mostra_QTD_Pedir.setBackground(new Color(240, 240, 240));
		Mostra_QTD_Pedir.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_QTD_Pedir.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_QTD_Pedir.setEditable(false);
		Mostra_QTD_Pedir.setColumns(10);
		contentPane.add(Mostra_QTD_Pedir);
		
		JLabel lblNewLabel_1_1_1_8_1_2 = new JLabel("Rejunte");
		lblNewLabel_1_1_1_8_1_2.setBounds(729, 376, 290, 25);
		lblNewLabel_1_1_1_8_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_8_1_2.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_8_1_2.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1_1_8_1_2);
		
		JLabel lblNewLabel_1_1_1_8_1_2_1 = new JLabel("Argamassa");
		lblNewLabel_1_1_1_8_1_2_1.setBounds(399, 376, 290, 25);
		lblNewLabel_1_1_1_8_1_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_8_1_2_1.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_8_1_2_1.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1_1_8_1_2_1);
		
		JButton Botao_Foto_Piso = new JButton("Foto Piso");
		Botao_Foto_Piso.setIcon(new ImageIcon(CaminhoImagem+"\\Foto.png"));
		Botao_Foto_Piso.setBounds(559, 206, 140, 35);
		Botao_Foto_Piso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
			        URI link = new URI(link_foto);
			        Desktop.getDesktop().browse(link);
			    }catch(Exception erro){
			            System.out.println(erro);
			        }
			}
		});
		Botao_Foto_Piso.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		contentPane.add(Botao_Foto_Piso);
		
		Mostra_KG_Argamassa = new JTextField();
		Mostra_KG_Argamassa.setBackground(new Color(240, 240, 240));
		Mostra_KG_Argamassa.setBounds(399, 439, 130, 20);
		Mostra_KG_Argamassa.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_KG_Argamassa.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_KG_Argamassa.setEditable(false);
		Mostra_KG_Argamassa.setColumns(10);
		contentPane.add(Mostra_KG_Argamassa);
		
		JLabel lblNewLabel_1_1_1_8_1_2_2 = new JLabel("KG");
		lblNewLabel_1_1_1_8_1_2_2.setBounds(729, 412, 130, 20);
		lblNewLabel_1_1_1_8_1_2_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_8_1_2_2.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_8_1_2_2.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1_1_8_1_2_2);
		
		Mostra_KG_Rejunte = new JTextField();
		Mostra_KG_Rejunte.setBounds(729, 439, 130, 20);
		Mostra_KG_Rejunte.setBackground(new Color(240, 240, 240));
		Mostra_KG_Rejunte.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_KG_Rejunte.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_KG_Rejunte.setEditable(false);
		Mostra_KG_Rejunte.setColumns(10);
		contentPane.add(Mostra_KG_Rejunte);
		
		Mostra_Sacos_Argamassa = new JTextField();
		Mostra_Sacos_Argamassa.setBackground(new Color(240, 240, 240));
		Mostra_Sacos_Argamassa.setBounds(559, 439, 130, 20);
		Mostra_Sacos_Argamassa.setForeground(new Color(255, 0, 0));
		Mostra_Sacos_Argamassa.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_Sacos_Argamassa.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_Sacos_Argamassa.setEditable(false);
		Mostra_Sacos_Argamassa.setColumns(10);
		contentPane.add(Mostra_Sacos_Argamassa);
		
		Mostra_Sacos_Rejunte = new JTextField();
		Mostra_Sacos_Rejunte.setBounds(889, 439, 130, 20);
		Mostra_Sacos_Rejunte.setForeground(new Color(255, 0, 0));
		Mostra_Sacos_Rejunte.setBackground(new Color(240, 240, 240));
		Mostra_Sacos_Rejunte.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_Sacos_Rejunte.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_Sacos_Rejunte.setEditable(false);
		Mostra_Sacos_Rejunte.setColumns(10);
		contentPane.add(Mostra_Sacos_Rejunte);
		
		JLabel lblNewLabel_1_1_1_8_1_2_2_1 = new JLabel("Sacos");
		lblNewLabel_1_1_1_8_1_2_2_1.setBounds(889, 412, 130, 20);
		lblNewLabel_1_1_1_8_1_2_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_8_1_2_2_1.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_8_1_2_2_1.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1_1_8_1_2_2_1);
		
		JLabel lblNewLabel_1_1_1_5_1 = new JLabel("Local de Uso");
		lblNewLabel_1_1_1_5_1.setBounds(71, 579, 234, 20);
		lblNewLabel_1_1_1_5_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_5_1.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_5_1.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1_1_5_1);
		
		JLabel lblNewLabel_1_1_1_5_1_1 = new JLabel("Tipo de Piso");
		lblNewLabel_1_1_1_5_1_1.setBounds(71, 517, 234, 20);
		lblNewLabel_1_1_1_5_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_5_1_1.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_5_1_1.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1_1_5_1_1);
		
		Mostra_Tipo_Piso = new JTextField();
		Mostra_Tipo_Piso.setBackground(new Color(240, 240, 240));
		Mostra_Tipo_Piso.setBounds(71, 548, 234, 20);
		Mostra_Tipo_Piso.setForeground(new Color(255, 0, 0));
		Mostra_Tipo_Piso.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_Tipo_Piso.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
		Mostra_Tipo_Piso.setEditable(false);
		Mostra_Tipo_Piso.setColumns(10);
		contentPane.add(Mostra_Tipo_Piso);
		
		Mostra_Pei = new JTextField();
		Mostra_Pei.setBackground(new Color(240, 240, 240));
		Mostra_Pei.setBounds(71, 483, 110, 20);
		Mostra_Pei.setForeground(new Color(255, 0, 0));
		Mostra_Pei.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_Pei.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_Pei.setEditable(false);
		Mostra_Pei.setColumns(10);
		contentPane.add(Mostra_Pei);
		
		JLabel lblNewLabel_1_1_1_5_1_1_1 = new JLabel("PEI");
		lblNewLabel_1_1_1_5_1_1_1.setBounds(71, 452, 110, 20);
		lblNewLabel_1_1_1_5_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_5_1_1_1.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_5_1_1_1.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1_1_5_1_1_1);
		
		JLabel lblNewLabel_1_1_1_8_1_2_3 = new JLabel("Retificado?");
		lblNewLabel_1_1_1_8_1_2_3.setBounds(195, 452, 110, 20);
		lblNewLabel_1_1_1_8_1_2_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_8_1_2_3.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_8_1_2_3.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(lblNewLabel_1_1_1_8_1_2_3);
		
		JButton Botao_Site_Piso = new JButton("Site Piso");
		Botao_Site_Piso.setIcon(new ImageIcon(CaminhoImagem+"\\Site.png"));
		Botao_Site_Piso.setBounds(399, 206, 140, 35);
		Botao_Site_Piso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
			        URI link = new URI(link_site);
			        Desktop.getDesktop().browse(link);
			    }catch(Exception erro){
			            System.out.println(erro);
			    }
			}
		});
		Botao_Site_Piso.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		contentPane.add(Botao_Site_Piso);
		
		Mostra_Retificado = new JTextField();
		Mostra_Retificado.setBackground(new Color(240, 240, 240));
		Mostra_Retificado.setBounds(195, 483, 110, 20);
		Mostra_Retificado.setHorizontalAlignment(SwingConstants.CENTER);
		Mostra_Retificado.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Mostra_Retificado.setEditable(false);
		Mostra_Retificado.setColumns(10);
		contentPane.add(Mostra_Retificado);
		
		BT_Whats = new JButton("Enviar por Whats");
		BT_Whats.setIcon(new ImageIcon(CaminhoImagem+"\\Whatsapp2.png"));
		BT_Whats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String telefoneComMascara = FTF_Telefone.getText();
	            String telefoneSemMascara = "55"+ telefoneComMascara.replaceAll("[^0-9]", "");
	            String nome = Mostra_Nome_Piso.getText(), m = Mostra_M_CX.getText(), pcx = Mostra_Peca_CX.getText(), valor = FTF_Valor.getText(), telefone = telefoneSemMascara, link = link_site;
	            String mensagem = nome+".\nCada caixa do piso vem com "+pcx+ " peças e com "+m+"m².\nO valor é de R$"+valor+" por m².\nVeja mais sobre o piso no site: "+link;
	            	
	            String mensagemCodificada = null;
				try {
					mensagemCodificada = URLEncoder.encode(mensagem, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
	            String url = "https://wa.me/" + telefone + "?text=" + mensagemCodificada;
				 try{
			        URI linkwpp = new URI(url);
			        Desktop.getDesktop().browse(linkwpp);
			    }catch(Exception erro){
			            System.out.println(erro);
			    }
			}
		});
		BT_Whats.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		BT_Whats.setBounds(779, 184, 240, 35);
		contentPane.add(BT_Whats);
		
		JLabel LB_Valor = new JLabel("R$ por m²");
		LB_Valor.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Valor.setForeground(Color.BLACK);
		LB_Valor.setFont(new Font("Arial", Font.PLAIN, 16));
		LB_Valor.setBounds(779, 121, 115, 20);
		contentPane.add(LB_Valor);
		
		JLabel LB_Telefone = new JLabel("Telefone");
		LB_Telefone.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Telefone.setForeground(Color.BLACK);
		LB_Telefone.setFont(new Font("Arial", Font.PLAIN, 16));
		LB_Telefone.setBounds(904, 121, 115, 20);
		contentPane.add(LB_Telefone);
		
		FTF_Valor = new JFormattedTextField();
		FTF_Valor.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		FTF_Valor.setHorizontalAlignment(SwingConstants.CENTER);
		FTF_Valor.setBounds(779, 152, 115, 20);
		contentPane.add(FTF_Valor);
		
		FTF_Telefone = new JFormattedTextField(Mascara("(##) #####-####"));
		FTF_Telefone.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
		FTF_Telefone.setHorizontalAlignment(SwingConstants.CENTER);
		FTF_Telefone.setBounds(904, 152, 115, 20);
		contentPane.add(FTF_Telefone);
		
		JLabel lblNewLabel_1_1_1_6_2 = new JLabel("Dados da Pesquisa");
		lblNewLabel_1_1_1_6_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_6_2.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_6_2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
		lblNewLabel_1_1_1_6_2.setBounds(23, 11, 322, 25);
		contentPane.add(lblNewLabel_1_1_1_6_2);
		
		JLabel lblNewLabel_1_1_1_6_2_1 = new JLabel("Dados do Piso");
		lblNewLabel_1_1_1_6_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_6_2_1.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_6_2_1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
		lblNewLabel_1_1_1_6_2_1.setBounds(399, 11, 620, 25);
		contentPane.add(lblNewLabel_1_1_1_6_2_1);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("Estoque Asso (M²)");
		lblNewLabel_1_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_2.setForeground(Color.BLACK);
		lblNewLabel_1_1_2.setFont(new Font("Arial", Font.PLAIN, 16));
		lblNewLabel_1_1_2.setBounds(399, 83, 140, 20);
		contentPane.add(lblNewLabel_1_1_2);
		
		JLabel lblNewLabel_1_1_3 = new JLabel("Status");
		lblNewLabel_1_1_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_3.setForeground(Color.BLACK);
		lblNewLabel_1_1_3.setFont(new Font("Arial", Font.PLAIN, 16));
		lblNewLabel_1_1_3.setBounds(559, 83, 140, 20);
		contentPane.add(lblNewLabel_1_1_3);
		
		BTEstoque_Asso = new JTextField();
		BTEstoque_Asso.setForeground(new Color(255, 0, 0));
		BTEstoque_Asso.setHorizontalAlignment(SwingConstants.CENTER);
		BTEstoque_Asso.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		BTEstoque_Asso.setEditable(false);
		BTEstoque_Asso.setColumns(10);
		BTEstoque_Asso.setBounds(399, 114, 140, 20);
		contentPane.add(BTEstoque_Asso);
		
		Status_Asso = new JTextField();
		Status_Asso.setForeground(new Color(255, 0, 0));
		Status_Asso.setHorizontalAlignment(SwingConstants.CENTER);
		Status_Asso.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Status_Asso.setEditable(false);
		Status_Asso.setColumns(10);
		Status_Asso.setBounds(559, 114, 140, 20);
		contentPane.add(Status_Asso);
		
		JLabel lblNewLabel_1_1_1_6_2_1_1 = new JLabel("Enviar Informações?");
		lblNewLabel_1_1_1_6_2_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_6_2_1_1.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_6_2_1_1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
		lblNewLabel_1_1_1_6_2_1_1.setBounds(779, 90, 240, 20);
		contentPane.add(lblNewLabel_1_1_1_6_2_1_1);
		
		Mostra_Local_Uso = new JTextArea();
		Mostra_Local_Uso.setWrapStyleWord(true);
		Mostra_Local_Uso.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
		Mostra_Local_Uso.setBorder(new LineBorder(new Color(135, 135, 135)));
		Mostra_Local_Uso.setCaretColor(new Color(0, 0, 0));
		Mostra_Local_Uso.setBackground(new Color(240, 240, 240));
		Mostra_Local_Uso.setEditable(false);
		Mostra_Local_Uso.setLineWrap(true);
		Mostra_Local_Uso.setBounds(71, 610, 234, 70);
		contentPane.add(Mostra_Local_Uso);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(CaminhoImagem+"\\Tabela NiveladorPQ.png"));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(399, 488, 620, 192);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1_1_1_8_1_2_2_2 = new JLabel("KG");
		lblNewLabel_1_1_1_8_1_2_2_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_8_1_2_2_2.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_8_1_2_2_2.setFont(new Font("Arial", Font.PLAIN, 16));
		lblNewLabel_1_1_1_8_1_2_2_2.setBounds(399, 412, 130, 20);
		contentPane.add(lblNewLabel_1_1_1_8_1_2_2_2);
		
		JLabel lblNewLabel_1_1_1_8_1_2_2_1_1 = new JLabel("Sacos");
		lblNewLabel_1_1_1_8_1_2_2_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1_8_1_2_2_1_1.setForeground(Color.BLACK);
		lblNewLabel_1_1_1_8_1_2_2_1_1.setFont(new Font("Arial", Font.PLAIN, 16));
		lblNewLabel_1_1_1_8_1_2_2_1_1.setBounds(559, 412, 130, 20);
		contentPane.add(lblNewLabel_1_1_1_8_1_2_2_1_1);
		
		lblStatus = new JLabel("");
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setBounds(709, 110, 35, 24);
		contentPane.add(lblStatus);
		
		JButton Botao_Imprimir = new JButton("Imprimir Dados do Piso");
		Botao_Imprimir.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	Imprimir();
		      }
		});
		Botao_Imprimir.setMnemonic(KeyEvent.VK_S);
		Botao_Imprimir.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		Botao_Imprimir.setBounds(22, 198, 323, 35);
		contentPane.add(Botao_Imprimir);
		
		JButton BT_Add_Etiqueta = new JButton("Adicionar Etiqueta");
		BT_Add_Etiqueta.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        GerenciadorEtiquetas.adicionarNaFila(
		        	Mostra_Nome_Piso.getText(),
		            Mostra_Cod_Asso.getText(),
		            Mostra_Cod_CTC.getText(),
		            Mostra_M_CX.getText(), 
		            Mostra_Peca_CX.getText(),
		            Mostra_Pei.getText(),
		            "R$ "+FTF_Valor.getText()
		        );
		    }
		});
		BT_Add_Etiqueta.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		BT_Add_Etiqueta.setBounds(779, 232, 240, 25);
		contentPane.add(BT_Add_Etiqueta);
	}
	
	// =========================================================================================
	//  NOVA LÓGICA DE LOGIN (COM OPÇÃO DE CANCELAMENTO E DESBLOQUEIO)
	// =========================================================================================
	public void iniciarRotinaDeLogin() {
	    File arquivoCookie = new File(EstoqueScraper.COOKIE_PATH);
	    if (arquivoCookie.exists()) {
	        lastModifiedTime = arquivoCookie.lastModified();
	    } else {
	        lastModifiedTime = 0;
	    }

	    EstoqueScraper.abrirNavegadorApenas();

	    dialogEspera = new JDialog(this, "Aguardando Login", true); 
	    dialogEspera.setSize(400, 220); // Aumentei um pouco a altura para caber o botão
	    dialogEspera.setLayout(new BorderLayout());
	    dialogEspera.setLocationRelativeTo(this);
	    
	    // PERMITE FECHAR NO 'X' PARA NÃO TRAVAR O SISTEMA
	    dialogEspera.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); 
	    loginConcluido = false;

	    JLabel lblAviso = new JLabel("<html><center><h2>Faça o Login no Chrome...</h2><br>"
	            + "Por favor, realize o login e o captcha na janela do navegador.<br>"
	            + "O sistema detectará automaticamente quando terminar.</center></html>");
	    lblAviso.setHorizontalAlignment(SwingConstants.CENTER);
	    dialogEspera.add(lblAviso, BorderLayout.CENTER);

	    // BOTÃO CANCELAR LOGIN
	    JButton btnCancelar = new JButton("Cancelar Login");
	    btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
	    btnCancelar.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            dialogEspera.dispose(); // Isso chama o evento windowClosed abaixo
	        }
	    });
	    dialogEspera.add(btnCancelar, BorderLayout.SOUTH);

	    // EVENTO: SE A JANELA FOR FECHADA (Seja pelo botão ou pelo 'X')
	    dialogEspera.addWindowListener(new java.awt.event.WindowAdapter() {
	        @Override
	        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
	            if (timerVerificacao != null) {
	                timerVerificacao.stop();
	            }
	            // Só exibe mensagem se fechou sem conseguir o cookie
	            if (!loginConcluido) {
	                EstoqueScraper.fecharChrome();
	                JOptionPane.showMessageDialog(null, "Login cancelado.");
	            }
	        }
	    });

	    timerVerificacao = new Timer(1000, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            EstoqueScraper.tentaCapturarCookieBackground();
	            verificarSeArquivoMudou();
	        }
	    });
	    timerVerificacao.start();

	    dialogEspera.setVisible(true); // Fica aqui até a janela sumir
	}

	private void verificarSeArquivoMudou() {
	    File arquivoCookie = new File(EstoqueScraper.COOKIE_PATH);
	    if (arquivoCookie.exists()) {
	        long currentModified = arquivoCookie.lastModified();
	        
	        // Se o login passou!
	        if (currentModified > lastModifiedTime) {
	            loginConcluido = true; 
	            timerVerificacao.stop();
	            
	            // Muda o texto da tela pra dar feedback
	            JLabel aviso = (JLabel) dialogEspera.getContentPane().getComponent(0);
	            aviso.setText("<html><center><h2>Login Identificado!</h2><br>Retornando ao sistema...</center></html>");
	            
	            // Esconde o botão cancelar para não clicarem atoa
	            Component[] comps = dialogEspera.getContentPane().getComponents();
	            for(Component c : comps) {
	                if(c instanceof JButton) c.setVisible(false);
	            }

	            Timer delayTimer = new Timer(1000, new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent evt) {
	                    dialogEspera.dispose(); 
	                    EstoqueScraper.fecharChrome(); 
	                    cookieValidado = true;
	                    realizarPesquisa();
	                }
	            });
	            delayTimer.setRepeats(false); 
	            delayTimer.start();
	        }
	    }
	}
	
	private void realizarPesquisa() {
        try {
            m_cliente = Double.parseDouble(Pesquisa_M_Cliente.getText().replace(",", "."));
            cx_cliente = Double.parseDouble(Pesquisa_CX_Cliente.getText().replace(",", "."));
        } catch (Exception e) {
            m_cliente = 0;
            cx_cliente = 0;
        }

        apagar();

	     if (carregarDadosDoPiso()) {
	    	 boolean temCodAsso = cod_asso_banco != null && !cod_asso_banco.isEmpty();

	    	 if (temCodAsso) {
	    	     try {
	    	         int resultado = EstoqueScraper.buscarEstoque(cod_asso_banco); 
	                 
	                 if (resultado == 1) {
	                     BTEstoque_Asso.setText(EstoqueScraper.estoque);
	                     Status_Asso.setText(EstoqueScraper.status);
	                 } else if (resultado == 2) {
	                     BTEstoque_Asso.setText("-");
	                     Status_Asso.setText("Fora de linha");
	                     lblStatus.setIcon(new ImageIcon(CaminhoImagem+"\\Circulo Laranja.png"));
	                     JOptionPane.showMessageDialog(null, "Produto fora de linha!");
	                     
	                 } else if (resultado == 0) {
	                     cookieValidado = false;
	                     JOptionPane.showMessageDialog(null, "Sessão inválida. Reiniciando login...");
	                     iniciarRotinaDeLogin(); 
	                     return;
	                 }
	             } catch (IOException e1) {
	                 JOptionPane.showMessageDialog(null, "Erro ao buscar estoque: " + e1.getMessage());
	                 return;
	             }
	         } else {
	             BTEstoque_Asso.setText("-");
	             Status_Asso.setText("Produto local");
	             lblStatus.setIcon(new ImageIcon(CaminhoImagem+"\\Circulo Verde.png"));
	         }
	         
	         calculos();
	         mostrar();
	         
	         try {
				status_cor = Status_Asso.getText();
				estoque_cor = Double.parseDouble(BTEstoque_Asso.getText().replace("," , "."));
				
				if(status_cor.equals("PADRAO")) lblStatus.setIcon(new ImageIcon(CaminhoImagem+"\\Circulo Verde.png"));
				
				if(status_cor.equals("ACABAR") && estoque_cor > 1) {
					lblStatus.setIcon(new ImageIcon(CaminhoImagem+"\\Circulo Azul.png"));
					JOptionPane.showMessageDialog(null, "Produto fora de linha porém com estoque na Asso!");
				}
				
				if(status_cor.equals("ACABAR") && estoque_cor < 1) {
					lblStatus.setIcon(new ImageIcon(CaminhoImagem+"\\Circulo Laranja.png"));
					JOptionPane.showMessageDialog(null, "Produto fora de linha!");
				}
				
				if(status_cor.equals("PADRAO") && estoque_cor < 1) {
					lblStatus.setIcon(new ImageIcon(CaminhoImagem+"\\Circulo Vermelho.png"));
					JOptionPane.showMessageDialog(null, "Produto padrão, mas sem estoque!");
				}
				if(status_cor.equals("NEGOCIACAO")) lblStatus.setIcon(new ImageIcon(CaminhoImagem+"\\Circulo Verde.png"));
	         } catch (Exception e) {}
	     }
	    txtltimaAtt.requestFocusInWindow();
	}
	
	public boolean carregarDadosDoPiso() {
		cod_asso_banco = null;
	    String cod_asso = Pesquisa_Cod_Piso.getText();
	    try {
	        Connection conexao;
	        Statement stm;
	        Class.forName("com.mysql.jdbc.Driver");
	        conexao = DriverManager.getConnection(CaminhoBD+"", "root", "");
	        stm = conexao.createStatement();
	        ResultSet rs = stm.executeQuery("Select * from piso WHERE cod_asso = '" + cod_asso + "' OR cod_ctc ='" + cod_asso + "'");

	        if (rs.next()) { 
	            altura = Double.parseDouble(rs.getString("altura"));
	            largura = Double.parseDouble(rs.getString("largura"));
	            rejunte = Double.parseDouble(rs.getString("rejunte"));
	            pecas_cx = Integer.parseInt(rs.getString("pecas_cx"));
	            m_caixas = Double.parseDouble(rs.getString("m_cx")); 
	            
	            cod_asso_banco = rs.getString("cod_asso");

	            Mostra_Nome_Piso.setText(rs.getString("nome"));
	            Mostra_Cod_Asso.setText(rs.getString("cod_asso"));
	            Mostra_Cod_CTC.setText(rs.getString("cod_ctc"));
	            Mostra_Retificado.setText(rs.getString("retificado"));
	            Mostra_Local_Uso.setText(rs.getString("local_uso"));
	            Mostra_Pei.setText(rs.getString("pei"));
	            Mostra_Tipo_Piso.setText(rs.getString("tipo_piso"));
	            link_site = rs.getString("site");
	            link_ambiente = rs.getString("ambiente");
	            link_foto = rs.getString("foto");
	            
	            return true; 
	        } else {
	            JOptionPane.showMessageDialog(null, "Piso com código '" + cod_asso + "' não encontrado.");
	            return false; 
	        }
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public boolean VerificaCodigo(String Pesquisa_Piso) {
		return Pesquisa_Piso.equals("");
		
	}
	
	public boolean VerificaValor(String Pesquisa_M, String Pesquisa_CX) {
		return Pesquisa_M.equals("") && Pesquisa_CX.equals("");
	}
	
	public void apagar () {
			
		Mostra_Alt.setText("");
		Mostra_Cod_Asso.setText("");
		Mostra_Cod_CTC.setText("");
		Mostra_CX_Arredondado.setText("");
		Mostra_Larg.setText("");
		Mostra_KG_Rejunte.setText("");
		Mostra_KG_Argamassa.setText("");
		Mostra_CX_Exato.setText("");
		Mostra_Local_Uso.setText("");
		Mostra_M_CX.setText("");
		Mostra_M_Peca.setText("");
		Mostra_Peca_CX.setText("");
		Mostra_Pei.setText("");
		Mostra_QTD_Pedir.setText("");
		Mostra_Rejunte.setText("");
		Mostra_Retificado.setText("");
		Mostra_Sacos_Argamassa.setText("");
		Mostra_Sacos_Rejunte.setText("");
		Mostra_Tipo_Piso.setText("");
		Mostra_Nome_Piso.setText("");
		
	}

	public void mostrar() {
	    Mostra_Larg.setText(String.valueOf(largura));
	    Mostra_Alt.setText(String.valueOf(altura));
	    Mostra_Peca_CX.setText(String.valueOf(pecas_cx));
	    Mostra_M_CX.setText(String.format("%.2f", m_caixas).replace(",", "."));
	    Mostra_Rejunte.setText(String.valueOf(rejunte));
	    Mostra_M_Peca.setText(String.format("%.4f", area_pc).replace(",", "."));

	    Mostra_CX_Arredondado.setText(String.valueOf(caixas_arredondadas)); 
	    Mostra_QTD_Pedir.setText(String.format("%.2f", m_pedir).replace(",", "."));
	    
	    Mostra_KG_Argamassa.setText(String.format("%.2f", argamassa).replace(",", "."));
	    Mostra_Sacos_Argamassa.setText(String.valueOf((int)Math.ceil(saco_argamassa)));
	    
	    Mostra_KG_Rejunte.setText(String.format("%.2f", rejunte_total).replace(",", "."));
	    Mostra_Sacos_Rejunte.setText(String.valueOf((int)Math.ceil(rejunte_total)));
	}
	
	public void calculos() {
	    area_pc = (altura / 100) * (largura / 100);

	    if (m_cliente != 0) {
	        BigDecimal m_cliente_bd = new BigDecimal(String.valueOf(m_cliente));
	        BigDecimal m_caixas_bd = new BigDecimal(String.valueOf(m_caixas));
	        
	        caixas_arredondadas = m_cliente_bd.divide(m_caixas_bd, 0, RoundingMode.CEILING).intValue();
	        cx_exatas = m_cliente / m_caixas;

	    } else {
	        cx_exatas = cx_cliente;
	        caixas_arredondadas = (int)Math.ceil(cx_exatas);
	    }

	    m_pedir = m_caixas * caixas_arredondadas;

	    double soma_lados_mm = (largura * 10) + (altura * 10);
	    double area_peca_mm = (largura * 10) * (altura * 10);
	    if (area_peca_mm > 0) {
	        rejunte_kg = (soma_lados_mm * profund * rejunte * coeficiente) / area_peca_mm;
	        rejunte_total = m_pedir * rejunte_kg;
	    }

	    saco_argamassa = m_pedir / 3.0;
	    argamassa = saco_argamassa * 20;
	}
	
	public MaskFormatter Mascara(String Mascara){
        MaskFormatter F_Mascara = new MaskFormatter();
        try{
            F_Mascara.setMask(Mascara); 
            F_Mascara.setPlaceholderCharacter(' '); 
        }
        catch (Exception excecao) {
        excecao.printStackTrace();
        } 
        return F_Mascara;
	} 	

	public void Imprimir() {
	    javax.print.PrintService[] services = java.awt.print.PrinterJob.lookupPrintServices();
	    javax.print.PrintService elginService = null;

	    for (javax.print.PrintService service : services) {
	        String nome = service.getName().toUpperCase();
	        if (nome.contains("ELGIN") || nome.contains("NFCE")) {
	            elginService = service;
	            break; 
	        }
	    }

	    if (elginService == null) {
	        javax.swing.JOptionPane.showMessageDialog(null, "Impressora ELGIN não encontrada!");
	        return;
	    }

	    java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
	    try {
	        job.setPrintService(elginService);
	    } catch (java.awt.print.PrinterException e) {
	        e.printStackTrace();
	        return;
	    }

	    java.awt.print.PageFormat pf = job.defaultPage();
	    java.awt.print.Paper paper = pf.getPaper();

	    double width = 226; 
	    double height = 3000; 
	    double margin = 5;   

	    paper.setSize(width, height);
	    paper.setImageableArea(margin, 0, width - (2 * margin), height);
	    pf.setPaper(paper);

	    job.setPrintable(new java.awt.print.Printable() {
	        @Override
	        public int print(java.awt.Graphics g, java.awt.print.PageFormat pf, int page) throws java.awt.print.PrinterException {
	            if (page > 0) return NO_SUCH_PAGE;

	            java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
	            g2d.translate(pf.getImageableX(), pf.getImageableY());

	            int y = 20; 
	            int x = 0; 
	            
	            java.awt.Font fonteTitulo = new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12);
	            java.awt.Font fonteNormal = new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10);
	            java.awt.Font fonteNegrito = new java.awt.Font("SansSerif", java.awt.Font.BOLD, 11);
	            java.awt.Font fonteGrande = new java.awt.Font("SansSerif", java.awt.Font.BOLD, 13);
	            java.awt.Font fonteNome = new java.awt.Font("SansSerif", java.awt.Font.BOLD, 11);
	            java.awt.Font fonteDivisoria = new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 10);

	            g2d.setFont(fonteTitulo);
	            g2d.drawString("   CASA DOS TUBOS", x, y); 
	            y += 18;
	            
	            g2d.setFont(fonteDivisoria);
	            g2d.drawString("---------------------------------", x, y); 
	            y += 18;

	            g2d.setFont(fonteNormal);
	            g2d.drawString("PRODUTO:", x, y); 
	            y += 15;
	            
	            g2d.setFont(fonteNome);
	            String nomePiso = Mostra_Nome_Piso.getText();
	            int limitePorLinha = 22;
	            
	            for (int i = 0; i < 6; i++) {
	                int inicio = i * limitePorLinha;
	                if (inicio >= nomePiso.length()) break; 
	                int fim = Math.min(inicio + limitePorLinha, nomePiso.length());
	                String pedaco = nomePiso.substring(inicio, fim);
	                if (i == 5 && fim < nomePiso.length()) {
	                    pedaco = nomePiso.substring(inicio, fim - 3) + "...";
	                }
	                g2d.drawString(pedaco, x + 5, y); 
	                y += 16; 
	            }

	            g2d.setFont(fonteDivisoria);
	            g2d.drawString("---------------------------------", x, y); 
	            y += 18;

	            g2d.setFont(fonteGrande); 
	            g2d.drawString("COD ASSO: " + Mostra_Cod_Asso.getText(), x, y); 
	            y += 18;
	            
	            g2d.setFont(fonteNormal);
	            g2d.drawString("COD CTC : " + Mostra_Cod_CTC.getText(), x, y); 
	            y += 20;

	            g2d.setFont(fonteDivisoria);
	            g2d.drawString("---------------------------------", x, y); 
	            y += 18;

	            g2d.setFont(fonteGrande);
	            g2d.drawString("QTD PEDIR: " + Mostra_QTD_Pedir.getText() + " m²", x, y); 
	            y += 20;
	            
	            g2d.drawString("QTD CAIXAS: " + Mostra_CX_Arredondado.getText() + " Caixa (s)", x, y); 
	            y += 20;

	            g2d.setFont(fonteDivisoria);
	            g2d.drawString("---------------------------------", x, y); 
	            y += 18;

	            g2d.setFont(fonteGrande);
	            g2d.drawString("Argamassa: " + Mostra_Sacos_Argamassa.getText() + " Saco (s)", x, y); 
	            y += 18;
	            g2d.drawString("Rejunte  : " + Mostra_Sacos_Rejunte.getText() + " Saco (s)", x, y); 
	            y += 20;

	            g2d.setFont(fonteDivisoria);
	            g2d.drawString("---------------------------------", x, y); 
	            y += 18;

	            g2d.setFont(new java.awt.Font("SansSerif", java.awt.Font.ITALIC, 9));
	            java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	            g2d.drawString(java.time.LocalDateTime.now().format(dtf), x + 40, y); 
	            
	            y += 20; 

	            if (y < 250) {
	                y = 250; 
	            }
	            g2d.drawString(".", x, y); 

	            return PAGE_EXISTS;
	        }
	    }, pf);

	    try {
	        job.print();
	    } catch (java.awt.print.PrinterException ex) {
	        javax.swing.JOptionPane.showMessageDialog(null, "Erro na impressão: " + ex.getMessage());
	    }
	}
}