package cs.ustc.MaxSATsolver;

import java.util.HashSet;
import java.util.Set;
/**
 * 
 * @ClassName: IVariable
 *
 * @Description: TODO
 *
 * @author: ccding
 * @date: 2016年10月6日 上午9:30:17
 *
 */
public class IVariable {
	ILiteral lit;
	ILiteral oppositeLit;
	Set<IClause> clauses;
	public IVariable(ILiteral lit){
		this.clauses = new HashSet<>();
		this.lit = lit;
		this.oppositeLit = lit.opposite;
		this.clauses.addAll(lit.getClas());
		this.clauses.addAll(oppositeLit.getClas());
	}

}
