package front;
import javax.swing.*;

import back.Direcao;
import back.GerenciadorPalavras;
import back.PalavraDev;
import back.PalavraPosicionada;

import java.awt.*;
import java.io.FileWriter;
import java.util.Scanner;

class PalavrasPainel extends JPanel {
    private JTextField palavraField;
    private JButton adicionarBtn;
    private JButton finalizarBtn;

    private DefaultListModel<String> palavrasModel;
    private JList<String> palavrasList;
    private JogoPalavrasCruzadasUI ui;
    public PalavrasPainel(JogoPalavrasCruzadasUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        ui.jogo.setGerenciador(new GerenciadorPalavras());

        JPanel inputPanel = new JPanel(new FlowLayout());
        palavraField = new JTextField(15);
        adicionarBtn = new JButton("Adicionar Palavra");

        inputPanel.add(new JLabel("Palavra:"));
        inputPanel.add(palavraField);
        inputPanel.add(adicionarBtn);


        palavrasModel = new DefaultListModel<>();
        palavrasList = new JList<>(palavrasModel);
        finalizarBtn = new JButton("Finalizar e montar tabuleiro");

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(palavrasList), BorderLayout.CENTER);

        add(finalizarBtn, BorderLayout.SOUTH);

        try {
            new FileWriter("palavrasinseridas.csv", false).close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao limpar CSV: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        adicionarBtn.addActionListener(e -> {
            String palavra = palavraField.getText().trim();
            try {
                if (palavra.isEmpty()){
                    throw new Exception("Digite uma palavra!");
                }

                PalavraDev p = new PalavraDev(palavra);
                if (!p.validar()){
                    throw new Exception("Palavra inválida: " + palavra);
                }

                if (palavra.length() > ui.jogo.getTabuleiro().getColunas() && palavra.length() > ui.jogo.getTabuleiro().getMatriz().length) {
                    throw new Exception("Palavra muito longa para o tabuleiro!");
                }
                    
                // escolher direção
                Object[] options = {"Horizontal", "Vertical"};
                int direcaoEscolhida = JOptionPane.showOptionDialog(
                        this,
                        "Escolha a direção da palavra:",
                        "Direção",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (direcaoEscolhida == JOptionPane.CLOSED_OPTION){
                    return; 
                }

                Direcao direcao;
                switch (direcaoEscolhida) {
                    case 0: 
                        direcao = Direcao.HORIZONTAL; 
                        break;
                    case 1: 
                        direcao = Direcao.VERTICAL; 
                        break;
                    default: 
                        direcao = Direcao.HORIZONTAL;
                }

                int maxLinha = ui.jogo.getTabuleiro().getLinhas() - 1;
                int maxColuna = ui.jogo.getTabuleiro().getColunas() - 1;

                int linha = -1, coluna = -1;

                boolean posicaoValida = false;
                while (!posicaoValida) {
                    String linhaStr = JOptionPane.showInputDialog(this, "Linha inicial (0 a " + maxLinha + "):");
                    String colunaStr = JOptionPane.showInputDialog(this, "Coluna inicial (0 a " + maxColuna + "):");

                    if (linhaStr == null || colunaStr == null){
                        return; // cancelado
                    }

                    try {
                        linha = Integer.parseInt(linhaStr.trim());
                        coluna = Integer.parseInt(colunaStr.trim());
                        if (linha < 0 || linha > maxLinha || coluna < 0 || coluna > maxColuna){
                            throw new Exception("Linha ou coluna fora do intervalo!");
                        }
                            

                        // checar se cabe na direção escolhida
                        int tam = palavra.length();
                        boolean cabe = false;
                        switch (direcao) {
                            case HORIZONTAL:
                                cabe = (coluna + tam <= ui.jogo.getTabuleiro().getColunas());
                                break;
                            case VERTICAL:
                                cabe = (linha + tam <= ui.jogo.getTabuleiro().getLinhas());
                                break;
                        }
                        if (!cabe) {
                            JOptionPane.showMessageDialog(this, "A palavra não cabe na posição e direção escolhidas!", "Erro", JOptionPane.ERROR_MESSAGE);
                        } else {
                            posicaoValida = true;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Digite apenas números inteiros para linha e coluna!", "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }

                boolean conflito = false;
                for (PalavraPosicionada pp : ui.jogo.getPalavrasComPosicao()) {
                    if (pp.getLinha() == linha && pp.getColuna() == coluna && pp.getDirecao() == direcao && pp.getPalavra().getTexto().equalsIgnoreCase(palavra)) {
                        conflito = true;
                        break;
                    }
                }
                if (conflito) {
                    JOptionPane.showMessageDialog(this, "Já existe uma palavra nessa posição inicial e direção!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean sobreposicao = false;
                for (int i = 0; i < palavra.length(); i++) {

                    int l = linha, c = coluna;
                    switch (direcao) {
                        case HORIZONTAL: 
                        // se a direção for HORIZONTAL, aumenta a coluna (c += i) para cada letra, mantendo a linha igual.
                            c += i; 
                            break;
                        case VERTICAL: 
                        // se for VERTICAL, aumenta a linha (l += i) para cada letra, mantendo a coluna igual.
                            l += i; 
                            break;
                    }

                    char[][] matriz = ui.jogo.getTabuleiro().getMatriz();
                    char letraAtual = matriz[l][c];

                    if (letraAtual != '\0' && letraAtual != palavra.toUpperCase().charAt(i) && letraAtual != ' ') {
                        sobreposicao = true;
                        break;
                    }
                }
                if (sobreposicao) {
                    JOptionPane.showMessageDialog(this, "A palavra sobrepõe outra já existente!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                palavrasModel.addElement(palavra + " (" + direcao + " - " + linha + "," + coluna + ")");
                PalavraPosicionada pp = new PalavraPosicionada(p, linha, coluna, direcao);
                ui.jogo.getPalavrasComPosicao().add(pp);
                ui.jogo.getGerenciador().adicionar(p);
                ui.jogo.getTabuleiro().inserirPalavra(palavra.toUpperCase(), linha, coluna, direcao);
                salvarPalavraInseridaCSV(pp);

                JOptionPane.showMessageDialog(this, "Palavra válida e adicionada!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                palavraField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        finalizarBtn.addActionListener(e -> {
            try {
                if (ui.jogo.getGerenciador().getPalavras().isEmpty()){
                    throw new Exception("Nenhuma palavra inserida!");
                }
                    
                ui.jogo.getTabuleiro().preencherAleatorio();
                for (PalavraPosicionada pp : ui.jogo.getPalavrasComPosicao()) {
                    try {
                        ui.jogo.getTabuleiro().inserirPalavra(pp.getPalavra().getTexto().toUpperCase(), pp.getLinha(), pp.getColuna(), pp.getDirecao());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao inserir palavra '" + pp.getPalavra().getTexto() + "': " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }

                ui.mainPanel.add(new JogadorPainel(ui), "jogador");
                ui.cardLayout.show(ui.mainPanel, "jogador");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void salvarPalavraInseridaCSV(PalavraPosicionada pp) {
        try (FileWriter fw = new FileWriter("palavrasinseridas.csv", true)) {
            fw.write(pp.getPalavra().getTexto() + "," + pp.getLinha() + "," + pp.getColuna() + "," + pp.getDirecao() + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar palavra no CSV: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void carregarPalavrasInseridasCSV() {
        ui.jogo.getPalavrasComPosicao().clear();
        ui.jogo.getGerenciador().getPalavras().clear();

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
                    ui.jogo.getPalavrasComPosicao().add(pp);
                    ui.jogo.getGerenciador().adicionar(p);
                    ui.jogo.getTabuleiro().inserirPalavra(texto.toUpperCase(), linha, coluna, direcao);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar palavras do CSV: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}