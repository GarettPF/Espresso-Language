package AST;

public class Token {
    private int sym;
    private String lexeme;
    private int line;
    private int charBegin;
    private int charEnd;
    
    public static final String names[] = {
	    "EOF",
        "error",
	    "BYTE",
        "CHAR",
        "SHORT",
        "INT",
        "LONG",
        "FLOAT",
        "DOUBLE",
        "BOOLEAN",
        "STRING",
        "BREAK",                
        "CLASS",                
        "CONTINUE",
        "IF",    
        "ELSE",    
        "WHILE",         
        "DO",      
        "FOR",    
        "NEW",   
        "SUPER",         
        "THIS",  
        "EXTENDS",
        "VOID",          
        "RETURN",  
        "PUBLIC", 
        "PRIVATE", 
        "STATIC", 
        "FINAL", 
        "BOOLEAN_LITERAL",
        "FLOAT_LITERAL",
        "DOUBLE_LITERAL",
        "IDENTIFIER", 
        "INTEGER_LITERAL", 
        "LONG_LITERAL", 
        "NULL_LITERAL",
        "STRING_LITERAL",
        "CHARACTER_LITERAL",
        "EQ",
        "LT",
        "GT",   
        "LTEQ",
        "GTEQ", 
        "EQEQ",
        "NOTEQ",        
        "ANDAND",
        "OROR", 
        "PLUSPLUS",     
        "MINUSMINUS",
        "PLUS",
        "MINUS",
        "MULT", 
        "DIV",  
        "MOD",  
        "COMP",
        "NOT", 
        "AND",
        "XOR",
        "OR",   
        "LSHIFT",
        "RSHIFT",
        "RRSHIFT",      
        "MULTEQ",
        "DIVEQ",   
        "PLUSEQ",
        "MINUSEQ", 
        "MODEQ",
        "XOREQ",    
        "LSHIFTEQ",
        "RSHIFTEQ", 
        "RRSHIFTEQ",
        "ANDEQ",
        "OREQ",     
        "SEMICOLON",
        "COLON",
        "COMMA",
        "DOT",   
        "INSTANCEOF",   
        "IMPORT", 
        "LBRACE",
        "RBRACE", 
        "LPAREN",
        "RPAREN",
        "LBRACK",
        "RBRACK"};
        
        public Token (int p_kind, String p_lexeme, int p_line, int p_charBegin, int p_charEnd) {
            sym = p_kind;
            lexeme = p_lexeme;
            line = p_line;
            charBegin = p_charBegin;
            charEnd = p_charEnd;
        }
    
    public int getCharBegin() { return charBegin; }
    public int getCharEnd()   { return charEnd; }
    public int getLine() { return line; }
    public String getLexeme() { return lexeme; }
    public int getSym() { return sym; }

    public String toString() {
	return "Token " + names[sym] + " '" + lexeme + "'" + " line " + line + 
	       " pos [" + charBegin + ".." + charEnd + "]";
    }
}



