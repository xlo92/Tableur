package tableur;

import java.util.ArrayList;

public class CellContainer {

	private ArrayList<Cellule> cells;
	
	public CellContainer() {
            this.cells = new ArrayList<Cellule>();
	}
	
	public Cellule getCellule(String idCell) {
            for(Cellule c : cells){
                if(c.getNom().getFullName().equals(idCell))
                    return c;
            }
            return null;
	}
	
	public void majDependantes(String uneCellule) {
		Interpreter i = new Interpreter(this);
		Cellule b = getCellule(uneCellule);
		if(b==null) return;
		for(Cellule c : cells){
            if(i.isDependante(c.getContenu(), uneCellule)){
            	c.affecterValeur(i.evaluer(c.getContenu()));
            	majDependantes(c.getNom().getFullName());
            }
        }
	}
	
	public boolean add(Cellule uneCellule) {
            if(uneCellule != null){
                this.cells.add(uneCellule.copy());
                return true;
            }
            return false;
	}
}
