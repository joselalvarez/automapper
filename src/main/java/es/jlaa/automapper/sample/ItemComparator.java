package es.jlaa.automapper.sample;

import java.util.Comparator;

public class ItemComparator implements Comparator<Item>{

	@Override
	public int compare(Item a, Item b) {
		return a.getStringField().compareTo(b.getStringField());
	}

}
