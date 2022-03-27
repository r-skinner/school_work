//
//Name: Skinner, Ryan
//Project: 2
//Due:October 25, 2017
//Course:cs-240-01-f17
//
//Description:
//	Implementation of Set ADT using a singly linked list
//

public class LinkedSet<T> implements SetInterface<T>{

	Node<T> head;
	private int size;
	
	public LinkedSet() {
		head = null;
		size = 0;
	}	
	
	@Override
	public boolean contains(T t) {
		boolean found = false;
		Node<T> node = head;
		while(!found && node != null) {
			if(node.getData().equals(t))
				found = true;
			else {
				node = node.getNext();
			}
		}
		return found;
	}

	@Override
	public boolean remove(T t) {
		boolean removed = false;
		Node<T> node = head;
		Node<T> nextNode = node.getNext();
		if(head.getData().equals(t)) {
			head = nextNode;
			removed = true;
			size--;
		}
		
		while(!removed && nextNode != null) {
			if(nextNode.getData().equals(t)) {
				node.setNext(nextNode.getNext());
				removed = true;
				size--;
			}
			node = nextNode;
			nextNode = nextNode.getNext();
		}
		return removed;
	}
	@Override
	public T remove(){
		Node<T> node = head;
		if(node != null)
			head = head.getNext();
		return node.getData();
	}
	@Override
	public void clear(){
		Node node = head;
		while(node != null && node.getNext() != null){
			remove();
			node = head;
		}
	}
	@Override
	public boolean add(T t) {
		boolean added = false;
		if(!contains(t)) {
			Node<T> node = new Node<T>(t, head);
			head = node;
			size++;
			added = true;
		}
		return added;
	}

	@Override
	public int getCurrentSize() {
		return size;
	}
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	//@Override
	public boolean subset(LinkedSet<T> s) {
		boolean contain = true;
		Node<T> node = head;
		while(node != null && contain == true) {
			contain = s.contains(node.getData());
			node = node.getNext();
		}
		return contain;
	}

	//@Override
	public boolean equals(LinkedSet<T> s) {
		return s.subset(this) && this.subset(s);
	}

	//@Override
	public LinkedSet<T> union(LinkedSet<T> s) {
		LinkedSet<T> list = new LinkedSet<T>();
		Node<T> node = head;
		while(node!=null) {
			list.add(node.getData());
			node = node.getNext();
		}
		node = s.head;
		while(node!=null) {
			list.add(node.getData());
			node = node.getNext();
		}
		return list;
	}

	//@Override
	public LinkedSet<T> intersection(LinkedSet<T> s) {
		LinkedSet<T> list = new LinkedSet<T>();
		Node<T> node = head;
		while(node!=null) {
			if(s.contains(node.getData())){
				list.add(node.getData());
			}
			node = node.getNext();
		}
		return list;
	}

	//@Override
	public LinkedSet<T> complement(LinkedSet<T> s) {
		LinkedSet<T> list = new LinkedSet<T>();
		Node<T> node = head;
		while(node!=null) {
			if(!s.contains(node.getData())){
				list.add(node.getData());
			}
			node = node.getNext();
		}
		return list;
	}
	
	@Override
	public String toString() {
		String str = "{";
		Node<T> node = head;
		while(node != null) {
			str += node.getData() + ", ";
			node = node.getNext();
		}
		str = str.replaceAll(", $", "");
		return str + "}";
	}

	public T[] toArray(){
		@SuppressWarnings("unchecked")
		T[] array = (T[])(new Object[getCurrentSize()]);
		Node<T> node = head;
		for(int i = 0; i < getCurrentSize(); i++){
			array[i] = node.getData();
			node = node.getNext();
		}
		return array;
	}
}

