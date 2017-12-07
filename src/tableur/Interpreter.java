package tableur;

public class Interpreter {
	
	private CellContainer cells;
	
	public Interpreter() {
		cells = null;
	}
	
	public Interpreter(CellContainer cells) {
		this.cells = cells;
	}
	
	public CellValeur evaluer(String contenuCellule){
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
