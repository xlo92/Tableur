package tableur;

import org.apache.commons.lang3.mutable.MutableInt;

public class Interpreter {
	
	private CellContainer cells;
	
	public Interpreter() {
		cells = null;
	}
	
	public Interpreter(CellContainer cells) {
		this.cells = cells;
	}
	
	private CellValeur evaluer_num(String contenuCellule, MutableInt p) {
		String res = "";
		for(int i = p.intValue();i<contenuCellule.length();i++) {
			if(Character.isDigit(contenuCellule.charAt(i))) {
				p.add(1);
				res +=contenuCellule.charAt(i);
			}else {
				break;
			}
		}
		if(p.intValue()<contenuCellule.length() && contenuCellule.charAt(p.intValue())=='.') {
			res +=".";
			p.add(1);
			for(int i = p.intValue();i<contenuCellule.length();i++) {
				if(Character.isDigit(contenuCellule.charAt(i))) {
					p.add(1);
					res +=contenuCellule.charAt(i);
				}else {
					break;
				}
			}
		}
		Double d = Double.valueOf(res);
		int i = d.intValue();
		if(d==i) return new CellInt(i);
		return new CellDouble(d);
	}
	
	private CellValeur evaluer_texte(String contenuCellule, MutableInt p) {
		String res = "";
		Cellule c;
		for(int i = p.intValue();i<contenuCellule.length();i++) {
			if(Character.isLetter(contenuCellule.charAt(i)) || contenuCellule.charAt(i) == '_' || Character.isDigit(contenuCellule.charAt(i))) {
				p.add(1);
				res +=contenuCellule.charAt(i);
			}else {
				break;
			}
		}
		if((c=cells.getCellule(res))!=null) {
			return c.getValeur();
		}else {
			return null;
		}
	}
	
	private CellValeur evaluer_fct_min(CellValeur param1, CellValeur param2) {
		double lval, rval;
		if(param1 == null || param2 == null || param1.getValeur().getClass()==String.class || param2.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param1.getValeur().getClass()==Integer.class) {
			lval = ((Integer)(param1.getValeur())).doubleValue();
		}else {
			lval = ((Double)(param1.getValeur())).doubleValue();
		}
		if(param2.getValeur().getClass()==Integer.class) {
			rval = ((Integer)(param2.getValeur())).doubleValue();
		}else {
			rval = ((Double)(param2.getValeur())).doubleValue();
		}
		if(lval <= rval) return param1;
		return param2;
	}
	
	private CellValeur evaluer_fct_max(CellValeur param1, CellValeur param2) {
		double lval, rval;
		if(param1 == null || param2 == null || param1.getValeur().getClass()==String.class || param2.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param1.getValeur().getClass()==Integer.class) {
			lval = ((Integer)(param1.getValeur())).doubleValue();
		}else {
			lval = ((Double)(param1.getValeur())).doubleValue();
		}
		if(param2.getValeur().getClass()==Integer.class) {
			rval = ((Integer)(param2.getValeur())).doubleValue();
		}else {
			rval = ((Double)(param2.getValeur())).doubleValue();
		}
		if(lval >= rval) return param1;
		return param2;
	}
	
	private CellValeur evaluer_getParamAt(String param,int n) {
		int nb = 0;
		int nParam = 0;
		int begin = 0;
		for(int i=0;i<param.length();i++) {
			if(param.charAt(i)=='(') nb++;
			if(param.charAt(i)==')') nb--;
			if(param.charAt(i)==',' && nb==0) {
				if(nParam==n) {
					return evaluer(param.substring(begin, i));
				}else {
					nParam++;
					begin=i+1;
				}
			}
			if(nb<0) return new CellString("!ERREUR!");
		}
		return evaluer(param.substring(begin));
	}
	
	private int evaluer_getNbParam(String param) {
		if(param==null || param.equals("")) return 0;
		int nb = 0;
		int nParam = 1;
		for(int i=0;i<param.length();i++) {
			if(param.charAt(i)=='(') nb++;
			if(param.charAt(i)==')') nb--;
			if(param.charAt(i)==',' && nb==0) nParam++;
			if(nb<0) return -1;
		}
		return nParam;
	}
	
