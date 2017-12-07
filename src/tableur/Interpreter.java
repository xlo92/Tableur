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
		return false;
	}
	
	public void setCells(CellContainer cells) {
		this.cells = cells;
	}
}
