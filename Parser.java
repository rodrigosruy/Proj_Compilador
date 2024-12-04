import java.util.List;
import java.util.Map;
import java.util.HashMap;
public class Parser {
    List<Token> tokens;
    Token token;
    String linguagem;
    String traducao;
    
    Map<String,String> variaveis;
    String tipovar;
    public Parser(List<Token> tokens,String linguagem){
        this.tokens = tokens;
        this.token = nextToken();//pega a 1a token
        this.traducao = ""; 
        variaveis = new HashMap<String,String>();
        this.linguagem = linguagem;


    }
    public Token nextToken(){
        if (tokens.size() > 0){
            //System.out.println("NAO EH NULO!"); //debug
            Token aux = tokens.remove(0);
            //System.out.println(aux);
            return aux;
        }
        return null;
        //alternativamente
        //return token.size()>0 ? tokens.remove(0) : null;
    }
    private void erro(String regra){
        System.out.println("Regra "+ regra);
        if (regra.equals("main")){
            System.out.println("FALTOU MAIN!");
        }
        else{
            System.out.println("token invalido "+ token.getLexema());
        }
        System.exit(0);
    }
    public boolean deteccao(){ //detecta qual caso deve entrar!
        while(token.getTipo().equals("COMMENT")){ //pula comentarios!
            token = nextToken(); //pega a prox token
        }
        
        if (token.getLexema().equals("se")){ //caso se
            System.out.println("ENTROU SE!"); //debug
            se();
            System.out.println("TERMINOU SE! :)"); //debug
            if (token.getTipo().equals("EOF")){
                System.out.println("EOF SE!");
                
            }
            

        }
        else if (token.getLexema().equals("enquanto")){ //caso enquanto
            System.out.println("ENTROU ENQUANTO!"); //debug
            enquanto();
            System.out.println("TERMINOU ENQUANTO! :)"); //debug
            if (token.getTipo().equals("EOF")){
                
                System.out.println("EOF ENQUANTO!");
            }
            
            

        }
        else if (token.getLexema().equals("por")){ //caso por
            System.out.println("ENTROU POR!"); //debug
            por();
            System.out.println("TERMINOU POR :)"); //debug
            if (token.getTipo().equals("EOF")){
                System.out.println("EOF POR!"); //debug

            }
            
        }
        else if (token.getTipo().equals("ID")){ //caso onde comando comeca com ID ex: var = 2
            System.out.println("ENTROU EXPRESSAO!"); //debug
            atribuicao();
            System.out.println("TERMINOU EXPRESSAO! :)");
            if (token.getTipo().equals("EOF")){
                System.out.println("EOF EXPRESSAO!");
            }
            
        }
        else if (token.getLexema().equals("int")||token.getLexema().equals("float")||token.getLexema().equals("bool")){ //caso de declaracao ex: bool foi = verdade ou bool foi
            System.out.println("ENTROU DECLARACAO!");
            declaracao();
            System.out.println("TERMINOU DECLARACAO! :)");
            if (token.getTipo().equals("EOF")){
                System.out.println("EOF DECLARACAO!");
            }

        }
        else if (token.getLexema().equals("saida")){
            System.out.println("ENTROU SAIDA!");
            saida();
            System.out.println("TERMINOU SAIDA! :)");
            if (token.getTipo().equals("EOF")){
                System.out.println("EOF SAIDA!");
            }
        }
        else if (token.getLexema().equals("entrada")){
            System.out.println("ENTROU ENTRADA!");
            entrada();
            System.out.println("TERMINOU ENTRADA! :)");
            if (token.getTipo().equals("EOF")){
                System.out.println("EOF ENTRADA!");
            }
        }
        else if (token.getTipo().equals("EOF")){ //caso chegou no fim, EOF! (possivelmente caso de erro?)
            System.out.println("ENTROU CASO EOF!");
            return true;
        }
        else{ //caso nao entre em nenhum ja feito, vai pra o prox token! Caso de erro (estou deixando passar por agora para facilitar debuggagem!)
            System.out.println("PROBLEMA! NAO ENTROU A TOKEN: "+token); //debug
            
            erro("deteccao: COMANDO NAO RECONHECIDO!");
            return false;
        }
        return true;
    }
    public String main(){ //principal! logica comecao aqui!
        if (linguagem.equals("j")){
                addToString("import java.util.Scanner;\n");
                addToString("public class traducao{\n");
        }
        else if (linguagem.equals("c")){
            addToString("#include <stdio.h>\n");
        }
        while(matchL("incluir","#include ", "import ")){ //inclusoes sao no comeco do codigo!
            
            System.out.println("ENTROU INCLUIR!");
            incluir();
            System.out.println("TERMINOU INCLUIR! :)");
        }
        System.out.println("ACABOU INCLUSOES!"); //debug
        //PROGRAMA COMECA AQUI!!!
        if (matchL("main","void main(){\n","public static void main(String args[]){\nScanner entrada = new Scanner(System.in);\n")){ //main logo apos inclusoes!
            while (!token.getTipo().equals("EOF")){ //vai pelos tokens ate achar EOF!
                deteccao();
            }
            System.out.println("FORA DO MAIN! EOF!"); //debug
            
            if (linguagem.equals("j")){
                addToString("\nentrada.close();\n}\n}");
            }
            else{
                addToString("\n}");
            }
            saveString();
            
            return traducao; 
        }
        erro("main"); //erro dado quando há falta de main!
        return null;

    }

