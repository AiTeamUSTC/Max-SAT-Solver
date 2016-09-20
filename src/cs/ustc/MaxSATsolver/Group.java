package cs.ustc.MaxSATsolver;

import java.util.HashSet;
import java.util.Set;

public class Group {
	Set<ILiteral> agents;
	public Group(){
		agents = new HashSet<ILiteral>();
	}
	
	public void rmConflictAgents(){
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
	
	public void setAgentAttr(){
		for (ILiteral lit : agents) {
			lit.forbid = true;
			lit.opposite.forbid = true;
			for(IClause c : lit.getClas()) {
				c.unsatLitsNum--;
			}
			for(IClause c: lit.opposite.getClas()){
				c.unsatLitsNum++;
			}
		}
	}
	
	
	

}
