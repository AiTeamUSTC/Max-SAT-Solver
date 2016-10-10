package cs.ustc.MaxSATsolver;
/**
 * MaxSAT 求解器
 * @author ccding  2016年3月7日 上午8:39:11
 */


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Solver  {
	static final int  MAX_ITERATIONS = 5;
	static final double RANDOM_COEF1 = 0.6;
	static final double RANDOM_COEF2 = 0.1;
	static final long TIME_LIMIT = 3*60*1000;
	public Solver(){
		
	}
	/**
	 * TODO 将 formula 中每个 literal 视作一个 agent，将所有 agents 按照一定规则分成若干个不相交的联盟
	 * 所有的分组构成 formula 的一个初始解 
	 * @param f 给定的 formula
	 * @param randomCoef1 随机参数
	 * @param randomCoef2 随机参数
	 * @throws IOException 
	 */
	public List<List<IVariable>> getGroups(IFormula f, double randomCoef) {
		List<List<IVariable>> groups = new ArrayList<>();
		while(true){
			List<IVariable> group = new ArrayList<>();
			group.addAll(f.getIndependentGroup(randomCoef));
			//jump out while loop
			if (group.isEmpty()){
				//still has some literals not visited
				group = f.getUnvisitedVars();
				f.setVisitedVariables(group);
				groups.add(group);
				break;
			}
			f.setVisitedVariables(group);
			groups.add(group);
		}
		return groups;
		
	}
	
	
	/**
	 * 
	 * TODO 按照变量翻转的策略， 迭代求解 formula 直到找到解或者达到预设的时间限制
	 * @param randomCoef
	 */
	public void solveFormulaBasedOnGroups(IFormula formula){
		boolean isSolved = false;
		long startTime = System.currentTimeMillis();
	}
	
	/**
	 * TODO 读取 cnf 文件，并将信息存入到 formula 中
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
		
		FileWriter fw = new FileWriter(new File(args[0]));
		Solver solver = new Solver();

		
		String directory = "D:\\data\\MaxSAT2016_benchmarks\\ms_crafted\\bipartite\\maxcut-140-630-0.7";
		File files = new File(directory);
 		File[] fileArr = files.listFiles();
 		for(File file: fileArr){
 			System.out.println(file.getPath());
			long begin = System.currentTimeMillis();
			IFormula formula = solver.getFormulaFromCNFFile(file.getPath());
			solver.getGroups(formula, RANDOM_COEF1);
			
			long time = System.currentTimeMillis()-begin;
			System.out.println(time);

 		}

		fw.close();
	}
}
