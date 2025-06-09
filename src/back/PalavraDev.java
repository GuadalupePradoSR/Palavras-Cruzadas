package back;
public class PalavraDev extends Palavra{

    public PalavraDev(String texto) {
        super(texto);

    }

    @Override
    public boolean validar() {
        return texto.matches("^[a-zA-Z]+$");
    }

}