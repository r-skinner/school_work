//
//Name: Skinner, Ryan
//Project: 1 
//Due:October 16, 2017
//Course:cs-240-01-f17
//
//Description:
//	Implementation of ArrayBag ADT
//

public interface BagInterface<T> { 
	boolean isEmpty();
	boolean add(T t);
	boolean remove(T t);
	boolean contains(T t);
	int getCurrentSize();
	int getFrequencyOf(T t);
	void clear();
	T remove();
	T[] toArray();
	
	
	
} 