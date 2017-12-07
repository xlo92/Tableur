package tableur;

public class Interpreter {
	
	private CellContainer cells;
	
	private static Interpreter interpreter = new Interpreter();;
	
	private Interpreter() {}
	
	public static Interpreter getInstance() {
		return interpreter;
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
		this.cells = cells.copy();
	}
}
