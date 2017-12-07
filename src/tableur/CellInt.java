package tableur;

public class CellInt extends CellValeur {

	private Integer val;
	
	public CellInt(Integer val) {
            this.val = val;
	}
	
        @Override
	public Integer getValeur() {
            return this.val;
	}

        @Override
        public CellValeur copy() {
            return new CellInt(val);
        }
}
