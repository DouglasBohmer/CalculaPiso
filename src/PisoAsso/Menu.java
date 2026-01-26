package PisoAsso;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Menu extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
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
	public Menu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("\\\\USUARIO-PC\\arquivos compartilhados\\Calcula Piso\\Logo Casa dos Tubos 50_page-0001.jpg"));
		setBounds(100, 100, 450, 352);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLocationRelativeTo(null);
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton BT_Tijolo = new JButton("Tijolo");
		BT_Tijolo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				CalcTijolo Tijolo = new CalcTijolo();
				Tijolo.setLocationRelativeTo(null);
				Tijolo.setVisible(true);
			
				dispose();
				
			}
		});
		BT_Tijolo.setBounds(34, 149, 89, 23);
		contentPane.add(BT_Tijolo);
		
		JButton BT_Piso = new JButton("Piso");
		BT_Piso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Procura Piso = new Procura();
				Piso.setLocationRelativeTo(null);
				Piso.setVisible(true);
			
				dispose();
				
			}
		});
		BT_Piso.setBounds(34, 184, 89, 23);
		contentPane.add(BT_Piso);
		
		JButton BT_Telha = new JButton("Telha");
		BT_Telha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				CalcTelha Telha = new CalcTelha();
				Telha.setLocationRelativeTo(null);
				Telha.setVisible(true);
			
				dispose();
				
			}
		});
		BT_Telha.setBounds(34, 218, 89, 23);
		contentPane.add(BT_Telha);
		
		JButton BT_Forro = new JButton("Forro");
		BT_Forro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//CalcForro Forro = new CalcForro();
				//Forro.setLocationRelativeTo(null);
				//Forro.setVisible(true);
			
				dispose();
				
			}
		});
		BT_Forro.setBounds(34, 252, 89, 23);
		contentPane.add(BT_Forro);
		
	}

}
