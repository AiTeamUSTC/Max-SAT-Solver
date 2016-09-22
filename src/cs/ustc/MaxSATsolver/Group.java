package cs.ustc.MaxSATsolver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class Group {
	List<ILiteral> agents;
	public Group(){
		agents = new Vector<ILiteral>();
	}
	
	public void removeConflictAgents(){
		Set<ILiteral> conflictAgs = new HashSet<>();
		ILiteral delAg;//delete literal
		for (ILiteral ag : agents) {
			if(ag.forbid){
				conflictAgs.add(ag);
			}
			else{
				//group contains lit and lit.opposite(conflict), must delete lit or lit.opposite
				if (agents.contains(ag.opposite)&&
						!(conflictAgs.contains(ag)||conflictAgs.contains(ag.opposite))) 
					{	
						delAg = ag.getClas().size()>ag.opposite.getClas().size() ?
								ag : ag.opposite;
						conflictAgs.add(delAg);
					}
			}
			
		}
		agents.removeAll(conflictAgs);
		conflictAgs.clear();
	}
	
	public void setGroupAttr(){
		for (ILiteral lit : agents) { 
			for(IClause c : lit.getClas()) {
				c.unsatLitsNum--;
				for(ILiteral l : c.literals){
					//对 clause c 中所有 lits 通知  c 已满足
					if(l.equals(lit))
						// lit 本身无需处理
						continue;
					l.visitedClas.add(c);
					l.getClas().remove(c);
					l.degree -= 2;
				}
			}
			//move all clas to visitedClas 
			lit.visitedClas.addAll(lit.getClas());
			lit.getClas().clear();
			lit.forbid = true;
			lit.opposite.forbid = true;
			for(IClause c: lit.opposite.getClas()){
				c.unsatLitsNum++;
			}
		}
	}
	
	
	

}
