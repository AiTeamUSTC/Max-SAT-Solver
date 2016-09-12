package cs.ustc.MaxSATsolver;

import java.util.ArrayList;


/**
 * 
 * @author ccding 
 * 2016年3月5日下午8:51:47
 */

public class ILiteral {
	private final ArrayList<IClause> clauses; //含有该literal的所有clauses
	ILiteral opposite; 
	final int id;
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
		clauses = new ArrayList<>();
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
        clauses = new ArrayList<>();
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
    
    public ArrayList<IClause> getClauses() {
		return clauses;
	}
    public String toString(){
    	return id+" ";
    }
}
