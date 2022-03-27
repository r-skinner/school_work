//
//Name: Skinner, Ryan
//Project:3
//Due: 11-27-17
//Course:cs-240-01-F18
//
//Description:
//Convert infix to postfix and evaluate postfix
//

public class Expression {
	
	//precidence.indexOf(x)/2 will return (+||-) < (*||/) < ^
	String precidence = "+-*/^";
	
	public String[] convertToPostfix(String[] infixExpression) throws Exception{
		validateInfix(infixExpression); // validate input
		
		int parenthesisCount = 0; // used to resize output array
		int location = 0; //array pointer
		ArrayStack<String> stack = new ArrayStack<String>(infixExpression.length); //stack
		String postfixExpression[] = new String[infixExpression.length]; //output array
		
		for (String currentValue : infixExpression) {
			try {
				Integer.parseInt(currentValue); //if number
				postfixExpression[location++] = currentValue;
			}
			
			catch (NumberFormatException e) { // if not number
				int prec = precidence.indexOf(currentValue);
				
				//deals with parenthesis
				if(currentValue.equals(")")) {
						parenthesisCount+=2;
						while(!stack.isEmpty() && !stack.peek().equals("(")) 
							postfixExpression[location++] = stack.pop();
						stack.pop();
				}
				//always push
				else if(stack.isEmpty() || stack.peek().equals("(") || currentValue.equals("(")) {
					stack.push(currentValue);
				}
				else {
					//conditional push/pop
					while(!stack.isEmpty() && prec/2 <= precidence.indexOf(stack.peek())/2 && !stack.peek().equals("(")) 
						postfixExpression[location++] = stack.pop();
					stack.push(currentValue);
				
				}
			}
		}
		while(!stack.isEmpty())
			postfixExpression[location++] = stack.pop();
		
		//remove empty cells (from parenthesis)
		String[] temp = new String[postfixExpression.length-parenthesisCount];
		for(int i = 0; i < temp.length; i++)
			temp[i] = postfixExpression[i];
		
		
		return temp;
	}

	public int evaluatePostfix(String[] postfixExpression) throws Exception{
		validatePostfix(postfixExpression); //validate input
		ArrayStack<Integer> stack = new ArrayStack<>(postfixExpression.length); //stack
		
		int rhs, lhs, total = 0;
		
		for (String currentValue : postfixExpression) { //for each in infix
			try {
				stack.push(Integer.parseInt(currentValue)); //if number
			}
			catch (NumberFormatException e) { // if not number
				rhs = Integer.parseInt("" + stack.pop());
				lhs = Integer.parseInt("" + stack.pop());
				switch (currentValue) {
					case "+":
							stack.push(lhs + rhs);
							break;
					case "-":
							stack.push(lhs - rhs);
							break;
					case "*":
						stack.push(lhs * rhs);
						break;
					case "/":
						stack.push(lhs / rhs);
						break;
					case "^":
						stack.push((int)Math.pow(lhs,rhs));
						break;
					default:
						throw new Exception("Invalid postfix expression, Unknown Operator: \"" + currentValue + "\"");
				}
			}
		}
		total = Integer.parseInt("" + stack.pop());
		return total;
	}

	public void validateInfix(String[] infixExpression) throws Exception {
		//Makes sure there is the correct amount of parenthesis, operators, and operands. Also verifies operator/operand order
		int parenthesisCount = 0;
		int last = 0;
		for (String currentValue : infixExpression) { //for each in infix
			try {
				Integer.parseInt(currentValue); //if number
				last++;
			}
			catch (NumberFormatException e) { // if not number
				switch (currentValue) {
					case "(":
						parenthesisCount++;
						break;
					case ")":
						parenthesisCount--;
						break;
					case "+":
					case "-":
					case "*":
					case "/":
					case "^":
						last--;
						break;
					default:
						throw new Exception("Invalid infix expression, unknown operator: \"" + currentValue + "\"");
				}
			}
			if(last != 1 && last != 0) {
				throw new Exception("Invalid infix expression, format error");}
		}
		if(parenthesisCount != 0)
			throw new Exception("Invalid infix expression, inconsistant parenthesis");
	}
	
	public void validatePostfix(String[] postfixExpression) throws Exception {
		//Makes sure there is the correct amount of operators and operands.
		int count = 0;
		for (String currentValue : postfixExpression) { //for each in infix
			try {
				Integer.parseInt(currentValue); //if number
				count++;
			}
			catch (NumberFormatException e) { // if not number
				switch (currentValue) {
					case "+":
					case "-":
					case "*":
					case "/":
					case "^":
						if(count-- <= 1) {
							throw new Exception("Invalid postfix expression, format error");}
						break;
					default:
						throw new Exception("Invalid postfix expression, unknown operator: \"" + currentValue + "\"");
				}
			}
		}
		if(count != 1)
			throw new Exception("Invalid postfix expression, format error");
	}
}
