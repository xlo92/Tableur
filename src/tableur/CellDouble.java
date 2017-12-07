package tableur;

public class CellDouble extends CellValeur {

	private Double val;
	
	public CellDouble(Double val) {
            this.val = val;
	}
	
        @Override
	public Double getValeur() {
            return this.val;
	}

        @Override
        public CellValeur copy() {
            return new CellDouble(val);
        }
}
