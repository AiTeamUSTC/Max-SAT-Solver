package cs.ustc.MaxSATsolver;

import java.util.ArrayList;
import java.util.List;





public class IClause {
	List<ILiteral> literals;
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
		sb.append(" "+hardCoef);
		return sb.toString();
	}
}
