package maxclique;

public interface BoundSolverMax{
	public double computeUpperBound (int[] y, int i);
	public double computeLowerBound (int[] y, int i, int[] z);
}