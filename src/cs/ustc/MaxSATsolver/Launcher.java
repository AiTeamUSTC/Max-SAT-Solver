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

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.SimpleGraph;


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
		formula.setLiterals();
		
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
		
		UndirectedGraph<ILiteral, DefaultEdge> graph;
		Group group = new Group();
		Set<IClause> unitClas = new HashSet<IClause>();
		//map to an undirected graph
		graph = new SimpleGraph<>(DefaultEdge.class);
		GraphTool.transFormulaToGraph(graph, formula);
		while(true){
//			JFrame frame = new JFrame();
//			GraphTool.paintGraph(frame, graph);
//			frame.getContentPane().removeAll();
			
			for(IClause c: unitClas){
				group.agents.addAll(c.literals);
			}
			unitClas.clear();	
			
			group.agents.addAll(GraphTool.findIndependentSet(graph));
//			group.addAll(VertexCovers.findGreedyCover(graph));
			
			group.rmConflictAgents();
			
			//jump out while loop
			if (group.agents.isEmpty()) {
				break;
			}
			group.setAgentAttr(unitClas);
			groupsSet.add(group);
			//delete vertices that in independent set
			graph.removeAllVertices(group.agents);

		}
		
		
		for(IClause c: formula.getClauses()){
			if(c.unsatLitsNum == c.literals.size()){
				formula.unsatClasNum++;
				fw.write(c.toString());
			}
		}
		System.out.println("unsat clas num: "+formula.unsatClasNum);
		fw.close();
		long time = System.currentTimeMillis()-begin;
		System.out.println("time:"+time);
	}	
}
