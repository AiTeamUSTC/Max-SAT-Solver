package cs.ustc.MaxSATsolver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.ListenableGraph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.VertexCovers;
import org.jgrapht.graph.DefaultListenableGraph;
import org.jgrapht.graph.SimpleGraph;



public class GraphTool {
	/**
	 * find independent set cover of graph (greedy)
	 * @param graph
	 * @return
	 */
	public static Set<ILiteral> findIndependentSet(UndirectedGraph<ILiteral, DefaultEdge> graph){
		Set<ILiteral> setCover = VertexCovers.findGreedyCover(graph);
		Set<ILiteral> inSet = new HashSet<>(graph.vertexSet());
		inSet.removeAll(setCover);
		return inSet;
	}
	
	/**
	 * map formula to graph
	 * edge <l1, l2> denotes two literals l1,l2 in the same clause
	 * @param graph
	 * @param formula
	 */
	public static void transFormulaToGraph(UndirectedGraph<ILiteral, DefaultEdge> graph, IFormula formula){
		ArrayList<ILiteral> tmpArrList;
		//vertex is the literals 1..nbVar
		for (Iterator<ILiteral> it = formula.getLiterals().iterator(); it.hasNext();) {
			graph.addVertex(it.next());
			
		}
		//iterate all clauses add edges
		for (IClause c:formula.getClauses()) {
			//if clause c is satisfied, need not consider 
			tmpArrList = c.literals;
			for (int i = 0; i < tmpArrList.size()-1; i++) {
				for (int j = i+1; j < tmpArrList.size(); j++) {
					graph.addEdge(tmpArrList.get(i), tmpArrList.get(j));
				}
			}
			
		}
	}
	
	public static void paintGraph(JFrame frame, UndirectedGraph<ILiteral, DefaultEdge> sg){
		ListenableGraph<String, DefaultEdge> g =
	            new ListenableUndirectedGraph<String, DefaultEdge>(
	                DefaultEdge.class);
		
		MyGraphAdapter mga = new MyGraphAdapter();
		mga.init(g);
		for (ILiteral lit : sg.vertexSet()) {
			g.addVertex(lit.toString());
		}
		for(DefaultEdge me: sg.edgeSet()){
			g.addEdge(sg.getEdgeSource(me).toString(), sg.getEdgeTarget(me).toString());
		}
		frame.getContentPane().add(mga);
		frame.setTitle("Jgraph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
	}
	/**
	 * a listenable directed multigraph that allows loops and parallel edges.
	 **/
    private static class ListenableUndirectedGraph<V, E>
    	extends DefaultListenableGraph<V, E>
        implements UndirectedGraph<V, E>{
    	private static final long serialVersionUID = 1L;

    	ListenableUndirectedGraph(Class<E> edgeClass){
            super(new SimpleGraph<V, E>(edgeClass));
    	}
    }
	
}
