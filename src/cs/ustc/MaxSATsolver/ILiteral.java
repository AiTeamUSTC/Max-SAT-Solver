package cs.ustc.MaxSATsolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



/**
 * 
 * @author ccding 
 * 2016年3月5日下午8:51:47
 */

public class ILiteral implements Comparable<ILiteral>{
	private final Set<IClause> clauses; //含有该literal的所有clauses
	Set<ILiteral> neighbors;
	int degree;
	ILiteral opposite; 
	final int id;
	int weight;
	boolean forbid;

	
	/**
	 * 构造literal
	 * @param id
	 */
	protected ILiteral(int id){
		this.id = id;
		if (this.opposite == null) {
			this.opposite = new ILiteral(this);
		}
		clauses = new HashSet<>();
		neighbors = new HashSet<>();
		weight = 0;
	}
	
	 /**
     * This constructor is to be used only to create the opposite literal.
     * 
     * @param opposite
     */
    private ILiteral(ILiteral opposite) {
        id = -opposite.id;
        this.opposite = opposite;
        opposite.opposite = this;
        clauses = new HashSet<>();
        neighbors = new HashSet<>();
        weight = 0;
    }
    
    /**
     * register clause
     * @param clause
     */
    public void addClause(IClause clause){
    	this.clauses.add(clause);
    }
    
    
    public ILiteral opposite() {
		return opposite;
	}
    
    public Set<IClause> getClas() {
		return clauses;
	}
    public String toString(){
    	return id+" ";
    }

	@Override
	public int compareTo(ILiteral lit) {
		// TODO Auto-generated method stub
		return (weight-degree) - (lit.weight-lit.degree);
	}
}
