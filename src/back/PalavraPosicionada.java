package back;
import java.util.Objects;

public class PalavraPosicionada {
    PalavraDev palavra;
    int linha;
    int coluna;
    Direcao direcao;

    public PalavraPosicionada(PalavraDev palavra, int linha, int coluna, Direcao direcao) {
        this.palavra = palavra;
        this.linha = linha;
        this.coluna = coluna;
        this.direcao = direcao;
    }

    public PalavraDev getPalavra() {
        return palavra;
    }

    public int getLinha(){
        return linha;
    }

    public int getColuna(){
        return coluna;
    }

    public Direcao getDirecao(){
        return direcao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        PalavraPosicionada that = (PalavraPosicionada) o;
        
        return linha == that.linha && coluna == that.coluna && direcao == that.direcao && palavra.getTexto().equalsIgnoreCase(that.palavra.getTexto());

    }

    @Override
    public int hashCode() {
        return Objects.hash(palavra.getTexto().toLowerCase(), linha, coluna, direcao);

    }
}
