package cs.ustc.BGModel;

import java.util.Iterator;

import cs.ustc.MaxSATsolver.IClause;
import cs.ustc.MaxSATsolver.ILiteral;
import cs.ustc.MaxSATsolver.ILiteral.VARTYPE;


public class Agent {
	static boolean matrix[][];
	private int varsControlledNum;//控制的变量个数
	private ILiteral[] variablesControlled;//控制的变量
	private ILiteral[] bestAction;
	ILiteral[] action;
	int lastBestAction;	
	/**
	 * 生成Agent 0-1搜索空间矩阵
	 */
	public void initMatrix(){
		int row = (int)Math.pow(2,variablesControlled.length/2);
		//行为2^n n表示控制变量的个数
		matrix = new boolean[row][variablesControlled.length/2];
		for (int j = 0; j < variablesControlled.length/2; j++) {
			boolean flag = true;
			int k = (int)Math.pow(2, variablesControlled.length/2-j-1);
			for (int i = 0; i < row; i++) {
				if ((i%k)==0) {
					flag = !flag;
				}
				if (flag) {
					matrix[i][j] = false;
				}else {
					matrix[i][j] = true;
				}
			}
		}
	}
	
	public void findBestOfAction() {
		int payoffs = 1;
		for (int i = 0; i < varsControlledNum; i++) {
			action[i] = variablesControlled[2*i];
		}
		this.setBestAction(action);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < varsControlledNum; j++) {
				if (matrix[i][j]) {
					action[j] = variablesControlled[2*j];
				}else {
					action[j] = variablesControlled[2*j].opposite();
				} 
			}

			int payoffsTemp = calculatePayoffs(action);
			if (payoffsTemp>payoffs) {
				setBestAction(action);
				payoffs = payoffsTemp;
			}
		}
		for (ILiteral iLiteral : bestAction) {
			for(Iterator<IClause> it = iLiteral.getClauses().iterator(); it.hasNext();){
				it.next().SATNum++;
			}
		}
	}
	
	
	public int calculatePayoffs(ILiteral[] iLiterals) {
		int external = 0;
		// for none league 
//		for (ILiteral iLiteral : iLiterals) {
//			for (Iterator<IClause> it = iLiteral.getClauses().iterator(); it.hasNext();) {
//				it.next();
//				external++;
//			}
//		}
		// for all league 
		for (ILiteral iLiteral : iLiterals) {
			if (iLiteral.vartype == VARTYPE.forbid ) {
				external+=100000;
			}
			for (Iterator<IClause> it = iLiteral.getClauses().iterator(); it.hasNext();) {
				if (it.next().SATNum<=0) {
					external++;
				}
			}
		}
		return external;
	}
	
	
	public ILiteral[] getVariablesControlled() {
		return variablesControlled;
	}
	public void addVariablesControlled(ILiteral literal,int i){
		if (variablesControlled==null) {
			variablesControlled = new ILiteral[2*varsControlledNum];
		}
		variablesControlled[i] = literal;
	}
	public int getVarsControlledNum() {
		return varsControlledNum;
	}
	public void setVarsControlledNum(int varsControlledNum) {
		this.varsControlledNum = varsControlledNum;
		action = new ILiteral[varsControlledNum];
	}
	public ILiteral[] getBestAction() {
		return bestAction;
	}
	public void setBestAction(ILiteral[] bestAction) {
		this.bestAction = bestAction.clone();
	}
}
