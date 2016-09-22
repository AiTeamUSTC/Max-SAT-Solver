package cs.ustc.MaxSATsolver;
/**
 * MaxSAT 求解器
 * @author ccding  2016年3月7日 上午8:39:11
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Solver  {
	static final int  MAX_ITERATIONS = 1000;
	/**
	 * 将 formula 中每个 literal 视作一个 agent，将所有 agents 按照一定规则分成若干个不相交的联盟
	 * 通过组内联盟来求解 formula 
	 * @param f 给定的 formula
	 * @param randomCoef 随机参数，大小代表 agents 组成联盟时，使用贪心策略的概率
	 */
	public void solveFormula(IFormula f, double randomCoef) {
		Group group = new Group();
		while(true){
			group.agents.addAll(f.getIndependentSet(randomCoef));
			//remove conflict agents
			group.removeConflictAgents();
			//jump out while loop
			if (group.agents.isEmpty()) {
				//still has some literals not visited
				group.agents = f.getUnvisitedLits();
				group.setGroupAttr();
				f.setFormulaByGroup(group);
				break;
			}
			group.setGroupAttr();
			f.setFormulaByGroup(group);
		}
		
	}
	
	/**
	 * 迭代求解 formula 直到达到预设的最大迭代数
	 * @param f 参见 solveFormula() 
	 * @param randomCoef 参见 solveFormula()
	 * @return 完整求解 formula 所需的迭代次数
	 */
	public int iteratedSolveFormula(IFormula f, double randomCoef){
		int iterations = 0;
		while(++iterations != MAX_ITERATIONS){
			this.solveFormula(f,randomCoef);
			if(f.getClauses().size() == 0)
				break;
			//对于未满足的句子，增加其 hardCoef，使得下次迭代优先满足难度系数(hardCoef)高的句子
			for(IClause c: f.getClauses()){
				c.hardCoef++;
				for(ILiteral l: c.literals)
					l.weight++;
			}
			//将 formula 中一些信息重置到初始状态
			f.reset();
			
		}
		return iterations;
	}
	
	public IFormula getFormulaFromCNFFile(String cnfFile) throws ParseFormatException, IOException{
		IFormula f = new IFormula();
		CNFFileReader cnfFileReader = new CNFFileReader();
		System.out.println("file reading...");
		cnfFileReader.parseInstance(cnfFile, f);
		return f;
	}

	public static void main(String[] args) throws IOException, ParseFormatException{
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		int iterations;
		float totalIterations = 0 ;
		double randomCoef = 0.01;
		FileWriter fw = new FileWriter(new File(args[1]));
		
		Solver solver = new Solver();
		int runNums = 100;
		while(--runNums >= 0){
			long begin = System.currentTimeMillis();
			IFormula formula = solver.getFormulaFromCNFFile(args[0]);
			iterations = solver.iteratedSolveFormula(formula, randomCoef);
			totalIterations += iterations;
			long time = System.currentTimeMillis()-begin;
			System.out.println("randomCoef:"+randomCoef+"time:"+time);
		}
		fw.write(randomCoef+" "+(float)totalIterations/100);
		fw.close();
	}
}