    public boolean bloco(){ //bloco! tipo o main, mas para quando ve }!
        
        while (!token.getLexema().equals("}")){ //vai pelos tokens ate achar fechamento do bloco!
            deteccao();
        }
        System.out.println("ACABOU BLOCO!"); //debug
        return true; //utilizado na recursao, para saber se deu certo!

    }
    public boolean incluir(){ //incluir! include de C, utilizado para bibliotecas/headers ex: incluir stdio.h
        
        if (token.getTipo().equals("ID") ){ //feito assim para evitar ser considerado uma variável de verdade!
            if (linguagem.equals("c")){ //checa se a linguagem selecionada eh C
                addToString("<"+token.getLexema()+".h>\n");
                token = nextToken();
                return true;
            }
            else if (linguagem.equals("j")){ //checa se a linguagem selecionada eh Java
                
                addToString("java.util."+token.getLexema()+";\n");
                token= nextToken();
                return true;
            }
            
            
        }
        
        erro("incluir");
        return false;
            
        
       
    }
    public boolean saida(){ //a fazer
        if (matchL("saida","printf","System.out.println") && matchL("(","(\"%","(")){
            if (token.getTipo().equals("ID")){
                if (variaveis.containsKey(token.getLexema())){
                    if (variaveis.get(token.getLexema()).equals("float")){ //caso onde variavel eh float!
                        matchT("ID","f\\n\","+token.getLexema(),token.getLexema());
                        
                    }
                    else{
                        matchT("ID","d\\n\","+token.getLexema(),token.getLexema());
                    }
                    matchL(")");
                    addToString(";\n");
                    return true;
                    
                }
                erro("saida: VARIAVEL NAO DECLARADA!");
                return false;
            }
            erro("saida: FALTA VARIAVEL!");
            return false;

        }
        erro("saida");
        return false;
    }
    public boolean entrada(){ //a fazer
        if (matchL("entrada","scanf","") && matchL("(","(\"%","")){
            if (token.getTipo().equals("ID")){
                if (variaveis.containsKey(token.getLexema())){
                    if (variaveis.get(token.getLexema()).equals("float")){ //caso onde variavel eh float!
                        matchT("ID","f\",&"+token.getLexema(),token.getLexema()+"=Float.parseFloat(entrada.nextLine()");
                        
                    }
                    else{
                        matchT("ID","d\",&"+token.getLexema(),token.getLexema()+"=Integer.parseInt(entrada.nextLine()");
                    }
                    matchL(")");
                    addToString(";\n");
                    return true;
                    
                }
                erro("saida: VARIAVEL NAO DECLARADA!");
                return false;
            }
            erro("saida: FALTA VARIAVEL!");
            return false;
            

        }
        erro("entrada");
        return false;
    }


    

    public boolean declaracao(){ //indica se ha uma declaracao ex: bool foi = true 
        tipovar = token.getLexema();
        if (tipo()){ //checa se foi usada palavra de tipo ex: bool, int, float
            if (variaveis.containsKey(token.getLexema())){ //caso onde uma variavel com o mesmo lexema foi declarada!
                erro("declaracao: VARIAVEL JA DECLARADA!");
                return false;
            }
            if (token.getTipo().equals("ID")){ //caso seja variavel, continua declaracao
                variaveis.put(token.getLexema(),tipovar);
                matchT("ID",token.getLexema());
                if (matchL("=")){
                    if (tipovar.equals("bool")){ //caso booleano verdade/falso
                        if(verdadefalso()){
                            addToString(";\n");
                            return true;
                        }
                        erro("declaracao");
                        return false;
                    }
                    else{
                        if (Eexpressao()){ //caso numerico int/float
                            addToString(";\n");
                            return true;
                        }
                        erro("declaracao");
                        return false;
                    }
                    
                }
                addToString(";\n");
                return true; //caso tipo var, onde nao eh declarado o valor inicial! ex: bool foi
            }
            erro("declaracao: ERRO NA VARIAVEL");
            
        }
        erro("declaracao");
        return false;
    }

