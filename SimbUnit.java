import java.text.CharacterIterator;
public class SimbUnit extends AFD{
    //possui problema se terminar com simb unit
    @Override
    public Token evaluate(CharacterIterator code){
        
        switch(code.current()){
            case '[':
                code.next();
                return new Token("[","CoA");
            case ']':
                code.next();
                return new Token("]","CoF");
            case '(':
                //System.out.println(code.current()); //debug
                code.next();
                return new Token("(","PA");
            case ')':
                //System.out.println(code.current()); //debug
                code.next();
                return new Token(")","PF");
            case '{':
                code.next();
                return new Token("{","ChA");

            case '}':
                code.next();
                return new Token("}","ChF");

            case ';':
                code.next();
                return new Token(";","EL");
            case '+':
                code.next();
                return new Token("+","PLUS");
            case '-':
                //System.out.println(code.current()); debug
                code.next();
                return new Token("-","MINUS");
            case '*':
                code.next();
                return new Token("*","MULTIPLY");
            case '/':
                code.next();
                return new Token("/","DIVIDE");
            case ',':
                code.next();
                return new Token(",","COMMA");
            case ':':
                code.next();
                return new Token(":","COLON");
            case '.':
                code.next();
                return new Token(".","PERIOD");
            case '=':
                code.next();
                return new Token("=","EQUALS");

            default:
                return null;
        }
        
    }
}