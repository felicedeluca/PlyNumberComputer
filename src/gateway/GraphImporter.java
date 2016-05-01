package gateway;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apfloat.Apfloat;

import plygraph.Edge;
import plygraph.Graph;
import plygraph.Vertex;
import utilities.Configurator;

public class GraphImporter {
	
	public static Graph testGraph(){
		
		
		
		Vertex a = new Vertex(1, new Apfloat("0", Configurator.apfloatPrecision()), new Apfloat("0", Configurator.apfloatPrecision()), "a");
		Vertex b = new Vertex(2, new Apfloat("1", Configurator.apfloatPrecision()), new Apfloat("1", Configurator.apfloatPrecision()), "b");
		Vertex c = new Vertex(3, new Apfloat("2", Configurator.apfloatPrecision()), new Apfloat("2", Configurator.apfloatPrecision()), "c");
		Vertex d = new Vertex(4, new Apfloat("3", Configurator.apfloatPrecision()), new Apfloat("3", Configurator.apfloatPrecision()), "d");
		Vertex e = new Vertex(5, new Apfloat("4", Configurator.apfloatPrecision()), new Apfloat("4", Configurator.apfloatPrecision()), "e");
		Vertex f = new Vertex(6, new Apfloat("5", Configurator.apfloatPrecision()), new Apfloat("5", Configurator.apfloatPrecision()), "f");
		Vertex g = new Vertex(7, new Apfloat("6", Configurator.apfloatPrecision()), new Apfloat("6", Configurator.apfloatPrecision()), "g");
		Vertex h = new Vertex(8, new Apfloat("7", Configurator.apfloatPrecision()), new Apfloat("7", Configurator.apfloatPrecision()), "h");


		HashMap<Integer, Vertex> vertices = new HashMap<Integer, Vertex>();
		vertices.put(a.identifier, a);
		vertices.put(b.identifier, b);
		vertices.put(c.identifier, c);
		vertices.put(d.identifier, d);
		vertices.put(e.identifier, e);
		vertices.put(f.identifier, f);
		vertices.put(g.identifier, g);
		vertices.put(h.identifier, h);

		
		
		HashMap<Integer, Edge> edges = new HashMap<Integer, Edge>();
		Edge ab = new Edge(0, a.identifier, b.identifier);
		Edge bc = new Edge(1, b.identifier, c.identifier);
		Edge cd = new Edge(2, c.identifier, d.identifier);
		Edge de = new Edge(3, d.identifier, e.identifier);
		Edge ef = new Edge(4, e.identifier, f.identifier);
		Edge fg = new Edge(5, f.identifier, g.identifier);
		Edge gh = new Edge(6, g.identifier, h.identifier);




		edges.put(ab.getIdentifier(), ab);
		edges.put(bc.getIdentifier(), bc);
		edges.put(cd.getIdentifier(), cd);
		edges.put(de.getIdentifier(), de);
		edges.put(ef.getIdentifier(), ef);
		edges.put(fg.getIdentifier(), fg);
		edges.put(gh.getIdentifier(), gh);

		return new Graph(vertices, edges);

		
	}
	
	public static Graph readInput(File f) throws Exception{
		
		HashMap<Integer, Vertex> vertices = new HashMap<Integer, Vertex>();
		HashMap<Integer, Edge> edge = new HashMap<Integer, Edge>();
		FileInputStream input = new FileInputStream(f);
		CharSequence fileContents = readFile(input);
		String regexRootForSections="\\s+\\[.*\\s+([^\\]]*\\s+)+]\\s";
		String regexRootForNodes="(id|x|y)\\s?[0-9]+(\\.[0-9]*)?";
		String regexRootForEdges="(source|target)\\s?[0-9]+";
		String nodeRegex = "node"+regexRootForSections;
		String edgeRegex = "edge"+regexRootForSections;
		Pattern pNodeSection = Pattern.compile(nodeRegex);
		Pattern pEdgeSection = Pattern.compile(edgeRegex);
		Pattern pNode = Pattern.compile(regexRootForNodes);
		Pattern pEdge = Pattern.compile(regexRootForEdges);

		Matcher nodeGroups = pNodeSection.matcher(fileContents);
		Matcher edgeGroups = pEdgeSection.matcher(fileContents);
		int start=0;
		while(nodeGroups.find(start)){
			String section = nodeGroups.group();
			Matcher nodeMatcher = pNode.matcher(section);
			int substart=0;
			int id = -1;
			Apfloat x = new Apfloat("0");
			Apfloat y =  new Apfloat("0");
			//int component = -1;
			String label = "";
			while(nodeMatcher.find(substart)){
				String[] splits = nodeMatcher.group().split("\\s");
				switch(splits[0]){
					case "id": id = Integer.parseInt(splits[1]);
							   break;
					case "label": label = splits[1];
					   break;
					case "x":{
						x =  new Apfloat(splits[1], Configurator.apfloatPrecision());
						
					}
					break;
					case "y":{ y =  new Apfloat(splits[1], Configurator.apfloatPrecision()); 

					}
					break;
				//	case "component": component = Integer.parseInt(splits[1]); break;
				}
				substart = nodeMatcher.end();
			}
			
			
			
			vertices.put(id, new Vertex(id, x, y, label));
			start=nodeGroups.end();
		}
		start=0;
		int edgeCounter = 0;
		while(edgeGroups.find(start)){
			String section = edgeGroups.group();
			Matcher edgeMatcher = pEdge.matcher(section);
			int substart=0;
			int source = -1;
			int target = -1;
			while(edgeMatcher.find(substart)){
				String[] splits = edgeMatcher.group().split("\\s");
				switch(splits[0]){
				case "source": source = Integer.parseInt(splits[1]); break;
				case "target": target = Integer.parseInt(splits[1]); break;
				}
				substart = edgeMatcher.end();
			}
			edge.put(edgeCounter, new Edge(edgeCounter, source, target));
			edgeCounter++;
			start=edgeGroups.end();			
		}
		
		input.close();
		
//		System.out.println("parsed " + f.getName() + "\nNodes: " + vertices.size() + "\nEdges: " + edge.size());
			
		return new Graph(vertices, edge);
	}

	public static CharSequence readFile(FileInputStream input) throws IOException {
		FileChannel channel = input.getChannel();
		ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int)channel.size());
		CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);
		return cbuf;
	}

}