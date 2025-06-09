package back;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GerenciadorPalavras implements CSVManager {
    private ArrayList<PalavraDev> palavras;

    public GerenciadorPalavras() {
        palavras = new ArrayList<>();
    }

    public void adicionar(PalavraDev palavra) {
        if (palavra.validar()) {
            palavras.add(palavra);
        }
    }

    public ArrayList<PalavraDev> getPalavras() {
        return palavras;
    }

    @Override
    public void salvarEmCSV(String caminhoArquivo) {
        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo));

            for (PalavraDev palavra : palavras) {
                writer.write(palavra.getTexto());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    @Override
    public void carregarDeCSV(String caminhoArquivo) {
        palavras.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo));

            String linha;
            while ((linha = reader.readLine()) != null) {
                PalavraDev palavra = new PalavraDev(linha);
                
                if (palavra.validar()) {
                    palavras.add(palavra);
                }
                
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Erro ao carregar: " + e.getMessage());
        }
    }
}
