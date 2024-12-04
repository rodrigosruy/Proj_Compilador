public class Variaveis {
    String nome;
    String tipo;
    public Variaveis(String tipo,String nome){
        this.tipo = tipo;
        this.nome = nome;
    }
    @Override
    public String toString(){
        return "<" + tipo + "," + nome + ">";
    }
}
