package PisoAsso;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Data.PisoTableModel;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TudoCadastrado extends JFrame {


	PisoTableModel TableModel = new PisoTableModel();
	
	private JPanel contentPane;
	private JTable tabela_piso;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TudoCadastrado frame = new TudoCadastrado();
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
	
	
	public TudoCadastrado() {
		
		tabela_piso.setModel(TableModel);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1095, 747);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("TODOS OS PISOS CADASTRADOS");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
		lblNewLabel.setBounds(20, 10, 1051, 21);
		contentPane.add(lblNewLabel);
		
		tabela_piso = new JTable();
		tabela_piso.setFont(new Font("Arial Black", Font.PLAIN, 12));
		tabela_piso.setBounds(20, 41, 1051, 623);
		contentPane.add(tabela_piso);
		
		JButton Botao_Voltar = new JButton("Voltar");
		Botao_Voltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Cadastro Cad = new Cadastro();
				Cad.setVisible(true);
				Cad.setLocationRelativeTo(null);
				dispose();
				
			}
		});
		Botao_Voltar.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		Botao_Voltar.setBounds(20, 679, 152, 21);
		contentPane.add(Botao_Voltar);
		
	}
}
