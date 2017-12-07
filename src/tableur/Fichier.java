package tableur;

import java.io.File;

public class Fichier {

	private File filename;
	
	public Fichier(String filename) {
            this.filename = new File(filename);
	}
	
	public boolean makeBackUp() {
            if(!filename.isDirectory()){
                String newFileName = this.filename.getName()+".bak";
                filename.renameTo(new File(newFileName));
                return true;
            }
            return false;
	}
	
	public boolean ecrireFichier(CellContainer cells) {
            
            return false;
	}
	
	public boolean IsOnDisk(){
            if(filename.exists()){
                return true;
            }
            return false;
	}
	
	public boolean ecrireCell(String cellText) {
		return false;
	}
	
	public CellContainer lireFichier() {
		return null;
	}
	
	public String lireCell() {
		return "";
	}
}
