package tableur;

import java.util.ArrayList;

import org.apache.commons.lang3.mutable.MutableInt;

public class Interpreter {
	
	private CellContainer cells;
	
	private String cellId;
	
	public Interpreter() {
		cells = null;
	}
	
	public Interpreter(CellContainer cells) {
		this.cells = cells;
	}
	
	private CellValeur evaluer_num(String contenuCellule, MutableInt p) {
		String res = "";
		boolean signe;
		if(contenuCellule.charAt(p.intValue())=='+') {
			signe = false;
			p.add(1);
		}else {
			if(contenuCellule.charAt(p.intValue())=='-') {
				signe = true;
				p.add(1);
			}else {
				signe = false;
			}
		}
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
		if(d==i) {
			if(signe) {
				return new CellInt(-i);
			}
			return new CellInt(i);
		}
		if(signe) {
			return new CellDouble(-d);
		}
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
			if(c.getValeur()!=null) return c.getValeur();
			return new CellString("!ERREUR!");
		}else {
			return null;
		}
	}
	
	private CellValeur evaluer_fct_cell(CellValeur param1, CellValeur param2) {
		if(param1 == null || param2 == null || param1.getValeur().getClass()!=Integer.class || param2.getValeur().getClass()!=Integer.class) return new CellString("!ERREUR!");
		CellName cn = new CellName(cellId);
		int val = 0;
		for(int i = 0; i<cn.getLet().length();i++) {
			val *= 27;
			val += cn.getLet().charAt(i) - 'A' + 1;
		}

		val += (Integer)param1.getValeur();
		
		String res = "";
		char l;
		
		while(val>0) {
			if(val%27==0 && 0>(Integer)param1.getValeur()) val--;
			if(val%27==0 && 0<(Integer)param1.getValeur()) val++;
			l = (char) ('A' + val%27 - 1);
			res = l+res;
			val/=27;
		}
		int nb = cn.getNum()+(Integer)param2.getValeur();
		Cellule c;
		if((c=cells.getCellule(res+nb))!=null && c.getValeur()!=null) return c.getValeur();
		return new CellString("!ERREUR!");
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
	
	private CellValeur evaluer_fct_min(String param) {
		String[] cs = param.split(":");
		Cellule c1 = cells.getCellule(cs[0]);
		Cellule c2 = cells.getCellule(cs[1]);
		int baseNum = java.lang.Math.min(c1.getNom().getNum(), c2.getNom().getNum());
		int maxNum = java.lang.Math.max(c1.getNom().getNum(),c2.getNom().getNum());
		int lval1 = 0;
		for(int i = 0; i<c1.getNom().getLet().length();i++) {
			lval1 *= 27;
			lval1 += c1.getNom().getLet().charAt(i) - 'A' + 1;
		}
		int lval2 = 0;
		for(int i = 0; i<c2.getNom().getLet().length();i++) {
			lval2 *= 27;
			lval2 += c2.getNom().getLet().charAt(i) - 'A' + 1;
		}
		int baseLet = java.lang.Math.min(lval1,lval2);
		int maxLet = java.lang.Math.max(lval1,lval2);
		CellValeur res = null;
		CellValeur tmp;
		int lval;
		String let;
		char l;
		for(int j=baseLet;j<=maxLet;j++) {
			lval = j;
			let="";
			while(lval>0) {
				if(!(lval%27 == 0)) {
					l = (char) ('A' + lval%27 - 1);
					let = l+let;
					lval/=27;
				}else {
					let="";
					break;
				}
			}
			if(!let.equals("")) {
				for(int i=baseNum;i<=maxNum;i++) {
					if((tmp=cells.getCellule(let+i).getValeur())!=null) {
						if(res!=null) {
							tmp = evaluer_fct_min(res,tmp);
							if(!(tmp.getValeur().getClass()==String.class)) {
								res = tmp;
							}
						}else {
							if(!(tmp.getValeur().getClass()==String.class)) {
								res = tmp;
							}
						}
					}
				}
			}
		}
		if(res==null) return new CellString("!ERREUR!");
		return res;
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
	
	private CellValeur evaluer_fct_max(String param) {
		String[] cs = param.split(":");
		Cellule c1 = cells.getCellule(cs[0]);
		Cellule c2 = cells.getCellule(cs[1]);
		int baseNum = java.lang.Math.min(c1.getNom().getNum(), c2.getNom().getNum());
		int maxNum = java.lang.Math.max(c1.getNom().getNum(),c2.getNom().getNum());
		int lval1 = 0;
		for(int i = 0; i<c1.getNom().getLet().length();i++) {
			lval1 *= 27;
			lval1 += c1.getNom().getLet().charAt(i) - 'A' + 1;
		}
		int lval2 = 0;
		for(int i = 0; i<c2.getNom().getLet().length();i++) {
			lval2 *= 27;
			lval2 += c2.getNom().getLet().charAt(i) - 'A' + 1;
		}
		int baseLet = java.lang.Math.min(lval1,lval2);
		int maxLet = java.lang.Math.max(lval1,lval2);
		CellValeur res = null;
		CellValeur tmp;
		int lval;
		String let;
		char l;
		for(int j=baseLet;j<=maxLet;j++) {
			lval = j;
			let="";
			while(lval>0) {
				if(!(lval%27 == 0)) {
					l = (char) ('A' + lval%27 - 1);
					let = l+let;
					lval/=27;
				}else {
					let="";
					break;
				}
			}
			if(!let.equals("")) {
				for(int i=baseNum;i<=maxNum;i++) {
					if((tmp=cells.getCellule(let+i).getValeur())!=null) {
						if(res!=null) {
							tmp = evaluer_fct_max(res,tmp);
							if(!(tmp.getValeur().getClass()==String.class)) {
								res = tmp;
							}
						}else {
							if(!(tmp.getValeur().getClass()==String.class)) {
								res = tmp;
							}
						}
					}
				}
			}
		}
		if(res==null) return new CellString("!ERREUR!");
		return res;
	}	
	
	private CellValeur evaluer_fct_add(CellValeur param1, CellValeur param2) {
		if(param1 == null || param2 == null || param1.getValeur().getClass()==String.class || param2.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param1.getValeur().getClass()==Integer.class) {
			if(param2.getValeur().getClass()==Integer.class) {
				return new CellInt(((Integer)(param1.getValeur())).intValue()+((Integer)(param2.getValeur())).intValue());
			}else {
				return new CellDouble(((Integer)(param1.getValeur())).doubleValue()+((Double)(param2.getValeur())).doubleValue());
			}
		}else {
			if(param2.getValeur().getClass()==Integer.class) {
				return new CellDouble(((Double)(param1.getValeur())).doubleValue()+((Integer)(param2.getValeur())).doubleValue());
			}else {
				return new CellDouble(((Double)(param1.getValeur())).doubleValue()+((Double)(param2.getValeur())).doubleValue());
			}
		}
	}
	
	private CellValeur evaluer_fct_add(String param) {
		String[] cs = param.split(":");
		Cellule c1 = cells.getCellule(cs[0]);
		Cellule c2 = cells.getCellule(cs[1]);
		int baseNum = java.lang.Math.min(c1.getNom().getNum(), c2.getNom().getNum());
		int maxNum = java.lang.Math.max(c1.getNom().getNum(),c2.getNom().getNum());
		int lval1 = 0;
		for(int i = 0; i<c1.getNom().getLet().length();i++) {
			lval1 *= 27;
			lval1 += c1.getNom().getLet().charAt(i) - 'A' + 1;
		}
		int lval2 = 0;
		for(int i = 0; i<c2.getNom().getLet().length();i++) {
			lval2 *= 27;
			lval2 += c2.getNom().getLet().charAt(i) - 'A' + 1;
		}
		int baseLet = java.lang.Math.min(lval1,lval2);
		int maxLet = java.lang.Math.max(lval1,lval2);
		CellValeur res = null;
		CellValeur tmp;
		int lval;
		String let;
		char l;
		for(int j=baseLet;j<=maxLet;j++) {
			lval = j;
			let="";
			while(lval>0) {
				if(!(lval%27 == 0)) {
					l = (char) ('A' + lval%27 - 1);
					let = l+let;
					lval/=27;
				}else {
					let="";
					break;
				}
			}
			if(!let.equals("")) {
				for(int i=baseNum;i<=maxNum;i++) {
					if((tmp=cells.getCellule(let+i).getValeur())!=null) {
						if(res!=null) {
							tmp = evaluer_fct_add(res,tmp);
							if(!(tmp.getValeur().getClass()==String.class)) {
								res = tmp;
							}
						}else {
							if(!(tmp.getValeur().getClass()==String.class)) {
								res = tmp;
							}
						}
					}
				}
			}
		}
		if(res==null) return new CellString("!ERREUR!");
		return res;
	}
	
	private CellValeur evaluer_fct_sou(CellValeur param1, CellValeur param2) {
		if(param1 == null || param2 == null || param1.getValeur().getClass()==String.class || param2.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param1.getValeur().getClass()==Integer.class) {
			if(param2.getValeur().getClass()==Integer.class) {
				return new CellInt(((Integer)(param1.getValeur())).intValue()-((Integer)(param2.getValeur())).intValue());
			}else {
				return new CellDouble(((Integer)(param1.getValeur())).doubleValue()-((Double)(param2.getValeur())).doubleValue());
			}
		}else {
			if(param2.getValeur().getClass()==Integer.class) {
				return new CellDouble(((Double)(param1.getValeur())).doubleValue()-((Integer)(param2.getValeur())).doubleValue());
			}else {
				return new CellDouble(((Double)(param1.getValeur())).doubleValue()-((Double)(param2.getValeur())).doubleValue());
			}
		}
	}
	
	private CellValeur evaluer_fct_mul(CellValeur param1, CellValeur param2) {
		if(param1 == null || param2 == null || param1.getValeur().getClass()==String.class || param2.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param1.getValeur().getClass()==Integer.class) {
			if(param2.getValeur().getClass()==Integer.class) {
				return new CellInt(((Integer)(param1.getValeur())).intValue()*((Integer)(param2.getValeur())).intValue());
			}else {
				return new CellDouble(((Integer)(param1.getValeur())).doubleValue()*((Double)(param2.getValeur())).doubleValue());
			}
		}else {
			if(param2.getValeur().getClass()==Integer.class) {
				return new CellDouble(((Double)(param1.getValeur())).doubleValue()*((Integer)(param2.getValeur())).doubleValue());
			}else {
				return new CellDouble(((Double)(param1.getValeur())).doubleValue()*((Double)(param2.getValeur())).doubleValue());
			}
		}
	}
	
	private CellValeur evaluer_fct_div(CellValeur param1, CellValeur param2) {
		if(param1 == null || param2 == null || param1.getValeur().getClass()==String.class || param2.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param1.getValeur().getClass()==Integer.class) {
			if(param2.getValeur().getClass()==Integer.class) {
				double d = ((Integer)(param1.getValeur())).doubleValue()/((Integer)(param2.getValeur())).doubleValue();
				int i = ((Integer)(param1.getValeur())).intValue()/((Integer)(param2.getValeur())).intValue();
				if(d==i) return new CellInt(i);
				return new CellDouble(d);
			}else {
				return new CellDouble(((Integer)(param1.getValeur())).doubleValue()/((Double)(param2.getValeur())).doubleValue());
			}
		}else {
			if(param2.getValeur().getClass()==Integer.class) {
				return new CellDouble(((Double)(param1.getValeur())).doubleValue()/((Integer)(param2.getValeur())).doubleValue());
			}else {
				return new CellDouble(((Double)(param1.getValeur())).doubleValue()/((Double)(param2.getValeur())).doubleValue());
			}
		}
	}
	
	private CellValeur evaluer_fct_PI() {
		return new CellDouble(java.lang.Math.PI);
	}
	
	private CellValeur evaluer_fct_E() {
		return new CellDouble(java.lang.Math.E);
	}
	
	private CellValeur evaluer_fct_rand() {
		return new CellDouble(java.lang.Math.random());
	}
	
	private CellValeur evaluer_fct_rand(CellValeur param) {
		if(param == null || param.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param.getValeur().getClass()==Integer.class) {
			Double d = java.lang.Math.random()*((Integer)(param.getValeur())).doubleValue();
			int i = d.intValue();
			if(d==i) {
				return new CellInt(i);
			}
			return new CellDouble(d);
		}else {
			Double d = java.lang.Math.random()*((Double)(param.getValeur())).doubleValue();
			int i = d.intValue();
			if(d==i) {
				return new CellInt(i);
			}
			return new CellDouble(d);
		}
	}
	
	private CellValeur evaluer_fct_rand(CellValeur param1, CellValeur param2) {
		if(param1 == null || param1.getValeur().getClass()==String.class || param2 == null || param2.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param1.getValeur().getClass()==Integer.class) {
			if(param2.getValeur().getClass()==Integer.class) {
				Double d = java.lang.Math.random()*(((Integer)(param2.getValeur())).doubleValue()-((Integer)(param1.getValeur())).doubleValue())+((Integer)(param1.getValeur())).doubleValue();
				int i = d.intValue();
				if(d==i) {
					return new CellInt(i);
				}
				return new CellDouble(d);
			}else {
				Double d = java.lang.Math.random()*(((Double)(param2.getValeur())).doubleValue()-((Integer)(param1.getValeur())).doubleValue())+((Integer)(param1.getValeur())).doubleValue();
				int i = d.intValue();
				if(d==i) {
					return new CellInt(i);
				}
				return new CellDouble(d);
			}
		}else {
			if(param1.getValeur().getClass()==Integer.class) {
				Double d = java.lang.Math.random()*(((Integer)(param2.getValeur())).doubleValue()-((Double)(param1.getValeur())).doubleValue())+((Double)(param1.getValeur())).doubleValue();
				int i = d.intValue();
				if(d==i) {
					return new CellInt(i);
				}
				return new CellDouble(d);
			}else {
				Double d = java.lang.Math.random()*(((Double)(param2.getValeur())).doubleValue()-((Double)(param1.getValeur())).doubleValue())+((Double)(param1.getValeur())).doubleValue();
				int i = d.intValue();
				if(d==i) {
					return new CellInt(i);
				}
				return new CellDouble(d);
			}
		}
	}
	
	private CellValeur evaluer_fct_rond(CellValeur param) {
		if(param == null || param.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param.getValeur().getClass()==Integer.class) {
			return param;
		}else {
			return new CellInt((int)(java.lang.Math.round((Double)(param.getValeur()))));
		}
	}
	
	private CellValeur evaluer_fct_abs(CellValeur param) {
		if(param == null || param.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param.getValeur().getClass()==Integer.class) {
			return new CellInt(java.lang.Math.abs((Integer)(param.getValeur())));
		}else {
			return new CellDouble(java.lang.Math.abs((Double)(param.getValeur())));
		}
	}
	
	private CellValeur evaluer_fct_acos(CellValeur param) {
		if(param == null || param.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param.getValeur().getClass()==Integer.class) {
			if(((Integer)(param.getValeur())).doubleValue()>1 || ((Integer)(param.getValeur())).doubleValue() <-1) return new CellString("!ERREUR!");
			return new CellDouble(java.lang.Math.acos(((Integer)(param.getValeur())).doubleValue()));
		}else {
			if(((Double)(param.getValeur())).doubleValue()>1 || ((Double)(param.getValeur())).doubleValue() <-1) return new CellString("!ERREUR!");
			return new CellDouble(java.lang.Math.acos((Double)(param.getValeur())));
		}
	}
	
	private CellValeur evaluer_fct_asin(CellValeur param) {
		if(param == null || param.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param.getValeur().getClass()==Integer.class) {
			if(((Integer)(param.getValeur())).doubleValue()>1 || ((Integer)(param.getValeur())).doubleValue() <-1) return new CellString("!ERREUR!");
			return new CellDouble(java.lang.Math.asin(((Integer)(param.getValeur())).doubleValue()));
		}else {
			if(((Double)(param.getValeur())).doubleValue()>1 || ((Double)(param.getValeur())).doubleValue() <-1) return new CellString("!ERREUR!");
			return new CellDouble(java.lang.Math.asin((Double)(param.getValeur())));
		}
	}
	
	private CellValeur evaluer_fct_atan(CellValeur param) {
		if(param == null || param.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param.getValeur().getClass()==Integer.class) {
			return new CellDouble(java.lang.Math.atan(((Integer)(param.getValeur())).doubleValue()));
		}else {
			return new CellDouble(java.lang.Math.atan((Double)(param.getValeur())));
		}
	}
	
	private CellValeur evaluer_fct_cos(CellValeur param) {
		if(param == null || param.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param.getValeur().getClass()==Integer.class) {
			return new CellDouble(java.lang.Math.cos(((Integer)(param.getValeur())).doubleValue()));
		}else {
			return new CellDouble(java.lang.Math.cos((Double)(param.getValeur())));
		}
	}
	
	private CellValeur evaluer_fct_sin(CellValeur param) {
		if(param == null || param.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param.getValeur().getClass()==Integer.class) {
			return new CellDouble(java.lang.Math.sin(((Integer)(param.getValeur())).doubleValue()));
		}else {
			return new CellDouble(java.lang.Math.sin((Double)(param.getValeur())));
		}
	}
	
	private CellValeur evaluer_fct_tan(CellValeur param) {
		if(param == null || param.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param.getValeur().getClass()==Integer.class) {
			return new CellDouble(java.lang.Math.tan(((Integer)(param.getValeur())).doubleValue()));
		}else {
			return new CellDouble(java.lang.Math.tan((Double)(param.getValeur())));
		}
	}
	
	private CellValeur evaluer_fct_exp(CellValeur param) {
		if(param == null || param.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param.getValeur().getClass()==Integer.class) {
			Double d = java.lang.Math.exp(((Integer)(param.getValeur())).doubleValue());
			int i = d.intValue();
			if(d==i) {
				return new CellInt(i);
			}
			return new CellDouble(d);
		}else {
			Double d = java.lang.Math.exp(((Double)(param.getValeur())));
			int i = d.intValue();
			if(d==i) {
				return new CellInt(i);
			}
			return new CellDouble(d);
		}
	}
	
	private CellValeur evaluer_fct_ln(CellValeur param) {
		if(param == null || param.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param.getValeur().getClass()==Integer.class) {
			if(((Integer)(param.getValeur())).doubleValue()<=0) return new CellString("!ERREUR!");
			Double d = java.lang.Math.log(((Integer)(param.getValeur())).doubleValue());
			int i = d.intValue();
			if(d==i) {
				return new CellInt(i);
			}
			return new CellDouble(d);
		}else {
			if(((Double)(param.getValeur())).doubleValue()<=0) return new CellString("!ERREUR!");
			Double d = java.lang.Math.log(((Double)(param.getValeur())));
			int i = d.intValue();
			if(d==i) {
				return new CellInt(i);
			}
			return new CellDouble(d);
		}
	}
	
	private CellValeur evaluer_fct_log(CellValeur param) {
		if(param == null || param.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param.getValeur().getClass()==Integer.class) {
			if(((Integer)(param.getValeur())).doubleValue()<=0) return new CellString("!ERREUR!");
			Double d = java.lang.Math.log10(((Integer)(param.getValeur())).doubleValue());
			int i = d.intValue();
			if(d==i) {
				return new CellInt(i);
			}
			return new CellDouble(d);
		}else {
			if(((Double)(param.getValeur())).doubleValue()<=0) return new CellString("!ERREUR!");
			Double d = java.lang.Math.log10(((Double)(param.getValeur())));
			int i = d.intValue();
			if(d==i) {
				return new CellInt(i);
			}
			return new CellDouble(d);
		}
	}
	
	private CellValeur evaluer_fct_pow(CellValeur param1, CellValeur param2) {
		if(param1 == null || param2 == null || param1.getValeur().getClass()==String.class || param2.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param1.getValeur().getClass()==Integer.class) {
			if(param2.getValeur().getClass()==Integer.class) {
				Double d = java.lang.Math.pow(((Integer)(param1.getValeur())).doubleValue(),((Integer)(param2.getValeur())).doubleValue());
				int i = d.intValue();
				if(d==i) return new CellInt(i);
				return new CellDouble(d);
			}else {
				Double d = java.lang.Math.pow(((Integer)(param1.getValeur())).doubleValue(),((Double)(param2.getValeur())).doubleValue());
				int i = d.intValue();
				if(d==i) return new CellInt(i);
				return new CellDouble(d);
			}
		}else {
			if(param2.getValeur().getClass()==Integer.class) {
				Double d = java.lang.Math.pow(((Double)(param1.getValeur())).doubleValue(),((Integer)(param2.getValeur())).doubleValue());
				int i = d.intValue();
				if(d==i) return new CellInt(i);
				return new CellDouble(d);			
			}else {
				Double d = java.lang.Math.pow(((Double)(param1.getValeur())).doubleValue(),((Double)(param2.getValeur())).doubleValue());
				int i = d.intValue();
				if(d==i) return new CellInt(i);
				return new CellDouble(d);
			}
		}
	}
	
	private CellValeur evaluer_fct_ent(CellValeur param) {
		if(param == null || param.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param.getValeur().getClass()==Integer.class) {
			return new CellInt((int)(java.lang.Math.floor(((Integer)(param.getValeur())).doubleValue())));
		}else {
			return new CellInt((int)(java.lang.Math.floor((Double)(param.getValeur()))));
		}
	}
	
	private CellValeur evaluer_fct_sqrt(CellValeur param) {
		if(param == null || param.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param.getValeur().getClass()==Integer.class) {
			if(((Integer)(param.getValeur())).doubleValue()<0) return new CellString("!ERREUR!");
			Double d = java.lang.Math.sqrt(((Integer)(param.getValeur())).doubleValue());
			int i = d.intValue();
			if(d==i) {
				return new CellInt(i);
			}
			return new CellDouble(d);
		}else {
			if(((Double)(param.getValeur())).doubleValue()<0) return new CellString("!ERREUR!");
			return new CellDouble(java.lang.Math.atan((Double)(param.getValeur())));
		}
	}
	
	private CellValeur evaluer_fct_cbrt(CellValeur param) {
		if(param == null || param.getValeur().getClass()==String.class) return new CellString("!ERREUR!");
		if(param.getValeur().getClass()==Integer.class) {
			Double d = java.lang.Math.cbrt(((Integer)(param.getValeur())).doubleValue());
			int i = d.intValue();
			if(d==i) {
				return new CellInt(i);
			}
			return new CellDouble(d);
		}else {
			return new CellDouble(java.lang.Math.atan((Double)(param.getValeur())));
		}
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
					return evaluer(cellId,param.substring(begin, i));
				}else {
					nParam++;
					begin=i+1;
				}
			}
			if(nb<0) return new CellString("!ERREUR!");
		}
		return evaluer(cellId,param.substring(begin));
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
	
	private boolean evaluer_isList(String param) {
		if(param.contains(":")) {
			String[] res = param.split(":");
			if(res.length==2) {
				if(cells.getCellule(res[0])!=null && cells.getCellule(res[1])!=null) return true;
			}
		}
		return false;
	}
	
	private boolean evaluer_paramAtIsList(String param,int n) {
		int nb = 0;
		int nParam = 0;
		int begin = 0;
		for(int i=0;i<param.length();i++) {
			if(param.charAt(i)=='(') nb++;
			if(param.charAt(i)==')') nb--;
			if(param.charAt(i)==',' && nb==0) {
				if(nParam==n) {
					return evaluer_isList(param.substring(begin, i));
				}else {
					nParam++;
					begin=i+1;
				}
			}
			if(nb<0) return false;
		}
		return evaluer_isList(param.substring(begin));
	}
	
	private CellValeur evaluer_call(String name, String param) {
		switch(name) {
		default:
			return new CellString("!ERREUR!");
		case "cell":
			if(evaluer_getNbParam(param)==2 && !evaluer_paramAtIsList(param,0) && !evaluer_paramAtIsList(param,1)) {
				return evaluer_fct_cell(evaluer_getParamAt(param,0),evaluer_getParamAt(param,1));
			}else {
				return new CellString("!ERREUR!");
			}
		case "min":
			if(evaluer_getNbParam(param)==2 && !evaluer_paramAtIsList(param,0) && !evaluer_paramAtIsList(param,1)) {
				return evaluer_fct_min(evaluer_getParamAt(param,0),evaluer_getParamAt(param,1));
			}else {
				if(evaluer_getNbParam(param)==1 && evaluer_paramAtIsList(param,0)) {
					return evaluer_fct_min(param);
				}else {
					return new CellString("!ERREUR!");
				}
			}
		case "max":
			if(evaluer_getNbParam(param)==2 && !evaluer_paramAtIsList(param,0) && !evaluer_paramAtIsList(param,1)) {
				return evaluer_fct_max(evaluer_getParamAt(param,0),evaluer_getParamAt(param,1));
			}else {
				if(evaluer_getNbParam(param)==1 && evaluer_paramAtIsList(param,0)) {
					return evaluer_fct_max(param);
				}else {
					return new CellString("!ERREUR!");
				}
			}
		case "add":
			if(evaluer_getNbParam(param)==2 && !evaluer_paramAtIsList(param,0) && !evaluer_paramAtIsList(param,1)) {
				return evaluer_fct_add(evaluer_getParamAt(param,0),evaluer_getParamAt(param,1));
			}else {
				if(evaluer_getNbParam(param)==1 && evaluer_paramAtIsList(param,0)) {
					return evaluer_fct_add(param);
				}else {
					return new CellString("!ERREUR!");
				}
			}
		case "sou":
			if(evaluer_getNbParam(param)==2 && !evaluer_paramAtIsList(param,0) && !evaluer_paramAtIsList(param,1)) {
				return evaluer_fct_min(evaluer_getParamAt(param,0),evaluer_getParamAt(param,1));
			}else {
				return new CellString("!ERREUR!");
			}
		case "mul":
			if(evaluer_getNbParam(param)==2 && !evaluer_paramAtIsList(param,0) && !evaluer_paramAtIsList(param,1)) {
				return evaluer_fct_min(evaluer_getParamAt(param,0),evaluer_getParamAt(param,1));
			}else {
				return new CellString("!ERREUR!");
			}
		case "div":
			if(evaluer_getNbParam(param)==2 && !evaluer_paramAtIsList(param,0) && !evaluer_paramAtIsList(param,1)) {
				return evaluer_fct_min(evaluer_getParamAt(param,0),evaluer_getParamAt(param,1));
			}else {
				return new CellString("!ERREUR!");
			}
		case "E":
			if(evaluer_getNbParam(param)==0) {
				return evaluer_fct_E();
			}else {
				return new CellString("!ERREUR!");
			}
		case "PI":
			if(evaluer_getNbParam(param)==0) {
				return evaluer_fct_PI();
			}else {
				return new CellString("!ERREUR!");
			}
		case "rand":
			if(evaluer_getNbParam(param)==0) {
				return evaluer_fct_rand();
			}else {
				if(evaluer_getNbParam(param)==1 && !evaluer_paramAtIsList(param,0)) {
					return evaluer_fct_rand(evaluer_getParamAt(param,0));
				}else {
					if(evaluer_getNbParam(param)==2 && !evaluer_paramAtIsList(param,0) && !evaluer_paramAtIsList(param,1)) {
						return evaluer_fct_rand(evaluer_getParamAt(param,0),evaluer_getParamAt(param,1));
					}else {
						return new CellString("!ERREUR!");
					}
				}
			}
		case "rond":
			if(evaluer_getNbParam(param)==1 && !evaluer_paramAtIsList(param,0)) {
				return evaluer_fct_rond(evaluer_getParamAt(param,0));
			}else {
				return new CellString("!ERREUR!");
			}
		case "abs":
			if(evaluer_getNbParam(param)==1 && !evaluer_paramAtIsList(param,0)) {
				return evaluer_fct_abs(evaluer_getParamAt(param,0));
			}else {
				return new CellString("!ERREUR!");
			}
		case "acos":
			if(evaluer_getNbParam(param)==1 && !evaluer_paramAtIsList(param,0)) {
				return evaluer_fct_acos(evaluer_getParamAt(param,0));
			}else {
				return new CellString("!ERREUR!");
			}
		case "asin":
			if(evaluer_getNbParam(param)==1 && !evaluer_paramAtIsList(param,0)) {
				return evaluer_fct_asin(evaluer_getParamAt(param,0));
			}else {
				return new CellString("!ERREUR!");
			}
		case "atan":
			if(evaluer_getNbParam(param)==1 && !evaluer_paramAtIsList(param,0)) {
				return evaluer_fct_atan(evaluer_getParamAt(param,0));
			}else {
				return new CellString("!ERREUR!");
			}
		case "cos":
			if(evaluer_getNbParam(param)==1 && !evaluer_paramAtIsList(param,0)) {
				return evaluer_fct_cos(evaluer_getParamAt(param,0));
			}else {
				return new CellString("!ERREUR!");
			}
		case "sin":
			if(evaluer_getNbParam(param)==1 && !evaluer_paramAtIsList(param,0)) {
				return evaluer_fct_sin(evaluer_getParamAt(param,0));
			}else {
				return new CellString("!ERREUR!");
			}
		case "tan":
			if(evaluer_getNbParam(param)==1 && !evaluer_paramAtIsList(param,0)) {
				return evaluer_fct_tan(evaluer_getParamAt(param,0));
			}else {
				return new CellString("!ERREUR!");
			}
		case "exp":
			if(evaluer_getNbParam(param)==1 && !evaluer_paramAtIsList(param,0)) {
				return evaluer_fct_exp(evaluer_getParamAt(param,0));
			}else {
				return new CellString("!ERREUR!");
			}
		case "ln":
			if(evaluer_getNbParam(param)==1 && !evaluer_paramAtIsList(param,0)) {
				return evaluer_fct_ln(evaluer_getParamAt(param,0));
			}else {
				return new CellString("!ERREUR!");
			}
		case "log":
			if(evaluer_getNbParam(param)==1 && !evaluer_paramAtIsList(param,0)) {
				return evaluer_fct_log(evaluer_getParamAt(param,0));
			}else {
				return new CellString("!ERREUR!");
			}
		case "pow":
			if(evaluer_getNbParam(param)==2 && !evaluer_paramAtIsList(param,0) && !evaluer_paramAtIsList(param,1)) {
				return evaluer_fct_pow(evaluer_getParamAt(param,0),evaluer_getParamAt(param,1));
			}else {
				return new CellString("!ERREUR!");
			}
		case "ent":
			if(evaluer_getNbParam(param)==1 && !evaluer_paramAtIsList(param,0)) {
				return evaluer_fct_ent(evaluer_getParamAt(param,0));
			}else {
				return new CellString("!ERREUR!");
			}
		case "sqrt":
			if(evaluer_getNbParam(param)==1 && !evaluer_paramAtIsList(param,0)) {
				return evaluer_fct_sqrt(evaluer_getParamAt(param,0));
			}else {
				return new CellString("!ERREUR!");
			}
		case "cbrt":
			if(evaluer_getNbParam(param)==1 && !evaluer_paramAtIsList(param,0)) {
				return evaluer_fct_cbrt(evaluer_getParamAt(param,0));
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
	
	public CellValeur evaluer(String cellId, String contenuCellule){
		this.cellId = cellId;
		if(contenuCellule==null) return new CellString("!ERREUR!");
		MutableInt p = new MutableInt();
		CellValeur res = null;
		int tmp = 0;
		if(Character.isDigit(contenuCellule.charAt(p.intValue())) || contenuCellule.charAt(p.intValue())=='-' || contenuCellule.charAt(p.intValue())=='+') {
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
							return new CellString("!ERREUR!");
						}else {
							res = evaluer(cellId,contenuCellule.substring(p.intValue()+1, tmp));
							p.add(1+tmp-p.intValue());
						}
					}else {
						return new CellString("!ERREUR!");
					}
				}
			}
		}
		
		ArrayList<CellValeur> l = new ArrayList<CellValeur>();
		ArrayList<Character> o = new ArrayList<Character>();
		
		l.add(res);
		
		while(p.intValue()<contenuCellule.length()) {
			if(contenuCellule.charAt(p.intValue())=='+' || contenuCellule.charAt(p.intValue())=='-' || contenuCellule.charAt(p.intValue())=='*' || contenuCellule.charAt(p.intValue())=='/'){
				o.add(contenuCellule.charAt(p.intValue()));
			}else {
				return new CellString("!ERREUR!");
			}
			p.add(1);
			if(p.intValue()<contenuCellule.length()) {
				if(Character.isDigit(contenuCellule.charAt(p.intValue())) || contenuCellule.charAt(p.intValue())=='-' || contenuCellule.charAt(p.intValue())=='+') {
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
									return new CellString("!ERREUR!");
								}else {
									res = evaluer(cellId,contenuCellule.substring(p.intValue()+1, tmp));
									p.add(1+tmp-p.intValue());
								}
							}else {
								return new CellString("!ERREUR!");
							}
						}
					}
				}
			}else {
				return new CellString("!ERREUR!");
			}
			l.add(res);
		}
		
		boolean flag;
		
		tmp = 0;
		do {
			flag = false;
			for(int i=tmp; i<o.size();i++){
	            switch(o.get(i)) {
	            case '*':
	            	flag = true;
	            	tmp = i;
	            	res = evaluer_fct_mul(l.get(i),l.get(i+1));
	            	break;
	            case '/':
	            	flag = true;
	            	tmp = i;
	            	res = evaluer_fct_div(l.get(i),l.get(i+1));
	            	break;
	            }
	            if(flag) break;
	        }
			if(flag) {
				l.remove(tmp);
            	l.remove(tmp);
            	l.add(tmp,res);
            	o.remove(tmp);
			}
		}while(flag);
		
		tmp = 0;
		do {
			flag = false;
			for(int i=tmp; i<o.size();i++){
	            switch(o.get(i)) {
	            case '+':
	            	flag = true;
	            	tmp = i;
	            	res = evaluer_fct_add(l.get(i),l.get(i+1));
	            	break;
	            case '-':
	            	flag = true;
	            	tmp = i;
	            	res = evaluer_fct_sou(l.get(i),l.get(i+1));
	            	break;
	            }
	            if(flag) break;
	        }
			if(flag) {
				l.remove(tmp);
            	l.remove(tmp);
            	l.add(tmp,res);
            	o.remove(tmp);
			}
		}while(flag);
		
		return l.get(0);
	}
	
	private String transformer_lettres(String b, String c, String a) {
		int bval = 0;
		int cval = 0;
		int aval = 0;
		
		int tmp;
		
		for(int i = 0; i<b.length();i++) {
			bval *= 27;
			bval += b.charAt(i) - 'A' + 1;
		}
		
		for(int i = 0; i<c.length();i++) {
			cval *= 27;
			cval += c.charAt(i) - 'A' + 1;
		}
		
		for(int i = 0; i<a.length();i++) {
			aval *= 27;
			aval += a.charAt(i) - 'A' + 1;
		}
		
		tmp = cval - bval + aval;
		
		String res = "";
		char l;
		
		while(tmp>0) {
			if(tmp%27==0 && cval - bval <0) tmp--;
			if(tmp%27==0 && cval - bval >0) tmp++;
			l = (char) ('A' + tmp%27 - 1);
			res = l+res;
			tmp/=27;
		}
		
		return res;
	}
	
	private Integer transformer_chiffres(Integer b, Integer c, Integer a) {
		int res = c - b + a;
		if(res>0) return res;
		return -1;
	}
	
	public String transformer(String contenuCelluleB, String nomCelluleB, String nomCelluleC) {
		String res = "";
		String contenuCelluleC = "";
		Cellule a;
		Cellule b = cells.getCellule(nomCelluleB);
		Cellule c = cells.getCellule(nomCelluleC);
		if(b==null || c== null) return "";
		for(int i=0;i<contenuCelluleB.length();i++) {
			if(Character.isLetter(contenuCelluleB.charAt(i)) || Character.isDigit(contenuCelluleB.charAt(i))) {
				res += contenuCelluleB.charAt(i);
			}else {
				if((a=cells.getCellule(res))!=null) {
					
					res = transformer_lettres(b.getNom().getLet(),c.getNom().getLet(),a.getNom().getLet());
					res += transformer_chiffres(b.getNom().getNum(),c.getNom().getNum(),a.getNom().getNum());
					if(cells.getCellule(res)!=null) {
						contenuCelluleC += res;
					}else {
						return "";
					}
				}else {
					contenuCelluleC += res;
				}
				res = "";
				contenuCelluleC+=contenuCelluleB.charAt(i);
			}
		}
		if((a=cells.getCellule(res))!=null) {
			res = transformer_lettres(b.getNom().getLet(),c.getNom().getLet(),a.getNom().getLet());
			res += transformer_chiffres(b.getNom().getNum(),c.getNom().getNum(),a.getNom().getNum());
			if(cells.getCellule(res)!=null) {
				contenuCelluleC += res;
			}else {
				return "";
			}
		}else {
			contenuCelluleC += res;
		}
		return contenuCelluleC;
	}
	
	public boolean isDependante(String contenuCelluleB, String nomCelluleC) {
		String res = "";
		Cellule c;
		boolean flag = false;
		for(int i=0;i<contenuCelluleB.length();i++) {
			if(Character.isLetter(contenuCelluleB.charAt(i)) || Character.isDigit(contenuCelluleB.charAt(i))) {
				res += contenuCelluleB.charAt(i);
			}else {
				if(contenuCelluleB.charAt(i)==':' && !flag) {
					res+=contenuCelluleB.charAt(i);
					flag=true;
				}else {
					if(res.equals(nomCelluleC)) {
						return true;
					}else {
						if(flag) {
							String[] cs = res.split(":");
							if(cs.length==2) {
								Cellule c1 = cells.getCellule(cs[0]);
								Cellule c2 = cells.getCellule(cs[1]);
								if(c1!=null && c2!=null) {
									int baseNum = java.lang.Math.min(c1.getNom().getNum(), c2.getNom().getNum());
									int maxNum = java.lang.Math.max(c1.getNom().getNum(),c2.getNom().getNum());
									int lval1 = 0;
									for(int j = 0; j<c1.getNom().getLet().length();j++) {
										lval1 *= 27;
										lval1 += c1.getNom().getLet().charAt(j) - 'A' + 1;
									}
									int lval2 = 0;
									for(int j = 0; j<c2.getNom().getLet().length();j++) {
										lval2 *= 27;
										lval2 += c1.getNom().getLet().charAt(j) - 'A' + 1;
									}
									int baseLet = java.lang.Math.min(lval1,lval2);
									int maxLet = java.lang.Math.max(lval1,lval2);
									int lval;
									String let;
									char l;
									for(int j=baseLet;j<=maxLet;j++) {
										lval = j;
										let="";
										while(lval>0) {
											if(!(lval%27 == 0)) {
												l = (char) ('A' + lval%27 - 1);
												let = l+let;
												lval/=27;
											}else {
												let="";
												break;
											}
										}
										if(!let.equals("")) {
											for(int k=baseNum;k<=maxNum;k++) {
												if((c=cells.getCellule(let+k))!=null) {
													if(c.getNom().getFullName().equals(nomCelluleC) || isDependante(c.getContenu(),nomCelluleC)) return true;
												}
											}
										}
									}
								}
							}
						}else {
							if((c=cells.getCellule(res))!=null) {
								if(isDependante(c.getContenu(),nomCelluleC)) return true;
								res="";
							}else {
								res = "";
							}
						}
					}
					flag=false;
				}
			}
		}
		if(res.equals(nomCelluleC)) {
			return true;
		}else {
			if(flag) {
				String[] cs = res.split(":");
				if(cs.length==2) {
					Cellule c1 = cells.getCellule(cs[0]);
					Cellule c2 = cells.getCellule(cs[1]);
					if(c1!=null && c2!=null) {
						int baseNum = java.lang.Math.min(c1.getNom().getNum(), c2.getNom().getNum());
						int maxNum = java.lang.Math.max(c1.getNom().getNum(),c2.getNom().getNum());
						int lval1 = 0;
						for(int j = 0; j<c1.getNom().getLet().length();j++) {
							lval1 *= 27;
							lval1 += c1.getNom().getLet().charAt(j) - 'A' + 1;
						}
						int lval2 = 0;
						for(int j = 0; j<c2.getNom().getLet().length();j++) {
							lval2 *= 27;
							lval2 += c1.getNom().getLet().charAt(j) - 'A' + 1;
						}
						int baseLet = java.lang.Math.min(lval1,lval2);
						int maxLet = java.lang.Math.max(lval1,lval2);
						int lval;
						String let;
						char l;
						for(int j=baseLet;j<=maxLet;j++) {
							lval = j;
							let="";
							while(lval>0) {
								if(!(lval%27 == 0)) {
									l = (char) ('A' + lval%27 - 1);
									let = l+let;
									lval/=27;
								}else {
									let="";
									break;
								}
							}
							if(!let.equals("")) {
								for(int k=baseNum;k<=maxNum;k++) {
									if((c=cells.getCellule(let+k))!=null) {
										if(c.getNom().getFullName().equals(nomCelluleC) || isDependante(c.getContenu(),nomCelluleC)) return true;
									}
								}
							}
						}
					}
				}
			}else {
				if((c=cells.getCellule(res))!=null) {
					if(isDependante(c.getContenu(),nomCelluleC)) return true;
					res="";
				}else {
					res = "";
				}
			}
		}
		return false;
	}
	
	public void setCells(CellContainer cells) {
		this.cells = cells;
	}
}
