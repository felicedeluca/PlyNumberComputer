package maxclique;

import java.io.*;

public class MaxClique implements BoundSolverMax{
	private byte[][] g; // un grafo rappresentato come matrice di adiacenza

	public MaxClique (byte[][] g){
		this.g = g;
	}

	public double computeUpperBound (int[] y, int i){
		int numCandidates = 0;
		// verifica se gli elementi presenti in y formano una clique e li conta
		for (int h=0; h<i; h++){
			if (y[h]==1){
				numCandidates++;
				for (int j=h+1; j<i; j++)
					if (y[j]==1 && this.g[h][j]==0)
						return 0; // la parte fissata di y non è una clique
			}
		}

		// conta il numero di elementi singoli che è ancora possibile aggiungere nel caso migliore
		for (int h=i; h<y.length; h++){
			boolean candidate = true;
			for (int j=0; j<i; j++)
				if (y[j]==1 && this.g[h][j] == 0){
					candidate = false;
					break;
				}
			if (candidate)
				numCandidates++;
		}
		return numCandidates;
	}


	public double computeLowerBound (int[] y, int i, int[] z){
		double valz = 0;
		for (int j=0; j<y.length; j++){
			z[j] = y[j];
			if (z[j]==1)
				valz++;
		}

		for (int h=i; h<y.length; h++){
			boolean candidate = true;
			for (int j=0; j<h; j++)
				if (z[j]==1 && this.g[h][j] == 0){
					candidate = false;
					break;
				}
			if (candidate){
				z[h]=1;
				valz++;
			}
		}
		return valz;
	}


	public void printSolution (){
		SimpleSet f = new SimpleStack();
		BranchAndBoundMax bbAlg = new BranchAndBoundMax (this, this.g.length, 1, f);
		int[] opt = new int[this.g.length];

		double maxVal = bbAlg.findMaxSolution (opt);

		System.out.println ("Trovata clique di dimensione: " + maxVal);
		for (int h=0; h<opt.length; h++)
			if (opt[h]==1)
				System.out.println ("Vertice " + h);
		System.out.println ();

	}
	
	
	public double findSolution(){
		
		SimpleSet f = new SimpleStack();
		BranchAndBoundMax bbAlg = new BranchAndBoundMax (this, this.g.length, 1, f);
		int[] opt = new int[this.g.length];

		double maxVal = bbAlg.findMaxSolution (opt);

		return maxVal;		
		
	}
	
	public static void readFromConsole() throws Exception{
		
		BufferedReader in = new BufferedReader (new InputStreamReader (System.in,"ASCII"));
		System.out.print ("\nNumero di vertici del grafo?..");
		int n = Integer.parseInt(in.readLine());
		byte[][] g = new byte[n][n];

		while (true){
			System.out.print ("\nScrivere u v per inserire un arco, e FINE per terminare..");
			String arco = in.readLine();
			if (arco.equals("FINE")) break;
			String[] estremo = arco.split(" ");
			int h = Integer.parseInt(estremo[0]);
			int j = Integer.parseInt(estremo[1]);
			g[h][j] = 1;
			g[j][h] = 1;
			System.out.println ("Inserito arco (" + h + "," + j + ")");
		}

		double maxClique =  (new MaxClique (g)).findSolution();
		
		System.out.println("MaxClique: "+maxClique);
		
	}

//	public static void main (String[] args) throws Exception {
//		BufferedReader in = new BufferedReader (new InputStreamReader (System.in,"ASCII"));
//		System.out.print ("\nNumero di vertici del grafo?..");
//		int n = Integer.parseInt(in.readLine());
//		byte[][] g = new byte[n][n];
//
//		while (true){
//			System.out.print ("\nScrivere u v per inserire un arco, e FINE per terminare..");
//			String arco = in.readLine();
//			if (arco.equals("FINE")) break;
//			String[] estremo = arco.split(" ");
//			int h = Integer.parseInt(estremo[0]);
//			int j = Integer.parseInt(estremo[1]);
//			g[h][j] = 1;
//			g[j][h] = 1;
//			System.out.println ("Inserito arco (" + h + "," + j + ")");
//		}
//
//		double maxClique =  (new MaxClique (g)).findSolution();
//		
//		System.out.println("MaxClique: "+maxClique);
//	}

}