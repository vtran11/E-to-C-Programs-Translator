
import java.util.*;

public class symbolTable1
{
    private Stack<ArrayList<Token>> stack;
    //private int scope;
    //*************************************constructor************************************
    public symbolTable1()
    {
        stack = new Stack<ArrayList<Token>>();
        //scope = -1;
    }
    //*******************************create_symbolTable*****************************
    //Create a new symbol table and pushes it to the table stack.
    public void create_symbolTable(){
    	ArrayList<Token> symbolTable = new ArrayList<Token>();
    	stack.push(symbolTable);
    	//scope++;
    }
    //************************************push_stack**************************************
    //Pushes symbol tables to the table stack.
    public void push_stack(ArrayList<Token> Array)  //Edited by Mohammed
    {
        stack.push(Array);
    }
    
    public void pop_stack()
    {
        stack.pop();
        //scope--;
    }
    
    public ArrayList<Token> peek(){
    	return stack.peek();
    }
    //*********************************add_stack***********************************
    //adds a token to the current scope symbol table if not declared yet.
    public void add_stack(Token token){
        
		boolean redeclared = false;
        ArrayList<Token> temp = new ArrayList<Token>(stack.peek());
        Token temptk;
        String tempst = token.string;
        for(int i = 0; i<temp.size(); i++){
            temptk = temp.get(i);
            if(tempst.equals(temptk.string))
                redeclared = true;
        }
		if (redeclared)
			System.err.println("redeclaration of variable " + token.string);
		else
			stack.peek().add(token);

    }
    //********************************contains*********************************
    // search the current scope's symbolTable for a given token
     public boolean contains(Token token)
    {
        boolean found = false;
        ArrayList<Token> temp = new ArrayList<Token>(stack.peek());
        Token temptk;
        String tempst = token.string;
        for(int i = 0; i<temp.size(); i++){
            temptk = temp.get(i);
            if(tempst.equals(temptk.string))
                found = true;
        }
        //if(!found)
           // System.out.println(token.string + " is an undeclared variable on line "+ token.lineNumber);
        return found;
    }
    //**********************************search(token, scope)**********************************************
    //search a specific scope's symbolTable.
    //Parameters: token to be searched for, scope  = ~ (N)
    public int search( Token token, int scope)
    {
        //The way this works is , for example if the passed scope = 2, that means
        //we want to go two levels down in stacks depth. Therefore, I pop()
        // the two top levels to a temporary stack and then search level two symbolTable
        // in the table stack.
        //After that I pop() the symbolTables in the temporary stack into the original
        // table stack.
        int ScopeOftheFoundVariable = -1;
        Stack<ArrayList<Token>> temp = new Stack<ArrayList<Token>>();
		for (int i = scope; i>=1; i--){
		    if (stack.empty()){
		        System.err.println("Scope specified is out of program reach");
		        break;
		    }
			temp.push(stack.pop());
		}
		ArrayList<Token> temp2 = new ArrayList<Token>(stack.peek()); //a temporary ArrayList to hold the peeked 
		//symbolTable array in table.
        Token temptk;    
        String tempst = token.string;
        boolean found = false;
        for(int i = 0; i<temp2.size(); i++){
            temptk = temp2.get(i);
            if(tempst.equals(temptk.string))
                found = true;
                ScopeOftheFoundVariable = temptk.scope;    //added 6PM 1/28
                     //added 6PM 1/28
        }
		for (int i = scope; i>=1; i--){
			stack.push(temp.pop());
		}
		if(!found){
		    System.out.println(token.string + " is an undeclared variable on line "+ token.lineNumber);
		             //added 6PM 1/28
		}
	return ScopeOftheFoundVariable;	
    }
    //**************************************search(token)****************************************
    //Search the whole program's table of symbolTables for a given token.
    public int search(Token token){
        Stack<ArrayList<Token>> temp = new Stack<ArrayList<Token>>();
        ArrayList<Token> temp2 = new ArrayList<Token>();
        Token temptk;
        String tempst = token.string;
        int scope =-1;
        boolean found = false;
        while(!(stack.empty())){
            temp2 = stack.peek();
            for(int i = 0; i<temp2.size(); i++){
                temptk = temp2.get(i);
                if(tempst.equals(temptk.string)){
                    found = true;
                    scope = temptk.scope;
                }
                    
            }
            temp.push(stack.pop());
        }
        
        while(!(temp.empty())){
            stack.push(temp.pop());
        }
        
        
        if(!found)
		{
		     System.out.println(token.string + " is an undeclared variable on line "+ token.lineNumber);
		}
        return scope;
	}
    
    public Token scope_search(Token tk, int isScope)
    {
        int depth;
        
        if(isScope == 0)
            depth = 0;
        else
             depth = scope - isScope;
             
        for (int i = depth; i>=0; i--)
        {
            //Vy, stack class in java doesn't have this method in it "elementAt".
            //Check this URL for stack methods.
            //https://docs.oracle.com/javase/7/docs/api/java/util/Stack.html
            // That is why I did the search(token, scope) method the way it is upthere
            // Go to line 75 if you desire to see the explination for my search method.
            
            
            // Have you checked my search method? does it make sense to you?
            //you know what, what make me confuse more is how you use temptk, temp,temp2,tempst..lol
            // oh, I know it is a bit confusing. I modified it this way to make it very
            //basic as a try to fix the error(symboTable1 uses unsafe/unchecked statements)
            //I'll try to explain it up there.^
            
            ArrayList<Token> var = stack.elementAt(depth);
            for( Token t: var)
            {
                if(t.string.equals(tk.string))
                    return t;
            }
        }
        return null;   
    }

}