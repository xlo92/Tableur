package tableurTest;

import org.junit.Test;

import tableur.*;

public class TestInterpreter {

	@Test
	public void testEvaluerTexte() {
		Cellule c = new Cellule("A1---test");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert(i.evaluer(c.getContenu())==null);
	}
	
	@Test
	public void testEvaluerDouble() {
		Cellule c = new Cellule("A1---7.5");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Double)(i.evaluer(c.getContenu()).getValeur())==7.5);
	}
	
	@Test
	public void testEvaluerEntier() {
		Cellule c = new Cellule("A1---3");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==3);
	}
	
	@Test
	public void testEvaluerAdd() {
		Cellule c = new Cellule("A1---3+4");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==7);
	}
	
	@Test
	public void testEvaluerSou() {
		Cellule c = new Cellule("A1---3-4");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==-1);
	}
	
	@Test
	public void testEvaluerMul() {
		Cellule c = new Cellule("A1---3*4");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==12);
	}
	
	@Test
	public void testEvaluerDiv() {
		Cellule c = new Cellule("A1---3/4");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Double)(i.evaluer(c.getContenu()).getValeur())==0.75);
	}
	
	@Test
	public void testEvaluerOrdre() {
		Cellule c = new Cellule("A1---8/4+5*7-2");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==35);
	}
	
	@Test
	public void testEvaluerAutreCellule() {
		Interpreter i =  new Interpreter();
		Cellule c = new Cellule("A1---A2");
		Cellule c2 = new Cellule("A2---5");
		CellContainer cells = new CellContainer();
		cells.add(c);
		cells.add(c2);
		cells.getCellule("A2").affecterValeur(i.evaluer("5"));
		i.setCells(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==5);
	}
	
	@Test
	public void testEvaluerParenthese() {
		Interpreter i =  new Interpreter();
		Cellule c = new Cellule("A1---(A2)");
		Cellule c2 = new Cellule("A2---5");
		CellContainer cells = new CellContainer();
		cells.add(c);
		cells.add(c2);
		cells.getCellule("A2").affecterValeur(i.evaluer("5"));
		i.setCells(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==5);
	}
	
	@Test
	public void testEvaluerFonction() {
		Interpreter i =  new Interpreter();
		Cellule c = new Cellule("A1---$max(A2,B1)");
		Cellule c2 = new Cellule("A2---5");
		Cellule c3 = new Cellule("B1---12");
		CellContainer cells = new CellContainer();
		cells.add(c);
		cells.add(c2);
		cells.add(c3);
		i.setCells(cells);
		cells.getCellule("A2").affecterValeur(i.evaluer("5"));
		cells.getCellule("B1").affecterValeur(i.evaluer("12"));
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==12);
	}
	
	@Test
	public void testEvaluerFonctionsEmboitees() {
		Interpreter i =  new Interpreter();
		Cellule c = new Cellule("A1---$max(A2,$min(B1,B2))");
		Cellule c2 = new Cellule("A2---5");
		Cellule c3 = new Cellule("B1---12");
		Cellule c4 = new Cellule("B2---7");
		CellContainer cells = new CellContainer();
		cells.add(c);
		cells.add(c2);
		cells.add(c3);
		cells.add(c4);
		i.setCells(cells);
		cells.getCellule("A2").affecterValeur(i.evaluer("5"));
		cells.getCellule("B2").affecterValeur(i.evaluer("7"));
		cells.getCellule("B1").affecterValeur(i.evaluer("12"));
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==7);
	}
	
	@Test
	public void testEvaluerErreurFonction() {
		Interpreter i =  new Interpreter();
		Cellule c = new Cellule("A1---$max(3,5,7)");
		assert(((String)(i.evaluer(c.getContenu()).getValeur())).equals("!ERREUR!"));
	}
	
	@Test
	public void testEvaluerErreurTexte() {
		Interpreter i =  new Interpreter();
		Cellule c = new Cellule("A1---&3");
		assert(((String)(i.evaluer(c.getContenu()).getValeur())).equals("!ERREUR!"));
	}
	
	@Test
	public void testIsDependantePasDependante() {
		Interpreter i =  new Interpreter();
		Cellule c = new Cellule("A1---7.5");
		CellContainer cells = new CellContainer();
		cells.add(c);
		i.setCells(cells);
		assert(!i.isDependante(c.getContenu(),"A2"));
	}
	
	@Test
	public void testIsDependanteDependante() {
		Interpreter i =  new Interpreter();
		Cellule c = new Cellule("A1---A2");
		Cellule c1 = new Cellule("A2---3");
		CellContainer cells = new CellContainer();
		cells.add(c);
		cells.add(c1);
		i.setCells(cells);
		assert(i.isDependante(c.getContenu(),"A2"));
	}
		
	@Test
	public void testIsDependanteDependanteEnCascade() {
		Interpreter i =  new Interpreter();
		Cellule c = new Cellule("A1---A3");
		Cellule c1 = new Cellule("A3---A2");
		Cellule c2 = new Cellule("A2---4");
		CellContainer cells = new CellContainer();
		cells.add(c);
		cells.add(c1);
		cells.add(c2);
		i.setCells(cells);
		assert(i.isDependante(c.getContenu(),"A2"));
	}	
	
	@Test
	public void testTransformer() {
		Interpreter i =  new Interpreter();
		Cellule c = new Cellule("A1---B1");
		assert(i.transformer(c.getContenu(),c.getNom().getFullName(),"A2").equals("B2"));
	}
}
