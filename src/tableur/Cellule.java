package tableur;

public class Cellule {

	private CellValeur valeur;
	
	private String contenu;
	
	private CellName nom;
	
	public Cellule(String cellText) {
            String[] elem = cellText.split("---");
            if(elem.length==2 || elem.length==3) {
            	nom = new CellName(elem[0]);
            	contenu = elem[1];
            }else {
            	nom = new CellName(cellText.substring(0, cellText.length()-3));
            	contenu = "";
            }
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
                if(valeur!=null) this.valeur = valeur.copy();
                else this.valeur=null;
	}
        
    public Cellule copy(){
        Cellule c = new Cellule(getText());
        if(valeur!=null) c.affecterValeur(valeur);
        return c;
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
