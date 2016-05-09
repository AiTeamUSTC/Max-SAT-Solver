package cs.ustc.MaxSATsolver;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * 存储cnf文件中读取的信息，及其相应的一些方法
 * @author ccding 
 * 2016年3月5日下午8:06:07
 */
public class IFormula {
	private  ArrayList<IClause> clauses; //所有的clauses
	private ILiteral[] literals; //formula的所有vars

	/**
	 * 设置vars和clauses的容量
	 * @param nbvars
	 * @param nbclauses
	 */
	public void setUniverse(int nbvars, int nbclauses) {
		literals = new ILiteral[nbvars];
		clauses = new ArrayList<>(nbclauses);
	}
	
	/**
	 * 通过读取的id创建literal
	 * @param i
	 * @return
	 */	
	public ILiteral getLiteral(int i) {
		ILiteral lit;
		int id = Math.abs(i) - 1; // maps from 1..n to 0..n-1
		if (literals[id] == null) {
			literals[id] = ILiteral.createLiteral(id + 1);
		}
		if (i > 0) {
			lit = literals[id];
		} else {
			lit = literals[id].opposite();
		}
		return lit;
	}
	
	/**
	 * 通过literals添加clause
	 * @param literals
	 */
	public void addClause(ArrayList<ILiteral> literals) {
		// create the clause
		IClause clause = new IClause(literals);
		clauses.add(clause);
		for (Iterator<ILiteral> it = literals.iterator(); it.hasNext();) {
			((ILiteral)it.next()).addClause(clause);
		}
	}
	
	/**
	 * get literals 
	 * @return literals
	 */
	public ILiteral[] getLiterals(){
		return literals;
	}
	
	/**
	 * get clauses	
	 * @return clauses
	 */
	public ArrayList<IClause> getClauses() {
		return this.clauses;
	}
	
}
