//
//Name: Skinner, Ryan
//Project:extra
//Due: December 4, 2017
//Course:cs-240-01-F17
//
//Description:
//Extra credit card shuffling
//
import java.util.Scanner;
public class Shuffle {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		Deck notShuffled, shuffled;
		int ins = 0;
		int outs = 0;
		
		System.out.print("R. Skinners's Shuffle\n\nEnter deck size:");
		int size = scan.nextInt();
		scan.close();
		notShuffled = new Deck(size);
		shuffled = new Deck(size);
		
		do {
			shuffled.inShuffle();
			ins++;
		}while(!shuffled.equals(notShuffled));
		
		do {
			shuffled.outShuffle();
			outs++;
		}while(!shuffled.equals(notShuffled));
		
		System.out.printf("It took %d in-shuffles and %d out-shuffles to return a deck of size %d to the orignal order.",ins, outs, size);
	}

}
