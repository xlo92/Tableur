package tableurTest;

import org.junit.Test;

import tableur.*;

public class TestCellValeur {

	@Test
	public void testgetValeurEntier() {
		CellValeur<Integer> v = new CellValeur<Integer>(7);
		assert(7==v.getValeur());
	}
	
	@Test
	public void testgetValeurDouble() {
		CellValeur<Double> v = new CellValeur<Double>(7.8);
		assert(7.8==v.getValeur());
	}
	
	@Test
	public void testgetValeurString() {
		CellValeur<String> v = new CellValeur<String>("test");
		assert(v.getValeur().equals("test"));
	}
	
	@Test
	public void testsetValeurEntier() {
		CellValeur<Integer> v = new CellValeur<Integer>();
		v.setValeur(7);
		assert(7==v.getValeur());
	}
	
	@Test
	public void testsetValeurDouble() {
		CellValeur<Double> v = new CellValeur<Double>();
		v.setValeur(7.8);
		assert(7.8==v.getValeur());
	}
	
	@Test
	public void testsetValeurString() {
		CellValeur<String> v = new CellValeur<String>();
		v.setValeur("test");
		assert(v.getValeur().equals("test"));
	}
	
	@Test
	public void testCopy() {
		CellValeur<Integer> v = new CellValeur<Integer>();
		v.setValeur(42);
		CellValeur<Integer> c = v.copy();
		assert(v.getValeur()==c.getValeur() && v!=c);
	}
}
