package cs.ustc.MaxSATsolver;

import java.util.ArrayList;





public class IClause {
	private ArrayList<ILiteral> literals;
	public int SATNum;
	
	public  IClause(ArrayList<ILiteral> lits) {
		literals = lits;
		SATNum = 0;
	}
	

	
	/**
	 * get literals
	 * @return literals
	 */
	public ArrayList<ILiteral> getLiterals() {
		return literals;
	}
	
	public boolean contains(ILiteral literal) {
		return literals.contains(literal);
	}
}
