package cs.ustc.MaxSATsolver;

import java.util.ArrayList;





public class IClause {
	ArrayList<ILiteral> literals;
	boolean isSatisfied;
	
	public  IClause(ArrayList<ILiteral> lits) {
		literals = new ArrayList<ILiteral>(lits);
		isSatisfied = false;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("[ ");
		for(ILiteral lit: literals){
			sb.append(lit.id+" ");
		}
		sb.append("] "+isSatisfied);
		return sb.toString();
	}
}
