//
//Name: Skinner, Ryan
//Project: 2
//Due:October 25, 2017
//Course:cs-240-01-f17
//
//Description:
//	Implementation of Set ADT using a singly linked list
//
class Node<T> {
	public T data;			// data entry
	public Node<T> link;   // link to next node
	
	
	/**
	 * @param data the data that this node holds
	 */
	public Node(T data) {
		this(data, null);
	}

	/**
	 * @param data the data that this node holds
	 * @param node the node to connect after current node
	 */
	public Node(T data, Node<T> node) {
		this.setData(data);
		this.link = node;
	}

	/**
	 * @return node next in the list
	 */
	public Node<T> getNext() {
		return link;
	}

	/**
	 * @param note to be linked
	 */
	public void setNext(Node<T> node) {
		this.link = node;
	}
	/**
	 * @return data held by the node
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data to be held by the node
	 */
	public void setData(T data) {
		this.data = data;
	}
}
