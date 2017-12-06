package tableurTest;

import org.junit.Test;

import tableur.*;

public class TestCellule {

	@Test
	public void testAffecterContenuTexte() {
		Cellule c = new Cellule("A1---test");
		c.affecterContenu("Nouveau test");
		assert(c.getContenu().equals("Nouveau test"));
	}
	
	@Test
	public void testAffecterContenuNombre() {
		Cellule c = new Cellule("A1---3.7");
		c.affecterContenu("12.3");
		assert(c.getContenu().equals("12.3"));
	}
	
	@Test
	public void testAffecterContenuFormule() {
		Cellule c = new Cellule("A1---$max(1,7)");
		c.affecterContenu("$min(3,2)");
		assert(c.getContenu().equals("$min(3,2)"));
	}
	
	@Test
	public void testAffecterValeurEntier() {
		Cellule c = new Cellule("A1---test");
		CellValeur<Integer> v = new CellValeur<Integer>(3);
		c.affecterValeur(v);
		assert(c.getValeur().getValeur()==v.getValeur());
	}
	
	@Test
	public void testAffecterValeurDouble() {
		Cellule c = new Cellule("A1---test");
		CellValeur<Double> v = new CellValeur<Double>(7.6);
		c.affecterValeur(v);
		assert(c.getValeur().getValeur()==v.getValeur());
	}
	
	@Test
	public void testAffecterValeurString() {
		Cellule c = new Cellule("A1---test");
		CellValeur<String> v = new CellValeur<String>("test valeur");
		c.affecterValeur(v);
		assert(c.getValeur().getValeur()==v.getValeur());
	}
	
	@Test
	public void testClear() {
		Cellule c = new Cellule("A1---test");
		c.clear();
		assert(c.getValeur()==null && c.getContenu().equals(""));
	}
	
	@Test
	public void testGetContenuFormule() {
		Cellule c = new Cellule("A1---$max(3,4)");
		assert(c.getContenu().equals("$max(3,4)"));
	}
	
	@Test
	public void testGetContenuNombre() {
		Cellule c = new Cellule("A1---1.7");
		assert(c.getContenu().equals("1.7"));
	}
	
	@Test
	public void testGetContenuTexte() {
		Cellule c = new Cellule("A1---test");
		assert(c.getContenu().equals("test"));
	}
	
	@Test
	public void testGetNom() {
		Cellule c = new Cellule("A1---test");
		assert(c.getNom().equals("A1"));
	}
	
	@Test
	public void testGetTextFormule() {
		Cellule c = new Cellule("A1---$min(7,12)");
		assert(c.getText().equals("A1---$min(7,12)"));
	}
	
	@Test
	public void testGetTextNombre() {
		Cellule c = new Cellule("A1---42,4242");
		assert(c.getText().equals("A1---42,4242"));
	}
	
	@Test
	public void testGetTextTexte() {
		Cellule c = new Cellule("A1---test");
		assert(c.getText().equals("A1---test"));
	}
	
	@Test
	public void testGetValeurEntier() {
		Cellule c = new Cellule("A1---test");
		CellValeur<Integer> v = new CellValeur<Integer>(5);
		c.affecterValeur(v);
		assert(c.getValeur().getValeur()==v.getValeur());
	}
	
	@Test
	public void testGetValeurDouble() {
		Cellule c = new Cellule("A1---test");
		CellValeur<Double> v = new CellValeur<Double>(9.18);
		c.affecterValeur(v);
		assert(c.getValeur().getValeur()==v.getValeur());
	}
	
	@Test
	public void testGetValeurString() {
		Cellule c = new Cellule("A1---test");
		CellValeur<String> v = new CellValeur<String>("test get valeur");
		c.affecterValeur(v);
		assert(c.getValeur().getValeur()==v.getValeur());
	}
}
