package tableur;

import java.io.File;

public class Tableur {
	
	private CellContainer cells;
	
	public Tableur() {
            this.cells = new CellContainer();
	}
	
	public void enregistrer(String filename) {
            Fichier f = new Fichier(filename);
            
            f.ecrireFichier(cells);
	}
	
	public void ouvrir(String filename) {
            File fic = new File(filename);
            
            Fichier f = new Fichier(filename);
            
            this.cells = f.lireFichier(fic);
	}
	
	public void supprimerDonnee(String idCell) {
            Cellule c = this.cells.getCellule(idCell);
            if(c!=null)
                c.clear();
	}

	public void modifierDonnee(String idCell, String data) {
            Cellule c = this.cells.getCellule(idCell);
            if(c!=null)
                c.affecterContenu(data);
	}
	
	public boolean propagerDonnee(String idBase, String idCible) {
	
        return false;
	}
}
