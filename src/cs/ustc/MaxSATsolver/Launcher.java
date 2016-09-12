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
		Set<Set<ILiteral>> groupsSets = new HashSet<>();
		CNFFileReader cnfFileReader = new CNFFileReader();
		String filename = args[0];
		System.out.println("file reading...");
		//instance cnf file to formula
		cnfFileReader.parseInstance(filename, formula);
		formula.setLiterals();
		
		String osType = System.getProperty("os.name");
		String fileName;
		String lf;
		if(osType.startsWith("Windows")){
			fileName = "D:\results.txt";
			lf = "\n\r";
		}else{
			fileName = "/Users/chenchen/Documents/graph.txt";
			lf = "\n";
		}
		
		FileWriter fw = new FileWriter(new File(fileName));
		
		UndirectedGraph<ILiteral, DefaultEdge> graph;
		Set<ILiteral> inSet ;
		int itNum = 100;
		while((itNum--)!=0){
			//map to an undirected graph
			graph = new SimpleGraph<>(DefaultEdge.class);
			GraphTool.transFormulaToGraph(graph, formula);
//			fw.write(graph.edgeSet().toString());
//			JFrame frame = new JFrame();
//			GraphTool.paintGraph(frame, graph);
//			frame.getContentPane().removeAll();
			fw.write("litNum:"+formula.getLiterals().size()+
					" clasNum:"+formula.getClauses().size()+lf);
			fw.write("VexNum:"+graph.vertexSet().size()+
					" edgesNum:"+graph.edgeSet().size()+lf);
			fw.write(lf);
			inSet = GraphTool.findIndependentSet(graph);
			Set<ILiteral> conflictLits = new HashSet<>();
			ILiteral delILiteral;
			for (ILiteral literal : inSet) {
				if (inSet.contains(literal.opposite)&&
					!(conflictLits.contains(literal)||conflictLits.contains(literal.opposite))
					) {
					delILiteral = literal.getClauses().size()>
								  literal.opposite.getClauses().size()?
								  literal : literal.opposite;
					conflictLits.add(delILiteral);
					fw.write(lf+"remove "+delILiteral.id+lf);
				}
			}
			inSet.removeAll(conflictLits);
			if (inSet.isEmpty()) {
				break;
			}
			groupsSets.add(inSet);
			fw.write("inSetSize: "+inSet.size()+lf);
			formula.getLiterals().removeAll(inSet);
			for (ILiteral lit : inSet) {
				fw.write(lit.toString());
				fw.write(lf);
				for (IClause c : lit.getClauses()) {
					fw.write(c.toString());
					c.isSatisfied = true;
				}
				fw.write(lf);
				formula.getClauses().removeAll(lit.getClauses());
			}
			fw.write("----------------------------");
		}
		
		long time = System.currentTimeMillis()-begin;
		fw.close();
		
		System.out.println("time:"+time);
		
	}
}
