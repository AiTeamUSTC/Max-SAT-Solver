package cs.ustc.MaxSATsolver;
/**
 * MaxSAT 求解器
 * @author ccding  2016年3月7日 上午8:39:11
 */


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.SimpleGraph;

import org.apache.poi.hssf.usermodel.HSSFWorkbook; 
import org.apache.poi.ss.usermodel.Row; 
import org.apache.poi.ss.usermodel.Sheet; 
import org.apache.poi.ss.usermodel.Workbook;


public class Solver  {
	static final int  MAX_ITERATIONS = 5;
	static final double RANDOM_COEF1 = 0.6;
	static final double RANDOM_COEF2 = 0.1;
	static final long TIME_LIMIT = 3*60*1000;
	public Solver(){
		
	}
	/**
	 *  将 formula 中每个 literal 视作一个 agent，将所有 agents 按照一定规则分成若干个不相交的联盟
	 * 所有的分组构成 formula 的一个初始解 
	 * @param f 给定的 formula
	 * @param randomCoef1 随机参数
	 * @param randomCoef2 随机参数
	 * @throws IOException 
	 */
	public List<List<IVariable>> getGroups(IFormula f, double randomCoef) {
		List<List<IVariable>> groups = new ArrayList<>();
		UndirectedGraph<IVariable, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
		GraphTool.transFormulaToGraph(graph, f);
		while(true){
			List<IVariable> group = new ArrayList<>();
			group.addAll(GraphTool.findIndependentSet(graph));
			//jump out while loop
			if (group.isEmpty()){
				break;
			}
			groups.add(group);
			graph.removeAllVertices(group);
		}
		return groups;
		
	}
	
	
	/**
	 * 
	 *  按照变量翻转的策略， 迭代求解 formula 直到找到解或者达到预设的时间限制
	 * @param randomCoef
	 */
	public void solveFormulaBasedOnGroups(IFormula formula, List<List<IVariable>> groups){
		List<ILiteral> solution = new ArrayList<>();
		List<ILiteral> groupSolution = new ArrayList<>();
		for(List<IVariable> group: groups){
			groupSolution = formula.getGroupSolution(group);
			formula.announceSatLits(groupSolution);
			solution.addAll(groupSolution);
		}

	}
	
	/**
	 *  读取 cnf 文件，并将信息存入到 formula 中
	 * @param cnfFile
	 * @return formula
	 * @throws ParseFormatException
	 * @throws IOException
	 */
	
	public IFormula getFormulaFromCNFFile(String cnfFile) throws ParseFormatException, IOException{
		IFormula f = new IFormula();
		CNFFileReader cnfFileReader = new CNFFileReader();
		cnfFileReader.parseInstance(cnfFile, f);
		return f;
	}
	/**
	 * 
	 * @param args args[0] cnf file path, args[1] text file path that store results
	 * @throws IOException
	 * @throws ParseFormatException
	 */
	public static void main(String[] args) throws IOException, ParseFormatException{
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		
		Workbook wb = new HSSFWorkbook();
		OutputStream os = null;
		Solver solver = new Solver();

		
		Path path = Paths.get(args[0]);

 		
 		final List<File> files = new ArrayList<File>();
 		SimpleFileVisitor<Path> finder = new SimpleFileVisitor<Path>(){
 		    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
 		    	if(file.toFile().getName().endsWith(".cnf"))
 		    		files.add(file.toFile());
 		        return super.visitFile(file, attrs);
 		    }
 		};
 		 
 		java.nio.file.Files.walkFileTree(path, finder);
 		
		Sheet sheet = wb.createSheet("MaxSAT2016_benchmarks results");
		Row r = sheet.createRow(0);
		r.createCell(0).setCellValue("Instance");
		r.createCell(1).setCellValue("UnsatNum");
		r.createCell(2).setCellValue("Time(ms)");
		
 		int rowNum = 1;
 		for(File file: files){
 			r = sheet.createRow(rowNum++);
 			System.out.println(file.getPath());
			long begin = System.currentTimeMillis();
			IFormula formula = solver.getFormulaFromCNFFile(file.getPath());
			List<List<IVariable>> groups = solver.getGroups(formula, RANDOM_COEF1);
			solver.solveFormulaBasedOnGroups(formula,groups);
			long time = System.currentTimeMillis()-begin;
			System.out.println(time);
			
			r.createCell(0).setCellValue(file.getName());
			r.createCell(1).setCellValue(formula.unsatClas.size());
			r.createCell(2).setCellValue(time);
 		}
 		os = new FileOutputStream(args[1]);
 		wb.write(os);
 		wb.close();
 		os.close();

	}
}
