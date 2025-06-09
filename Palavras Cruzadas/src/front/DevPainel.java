package front;
import javax.swing.*;

import back.Dev;
import java.awt.*;

class DevPainel extends JPanel {

    public DevPainel(JogoPalavrasCruzadasUI ui) {
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new FlowLayout());
        
        JLabel label = new JLabel("Dev, digite seu nome:");

        JTextField nomeField = new JTextField(20);

        JButton nextBtn = new JButton("Próximo");

        form.add(label);
        form.add(nomeField);
        form.add(nextBtn);
        add(form, BorderLayout.CENTER);
        

        nextBtn.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            
            try {
                if (nome.isEmpty()){
                    throw new Exception("Nome não pode ser vazio");
                }
                Dev devTemp = new Dev(nome);

                if (!devTemp.validar(nome)){
                    throw new Exception("Nome deve conter apenas letras e acento");
                }
                ui.jogo.setDev(devTemp);

                ui.cardLayout.show(ui.mainPanel, "tabuleiro");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}