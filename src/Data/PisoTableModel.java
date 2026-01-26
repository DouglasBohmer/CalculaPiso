package Data;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class PisoTableModel extends AbstractTableModel{

	private List<Piso> dados = new ArrayList<>();
	private String [] colunas = {"Nome", "Cód Asso", "Cód Loja","Largura", "Altura", "Rejunte", "Peças/CX", "M²/CX","Local", "Tipo", "PEI","Retificado"};
	
	
	public String getColunmName (int colunm) {
		return colunas[colunm];
	}
	
	
	@Override
	public int getRowCount() {
		return dados.size();
	}

	@Override
	public int getColumnCount() {
		return colunas.length;
	}

	@Override
	public Object getValueAt(int linha, int coluna) {
		
		switch(coluna) {
			/*case 0:
				return dados.get(linha).getNome();
			case 1:
				return dados.get(linha).getCod_asso();
			case 2:	
				return dados.get(linha).getCod_ctc();
			case 3:
				return dados.get(linha).getLargura();
			case 4:
				return dados.get(linha).getAltura();
			case 5:	
				return dados.get(linha).getRejunte();	
			case 6:
				return dados.get(linha).getPeca_cx();
			case 7:
				return dados.get(linha).getM_cx();
			case 8:	
				return dados.get(linha).getLocal_uso();
			case 9:
				return dados.get(linha).getTipo_piso();
			case 10:
				return dados.get(linha).getPei();
			case 11:
				return dados.get(linha).getRetificado();
			case 12:	
				return dados.get(linha);*/
			case 0:
				return "teste";
			case 1:
				return "teste";
			case 2:	
				return "teste";
			case 3:
				return "teste";
			case 4:
				return "teste";
			case 5:	
				return "teste";	
			case 6:
				return "teste";
			case 7:
				return "teste";
			case 8:	
				return "teste";
			case 9:
				return "teste";
			case 10:
				return "teste";
			case 11:
				return "teste";
			case 12:	
				return "teste";
			
		}
		return null;
	}

}
