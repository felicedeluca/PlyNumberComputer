package gateway;

import java.io.PrintWriter;
import java.io.Writer;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

public class GMLExporter {
	
    private static final String delim = " ";
    private static final String tab1 = "\t";
    private static final String tab2 = "\t\t";
    private static final String tab3 = "\t\t\t";
	
	static void exportToGML(Graph graph){
		
	}
	
	
    private void exportVertices(PrintWriter out, Graph g)
        {
            for (Vertex vertex : g.getVertices()) {
                out.println(tab1 + "node");
                out.println(tab1 + "[");
                out.println(tab2 + "id" + delim + vertex.identifier);
                out.println(tab2 + "graphics");
                out.println(tab2 + "[");
                out.println(tab3 + "x" + delim + vertex.x);
                out.println(tab3 + "y" + delim + vertex.y);
                out.println(tab3 + "w" + delim + (vertex.circleRadius*2));
                out.println(tab3 + "h" + delim + (vertex.circleRadius*2));
                out.println(tab3 + "type" + delim + quoted("ellipse"));
                out.println(tab2 + "]");
                if (vertex.label != null)
                {
                    out.println(tab2 + "label" + delim + quoted(vertex.label));
                }
                out.println(tab1 + "]");
            }
        }
    
    
    private void exportEdges(PrintWriter out, Graph g)
        {
            for (Edge edge : g.getEdges()) {
                out.println(tab1 + "edge");
                out.println(tab1 + "[");
                String s = edge.getSourceIdentifier() + "";
                out.println(tab2 + "source" + delim + s);
                String t = edge.getTargetIdentifier() +"";
                out.println(tab2 + "target" + delim + t);
                if (edge.label != null)
                {
                    out.println(tab2 + "label" + delim + quoted(edge.label));
                }
                if (edge.getIsDirected())
                {
                    out.println(tab2 + "graphics");
                    out.println(tab2 + "[");
                    out.println(tab3 + "targetArrow" + delim + "\"standard\"");
                    out.println(tab2 + "]");
                	
                }
                out.println(tab1 + "]");
            }
        }

    
    public void export(Writer output, Graph g)
    {
        PrintWriter out = new PrintWriter(output);

        out.println("graph");
        out.println("[");
        out.println(tab1 + "label" + delim + quoted(""));
//        if (directed) {
//            out.println(tab1 + "directed" + delim + "1");
//        } else {
            out.println(tab1 + "directed" + delim + "0");
//        }
        exportVertices(out, g);
        exportEdges(out, g);
        out.println("]");
        out.flush();
    }
 
    
    private String quoted(final String s)
    {
        return "\"" + s + "\"";
    }
    
}
