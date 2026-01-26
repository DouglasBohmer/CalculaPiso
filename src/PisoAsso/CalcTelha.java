package PisoAsso;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JRadioButton;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import java.awt.Point;
import java.awt.SystemColor;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

public class CalcTelha extends JFrame {
	
	private JPanel contentPane;
	private JTextField TF_Larg1;
	private JTextField TF_Comp1;
	private double larg1, comp1, result1, larg2, comp2, result2, total;
	private JTextField TF_Larg2;
	private JTextField TF_Comp2;
	
	private double Transp4SIDE = 0.05, Transp6SIDE = 0.10,Transp4UP = 0.20, Transp6UP = 0.20, BeralVert = 0.80, BeralLat = 0.80;
	private double A4 = 2.44, Larg4m = 0.50;
	private double A6 = 1.53, B6 = 1.83, C6 = 2.13, D6 = 2.44, E6 = 3.04, Larg6m = 1.10;
	private double TA4, TA6, TB6, TC6, TD6, TE6;
	
	
	//Telha 6mm - A 1.53, B 1.83, C 2.13, D 2.44, E 3.05
	//Telha 4mm - A 2.44
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CalcTelha frame = new CalcTelha();
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
	public CalcTelha() {
		setTitle("Calcula Telha");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("\\\\USUARIO-PC\\arquivos compartilhados\\Calcula Piso\\Logo Casa dos Tubos 50_page-0001.jpg"));
		setBounds(100, 100, 573, 647);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLocationRelativeTo(null);

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton BT_Voltar = new JButton("Voltar p/ Menu");
		BT_Voltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Menu Menu = new Menu();
				Menu.setLocationRelativeTo(null);
				Menu.setVisible(true);
			
