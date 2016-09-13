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
						//if lit in unit clause, it must be satisfied, so delete lit.opposite
						if(ag.unit){
							delAg = ag.opposite;
						}
						//on the contrary
						else if(ag.opposite.unit){
							delAg = ag;
						}
						//else delete literal that relate to  less clas
						else{	
							delAg = ag.getClas().size()>ag.opposite.getClas().size() ?
									ag : ag.opposite;
						}
						conflictAgs.add(delAg);
					}
			}
			
		}
		agents.removeAll(conflictAgs);
		conflictAgs.clear();
	}
	
	public void setAgentAttr(Set<IClause> unitClas){
		for (ILiteral lit : agents) {
			lit.forbid = true;
			lit.opposite.forbid = true;
			for(IClause c : lit.getClas()) {
				c.unsatLitsNum--;
			}
			for(IClause c: lit.opposite.getClas()){
				c.unsatLitsNum++;
				if(c.unsatLitsNum == c.literals.size()-1){
					for(ILiteral lit2 : c.literals){
						if(!lit2.forbid){
							lit2.unit = true;
						}
					}
					unitClas.add(c);
				}
			}
		}
	}
	
	
	

}
