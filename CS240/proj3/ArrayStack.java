//
//Name: Skinner, Ryan
//Project:3
//Due: 11-27-17
//Course:cs-240-01-F18
//
//Description:
//A brief description of the project.
//
public class ArrayStack <T>{
    private int size;
    private int location;
    private T[] stack;

	@SuppressWarnings("unchecked")
	public ArrayStack(int size){
        location=-1;
        stack=(T[])(new Object[size]);
        this.size=size;
    }
    
    public boolean isEmpty() {
        return location<0;
    }
    
    public boolean isFull() {
        return (location==size-1);
    }
    
    
	public void push(T t) {
            stack[++location]=t;
    }
    
    public T pop() {

    	if(isEmpty()) {
    		return null;
    	}
    	else {

    		T out =stack[location];
    		stack[location--]=null;
    		return out;
       }
    }

    public T peek() {
    	if(!isEmpty())
    		return stack[location];
    	else
    		return null;
    }

	public void empty() {
		while(!isEmpty())
			pop();
		
	}
}