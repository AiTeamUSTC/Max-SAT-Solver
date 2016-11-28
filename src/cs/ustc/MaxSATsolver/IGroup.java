package cs.ustc.MaxSATsolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLException;

/**
 * 
 * @ClassName: IGroup
 *
 * @Description:  
 *
 * @author: ccding
 * @date: 2016年10月19日 上午8:45:23
 *
 */
public class IGroup implements Runnable{
	List<IVariable> agents;
	List<ILiteral> solution;
	Map<IGroup, Integer>  neighbors;
	IFormula formula;
	List<ILiteral> flipLits;
	String id;
	static final long LIMIT_TIME = 3*60*1000;  
	
	public IGroup(List<IVariable> agents, IFormula f, String id){
		this.agents = new ArrayList<>(agents); 
		solution = new ArrayList<>(agents.size());
		neighbors = new HashMap<>();
		this.formula = f;
		flipLits = new ArrayList<>();
		this.id = id;
	}
	
	
	/**
	 * 求每个组对应最好的解
	 * @param randomCoefSolution 采用贪婪的随机性大小
	 */
	public List<ILiteral> getSolution(){
		//构造每组的初始解，贪婪策略
		if(solution.isEmpty()){
			for(IVariable var: agents){	
				solution.add(var.lit.unsatClas.size() > var.oppositeLit.unsatClas.size() ? var.lit : var.oppositeLit);
			}
			return solution;
		}
		List<ILiteral> flipLits = new ArrayList<>();
		Collections.sort(agents);
		for(IVariable var: agents){
			if(flipVariable(var) > 0){
				if(solution.contains(var.lit)){
					solution.remove(var.lit);
					solution.add(var.oppositeLit);
					flipLits.add(var.oppositeLit);
				}else{
					solution.remove(var.oppositeLit);
					solution.add(var.lit);
					flipLits.add(var.lit);
				}
				break;
			}
		}
		return flipLits;
	}
	
	public double flipVariable(IVariable var){
		ILiteral satLit = solution.contains(var.lit) ? var.lit : var.oppositeLit;
		ILiteral unsatLit = satLit.opposite;
		double increasdeWeight = unsatLit.unsatClas.size() + ((double)unsatLit.weight) * 0.1;
		double decreasedWeight = (double)satLit.weight * 0.1;
		for(IClause c: satLit.satClas){
			if(c.satLitsNum == 1)
				decreasedWeight+=1;
		}
		return increasdeWeight - decreasedWeight;
	} 

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int repeatedCount = 0;
		while(true){
			flipLits.addAll(this.getSolution());
			if(!flipLits.isEmpty()){
				formula.announceSatLits(flipLits);
			}else{
				repeatedCount++;
				if(repeatedCount > 100){
					break;
				}
			}
			formula.increaseLitsWeightinUnsatClas();
			formula.updateMinUnsatNum();
			System.out.println(id + " flips " + flipLits.size() +" variables");
			System.out.println("formula minUnsatNum: "+formula.minUnsatNum);

		}
		
	}
	
	

}
