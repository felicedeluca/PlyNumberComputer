package maxclique;

public class BranchAndBoundMax{

	private BoundSolverMax bs; 	// l'oggetto che calcola i bound
	private SimpleSet f;		// insieme dei nodi dai quali continuare la visita
	int n; 						// dimensione dell'array che codifica i nodi dell'albero
	int k;						// massimo valore in una cella dell'array (k=1 per array binari)

	public BranchAndBoundMax (BoundSolverMax bs, int n, int k, SimpleSet f){
		this.bs = bs;
		this.n = n;
		this.k = k;
		this.f = f;
	}


	public double findMaxSolution (int[] opt){
		int[] solMax = null;	// la migliore soluzione corrente
		double valSolMax = Double.NEGATIVE_INFINITY;
		f.add(new TreeNode (new int[n], 0)); // aggiunge la radice ad f

		while (!f.isEmpty()){
			TreeNode node = (TreeNode)f.remove();
			int[] y = node.getArray();
			int i = node.getLevel();

			//System.out.print ("Estratto nodo: " + node + "\n");	// debug


			double ub = bs.computeUpperBound(y,i);
			//System.out.println ("upper bound = " + ub); // debug
			if (ub >= valSolMax){
				int[] z = new int[n];
				double valz = bs.computeLowerBound(y,i,z);
				//System.out.println ("Nuovo lower bound = " + valz); // debug
				if (valz > valSolMax){// aggiorna la migliore soluzione corrente
					solMax = z;
					valSolMax = valz;
					//System.out.println ("Lower bound aggiornato = "); // debug
				}
				if (i < n)
					for (int j=0; j<=k; j++){
						int[] yChild = new int[n];
						for (int h=0; h<i; h++) // copia le celle fissate di y in yChild
							yChild[h] = y[h];
						yChild[i]=j; // assegna un valore alla prossima cella
						TreeNode newChild = new TreeNode (yChild,i+1);
						f.add(newChild);
						//System.out.println ("   - Inserito nodo: " + newChild); // debug
					}
			}
			else{// taglio
				//System.out.println ("taglio effettuato");	//debug
			}

		} //end while
		for (int h=0; h<n; h++)
			opt[h]=solMax[h];
	return valSolMax;
	}


}



class TreeNode{
	private int[] v;	// l'array che rappresenta il nodo
	private int i;		// il livello del nodo (cioè il numero di celle fissate)

	public TreeNode (int[] v, int i){
		this.v = v;
		this.i = i;
	}

	public int[] getArray (){
		return this.v;
	}

	public int getLevel (){
		return this.i;
	}

	public String toString (){
		String s = "";
		for (int h=0; h<i; h++)
			s += v[h] + " ";
		for (int h=i; h<v.length; h++)
			s += "- ";
		return s;
	}
}