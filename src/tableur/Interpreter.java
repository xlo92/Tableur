package tableur;

public class Interpreter {
	
	private CellContainer cells;
	
	public Interpreter() {
		cells = null;
	}
	
	public Interpreter(CellContainer cells) {
		this.cells = cells;
	}
	
	private CellValeur evaluer_num(String contenuCellule) {
		Double d = Double.valueOf(contenuCellule);
		int i = d.intValue();
		if(d==i) return new CellInt(i);
		return new CellDouble(d);
	}
	
	private CellValeur evaluer_texte(String contenuCellule) {
		String res = "";
		Cellule c;
		for(int i = 0;i<contenuCellule.length();i++) {
			if(Character.isLetter(contenuCellule.charAt(i)) || contenuCellule.charAt(i) == '_' || Character.isDigit(contenuCellule.charAt(i))) {
				res +=contenuCellule.charAt(i);
			}else {
				break;
			}
		}
		if((c=cells.getCellule(res))!=null) {
			return c.getValeur();
		}else {
			return null;
		}
	}
	
	public CellValeur evaluer(String contenuCellule){
		if(Character.isDigit(contenuCellule.charAt(0))) {
			return evaluer_num(contenuCellule);
		}else {
			if(Character.isLetter(contenuCellule.charAt(0)) || contenuCellule.charAt(0) == '_') {
				return evaluer_texte(contenuCellule);
			}else {
				if(contenuCellule.charAt(0)=='$') {
					//TODO
				}
			}
		}
		return null;
	}
	
	public String transformer(String contenuCelluleB, String nomCelluleB, String nomCelluleC) {
		return "";
	}
	
	public boolean isDependante(String contenuCelluleB, String nomCelluleC) {
		if(cells!=null) {
			String[] res;
			Cellule c;
			boolean flag = true;
			while(flag) {
				res = contenuCelluleB.split("(?<=\\D)(?=\\d)");
				if(res.length==2) {
					c = cells.getCellule(res[0]+res[1]);
					if(c!=null) {
						if(c.getNom().getFullName().equals(nomCelluleC)) {
							return true;
						}else {
							if(isDependante(c.getContenu(),nomCelluleC)) {
								return true;
							}
						}
					}
					if(res[0].length()+res[1].length() < contenuCelluleB.length()) {
						contenuCelluleB = contenuCelluleB.substring(res[0].length()+res[1].length());
					}else {
						return false;
					}
				}else {
					flag=false;
				}
			}
		}
		return false;
	}
	
	public void setCells(CellContainer cells) {
		this.cells = cells;
	}
}
