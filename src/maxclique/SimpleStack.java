package maxclique;

import java.util.*;

public class SimpleStack implements SimpleSet{
	private LinkedList list;

	public SimpleStack (){
		this.list = new LinkedList();
	}

	/** aggiunge un elemento all'insieme
	*/
	public void add (Object elem){
		list.add (elem);
	}

	/** rimuove un elemento dall'insieme e lo restituisce
	*/
	public Object remove (){
		Object elem = null;
		if (!list.isEmpty())
			elem = list.removeLast();
		return elem;
	}

		/** restituisce true se l'insieme è vuoto
		*/
	public boolean isEmpty (){
		return list.isEmpty();
	}
}