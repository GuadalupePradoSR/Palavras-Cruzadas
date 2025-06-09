package back;
public abstract class Palavra {

    protected String texto;

    public Palavra(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public abstract boolean validar(); //polimorfismo: método abstrato

}
