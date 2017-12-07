package tableurTest;

import org.junit.Test;

import tableur.*;

public class TestCellValeur {

	@Test
	public void testgetValeurEntier() {
		CellValeur v = new CellInt(7);
		assert(7==(Integer)(v.getValeur()));
	}
	
	@Test
	public void testgetValeurDouble() {
		CellValeur v = new CellDouble(7.8);
		assert(7.8==(Double)(v.getValeur()));
	}
	
	@Test
	public void testgetValeurString() {
		CellValeur v = new CellString("test");
		assert(((String)(v.getValeur())).equals("test"));
	}
	
	@Test
	public void testCopy() {
		CellValeur v = new CellInt(42);
		CellValeur c = v.copy();
		assert((Integer)(v.getValeur())==(Integer)(c.getValeur()) && v!=c);
	}
}
