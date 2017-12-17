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
	public void testFonction_min() {
		Cellule c = new Cellule("A1---$min(5,7)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==5);
	}
	
	@Test
	public void testFonction_max() {
		Cellule c = new Cellule("A1---$max(5,7)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==7);
	}
	
	@Test
	public void testFonction_add() {
		Cellule c = new Cellule("A1---$add(5,7)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==12);
	}
	
	@Test
	public void testFonction_sou() {
		Cellule c = new Cellule("A1---$sou(5,7)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==-2);
	}
	
	@Test
	public void testFonction_mul() {
		Cellule c = new Cellule("A1---$mul(5,7)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==35);
	}
	
	@Test
	public void testFonction_div() {
		Cellule c = new Cellule("A1---$div(5,7)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Double)(i.evaluer(c.getContenu()).getValeur())==5.0/7.0);
	}
	
	@Test
	public void testFonction_PI() {
		Cellule c = new Cellule("A1---$PI()");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Double)(i.evaluer(c.getContenu()).getValeur())==java.lang.Math.PI);
	}
	
	@Test
	public void testFonction_E() {
		Cellule c = new Cellule("A1---$E()");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Double)(i.evaluer(c.getContenu()).getValeur())==java.lang.Math.E);
	}

	@Test
	public void testFonction_rond() {
		Cellule c = new Cellule("A1---$rond(7.5)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==8);
	}
	
	@Test
	public void testFonction_abs() {
		Cellule c = new Cellule("A1---$abs(-5)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==5);
	}
	
	@Test
	public void testFonction_acos() {
		Cellule c = new Cellule("A1---$acos(1)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Double)(i.evaluer(c.getContenu()).getValeur())==java.lang.Math.acos(1));
	}
	
	@Test
	public void testFonction_asin() {
		Cellule c = new Cellule("A1---$asin(1)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Double)(i.evaluer(c.getContenu()).getValeur())==java.lang.Math.asin(1));
	}
	
	@Test
	public void testFonction_atan() {
		Cellule c = new Cellule("A1---$atan(1)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Double)(i.evaluer(c.getContenu()).getValeur())==java.lang.Math.atan(1));
	}
	
	@Test
	public void testFonction_cos() {
		Cellule c = new Cellule("A1---$cos($PI())");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Double)(i.evaluer(c.getContenu()).getValeur())==java.lang.Math.cos(java.lang.Math.PI));
	}
	
	@Test
	public void testFonction_sin() {
		Cellule c = new Cellule("A1---$sin($PI())");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Double)(i.evaluer(c.getContenu()).getValeur())==java.lang.Math.sin(java.lang.Math.PI));
	}
	
	@Test
	public void testFonction_tan() {
		Cellule c = new Cellule("A1---$tan($PI())");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Double)(i.evaluer(c.getContenu()).getValeur())==java.lang.Math.tan(java.lang.Math.PI));
	}
	
	@Test
	public void testFonction_exp() {
		Cellule c = new Cellule("A1---$exp(2)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Double)(i.evaluer(c.getContenu()).getValeur())==java.lang.Math.exp(2));
	}
	
	@Test
	public void testFonction_ln() {
		Cellule c = new Cellule("A1---$ln($exp(2))");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==2);
	}
	
	@Test
	public void testFonction_log() {
		Cellule c = new Cellule("A1---$log(100.00)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==2);
	}
	
	@Test
	public void testFonction_pow() {
		Cellule c = new Cellule("A1---$pow(2.0,3.0)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==8);
	}
	
	@Test
	public void testFonction_ent() {
		Cellule c = new Cellule("A1---$ent(3.2)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==3);
	}
	
	@Test
	public void testFonction_sqrt() {
		Cellule c = new Cellule("A1---$sqrt(64)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==8);
	}
	
	@Test
	public void testFonction_cbrt() {
		Cellule c = new Cellule("A1---$cbrt(-64)");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==-4);
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
		Cellule c = new Cellule("A1---8/4+5*7--2");
		CellContainer cells = new CellContainer();
		cells.add(c);
		Interpreter i =  new Interpreter(cells);
		assert((Integer)(i.evaluer(c.getContenu()).getValeur())==39);
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
	public void testIsDependanteDependante2() {
		Interpreter i =  new Interpreter();
		Cellule c = new Cellule("A1---A2+A3+$abs(A4)");
		Cellule c1 = new Cellule("A2---3");
		Cellule c2 = new Cellule("A3---5");
		Cellule c3 = new Cellule("A4---8");
		CellContainer cells = new CellContainer();
		cells.add(c);
		cells.add(c1);
		cells.add(c2);
		cells.add(c3);
		i.setCells(cells);
		assert(i.isDependante(c.getContenu(),"A2") && i.isDependante(c.getContenu(),"A3") && i.isDependante(c.getContenu(),"A4"));
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
		Cellule c1 = new Cellule("B1---3");
		Cellule c2 = new Cellule("A2---4");
		Cellule c3 = new Cellule("B2---7");
		CellContainer cells = new CellContainer();
		cells.add(c);
		cells.add(c1);
		cells.add(c2);
		cells.add(c3);
		i.setCells(cells);
		assert(i.transformer("B1","A1","A2").equals("B2"));
	}
}
