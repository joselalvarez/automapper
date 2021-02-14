package es.jlaa.automapper;

import static org.junit.Assert.assertTrue;

import java.util.Hashtable;

import org.junit.Test;


import es.jlaa.automapper.sample.Sample;
import es.jlaa.automapper.utils.AutoMapperUtil;

public class AutoMapperTest {

	@Test
	public void mapSample() {
		Sample sample = new Sample(1);
		
		Hashtable<String, Object> sampleTable = AutoMapperUtil.map(new Hashtable<String, Object>(), sample);
		
		assertTrue(sampleTable.get("NULL_VALUE_EMPTY").equals(""));
		assertTrue(sampleTable.get("BOOLEAN_FIELD").equals("0"));
		assertTrue(!sampleTable.containsKey("FIELD_IGNORED"));
		assertTrue(sampleTable.get("DATE_FIELD").equals("20001224"));
		assertTrue(((Hashtable[])sampleTable.get("ITEM_ARRAY")).length == 2);
		assertTrue(((Hashtable[])sampleTable.get("ITEM_ARRAY"))[0].get("ID").equals("0002"));
		
		Sample copy = AutoMapperUtil.map(new Sample(), sampleTable);
		
		assertTrue(sample.getBooleanField().equals(copy.getBooleanField()));
		assertTrue(sample.getByteField().equals(copy.getByteField()));
		assertTrue(sample.getCharacterField().equals(copy.getCharacterField()));
		assertTrue(sample.getDoubleField().equals(copy.getDoubleField()));
		assertTrue(sample.getFloatField().equals(copy.getFloatField()));
		assertTrue(sample.getIntegerField().equals(copy.getIntegerField()));
		assertTrue(sample.getLongField().equals(copy.getLongField()));
		assertTrue(sample.getShortField().equals(copy.getShortField()));
		assertTrue(sample.getStringField().equals(copy.getStringField()));
		assertTrue(sample.getEnumField().equals(copy.getEnumField()));
		assertTrue(sample.getDateField().equals(copy.getDateField()));
		assertTrue(sample.getNullValueEmpty() == null);
		
		assertTrue(sample.getItemArray().length == copy.getItemArray().length);
		assertTrue(sample.getItemCollection().size() == copy.getItemCollection().size());
		assertTrue(sample.getItemList().size() == copy.getItemList().size());
		assertTrue(sample.getItemArrayList().size() == copy.getItemArrayList().size());
		assertTrue(sample.getItemSet().size() == copy.getItemSet().size());
		assertTrue(sample.getItemHashSet().size() == copy.getItemHashSet().size());
		
		assertTrue(copy.getItemArray()[0].getId() == 2);
		assertTrue(copy.getItemArray()[0].getStringField() == "B");
	}
}
