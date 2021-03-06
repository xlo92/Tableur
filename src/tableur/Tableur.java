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
        }else {
        	String[] cs = idCell.split(":");
			if(cs.length==2) {
				Cellule c1 = cells.getCellule(cs[0]);
				Cellule c2 = cells.getCellule(cs[1]);
				if(c1!=null && c2!=null) {
					int baseNum = java.lang.Math.min(c1.getNom().getNum(), c2.getNom().getNum());
					int maxNum = java.lang.Math.max(c1.getNom().getNum(),c2.getNom().getNum());
					int lval1 = 0;
					for(int j = 0; j<c1.getNom().getLet().length();j++) {
						lval1 *= 27;
						lval1 += c1.getNom().getLet().charAt(j) - 'A' + 1;
					}
					int lval2 = 0;
					for(int j = 0; j<c2.getNom().getLet().length();j++) {
						lval2 *= 27;
						lval2 += c2.getNom().getLet().charAt(j) - 'A' + 1;
					}
					int baseLet = java.lang.Math.min(lval1,lval2);
					int maxLet = java.lang.Math.max(lval1,lval2);
					int lval;
					String let;
					char l;
					for(int j=baseLet;j<=maxLet;j++) {
						lval = j;
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
							for(int k=baseNum;k<=maxNum;k++) {
								if((c=cells.getCellule(let+k))!=null) {
									c.clear();
						        	cells.majDependantes(c.getNom().getFullName());
								}
							}
						}
					}
				}
			}
        }
	}

	public Boolean modifierDonnee(String idCell, String data) {
        Cellule c = this.cells.getCellule(idCell);
        if(c!=null) {
        	Interpreter i = new Interpreter(cells);
        	if(!i.isDependante(data, idCell)) {
	        	c.affecterContenu(data);
	        	c.affecterValeur(i.evaluer(idCell,data));
	        	cells.majDependantes(idCell);
	        	return true;
        	}
        	return false;
        }else {
        	Interpreter i = new Interpreter(cells);
        	String[] cs = idCell.split(":");
			if(cs.length==2) {
				Cellule c1 = cells.getCellule(cs[0]);
				Cellule c2 = cells.getCellule(cs[1]);
				if(c1!=null && c2!=null) {
					int baseNum = java.lang.Math.min(c1.getNom().getNum(), c2.getNom().getNum());
					int maxNum = java.lang.Math.max(c1.getNom().getNum(),c2.getNom().getNum());
					int lval1 = 0;
					for(int j = 0; j<c1.getNom().getLet().length();j++) {
						lval1 *= 27;
						lval1 += c1.getNom().getLet().charAt(j) - 'A' + 1;
					}
					int lval2 = 0;
					for(int j = 0; j<c2.getNom().getLet().length();j++) {
						lval2 *= 27;
						lval2 += c2.getNom().getLet().charAt(j) - 'A' + 1;
					}
					int baseLet = java.lang.Math.min(lval1,lval2);
					int maxLet = java.lang.Math.max(lval1,lval2);
					int lval;
					String let;
					char l;
					for(int j=baseLet;j<=maxLet;j++) {
						lval = j;
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
							for(int k=baseNum;k<=maxNum;k++) {
								if((c=cells.getCellule(let+k))!=null) {
						        	if(!i.isDependante(data, c.getNom().getFullName())) {
							        	c.affecterContenu(data);
							        	c.affecterValeur(i.evaluer(c.getNom().getFullName(),data));
							        	cells.majDependantes(c.getNom().getFullName());
						        	}else {
						        		return false;
						        	}
								}
							}
						}
					}
					return true;
				}
			}
        }
        return null;
	}
	
	public Boolean propagerDonnee(String idBase, String idCible) {
		Cellule b = this.cells.getCellule(idBase);
		Cellule c = this.cells.getCellule(idCible);
		if(b!=null && c!=null) {
			Interpreter i = new Interpreter(cells);
			String data = i.transformer(b.getContenu(), idBase, idCible);
			if(data.equals("")) return null;
			if(!data.equals("") && !i.isDependante(data, idCible)) {
				c.affecterContenu(data);
				c.affecterValeur(i.evaluer(idCible,data));
				cells.majDependantes(idCible);
				return true;
			}
			return false;
		}else {
			if(b!=null) {
				Interpreter i = new Interpreter(cells);
				String[] cs = idCible.split(":");
				if(cs.length==2) {
					Cellule c1 = cells.getCellule(cs[0]);
					Cellule c2 = cells.getCellule(cs[1]);
					if(c1!=null && c2!=null) {
						int baseNum = java.lang.Math.min(c1.getNom().getNum(), c2.getNom().getNum());
						int maxNum = java.lang.Math.max(c1.getNom().getNum(),c2.getNom().getNum());
						int lval1 = 0;
						for(int j = 0; j<c1.getNom().getLet().length();j++) {
							lval1 *= 27;
							lval1 += c1.getNom().getLet().charAt(j) - 'A' + 1;
						}
						int lval2 = 0;
						for(int j = 0; j<c2.getNom().getLet().length();j++) {
							lval2 *= 27;
							lval2 += c2.getNom().getLet().charAt(j) - 'A' + 1;
						}
						int baseLet = java.lang.Math.min(lval1,lval2);
						int maxLet = java.lang.Math.max(lval1,lval2);
						int lval;
						String let;
						char l;
						for(int j=baseLet;j<=maxLet;j++) {
							lval = j;
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
								for(int k=baseNum;k<=maxNum;k++) {
									if((c=cells.getCellule(let+k))!=null) {
										String data = i.transformer(b.getContenu(), idBase, c.getNom().getFullName());
										if(data.equals("")) return null;
										if(!data.equals("") && !i.isDependante(data, c.getNom().getFullName())) {
											c.affecterContenu(data);
											c.affecterValeur(i.evaluer(c.getNom().getFullName(),data));
											cells.majDependantes(idCible);
										}else {
											return false;
										}
									}
								}
							}
						}
						return true;
					}
				}
			}
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
			String res = "";
			String[] cs = idCell.split(":");
			if(cs.length==2) {
				Cellule c1 = cells.getCellule(cs[0]);
				Cellule c2 = cells.getCellule(cs[1]);
				if(c1!=null && c2!=null) {
					int baseNum = java.lang.Math.min(c1.getNom().getNum(), c2.getNom().getNum());
					int maxNum = java.lang.Math.max(c1.getNom().getNum(),c2.getNom().getNum());
					int lval1 = 0;
					for(int j = 0; j<c1.getNom().getLet().length();j++) {
						lval1 *= 27;
						lval1 += c1.getNom().getLet().charAt(j) - 'A' + 1;
					}
					int lval2 = 0;
					for(int j = 0; j<c2.getNom().getLet().length();j++) {
						lval2 *= 27;
						lval2 += c2.getNom().getLet().charAt(j) - 'A' + 1;
					}
					int baseLet = java.lang.Math.min(lval1,lval2);
					int maxLet = java.lang.Math.max(lval1,lval2);
					int lval;
					String let;
					char l;
					for(int j=baseLet;j<=maxLet;j++) {
						lval = j;
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
							for(int k=baseNum;k<=maxNum;k++) {
								if((b=cells.getCellule(let+k))!=null) {
									res += "Nom: "+b.getNom().getFullName()+System.getProperty("line.separator")+"Contenu: "+b.getContenu()+System.getProperty("line.separator");
									if(b.getValeur()!=null) {
										res+="Valeur: "+b.getValeur().getValeur()+System.getProperty("line.separator");
									}
								}
							}
						}
					}
					return res;
				}
			}
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
