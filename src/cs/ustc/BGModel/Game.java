package cs.ustc.BGModel;

/**
 * 将maxsat问题对应到BGmodel
 * @author ccding  2016年3月6日 上午11:07:01
 * 
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;






import cs.ustc.MaxSATsolver.IClause;
import cs.ustc.MaxSATsolver.IFormula;
import cs.ustc.MaxSATsolver.ILiteral;
import cs.ustc.MaxSATsolver.ILiteral.VARTYPE;



public class Game {	
	private Vector<Agent> agents;//agents in game
	protected int agentsCount;//agents count
	private ILiteral[] variables;//boolean variables
	public ILiteral[] strategy;
	protected ArrayList<IClause> clauses;
	private Stack<ILiteral> announcements;
	public int satClausesCount;
//	protected Set<IClause> satClauses;
//	protected Set<IClause> unsatClauses;
	private float satRate;
	public int maxOccence;
	

	public  Game(IFormula cnf) {
		agentsCount = cnf.getLiterals().length;
		agents = new Vector<Agent>(agentsCount);
		announcements = new Stack<>();
		variables = cnf.getLiterals();
		for (int i = 0;i<variables.length;i++) {
			if (variables[i] == null) {
				variables[i] = ILiteral.createLiteral(i+1);
			}
		}
		strategy = new ILiteral[variables.length];
		clauses = cnf.getClauses();
		satClausesCount = 0;
//		satClausesCount = 0;
//		satClauses = new HashSet<>();
//		unsatClauses = new HashSet<>();
	}
	
	public void setStrategy() {
		int i=0;
		Agent agent;
		for (Iterator<Agent> it = agents.iterator(); it.hasNext();) {
			agent = it.next();
			for (int j=0 ; j<agent.getBestAction().length;j++) {
				strategy[i] = agent.getBestAction()[j];
				i++;
			}
		}
	}
	
	public int announce() {
		int announceNum = 0;
		for (ILiteral literal : strategy) {
			if ((literal.opposite().getWeight()==maxOccence)&&(literal.opposite().getWeight()>0)) {				
//				System.out.println(literal.getId()+"  changed!");
				announcements.push(literal.opposite());
				literal.opposite().vartype = VARTYPE.forbid;
				announceNum++;
			}
		}
		return announceNum;
	}
	
	public void annouceRandom(int num){
		while(num!=0) {
			int index = (int)(Math.random()*strategy.length);
			if (!(strategy[index].opposite().vartype == VARTYPE.forbid)) {
				announcements.push(strategy[index].opposite());
				strategy[index].opposite().vartype = VARTYPE.forbid;
				num--;
			}
		}
	}
	public void cancleAnnounce(int num){
		while(num-- !=0){
			ILiteral popLiteral = announcements.pop();
			popLiteral.vartype = VARTYPE.free;
//			System.out.println(popLiteral.getId()+"  cancle!");
		}
	}
	public void agentUpdateAction(){
		if (announcements==null) {
			return;
		}
		Iterator<Agent> itAgent;
		for (itAgent = agents.iterator();itAgent.hasNext();) {
			for (ILiteral iLiteral : itAgent.next().getBestAction()) {
				for(Iterator<IClause> it = iLiteral.getClauses().iterator(); it.hasNext();){
					it.next().SATNum--;
				}
			}
		}
		for(itAgent=agents.iterator(); itAgent.hasNext();)
			itAgent.next().findBestOfAction();
	}
	
	public void agentUpdateActionNoAnnouncement(){
		Iterator<Agent> itAgent;
		Agent agent;
		for (itAgent = agents.iterator();itAgent.hasNext();) {
			agent = itAgent.next();
			for (ILiteral iLiteral : agent.getBestAction()) {
				for(Iterator<IClause> it = iLiteral.getClauses().iterator(); it.hasNext();){
					it.next().SATNum--;
				}
			}
			agent.findBestOfAction();
		}		
	}
	
	public boolean isGameSatisfied(){
		if (satClausesCount == clauses.size()) {
			return true;
		}else{
			return false;
		}
	}
	
//	public void calUNSATClauses() {
//		if (satClauses==null) {
//			return;
//		}
//		unsatClauses.clear();
//		for (IClause iClause : clauses) {
//			if ( ! satClauses.contains(iClause)) {
//				unsatClauses.add(iClause);
//			}
//		}
//	}
	
	public void calSATClauses() {
		satClausesCount = 0;
		for (Iterator<IClause> it = clauses.iterator(); it.hasNext();) {
			if(it.next().SATNum>0){
				satClausesCount++;
			}
		}
	} 

	public void setAgentsInfo() {
		int w = variables.length/agentsCount;
		for (int i = 0; i < agentsCount; i++) {
			Agent agent = new Agent();
			agent.setVarsControlledNum(w);
			for (int j = 0; j < 2*w; j+=2) {
				agent.addVariablesControlled(variables[w*i+j/2],j);
				agent.addVariablesControlled(variables[w*i+j/2].opposite(),j+1);
			}
			if (Agent.matrix ==null) {
				agent.initMatrix();
			}
			this.addAgent(agent);
		}
	}
	
	public void calUNSATOccurrences() {
		clearOccurrences();
		IClause clause;
		for (Iterator<IClause> it = clauses.iterator(); it.hasNext();) {
			clause = it.next();
			if (clause.SATNum <= 0) {
				for (ILiteral literal : clause.getLiterals()) {
					literal.setWeight(literal.getWeight()+1);
					if (literal.getWeight()>maxOccence) {
						maxOccence = literal.getWeight();
					}
				}
			}
		}
	}
	
	
	public void clearOccurrences(){
		for (ILiteral iLiteral : variables) {
			iLiteral.setWeight(0);
			iLiteral.opposite().setWeight(0);
		}
	}
	
	
	public void calSATRate(){
//		setStrategy();
		calSATClauses();
		setSatRate();
	}
	
	public Vector<Agent> getAgents() {
		return agents;
	}
	public void addAgent(Agent agent) {
		this.agents.add(agent);
	}

	public ILiteral[] getVariables() {
		return variables;
	}
	public void setVariables(IFormula cnf) {
		this.variables = cnf.getLiterals();
	}
	
	public void getStrategyInfo() {
		for (ILiteral literal : strategy) {
			System.out.print(literal.getId()+" ");
		}
		System.out.println();
	}
	public String getGameInfo() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Variables:"+agents.size()+"  Clauses:"+clauses.size());
		stringBuffer.append("\nSATClausesCount:"+satClausesCount);
		stringBuffer.append("\nUNSATClausesCount:"+(clauses.size()-satClausesCount));
		stringBuffer.append("\nSAT rate:"+satRate);
		return stringBuffer.toString();
	}



	public float getSatRate() {
		return satRate;
	}

	public void setSatRate() {
		this.satRate = (float)satClausesCount/(float)clauses.size();
	}
	
	public void printStrategy(ILiteral[] strtegy){
		System.out.println();
		for (int i = 0; i < strtegy.length; i++) {
			System.out.print(strtegy[i].getId()+"  ");
		}
		System.out.println();
	}

	
}

