package cs.ustc.MaxSATsolver;
/**
 * MaxSAT 求解器
 * @author ccding  2016年3月7日 上午8:39:11
 */


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class Launcher {

	public static void main(String[] args) throws IOException, ParseFormatException{
		long begin = System.currentTimeMillis();
		IFormula formula = new IFormula();
		List<Group> groups = new ArrayList<>();
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
		int iterations = 3;
		while(iterations-- != 0){
			fw.write(lf + "iterations: " + iterations + lf);
			while(true){
				group.agents.addAll(formula.getIndependentSet());
				
				group.rmConflictAgents();
				
				//jump out while loop
				if (group.agents.isEmpty()) {
					break;
				}
				
				group.setAgentAttr();
				groups.add(group);
				
				for(int i=0; i<group.agents.size(); i++){
					formula.visitedClas.addAll(group.agents.get(i).getClas());
				}
				formula.getClauses().removeAll(formula.visitedClas);
				formula.visitedLits.addAll(group.agents);
				formula.getLiterals().removeAll(group.agents);
				fw.write("visited lits:"+lf);
				for(ILiteral l: formula.visitedLits){
					fw.write(l.toString());
				}
				fw.write(lf+"visited clas:"+lf);
				for(IClause c: formula.visitedClas){
					fw.write(c.toString()+lf);
				}
				fw.write(lf);
			}
			
			fw.write("unsat clas:"+lf);
			for(IClause c: formula.getClauses()){
				c.hardCoef++;
				for(ILiteral l: c.literals)
					l.weight++;
				fw.write(c.toString()+lf);
			}
			
			formula.reset();
			
		}
		fw.close();
		long time = System.currentTimeMillis()-begin;
		System.out.println("time:"+time);
	}	
}
