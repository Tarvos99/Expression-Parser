/*Jason Grant
 * 7/2/18
 * CS 610-850 Summer 18
 * Programming Assignment #2 */
import java.util.Scanner;
import java.util.StringTokenizer;

public class ExpTree{
	public ExpTree(String [] exp) {
		this.exp = exp;
	}
    private class Node<T> {//class to represent a node in expression tree with left/right children and a data field
        private Node(T data) {
            this.data = data;
        }
        private T data;
        private Node<T> left;
        private Node<T> right;
    }
    
    Node<String> root; //initial root node of tree
    String [] exp; //string array to hold user-inputted expression. Each character has its own slot in array
    
    public void inOrder (Node<String> curr) { // used for testing purposes
    	if (curr!=null) {
    		inOrder(curr.left);
    		System.out.print(curr.data + " ");
    		inOrder(curr.right);
    	}
    }
    
    public void inOrderRep (Node<String> curr) {// method to display info about tree
    	System.out.println("The node with character "+curr.data+" has the following children: ");
    	if(curr.left != null) {
    		System.out.println("Left Child: "+ curr.left.data);
    	}
    	else {
    		System.out.println("Left Child: empty");
    	}
    	
    	if (curr.right != null) {
    		System.out.println("Right child: "+curr.right.data);
    	}
    	else {
    		System.out.println("Right Child: empty");
    	}
    	
    	System.out.println();
    	if(curr.left != null) {
    		inOrderRep(curr.left);
    	}
    	
    	if(curr.right != null) {
    		inOrderRep(curr.right);
    	}	
    }
    
    public static void printExpTree(Node<String> root, int level){//method to print visual representation of tree
        if(root==null)
             return;
        printExpTree(root.right, level+1);
        if(level!=0){
            for(int i=0;i<level-1;i++)
                System.out.print("|\t");
                System.out.println("|-------("+root.data+")");
        }
        else
            System.out.println("("+root.data+")");
        printExpTree(root.left, level+1);
    }    
    
    public Node<String> getRoot(){//getter method to return current root of expression tree
    	return root;
    }
    public void build() {//called from main method to initialize building of tree
    	root = buildexp(exp);
    }
    
    private Node<String> buildexp(String [] info) {//method to handle expression part of BNF language
    	if(info[0] == null) {
    		return null;
    	}
    	
    	String [] pre = new String [info.length];
    	int index = 0;
    	int plusflag = 0;//flag to indicate a plus was found
    	int minusflag = 0; //flag to indicate a minus was found
    	int frontparen = 0;//how many front parenthesis I've passed through. Ignore any operators until I find the corresponding closing parenthesis
    	//iterate through list to find any + or - sign to indicate <term> +- <expression> situation
    	for(int i = 0;i < pre.length && info[i]!=null; i++) {
    		if(info[i].equals("+") && frontparen==0) {
    			plusflag = 1;
    			break;
    		}
    		if(info[i].equals("-") && frontparen==0) {
    			minusflag = 1;
    			break;
    		}
    		if(info[i].equals("(")) {
    			frontparen++;
        		pre[i]= info[i];
        		index++;
    		}
    		else {
        		if(info[i].equals(")")) {
        			frontparen--;
            		pre[i]= info[i];
            		index++;
        		}
        		else {
            		pre[i]= info[i];
            		index++;
        		}
    		}
    	}

    	if(plusflag == 1) {// if a "+" was found do this
        	String [] post = new String [info.length];//find everything post + sign
        	for(int j = 0; index+1 < post.length; j++) {
        		post[j]= info[index+1];
        		index++;
        	}
        	
        	Node<String> curr = new Node<String> ("+");
        	//follow BNF language to recursively build out the rest of the tree
        	curr.left = buildterm(pre);
        	curr.right = buildexp(post);
        	return curr;
    	}
    	if(minusflag == 1) {// if a "-" was found do this
        	String [] post = new String [info.length];//find everything post - sign
        	for(int j = 0; index+1 < post.length; j++) {
        		post[j]= info[index+1];
        		index++;
        	}
        	
        	Node<String> curr = new Node<String> ("-");

        	curr.left = buildterm(pre);
        	curr.right = buildexp(post);
        	return curr;
    	}
    	else {//else we are in case where input is just a term
    		//Node<String> curr= new Node<String>(info[0]); Used for Testing
    		Node<String> curr = buildterm(info);
    		return curr;
    	}
	}
    	
    
    private Node<String> buildterm(String [] data) {//method to handle term part of BNF language
    	if(data[0] == null) {
    		return null;
    	}
    	//should be nearly identical to buildexpression method
    	//iterate through list to find any * or / sign to indicate <factor> */ <term> situation
    	String [] pre = new String [data.length];
    	int index = 0;
    	int frontparen = 0;
    	int multiflag = 0;//flag to indicate a plus was found
    	int divisflag = 0;//flag to indicate a division was found
    	for(int i = 0;i < pre.length && data[i]!=null; i++) {
    		if(data[i].equals("*") && frontparen == 0) {
    			multiflag = 1;
    			break;
    		}
    		if(data[i].equals("/") && frontparen == 0) {
    			divisflag = 1;
    			break;
    		}
    		if(data[i].equals("(")) {
    			frontparen++;
        		pre[i]= data[i];
        		index++;
    		}
    		else {
        		if(data[i].equals(")")) {
        			frontparen--;
            		pre[i]= data[i];
            		index++;
        		}
        		else {
            		pre[i]= data[i];
            		index++;
        		}
    		}
    	}
    	if(multiflag == 1) {// if a "*" was found do this
        	String [] post = new String [data.length];//find everything post * sign
        	for(int j = 0; index+1 < post.length; j++) {
        		post[j]= data[index+1];
        		index++;
        	}
        	
        	Node<String> curr = new Node<String> ("*");

        	curr.left = buildfactor(pre);
        	curr.right = buildterm(post);
        	return curr;
    	}
    	if(divisflag == 1) {// if a "/" was found do this
        	String [] post = new String [data.length];//find everything post / sign
        	for(int j = 0; index+1 < post.length; j++) {
        		post[j]= data[index+1];
        		index++;
        	}
        	
        	Node<String> curr = new Node<String> ("/");

        	curr.left = buildfactor(pre);
        	curr.right = buildterm(post);
        	return curr;
    	}
    	else {//else we are in case where input is a factor
			//Node<String> curr= new Node<String>(data[0]); Used for Testing Purposes
    		Node<String> curr = buildfactor(data);
    		return curr;
    	}
    }
    	
