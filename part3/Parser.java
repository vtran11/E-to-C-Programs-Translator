/* *** This file is given as part of the programming assignment. *** */
import java.lang.*;

public class Parser
{
    private symbolTable1 table = new symbolTable1();      //p3
    private Token tok; // the current token
    private int TeldaScope;
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
        table.create_symbolTable();    //Mohammed
	    declaration_list();
	    statement_list();
	    table.pop_stack();            //Vy
    }

    private void declaration_list() 
    {
	    while( is(TK.DECLARE) ) 
	    {
	        declaration();
	    }
    }

    private void declaration() 
    {
	    mustbe(TK.DECLARE);
	    table.add_stack(tok);    //added Mohammed
	    mustbe(TK.ID);
	    while( is(TK.COMMA) )
    	{
	        scan();
	        table.add_stack(tok);   //added Mohammed
	        mustbe(TK.ID);
    	}
    }

    private void statement_list()                               
    {
        while( is(TK.ID) || is(TK.TILDE) || is(TK.PRINT)|| is(TK.DO)||is(TK.IF)) 
                statement();                                         
    }
    
    private boolean statement()                                     
    {
        if (  is(TK.ID) || is(TK.TILDE) )                         
            assignment();                      
        
        else if ( is(TK.PRINT))                                  
            print();
            
        else if ( is(TK.DO))                                     
        {
             mustbe(TK.DO);
             guarded_command();
             mustbe(TK.ENDDO);
        }
            
        else if ( is(TK.IF))                                     
        {
            mustbe(TK.IF);                                      
            guarded_command();
             
            while( is(TK.ELSEIF))                                
            {
                scan();
                guarded_command();
            }
                
            if (is(TK.ELSE))                                   
            {
                scan();
                block();
            }
                
            mustbe(TK.ENDIF);                                  
        }
        
        else
            return false;
            
        return true;
    }
    
    private void print()                                       
    {
        mustbe(TK.PRINT);
        expr();
    }
    
 private void assignment()                                  
    {
        ref_id();                                            
        if(is(TK.ASSIGN)){  //added Mohammed
            scan();
            expr();
        }
        else
           System.exit(1);          // added Mohammed
    }

    private void ref_id()                                     
    {
        int TeldaScope = -1;

         if(is(TK.TILDE))                                      
        {
            mustbe(TK.TILDE);                                           
            if( is(TK.NUM))
            {
                
                TeldaScope = Integer.parseInt(tok.string); 
                if( TeldaScope== 0)
					TeldaScope = -1;
				
				mustbe(TK.NUM);
            }
            else if(!is(TK.NUM))
                TeldaScope = 0;
        }
        
        if(is(TK.ID))
        {
            if(TeldaScope != -1) 
            {
                if (table.scope_search(tok,TeldaScope ) == null)
                {		
                System.err.println("no such variable ~" + (TeldaScope == 0 ? "" : TeldaScope) 
                            + tok.string + " on line " + tok.lineNumber);
                }
            }
            else if (TeldaScope == -1) //variable is found in the current scope
            {
                table.search(tok);
            }
        }
        
        mustbe(TK.ID);
}

    private void guarded_command()                            
    {
        expr();                                               
        mustbe(TK.THEN);                                      
        block();                                              
    }
    
    private void expr()                                       
    {
        term();                                               
        while( addop() )                                      
            {
                scan();                                       
                term();                                        
            }
    }
    
    private boolean addop()                                   
    {
        if (is(TK.PLUS) || is(TK.MINUS))
            return true;
        
        else
            return false;
    }
    
    private void term()                                        
    {
        factor();                                              
        while( multop() )                                     
            {
                scan();                                        
                factor();                                      
            }
    }
    
    private boolean multop()                                    
    {
        if (is(TK.TIMES)|| is(TK.DIVIDE))
            return true;
        else
            return false;
    }
    
    private void factor()                                       
    {
        if( is(TK.LPAREN) )                                     
        {
            scan();                                             
            expr();                                             
            mustbe(TK.RPAREN);                                  
        }
        
        else if( is(TK.NUM))                                     
            mustbe(TK.NUM);                                      
            
        else
            ref_id();                                            
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