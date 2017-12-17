package tableur;

import java.io.File;

public class Tableur {
	
	private CellContainer cells;
	
	public Tableur() {
        this.cells = new CellContainer();
	}
	
	public void enregistrer(String filename) {
        Fichier f = new Fichier(filename);
        if(f.IsOnDisk()) f.makeBackUp();
        f.ecrireFichier(cells);
	}
	
	public void ouvrir(String filename) {
		char c;
		String s;
		Fichier f = new Fichier(filename);
		if(f.IsOnDisk()) {
	        File fic = new File(filename);
	        this.cells = f.lireFichier(fic);
		}else {
			this.cells = new CellContainer();
			for(int i = 0; i < 20; i++) {
				c = (char)('A'+i);
				for(int j = 1; j < 21; j++) {
					s=""+c+j;
					cells.add(new Cellule(s));
				}
			}
		}
	}
	
	public void supprimerDonnee(String idCell) {
        Cellule c = this.cells.getCellule(idCell);
        if(c!=null) {
        	c.clear();
        	cells.majDependantes(idCell);
        }
	}

	public void modifierDonnee(String idCell, String data) {
        Cellule c = this.cells.getCellule(idCell);
        if(c!=null) {
        	c.affecterContenu(data);
        	Interpreter i = new Interpreter(cells);
        	c.affecterValeur(i.evaluer(data));
        	cells.majDependantes(idCell);
        }
	}
	
	public void propagerDonnee(String idBase, String idCible) {
		Cellule b = this.cells.getCellule(idBase);
		Cellule c = this.cells.getCellule(idCible);
		if(b!=null && c!=null) {
			Interpreter i = new Interpreter(cells);
			c.affecterContenu(i.transformer(b.getContenu(), idBase, idCible));
			c.affecterValeur(i.evaluer(c.getContenu()));
			cells.majDependantes(idCible);
		}
	}
}
