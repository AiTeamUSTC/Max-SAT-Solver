package cs.ustc.MaxSATsolver;
/**
 * MaxSAT 求解器
 * @author ccding  2016年3月7日 上午8:39:11
 */


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;



public class Launcher {

	public static void main(String[] args) throws IOException, ParseFormatException{
		long begin = System.currentTimeMillis();
		IFormula formula = new IFormula();
		Set<Group> groupsSet = new HashSet<>();
		CNFFileReader cnfFileReader = new CNFFileReader();
		String filename = args[0];
		System.out.println("file reading...");
		//map cnf file to formula
		cnfFileReader.parseInstance(filename, formula);
		
		String osType = System.getProperty("os.name");
		String fileName;
		String lf;
		if(osType.startsWith("Windows")){
			fileName = "D:\\results.txt";
			lf = "\r\n";
		}else{
			fileName = "/Users/chenchen/Documents/graph.txt";
			lf = "\n";
		}
		
		FileWriter fw = new FileWriter(new File(fileName));
		
		Group group = new Group();
		while(true){
			group.agents.addAll(formula.getIndependentSet());
			
			group.rmConflictAgents();
			
			//jump out while loop
			if (group.agents.isEmpty()) {
				break;
			}
			
			group.setAgentAttr();
			groupsSet.add(group);
			
			
			formula.visitedLits.addAll(group.agents);
			formula.getLiterals().removeAll(group.agents);
		}
		
		for(IClause c: formula.getClauses()){
			if(c.unsatLitsNum == c.literals.size()){
				formula.unsatClasNum++;
				formula.unsatClas.add(c);
			}
		}
		
		
		
		
		
		
		
		System.out.println("unsat clas num: "+formula.unsatClasNum);
		fw.close();
		long time = System.currentTimeMillis()-begin;
		System.out.println("time:"+time);
	}	
}
