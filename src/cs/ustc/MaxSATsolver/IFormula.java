package cs.ustc.MaxSATsolver;

import java.util.List;
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
	private  List<IClause> clauses; //所有的clauses
	private ILiteral[] vars; //formula的所有vars
	private List<ILiteral> literals;
	Set<ILiteral> satLits;
	Set<IClause> satClas;
	int nbVar, nbClas;
	Set<IClause> unsatClas;
	Set<ILiteral> unsatLits;
	


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
		literals = new ArrayList<>(nbvars*2);
		satLits = new HashSet<>(nbvars*2);
		unsatLits = new HashSet<>(nbvars*2);
		satClas = new HashSet<>(nbclauses);
		unsatClas = new HashSet<>(nbclauses);

		
		
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
	 * get vars 
	 * @return vars
	 */
	public ILiteral[] getvars(){
		return vars;
	}
	public List<ILiteral> getLiterals() {
		return literals;
	}
	/**
	 * get clauses	
	 * @return clauses
	 */
	public List<IClause> getClauses() {
		return this.clauses;
	}
	/**
	 * set literals
	 */
	public void setLiterals(){
		for (int i = 0; i < vars.length; i++) {
			if(vars[i]!=null){
				literals.add(vars[i]);
				literals.add(vars[i].opposite);
			}
		}
	}
	

	
	public ILiteral getMaxWeightLit(){
		ILiteral maxWeightLit = literals.get(0);
		for(int i=1; i<literals.size(); i++){
			if(literals.get(i).weight > maxWeightLit.weight)
				maxWeightLit = literals.get(i);
		}
		return maxWeightLit;
	}
	
	
	/**
	 * get independent set 
	 * first, find vertexes set covers all edges
	 * then, the complementary set of all vertexes is independent set
	 * @return independent set
	 */
	public Set<ILiteral> getIndependentSet(double randomCoef){
		Set<ILiteral> vertexCover = new HashSet<>();
		Set<IClause> coverEdges = new HashSet<>();
		Set<ILiteral> independentSet = new HashSet<>(literals);
		
		ILiteral lit;
		if(Math.random() < randomCoef)
			Collections.sort(literals);

		for(int i=0; i<literals.size(); i++){
			if(coverEdges.size()==clauses.size())
				break;
			lit = literals.get(i);
			vertexCover.add(lit);
			coverEdges.addAll(lit.getClas());	
		}
		independentSet.removeAll(vertexCover);
		return independentSet;
		
	}
	
	
	public List<ILiteral> getUnvisitedLits(){
		List<ILiteral> unvisitedLits = new ArrayList<>();
		for(ILiteral l: literals){
			if(!l.forbid){
				unvisitedLits.add(l.getClas().size()>l.opposite.getClas().size()
						? l:l.opposite);
				l.forbid = true;
				l.opposite.forbid = true;
			}
		}
		return unvisitedLits;
	}
	
	public void increaseLitsWeightinUnsatClas(){
		for(IClause c: unsatClas){
			c.hardCoef++;
			for(ILiteral l: c.literals)
				l.weight++;
		}
	}
	
	
	/**
	 * 
	 * TODO delete conflict lit from lits 
	 * @param Lits
	 */
	public void removeConflictLits(List<ILiteral> Lits){
		Set<ILiteral> conflictAgs = new HashSet<>();
		ILiteral delAg;//delete literal
		for (ILiteral ag : Lits) {
			if(ag.forbid){
				conflictAgs.add(ag);
			}
			else{
				//group contains lit and lit.opposite(conflict), must delete lit or lit.opposite
				if (Lits.contains(ag.opposite)&&
						!(conflictAgs.contains(ag)||conflictAgs.contains(ag.opposite))) 
					{	
						delAg = ag.getClas().size()>ag.opposite.getClas().size() ?
								ag : ag.opposite;
						conflictAgs.add(delAg);
					}
			}	
		}
		Lits.removeAll(conflictAgs);
		conflictAgs.clear();
	}
	
	public void announceSatLit(ILiteral lit){
		lit.forbid = true;
		lit.opposite.forbid = true;
		
		this.satLits.add(lit);
		if(this.satLits.contains(lit.opposite))
			this.satLits.remove(lit.opposite);
		
		this.unsatLits.add(lit.opposite);
		if(this.unsatLits.contains(lit))
			this.unsatLits.remove(lit);
		
		
		for(IClause c: lit.getClas()){
			c.unsatLitsNum--;
			this.satClas.add(c);
			if(this.unsatClas.contains(c))
				this.unsatClas.remove(c);
			
			for(ILiteral l : c.literals){
				l.degree -= c.unsatLitsNum;
				//对 clause c 中所有 lits 通知  c 已满足
				l.satClas.add(c);
				if(l.unsatClas.contains(c))
					l.unsatClas.remove(c);
			}
			
		}
		for(IClause c: lit.opposite.getClas()){
			if(c.unsatLitsNum < c.literals.size())
				c.unsatLitsNum++;
			if(c.unsatLitsNum == c.literals.size()){
				this.unsatClas.add(c);
				if(this.satClas.contains(c))
					this.satClas.remove(c);
				//对 clause c 中所有 lits 通知  c 不满足
				for(ILiteral l : c.literals){
					if(l.degree < l.initDegree)
						l.degree += c.unsatLitsNum;
					//对 clause c 中所有 lits 通知  c 已满足
					l.unsatClas.add(c);
					if(l.satClas.contains(c))
						l.satClas.remove(c);
				}
			}
				
		}
	
	}
	public void announceSatLits(List<ILiteral> lits){
		for (ILiteral lit : lits) { 
			announceSatLit(lit);
		}
	}
	
	
	
	public void reset(){
		satClas.clear();
		unsatClas.clear();
		satLits.clear();
		unsatLits.clear();
		
		for(ILiteral l: literals){
			l.forbid = false;
			l.degree = l.initDegree;
			l.satClas.clear();
			l.unsatClas.clear();
		}
		for(IClause c: clauses){
			c.unsatLitsNum = c.literals.size();
		}
	}
	
	
	
}
