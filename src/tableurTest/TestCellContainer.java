package tableurTest;

import static org.junit.Assert.*;

import org.junit.Test;

import tableur.*;

public class TestCellContainer {

	@Test
	public void testadd() {
		assert(false);
	}
	
	@Test
	public void testgetCelluleNExistePas() {
		assert(false);
	}
	
	@Test
	public void testGetCelluleExiste() {
		assert(false);
	}
	
	@Test
	public void testMajDependantesSansDependantes() {
		assert(false);
	}
	
	@Test
	public void testMajDependantes() {
		assert(false);
	}
	
	@Test
	public void testCopy() {
		CellContainer c = new CellContainer();
		CellContainer c2 = c.copy();
		assert(c!=c2);
	}
}
