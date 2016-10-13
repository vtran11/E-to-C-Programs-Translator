/* *** This file is given as part of the programming assignment. *** */


//DONE!!
//you can make it simpler or easier to understand
//Simple and short code makes the other parts much easier to do work only



public class Parser 
{


    // tok is global to all these parsing methods;
    // scan just calls the scanner's scan method and saves the result in tok.
    private Token tok; // the current token
    private void scan() 
    {
	    tok = scanner.scan();
    }

    private Scan scanner;
    Parser(Scan scanner) 
    {
	    this.scanner = scanner;
	    scan();
	    program();
	    if( tok.kind != TK.EOF )
	        parse_error("junk after logical end of program");
    }

    private void program() 
    {
	    block();
    }

    private void block()
    {
	    declaration_list();
	    statement_list();
    }

    private void declaration_list() 
    {
	// below checks whether tok is in first set of declaration.
	// here, that's easy since there's only one token kind in the set.
	// in other places, though, there might be more.
	// so, you might want to write a general function to handle that.
	    while( is(TK.DECLARE) ) 
	    {
	        declaration();
	    }
    }

    private void declaration() 
    {
	    mustbe(TK.DECLARE);
	    mustbe(TK.ID);
	    while( is(TK.COMMA) )
    	{
	        scan();
	        mustbe(TK.ID);
    	}
    }

    private void statement_list()                               //**vy
    {
        while( is(TK.ID) || is(TK.TILDE) || is(TK.PRINT)|| is(TK.DO)||is(TK.IF)) //**vy
                statement();                                         //**vy
    }
    
    private boolean statement()                                     //**vy
    {
        if (  is(TK.ID) || is(TK.TILDE) )                         //**vy
            assignment();
        
        else if ( is(TK.PRINT))                                  //**vy
            print();
            
        else if ( is(TK.DO))                                     //**vy
        {
             mustbe(TK.DO);
             guarded_command();
             mustbe(TK.ENDDO);
        }
            
        else if ( is(TK.IF))                                     //**vy
        {
            mustbe(TK.IF);                                      
            guarded_command();
             
            while( is(TK.ELSEIF))                                //**vy
            {
                scan();
                guarded_command();
            }
                
            if (is(TK.ELSE))                                   //**vy
            {
                scan();
                block();
            }
                
            mustbe(TK.ENDIF);                                  //**vy
        }
        
        else
            return false;
            
        return true;
    }
    
    private void print()                                       //**vy
    {
        mustbe(TK.PRINT);
        expr();
    }
    
    private void assignment()                                  //**vy
    {
        ref_id();                                            //**vy
        mustbe(TK.ASSIGN);                                   //**vy
        expr();                                               //**vy
    }

    private void ref_id()                                     //**vy
    {
        if(is(TK.TILDE))                                      //**vy
        {
            scan();                                           //**vy
            if( is(TK.NUM))                                   //**vy
                mustbe(TK.NUM);                               //**vy
        }
        
        mustbe(TK.ID);                                        //**vy
    }
    
    private void guarded_command()                            //**vy
    {
        expr();                                               //**vy
        mustbe(TK.THEN);                                      //**vy
        block();                                              //**vy
    }
    
    private void expr()                                       //**vy
    {
        term();                                               //**vy
        while( addop() )                                      //**vy
            {
                scan();                                       //**vy
                term();                                        //**vy
            }
    }
    
    private boolean addop()                                   //***vy
    {
        if (is(TK.PLUS) || is(TK.MINUS))
            return true;
        
        else
            return false;
    }
    
    private void term()                                        //**vy
    {
        factor();                                              //**vy
        while( multop() )                                     //**vy
            {
                scan();                                        //**vy
                factor();                                      //**vy
            }
    }
    
    private boolean multop()                                    //**vy
    {
        if (is(TK.TIMES)|| is(TK.DIVIDE))
            return true;
        else
            return false;
    }
    
    private void factor()                                       //**vy
    {
        if( is(TK.LPAREN) )                                     //**vy
        {
            scan();                                             //**vy
            expr();                                             //**vy
            mustbe(TK.RPAREN);                                  //**vy
        }
        
        else if( is(TK.NUM))                                     //**vy
            mustbe(TK.NUM);                                      //**vy
            
        else
            ref_id();                                            //**vy
    }
    
    
    // is current token what we want?
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