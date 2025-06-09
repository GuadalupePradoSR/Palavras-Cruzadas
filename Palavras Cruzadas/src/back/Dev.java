package back;
public class Dev implements ValidadorNome{
    String nome;

    public Dev(String nome){
        this.nome = nome;
    }

    @Override
    public boolean validar(String nome) {

        return nome.matches("^(?=.*\\p{L})[\\p{L} ]+$");
        // pelo menos uma letra incluindo acentuadas, pode apenas letras e espaços
    }
}