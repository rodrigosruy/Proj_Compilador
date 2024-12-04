import java.text.CharacterIterator;
public class Numero extends AFD {
    
    @Override
    public Token evaluate(CharacterIterator code){

        if (Character.isDigit(code.current())){
            String number = readNumber(code);
            if (code.current() == '.'){
                number = number + '.';
                code.next();
                if (Character.isDigit(code.current())){
                    number = number + readNumber(code); 
                    
                    //System.out.println(number); //debug
                    return new Token(number,"FLOAT");
                    
                }
                
            }
            else{
                return new Token(number,"INT");
            }
            
        }
        return null;
    }
    private String readNumber(CharacterIterator code){
        String number = "";
       
        while (Character.isDigit(code.current())){
            
            number+= code.current();
            code.next();
        }
        
       
        return number;
    }
    
}
