package front;
import javax.swing.*;

import back.Direcao;
import back.JogoPalavrasCruzadas;
import back.PalavraDev;
import back.PalavraPosicionada;

import java.awt.*;
import java.util.Scanner;

public class JogoPalavrasCruzadasUI extends JFrame {
    CardLayout cardLayout;
    JPanel mainPanel;
    JogoPalavrasCruzadas jogo;

    public JogoPalavrasCruzadasUI() {

        super("Jogo Palavras Cruzadas");
        jogo = new JogoPalavrasCruzadas();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(new DevPainel(this), "dev");
        mainPanel.add(new TabuleiroPainel(this), "tabuleiro");
        mainPanel.add(new PalavrasPainel(this), "palavras");

        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        cardLayout.show(mainPanel, "dev");
    }

    public void carregarPalavrasInseridasCSV() {
        jogo.getPalavrasComPosicao().clear();
        jogo.getGerenciador().getPalavras().clear();
        try (Scanner sc = new Scanner(new java.io.File("palavrasinseridas.csv"))) {
            while (sc.hasNextLine()) {
                String[] partes = sc.nextLine().split(",");
                if (partes.length == 4) {
                    String texto = partes[0];
                    int linha = Integer.parseInt(partes[1]);
                    int coluna = Integer.parseInt(partes[2]);
                    Direcao direcao = Direcao.valueOf(partes[3]);
                    PalavraDev p = new PalavraDev(texto);
                    PalavraPosicionada pp = new PalavraPosicionada(p, linha, coluna, direcao);
                    jogo.getPalavrasComPosicao().add(pp);
                    jogo.getGerenciador().adicionar(p);
                    jogo.getTabuleiro().inserirPalavra(texto.toUpperCase(), linha, coluna, direcao);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar palavras do CSV: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
