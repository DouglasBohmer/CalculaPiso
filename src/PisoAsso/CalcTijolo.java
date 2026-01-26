package PisoAsso;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.awt.event.ActionEvent;
import java.awt.Color;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CalcTijolo extends JFrame {

	private JPanel contentPane;
	private JTextField LB_Alt;
	private JTextField LB_Larg;
	private double larg, alt, result;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CalcTijolo frame = new CalcTijolo();
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
	public CalcTijolo() {
		
		
		setTitle("Calcula Tijolo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("\\\\USUARIO-PC\\arquivos compartilhados\\Calcula Piso\\Logo Casa dos Tubos 50_page-0001.jpg"));
		setBounds(100, 100, 450, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLocationRelativeTo(null);

		setContentPane(contentPane);
		contentPane.setLayout(null);
		NumberFormat formato = new DecimalFormat("#0");
		
		JLabel lblNewLabel = new JLabel("Calcula Tijolo");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 414, 38);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Tijolo 06 Furos 09 x 14 x 24,50");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Arial", Font.ITALIC, 12));
		lblNewLabel_1.setBounds(10, 60, 414, 19);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("42 peças/m² HORIZONTAL (Deitado)");
		lblNewLabel_1_1.setFont(new Font("Arial", Font.ITALIC, 12));
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setBounds(10, 283, 414, 19);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel LB_Result = new JLabel();
		LB_Result.setForeground(new Color(255, 0, 0));
		LB_Result.setBackground(new Color(255, 255, 255));
		LB_Result.setFont(new Font("Arial", Font.ITALIC, 16));
		LB_Result.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Result.setBounds(10, 200, 414, 48);
		contentPane.add(LB_Result);
		
		JLabel lblNewLabel_3 = new JLabel("ALTURA em metros");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setBounds(10, 91, 197, 14);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_3_1 = new JLabel("LARGURA em metros");
		lblNewLabel_3_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3_1.setBounds(227, 91, 197, 14);
		contentPane.add(lblNewLabel_3_1);
		
		LB_Alt = new JTextField();
		LB_Alt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				
				LB_Alt.setText("");
				
			}
		});
		LB_Alt.setText("1");
		LB_Alt.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Alt.setBounds(10, 109, 197, 20);
		contentPane.add(LB_Alt);
		LB_Alt.setColumns(10);
		
		LB_Larg = new JTextField();
		LB_Larg.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				
				LB_Larg.setText("");
				
			}
		});
		LB_Larg.setText("1");
		LB_Larg.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Larg.setColumns(10);
		LB_Larg.setBounds(227, 109, 197, 20);
		contentPane.add(LB_Larg);
				
		JRadioButton RB_Vert = new JRadioButton("Vertical (Em pé)");
		RB_Vert.setSelected(true);
		RB_Vert.setHorizontalAlignment(SwingConstants.CENTER);
		RB_Vert.setBounds(10, 136, 197, 23);
		contentPane.add(RB_Vert);
		
		JRadioButton RB_Horiz = new JRadioButton("Horizontal (Deitado)");
		RB_Horiz.setHorizontalAlignment(SwingConstants.CENTER);
		RB_Horiz.setBounds(227, 136, 197, 23);
		contentPane.add(RB_Horiz);
		
		

		JButton BT_Calc = new JButton("Calcular");
		getRootPane().setDefaultButton(BT_Calc);
		BT_Calc.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					
					BT_Calc.doClick();
				}else {
					BT_Calc.doClick();
				}
				
			}
		});
		BT_Calc.setMnemonic(KeyEvent.VK_S);
		
		BT_Calc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				larg =  Double.parseDouble(LB_Larg.getText().replace(",","."));
				alt =  Double.parseDouble(LB_Alt.getText().replace(",","."));
				
				if (RB_Vert.isSelected()){
					
					result = (larg * alt) * 27;
					
					double tijolo = Math.ceil(result);
					
					LB_Result.setText(formato.format(tijolo) + " tijolos pra "+ new DecimalFormat("#,##0.00").format(larg * alt)+"m² na VERTICAL (Em pé)");
					
				}else {
					
					result = (larg * alt) * 42;
					
					double tijolo = Math.ceil(result);
					
					LB_Result.setText(formato.format(tijolo) + " tijolos pra "+ new DecimalFormat("#,##0.00").format(larg * alt)+"m² na HORIZONTAL (Deitado)");
					
				}
			}
		});
		BT_Calc.setBounds(175, 166, 89, 23);
		contentPane.add(BT_Calc);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("27 peças/m² VERTICAL (Em pé)");
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1.setFont(new Font("Arial", Font.ITALIC, 12));
		lblNewLabel_1_1_1.setBounds(10, 259, 414, 19);
		contentPane.add(lblNewLabel_1_1_1);
		
		ButtonGroup GrupoRB = new javax.swing.ButtonGroup();
		GrupoRB.add(RB_Vert);
		GrupoRB.add(RB_Horiz);
		
		JButton BT_Voltar = new JButton("Voltar p/ Menu");
		BT_Voltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Menu Menu = new Menu();
				Menu.setLocationRelativeTo(null);
				Menu.setVisible(true);
			
				dispose();
				
			}
		});
		BT_Voltar.setBounds(150, 307, 151, 23);
		contentPane.add(BT_Voltar);
		
	}
}