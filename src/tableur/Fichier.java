package tableur;

public class Fichier {

	private String filename;
	
	public Fichier(String filename) {
		
	}
	
	public boolean makeBackUp() {
		return false;
	}
	
	public boolean ecrireFichier(CellContainer cells) {
		return false;
	}
	
	public boolean IsOnDisk(){
		return false;
	}
	
	private boolean ecrireCell(String cellText) {
		return false;
	}
	
	public CellContainer lireFichier() {
		return null;
	}
	
	public String lireCell() {
		return "";
	}
}
