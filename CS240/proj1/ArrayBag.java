//
//Name: Skinner, Ryan
//Project: 1 
//Due:October 16, 2017
//Course:cs-240-01-f17
//
//Description:
//	Implementation of ArrayBag ADT
//

public final class ArrayBag<T> implements BagInterface<T>{
	
	private int numberOfEntries;
	private final T[] bag;
	private final static int DEFAULT_CAPACITY = 25;
	private boolean initialized = false;
	private static final int MAX_CAPACITY = 10000;
	
	public ArrayBag() {
		this(DEFAULT_CAPACITY);
	}
	
	public ArrayBag(int desiredCapacity) {
		if(desiredCapacity <= MAX_CAPACITY) {
			@SuppressWarnings("unchecked")
			T[] tempBag = (T[])new Object[desiredCapacity];
			bag = tempBag;
			numberOfEntries = 0;
			initialized = true;
		}
		else {
			throw new IllegalStateException("Attempted to make bag size larger than max allowed size.");
		}
	}
	
	public void checkInitialization() {
		if(!initialized)
			throw new SecurityException("ArrayBag object is not initialized properly");
	}

	public boolean isArrayFull() {
		checkInitialization();
		return numberOfEntries >= bag.length;
	}
	
	public int getIndexOf(T t) {
		for(int i = 0; i < numberOfEntries; i++) {

			if(bag[i].equals(t)) {
				return i;
				}
		}
		return -1;
	}
	
	@Override
	public boolean isEmpty() {
		checkInitialization();
		return numberOfEntries == 0;
	}

	@Override
	public boolean add(T t) {
		checkInitialization();
		if(isArrayFull())
			return false;
		bag[numberOfEntries++] = t;
		return true;
	}

	@Override
	public boolean remove(T t) {
		checkInitialization();
		int index = getIndexOf(t);
		if(index < 0)
			return false;
		bag[index] = bag[--numberOfEntries];
		bag[numberOfEntries] = null;
		return true;
	}

	@Override
	public boolean contains(T t) {
		checkInitialization();
		for(int i = 0; i < numberOfEntries; i++) {
			if(bag[i].equals(t))
				return true;
		}
		return false;
		
	}

	@Override
	public int getCurrentSize() {
		checkInitialization();
		return numberOfEntries;
	}

	@Override
	public int getFrequencyOf(T t) {
		checkInitialization();
		int count = 0;
		for(int i = 0; i < numberOfEntries; i++) {
			if(bag[i].equals(t))
				count++;
		}
		return count;
	}

	@Override
	public void clear() {
		checkInitialization();
		while(!isEmpty())
			remove();
	}

	@Override
	public T remove() {
		checkInitialization();
		T result = null;
		if(numberOfEntries > 0) {
			result = bag[numberOfEntries -1];
			bag[--numberOfEntries] = null;
		}
		return result;
	}

	@Override
	public T[] toArray() {
		checkInitialization();
		@SuppressWarnings("unchecked")
		T[] result = (T[])new Object[numberOfEntries];
		for(int i = 0; i < numberOfEntries; i++)
			result[i] = bag[i];
		return result;
	}
	
	public String toString() {
		checkInitialization();
		String s = "";
		for(T t:toArray()){
			s += "["+t+"]";
		}
		return s;
	}
	
}