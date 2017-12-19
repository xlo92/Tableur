package tableurTest;

import org.junit.Test;

import tableur.*;

public class TestCellContainer {

	@Test
	public void testadd() {
		Cellule c = new Cellule("A1---test");
		CellContainer cc = new CellContainer();
		assert(cc.add(c) && cc.getCellule("A1")!=null && cc.getCellule("A1").getNom().getFullName().equals("A1") && cc.getCellule("A1").getContenu().equals("test"));
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
		assert((c1=cc.getCellule("A1"))!=null && c1.getNom().getFullName().equals("A1") && c1.getContenu().equals("test"));
	}
	
	@Test
	public void testMajDependantes() {
		Cellule c = new Cellule("A1---3");
		Cellule c2 = new Cellule("A2---A1");
		CellContainer cc = new CellContainer();
		cc.add(c);
		cc.add(c2);
		cc.getCellule("A2").affecterValeur(new CellInt(8));
		cc.getCellule("A1").affecterValeur(new CellInt(3));
		cc.majDependantes("A1");
		assert((Integer)(cc.getCellule("A2").getValeur().getValeur())==3);
	}
	@Test
	public void testMajDependantes2() {
		Cellule c = new Cellule("A1---");
		Cellule c2 = new Cellule("A2---");
		Cellule c3 = new Cellule("A3---");
		CellContainer cc = new CellContainer();
		cc.add(c);
		cc.add(c2);
		cc.add(c3);
		cc.getCellule("A1").affecterContenu("A2+A3");
		Interpreter i = new Interpreter(cc);
		cc.getCellule("A1").affecterValeur(i.evaluer("A1","A2+A3"));
		cc.majDependantes("A1");
		cc.getCellule("A2").affecterContenu("10");
		cc.getCellule("A2").affecterValeur(i.evaluer("A2","10"));
		cc.majDependantes("A2");
		cc.getCellule("A3").affecterContenu("5");
		cc.getCellule("A3").affecterValeur(i.evaluer("A3","5"));
		cc.majDependantes("A3");
		assert((Integer)(cc.getCellule("A1").getValeur().getValeur())==15);
	}
}
