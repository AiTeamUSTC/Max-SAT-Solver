package cs.ustc.MaxSATsolver;

import java.util.ArrayList;





public class IClause {
	ArrayList<ILiteral> literals;
	int unsatLitsNum;
	int hardCoef;
	
	public  IClause(ArrayList<ILiteral> lits) {
		literals = new ArrayList<ILiteral>(lits);
		unsatLitsNum = 0;
		hardCoef = 0;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("[ ");
		for(ILiteral lit: literals){
			sb.append(lit.id+" ");
		}
		sb.append("] "+unsatLitsNum);
		return sb.toString();
	}
}
