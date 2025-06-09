package back;
import java.util.Random;

public class Tabuleiro {
    private char[][] matriz;
    private int linhas;
    private int colunas;

    public Tabuleiro(int linhas, int colunas) {

        this.linhas = linhas;
        this.colunas = colunas;
        matriz = new char[linhas][colunas];

    }

    public void preencherAleatorio() {
        Random rand = new Random();
        for (int i = 0; i < linhas; i++) {
            // i = linha
            for (int j = 0; j < colunas; j++) {
                // j = coluna
                matriz[i][j] = (char) ('A' + rand.nextInt(26));
            }
        }
    }

    public void inserirPalavra(String palavra, int linha, int coluna, Direcao direcao) {
        for (int i = 0; i < palavra.length(); i++) {
            int l = linha, c = coluna;
            switch (direcao) {
                case HORIZONTAL:
                    c += i; // se for HORIZONTAL, aumenta a coluna (c += i) para cada letra, mantendo a linha igual.
                    break;
                case VERTICAL:
                    l += i; // se for VERTICAL, aumenta a linha (l += i) para cada letra, mantendo a coluna igual.
                    break;
            }
            matriz[l][c] = palavra.charAt(i); // coloca a letra correspondente da palavra (palavra.charAt(i)) na posição [l][c] da matriz.
        }
    }

    public char[][] getMatriz() {
        return matriz;
    }

    public int getColunas(){
        return colunas;
    }

    public int getLinhas(){
        return linhas;
    }
}
