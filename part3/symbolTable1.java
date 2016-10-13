
import java.util.*;

public class symbolTable1
{
    private Stack<ArrayList<Token>> stack;
    private int scope;
    
    public symbolTable1()
    {
        stack = new Stack<ArrayList<Token>>();
        scope = -1;
    }
    
    public void create_symbolTable(){
    	ArrayList<Token> symbolTable = new ArrayList<Token>();
    	stack.push(symbolTable);
    	scope++;
    }
    
    public void push_stack(ArrayList<Token> Array)  //Edited by Mohammed
    {
        stack.push(Array);
    }
    
    public void pop_stack()
    {
        stack.pop();
        scope--;
    }
    
    public ArrayList<Token> peek(){
    	return stack.peek();
    }
    
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
        if(!found)
            System.out.println(token.string + " is an undeclared variable on line "+ token.lineNumber);
        return found;
    }
    
    //search a specific scope's symbolTable.
    //Parameters: token to be searched for, scope  = ~ (N)
    public void search( Token token, int scope)
    {
        Stack<ArrayList<Token>> temp = new Stack<ArrayList<Token>>();
		for (int i = scope; i>=1; i--){
		    if (stack.empty()){
		        System.err.println("Scope specified is out of program reach");
		        break;
		    }
			temp.push(stack.pop());
		}
		ArrayList<Token> temp2 = new ArrayList<Token>(stack.peek());
        Token temptk;
        String tempst = token.string;
        boolean found = false;
        for(int i = 0; i<temp2.size(); i++){
            temptk = temp2.get(i);
            if(tempst.equals(temptk.string))
                found = true;
        }
		for (int i = scope; i>=1; i--){
			stack.push(temp.pop());
		}
		if(!found)
		    System.out.println(token.string + " is an undeclared variable on line "+ token.lineNumber);
		
    }
    //Search the whole program's table of symbolTables for a given token.
    public void search(Token token){
        Stack<ArrayList<Token>> temp = new Stack<ArrayList<Token>>();
        ArrayList<Token> temp2 = new ArrayList<Token>();
        Token temptk;
        String tempst = token.string;
        boolean found = false;
        while(!stack.empty()){
            temp2 = stack.peek();
            for(int i = 0; i<temp2.size(); i++){
                temptk = temp2.get(i);
                if(tempst.equals(temptk.string))
                    found = true;
            }
            temp.push(stack.pop());
        }
        
        while(!temp.empty()){
            stack.push(temp.pop());
        }
        
        if(!found)
		    {
		        System.out.println(token.string + " is an undeclared variable on line "+ token.lineNumber);
		        System.exit(1);
		    }
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