    public boolean enquanto(){ //while = enquanto 
        //exemplo:
        //enquanto condicao {
        //  bloco 
        //}
        if (matchL("enquanto","while") && condicao() && matchL("{","{\n") && bloco() && matchL("}","}\n")){
            return true;
        }
        erro("enquanto");
        return false;
    }

    public boolean por(){ //for = por //TERMINAR FINAL!
        //exemplo:
        // por x x<2 + {
        // bloco
        //}
        if (matchL("por","for(") && matchT("ID",token.getLexema()+";",token.getLexema()+"="+token.getLexema()+";") && condicao() && (matchL("+",";++") || matchL("-",";--")) && matchT("ID",token.getLexema()+")") && matchL("{","{\n") && bloco() && matchL("}","}\n")){
            return true;
        }
        erro("por");
        return false;
    }

    public boolean se(){ //if else = se senao 
        //exemplo:
        // se condicao{
        //      bloco
        //}
        // senao{
        //      bloco
        //}
        
        if (matchL("se","if")  && condicao() && matchL("{","{\n") && bloco() && matchL("}","}\n")){
                if (token.getLexema().equals("senao")){ //encontra senao (faz com que senao se torne opcional!)
                    senao();
                }
                return true;
        }

        return false;
    }

    public boolean senao(){ //else separado!
        if(matchL("senao","else") && matchL("{","{\n") && bloco() && matchL("}","}\n")){
            return true;
        }
        erro("senao");
        return false;
    }

    private boolean idounum(){ //retorna se eh id ou num
        if (token.getTipo().equals("ID")){ //checa se eh id
            if (variaveis.containsKey(token.getLexema())){ //checa se ja foi declarado
                String varatual = variaveis.get(token.getLexema());//qual o tipo da var atual
                if (tipovar.equals("float") && (varatual.equals("float") || varatual.equals("int"))){ //checa se a variavel atual eh float e se eh atribuida ints ou floats
                    matchT("ID",token.getLexema());
                    return true;
                }
                else if (tipovar.equals("int") && varatual.equals("int")){//checa se a variavel eh int e se eh atribuida apenas ints
                    matchT("ID",token.getLexema());
                    return true;
                }
                else if (tipovar.equals("bool") && varatual.equals("bool")){//checa se a variavel eh bool e se eh atribuida apenas bool
                    matchT("ID",token.getLexema());
                    return true;
                }
                erro("idounum: VARIAVEL DE TIPO ERRADO!");
                return false;
                
            }
            erro("idounum: VARIAVEL NAO EXISTE");
            return false;
            
        }
        else if (token.getTipo().equals("INT") || token.getTipo().equals("FLOAT")){ //checa se eh numero
            num();
            return true;
        }
        
        return false;
    }

    private boolean atribuicao(){ //retorna se eh uma atribuicao ( id = idounum)
        tipovar = variaveis.get(token.getLexema());
        if (tipovar.equals("bool")){
            if (matchT("ID",token.getLexema()) && matchL("=") && verdadefalso()){ //caso verdade falso (booleano)
                addToString(";\n");
                return true;
            }
            erro("atribuicao");
            return false;
        }
        else if (matchT("ID",token.getLexema()) && matchL("=") && Eexpressao()){ //caso numerico (expressao)
            addToString(";\n");
            return true;
        }
        erro("atribuicao");
        return false;
    }
    // EXPRESSOES!!!!!!!!!
    private boolean Eexpressao(){ 
        if(Texpressao() && Elinha()){
            return true;
        }
        return false;
        
    }
    private boolean Elinha(){
        if (matchL("+") || matchL("-")){
            Texpressao();
            Elinha();
            
            
        }
        return true; //caso E
    }
    private boolean Texpressao(){ //continuacao de expressao
        if (Fexpressao() && Tlinha()){
            return true;
        }
        return false;

    }
    private boolean Tlinha(){
        if (matchL("*") ||matchL("/")){
            Fexpressao();
            Tlinha();
                
                
            
        }
        return true; //caso E
    }
    private boolean Fexpressao(){
        if (!token.getLexema().equals("(")&&idounum()){
            return true;
        }
        else if (matchL("(")){
            Eexpressao();
            if(matchL(")")){
                return true;
            }
            return false; //parenteses nao fechados
        }
        return false;

    }


