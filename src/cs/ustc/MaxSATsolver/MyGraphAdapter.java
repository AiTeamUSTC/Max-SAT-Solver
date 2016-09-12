package cs.ustc.MaxSATsolver;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JApplet;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphModelAdapter;



public class MyGraphAdapter extends JApplet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
	private static final Dimension DEFAULT_SIZE = new Dimension(800,600);
	private JGraphModelAdapter<String, DefaultEdge> jgAdapter;
	
	public void init(ListenableGraph<String, DefaultEdge> g){
		jgAdapter = new JGraphModelAdapter<>(g);
		JGraph jGraph = new JGraph(jgAdapter);
		getContentPane().add(jGraph);
		adjustDisplaySettings(jGraph);
		resize(DEFAULT_SIZE);
		

	}
    private void adjustDisplaySettings(JGraph jg)
    {
        jg.setPreferredSize(DEFAULT_SIZE);

        Color c = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter("bgcolor");
        } catch (Exception e) {
        }

        if (colorStr != null) {
            c = Color.decode(colorStr);
        }

        jg.setBackground(c);
    }
	

}
