package tableur;

public class CellValeur<T> {

	private T val;
	
	public CellValeur() {
		this.val = null;
	}
	
	public CellValeur(T val) {
		this.val = val;
	}
	
	public void setValeur(T val) {
		this.val = val;
	}
	
	public T getValeur() {
		return this.val;
	}
	
	public CellValeur<T> copy(){
		return new CellValeur<T>(val);
	}
}
