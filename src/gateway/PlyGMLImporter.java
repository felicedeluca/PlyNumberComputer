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

import graphap.EdgeAP;
import graphap.GraphAP;
import graphap.VertexAP;
import utilities.PlyConfigurator;

public class PlyGMLImporter {
	
	public static GraphAP testGraph(){
		
		
		
		VertexAP a = new VertexAP(1, new Apfloat("0", PlyConfigurator.apfloatPrecision()), new Apfloat("0", PlyConfigurator.apfloatPrecision()), "a");
		VertexAP b = new VertexAP(2, new Apfloat("1", PlyConfigurator.apfloatPrecision()), new Apfloat("1", PlyConfigurator.apfloatPrecision()), "b");
		VertexAP c = new VertexAP(3, new Apfloat("2", PlyConfigurator.apfloatPrecision()), new Apfloat("2", PlyConfigurator.apfloatPrecision()), "c");
		VertexAP d = new VertexAP(4, new Apfloat("3", PlyConfigurator.apfloatPrecision()), new Apfloat("3", PlyConfigurator.apfloatPrecision()), "d");
		VertexAP e = new VertexAP(5, new Apfloat("4", PlyConfigurator.apfloatPrecision()), new Apfloat("4", PlyConfigurator.apfloatPrecision()), "e");
		VertexAP f = new VertexAP(6, new Apfloat("5", PlyConfigurator.apfloatPrecision()), new Apfloat("5", PlyConfigurator.apfloatPrecision()), "f");
		VertexAP g = new VertexAP(7, new Apfloat("6", PlyConfigurator.apfloatPrecision()), new Apfloat("6", PlyConfigurator.apfloatPrecision()), "g");
		VertexAP h = new VertexAP(8, new Apfloat("7", PlyConfigurator.apfloatPrecision()), new Apfloat("7", PlyConfigurator.apfloatPrecision()), "h");


		HashMap<Integer, VertexAP> vertices = new HashMap<Integer, VertexAP>();
		vertices.put(a.identifier, a);
		vertices.put(b.identifier, b);
		vertices.put(c.identifier, c);
		vertices.put(d.identifier, d);
		vertices.put(e.identifier, e);
		vertices.put(f.identifier, f);
		vertices.put(g.identifier, g);
		vertices.put(h.identifier, h);

		
		
		HashMap<Integer, EdgeAP> edges = new HashMap<Integer, EdgeAP>();
		EdgeAP ab = new EdgeAP(0, a.identifier, b.identifier);
		EdgeAP bc = new EdgeAP(1, b.identifier, c.identifier);
		EdgeAP cd = new EdgeAP(2, c.identifier, d.identifier);
		EdgeAP de = new EdgeAP(3, d.identifier, e.identifier);
		EdgeAP ef = new EdgeAP(4, e.identifier, f.identifier);
		EdgeAP fg = new EdgeAP(5, f.identifier, g.identifier);
		EdgeAP gh = new EdgeAP(6, g.identifier, h.identifier);




		edges.put(ab.getIdentifier(), ab);
		edges.put(bc.getIdentifier(), bc);
		edges.put(cd.getIdentifier(), cd);
		edges.put(de.getIdentifier(), de);
		edges.put(ef.getIdentifier(), ef);
		edges.put(fg.getIdentifier(), fg);
		edges.put(gh.getIdentifier(), gh);

		return new GraphAP(vertices, edges);

		
	}
	
	public static GraphAP readInput(File f) throws Exception{
		
		HashMap<Integer, VertexAP> vertices = new HashMap<Integer, VertexAP>();
		HashMap<Integer, EdgeAP> edge = new HashMap<Integer, EdgeAP>();
		FileInputStream input = new FileInputStream(f);
		CharSequence fileContents = readFile(input);
		String regexRootForSections="\\s+\\[.*\\s+([^\\]]*\\s+)+]\\s";
		String regexRootForNodes="(id|x|y)\\s?-*[0-9]+(\\.[0-9]*)?";
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
						x =  new Apfloat(splits[1], PlyConfigurator.apfloatPrecision());	
					}
					break;
					case "y":{ y =  new Apfloat(splits[1], PlyConfigurator.apfloatPrecision());
					}
					break;
				//	case "component": component = Integer.parseInt(splits[1]); break;
				}
				substart = nodeMatcher.end();
			}
			
			
			
			vertices.put(id, new VertexAP(id, x, y, label));
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
			edge.put(edgeCounter, new EdgeAP(edgeCounter, source, target));
			edgeCounter++;
			start=edgeGroups.end();			
		}
		
		input.close();
		
//		System.out.println("parsed " + f.getName() + "\nNodes: " + vertices.size() + "\nEdges: " + edge.size());
			
		return new GraphAP(vertices, edge);
	}

	public static CharSequence readFile(FileInputStream input) throws IOException {
		FileChannel channel = input.getChannel();
		ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int)channel.size());
		CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);
		return cbuf;
	}

}