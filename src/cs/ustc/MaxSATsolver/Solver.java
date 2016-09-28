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
	static final int  MAX_ITERATIONS = 10;
	static final double RANDOM_COEFFICIENT = 0.6;
	public Solver(){
		
	}
	/**
	 * TODO 将 formula 中每个 literal 视作一个 agent，将所有 agents 按照一定规则分成若干个不相交的联盟
	 * 通过组内联盟来求解 formula 
	 * @param f 给定的 formula
	 * @param randomCoef1 随机参数
	 * @param randomCoef2 随机参数
	 */
	public void solveFormula(IFormula f, double randomCoef) {
		List<Group> groups = new ArrayList<>();
		while(true){
			Group group = new Group();
			group.agents.addAll(f.getIndependentSet(randomCoef));
			//remove conflict agents
			group.removeConflictAgents();
			//jump out while loop
			if (group.agents.isEmpty()) {
				//still has some literals not visited
				group.agents = f.getUnvisitedLits();
				group.setGroupAttr();
				f.setFormulaByGroup(group);
				groups.add(group);
				
				for(Group g: groups)
					f.solution.addAll(g.agents);
				break;
			}
			group.setGroupAttr();
			f.setFormulaByGroup(group);
			groups.add(group);
		}
		
	}
	
	/**
	 * TODO 迭代求解 formula 直到达到预设的最大迭代数
	 * @param f 参见 solveFormula() 
	 * @param randomCoef 参见 solveFormula()
	 * @return 完整求解 formula 所需的迭代次数
	 * @throws IOException 
	 */
	public int iteratedSolveFormula(IFormula f, double randomCoef, FileWriter fw) throws IOException{
		int iterations = 0;
		while(++iterations != MAX_ITERATIONS){
			//将 formula 中一些信息重置到初始状态
			f.reset();
			this.solveFormula(f, randomCoef);
			fw.write(f.getClauses().size()+" ");
			if(f.getClauses().size() == 0)
				break;
			//对于未满足的句子，增加其 hardCoef，使得下次迭代优先满足难度系数(hardCoef)高的句子
			f.increaseLitsWeightinUnsatClas();

			
		}
		return iterations;
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
		System.out.println("file reading...");
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
		
		FileWriter fw = new FileWriter(new File(args[1]));
		Solver solver = new Solver();
		long begin = System.currentTimeMillis();
		
		IFormula formula = solver.getFormulaFromCNFFile(args[0]);
		solver.solveFormula(formula, RANDOM_COEFFICIENT);
		while(formula.unsatClas.size() != 0){
			formula.increaseLitsWeightinUnsatClas();
			ILiteral l = formula.getMaxWeightLit();
			formula.solution.remove(l.opposite);
			formula.solution.add(l);
			
			
		}
		
		
		
		long time = System.currentTimeMillis()-begin;
		System.out.println("time:"+time);

		fw.close();
	}
}
