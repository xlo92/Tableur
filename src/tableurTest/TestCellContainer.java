package tableurTest;

import org.junit.Test;

import tableur.*;

public class TestCellContainer {

	@Test
	public void testadd() {
		Cellule c = new Cellule("A1---test");
		CellContainer cc = new CellContainer();
		assert(cc.add(c));
	}
	
	@Test
	public void testgetCelluleNExistePas() {
		Cellule c = new Cellule("A1---test");
		CellContainer cc = new CellContainer();
		cc.add(c);
		assert(cc.getCellule("A2")==null);
	}
	
	@Test
	public void testGetCelluleExiste() {
		Cellule c = new Cellule("A1---test");
		CellContainer cc = new CellContainer();
		cc.add(c);
		Cellule c1 = null;
		assert((c1=cc.getCellule("A1"))!=null && c1.getNom().equals("A1") && c1.getValeur().getValeur().equals("test"));
	}
	
	@Test
	public void testMajDependantes() {
		Cellule c = new Cellule("A1---3");
		Cellule c2 = new Cellule("A2---A1");
		c2.affecterValeur(new CellValeur<Integer>(8));
		CellContainer cc = new CellContainer();
		cc.add(c);
		cc.add(c2);
		cc.majDependantes("A1");
		assert(cc.getCellule("A2").getValeur().getValeur()==(new CellValeur<Integer>(3)).getValeur());
	}
	
	@Test
	public void testCopy() {
		CellContainer c = new CellContainer();
		CellContainer c2 = c.copy();
		assert(c!=c2);
	}
}
