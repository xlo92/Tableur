package tableurTest;

import org.junit.Test;

import tableur.*;

public class TestInterpreter {
	
	@Test
	public void testSingleton() {
		Interpreter i = Interpreter.getInstance();
		Interpreter j = Interpreter.getInstance();
		assert(i==j);
	}
	
	@Test
	public void testEvaluerTexte() {
		Interpreter i = Interpreter.getInstance();
		Cellule c = new Cellule("A1---test");
		assert(i.evaluer(c.getContenu())==null);
	}
	
	@Test
	public void testEvaluerNombre() {
		Interpreter i = Interpreter.getInstance();
		Cellule c = new Cellule("A1---7.5");
		assert(i.evaluer(c.getContenu()).getValeur()==(new CellValeur<Double>(7.5)).getValeur());
	}
	
	@Test
	public void testEvaluerAutreCellule() {
		Interpreter i = Interpreter.getInstance();
		Cellule c = new Cellule("A1---A2");
		Cellule c2 = new Cellule("A2---5");
		c2.affecterValeur(i.evaluer(c2.getContenu()));
		CellContainer cells = new CellContainer();
		cells.add(c);
		cells.add(c2);
		i.setCells(cells);
		assert(i.evaluer(c.getContenu()).getValeur()==(new CellValeur<Integer>(5)).getValeur());
	}
	
	@Test
	public void testEvaluerFonction() {
		Interpreter i = Interpreter.getInstance();
		Cellule c = new Cellule("A1---$max(A2,B1)");
		Cellule c2 = new Cellule("A2---5");
		Cellule c3 = new Cellule("B1---12");
		c2.affecterValeur(i.evaluer(c2.getContenu()));
		c3.affecterValeur(i.evaluer(c3.getContenu()));
		CellContainer cells = new CellContainer();
		cells.add(c);
		cells.add(c2);
		cells.add(c3);
		i.setCells(cells);
		assert(i.evaluer(c.getContenu()).getValeur()==(new CellValeur<Integer>(12)).getValeur());
	}
	
	@Test
	public void testEvaluerFonctionsEmboitees() {
		Interpreter i = Interpreter.getInstance();
		Cellule c = new Cellule("A1---$max(A2,$max(B1,B2))");
		Cellule c2 = new Cellule("A2---5");
		Cellule c3 = new Cellule("B1---12");
		Cellule c4 = new Cellule("B2---7");
		c2.affecterValeur(i.evaluer(c2.getContenu()));
		c3.affecterValeur(i.evaluer(c3.getContenu()));
		c4.affecterValeur(i.evaluer(c4.getContenu()));
		CellContainer cells = new CellContainer();
		cells.add(c);
		cells.add(c2);
		cells.add(c3);
		cells.add(c4);
		i.setCells(cells);
		assert(i.evaluer(c.getContenu()).getValeur()==(new CellValeur<Integer>(12)).getValeur());
	}
	
	@Test
	public void testEvaluerErreurContenu() {
		Interpreter i = Interpreter.getInstance();
		Cellule c = new Cellule("A1---$max(3,5,7)");
		assert(i.evaluer(c.getContenu()).getValeur()==(new CellValeur<String>("!ERREUR!")).getValeur());
	}
	
	@Test
	public void testIsDependantePasDependante() {
		assert(false);
	}
	
	@Test
	public void testIsDependanteDependante() {
		assert(false);
	}
	
	@Test
	public void testTransformer() {
		assert(false);
	}
}
