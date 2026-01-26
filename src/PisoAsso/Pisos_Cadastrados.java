package PisoAsso;

import java.awt.Color;
import java.awt.EventQueue;

//Conexão banco de dados;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class Pisos_Cadastrados extends JFrame {

	private JPanel contentPane;
	private JTextField Nome_Piso;
	private JTextField Cod_Asso;
	private JTextField Cod_Loja;
	private JTextField Largura;
	private JTextField Altura;
	private JTextField Rejunte;
	private JTextField Peca_CX;
	private JTextField M_CX;
	private JTextField Local_Uso;
	private JTextField Site_Piso;
	private JTextField Foto_Piso;
	private JTextField Tipo_Piso;
	private JTextField PEI;
	private JTextField Retificado;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
				
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Pisos_Cadastrados frame = new Pisos_Cadastrados();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Pisos_Cadastrados() {
		setTitle("Piso Cadastrado");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Procura.CaminhoImagem+"\\Logo Casa dos Tubos 50_page-0001.jpg"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1048, 554);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton Volta_Tela_Inicial = new JButton("Voltar Tela Inicial");
		Volta_Tela_Inicial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Procura TelaInicial = new Procura();
				TelaInicial.setVisible(true);
				TelaInicial.setLocationRelativeTo(null);
				dispose();
				
			}
		});
		Volta_Tela_Inicial.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		Volta_Tela_Inicial.setBounds(10, 482, 220, 21);
		contentPane.add(Volta_Tela_Inicial);
		
		JButton Volta_Tela_Cadastro = new JButton("Voltar Tela Cadastro");
		Volta_Tela_Cadastro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
								
				Cadastro TelaProcura = new Cadastro();
				TelaProcura.setVisible(true);
				TelaProcura.setLocationRelativeTo(null);
				dispose();
				
			}
		});
		Volta_Tela_Cadastro.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		Volta_Tela_Cadastro.setBounds(240, 482, 210, 21);
		contentPane.add(Volta_Tela_Cadastro);
		
		JButton Apagar_Piso = new JButton("Apagar Piso");
		Apagar_Piso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				apagar_piso();		
				
				Cadastro TelaProcura = new Cadastro();
				TelaProcura.setVisible(true);
				TelaProcura.setLocationRelativeTo(null);
				dispose();
				
			}
		});
		Apagar_Piso.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		Apagar_Piso.setBounds(626, 482, 156, 21);
		contentPane.add(Apagar_Piso);
		
		JButton Alterar = new JButton("Alterar");
		Alterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Cod_Asso.setEditable(true);
				Nome_Piso.setEnabled(false);
				Cod_Loja.setEditable(true);
				Largura.setEditable(true);
				Altura.setEditable(true);
				Rejunte.setEditable(true);
				Peca_CX.setEditable(true);
				M_CX.setEditable(true);
				Local_Uso.setEditable(true);
				Site_Piso.setEditable(true);
				Foto_Piso.setEditable(true);
				//Foto_Ambiente.setEditable(true);
				Tipo_Piso.setEditable(true);
				PEI.setEditable(true);
				Retificado.setEditable(true);
								
			}
		});
		Alterar.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		Alterar.setBounds(792, 482, 104, 21);
		contentPane.add(Alterar);
		
		JButton Salvar = new JButton("Salvar");
		Salvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				salvar_alteracao();
								
				Altura.setText("");
				Cod_Asso.setText("");
				Cod_Loja.setText("");
				//Foto_Ambiente.setText("");
				Foto_Piso.setText("");
				Largura.setText("");
				Local_Uso.setText("");
				M_CX.setText("");
				Nome_Piso.setText("");
				Peca_CX.setText("");
				PEI.setText("");
				Rejunte.setText("");
				Site_Piso.setText("");
				Tipo_Piso.setText("");
				Retificado.setText("");
				
				Cod_Asso.setEnabled(true);
				Nome_Piso.setEditable(false);
				Cod_Loja.setEnabled(true);
				Largura.setEditable(false);
				Altura.setEditable(false);
				Rejunte.setEditable(false);
				Peca_CX.setEditable(false);
				M_CX.setEditable(false);
				Local_Uso.setEditable(false);
				Site_Piso.setEditable(false);
				Foto_Piso.setEditable(false);
				//Foto_Ambiente.setEditable(false);
				Tipo_Piso.setEditable(false);
				PEI.setEditable(false);
				Retificado.setEditable(false);
				
				Cod_Asso.requestFocus();
				
			}
		});
		Salvar.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		Salvar.setBounds(906, 482, 104, 21);
		contentPane.add(Salvar);
		
		//Código Copiado
		
		JLabel lblNewLabel = new JLabel("PISOS CADASTRADOS");
		lblNewLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(212, 10, 798, 27);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Nome do Piso");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(10, 47, 192, 21);
		contentPane.add(lblNewLabel_1);
			
		Nome_Piso = new JTextField();
		Nome_Piso.setEditable(false);
		Nome_Piso.setHorizontalAlignment(SwingConstants.CENTER);
		Nome_Piso.setFont(new Font("Arial", Font.PLAIN, 16));
		Nome_Piso.setBounds(212, 47, 798, 21);
		contentPane.add(Nome_Piso);
		Nome_Piso.setColumns(10);
		
		Cod_Asso = new JTextField();
		Cod_Asso.setHorizontalAlignment(SwingConstants.CENTER);
		Cod_Asso.setFont(new Font("Arial", Font.PLAIN, 16));
		Cod_Asso.setColumns(10);
		Cod_Asso.setBounds(212, 78, 798, 21);
		contentPane.add(Cod_Asso);
		
		Cod_Loja = new JTextField();
		Cod_Loja.setHorizontalAlignment(SwingConstants.CENTER);
		Cod_Loja.setFont(new Font("Arial", Font.PLAIN, 16));
		Cod_Loja.setColumns(10);
		Cod_Loja.setBounds(212, 109, 798, 21);
		contentPane.add(Cod_Loja);
		
		Largura = new JTextField();
		Largura.setEditable(false);
		Largura.setHorizontalAlignment(SwingConstants.CENTER);
		Largura.setFont(new Font("Arial", Font.PLAIN, 16));
		Largura.setColumns(10);
		Largura.setBounds(212, 140, 798, 21);
		contentPane.add(Largura);
		
		Altura = new JTextField();
		Altura.setEditable(false);
		Altura.setHorizontalAlignment(SwingConstants.CENTER);
		Altura.setFont(new Font("Arial", Font.PLAIN, 16));
		Altura.setColumns(10);
		Altura.setBounds(212, 171, 798, 21);
		contentPane.add(Altura);
		
		Rejunte = new JTextField();
		Rejunte.setEditable(false);
		Rejunte.setHorizontalAlignment(SwingConstants.CENTER);
		Rejunte.setFont(new Font("Arial", Font.PLAIN, 16));
		Rejunte.setColumns(10);
		Rejunte.setBounds(212, 202, 798, 21);
		contentPane.add(Rejunte);
		
		Peca_CX = new JTextField();
		Peca_CX.setEditable(false);
		Peca_CX.setHorizontalAlignment(SwingConstants.CENTER);
		Peca_CX.setFont(new Font("Arial", Font.PLAIN, 16));
		Peca_CX.setColumns(10);
		Peca_CX.setBounds(212, 233, 798, 21);
		contentPane.add(Peca_CX);
		
		M_CX = new JTextField();
		M_CX.setEditable(false);
		M_CX.setHorizontalAlignment(SwingConstants.CENTER);
		M_CX.setFont(new Font("Arial", Font.PLAIN, 16));
		M_CX.setColumns(10);
		M_CX.setBounds(212, 264, 798, 21);
		contentPane.add(M_CX);
		
		Local_Uso = new JTextField();
		Local_Uso.setEditable(false);
		Local_Uso.setForeground(new Color(0, 0, 0));
		Local_Uso.setHorizontalAlignment(SwingConstants.CENTER);
		Local_Uso.setFont(new Font("Arial", Font.PLAIN, 16));
		Local_Uso.setColumns(10);
		Local_Uso.setBounds(212, 295, 798, 21);
		contentPane.add(Local_Uso);
		
		JLabel lblNewLabel_1_1 = new JLabel("Código Asso");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1_1.setBounds(10, 78, 192, 21);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("Código Loja (Se tiver)");
		lblNewLabel_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1_2.setBounds(10, 109, 192, 21);
		contentPane.add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_3 = new JLabel("Largura (cm)");
		lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1_3.setBounds(10, 140, 192, 21);
		contentPane.add(lblNewLabel_1_3);
		
		JLabel lblNewLabel_1_4 = new JLabel("Altura (cm)");
		lblNewLabel_1_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_4.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1_4.setBounds(10, 171, 192, 21);
		contentPane.add(lblNewLabel_1_4);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Rejunte (mm)");
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1_1_1.setBounds(10, 202, 192, 21);
		contentPane.add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_1_2_1 = new JLabel("Peças p/ CX");
		lblNewLabel_1_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2_1.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1_2_1.setBounds(10, 233, 192, 21);
		contentPane.add(lblNewLabel_1_2_1);
		
		JLabel lblNewLabel_1_3_1 = new JLabel("M² p/  CX");
		lblNewLabel_1_3_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3_1.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1_3_1.setBounds(10, 264, 192, 21);
		contentPane.add(lblNewLabel_1_3_1);
		
		JLabel lblNewLabel_1_5 = new JLabel("Local de Uso");
		lblNewLabel_1_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_5.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1_5.setBounds(10, 295, 192, 21);
		contentPane.add(lblNewLabel_1_5);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("Tipo de Piso");
		lblNewLabel_1_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_2.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1_1_2.setBounds(10, 326, 192, 21);
		contentPane.add(lblNewLabel_1_1_2);
		
		JLabel lblNewLabel_1_2_2 = new JLabel("PEI");
		lblNewLabel_1_2_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2_2.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1_2_2.setBounds(10, 357, 192, 21);
		contentPane.add(lblNewLabel_1_2_2);
		
		JLabel lblNewLabel_1_3_2_1 = new JLabel("É Retificado?");
		lblNewLabel_1_3_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3_2_1.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1_3_2_1.setBounds(10, 388, 192, 21);
		contentPane.add(lblNewLabel_1_3_2_1);
		
		JLabel lblNewLabel_1_1_2_1 = new JLabel("Site Piso");
		lblNewLabel_1_1_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_2_1.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1_1_2_1.setBounds(10, 419, 192, 21);
		contentPane.add(lblNewLabel_1_1_2_1);
		
		Site_Piso = new JTextField();
		Site_Piso.setEditable(false);
		Site_Piso.setHorizontalAlignment(SwingConstants.CENTER);
		Site_Piso.setFont(new Font("Arial", Font.PLAIN, 16));
		Site_Piso.setColumns(10);
		Site_Piso.setBounds(212, 419, 798, 21);
		contentPane.add(Site_Piso);
		
		JLabel lblNewLabel_1_2_2_1 = new JLabel("Link Foto");
		lblNewLabel_1_2_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2_2_1.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1_2_2_1.setBounds(10, 450, 192, 21);
		contentPane.add(lblNewLabel_1_2_2_1);
		
		Foto_Piso = new JTextField();
		Foto_Piso.setEditable(false);
		Foto_Piso.setHorizontalAlignment(SwingConstants.CENTER);
		Foto_Piso.setFont(new Font("Arial", Font.PLAIN, 16));
		Foto_Piso.setColumns(10);
		Foto_Piso.setBounds(212, 450, 798, 21);
		contentPane.add(Foto_Piso);
		
		Tipo_Piso = new JTextField();
		Tipo_Piso.setEditable(false);
		Tipo_Piso.setForeground(new Color(0, 0, 0));
		Tipo_Piso.setHorizontalAlignment(SwingConstants.CENTER);
		Tipo_Piso.setFont(new Font("Arial", Font.PLAIN, 16));
		Tipo_Piso.setColumns(10);
		Tipo_Piso.setBounds(212, 326, 798, 21);
		contentPane.add(Tipo_Piso);
		
		PEI = new JTextField();
		PEI.setEditable(false);
		PEI.setHorizontalAlignment(SwingConstants.CENTER);
		PEI.setFont(new Font("Arial", Font.PLAIN, 16));
		PEI.setColumns(10);
		PEI.setBounds(212, 357, 798, 21);
		contentPane.add(PEI);
		
		Retificado = new JTextField();
		Retificado.setEditable(false);
		Retificado.setHorizontalAlignment(SwingConstants.CENTER);
		Retificado.setFont(new Font("Arial", Font.PLAIN, 16));
		Retificado.setColumns(10);
		Retificado.setBounds(212, 388, 798, 21);
		contentPane.add(Retificado);
		
		JButton Pesquisa = new JButton("Pesquisar");
		Pesquisa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				pesquisar();
				
			}
		});
		Pesquisa.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		Pesquisa.setBounds(460, 482, 156, 21);
		contentPane.add(Pesquisa);
		
	}
	
	public void pesquisar() {
		
		String cod_asso = Cod_Asso.getText();
		String cod_loja = Cod_Loja.getText();
				
		try {		
			
			Connection conexao;
			Statement stm;
			Class.forName("com.mysql.jdbc.Driver");
			
			//conexao = DriverManager.getConnection("jdbc:mysql://192.168.0.99:3306/calcula_piso","root","");
			conexao = DriverManager.getConnection(Procura.CaminhoBD+"","root","");
			
			stm = conexao.createStatement();
			
			if(cod_asso.equals("")) {
				
				stm.executeQuery("Select * from piso WHERE cod_ctc ='"+cod_loja+"'");
				
			}else {
				
				stm.executeQuery("Select * from piso WHERE cod_asso = '"+cod_asso+"'");
			}
					
			//stm.executeQuery("Select * from piso WHERE cod_asso = '"+cod_asso+"' OR cod_ctc ='"+cod_loja+"'");
			
			ResultSet rs = stm.getResultSet();
			
			while (rs.next()) {
				
				Altura.setText(rs.getString("altura"));
				Cod_Asso.setText(rs.getString("cod_asso"));
				Cod_Loja.setText(rs.getString("cod_ctc"));
				//Foto_Ambiente.setText(rs.getString("ambiente"));
				Foto_Piso.setText(rs.getString("foto"));
				Largura.setText(rs.getString("largura"));
				Local_Uso.setText(rs.getString("local_uso"));
				M_CX.setText(rs.getString("m_cx"));
				Nome_Piso.setText(rs.getString("nome"));
				Peca_CX.setText(rs.getString("pecas_cx"));
				PEI.setText(rs.getString("pei"));
				Rejunte.setText(rs.getString("rejunte"));
				Site_Piso.setText(rs.getString("site"));
				Tipo_Piso.setText(rs.getString("tipo_piso"));
				Retificado.setText(rs.getString("retificado"));
				
			}
						
			//JOptionPane.showMessageDialog(null, "Pesquisa realizada com sucesso!");
			
			//Cod_Asso.requestFocus();
			
		} catch (ClassNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "Driver não está na biblioteca!");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
			
		}
		
		
	}
	
	public void salvar_alteracao() {
		
		String nome = Nome_Piso.getText();
		String cod_asso = Cod_Asso.getText();
		String cod_ctc = Cod_Loja.getText();
		String largura = Largura.getText().replace("," , ".");
		String altura = Altura.getText().replace("," , ".");
		String rejunte = Rejunte.getText().replace("," , ".");
		String peca_m = Peca_CX.getText().replace("," , ".");
		String m_cx = M_CX.getText().replace("," , ".");
		String local_uso = Local_Uso.getText();
		String tipo_piso = Tipo_Piso.getText();
		String pei = PEI.getText();
		String retificado = Retificado.getText();
		String site = Site_Piso.getText();
		String foto = Foto_Piso.getText();
		//String ambiente = Foto_Ambiente.getText();
		
		try {		
			
			Connection conexao;
			Statement stm;
			Class.forName("com.mysql.jdbc.Driver");
			
			//conexao = DriverManager.getConnection("jdbc:mysql://192.168.0.99:3306/calcula_piso","root","");
			conexao = DriverManager.getConnection(Procura.CaminhoBD+"","root","");
			
			//http://192.168.0.99/phpmyadmin/index.php?route=/sql&pos=0&db=calcula_piso&table=piso Link BD Pisos
			
			stm = conexao.createStatement();
			
			/* if(cod_asso.equals("")) {
				
				stm.executeUpdate("UPDATE piso SET cod_asso = '"+cod_asso+"',cod_ctc = '"+cod_ctc+"',largura = '"+largura+"',altura = '"+altura+"',"
						+ "rejunte = '"+rejunte+"',pecas_cx	 = '"+peca_m+"', m_cx = '"+m_cx+"',local_uso = '"+local_uso+"',tipo_piso = '"+tipo_piso+"',pei = '"+pei+"',retificado = '"+retificado+"',site = '"+site+"',foto = '"+foto+"',ambiente =  '"+ambiente+"' "
										+ "WHERE nome ='"+nome+"'");
				
			}else {
				
				stm.executeUpdate("UPDATE piso SET cod_asso = '"+cod_asso+"',cod_ctc = '"+cod_ctc+"',largura = '"+largura+"',altura = '"+altura+"',"
						+ "rejunte = '"+rejunte+"',pecas_cx	 = '"+peca_m+"', m_cx = '"+m_cx+"',local_uso = '"+local_uso+"',tipo_piso = '"+tipo_piso+"',pei = '"+pei+"',retificado = '"+retificado+"',site = '"+site+"',foto = '"+foto+"',ambiente =  '"+ambiente+"' "
										+ "WHERE cod_asso = '"+cod_asso+"'");
				
			}
			
			 stm.executeUpdate("UPDATE piso SET cod_asso = '"+cod_asso+"',cod_ctc = '"+cod_ctc+"',largura = '"+largura+"',altura = '"+altura+"',"
					+ "rejunte = '"+rejunte+"',pecas_cx	 = '"+peca_m+"', m_cx = '"+m_cx+"',local_uso = '"+local_uso+"',tipo_piso = '"+tipo_piso+"',pei = '"+pei+"',retificado = '"+retificado+"',site = '"+site+"',foto = '"+foto+"',ambiente =  '"+ambiente+"' "
									+ "WHERE cod_asso = '"+cod_asso+"'"); */
			
			String ambiente ="";
			
			stm.executeUpdate("UPDATE piso SET cod_asso = '"+cod_asso+"',cod_ctc = '"+cod_ctc+"',largura = '"+largura+"',altura = '"+altura+"',"
					+ "rejunte = '"+rejunte+"',pecas_cx	 = '"+peca_m+"', m_cx = '"+m_cx+"',local_uso = '"+local_uso+"',tipo_piso = '"+tipo_piso+"',pei = '"+pei+"',retificado = '"+retificado+"',site = '"+site+"',foto = '"+foto+"',ambiente =  '"+ambiente+"' "
									+ "WHERE nome ='"+nome+"'");
			
			
			
			JOptionPane.showMessageDialog(null, "Alterações realizadas com sucesso!");
			
			Cod_Asso.requestFocus();
			
		} catch (ClassNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "Driver não está na biblioteca!");
		} catch (SQLException | NumberFormatException e2) {
			JOptionPane.showMessageDialog(null, "Existe erro (s) em algum campo! Prováveis erros: \n- Algum campo digitado está com um caracter especial (Aspas ou apóstrofo por exemplo), verifique e retire para que o cadastro seja completo!\n- Piso já cadastrado no sistema.\n- O campo Largura, Altura ou M² por Caixa está branco, necessário preencher!");
		}
		
	}
	
	public void apagar_piso() {
		
		String nome = Nome_Piso.getText();
		
		try {		
			
			Connection conexao;
			Statement stm;
			Class.forName("com.mysql.jdbc.Driver");
			
			//conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/calcula_piso","root","");
			//conexao = DriverManager.getConnection("jdbc:mysql://192.168.0.99:3306/calcula_piso","root","");
			conexao = DriverManager.getConnection(Procura.CaminhoBD+"","root","");
			
			stm = conexao.createStatement();
			stm.executeUpdate("DELETE from piso WHERE nome='"+nome+"';");
			
			JOptionPane.showMessageDialog(null, "Piso deletado com sucesso!");
			
			Cod_Asso.requestFocus();
			
		} catch (ClassNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "Driver não está na biblioteca!");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		}
			
	}
	
}
