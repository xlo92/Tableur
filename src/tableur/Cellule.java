package tableur;

public class Cellule {

	private CellValeur valeur;
	
	private String contenu;
	
	private CellName nom;
	
	public Cellule(String cellText) {
            String[] elem = cellText.split("---");
            nom = new CellName(elem[0]);
            contenu = elem[1];
            valeur = null;
	}
	
	public void affecterContenu(String data) {
		contenu = data;
	}
	
	public String getText() {
		return nom.getFullName()+"---"+contenu;
	}
	
	public void clear() {
		contenu = "";
		valeur = null;
	}
	
	public void affecterValeur(CellValeur valeur) {
                this.valeur = valeur.copy();
	}
        
        public Cellule copy(){
            return new Cellule(getText());
        }
	
	public String getContenu() {
		return contenu;
	}
	
	public CellName getNom() {
		return nom;
	}
	
	public CellValeur getValeur() {
		if(valeur!=null) return valeur.copy();
		return null;
	}
}
