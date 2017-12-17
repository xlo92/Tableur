package tableur;

import java.io.File;

public class Tableur {
	
	private CellContainer cells;
	
	public Tableur() {
		char c;
		String s;
		this.cells = new CellContainer();
		for(int i = 0; i < 3; i++) {
			c = (char)('A'+i);
			for(int j = 1; j < 4; j++) {
				s=""+c+j+"---";
				cells.add(new Cellule(s));
			}
		}
	}
	
	public void enregistrer(String filename) {
        Fichier f = new Fichier(filename);
        if(f.IsOnDisk()) f.makeBackUp();
        f.ecrireFichier(cells);
	}
	
	public boolean ouvrir(String filename) {
		Fichier f = new Fichier(filename);
		if(f.IsOnDisk()) {
	        File fic = new File(filename);
	        this.cells = f.lireFichier(fic);
	        return true;
		}else {
			return false;
		}
	}
	
	public void supprimerDonnee(String idCell) {
        Cellule c = this.cells.getCellule(idCell);
        if(c!=null) {
        	c.clear();
        	cells.majDependantes(idCell);
        }
	}

	public Boolean modifierDonnee(String idCell, String data) {
        Cellule c = this.cells.getCellule(idCell);
        if(c!=null) {
        	Interpreter i = new Interpreter(cells);
        	if(!i.isDependante(data, idCell)) {
	        	c.affecterContenu(data);
	        	c.affecterValeur(i.evaluer(data));
	        	cells.majDependantes(idCell);
	        	return true;
        	}
        	return false;
        }
        return null;
	}
	
	public Boolean propagerDonnee(String idBase, String idCible) {
		Cellule b = this.cells.getCellule(idBase);
		Cellule c = this.cells.getCellule(idCible);
		if(b!=null && c!=null) {
			Interpreter i = new Interpreter(cells);
			String data = i.transformer(b.getContenu(), idBase, idCible);
			if(!i.isDependante(data, idCible)) {
				c.affecterContenu(data);
				c.affecterValeur(i.evaluer(data));
				cells.majDependantes(idCible);
			}
			return false;
		}
		return null;
	}
	
	public String getInfosCellule(String idCell) {
		Cellule b = this.cells.getCellule(idCell);
		if(b!=null) {
			String res = "Nom: "+idCell+System.getProperty("line.separator")+"Contenu: "+b.getContenu()+System.getProperty("line.separator");
			if(b.getValeur()!=null) {
				res+="Valeur: "+b.getValeur().getValeur()+System.getProperty("line.separator");
			}
			return res;
		}else {
			return "";
		}
	}
	
	public String cellsToString() {
		String res = "";
		for(Cellule c : cells.getCellList()) {
			res+=c.getNom().getFullName()+" : ";
			if(c.getValeur()!=null) {
				res+=c.getValeur().getValeur();
			}else {
				res+=c.getContenu();
			}
			res+=System.getProperty("line.separator");
		}
		return res;
	}
	public void addLigne() {
		int maxNum = 0;
		int maxLet = 0;
		int lval;
		String let;
		char l;
		int pos;
		for(Cellule c : cells.getCellList()) {
			if(c.getNom().getNum()>maxNum) maxNum = c.getNom().getNum();
			lval = 0;
			for(int i = 0; i<c.getNom().getLet().length();i++) {
				lval *= 27;
				lval += c.getNom().getLet().charAt(i) - 'A' + 1;
			}
			if(lval>maxLet) maxLet=lval;
		}
		maxNum++;
		pos=0;
		for(int i=1;i<=maxLet;i++) {
			lval = i;
			let="";
			while(lval>0) {
				if(!(lval%27 == 0)) {
					l = (char) ('A' + lval%27 - 1);
					let = l+let;
					lval/=27;
				}else {
					let="";
					break;
				}
			}
			if(!let.equals("")) {
				cells.add(maxNum-1+pos*maxNum,new Cellule(let+maxNum+"---"));
				pos++;
			}
		}
	}
	
	public void addColonne() {
		int maxNum = 0;
		int maxLet = 0;
		int lval;
		String let;
		char l;
		for(Cellule c : cells.getCellList()) {
			if(c.getNom().getNum()>maxNum) maxNum = c.getNom().getNum();
			lval = 0;
			for(int i = 0; i<c.getNom().getLet().length();i++) {
				lval *= 27;
				lval += c.getNom().getLet().charAt(i) - 'A' + 1;
			}
			if(lval>maxLet) maxLet=lval;
		}
		do {
			maxLet++;
			lval = maxLet;
			let="";
			while(lval>0) {
				if(!(lval%27 == 0)) {
					l = (char) ('A' + lval%27 - 1);
					let = l+let;
					lval/=27;
				}else {
					let="";
					break;
				}
			}
		}while(let.equals(""));
		
		for(int i=1;i<=maxNum;i++) {
			cells.add(new Cellule(let+i+"---"));
		}
	}
}
