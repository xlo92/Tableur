package tableur;

import java.util.ArrayList;

public class CellContainer {

	private ArrayList<Cellule> cells;
	
	public CellContainer() {
            this.cells = new ArrayList<Cellule>();
	}
	
	public Cellule getCellule(String idCell) {
            CellName n = new CellName(idCell);
            for(Cellule c : cells){
                if(n.equals(c.getNom()))
                    return c;
            }
            return null;
	}
	
	public void majDependantes(String uneCellule) {
		
	}
	
	public boolean add(Cellule uneCellule) {
            if(uneCellule != null){
                this.cells.add(uneCellule.copy());
                return true;
            }
            return false;
	}
	
	public CellContainer copy() {
            CellContainer newCC = new CellContainer();
            for(Cellule c : cells){
                newCC.cells.add(c);
            }
            return newCC;
	}
}
