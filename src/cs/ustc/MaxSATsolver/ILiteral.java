package cs.ustc.MaxSATsolver;

import java.util.ArrayList;


/**
 * 
 * @author ccding 
 * 2016年3月5日下午8:51:47
 */

public class ILiteral {
	private final ArrayList<IClause> clauses; //含有该literal的所有clauses
	private ILiteral opposite; //对应的literal
	private final int id;
	private int weight;
	public enum  VARTYPE {forbid,freeze,free};
	public VARTYPE vartype;
	
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
		vartype = VARTYPE.free;
	}
	
	 /**
     * This constructor is to be used only to create the opposite literal.
     * 
     * @param opposite
     */
    protected ILiteral(ILiteral opposite) {
        id = -opposite.id;
        this.opposite = opposite;
        opposite.opposite = this;
        clauses = new ArrayList<>();
        vartype = VARTYPE.free;
    }
    
    /**
     * register clause
     * @param clause
     */
    public void addClause(IClause clause){
    	this.clauses.add(clause);
    }
    /**
     * 构建literal
     * @param i
     * @return
     */
    public static ILiteral createLiteral(int i) {
    	ILiteral lit = new ILiteral(i);
    	lit.opposite = new ILiteral(lit);
    	return lit;
    }
    
    public ILiteral opposite() {
		return opposite;
	}
    
    public ArrayList<IClause> getClauses() {
		return clauses;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public int getId() {
		return id;
	}
	
	public void setOpposite(ILiteral literal){
		opposite = literal;
	}
	public void weightPlus(int i) {
		weight+=i;
	}
}
