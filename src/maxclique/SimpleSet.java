package maxclique;

public interface SimpleSet{

	/** aggiunge un elemento all'insieme
	*/
	public void add (Object elem);

	/** rimuove un elemento dall'insieme e lo restituisce;
	se l'insieme � vuoto restituisce null
	*/
	public Object remove ();

	/** restituisce true se l'insieme � vuoto
	*/
	public boolean isEmpty ();

}