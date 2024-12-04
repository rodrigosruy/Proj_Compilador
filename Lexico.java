import java.util.ArrayList;
import java.util.List;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
public class Lexico {
    private List<Token> tokens;
    private List<AFD> afds;
    private CharacterIterator code;
    int linha;
    int posultimoln;
    int coluna;

    public Lexico(String code){
        tokens = new ArrayList<>();
        afds = new ArrayList<>();
        this.code = new StringCharacterIterator(code);
        
        afds.add(new Numero()); //identifica int e float
        afds.add(new Identificador()); //identifica identificadores (hehe) //IMP! Deixa-lo antes do SimbUnit!
        afds.add(new Comparadores()); //comparadores como ==, >=, <= etc.
        afds.add(new SimbUnit()); //identifica simbolos unitarios como +,-,(,),[,],{,}, etc.
        afds.add(new Comentario()); //identifica comentarios
        
        

    }
    public void pularEspaco(){ 
        
        while (code.current() == ' ' || code.current() == '\n') {
            if (code.current() == '\n'){
                posultimoln = code.getIndex();
                linha+=1;
                //System.out.println("LENDO LINHA " + linha ); //debug
            }
            coluna = code.getIndex() - posultimoln;
            code.next();
        }
        
    }
    public List<Token> getTokens(){
        boolean aceito;
        linha = 0;
        posultimoln = 0;
        coluna = 0;
        while(code.current() != CharacterIterator.DONE){ //percorre todo o arquivo, roda até EOF
            aceito = false;
            pularEspaco();
            
            if (code.current() == CharacterIterator.DONE){ //evita casos onde o arquivo nao contem nenhum simbolo do alfabeto
                break; //sai do while
            }
            for (AFD afd:afds){ //percorre todos os afd
                int posatual = code.getIndex();
                //System.out.println(posatual); //mostra a posicao da token
                Token t1 = afd.evaluate(code);
                if (t1 != null){ //caso seja valido
                    aceito = true;
                    tokens.add(t1);
                    break; //sai do for
                }
                else{
                    code.setIndex(posatual); //caso o afd atual seja invalido, coloca o leitor de volta na posicao inicial e testa o proximo afd
                }
            }
            if (aceito == false){
                throw new RuntimeException("\nErro! Token Nao Reconhecida!\nToken "+linha+":"+coluna+" >> "+ code.current());
            }

        }
        tokens.add(new Token("$","EOF"));
        return tokens;
    }
    
}
