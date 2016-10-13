import java.lang.*;

public class Parser
{
    private symbolTable1 table = new symbolTable1();

    // tok is global to all these parsing methods;
    // scan just calls the scanner's scan method and saves the result in tok.
    private Token tok; // the current token
    private int blockScope = -1;    
    private void scan()
    {
	    tok = scanner.scan();
    }

    private Scan scanner;

    boolean minus = false;              

    Parser(Scan scanner)
    {
	    this.scanner = scanner;
        scan();
	    program();
	    if( tok.kind != TK.EOF )
	        parse_error("junk after logical end of program");
    }

    //*************************program***********************************
    private void program()
    {
        System.out.println("#include <stdio.h>");
        System.out.println("int main()");
        System.out.println("{");
	    block();
	    System.out.println("}");
    }

    //*************************block**************************************
    private void block()
    {
        blockScope++;            //Mohammed (Scoping)
        table.create_symbolTable();
	    declaration_list();
	    statement_list();
	    table.pop_stack();
	    blockScope--;

    }

   //*************************declaration_list***********************************
    private void declaration_list()
    {
	    while( is(TK.DECLARE) )
	    {
	        declaration();
	    }
    }

    //************************declaration************************************
    private void declaration()
    {
       mustbe(TK.DECLARE);
        System.out.print("int ");//vy

        tok.addScope(blockScope);

	    table.add_stack(tok);

	    boolean check = false;
	    System.out.print(" x_"+tok.string+"_"+tok.scope);

	    mustbe(TK.ID);
	    while( is(TK.COMMA) )
    	{
    	    mustbe(TK.COMMA);
    	    System.out.print(", ");

    	    tok.addScope(blockScope);

	        table.add_stack(tok);
	        System.out.print(" x_"+tok.string+"_"+tok.scope);

	    mustbe(TK.ID);
    	}
    	System.out.println(";");
    }

    //*************************statement_list***********************************
    private void statement_list()
    {
        while( is(TK.ID) || is(TK.TILDE) || is(TK.PRINT)|| is(TK.DO)||is(TK.IF))
                statement();
    }

    //************************statement************************************
    private void statement()
    {
       if (  is(TK.ID) || is(TK.TILDE) )
            assignment();

        else if ( is(TK.PRINT))
            print();
       else if ( is(TK.DO))
        {
             mustbe(TK.DO);         //vy
             System.out.print(" while(");
             guarded_command();
             mustbe(TK.ENDDO);         //vy
        }

        else if ( is(TK.IF))
        {
            mustbe(TK.IF);
            System.out.print("if(");
            guarded_command();

            while( is(TK.ELSEIF))
            {
                mustbe(TK.ELSEIF);
                System.out.print("else if(");
                guarded_command();
            }

            if (is(TK.ELSE))
            {
                mustbe(TK.ELSE);
                System.out.println("else {");
                block();
                System.out.print("}\n");
            }

             mustbe(TK.ENDIF);
        }
    }


     //************************ref_id************************************
    private void ref_id()
    {
        int requestedVariableScope;
        int TeldaScope = 0;

         if(is(TK.TILDE))
         {
            mustbe(TK.TILDE);

            if( is(TK.NUM))
            {
                TeldaScope = Integer.parseInt(tok.string);
				mustbe(TK.NUM);
				requestedVariableScope = table.search(tok, (TeldaScope-1)); //added 6PM 1/28
                if(requestedVariableScope != -1)
			        System.out.print("x_"+tok.string+"_"+requestedVariableScope);//added 6PM 1/28
			    mustbe(TK.ID);

            }

            else if(is(TK.ID))
            {
                table.search(tok, (blockScope-1));
		        System.out.println("x_"+tok.string+"_0");
		        mustbe(TK.ID);
            }

            else if(is(TK.TILDE)){
                System.err.println("no such variable ~" + (TeldaScope == -1 ? "" : TeldaScope) + tok.string + " on line " + tok.lineNumber);
                scan();
            }
           else{
                TeldaScope = -1;
                scan();
            }


         }

		else if(is(TK.ID))
		{
		    TeldaScope = table.search(tok);
    	    if(TeldaScope!=-1)
		        System.out.println("x_"+tok.string+"_"+TeldaScope);
		    mustbe(TK.ID);


		}

    }

    //*******DONE***********print************************************
    private void print()
    {
        mustbe(TK.PRINT);
        System.out.print("printf(\"%d\\n\",");      //vy
        expr();
        System.out.print(");\n");                   //vy
    }

 //*****DONE****************assignment*****************************
 private void assignment()
    {
        ref_id();
        if(is(TK.ASSIGN)){
            mustbe(TK.ASSIGN);
            System.out.print(" = ");
            expr();
            System.out.print(";\n");
        }
    }


   //***************guarded_command*****************************
    private void guarded_command()
    {
        expr();
        if(minus ==true)
		    System.out.print(" <= 0)\n{\n");
    	else
		    System.out.print(" == 0)\n{\n");
        mustbe(TK.THEN);
        block();
        System.out.print("}\n");
    }

   //*********done************expr**************************************
    private void expr()
    {
        term();
       while( is(TK.PLUS) || is(TK.MINUS) )
            {
                addop();
                term();
            }
    }

    //*******DONE********vy*****addop**********************************
    private void addop()
    {
        if (is(TK.PLUS))                        //vy
            {
                mustbe(TK.PLUS);
                System.out.print(" + ");
            }

        else if (is(TK.MINUS))                  //vy
            {
               minus = true;
                mustbe(TK.MINUS);
                System.out.print(" - ");             //vy
           }
    }

    //*******DONE********vy*****term**********************************
    private void term()
    {
        factor();
        while( tok.kind == TK.TIMES || tok.kind == TK.DIVIDE )         //vy
            {
                System.out.print(tok.kind == TK.TIMES ? '*': '/');    //vy
                scan();
                factor();
            }
    }


    //*******DONE************factor**********************************
    private void factor()
    {
        if( is(TK.LPAREN) )
        {
            mustbe(TK.LPAREN);
            System.out.print("(");    //vy
            expr();

            mustbe(TK.RPAREN);
			System.out.print(")");
        }

        else if( is(TK.NUM))
        {
            System.out.print(tok.string);
            mustbe(TK.NUM);
        }
        else if( is(TK.TILDE) || is(TK.ID) )
            ref_id();
    }

    private boolean is(TK tk)
    {
        return tk == tok.kind;
    }

    // ensure current token is tk and skip over it.
    private void mustbe(TK tk)
    {
	if( tok.kind != tk )
	{
	    System.err.println( "mustbe: want " + tk + ", got " +
				    tok);
	    parse_error( "missing token (mustbe)" );
	}
	scan();
   }

   private void parse_error(String msg) {
	System.err.println( "can't parse: line "
			    + tok.lineNumber + " " + msg );
	System.exit(1);
    }
}





public Sequence flatten(){

Sequence nwSeq = new Sequence();
Sequence current = this;
Element currente = current.first(); // first element of the Sequence
while(currente != null){ // if current element != null
if((currente instanceof MyInteger) || (currente instanceof MyChar))
{
Sequence temp = new Sequence();
Element tempe = currente;
nwSeq.add(tempe, nwSeq.length()); //add to new Sequence
}
else if(currente instanceof Sequence)
{
Sequence temp = ((Sequence)currente).flatten();
Element tempe = currente;
nwSeq.add(tempe, nwSeq.length());
}
if(current.rest() == null){
currente = null;
}
else{
currente = current.first();
current = current.rest();
}
}
return nwSeq;