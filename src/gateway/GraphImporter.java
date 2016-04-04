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

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import utilities.Configurator;

public class GraphImporter {
	
/*	public static Graph testGraph(){
		
		
		Vertex one = new Vertex(0, 0, 0);
		Vertex two = new Vertex(1, 1, 0);
		Vertex three = new Vertex(2, 0, 1);
		Vertex four = new Vertex(3, 1, 1);
		Vertex five = new Vertex(4, 0, -0.2);
		Vertex six = new Vertex(5, 0, -0.5);


		HashMap<Integer, Vertex> vertices = new HashMap<Integer, Vertex>();
		vertices.put(one.identifier, one);
		vertices.put(two.identifier, two);
		vertices.put(three.identifier, three);
		vertices.put(four.identifier, four);
		vertices.put(five.identifier, five);

		
		
		HashMap<Integer, Edge> edges = new HashMap<Integer, Edge>();
		Edge onetwo = new Edge(0, one.identifier, two.identifier);
		Edge onethree = new Edge(1, one.identifier, three.identifier);
		Edge twofour = new Edge(2, two.identifier, four.identifier);
		Edge threefour = new Edge(3, three.identifier, four.identifier);
		Edge onefour = new Edge(4, one.identifier, four.identifier);
		Edge twofive = new Edge(5, two.identifier, five.identifier);



		edges.put(onetwo.getIdentifier(), onetwo);
		edges.put(onethree.getIdentifier(), onethree);
		edges.put(twofour.getIdentifier(), twofour);
		edges.put(threefour.getIdentifier(), threefour);
		edges.put(onefour.getIdentifier(), onefour);
		edges.put(twofive.getIdentifier(), twofive);



		return new Graph(vertices, edges);

		
	}*/
	
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
			int component = -1;
			String label = "";
			while(nodeMatcher.find(substart)){
				String[] splits = nodeMatcher.group().split("\\s");
				switch(splits[0]){
					case "id": id = Integer.parseInt(splits[1]);
							   break;
					case "label": label = splits[1];
					   break;
					case "x": x =  new Apfloat(splits[1], Configurator.apfloatPrecision()); break;
					case "y": y =  new Apfloat(splits[1], Configurator.apfloatPrecision()); break;
					case "component": component = Integer.parseInt(splits[1]); break;
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