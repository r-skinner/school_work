//
//Name: Skinner, Ryan
//Project:extra
//Due: December 4, 2017
//Course:cs-240-01-F17
//
//Description:
//Extra credit card shuffling
//
import java.util.ArrayDeque;
public class Deck {
	private ArrayDeque<Card> deck;
	private int length;
	public Deck(int numOfCards) {
		length = numOfCards;
		deck = new ArrayDeque<>();
		for(int i = 0; i < numOfCards; i++)
			deck.add(new Card(i+1));
	}
	public String toString() {
		String s = "";
		for(int i = 0; i < length; i++) {
			Card temp = deck.remove();
			s += "[" + temp.toString() + "]";
			deck.addLast(temp);
		}
		return s;
	}
	
	public boolean equals(Deck d2) {
		return this.toString().equals(d2.toString());
	}
	
	public void inShuffle(){
		ArrayDeque<Card> d = new ArrayDeque<>();
		ArrayDeque<Card> t1 = new ArrayDeque<>();
		ArrayDeque<Card> t2 = new ArrayDeque<>();
		while(!deck.isEmpty()) {
			if(deck.size() == 1)
				t1.add(deck.removeFirst());
			else {
				t1.add(deck.removeFirst());
				t2.add(deck.removeLast());
			}
		}
		while(!t1.isEmpty()) {
			if(!t2.isEmpty())
				d.add(t2.removeLast());
			d.add(t1.removeFirst());
		}
		deck = d;		
	}
	
	public void outShuffle(){
		ArrayDeque<Card> d = new ArrayDeque<>();
		ArrayDeque<Card> t1 = new ArrayDeque<>();
		ArrayDeque<Card> t2 = new ArrayDeque<>();
		while(!deck.isEmpty()) {
			if(deck.size() == 1)
				t1.add(deck.removeFirst());
			else {
				t1.add(deck.removeFirst());
				t2.add(deck.removeLast());
			}
		}
		while(!t1.isEmpty()) {
			d.add(t1.removeFirst());
			if(!t2.isEmpty())
				d.add(t2.removeLast());
		}
		deck = d;	
	}

}

class Card {
	private int value;
	public Card(int value) {
		this.value = value;
	}
	public String toString() {
		return ""+value;
	}
}