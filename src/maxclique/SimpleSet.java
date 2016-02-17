package maxclique;

public interface SimpleSet{

	/** aggiunge un elemento all'insieme
	*/
	public void add (Object elem);

	/** rimuove un elemento dall'insieme e lo restituisce;
	se l'insieme è vuoto restituisce null
	*/
	public Object remove ();

	/** restituisce true se l'insieme è vuoto
	*/
	public boolean isEmpty ();

}