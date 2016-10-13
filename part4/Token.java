public class Token {
    public TK kind;
    public String string;
    public int lineNumber;
    public int scope; // Mohammed
    public Token(TK kind, String string, int lineNumber) {
	this.kind = kind;
	this.string = string;
	this.lineNumber = lineNumber;
    }
    /***addScope adds scope to the token(interested on ID tokens only).***/
  
   public void addScope(int scope) {
	     this.scope = scope;
    }
    
    //End of the addScope.
    public String toString() { // make it printable for debugging
	return "Token("+kind.toString()+" "+string+" "+lineNumber+")";
    }
}
