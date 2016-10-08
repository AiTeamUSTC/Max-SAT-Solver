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
	public void getInitSolution(IFormula f, double randomCoef) {
		List<List<ILiteral>> groups = new ArrayList<>();
		while(true){
			List<ILiteral> group = new ArrayList<>();
			group.addAll(f.getIndependentSet(randomCoef));
			//remove conflict lits
			f.removeConflictLits(group);
			//jump out while loop
			if (group.isEmpty()) {
				//still has some literals not visited
				group = f.getUnvisitedLits();
				f.announceSatLits(group);
				groups.add(group);
				break;
			}
			f.announceSatLits(group);
			groups.add(group);
		}
		
	}
	
	/**
	 * TODO 迭代求 formula 的初始解 直到达到预设的最大迭代数
	 * @param f 参见 solveFormula() 
	 * @param randomCoef 参见 solveFormula()
	 * @return 完整求解 formula 所需的迭代次数
	 * @throws IOException 
	 */
	public int iteratedGetInitSolution(IFormula f, double randomCoef) throws IOException{
		int iterations = 0;
		while(++iterations != MAX_ITERATIONS){
			//将 formula 中一些信息重置到初始状态
			f.reset();
			this.getInitSolution(f, randomCoef);
			if(f.getClauses().size() == 0)
				break;
			//对于未满足的句子，增加其 hardCoef，使得下次迭代优先满足难度系数(hardCoef)高的句子
			f.increaseLitsWeightinUnsatClas();

			
		}
		return iterations;
	}
	
	/**
	 * 
	 * TODO 按照变量翻转的策略， 迭代求解 formula 直到找到解或者达到预设的时间限制
	 * @param randomCoef
	 */
	public boolean solveFormulaBasedOnInitSolution(IFormula formula, double randomCoef, long timeLimit){
		boolean isSolved = false;
		long startTime = System.currentTimeMillis();
		while(formula.unsatClas.size() != 0){
			formula.increaseLitsWeightinUnsatClas();
			ILiteral l = null;
			if(Math.random() > randomCoef){
			    l = formula.getMaxWeightUnsatLit();
			}else{
				l = formula.getRandomUnsatLit();
			}
			if(l == null)
				l = formula.getRandomUnsatLit();
			formula.announceSatLit(l);
//			l.weight--;
			l.lastModified = true;
			l.opposite.lastModified = true;
			for(ILiteral neibor: l.neighbors)
				neibor.lastModified = false;
			System.out.println(formula.unsatClas.size());
			if(System.currentTimeMillis()-startTime > timeLimit)
				break;
		}
		if(formula.unsatClas.size() == 0)
			isSolved = true;
		return isSolved;
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
			solver.getInitSolution(formula, RANDOM_COEF1);
			boolean isSolved = solver.solveFormulaBasedOnInitSolution(formula, RANDOM_COEF2, TIME_LIMIT);
			long time = System.currentTimeMillis()-begin;
			System.out.println(time);
			if(isSolved){
				fw.write(file.getPath()+" "+time+" "+formula.unsatClas.size()+"\r\n");
			}else{
				fw.write(file.getPath()+" time out "+formula.unsatClas.size()+"\r\n");
			}

 		}

		fw.close();
	}
}
