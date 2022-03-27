//
//Name: Skinner, Ryan
//Project: 2
//Due:October 25, 2017
//Course:cs-240-01-f17
//
//Description:
//	Implementation of Set ADT using a singly linked list
//
public class SetTest {

	public static void main(String[] args) {
		LinkedSet<Integer> listA = new LinkedSet<Integer>();
		System.out.println("R. Skinner's Set ADT");
		System.out.println("A: " + listA);
		System.out.println("A length: " + listA.getCurrentSize());
		System.out.println("\nAdd 1 to A: " + (listA.add(1)?"Success":"Failed"));
		System.out.println("Add 2 to A: " + (listA.add(2)?"Success":"Failed"));
		System.out.println("Add 3 to A: " + (listA.add(3)?"Success":"Failed"));
		System.out.println("Add 4 to A: " + (listA.add(4)?"Success":"Failed"));
		System.out.println("Add 1 to A: " + (listA.add(1)?"Success":"Failed"));
		System.out.println("A: " + listA);
		System.out.println("A length: " + listA.getCurrentSize());
		System.out.println("\nRemove 4 from A: " + (listA.remove(4)?"Success":"Failed"));
		System.out.println("A: " + listA);
		System.out.println("\nRemove 4 from A: " + (listA.remove(4)?"Success":"Failed"));
		System.out.println("A: " + listA);
		System.out.println("A length: " + listA.getCurrentSize());
		System.out.println("Remove 2 from A: " + (listA.remove(2)?"Success":"Failed"));
		System.out.println("A: " + listA);
		System.out.println("A length: " + listA.getCurrentSize());
		System.out.println("\nA contains 1: " + (listA.contains(1)?"Yes":"No"));
		System.out.println("A contains 2: " + (listA.contains(2)?"Yes":"No"));
		listA.add(5);
		listA.add(7);
		LinkedSet<Integer> listB = new LinkedSet<Integer>();
		listB.add(3);
		listB.add(4);
		listB.add(5);
		LinkedSet<Integer> listC = new LinkedSet<Integer>();
		System.out.println("\nList A: " + listA);
		System.out.println("List B: " + listB);
		System.out.println("List C: " + listC);
		System.out.println("A union B: " + listA.union(listB));
		System.out.println("A intersection B: " + listA.intersection(listB));
		System.out.println("A complement B: " + listA.complement(listB));
		System.out.println("C is a subset of A: " + listC.subset(listA));
		System.out.println("C is a subset of B: " + listC.subset(listB));
		System.out.println("C is a subset of C: " + listC.subset(listC));
		System.out.println("A is a subset of B: " + listA.subset(listB));
		System.out.println("(A intersection B) is a subset of A: " + listA.intersection(listB).subset(listA));
		

		//Case 1
		System.out.println("\nCase 1:");
		listA = new LinkedSet<Integer>();
		listA.add(1);
		listA.add(2);
		listA.add(3);
		listB = new LinkedSet<Integer>();
		listB.add(2);
		listB.add(3);
		listB.add(1);
		runTests(listA,listB);
		
		//Case 2
		System.out.println("\nCase 2:");
		listA.remove(2);
		runTests(listA,listB);
		
		//Case 3
		System.out.println("\nCase 3:");
		listA.add(4);
		listB.add(5);
		runTests(listA,listB);
		
		//Case 4
		System.out.println("\nCase 4:");
		listA.remove(1);
		listB.remove(3);
		runTests(listA,listB);
		
		//Case 5
		System.out.println("\nCase 5:");
		listA = new LinkedSet<Integer>();
		runTests(listA,listB);
		
	}
	public static <T> void runTests(LinkedSet<T> a, LinkedSet<T> b) {
		System.out.println("\tList A: " + a);
		System.out.println("\tList B: " + b);
		System.out.println("\tA equals B: " + a.equals(b));
		System.out.println("\tA is a subset of B: " + a.subset(b));
		System.out.println("\tA union B: " + a.union(b));
		System.out.println("\tA intersetion B: " + a.intersection(b));
		System.out.println("\tA complement B: " + a.complement(b));
	}

}
