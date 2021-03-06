package tableur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	public File ecrireFichier(CellContainer cells) {
        try {
            PrintWriter writer = new PrintWriter(filename.getName(), "UTF-8");
        
            for(Cellule c : cells.getCellList()){
            	if(c.getValeur()!=null) writer.println(c.getNom().getFullName() +"---"+ c.getContenu() +"---"+ c.getValeur().getValeur());
            	else writer.println(c.getNom().getFullName() +"---"+ c.getContenu());
            }
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(Fichier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return filename;
	}
	
	public boolean IsOnDisk(){
            if(filename.exists() && !filename.isDirectory()){
                return true;
            }
            return false;
	}
	
	public CellContainer lireFichier(File f) {
		 CellContainer cellList = new CellContainer();
         
         FileReader fr;
         BufferedReader br;
         try {
             fr = new FileReader(f);
             br = new BufferedReader(fr);
             
             String line;
             
             try {
                 while((line = br.readLine()) != null){
                     Cellule c = new Cellule(line);
                     String[] splittedLine = line.split("---");
                     if(splittedLine.length==3) {
	                     String value = splittedLine[2];
	                     
	                     try{
	                         int i = Integer.parseInt(value);
	                         CellInt ci = new CellInt(i);
	                         c.affecterValeur(ci);
	                     }catch(Exception e){
	                         try{
	                             Double d = Double.parseDouble(value);
	                             CellDouble cd = new CellDouble(d);
	                             c.affecterValeur(cd);
	                         }catch(Exception e2){
	                             CellString cs = new CellString(value);
	                             c.affecterValeur(cs);
	                         }
	                     }finally{
	                         cellList.add(c);
	                     }
                     }else {
                    	 cellList.add(c);
                     }
                 }
                 fr.close();
                 br.close();
             } catch (IOException ex) {
                 Logger.getLogger(Fichier.class.getName()).log(Level.SEVERE, null, ex);
             }             
         } catch (FileNotFoundException ex) {
             Logger.getLogger(Fichier.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         return cellList;
	}
}
