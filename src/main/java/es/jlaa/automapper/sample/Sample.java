package es.jlaa.automapper.sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import es.jlaa.automapper.annotations.AutoMapper;
import es.jlaa.automapper.annotations.AutoMapperCollection;
import es.jlaa.automapper.annotations.AutoMapperField;
import es.jlaa.automapper.annotations.AutoMapperFormat;
import es.jlaa.automapper.options.AdhocFormat;
import es.jlaa.automapper.options.NullValue;
import es.jlaa.automapper.options.Order;
import es.jlaa.formatter.config.Format;
import es.jlaa.formatter.utils.FormatterUtil;

@AutoMapper(format = AdhocFormat.class)
public class Sample {
	
	private Boolean booleanField;
	private Byte byteField;
	private Character characterField;
	private Double doubleField;
	private Float floatField;
	private Integer integerField;
	private Long longField;
	private Short shortField;
	private String stringField;
	
	@AutoMapperFormat(nullValue = NullValue.EMPTY)
	private Integer nullValueEmpty = null;
	
	@AutoMapperField(ignored = true)
	private Integer fieldIgnored;
	
	@AutoMapperField(name = "ORDER_ENUM")
	private Order enumField;
	
	@AutoMapperFormat(pattern = "yyyyMMdd")
	private Date dateField;
	
	@AutoMapperCollection(order = Order.REVERSE, comparator = ItemComparator.class)
	private Item [] itemArray;
	
	private Collection<Item> itemCollection;
	private List<Item> itemList;
	private ArrayList<Item> itemArrayList;
	
	@AutoMapperField(name = "SET")
	private Set<Item> itemSet;
	private HashSet<Item> itemHashSet;
	
	public Sample() {
	}
	
	public Sample(Integer id) {
		this.booleanField = false;
		this.byteField = 12;
		this.characterField = 'B';
		this.doubleField = 1234.0d;
		this.floatField = 2345.0f;
		this.integerField = id;
		this.longField = 900000000l;
		this.shortField = 23;
		this.stringField = "Hello world";
		this.enumField = Order.REVERSE;
		this.dateField = FormatterUtil.parse(Date.class, "24/12/2000", Format.config().withPattern("dd/MM/yyyy"));
		
		this.itemArray = new Item [2];
		this.itemArray[0] = new Item(1l, "A");
		this.itemArray[1] = new Item(2l, "B");
		
		this.itemCollection = new LinkedList<Item>();
		this.itemCollection.add(new Item(3l, "C"));
		this.itemCollection.add(new Item(4l, "D"));
		
		this.itemList = new ArrayList<Item>();
		this.itemList.add(new Item(5l, "E"));
		this.itemList.add(new Item(6l, "F"));
		
		this.itemArrayList = new ArrayList<Item>();
		this.itemArrayList.add(new Item(7l, "G"));
		this.itemArrayList.add(new Item(8l, "H"));
		
		this.itemSet = new HashSet<Item>();
		this.itemSet.add(new Item(9l, "I"));
		this.itemSet.add(new Item(10l, "J"));
		
		this.itemHashSet = new HashSet<Item>();
		this.itemHashSet.add(new Item(11l, "K"));
		this.itemHashSet.add(new Item(12l, "L"));
	}

	public Boolean getBooleanField() {
		return booleanField;
	}

	public void setBooleanField(Boolean booleanField) {
		this.booleanField = booleanField;
	}

	public Byte getByteField() {
		return byteField;
	}

	public void setByteField(Byte byteField) {
		this.byteField = byteField;
	}

	public Character getCharacterField() {
		return characterField;
	}

	public void setCharacterField(Character characterField) {
		this.characterField = characterField;
	}

	public Double getDoubleField() {
		return doubleField;
	}

	public void setDoubleField(Double doubleField) {
		this.doubleField = doubleField;
	}

	public Float getFloatField() {
		return floatField;
	}

	public void setFloatField(Float floatField) {
		this.floatField = floatField;
	}

	public Integer getIntegerField() {
		return integerField;
	}

	public void setIntegerField(Integer integerField) {
		this.integerField = integerField;
	}

	public Long getLongField() {
		return longField;
	}

	public void setLongField(Long longField) {
		this.longField = longField;
	}

	public Short getShortField() {
		return shortField;
	}

	public void setShortField(Short shortField) {
		this.shortField = shortField;
	}

	public String getStringField() {
		return stringField;
	}

	public void setStringField(String stringField) {
		this.stringField = stringField;
	}

	public Order getEnumField() {
		return enumField;
	}

	public void setEnumField(Order enumField) {
		this.enumField = enumField;
	}

	public Date getDateField() {
		return dateField;
	}

	public void setDateField(Date dateField) {
		this.dateField = dateField;
	}

	public Item[] getItemArray() {
		return itemArray;
	}

	public void setItemArray(Item[] itemArray) {
		this.itemArray = itemArray;
	}

	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}

	public ArrayList<Item> getItemArrayList() {
		return itemArrayList;
	}

	public void setItemArrayList(ArrayList<Item> itemArrayList) {
		this.itemArrayList = itemArrayList;
	}

	public Set<Item> getItemSet() {
		return itemSet;
	}

	public void setItemSet(Set<Item> itemSet) {
		this.itemSet = itemSet;
	}

	public HashSet<Item> getItemHashSet() {
		return itemHashSet;
	}

	public void setItemHashSet(HashSet<Item> itemHashSet) {
		this.itemHashSet = itemHashSet;
	}

	public Integer getNullValueEmpty() {
		return nullValueEmpty;
	}

	public void setNullValueEmpty(Integer nullValueEmpty) {
		this.nullValueEmpty = nullValueEmpty;
	}

	public Integer getFieldIgnored() {
		return fieldIgnored;
	}

	public void setFieldIgnored(Integer fieldIgnored) {
		this.fieldIgnored = fieldIgnored;
	}

	public Collection<Item> getItemCollection() {
		return itemCollection;
	}

	public void setItemCollection(Collection<Item> itemCollection) {
		this.itemCollection = itemCollection;
	}

}
