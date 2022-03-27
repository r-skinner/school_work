//
//Name: Skinner, Ryan
//Project: 1 
//Due:October 16, 2017
//Course:cs-240-01-f17
//
//Description:
//	Implementation of ArrayBag ADT
//

public class BagTest {

	public static void main(String[] args) {
		System.out.println("R. Skinner's Bag ADT\n");
		
		System.out.println("Testing Security Features:");
		ArrayBag<Integer> a;
		try {
			a = new ArrayBag<Integer>(100000);
		}
		catch (IllegalStateException e) {

			System.out.println("\t"+e);
		}
		
		System.out.println("\nMaking bag with of size 6.");
		a = new ArrayBag<Integer>(6);
		System.out.println("\tBag:"+a.toString());
		System.out.println("\tNumber Of Entries: "+a.getCurrentSize());
		System.out.println("\tEmpty: "+ a.isEmpty());
		System.out.println("\tFull: "+a.isArrayFull());
		
		
		System.out.println("\nFilling Array:");
		for(int i = 0; i < 7; i++) {
			System.out.println("\tAdding " + i + " to bag: " + (a.add(i+1)?"Success":"Failed"));
			System.out.println("\t\tBag:"+a.toString());
			System.out.println("\t\tNumber Of Entries: "+a.getCurrentSize());
			System.out.println("\t\tEmpty: "+ a.isEmpty());
			System.out.println("\t\tFull: "+a.isArrayFull());
		}
		System.out.println("\nContains 5: "+ a.contains(5));
		System.out.println("Contains 7: "+ a.contains(7));
		

		System.out.println("\nRemoving any item: Removed "+ a.remove());
		System.out.println("\t\tBag:"+a.toString());
		System.out.println("Removing number 2: "+ (a.remove(2)?"Success":"Failed"));
		System.out.println("\t\tBag:"+a.toString());
		System.out.println("Removing number 10: "+ (a.remove(10)?"Success":"Failed"));
		System.out.println("\t\tBag:"+a.toString());
		
		System.out.println("Adding number 5 twice: "+ (a.add(5) && a.add(5)?"Success":"Failed"));
		System.out.println("\t\tBag:"+a.toString());
		
		System.out.println("\nFrequency of 1: " + a.getFrequencyOf(1));
		System.out.println("Frequency of 5: " + a.getFrequencyOf(5));
		System.out.println("Frequency of 10: " + a.getFrequencyOf(10));


		System.out.println("\nEmptying bag.");
		a.clear();
		System.out.println("\tBag:"+a.toString());
		System.out.println("\tNumber Of Entries: "+a.getCurrentSize());
		System.out.println("\tEmpty: "+ a.isEmpty());
		System.out.println("\tFull: "+a.isArrayFull());
		
	}

}