    private boolean comparador(){ //retorna se eh um comparador
        if (matchL(">") || matchL("<") || matchL(">=")||matchL("<=") || matchL("!=") || matchL("==")){ 
            return true;
        }
        erro("comparador");
        return false;
    }

    private boolean condicao(){ //retorna se eh uma condicao
        addToString("(");
        modificarTipoVar(token.getTipo()); //pega o tipo da 1a variavel
        if (idounum() && comparador() && (idounum()||verdadefalso())){
            addToString(")");
            return true;
        }
        erro("condicao");
        return false;
    }
    private void modificarTipoVar(String tipo){
        if(tipo.equals("ID")){
            tipovar = variaveis.get(token.getLexema());
        }
        else if (tipo.equals("INT")){
            tipovar = "int";
        }
        else if (tipo.equals("FLOAT")){
            tipovar = "float";
        }
    }

    private boolean num(){ //retorna se eh um tipo de num
        if (tipovar.equals("int")){
            if(matchT("INT",token.getLexema())){
                return true;
            }
            
            
        }
        else if (tipovar.equals("float")){
            if  (matchT("FLOAT",token.getLexema())||matchT("INT",token.getLexema())){
                return true;
            }
            
        }
        erro("tipo incompativel de var!");
        return false;
    }

    private boolean verdadefalso(){
        if (matchT("FALSE","0") || matchT("TRUE","1")){
            return true;
        }
        return false;
    }

    //private boolean simbcalc(){
    //    if (matchL("+") || matchL("-") || matchL("/") || matchL("*")){
    //        return true;
    //    }
    //    return false;
    //}

    private boolean tipo(){
        if (matchL("int","int ") || matchL("float","float ") || matchL("bool", "int ")){
            
            return true;
        }
        return false;
    }

    private boolean matchL(String lexema){ //retorna se o lexema dado bate com o lexema na token
        //System.out.println("ENTROU"); //debug
        if (token.getLexema().equals(lexema)){
            
            addToString(lexema);
            token = nextToken();
            return true;
        }
        return false;
    }
    private boolean matchL(String lexema,String novapalavra){ //retorna se o lexema dado bate com o lexema na token
        //System.out.println("ENTROU"); //debug
        if (token.getLexema().equals(lexema)){
            
            addToString(novapalavra);
            token = nextToken();
            return true;
        }
        return false;
    }
    private boolean matchL(String lexema,String novapalavra,String novapalavra2){ //retorna se o lexema dado bate com o lexema na token, e ha diferenca com java
        //System.out.println("ENTROU"); //debug
        if (token.getLexema().equals(lexema)){
            if (linguagem.equals("c")){
                addToString(novapalavra);
                
            }
            else if (linguagem.equals("j")) {
                addToString(novapalavra2);

            }
            token = nextToken();
            return true;
            
        }
        return false;
    }

    private boolean matchT(String tipo){ //retorna se o tipo dado bate com o tipo na token
        if (token.getTipo().equals(tipo)){
            addToString(tipo);
            token = nextToken();
            return true;
        }
        return false;
    }
    private boolean matchT(String tipo,String novapalavra){ //retorna se o tipo dado bate com o tipo na token
        if (token.getTipo().equals(tipo)){
            addToString(novapalavra);
            token = nextToken();
            return true;
        }
        return false;
    }
    private boolean matchT(String tipo,String novapalavra,String novapalavra2){ //retorna se o tipo dado bate com o tipo na token
        if (token.getTipo().equals(tipo)){
            if (linguagem.equals("c")){
                addToString(novapalavra);
            }
            else if (linguagem.equals("j")){
                addToString(novapalavra2);
            }
            
            token = nextToken();
            return true;
        }
        return false;
    }
    private void addToString(String palavra){ //adiciona dados para a string do arquivo!
        traducao += palavra;
    }
    
    private void saveString(){ //debuggao
        System.out.println(traducao);
        System.out.println(variaveis);
    }
    
}
