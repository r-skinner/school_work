import java.util.*;

public class proj2 {
    static int bagCapacity;
    static int numberOfItems;
    static ArrayList<Item> items = new ArrayList<>();
    private static int totalWeight = 0;

    public static void main(String[] args) throws Exception{

        Scanner scan = new Scanner(System.in);

        System.out.print("How many items: ");
        numberOfItems = scan.nextInt();

        for(int i = 0; i < numberOfItems; i++){
            System.out.print("Enter weight of item " + i + ": ");
            int w = scan.nextInt();
            System.out.print("Enter value of item " + i + ": ");
            int v = scan.nextInt();
            items.add(new Item(w,v));
        }
        System.out.print("How much can the bag hold: ");
        bagCapacity = scan.nextInt();




        Collections.sort(items);
        Collections.reverse(items);

        for(Item i : proj2.items)
            totalWeight += i.getWeight();


        Algorithm brute = new Algorithm("brute");
        Algorithm backtrack = new Algorithm("backtrack");
        Algorithm bnb = new Algorithm("bnb");

        brute.solve();

        backtrack.solve();

        bnb.solve();

        System.out.printf("The %s method found the solution in %d steps\n", "Brute-Force", brute.getCount() );
        System.out.printf("The %s method found the solution in %d steps\n", "Backtracking", backtrack.getCount());
        System.out.printf("The %s method found the solution in %d steps\n", "Branch-and-Bound", bnb.getCount());

        System.out.printf("You can get a profit of %.0f if you take items: ", brute.getMaxProfit());
        for(int i = 0; i < numberOfItems; i ++)
            if(brute.result[i] == 1)
                System.out.printf(" %d ", i );
    }
}

class Algorithm {
    private int type;
    private int count = 1;
    private double maxProfit = 0;
    int[] result = new int[proj2.numberOfItems];
    private int[] tempResult = new int[proj2.numberOfItems];
    private Tree tree = new Tree();
    private PriorityQueue<Node> q = new PriorityQueue<>(new Comparator<Node>() {
        public int compare(Node a, Node b) {
            return b.compareTo(a);
        }
    });


    Algorithm(String type) throws Exception {
        switch (type) {
            case "brute": this.type = 0;
                break;
            case "backtrack": this.type = 1;
                break;
            case "bnb": this.type = 2;
                break;
            default:
                throw new Exception(type + " is not a valid algorithm.");
        }
    }

    void solve(){solve(tree.getHead());}

    private void solve(Node node){
        switch(this.type){
            case 0:
                if(node.getTotalValue() > maxProfit && node.getTotalWeight() <= proj2.bagCapacity){
                    maxProfit = node.getTotalValue();
                    result = tempResult.clone();
                }
                if(node.getDepth() < proj2.numberOfItems) {
                    Node n2;

                    n2 = setRight(node);
                    tempResult[node.getDepth()] = 1;
                    solve(n2);


                    n2 = setLeft(node);
                    tempResult[node.getDepth()] = 0;
                    solve(n2);
                }
                break;
            case 1:
                if(node.getTotalValue() > maxProfit && node.getTotalWeight() <= proj2.bagCapacity){
                    maxProfit = node.getTotalValue();
                    result = tempResult.clone();
                }

                if(!isPromising(node)) return;


                if(node.getDepth() < proj2.numberOfItems) {
                    Node n2;

                    n2 = setRight(node);
                    tempResult[node.getDepth()] = 1;
                    solve(n2);


                    n2 = setLeft(node);
                    tempResult[node.getDepth()] = 0;
                    solve(n2);
                }
                break;

            case 2:

                q.add(node);
                Node n;
                while(!q.isEmpty()){
                    n = q.poll();
                    n.setBound();

                    if(n.getBound() > maxProfit){

                        Node n2 = setRight(n);
                        n2.setBound();
                        if(n2.getTotalWeight() <= proj2.bagCapacity && n2.getTotalValue() > maxProfit)
                            maxProfit = n2.getTotalValue();
                        if(n2.getBound() > maxProfit) {
                            tempResult[node.getDepth()] = 1;
                            q.add(n2);
                        }

                        n2 = setLeft(n);
                        n2.setBound();
                        if(n2.getBound() > maxProfit){
                            tempResult[node.getDepth()] = 1;
                            q.add(n2);
                        }

                    }
                }
                break;
        }
    }


    private Node setRight(Node node){
        count++;
        Node n = new Node(null, null,
                node.getDepth()+1,
                node.getTotalWeight() + proj2.items.get(node.getDepth()).getWeight(),
                node.getTotalValue()+ proj2.items.get(node.getDepth()).getValue());
        node.setRight(n);
        return n;
    }

    private Node setLeft(Node node){
        count++;
        Node n = new Node(null, null,
                node.getDepth()+1,
                node.getTotalWeight(),
                node.getTotalValue());
        node.setLeft(n);
        return n;
    }


    private boolean isPromising(Node node){
        node.setBound();
        return (node.getBound() > maxProfit);
    }

    int getCount() {
        return count;
    }

    double getMaxProfit() {
        return maxProfit;
    }
}

class Item implements Comparable {
    private double ratio, weight, value;
    Item (double weight, double value){
        this.weight = weight;
        this.value = value;
        this.ratio = value/weight;
    }
     double getRatio(){
        return ratio;
    }
    double getWeight(){
        return weight;
    }
    double getValue(){
        return value;
    }
    @Override
    public String toString() {
        //return "W: " + weight + ", $: " + maxProfit;
        return weight+" "+value;
    }

    @Override
    public int compareTo(Object o) {
        return (int)(this.ratio - ((Item)o).getRatio());
    }
}

class Tree{
    private Node head;
    Tree(){
        head = new Node();
    }
    Node getHead(){
        return head;
    }
}

class Node implements Comparable{
    private int depth;
    private double totalWeight, totalValue, bound = 0;
    private Node left, right;
    Node(Node left, Node right, int depth, double totalWeight, double totalValue){
        this.left = left;
        this.right = right;
        this.depth = depth;
        this.totalWeight = totalWeight;
        this.totalValue = totalValue;
    }
    Node(){
        new Node(null,null,0,0,0);
    }
    int getDepth() {
        return depth;
    }
    double getTotalWeight(){
        return totalWeight;
    }
    double getTotalValue(){
        return totalValue;
    }
    void setLeft(Node left){
        this.left = left;
    }
    void setRight(Node right){
        this.right = right;
    }
    void setBound(){
        Node node = this;
        if (node.getTotalWeight() >= proj2.bagCapacity) {
            bound = 0;
        }
        else {
            int j = node.getDepth();
            bound = node.getTotalValue();
            double totweight = node.getTotalWeight();
            while (j < proj2.numberOfItems && totweight + proj2.items.get(j).getWeight() <= proj2.bagCapacity) {
                totweight += proj2.items.get(j).getWeight();
                bound += proj2.items.get(j).getValue();
                j++;
            }
            if (j < proj2.numberOfItems)
                bound += (proj2.bagCapacity - totweight) * proj2.items.get(j).getRatio();
        }
    }

    double getBound(){
        return bound;
    }

    @Override
    public int compareTo(Object o) {
        return Double.compare(this.bound, ((Node) o).getBound());
    }

}