	private CellValeur evaluer_call(String name, String param) {
		switch(name) {
		default:
			return new CellString("!ERREUR!");
		case "min":
			if(evaluer_getNbParam(param)==2) {
				return evaluer_fct_min(evaluer_getParamAt(param,0),evaluer_getParamAt(param,1));
			}else {
				return new CellString("!ERREUR!");
			}
		case "max":
			if(evaluer_getNbParam(param)==2) {
				return evaluer_fct_max(evaluer_getParamAt(param,0),evaluer_getParamAt(param,1));
			}else {
				return new CellString("!ERREUR!");
			}
		}
	}
	
	private CellValeur evaluer_fonction(String contenuCellule, MutableInt p) {
		String res = "";
		int tmp;
		for(int i = p.intValue();i<contenuCellule.length();i++) {
			if(Character.isLetter(contenuCellule.charAt(i)) || contenuCellule.charAt(i) == '_' || Character.isDigit(contenuCellule.charAt(i))) {
				p.add(1);
				res +=contenuCellule.charAt(i);
			}else {
				break;
			}
		}
		if(p.intValue()<contenuCellule.length() && contenuCellule.charAt(p.intValue())=='(') {
			tmp = getNextParenthesis(contenuCellule,p.intValue()+1);
			if(tmp==-1) {
				return new CellString("!ERREUR!");
			}else {
				String param = contenuCellule.substring(p.intValue()+1, tmp);
				p.add(1+tmp-p.intValue());
				return evaluer_call(res,param);
			}
		}else {
			return new CellString("!ERREUR!");
		}
	}
	
	private int getNextParenthesis(String contenuCellule, int n) {
		int nb = 0;
		for(int i=n; i < contenuCellule.length();i++) {
			if(contenuCellule.charAt(i)=='(') nb++;
			if(contenuCellule.charAt(i)==')') nb--;
			if(nb<0) return i;
		}
		return -1;
	}
	
	public CellValeur evaluer(String contenuCellule){
		if(contenuCellule==null) return new CellString("!ERREUR!");
		MutableInt p = new MutableInt();
		CellValeur res = null;
		int tmp;
		if(Character.isDigit(contenuCellule.charAt(p.intValue())) || contenuCellule.charAt(p.intValue())=='.') {
			res = evaluer_num(contenuCellule,p);
		}else {
			if(Character.isLetter(contenuCellule.charAt(p.intValue())) || contenuCellule.charAt(p.intValue()) == '_') {
				res = evaluer_texte(contenuCellule,p);
			}else {
				if(contenuCellule.charAt(p.intValue())=='$') {
					p.add(1);
					res = evaluer_fonction(contenuCellule,p);
				}else {
					if(contenuCellule.charAt(p.intValue())=='(') {
						tmp = getNextParenthesis(contenuCellule,p.intValue()+1);
						if(tmp==-1) {
							res = new CellString("!ERREUR!");
						}else {
							res = evaluer(contenuCellule.substring(p.intValue()+1, tmp));
							p.add(1+tmp-p.intValue());
						}
					}else {
						res = new CellString("!ERREUR!");
					}
				}
			}
		}
		return res;
	}
	
	public String transformer(String contenuCelluleB, String nomCelluleB, String nomCelluleC) {
		return "";
	}
	
	public boolean isDependante(String contenuCelluleB, String nomCelluleC) {
		if(cells!=null) {
			String[] res;
			Cellule c;
			boolean flag = true;
			while(flag) {
				res = contenuCelluleB.split("(?<=\\D)(?=\\d)");
				if(res.length==2) {
					c = cells.getCellule(res[0]+res[1]);
					if(c!=null) {
						if(c.getNom().getFullName().equals(nomCelluleC)) {
							return true;
						}else {
							if(isDependante(c.getContenu(),nomCelluleC)) {
								return true;
							}
						}
					}
					if(res[0].length()+res[1].length() < contenuCelluleB.length()) {
						contenuCelluleB = contenuCelluleB.substring(res[0].length()+res[1].length());
					}else {
						return false;
					}
				}else {
					flag=false;
				}
			}
		}
		return false;
	}
	
	public void setCells(CellContainer cells) {
		this.cells = cells;
	}
}
