import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.List;

public class MainAutomato{
    public static void main(String[] args){
        try{
        File arquivo = new File(args[0]);
        String linguagem = args[1];
        Scanner leitor = new Scanner(arquivo);
        String tudo = "";
        
        while(leitor.hasNextLine()){
            
            tudo += leitor.nextLine(); //adiciona tudo do arquivo para a string
            tudo += "\n";
        }
        tudo+=" ";
        leitor.close(); //fecha leitor
        System.out.println(tudo); //mostra o conteudo do arquivo
        
        
        Lexico l1 = new Lexico(tudo);
        List<Token> t1 = l1.getTokens(); //t1 agora possui todas as tokens

        for (Token token:t1){ //printa todas as tokens
            System.out.println(token);
        }
        
        Parser p1 = new Parser(t1,linguagem); //parser para o sintatico
        String traducao = p1.main();
        String nomearquivo ="traducao";
        if (linguagem.equals("c")){
            nomearquivo +=".c";
        }
        else if (linguagem.equals("j")){
            nomearquivo+=".java";

        }

        try{
            
            File arqFinal = new File(nomearquivo);
            arqFinal.createNewFile();
            FileWriter escritor = new FileWriter(nomearquivo);
            escritor.write(traducao);
            escritor.close();
            System.out.println("SUCESSO!");
            
        }
        catch(IOException e){
            throw new RuntimeException("\nPROBLEMA AO CRIAR OU ESCREVER EM ARQUIVO!");
            
        }
        


        }
        catch (FileNotFoundException e){
            throw new RuntimeException("\nArquivo nao encontrado!\nVerifique se o arquivo esta no diretorio correto e se o nome e extensao estao corretos!");
        }
        

    }
}