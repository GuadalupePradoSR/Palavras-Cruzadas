package back;
import java.util.HashSet;

public class Jogador implements ValidadorNome{
    private String nome;
    private HashSet<PalavraPosicionada> palavrasEncontradas;

    public Jogador(String nome) {
        this.nome = nome;
        palavrasEncontradas = new HashSet<>();
    }

    public void adicionarPalavra(PalavraPosicionada palavraPosicionada) {
        palavrasEncontradas.add(palavraPosicionada);
    }

    public HashSet<PalavraPosicionada> getPalavrasEncontradas() {
        return palavrasEncontradas;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public boolean validar(String nome) {
        return nome.matches(".*\\p{L}.*") && nome.matches("^[\\p{L} ]+$");
        // pelo menos uma letra, apenas letras e espaços
    }
}