				dispose();
				
			}
		});
		BT_Voltar.setBounds(207, 574, 151, 23);
		contentPane.add(BT_Voltar);
		
		JLabel lblNewLabel = new JLabel("Calcula Telha");
		lblNewLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 534, 34);
		contentPane.add(lblNewLabel);
		
		TF_Larg1 = new JTextField();
		TF_Larg1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				
				TF_Larg1.setText("");
				
			}
			@Override
			public void focusLost(FocusEvent e) {
				
				String valida_larg1="";
				
				if (valida_larg1.equals(TF_Larg1.getText())) {
					TF_Larg1.setText("1");
				}
				
			}
		});
		TF_Larg1.setText("1");
		TF_Larg1.setHorizontalAlignment(SwingConstants.CENTER);
		TF_Larg1.setBounds(10, 75, 259, 20);
		contentPane.add(TF_Larg1);
		TF_Larg1.setColumns(10);
		
		TF_Comp1 = new JTextField();
		TF_Comp1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				
				TF_Comp1.setText("");
				
			}
			@Override
			public void focusLost(FocusEvent e) {
				
				String valida_comp1="";
				
				if (valida_comp1.equals(TF_Comp1.getText())) {
					TF_Comp1.setText("3,68");
				}
				
			}
		});
		TF_Comp1.setText("3,68");
		TF_Comp1.setHorizontalAlignment(SwingConstants.CENTER);
		TF_Comp1.setColumns(10);
		TF_Comp1.setBounds(285, 75, 259, 20);
		contentPane.add(TF_Comp1);
		
		JLabel lblNewLabel_1 = new JLabel("Largura do Telhado 1 (em mts)");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(10, 56, 259, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Comprimento do Telhado 1 (em mts)");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setBounds(285, 56, 259, 14);
		contentPane.add(lblNewLabel_1_1);
		
		JRadioButton RB_4mm = new JRadioButton("Telha 4mm");
		RB_4mm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				

				
			}
		});
		RB_4mm.setSelected(true);
		RB_4mm.setHorizontalAlignment(SwingConstants.CENTER);
		RB_4mm.setBounds(10, 160, 176, 20);
		contentPane.add(RB_4mm);
		
		JRadioButton RB_6mm = new JRadioButton("Telha 6mm");
		RB_6mm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		RB_6mm.setHorizontalAlignment(SwingConstants.CENTER);
		RB_6mm.setBounds(188, 160, 176, 20);
		contentPane.add(RB_6mm);
		
		TF_Larg2 = new JTextField();
		TF_Larg2.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				
				TF_Larg2.setText("");
				
			}
			@Override
			public void focusLost(FocusEvent e) {
				
				String valida_larg2="";
				
				if (valida_larg2.equals(TF_Larg2.getText())) {
					TF_Larg2.setText("0");
				}
				
			}
		});
		TF_Larg2.setText("0");
		TF_Larg2.setHorizontalAlignment(SwingConstants.CENTER);
		TF_Larg2.setColumns(10);
		TF_Larg2.setBounds(10, 133, 259, 20);
		contentPane.add(TF_Larg2);
		
		TF_Comp2 = new JTextField();
		TF_Comp2.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				
				TF_Comp2.setText("");
				
			}
			@Override
			public void focusLost(FocusEvent e) {
				
				String valida_comp2="";
				
				if (valida_comp2.equals(TF_Comp2.getText())) {
					TF_Comp2.setText("0");
				}
				
			}
		});
		TF_Comp2.setText("0");
		TF_Comp2.setHorizontalAlignment(SwingConstants.CENTER);
		TF_Comp2.setColumns(10);
		TF_Comp2.setBounds(285, 133, 259, 20);
		contentPane.add(TF_Comp2);
		
		JLabel lblNewLabel_1_2 = new JLabel("Largura do Telhado 2, se tiver (em mts)");
		lblNewLabel_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2.setBounds(10, 106, 259, 14);
		contentPane.add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Comprimento do Telhado 2, se tiver (em mts)");
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1.setBounds(285, 106, 259, 14);
		contentPane.add(lblNewLabel_1_1_1);
				
		JRadioButton RB_Ambas = new JRadioButton("4mm e 6mm");
		RB_Ambas.setHorizontalAlignment(SwingConstants.CENTER);
		RB_Ambas.setBounds(368, 160, 176, 20);
		contentPane.add(RB_Ambas);

		ButtonGroup GrupoBT = new ButtonGroup();
		GrupoBT.add(RB_6mm);
		GrupoBT.add(RB_4mm);
		GrupoBT.add(RB_Ambas);	
		
		JTextArea TA_Result1 = new JTextArea();
		TA_Result1.setRows(10);
		TA_Result1.setLocation(new Point(50, 15));
		TA_Result1.setToolTipText("");
		TA_Result1.setForeground(new Color(255, 0, 0));
		TA_Result1.setText("A QUANTIDADE DE TELHAS IRÁ APARECER AQUI MAGICAMENTE!!!");
		TA_Result1.setBackground(new Color(240, 240, 240));
		TA_Result1.setEditable(false);
		TA_Result1.setLineWrap(true);
		TA_Result1.setWrapStyleWord(true);
		TA_Result1.setBounds(10, 329, 259, 200);	
		contentPane.add(TA_Result1);
		
		JTextArea TA_Result2 = new JTextArea();
		TA_Result2.setWrapStyleWord(true);
		TA_Result2.setToolTipText("");
		TA_Result2.setText("A QUANTIDADE DE TELHAS IRÁ APARECER AQUI MAGICAMENTE!!!");
		TA_Result2.setRows(10);
		TA_Result2.setLocation(new Point(50, 15));
		TA_Result2.setLineWrap(true);
		TA_Result2.setEditable(false);
		TA_Result2.setForeground(Color.RED);
		TA_Result2.setBackground(SystemColor.menu);
		TA_Result2.setBounds(285, 329, 259, 200);
		contentPane.add(TA_Result2);
		
		JLabel LB_Tamanho1 = new JLabel("X m²");
		LB_Tamanho1.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Tamanho1.setForeground(Color.RED);
		LB_Tamanho1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_Tamanho1.setBounds(285, 223, 259, 25);
		contentPane.add(LB_Tamanho1);
		
		JLabel LB_Tamanho2 = new JLabel("X m²");
		LB_Tamanho2.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Tamanho2.setForeground(Color.RED);
		LB_Tamanho2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_Tamanho2.setBounds(285, 259, 259, 25);
		contentPane.add(LB_Tamanho2);
		
		JLabel LB_Tamanho3 = new JLabel("X Telhas");
		LB_Tamanho3.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Tamanho3.setForeground(Color.RED);
		LB_Tamanho3.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_Tamanho3.setBounds(285, 295, 259, 25);
		contentPane.add(LB_Tamanho3);
		
		JLabel LB_Tamanho = new JLabel("QUANTIDADE DA TELHA");
		LB_Tamanho.setForeground(new Color(255, 0, 0));
		LB_Tamanho.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_Tamanho.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Tamanho.setBounds(10, 187, 534, 25);
		contentPane.add(LB_Tamanho);
	
		JLabel LB_Telhado1 = new JLabel("M² Telhado 1");
		LB_Telhado1.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Telhado1.setForeground(Color.RED);
		LB_Telhado1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_Telhado1.setBounds(10, 223, 259, 25);
		contentPane.add(LB_Telhado1);
		
		JLabel LB_Telhado2 = new JLabel("M² Telhado 2");
		LB_Telhado2.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Telhado2.setForeground(Color.RED);
		LB_Telhado2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_Telhado2.setBounds(10, 259, 259, 25);
		contentPane.add(LB_Telhado2);
				
		JLabel LB_Telhado2_1 = new JLabel("M² Total do Telhado");
		LB_Telhado2_1.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Telhado2_1.setForeground(Color.RED);
		LB_Telhado2_1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_Telhado2_1.setBounds(10, 295, 259, 25);
		contentPane.add(LB_Telhado2_1);
			
		
		
		
		
		
		/*					
							
		JLabel LB_Telhado2_1_1 = new JLabel("Total 4mm 2,44mts");
		LB_Telhado2_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Telhado2_1_1.setForeground(Color.RED);
		LB_Telhado2_1_1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_Telhado2_1_1.setBounds(10, 331, 259, 25);
		contentPane.add(LB_Telhado2_1_1);
		
		JLabel LB_Telhado2_1_1_1 = new JLabel("Total 6mm 1,53mts");
		LB_Telhado2_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Telhado2_1_1_1.setForeground(Color.RED);
		LB_Telhado2_1_1_1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_Telhado2_1_1_1.setBounds(10, 367, 259, 25);
		contentPane.add(LB_Telhado2_1_1_1);
		
		JLabel LB_Telhado2_1_1_1_1 = new JLabel("Total 6mm 1,83mts");
		LB_Telhado2_1_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Telhado2_1_1_1_1.setForeground(Color.RED);
		LB_Telhado2_1_1_1_1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_Telhado2_1_1_1_1.setBounds(10, 403, 259, 25);
		contentPane.add(LB_Telhado2_1_1_1_1);
		
		JLabel LB_Telhado2_1_1_1_2 = new JLabel("Total 6mm 2,13mts");
		LB_Telhado2_1_1_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Telhado2_1_1_1_2.setForeground(Color.RED);
		LB_Telhado2_1_1_1_2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_Telhado2_1_1_1_2.setBounds(10, 439, 259, 25);
		contentPane.add(LB_Telhado2_1_1_1_2);
		
		JLabel LB_Telhado2_1_1_1_2_1 = new JLabel("Total 6mm 2,44mts");
		LB_Telhado2_1_1_1_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Telhado2_1_1_1_2_1.setForeground(Color.RED);
		LB_Telhado2_1_1_1_2_1.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_Telhado2_1_1_1_2_1.setBounds(10, 473, 259, 25);
		contentPane.add(LB_Telhado2_1_1_1_2_1);	
		
		
		JLabel LB_Telhado2_1_1_1_2_2 = new JLabel("Total 6mm 3,05mts");
		LB_Telhado2_1_1_1_2_2.setHorizontalAlignment(SwingConstants.CENTER);
		LB_Telhado2_1_1_1_2_2.setForeground(Color.RED);
		LB_Telhado2_1_1_1_2_2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_Telhado2_1_1_1_2_2.setBounds(10, 504, 259, 25);
		contentPane.add(LB_Telhado2_1_1_1_2_2);
		
		JLabel LB_TamanhoA4 = new JLabel("X Telhas");
		LB_TamanhoA4.setHorizontalAlignment(SwingConstants.CENTER);
		LB_TamanhoA4.setForeground(Color.RED);
		LB_TamanhoA4.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_TamanhoA4.setBounds(285, 331, 259, 25);
		contentPane.add(LB_TamanhoA4);
		
		JLabel LB_TamanhoA6 = new JLabel("X Telhas");
		LB_TamanhoA6.setHorizontalAlignment(SwingConstants.CENTER);
		LB_TamanhoA6.setForeground(Color.RED);
		LB_TamanhoA6.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_TamanhoA6.setBounds(285, 367, 259, 25);
		contentPane.add(LB_TamanhoA6);
		
		JLabel LB_TamanhoB6 = new JLabel("X Telhas");
		LB_TamanhoB6.setHorizontalAlignment(SwingConstants.CENTER);
		LB_TamanhoB6.setForeground(Color.RED);
		LB_TamanhoB6.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_TamanhoB6.setBounds(285, 403, 259, 25);
		contentPane.add(LB_TamanhoB6);
		
		JLabel LB_TamanhoC6 = new JLabel("X Telhas");
		LB_TamanhoC6.setHorizontalAlignment(SwingConstants.CENTER);
		LB_TamanhoC6.setForeground(Color.RED);
		LB_TamanhoC6.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_TamanhoC6.setBounds(285, 439, 259, 25);
		contentPane.add(LB_TamanhoC6);
		
		JLabel LB_TamanhoD6 = new JLabel("X Telhas");
		LB_TamanhoD6.setHorizontalAlignment(SwingConstants.CENTER);
		LB_TamanhoD6.setForeground(Color.RED);
		LB_TamanhoD6.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_TamanhoD6.setBounds(285, 473, 259, 25);
		contentPane.add(LB_TamanhoD6);
		
		JLabel LB_TamanhoE6 = new JLabel("X Telhas");
		LB_TamanhoE6.setHorizontalAlignment(SwingConstants.CENTER);
		LB_TamanhoE6.setForeground(Color.RED);
		LB_TamanhoE6.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
		LB_TamanhoE6.setBounds(285, 504, 259, 25);
		contentPane.add(LB_TamanhoE6);
		
		*/
		
		
		
		//BOTÃO
		
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
				
				larg1 =  Double.parseDouble(TF_Larg1.getText().replace(",","."));
				comp1 =  Double.parseDouble(TF_Comp1.getText().replace(",","."));
				larg2 =  Double.parseDouble(TF_Larg2.getText().replace(",","."));
				comp2 =  Double.parseDouble(TF_Comp2.getText().replace(",","."));
				
				result1 = larg1 * comp1;
				result2 = larg2 * comp2;
				
				Double TelhaVert1, TelhasHor1, TelhaVert2, TelhasHor2;
								
				total = result1 + result2;
				
				LB_Tamanho.setText("QUANTIDADE DE TELHAS 4MM E 6MM");
				LB_Tamanho1.setText(new DecimalFormat("#,##0.00").format(result1) +"m²");
				LB_Tamanho2.setText(new DecimalFormat("#,##0.00").format(result2) +"m²");
				LB_Tamanho3.setText(new DecimalFormat("#,##0.00").format(total) +"m²");
								
				if (RB_4mm.isSelected()) {
					
					TelhaVert1 = (comp1 + BeralVert) / (A4 - Transp4UP);
					TelhasHor1 = (larg1 + BeralLat) / (Larg4m - Transp4SIDE);
					
					if (result2 != 0) {
						TelhaVert2 = (comp2 + BeralVert) / (A4 - Transp4UP);
						TelhasHor2 = (larg2 + BeralLat) / (Larg4m - Transp4SIDE);
					}else {
						TelhaVert2 = 0.0;
						TelhasHor2 = 0.0;
					}
					
					TA4 = (TelhaVert1 * TelhasHor1) + (TelhaVert2 * TelhasHor2);
					
					LB_Tamanho.setText("QUANTIDADE DE TELHAS 4MM");
					LB_Tamanho1.setText(new DecimalFormat("#,##0.00").format(result1) +"m²");
					LB_Tamanho2.setText(new DecimalFormat("#,##0.00").format(result2) +"m²");
					LB_Tamanho3.setText(new DecimalFormat("#,##0.00").format(total) +"m²");
					
					TA_Result1.setText("\nComprimento + Beral Baixo Telhado 1: "+new DecimalFormat("#,##0.00").format(comp1 + BeralVert)+
							"\nLargura + Beral Lado Telhado 1: "+new DecimalFormat("#,##0.00").format(larg1 + BeralLat)+
							"\n\nComprimento + Beral Baixo Telhado 2: "+new DecimalFormat("#,##0.00").format(comp2 + BeralVert)+
							"\nLargura + Beral Lado Telhado 2: "+new DecimalFormat("#,##0.00").format(larg2 + BeralLat)+
							"\n\nComprimento útil da Telha 4mm: "+new DecimalFormat("#,##0.00").format(A4 - Transp4UP)+
							"\nLargura útil da Telha 4mm: "+new DecimalFormat("#,##0.00").format(Larg4m - Transp4SIDE));		
					
					TA_Result2.setText("\nTotal de Telhas Verticais Telhado 1: "+new DecimalFormat("#,##0.00").format(TelhaVert1)+
							"\nTotal de Telhas Horizontais Telhado 1: "+new DecimalFormat("#,##0.00").format(TelhasHor1)+
							"\n\nTotal de Telhas Verticais Telhado 2: "+new DecimalFormat("#,##0.00").format(TelhaVert2)+
							"\nTotal de Telhas Horizontais Telhado 2: "+new DecimalFormat("#,##0.00").format(TelhasHor2)+
							"\n\nTotal de Telhas: "+new DecimalFormat("#,##0.00").format(TA4));
					
			
				}else if (RB_6mm.isSelected()) {
					
					LB_Tamanho.setText("QUANTIDADE DE TELHAS 6MM");
					LB_Tamanho1.setText(new DecimalFormat("#,##0.00").format(result1) +"m²");
					LB_Tamanho2.setText(new DecimalFormat("#,##0.00").format(result2) +"m²");
					LB_Tamanho3.setText(new DecimalFormat("#,##0.00").format(total) +"m²");
					
				}else if(RB_Ambas.isSelected()) {
					
					LB_Tamanho.setText("QUANTIDADE DE TELHAS 4MM E 6MM");
					LB_Tamanho1.setText(new DecimalFormat("#,##0.00").format(result1) +"m²");
					LB_Tamanho2.setText(new DecimalFormat("#,##0.00").format(result2) +"m²");
					LB_Tamanho3.setText(new DecimalFormat("#,##0.00").format(total) +"m²");
					
				}
				
				
			}
		});
		BT_Calc.setBounds(236, 540, 89, 23);
		contentPane.add(BT_Calc);
		
		
		
		
		
	}
}
