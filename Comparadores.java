import java.text.CharacterIterator;

public class Comparadores extends AFD {
    @Override
    public Token evaluate(CharacterIterator code){

        //comparadores == != >= <= < >
        String[] todoscomparadores = new String[]{"==","!=",">","<",">=","<="};
        
        String comparador = "";
        while (endLetter(code)){ //vai ler até achar espaco OU numero OU letra
            comparador += code.current();
            code.next();
        }
        for (int i = 0; i <todoscomparadores.length; ++i){
            if (comparador.equals(todoscomparadores[i])){
                switch(i){
                    case 0: // ==
                        return new Token(comparador,"COMP_EQ");
                    case 1: // !=
                        return new Token(comparador,"COMP_INEQ");
                    case 2: // >
                        return new Token(comparador,"COMP_BIG");
                    case 3: // <
                        return new Token(comparador,"COMP_SMALL");
                    case 4: // >=
                        return new Token(comparador,"COMP_BIG_EQ");
                    case 5: // <=
                        return new Token(comparador,"COMP_SMALL_EQ");


                }
            }
        }

        return null;
    }
    public boolean endLetter(CharacterIterator code){ //colocar caracteres que devem interromper
        if (Character.isLetterOrDigit(code.current()) || code.current() == ' '){ //qualquer letra ou digito ou espaco vai terminalo
            //System.out.println("SAIU COMPARADORES!");
            return false;
            
        }
        //System.out.println("ENTROU COMPARADORES!");
        return true;
        
    }

    
}
