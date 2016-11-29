package cs.ustc.MaxSATsolver;
/**
 * incomplete MaxSAT solver
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFWorkbook; 
import org.apache.poi.ss.usermodel.Row; 
import org.apache.poi.ss.usermodel.Sheet; 
import org.apache.poi.ss.usermodel.Workbook;


public class Solver  {
	static final int  MAX_ITERATIONS = 20000;
	static final double RANDOM_COEF_SOLUTION = 0.7;
	static final double RANDOM_COEF_INDEPENDENTSET = 0.8;
	static final double RANDOM_COEF_NEXTGROUP = 0;
	static final long TIME_LIMIT = 3*60*1000;
	
	/**
	 * 将 formula 中每个 literal 视作一个 agent，将所有 agents 按照一定规则分成若干个不相交的联盟
	 * 
	 * @param f 存储 cnf 文件信息的 formula
	 * @param randomCoefIndependentSet 寻找独立集时，采取贪婪策略的随机性大小
	 * @return 不相交的各个分组（联盟）
	 */
	public List<IGroup> getGroups(IFormula f, double randomCoefIndependentSet) {
		List<IGroup> groups = new ArrayList<>();
		int groupIdx = 0;
		while(true){
			IGroup group = new IGroup(f.getIndependentGroup(randomCoefIndependentSet),f,"group"+groupIdx++);
			//jump out while loop
			if (group.agents.isEmpty()){
				break;
			}
			groups.add(group);
			f.removeGroupFromFormula(group.agents);
		}
		return groups;
		
	}
	
	
	
	/**
	 * 
	 * 
	 * @param formula 存储 cnf 文件信息的 formula f
 	 * @param groups  独立的各个分组
	 * @return
	 * @throws InterruptedException 
	 */
	public List<ILiteral> solveFormulaBasedOnGroups(IFormula formula, List<IGroup> groups) throws InterruptedException{
		List<ILiteral> solution = new ArrayList<>();
		List<Thread> threads = new ArrayList<>();
		
		for(IGroup group: groups){
			threads.add(new Thread(group));
		}
		for(Thread t: threads)
			t.start();
		boolean flag = true;
		while(flag){
			flag = false;
			for(Thread t:threads)
				if (t.isAlive()) 
					flag = true;
		}
		for(IGroup g: groups)
			solution.addAll(g.solution);
		return solution;
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
		f.setVariables();
		f.setVarsNeighbors();
		return f;
	}
	
	/**
	 * 程序入口
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ParseFormatException
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, ParseFormatException, InterruptedException{
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");  
	    Date dt = new Date();  
	    FileWriter fw = null;
	    SimpleDateFormat sdf = new SimpleDateFormat("MMdd_HHmm");  
	    String dataStr = sdf.format(dt);
	    String outResultPath = args[1]+"results_random_flipn_updateweight"+dataStr+".xls";
//	    String outResultAnalysisPath = args[1]+"results_analysis_"+dataStr+".txt";
		
//		fw = new FileWriter(new File(outResultAnalysisPath));
 		Workbook wb = new HSSFWorkbook();
		OutputStream os = null;
		
		Solver solver = new Solver();
		File rootPath = new File(args[0]);
		File[] paths = rootPath.listFiles();
		
		for(File path: paths){
			//跳过 industrial instances
			if(path.getName().equals("ms_industrial"))
				continue;
			
			//获取 path 目录下的所有 .cnf 文件路径
			Path filesPath = Paths.get(path.getAbsolutePath());
	 		final List<File> files = new ArrayList<File>();
	 		SimpleFileVisitor<Path> finder = new SimpleFileVisitor<Path>(){
	 		    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
	 		    	if(file.toFile().getName().endsWith(".cnf"))
	 		    		files.add(file.toFile());
	 		        return super.visitFile(file, attrs);
	 		    }
	 		};
	 		java.nio.file.Files.walkFileTree(filesPath, finder);
	 		
			Sheet sheet = wb.createSheet(path.getName());
			Row r = null;
	 		int rowNum = 0;
	 		
	 		for(File file: files){
//	 			fw.write(file.getName()+"\r\n");
	 			r = sheet.createRow(rowNum++);
				r.createCell(0).setCellValue(file.getName());
	 			System.out.println(file.getPath());
	 			
				long begin = System.currentTimeMillis();
				IFormula formula = solver.getFormulaFromCNFFile(file.getPath());
				List<IGroup> groups = null;
				List<ILiteral> solution = null;
				
		 		groups = solver.getGroups(formula, RANDOM_COEF_INDEPENDENTSET);
		 		solution = solver.solveFormulaBasedOnGroups(formula, groups);
		 		Collections.sort(solution);
//		 		StringBuffer sb = new StringBuffer();
//				for(int i=0; i<solution.size(); i++){
//					sb.append(solution.get(i).id>0 ? "1 ":"0 ");
//				} 
//				fw.write(sb.toString()+"\r\n");
				long time = System.currentTimeMillis()-begin;
				System.out.println(time);
				r.createCell(1).setCellValue(formula.minUnsatNum);
				r.createCell(2).setCellValue(time);
				System.out.println(solution.toString());
//				System.out.println(formula.minUnsatNum);
	 		}
			
		}
		
 		os = new FileOutputStream(outResultPath);
 		wb.write(os);
 		wb.close();
 		os.close();
// 		fw.close();

	}
}
