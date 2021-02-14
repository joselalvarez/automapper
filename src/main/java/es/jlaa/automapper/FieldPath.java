package es.jlaa.automapper;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class FieldPath {
	
	private static class Node {
		
		private String fieldName;
		private boolean collection;
		private boolean collectionItem;
		private int index;
		
		private Node(String src) {

			if (src == null || src.trim().isEmpty()) throw new IllegalArgumentException("Un nodo del path no puede ser vacio");
			fieldName = "";
			collection = src.contains("[]");
			collectionItem = !collection && src.contains("[");
			index = -1;
			
			StringTokenizer tokenizer = new StringTokenizer(src.trim(), "[]");
			
			if (tokenizer.countTokens() == 1) {
				if (collectionItem){
					index = Integer.parseInt(tokenizer.nextToken());
				}else {
					fieldName = tokenizer.nextToken().trim();
				}
			}else if (tokenizer.countTokens() > 1) {
				fieldName = tokenizer.nextToken().trim();
				index = Integer.parseInt(tokenizer.nextToken());
			}
			
		}
		
		@Override
		public String toString() {
			if (collection) return fieldName + "[]";
			if (collectionItem) return fieldName + "[" + index + "]";
			return fieldName;
		}
	}
	
	private LinkedList<Node> nodes;
	
	public static FieldPath root() {
		return new FieldPath("#");
	}
	
	public FieldPath(String path) {
		nodes = new LinkedList<Node>();
		StringTokenizer tokenizer = new StringTokenizer(path.trim(), ".");
		while (tokenizer.hasMoreTokens()) {
			nodes.add(new Node(tokenizer.nextToken()));
		}
	}
	
	public String getFieldName() {
		return nodes.getLast().fieldName;
	}
	
	public boolean isCollection() {
		return nodes.getLast().collection;
	}
	
	public boolean isCollectionItem() {
		return nodes.getLast().collectionItem;
	}
	
	public boolean isRoot() {
		return this.equals(root());
	}
	
	public int getIndex() {
		return nodes.getLast().index;
	}
	
	public FieldPath getParent() {
		if (nodes.size() == 1) return null;
		return new FieldPath(toString(nodes, nodes.size() - 1));
	}
	
	public FieldPath add(FieldPath path) {
		return new FieldPath(this.toString() + "." + path.toString());
	}
	
	public FieldPath addCollection() {
		return new FieldPath(this.toString() + "[]");
	}
	
	public FieldPath addCollectionItem(long pos) {
		return new FieldPath(this.toString() + "[" + pos + "]");
	}
	
	private String toString(List<Node> nodes, int n) {
		String path = "";
		for (int i = 0; i < n && i < nodes.size(); i++) {
			path += i > 0 ? "." + nodes.get(i).toString() : nodes.get(i).toString();
		}
		return path;
	}
	
	@Override
	public String toString() {
		return toString(nodes, nodes.size());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FieldPath) {
			return toString().equals(obj.toString());
		}
		return false;
	}
	
	
}
