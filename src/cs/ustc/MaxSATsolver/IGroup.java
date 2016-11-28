package cs.ustc.MaxSATsolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	static final long LIMIT_TIME = 3*60*1000;  
	
	public IGroup(List<IVariable> agents, IFormula f){
		this.agents = new ArrayList<>(agents); 
		solution = new ArrayList<>(agents.size());
		neighbors = new HashMap<>();
		this.formula = f;
	}
	
	
	/**
	 * 求每个组对应最好的解
	 * @param randomCoefSolution 采用贪婪的随机性大小
	 */
	public List<ILiteral> getSolution(double randomCoefSolution){
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

	/**
	 * 设置每个 group 的邻居， 两个 group 是邻居当且仅当组中 agent 出现在同一个 clause 中
	 * 
	 * @param groups 
	 */
	public static void initGroupNeighbors(List<IGroup> groups){
		List<IVariable> tmp = new ArrayList<>();
		List<IVariable> tmpCopy = new ArrayList<>();
		IGroup groupTmp1 = null;
		IGroup groupTmp2 = null;
		for(int i=0; i<groups.size(); i++){
			groupTmp1 = groups.get(i);
			for(IVariable agent: groupTmp1.agents){
				tmp.addAll(agent.neighbors);
			}
		
			for(int j=i+1; j<groups.size(); j++){
				tmpCopy.addAll(tmp);
				groupTmp2 = groups.get(j);
				tmpCopy.retainAll(groupTmp2.agents);
				if(tmpCopy.size()>0){
					groupTmp1.neighbors.put(groupTmp2, tmpCopy.size());
					groupTmp2.neighbors.put(groupTmp1, tmpCopy.size());
				}
				tmpCopy.clear();
			}
			tmp.clear();
		}
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
	}
	
	

}
