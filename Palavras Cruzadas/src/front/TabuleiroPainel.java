package front;
import javax.swing.*;
import back.Tabuleiro;
import java.awt.*;


class TabuleiroPainel extends JPanel {
    public TabuleiroPainel(JogoPalavrasCruzadasUI ui) {
        setLayout(new FlowLayout());

        JLabel linhasLabel = new JLabel("Linhas (mínimo 2):");
        JTextField linhasField = new JTextField(5);
        JLabel colunasLabel = new JLabel("Colunas (mínimo 2):");
        JTextField colunasField = new JTextField(5);
        JButton nextBtn = new JButton("Próximo");

        add(linhasLabel); 
        add(linhasField);
        add(colunasLabel); 
        add(colunasField);
        add(nextBtn);

        nextBtn.addActionListener(e -> {
            String linhasTxt = linhasField.getText().trim();
            String colunasTxt = colunasField.getText().trim();

            // verifica se algum dos campos está vazio
            if (linhasTxt.isEmpty() || colunasTxt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Não pode haver entradas vazias!", "Erro", JOptionPane.ERROR_MESSAGE);
                return; // interrompe a execução
            }
            try {
                int linhas = Integer.parseInt(linhasTxt);
                int colunas = Integer.parseInt(colunasTxt);

                if (linhas < 2 || colunas < 2){ 
                    throw new Exception("Mínimo 2 para linhas e colunas");
                }
                ui.jogo.setTabuleiro(new Tabuleiro(linhas, colunas));
                ui.cardLayout.show(ui.mainPanel, "palavras");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Não pode inserir letras, digite apenas números inteiros", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}