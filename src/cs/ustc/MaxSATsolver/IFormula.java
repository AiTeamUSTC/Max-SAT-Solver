package cs.ustc.MaxSATsolver;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


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
	Set<IVariable> visitedVars;
	Set<IVariable> unVisitedVars;
	Set<IClause> visitedClas;
	Set<IClause> unVisitedClas;
	


	/**
	 * 设置vars和clauses的容量
	 * @param nbvars
	 * @param nbclauses
	 */
	public void setUniverse(int nbvars, int nbclauses) {
		nbVar = nbvars;
		nbClas = nbclauses;
		vars = new ILiteral[nbvars];
		clauses = new ArrayList<>(nbclauses);
		visitedVars = new HashSet<>(nbvars);
		unVisitedVars = new HashSet<>(nbvars);
		visitedClas = new HashSet<>(nbclauses);
		unVisitedClas = new HashSet<>(nbclauses);
		variables = new ArrayList<>(nbvars);
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
	 * TODO 初始化每个 variable 的邻居，并设置相应的 degree
	 */
	public void setVarsNeighbors(){
		for(IVariable var: variables){
			for(ILiteral lit: var.lit.neighbors){
				var.neighbors.add(this.getVariable(lit));
			}
			for(ILiteral lit: var.oppositeLit.neighbors){
				var.neighbors.add(this.getVariable(lit));
			}
			var.initDegree = var.neighbors.size();
			var.degree = var.initDegree;
		}
		
	}
	
	/**
	 * 
	 * TODO 通过 lit 找到对应的 variable
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
	 * 通过vars添加clause
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
	 * set literals
	 */
	public void setVariables(){
		for (int i = 0; i < vars.length; i++) {
			if(vars[i]!=null){
				variables.add(new IVariable(vars[i]));
			}
		}
		unVisitedVars.addAll(variables);
		unVisitedClas.addAll(clauses);
	}
	
	
	/**
	 * get independent set 
	 * first, find vertexes set covers all edges
	 * then, the complementary set of all vertexes is independent set
	 * @return independent set
	 */
	public Set<IVariable> getIndependentGroup(double randomCoef){
		Set<IVariable> vertexCover = new HashSet<>();
		Set<IClause> coverEdges = new HashSet<>();
		Set<IVariable> independentSet = new HashSet<>(variables);
		
		IVariable var;
		if(Math.random() < randomCoef)
			Collections.sort(variables);

		for(int i=0; i<variables.size(); i++){
			if(coverEdges.size()==clauses.size())
				break;
			var = variables.get(i);
			vertexCover.add(var);
			coverEdges.addAll(var.clauses);	
		}
		independentSet.removeAll(vertexCover);
		return independentSet;
		
	}
	
	
	
	/**
	 * 
	 * TODO 查找 formula 中从未访问过的 literal 并返回 
	 * @return
	 */
	public List<IVariable> getUnvisitedVars(){
		List<IVariable> unvisitedVars = new ArrayList<>();
		for(IVariable var: variables){
			if(!var.visited){
				unvisitedVars.add(var);
				var.visited = true;
			}
		}
		return unvisitedVars;
	}	
	
	/**
	 * 
	 * TODO 将 var 设置为已访问，并更新 formula 中的信息
	 * @param var
	 * @throws IOException 
	 */
	
	public void setVisitedVariable(IVariable var){
		var.visited = true;
		for(IVariable neighbor: var.neighbors)
			neighbor.degree--;
		this.unVisitedVars.remove(var);
		this.visitedVars.add(var);
		this.unVisitedClas.removeAll(var.clauses);
		this.visitedClas.addAll(var.clauses);
	
	}
	/**
	 * 
	 * TODO 将 vars 中 所有 variable 都设置为已访问，并更新 formula 中的信息
	 * @param vars
	 * @throws IOException 
	 */
	public void setVisitedVariables(List<IVariable> vars){
		for (IVariable var : vars) { 
			setVisitedVariable(var);
		}
	}
	
	
	/**
	 * 
	 * TODO reset formula information to origin status
	 */
	public void reset(){
		visitedClas.clear();
		unVisitedClas.clear();
		visitedVars.clear();
		unVisitedVars.clear();
		
		for(IVariable v: variables){
			v.visited = false;
			v.degree = v.initDegree;
		}
	}
	
	
	
}
