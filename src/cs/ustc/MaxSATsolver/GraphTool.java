package cs.ustc.MaxSATsolver;
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
	public static Set<IVariable> findIndependentSet(UndirectedGraph<IVariable, DefaultEdge> graph){
		Set<IVariable> setCover = VertexCovers.findGreedyCover(graph);
		Set<IVariable> inSet = new HashSet<>(graph.vertexSet());
		inSet.removeAll(setCover);
		return inSet;
	}
	
	/**
	 * map formula to graph
	 * edge <l1, l2> denotes two literals l1,l2 in the same clause
	 * @param graph
	 * @param formula
	 */
	public static void transFormulaToGraph(UndirectedGraph<IVariable, DefaultEdge> graph, IFormula formula){
		for (Iterator<IVariable> it = formula.variables.iterator(); it.hasNext();) {
			graph.addVertex(it.next());
			
		}
		for (IVariable var: formula.variables) {
			for (IVariable neighbor: var.neighbors) {
				graph.addEdge(var, neighbor);
			}
			
		}
	}
	
	public static void paintGraph(JFrame frame, UndirectedGraph<IVariable, DefaultEdge> sg){
		ListenableGraph<String, DefaultEdge> g =
	            new ListenableUndirectedGraph<String, DefaultEdge>(
	                DefaultEdge.class);
		
		MyGraphAdapter mga = new MyGraphAdapter();
		mga.init(g);
		for (IVariable var : sg.vertexSet()) {
			g.addVertex(var.toString());
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
