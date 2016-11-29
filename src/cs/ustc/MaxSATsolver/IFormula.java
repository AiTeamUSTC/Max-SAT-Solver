package cs.ustc.MaxSATsolver;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


/**
 * 存储cnf文件中读取的信息，及其相应的一些方法
 * @author ccding 
 * 2016年3月5日下午8:06:07
 */
public class IFormula{
	List<IClause> clauses; //所有的clauses
	ILiteral[] vars; //formula的所有vars
	int nbVar, nbClas;
	List<IVariable> variables;
	Set<ILiteral> satLits;
	Set<IClause> satClas;
	Set<IClause> unsatClas;
	Set<ILiteral> unsatLits;
	List<IVariable> visitedVars;
	List<IVariable> unVisitedVars;
	int minUnsatNum;
	
	


	/**
	 * 设置vars和clauses的容量
	 * @param nbvars
	 * @param nbclauses
	 */
	public void init(int nbvars, int nbclauses) {
		nbVar = nbvars;
		nbClas = nbclauses;
		vars = new ILiteral[nbvars];
		clauses = new ArrayList<>(nbclauses);
		variables = new ArrayList<>(nbvars);
		satLits = new HashSet<>(nbvars*2);
		unsatLits = new HashSet<>(nbvars*2);
		satClas = new HashSet<>(nbclauses);
		unsatClas = new HashSet<>(nbclauses);
		visitedVars = new ArrayList<>(nbvars);
		unVisitedVars = new ArrayList<>(nbvars);
		minUnsatNum = nbclauses;
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
	 * 
	 *  初始化每个 variable 的邻居，并设置相应的 degree
	 */
	public void setVarsNeighbors(){
		IVariable tmp = null;
		for(IVariable var: variables){
			for(ILiteral lit: var.lit.neighbors){
				tmp = this.getVariable(lit);
				if(!var.neighbors.contains(tmp)){
					var.neighbors.add(tmp);
				}	
			}
			for(ILiteral lit: var.oppositeLit.neighbors){
				tmp = this.getVariable(lit);
				if(!var.neighbors.contains(tmp)){
					var.neighbors.add(tmp);
				}
			}
			var.initDegree = var.neighbors.size();
			var.degree = var.initDegree;

		}
		unVisitedVars.addAll(variables);
		
	}
	
	/**
	 * 
	 *  通过 lit 找到对应的 variable
	 * @param lit
	 * @return
	 */
	private IVariable getVariable(ILiteral lit){
		for(IVariable var: variables){
			if(lit.id == var.lit.id || lit.id == var.oppositeLit.id)
				return var;
		}
		return null;
	}
	
	/**
	 *  通过vars添加clause
	 * @param vars
	 */
	public void addClause(ArrayList<ILiteral> lits) {
		// create the clause
		IClause clause = new IClause(lits);
		clauses.add(clause);
		for (ILiteral lit : lits) {
			lit.addClause(clause);
			lit.neighbors.addAll(lits);
			lit.neighbors.remove(lit);
			lit.degree += lits.size()-1;
			lit.initDegree = lit.degree;
		} 
	}
	
	/**
	 *  set literals
	 */
	public void setVariables(){
		unsatClas.addAll(clauses);
		for (int i = 0; i < vars.length; i++) {
			if(vars[i]!=null){
				vars[i].unsatClas.addAll(vars[i].getClas());
				vars[i].opposite.unsatClas.addAll(vars[i].opposite.getClas());
				variables.add(new IVariable(vars[i]));
			}
		}
	}
	
	

	
	
	/**
	 * get independent set 
	 * first, find vertexes set covers all edges
	 * then, the complementary set of all vertexes is independent set
	 * @return independent set
	 */
	public List<IVariable> getIndependentGroup(double randomCoef){
		List<IVariable> tmp = new ArrayList<>(unVisitedVars);
		List<IVariable> independentSet = new ArrayList<>();
		IVariable var;
		while(!tmp.isEmpty()){
			if(Math.random() < randomCoef){
				var = Collections.min(tmp);
			}else{
				var = tmp.get((int)(Math.random() * tmp.size()));
			}
			independentSet.add(var);
			tmp.remove(var);
			tmp.removeAll(var.neighbors);
		}
		return independentSet;
	}
	
	
	
	public void removeGroupFromFormula(List<IVariable> group){
		unVisitedVars.removeAll(group);
		visitedVars.addAll(group);
	}
	
	synchronized public void increaseLitsWeightinUnsatClas(){
		for(IClause c: unsatClas){
			c.hardCoef++;
			for(ILiteral l: c.literals){
				l.weight++;
				if(l.weight > (nbClas/nbVar)*20)
					l.weight = nbClas/nbVar;
			}
		}
	}
	

		
	
	/**
	 * 
	 * TODO 将 lit 设置为满足，并更新 formula 中的信息
	 * @param lit
	 * @throws IOException 
	 */
	
	synchronized private void announceSatLit(ILiteral lit){				
		for(IClause c: lit.getClas()){
			if(!this.satLits.contains(lit))
				c.satLitsNum++;
			if(this.unsatLits.contains(lit))
				c.unsatLitsNum--;
			this.satClas.add(c);
			if(this.unsatClas.contains(c))
				this.unsatClas.remove(c);
			
			for(ILiteral l : c.literals){
				//对 clause c 中所有 lits 通知  c 已满足
				if(! l.satClas.contains(c))
					l.satClas.add(c);
				if(l.unsatClas.contains(c))
					l.unsatClas.remove(c);
			}
			
		}
		for(IClause c: lit.opposite.getClas()){
			if(! this.unsatLits.contains(lit.opposite))
				c.unsatLitsNum++;
			if(this.satLits.contains(lit.opposite))
				c.satLitsNum--;
			if(c.unsatLitsNum == c.literals.size()){
				this.unsatClas.add(c);
				if(this.satClas.contains(c))
					this.satClas.remove(c);
				//对 clause c 中所有 lits 通知  c 不满足
				for(ILiteral l : c.literals){
					if(! l.unsatClas.contains(c))
						l.unsatClas.add(c);
					if(l.satClas.contains(c))
						l.satClas.remove(c);
				}
			}		
		}
		
		this.satLits.add(lit);
		if(this.satLits.contains(lit.opposite))
			this.satLits.remove(lit.opposite);
		this.unsatLits.add(lit.opposite);
		if(this.unsatLits.contains(lit))
			this.unsatLits.remove(lit);
	
	}
	/**
	 * 
	 * TODO 将 lits 中 所有 literal 都设置为满足，并更新 formula 中的信息
	 * @param lits
	 * @throws IOException 
	 */
	public void announceSatLits(List<ILiteral> lits){
		for (ILiteral lit : lits) { 
			announceSatLit(lit);
		}
	}
	
	synchronized public boolean updateMinUnsatNum(){
		if(minUnsatNum > unsatClas.size()){
			minUnsatNum = unsatClas.size();
			return true;
		}
		return false;
	}
	
	public void resetFormula(){
		this.unVisitedVars.addAll(this.variables);
		this.visitedVars.clear();
		this.satClas.clear();
		this.unsatClas.clear();
		this.satLits.clear();
		this.unsatLits.clear();
		for(IVariable v: this.variables){
			v.degree = v.initDegree;
			v.visited = false;
			v.lit.satClas.clear();
			v.lit.unsatClas.addAll(v.lit.getClas());
			v.oppositeLit.satClas.clear();
			v.oppositeLit.unsatClas.addAll(v.oppositeLit.getClas());
		}
		for(IClause c: this.clauses){
			c.satLitsNum = 0;
			c.unsatLitsNum = 0;
		}
			
	}
	
}
