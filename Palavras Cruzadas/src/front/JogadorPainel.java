package front;
import javax.swing.*;
import back.PalavraPosicionada;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

class JogadorPainel extends JPanel {
    private JTextArea tabuleiroArea;
    private JTextField tentativaField;
    private JTextArea encontradasArea;
    private JButton tentarBtn;
    private JButton finalizarBtn;

    private Set<PalavraPosicionada> palavrasEncontradas = new HashSet<>();

    public JogadorPainel(JogoPalavrasCruzadasUI ui) {

        ui.carregarPalavrasInseridasCSV();

        setLayout(new BorderLayout());

        tabuleiroArea = new JTextArea(5, 30);

        tabuleiroArea.setEditable(false);

        tentativaField = new JTextField(20);
        tentarBtn = new JButton("Tentar");
        encontradasArea = new JTextArea(5, 30);
        encontradasArea.setEditable(false);
        finalizarBtn = new JButton("Finalizar Jogo");

        JPanel top = new JPanel(new BorderLayout());

        top.add(new JLabel("Tabuleiro:"), BorderLayout.NORTH);

        top.add(new JScrollPane(tabuleiroArea), BorderLayout.CENTER);

        JPanel mid = new JPanel(new FlowLayout());

        mid.add(new JLabel("Digite uma palavra encontrada:"));

        mid.add(tentativaField);

        mid.add(tentarBtn);

        JPanel bot = new JPanel(new BorderLayout());
        bot.add(new JLabel("Palavras encontradas:"), BorderLayout.NORTH);
        bot.add(new JScrollPane(encontradasArea), BorderLayout.CENTER);
        bot.add(finalizarBtn, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(mid, BorderLayout.CENTER);
        add(bot, BorderLayout.SOUTH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                mostrarTabuleiro(ui);
                encontradasArea.setText("");
            }
        });

        tentarBtn.addActionListener(e -> {
            String tentativa = tentativaField.getText().trim();
            if (tentativa.isEmpty()) return;
            boolean achou = false;

            for (PalavraPosicionada pp : ui.jogo.getPalavrasComPosicao()) {
                if (pp.getPalavra().getTexto().equalsIgnoreCase(tentativa) && !palavrasEncontradas.contains(pp)) {
                    palavrasEncontradas.add(pp);
                    encontradasArea.append(tentativa + " (" + pp.getDirecao() + " - " + pp.getLinha() + "," + pp.getColuna() + ")\n");
                    salvarPalavraAchadaCSV(pp);
                    achou = true;
                    break;
                }
            }

            if (!achou) {
                JOptionPane.showMessageDialog(this, "Palavra não encontrada ou já encontrada em todas as posições!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            tentativaField.setText("");

            if (palavrasEncontradas.size() == ui.jogo.getPalavrasComPosicao().size()) {
                JOptionPane.showMessageDialog(this, "Parabéns! Você encontrou todas as palavras", "Fim de jogo", JOptionPane.INFORMATION_MESSAGE);
                tentativaField.setEditable(false);
                tentarBtn.setEnabled(false);
            }
        });

        finalizarBtn.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(this, "Deseja finalizar o jogo?", "Finalizar", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window != null) window.dispose();
            }
        });
    }

    private void mostrarTabuleiro(JogoPalavrasCruzadasUI ui) {
        char[][] matriz = ui.jogo.getTabuleiro().getMatriz();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                sb.append(matriz[i][j]).append(" ");
            }
            sb.append("\n");
        }
        tabuleiroArea.setText(sb.toString());
    }

    private void salvarPalavraAchadaCSV(PalavraPosicionada pp) {
        try (FileWriter fw = new FileWriter("palavrasachadas.csv", true)) {
            fw.write(pp.getPalavra().getTexto() + "," + pp.getLinha() + "," + pp.getColuna() + "," + pp.getDirecao() + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar palavra achada no CSV: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}