    private Node<String> buildfactor(String [] stuff) {//method to handle factor and digit part of BNF language
    	if(stuff[0] == null) {
    		return null;
    	}   	
    	if(stuff[0].equals("(")) {//if first entry in array is parenthesis, this indicates ( <expression ) situation

    		String [] pre = new String [stuff.length];
    		int index = 1;
    		for(int h = 0; h < stuff.length && stuff[index]!= null; h++){
    			//parse through string to find expression inside of parenthesis
    			if(stuff[index].equals(")")){
    				break;
    			}
    			else {
        			pre[h] = stuff[index];
        			index++;
    			}
    		}
    		String [] post = new String [stuff.length];
    		for(int k = 0; index + 1 < post.length;k++) {
    			//find rest of expression that rests right after )
        		post[k]= stuff[index+1];
        		index++;
    		}

    		Node<String> curr = buildexp(post);
    		Node<String> lefcurr= buildexp(pre);
    		if(curr != null) {
        		curr.left = lefcurr;
        		return curr;
    		}
    		else {
    			return lefcurr;
    		}
    	}
    	/*else {//check if this is still expression with operator to be evaluated
    		//should never reach this part of code if I follow BNF rules
    		//print woops to show that expression somehow reached here
    		String cars = new String();
    		for(int g=0; g < stuff.length; g++) {
    			cars = cars + stuff[g];
    		}
    		if(cars.contains("-")||cars.contains("+")||cars.contains("*")||cars.contains("/") ) {
        		Node<String> curr= buildexp(stuff);
        		System.out.println("woops");
        		return curr;
    		}*/
    		else {//otherwise its just a digit/number so return it
    			if(!stuff[0].equals(")") && !stuff[0].equals("(") ) {
            		
            		Node<String> curr= new Node<String>(stuff[0]);
            		return curr;
    			}
    			else
    				return null;
    		}
    }
    
    public double evaluate(Node <String> curr){
    	//method to evalauate expression tree
    	if(curr.data == "+") {
    		return evaluate(curr.left) + evaluate(curr.right);
    	}
    	else {
            if(curr.data == "-") {
            	return evaluate(curr.left) - evaluate(curr.right);
            }
            else {
                if(curr.data == "*") {
                	return evaluate(curr.left) * evaluate(curr.right);
                }
                else {
                    if(curr.data == "/") {
                    	return evaluate(curr.left) / evaluate(curr.right);
                    }
                    else {// if data field does not hold operator, we must be at a leaf with an int value
                        	double result = Double.parseDouble(curr.data);
                        	return result;
                    }
                }
            }
    	}
    }
    
	public static void main(String[] args) {
		System.out.println("Hey there Zhenliang, please input the expression you want to evaluate, with a space in-between each character you enter:");
		
		Scanner scanner = new Scanner(System.in);//scan for user input
		String expression = scanner.nextLine();
		StringTokenizer tokenexpr = new StringTokenizer(expression, " ");
		String [] coolbeans = new String [tokenexpr.countTokens()];//array with each entry containing a token
		for (int i = 0; i < coolbeans.length; i ++) {
			coolbeans[i]= tokenexpr.nextToken();
		}
		
		ExpTree nicebeans = new ExpTree(coolbeans);
		nicebeans.build();
		//nicebeans.inOrder(nicebeans.getRoot()); //testing purposes
		System.out.println();
		double y = nicebeans.evaluate(nicebeans.getRoot());
		System.out.println("Result: "+y);
		System.out.println();
		System.out.println();
		
		
		printExpTree(nicebeans.getRoot(), 0);
		System.out.println();
		System.out.println();
		
		System.out.println("In case the above tree is a bit confusing, here is some information about your expression tree:");
		System.out.println();
		nicebeans.inOrderRep(nicebeans.getRoot());

		scanner.close();
	}
}
