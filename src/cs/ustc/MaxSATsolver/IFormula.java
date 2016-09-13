package cs.ustc.MaxSATsolver;

import java.util.ArrayList;
import java.util.HashSet;
//import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;


/**
 * 存储cnf文件中读取的信息，及其相应的一些方法
 * @author ccding 
 * 2016年3月5日下午8:06:07
 */
public class IFormula {
	private  Set<IClause> clauses; //所有的clauses
	private ILiteral[] vars; //formula的所有vars
	private Set<ILiteral> literals;
	Set<IClause> unsatClas;
	int nbVar, nbClas;
	int unsatClasNum;
//	Hashtable<String, Integer> relateMat;
//	int [][] relateMat;

	/**
	 * 设置vars和clauses的容量
	 * @param nbvars
	 * @param nbclauses
	 */
	public void setUniverse(int nbvars, int nbclauses) {
		nbVar = nbvars;
		nbClas = nbclauses;
		vars = new ILiteral[nbvars];
		clauses = new HashSet<>(nbclauses);
		literals = new HashSet<>(nbvars*2);
		unsatClas = new HashSet<>();
//		relateMat = new Hashtable<>();
//		relateMat = new int[nbvars][nbvars];
	}
	
	/**
	 * 通过读取的id创建literal
	 * @param i
	 * @return
	 */	
	protected ILiteral getLiteral(int i) {
		ILiteral lit;
		int id = Math.abs(i) - 1; // maps from 1..n to 0..n-1
		if (vars[id] == null) {
			vars[id] = new ILiteral(id + 1);
		}
		if (i > 0) {
			lit = vars[id];
		} else {
			lit = vars[id].opposite();
		}
		return lit;
	}
	
	/**
	 * 通过vars添加clause
	 * @param vars
	 */
	public void addClause(ArrayList<ILiteral> vars) {
		// create the clause
		IClause clause = new IClause(vars);
		clauses.add(clause);
		for (Iterator<ILiteral> it = vars.iterator(); it.hasNext();) {
			((ILiteral)it.next()).addClause(clause);
		}
//		int rownb, colnb;
//		int tmp;
//		String subscript;
//		for (int i = 0; i < vars.size()-1; i++) {
//			for (int j = i+1; j < vars.size(); j++) {
//				rownb = Math.abs(vars.get(i).id)-1;
//				colnb = Math.abs(vars.get(j).id)-1;
//				subscript = rownb>colnb ? (colnb+" "+rownb):(rownb+" "+colnb);
//				if(relateMat.containsKey(subscript)){
//					tmp = relateMat.get(subscript);
//					relateMat.put(subscript, ++tmp);
//				}else{
//					relateMat.put(subscript, 1);
//				}
//				relateMat[rownb][colnb]++;
//				relateMat[colnb][rownb]++;
//			}
//		}
	}
	
	/**
	 * get vars 
	 * @return vars
	 */
	public ILiteral[] getvars(){
		return vars;
	}
	public Set<ILiteral> getLiterals() {
		return literals;
	}
	/**
	 * get clauses	
	 * @return clauses
	 */
	public Set<IClause> getClauses() {
		return this.clauses;
	}
	public void setLiterals(){
		for (int i = 0; i < vars.length; i++) {
			if(vars[i]!=null){
				literals.add(vars[i]);
				literals.add(vars[i].opposite);
			}
		}
	}
	
}
