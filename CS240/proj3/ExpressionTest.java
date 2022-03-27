
public class ExpressionTest {

	static Expression e = new Expression();
		
	public static void main(String[] args) throws Exception {
		System.out.println("R. Skinner's Expression Class");
		System.out.println("\nTest 1: Order of operations");
		compute("4 + 4 / 2");

		System.out.println("\nTest 2: Parenthesis");
		compute("( 4 + 4 ) / 2");

		System.out.println("\nTest 3: Mismached Parenthesis");
		compute("( 4 + 4 ( / 2");
		
		System.out.println("\nTest 4: Odd Parenthesis");
		compute("( 4 + 4 / 2");

		System.out.println("\nTest 5: Invalid Operator");
		compute("4 + 4 % 2");

		System.out.println("\nTest 6: Complex Equation");
		compute("( 4 + 4 ) ^ 2 * 5 + ( 8 / ( 2 * 4 ) )");

		System.out.println("\nTest 7: Missing Operator");
		compute("( 4 4 ) ^ 2 * 5 + ( 7 / 2 )");
		
		
		//Postfix tests
		System.out.println("\nTest 8: Missing Operator (postfix)");
		try {
			String[] sa2 = {"4", "5"};
			String s2 = "";
			for(String s : sa2) {
				s2+=(s + " ");
			}
			System.out.println("\tPostfix:   " + s2);
			System.out.println("\tEvaluated: " + e.evaluatePostfix(sa2));
		}
		catch(Exception e) {
			System.out.println("\tError: "+e.getMessage());
		}
		
		System.out.println("\nTest 9: Invalid Operator (postfix)");
		try {
			String[] sa2 = {"4", "5", "%"};
			String s2 = "";
			for(String s : sa2) {
				s2+=(s + " ");
			}
			System.out.println("\tPostfix:   " + s2);
			System.out.println("\tEvaluated: " + e.evaluatePostfix(sa2));
		}
		catch(Exception e) {
			System.out.println("\tError: "+e.getMessage());
		}
		
		System.out.println("\nTest 10: Extra Operator (postfix)");
		try {
			String[] sa2 = {"4", "5", "*", "*"};
			String s2 = "";
			for(String s : sa2) {
				s2+=(s + " ");
			}
			System.out.println("\tPostfix:   " + s2);
			System.out.println("\tEvaluated: " + e.evaluatePostfix(sa2));
		}
		catch(Exception e) {
			System.out.println("\tError: "+e.getMessage());
		}

		
		
	}
	private static void compute(String s1) throws Exception {
		try {
			System.out.println("\tInfix:     " + s1);
			String[] sa1 = s1.split(" ");
			String[] sa2 = e.convertToPostfix(sa1);
			String s2 = "";
			for(String s : sa2) {
				s2+=(s + " ");
			}
			System.out.println("\tPostfix:   " + s2);
			System.out.println("\tEvaluated: " + e.evaluatePostfix(sa2));
		}
		catch(Exception e) {
			System.out.println("\tError: "+e.getMessage());
		}
	}
}
