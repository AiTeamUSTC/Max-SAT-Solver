package cs.ustc.MaxSATsolver;
/**
 * MaxSAT 求解器
 * @author ccding  2016年3月7日 上午8:39:11
 */

import java.io.IOException;

import cs.ustc.BGModel.Agent;
import cs.ustc.BGModel.Game;


public class Launcher {
	/**
	 * 
	 * @param args 控制台输入解决的实例和迭代的次数
	 * @throws IOException
	 * @throws ParseFormatException
	 */
	public static void main(String[] args) throws IOException, ParseFormatException{
		long begin = System.currentTimeMillis();
		IFormula formula = new IFormula();
		CNFFileReader cnfFileReader = new CNFFileReader();
		String filename = args[0];
		//从cnf文件中读取信息
		System.out.println("file reading...");
		cnfFileReader.parseInstance(filename, formula);
		//将maxsat问题映射到boolean game model
		System.out.println("constructing...");
		Game game = new Game(formula);
		game.setAgentsInfo();
		System.out.println("processing...");
		for (Agent agent : game.getAgents()) {
			agent.findBestOfAction();
		}
		game.calSATRate();
		System.out.println(game.satClausesCount);
		@SuppressWarnings("unused")
		ILiteral[] bestStragety = game.strategy;
		int maxSatClausesNum = game.satClausesCount;
		int round;
		if (args[1]!=null) {
			round = Integer.parseInt(args[1]);
		}else {
			round = 1000;
		}
		
		float initSATRate = game.getSatRate();
		int repeatLimit = 0;
		while(--round != 0 ){
			game.agentUpdateActionNoAnnouncement();
			game.calSATRate();
			if (initSATRate >= game.getSatRate()) {
				repeatLimit++;
				if (repeatLimit>100) {
					break;
				}
			}else{
				bestStragety = game.strategy;
				maxSatClausesNum = game.satClausesCount;
				System.out.println(game.satClausesCount);
				initSATRate = game.getSatRate();
				repeatLimit = 0;
			}
		
		}
		System.out.println("Instance: "+args[0]);
		game.satClausesCount = maxSatClausesNum;
		System.out.println(game.getGameInfo());
		long time = System.currentTimeMillis()-begin;
		System.out.println("time :"+time/1000+" s");
		
	}
}
