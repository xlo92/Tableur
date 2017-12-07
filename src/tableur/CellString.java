package tableur;

public class CellString extends CellValeur {

	private String val;
	
	public CellString(String val) {
            this.val = val;
	}
	
        @Override
	public String getValeur() {
            return this.val;
	}

        @Override
        public CellValeur copy() {
            return new CellString(val);
        }
}
