package PisoAsso;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Cadastro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField Nome_Piso;
	private JTextField Cod_Asso;
	private JTextField Cod_Loja;
	private JTextField Largura;
	private JTextField Altura;
	private JTextField Peca_CX;
	private JTextField M_CX;
	private JTextField Local_Uso;
	private JTextField Site_Piso;
	private JTextField Foto_Piso;
	private JTextField Tipo_Piso;
	private JTextField Bruto;
	private JTextField Lucro;
	private JTextField Desc;
	private JTextField Valor_Desc;
	private JTextField Piso_P_Copiar;
	private JRadioButton LA;
	private JRadioButton LB;
	private JRadioButton LC;
	private JRadioButton LD;
	private JRadioButton LE;
	private JRadioButton Ret_Sim;
	private JRadioButton Ret_Nao;
	private JRadioButton mm1;
	private JRadioButton mm15;
	private JRadioButton mm3;
	private JRadioButton mm4;
	private JRadioButton mm2;
	private JRadioButton mm5;
	private JLabel Tamanho;
	private JButton Copia_Infos;
	public static int fila =0;
	private JButton Botao_Cadastra_Piso;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cadastro frame = new Cadastro();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Cadastro() {
		setTitle("Cadastra Piso");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Procura.CaminhoImagem + "\\Logo Casa dos Tubos 50_page-0001.jpg"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 829, 680);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton Botao_Volta_Tela_Inicial = new JButton("Voltar Tela Inicial");
		Botao_Volta_Tela_Inicial.setBounds(608, 606, 194, 21);
		Botao_Volta_Tela_Inicial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Procura TelaProcura = new Procura();
				TelaProcura.setVisible(true);
				TelaProcura.setLocationRelativeTo(null);
				dispose();
			}
		});
		Botao_Volta_Tela_Inicial.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		contentPane.add(Botao_Volta_Tela_Inicial);
		
		Botao_Cadastra_Piso = new JButton("Cadastrar Piso (" + GerenciadorEtiquetas.contarFila() + ")");
		Botao_Cadastra_Piso.setBounds(400, 575, 198, 21);
		Botao_Cadastra_Piso.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        
		        // 1. PEGAR OS DADOS ANTES DE TUDO (Enquanto os campos ainda estão preenchidos)
		        String nomeSalvo = Nome_Piso.getText();
		        String assoSalvo = Cod_Asso.getText();
		        String lojaSalvo = Cod_Loja.getText(); // ou Cod_CTC dependendo do nome do seu campo
		        String mcxSalvo  = M_CX.getText();
		        String precoSalvo = "R$ " + Valor_Desc.getText();
		        
		        // Lógica do PEI
		        String peiSalvo = "";
		        if (LA.isSelected()) peiSalvo = "LA";
		        else if (LB.isSelected()) peiSalvo = "LA, LB";
		        else if (LC.isSelected()) peiSalvo = "LA, LB, LC";
		        else if (LD.isSelected()) peiSalvo = "LA, LB, LC, LD";
		        else if (LE.isSelected()) peiSalvo = "LA, LB, LC, LD, LE";

		        // Lógica de Cálculo (Blindada contra erro de campo vazio)
		        String peca_caixas = "0";
		        try {
		            double alt = Double.parseDouble(Altura.getText().replace(",", "."));
		            double larg = Double.parseDouble(Largura.getText().replace(",", "."));
		            double metros_caixa = Double.parseDouble(M_CX.getText().replace(",", "."));
		            
		            if (alt > 0 && larg > 0) {
		                double result = metros_caixa / ((alt / 100) * (larg / 100));
		                peca_caixas = new DecimalFormat("#0").format(result);
		            }
		        } catch (Exception ex) {
		            // Se der erro no calculo (campo vazio), define como zero mas não trava o sistema
		            peca_caixas = "0";
		        }

		        // 2. TENTA CADASTRAR NO BANCO
		        // Se o cadastra_piso() der erro, o código para aqui e não adiciona na fila (o que é bom)
		        cadastra_piso();
		        
		        // 3. ADICIONA NA FILA (Usando as variáveis que salvamos no passo 1)
		        GerenciadorEtiquetas.adicionarNaFila(
		            nomeSalvo,
		            assoSalvo,
		            lojaSalvo,
		            mcxSalvo, 
		            peca_caixas,
		            peiSalvo,
		            precoSalvo
		        );
		        
		        Botao_Cadastra_Piso.setText("Cadastrar Piso (" + GerenciadorEtiquetas.contarFila() + ")");
		    }
		});
		Botao_Cadastra_Piso.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		contentPane.add(Botao_Cadastra_Piso);
		
		JButton Botao_Apaga_Tudo = new JButton("Limpar Dados");
		Botao_Apaga_Tudo.setBounds(400, 606, 198, 21);
		Botao_Apaga_Tudo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apaga_dados();
			}
		});
		Botao_Apaga_Tudo.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		contentPane.add(Botao_Apaga_Tudo);
		
		JLabel lblNewLabel = new JLabel("TELA DE CADASTRO DE PISO");
		lblNewLabel.setBounds(10, 11, 798, 27);
		lblNewLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Nome do Piso");
		lblNewLabel_1.setBounds(10, 47, 798, 21);
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel_1);
		
		JButton Botao_Piso_Cadastrados = new JButton("Alterar Cadastros");
		Botao_Piso_Cadastrados.setBounds(608, 575, 194, 21);
		Botao_Piso_Cadastrados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Pisos_Cadastrados TelaCadastro = new Pisos_Cadastrados();
				TelaCadastro.setVisible(true);
				TelaCadastro.setLocationRelativeTo(null);
				dispose();
			}
		});
		Botao_Piso_Cadastrados.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		contentPane.add(Botao_Piso_Cadastrados);
		
		Nome_Piso = new JTextField();
		Nome_Piso.setBounds(10, 79, 798, 21);
		Nome_Piso.setHorizontalAlignment(SwingConstants.CENTER);
		Nome_Piso.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(Nome_Piso);
		Nome_Piso.setColumns(10);
		
		Cod_Asso = new JTextField();
		Cod_Asso.setBounds(10, 140, 192, 21);
		Cod_Asso.setHorizontalAlignment(SwingConstants.CENTER);
		Cod_Asso.setFont(new Font("Arial", Font.PLAIN, 16));
		Cod_Asso.setColumns(10);
		contentPane.add(Cod_Asso);
		
		Cod_Loja = new JTextField();
		Cod_Loja.setBounds(616, 140, 192, 21);
		Cod_Loja.setHorizontalAlignment(SwingConstants.CENTER);
		Cod_Loja.setFont(new Font("Arial", Font.PLAIN, 16));
		Cod_Loja.setColumns(10);
		contentPane.add(Cod_Loja);
		
		Largura = new JTextField();
		Largura.setBounds(10, 232, 192, 21);
		Largura.setHorizontalAlignment(SwingConstants.CENTER);
		Largura.setFont(new Font("Arial", Font.PLAIN, 16));
		Largura.setColumns(10);
		contentPane.add(Largura);
		
		Altura = new JTextField();
		Altura.setBounds(212, 232, 192, 21);
		Altura.setHorizontalAlignment(SwingConstants.CENTER);
		Altura.setFont(new Font("Arial", Font.PLAIN, 16));
		Altura.setColumns(10);
		contentPane.add(Altura);
		
		Peca_CX = new JTextField();
		Peca_CX.setBounds(414, 232, 192, 21);
		Peca_CX.setEditable(false);
		Peca_CX.setHorizontalAlignment(SwingConstants.CENTER);
		Peca_CX.setFont(new Font("Arial", Font.PLAIN, 16));
		Peca_CX.setColumns(10);
		contentPane.add(Peca_CX);
		
		M_CX = new JTextField();
		M_CX.setBounds(616, 232, 192, 21);
		M_CX.setHorizontalAlignment(SwingConstants.CENTER);
		M_CX.setFont(new Font("Arial", Font.PLAIN, 16));
		M_CX.setColumns(10);
		contentPane.add(M_CX);
		
		Local_Uso = new JTextField();
		Local_Uso.setBounds(10, 415, 792, 21);
		Local_Uso.setForeground(new Color(128, 128, 128));
		Local_Uso.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if ("Ex.: Garagem, piscina, parede, externo, interno...".equals(Local_Uso.getText())) {
					Local_Uso.setText("");
					Local_Uso.setForeground(Color.BLACK);
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (Local_Uso.getText().isEmpty()) {
					Local_Uso.setText("Ex.: Garagem, piscina, parede, externo, interno...");
					Local_Uso.setForeground(Color.GRAY);
				}
			}
		});
		Local_Uso.setText("Ex.: Garagem, piscina, parede, externo, interno...");
		Local_Uso.setHorizontalAlignment(SwingConstants.CENTER);
		Local_Uso.setFont(new Font("Arial", Font.PLAIN, 16));
		Local_Uso.setColumns(10);
		contentPane.add(Local_Uso);
		
		JLabel lblNewLabel_1_1 = new JLabel("Código Asso");
		lblNewLabel_1_1.setBounds(10, 111, 192, 21);
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setFont(new Font("Arial", Font.BOLD, 16));
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("Código Loja (Se tiver)");
		lblNewLabel_1_2.setBounds(616, 108, 192, 21);
		lblNewLabel_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2.setFont(new Font("Arial", Font.BOLD, 16));
		contentPane.add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_3 = new JLabel("Largura (cm)");
		lblNewLabel_1_3.setBounds(10, 201, 192, 21);
		lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3.setFont(new Font("Arial", Font.BOLD, 16));
		contentPane.add(lblNewLabel_1_3);
		
		JLabel lblNewLabel_1_4 = new JLabel("Altura (cm)");
		lblNewLabel_1_4.setBounds(212, 200, 192, 21);
		lblNewLabel_1_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_4.setFont(new Font("Arial", Font.BOLD, 16));
		contentPane.add(lblNewLabel_1_4);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Rejunte (mm)");
		lblNewLabel_1_1_1.setBounds(10, 263, 394, 21);
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1.setFont(new Font("Arial", Font.BOLD, 16));
		contentPane.add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_1_2_1 = new JLabel("Peças p/ CX");
		lblNewLabel_1_2_1.setBounds(414, 201, 192, 21);
		lblNewLabel_1_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2_1.setFont(new Font("Arial", Font.BOLD, 16));
		contentPane.add(lblNewLabel_1_2_1);
		
		JLabel lblNewLabel_1_3_1 = new JLabel("M² p/  CX");
		lblNewLabel_1_3_1.setBounds(616, 200, 192, 21);
		lblNewLabel_1_3_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3_1.setFont(new Font("Arial", Font.BOLD, 16));
		contentPane.add(lblNewLabel_1_3_1);
		
		JLabel lblNewLabel_1_5 = new JLabel("Local de Uso");
		lblNewLabel_1_5.setBounds(10, 383, 792, 21);
		lblNewLabel_1_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_5.setFont(new Font("Arial", Font.BOLD, 16));
		contentPane.add(lblNewLabel_1_5);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("Tipo de Piso");
		lblNewLabel_1_1_2.setBounds(414, 319, 388, 21);
		lblNewLabel_1_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_2.setFont(new Font("Arial", Font.BOLD, 16));
		contentPane.add(lblNewLabel_1_1_2);
		
		JLabel lblNewLabel_1_2_2 = new JLabel("PEI");
		lblNewLabel_1_2_2.setBounds(10, 319, 394, 21);
		lblNewLabel_1_2_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2_2.setFont(new Font("Arial", Font.BOLD, 16));
		contentPane.add(lblNewLabel_1_2_2);
		
		JLabel lblNewLabel_1_1_2_1 = new JLabel("Site Piso");
		lblNewLabel_1_1_2_1.setBounds(10, 447, 792, 21);
		lblNewLabel_1_1_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_2_1.setFont(new Font("Arial", Font.BOLD, 16));
		contentPane.add(lblNewLabel_1_1_2_1);
		
		Site_Piso = new JTextField();
		Site_Piso.setBounds(10, 479, 792, 21);
		Site_Piso.setHorizontalAlignment(SwingConstants.CENTER);
		Site_Piso.setFont(new Font("Arial", Font.PLAIN, 16));
		Site_Piso.setColumns(10);
		contentPane.add(Site_Piso);
		
		JLabel lblNewLabel_1_2_2_1 = new JLabel("Link Foto");
		lblNewLabel_1_2_2_1.setBounds(14, 511, 788, 21);
		lblNewLabel_1_2_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2_2_1.setFont(new Font("Arial", Font.BOLD, 16));
		contentPane.add(lblNewLabel_1_2_2_1);
		
		Foto_Piso = new JTextField();
		Foto_Piso.setBounds(10, 543, 792, 21);
		Foto_Piso.setHorizontalAlignment(SwingConstants.CENTER);
		Foto_Piso.setFont(new Font("Arial", Font.PLAIN, 16));
		Foto_Piso.setColumns(10);
		contentPane.add(Foto_Piso);
		
		Tipo_Piso = new JTextField();
		Tipo_Piso.setBounds(414, 351, 388, 21);
		Tipo_Piso.setText("Ex.: Acetinado, Polído...");
		Tipo_Piso.setForeground(new Color(128, 128, 128));
		Tipo_Piso.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if ("Ex.: Acetinado, Polído...".equals(Tipo_Piso.getText())) {
					Tipo_Piso.setText("");
					Tipo_Piso.setForeground(Color.BLACK);
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (Tipo_Piso.getText().isEmpty()) {
					Tipo_Piso.setText("Ex.: Acetinado, Polído...");
					Tipo_Piso.setForeground(Color.GRAY);
				}
			}
		});
		Tipo_Piso.setHorizontalAlignment(SwingConstants.CENTER);
		Tipo_Piso.setFont(new Font("Arial", Font.PLAIN, 16));
		Tipo_Piso.setColumns(10);
		contentPane.add(Tipo_Piso);
		
		Bruto = new JTextField();
		Bruto.setHorizontalAlignment(SwingConstants.CENTER);
		Bruto.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Bruto.setBounds(43, 575, 70, 20);
		contentPane.add(Bruto);
		Bruto.setColumns(10);
		
		Lucro = new JTextField();
		Lucro.setHorizontalAlignment(SwingConstants.CENTER);
		Lucro.setText("90");
		Lucro.setBounds(123, 575, 59, 20);
		Lucro.setColumns(10);
		contentPane.add(Lucro);
		
		Desc = new JTextField();
		Desc.setText("12");
		Desc.setHorizontalAlignment(SwingConstants.CENTER);
		Desc.setBounds(191, 575, 59, 20);
		Desc.setColumns(10);
		contentPane.add(Desc);
		
		Valor_Desc = new JTextField();
		Valor_Desc.setForeground(new Color(255, 0, 0));
		Valor_Desc.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Valor_Desc.setHorizontalAlignment(SwingConstants.CENTER);
		Valor_Desc.setBounds(43, 606, 70, 20);
		Valor_Desc.setColumns(10);
		contentPane.add(Valor_Desc);
		
		JButton Calc_Desc = new JButton("Calcular Valor");
		Calc_Desc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calcularValorFinal();
			}
		});
		Calc_Desc.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 11));
		Calc_Desc.setBounds(123, 606, 128, 21);
		contentPane.add(Calc_Desc);
		
		JLabel R$ = new JLabel("R$");
		R$.setHorizontalAlignment(SwingConstants.CENTER);
		R$.setBounds(10, 575, 31, 19);
		contentPane.add(R$);
		
		JLabel R$_1 = new JLabel("R$");
		R$_1.setHorizontalAlignment(SwingConstants.CENTER);
		R$_1.setBounds(10, 606, 31, 19);
		contentPane.add(R$_1);
		
		Copia_Infos = new JButton("Copiar Infos");
		Copia_Infos.setBounds(261, 575, 129, 20);
		Copia_Infos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copiar();
		        try { java.awt.Desktop.getDesktop().browse(new java.net.URI("https://www.google.com/search?q=" + java.net.URLEncoder.encode(Nome_Piso.getText(), "UTF-8"))); } catch (Exception ex) {}
			}
		});
		Copia_Infos.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		contentPane.add(Copia_Infos);
		
		Piso_P_Copiar = new JTextField();
		Piso_P_Copiar.setHorizontalAlignment(SwingConstants.CENTER);
		Piso_P_Copiar.setFont(new Font("Arial", Font.PLAIN, 16));
		Piso_P_Copiar.setColumns(10);
		Piso_P_Copiar.setBounds(261, 606, 129, 20);
		contentPane.add(Piso_P_Copiar);
		
		LA = new JRadioButton("LA (1)");
		LA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Local_Uso.setText("Paredes residenciais e comerciais internas e externas até 3 metros de altura");
				Local_Uso.setForeground(Color.BLACK);
			}
		});
		LA.setSelected(true);
		LA.setHorizontalAlignment(SwingConstants.CENTER);
		LA.setBounds(10, 351, 70, 21);
		contentPane.add(LA);
		
		LB = new JRadioButton("LB (2)");
		LB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Local_Uso.setText("Ambientes residenciais sem acesso para a rua, como banheiros, salas, quartos e cozinhas");
				Local_Uso.setForeground(Color.BLACK);
			}
		});
		LB.setHorizontalAlignment(SwingConstants.CENTER);
		LB.setBounds(90, 351, 70, 21);
		contentPane.add(LB);
		
		LC = new JRadioButton("LC (3)");
		LC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Local_Uso.setText("Ambientes comerciais e residências internos, sem acesso a áreas externas e de equipamentos");
				Local_Uso.setForeground(Color.BLACK);
			}
		});
		LC.setHorizontalAlignment(SwingConstants.CENTER);
		LC.setBounds(170, 351, 70, 21);
		contentPane.add(LC);
		
		LD = new JRadioButton("LD (4)");
		LD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Local_Uso.setText("Indicado para uso como piso em todos os ambientes residenciais");
				Local_Uso.setForeground(Color.BLACK);
			}
		});
		LD.setHorizontalAlignment(SwingConstants.CENTER);
		LD.setBounds(250, 351, 70, 21);
		contentPane.add(LD);
		
		LE = new JRadioButton("LE (5)");
		LE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Local_Uso.setText("Todos os ambientes residenciais e comerciais externos");
				Local_Uso.setForeground(Color.BLACK);
			}
		});
		LE.setHorizontalAlignment(SwingConstants.CENTER);
		LE.setBounds(330, 351, 70, 21);
		contentPane.add(LE);
		
		JLabel lblNewLabel_1_3_2_1_1 = new JLabel("É Retificado?");
		lblNewLabel_1_3_2_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3_2_1_1.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1_3_2_1_1.setBounds(414, 263, 394, 21);
		contentPane.add(lblNewLabel_1_3_2_1_1);
		
		Ret_Sim = new JRadioButton("Retificado");
		Ret_Sim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mm2.setSelected(true);
			}
		});
		Ret_Sim.setSelected(true);
		Ret_Sim.setHorizontalAlignment(SwingConstants.CENTER);
		Ret_Sim.setBounds(414, 291, 192, 21);
		contentPane.add(Ret_Sim);
		
		Ret_Nao = new JRadioButton("Bold");
		Ret_Nao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mm5.setSelected(true);
			}
		});
		Ret_Nao.setHorizontalAlignment(SwingConstants.CENTER);
		Ret_Nao.setBounds(616, 291, 192, 21);
		contentPane.add(Ret_Nao);
		
		mm1 = new JRadioButton("1,0 mm");
		mm1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Ret_Sim.setSelected(true);
			}
		});
		mm1.setFont(new Font("Arial", Font.PLAIN, 11));
		mm1.setSelected(true);
		mm1.setHorizontalAlignment(SwingConstants.CENTER);
		mm1.setBounds(10, 291, 65, 21);
		contentPane.add(mm1);
		
		mm15 = new JRadioButton("1,5 mm");
		mm15.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Ret_Sim.setSelected(true);
			}
		});
		mm15.setFont(new Font("Arial", Font.PLAIN, 11));
		mm15.setHorizontalAlignment(SwingConstants.CENTER);
		mm15.setBounds(75, 291, 65, 21);
		contentPane.add(mm15);
		
		mm2 = new JRadioButton("2,0 mm");
		mm2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Ret_Sim.setSelected(true);
			}
		});
		mm2.setFont(new Font("Arial", Font.PLAIN, 11));
		mm2.setHorizontalAlignment(SwingConstants.RIGHT);
		mm2.setBounds(140, 291, 65, 21);
		contentPane.add(mm2);
		
		mm3 = new JRadioButton("3,0 mm");
		mm3.setFont(new Font("Arial", Font.PLAIN, 11));
		mm3.setHorizontalAlignment(SwingConstants.RIGHT);
		mm3.setBounds(205, 291, 65, 21);
		contentPane.add(mm3);
		
		mm4 = new JRadioButton("4,0 mm");
		mm4.setFont(new Font("Arial", Font.PLAIN, 11));
		mm4.setHorizontalAlignment(SwingConstants.RIGHT);
		mm4.setBounds(270, 291, 65, 21);
		contentPane.add(mm4);
		
		mm5 = new JRadioButton("5,0 mm");
		mm5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Ret_Nao.setSelected(true);
			}
		});
		mm5.setFont(new Font("Arial", Font.PLAIN, 11));
		mm5.setHorizontalAlignment(SwingConstants.RIGHT);
		mm5.setBounds(335, 291, 65, 21);
		contentPane.add(mm5);
		
		Tamanho = new JLabel("TAMANHO");
		Tamanho.setHorizontalAlignment(SwingConstants.CENTER);
		Tamanho.setFont(new Font("Arial", Font.BOLD, 16));
		Tamanho.setBounds(10, 171, 798, 21);
		contentPane.add(Tamanho);
		
		ButtonGroup BTRejunte = new ButtonGroup();
		BTRejunte.add(mm1);
		BTRejunte.add(mm2);
		BTRejunte.add(mm3);
		BTRejunte.add(mm15);
		BTRejunte.add(mm4);
		BTRejunte.add(mm5);
		
		ButtonGroup BTRetif = new ButtonGroup();
		BTRetif.add(Ret_Sim);
		BTRetif.add(Ret_Nao);
		
		ButtonGroup BTPEI = new ButtonGroup();
		BTPEI.add(LA);
		BTPEI.add(LB);
		BTPEI.add(LC);
		BTPEI.add(LD);
		BTPEI.add(LE);
	}
	
	public void apaga_dados() {
		Nome_Piso.setText("");
		Cod_Asso.setText("");
		Cod_Loja.setText("");
		Largura.setText("");
		Altura.setText("");
		Peca_CX.setText("");
		M_CX.setText("");
		Site_Piso.setText("");
		Foto_Piso.setText("");
		
		Bruto.setText("");
		Lucro.setText("90");
		Desc.setText("12");
		Valor_Desc.setText("");

		// Restaura os textos de placeholder
		Local_Uso.setText("Ex.: Garagem, piscina, parede, externo, interno...");
		Local_Uso.setForeground(Color.GRAY);
		Tipo_Piso.setText("Ex.: Acetinado, Polído...");
		Tipo_Piso.setForeground(Color.GRAY);

		// Redefine a seleção padrão dos RadioButtons
		LA.setSelected(true);
		mm2.setSelected(true);
		Ret_Sim.setSelected(true);
	}

	public void cadastra_piso() {
		String nome = Nome_Piso.getText();
		String cod_asso = Cod_Asso.getText();
		String cod_ctc = Cod_Loja.getText();
		String largura = Largura.getText().replace(",", ".");
		String altura = Altura.getText().replace(",", ".");
		String m_cx = M_CX.getText().replace(",", ".");
		String local_uso = Local_Uso.getText();
		String tipo_piso = Tipo_Piso.getText();
		String site = Site_Piso.getText();
		String foto = Foto_Piso.getText();
		String ambiente = ""; // Campo não utilizado na interface atual

		// Captura dos RadioButtons
		String rejunte = "";
		if (mm1.isSelected()) rejunte = "1";
		else if (mm15.isSelected()) rejunte = "1.5";
		else if (mm2.isSelected()) rejunte = "2";
		else if (mm3.isSelected()) rejunte = "3";
		else if (mm4.isSelected()) rejunte = "4";
		else if (mm5.isSelected()) rejunte = "5";

		String pei = "";
		if (LA.isSelected()) pei = "LA";
		else if (LB.isSelected()) pei = "LA, LB";
		else if (LC.isSelected()) pei = "LA, LB, LC";
		else if (LD.isSelected()) pei = "LA, LB, LC, LD";
		else if (LE.isSelected()) pei = "LA, LB, LC, LD, LE";

		String retificado = Ret_Sim.isSelected() ? "Sim, Retificado" : "Não, Bold";

		// Bloco try-with-resources para garantir que a conexão seja fechada
		try (Connection conexao = DriverManager.getConnection(Procura.CaminhoBD + "", "root", "");
			 Statement stm = conexao.createStatement()) {
			
			Class.forName("com.mysql.jdbc.Driver");
			
			double alt = Double.parseDouble(altura);
			double larg = Double.parseDouble(largura);
			double metros_caixa = Double.parseDouble(m_cx);
			double result = metros_caixa / ((alt / 100) * (larg / 100));

			String peca_caixas = new DecimalFormat("#0").format(result);
			
			String sql = "INSERT INTO piso VALUES ('" + nome + "','" + cod_asso + "','" + cod_ctc + "','" + largura + "','" + altura + "','" + rejunte
					+ "','" + peca_caixas + "','" + m_cx + "','" + local_uso + "','" + tipo_piso + "','" + pei + "','" + retificado + "','" + site + "','" + foto + "','" + ambiente + "')";
			
			stm.executeUpdate(sql);

			MostraInfosPiso();
			
			apaga_dados();
			Nome_Piso.requestFocus();

		} catch (ClassNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "Driver não está na biblioteca!");
		} catch (SQLException | NumberFormatException e2) {
			JOptionPane.showMessageDialog(null, "Existe erro(s) em algum campo! Prováveis erros: \n- Algum campo digitado está com um caracter especial (Aspas ou apóstrofo por exemplo), verifique e retire para que o cadastro seja completo!\n- Piso já cadastrado no sistema.\n- O campo Largura, Altura ou M² por  está branco, necessário preencher!");
		}
	}
		
	public void copiar() {
		String cod_cop = Piso_P_Copiar.getText().trim();

		if (cod_cop.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Por favor, insira um código para copiar.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}

		apaga_dados(); 
		Cod_Asso.setText(cod_cop);

		try (Connection conexao = DriverManager.getConnection(Procura.CaminhoBD + "", "root", "");
			 Statement stm = conexao.createStatement();
			 ResultSet rs = stm.executeQuery("SELECT * FROM piso WHERE cod_asso = '" + cod_cop + "' OR cod_ctc ='" + cod_cop + "'")) {

			Class.forName("com.mysql.jdbc.Driver");
			
			if (rs.next()) {
				System.out.println("Produto encontrado no banco de dados local. Preenchendo campos...");
				Largura.setText(rs.getString("largura"));
				Altura.setText(rs.getString("altura"));
				M_CX.setText(rs.getString("m_cx"));
				Peca_CX.setText(rs.getString("peca_m"));
				Site_Piso.setText(rs.getString("site"));
				Foto_Piso.setText(rs.getString("foto"));
				
				Local_Uso.setText(rs.getString("local_uso"));
				Local_Uso.setForeground(Color.BLACK);
				Tipo_Piso.setText(rs.getString("tipo_piso"));
				Tipo_Piso.setForeground(Color.BLACK);

				String pei = rs.getString("pei");
				if (pei != null) {
					if (pei.equals("LA")) LA.setSelected(true);
					else if (pei.equals("LA, LB")) LB.setSelected(true);
					else if (pei.equals("LA, LB, LC")) LC.setSelected(true);
					else if (pei.equals("LA, LB, LC, LD")) LD.setSelected(true);
					else if (pei.equals("LA, LB, LC, LD, LE")) LE.setSelected(true);
				}

				String rej = rs.getString("rejunte");
				if (rej != null) {
					if (rej.equals("1")) mm1.setSelected(true);
					else if (rej.equals("1.5")) mm15.setSelected(true);
					else if (rej.equals("2")) mm2.setSelected(true);
					else if (rej.equals("3")) mm3.setSelected(true);
					else if (rej.equals("4")) mm4.setSelected(true);
					else if (rej.equals("5")) mm5.setSelected(true);
				}

				String retif = rs.getString("retificado");
				if (retif != null) {
					if (retif.contains("Sim")) Ret_Sim.setSelected(true);
					else if (retif.contains("Não")) Ret_Nao.setSelected(true);
				}
			} else {
				//System.out.println("Produto não encontrado no banco de dados local. Prosseguindo para busca online.");
			}
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Driver do banco de dados não foi encontrado!", "Erro de Driver", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Erro ao conectar ou consultar o banco de dados: " + e.getMessage(), "Erro de SQL", JOptionPane.ERROR_MESSAGE);
		}

		try {
			int resultado = EstoqueScraper.buscarEstoque(cod_cop);
			if (resultado == 1) {
				Nome_Piso.setText(EstoqueScraper.nome);
				Bruto.setText(EstoqueScraper.valor);
				M_CX.setText(EstoqueScraper.multiplo);
				//JOptionPane.showMessageDialog(null, ""+EstoqueScraper.status);
				
				//System.out.println("Informações do site encontradas e preenchidas.");
				calcularValorFinal();
			} else {
				JOptionPane.showMessageDialog(this, "Não foi possível buscar nome e valor do site para o código informado.", "Busca Online", JOptionPane.WARNING_MESSAGE);
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "Erro de conexão ao buscar informações do site: " + ex.getMessage(), "Erro de Rede", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void calcularValorFinal() {
		try {
			String brutoStr = Bruto.getText().replace(",", ".");
			String lucroStr = Lucro.getText().replace(",", ".");
			String descStr = Desc.getText().replace(",", ".");

			if (brutoStr.isEmpty() || lucroStr.isEmpty() || descStr.isEmpty()) {
				return; // Não calcula se algum campo estiver vazio
			}

			double bruto = Double.parseDouble(brutoStr);
			double lucro = Double.parseDouble(lucroStr);
			double desc = Double.parseDouble(descStr);
			
			double brutoComLucro = bruto * (1 + lucro / 100.0);
			double valorFinal = brutoComLucro * (1 - desc / 100.0);

			Valor_Desc.setText(new DecimalFormat("#,##0.00").format(valorFinal));
			
		} catch (NumberFormatException e) {
			Valor_Desc.setText("Erro");
			JOptionPane.showMessageDialog(this, "Por favor, insira valores numéricos válidos para Bruto, Lucro e Desconto.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void MostraInfosPiso() {
		
		try {
			int resultado = EstoqueScraper.buscarEstoque(Cod_Asso.getText());
			if (resultado == 1) {
				JOptionPane.showMessageDialog(null, "Piso cadastrado com sucesso!\n\n\nEstoque: "+EstoqueScraper.estoque+"m²\nStatus: "+EstoqueScraper.status);
				
				calcularValorFinal();
			} else {
				JOptionPane.showMessageDialog(this, "Não foi possível buscar nome e valor do site para o código informado.", "Busca Online", JOptionPane.WARNING_MESSAGE);
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "Erro de conexão ao buscar informações do site: " + ex.getMessage(), "Erro de Rede", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	
}