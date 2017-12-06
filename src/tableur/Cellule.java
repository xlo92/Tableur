package tableur;

public class Cellule {

	private CellValeur<?> valeur;
	
	private String contenu;
	
	private String nom;
	
	public Cellule(String cellText) {
		String[] elem = cellText.split("---");
		nom = elem[0];
		contenu = elem[1];
		valeur = null;
	}
	
	public void affecterContenu(String data) {
		contenu = data;
	}
	
	public String getText() {
		return nom+"---"+contenu;
	}
	
	public void clear() {
		contenu = "";
		valeur = null;
	}
	
	public void affecterValeur(CellValeur<?> valeur) {
			this.valeur = valeur.copy();
	}
	
	public String getContenu() {
		return contenu;
	}
	
	public String getNom() {
		return nom;
	}
	
	public CellValeur<?> getValeur() {
		return valeur;
	}
}
