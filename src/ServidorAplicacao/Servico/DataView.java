package ServidorAplicacao.Servico;

/**
 * This is the view class that contains information about the sitio domain
 * objects.
 * 
 * @author Ricardo Nortadas
 */

public class DataView {
    private int _dia;

    private String _mes;

    private int _ano;

    public DataView(int dia, String mes, int ano) {
        _dia = dia;
        _mes = mes;
        _ano = ano;
    }

    public int getDia() {
        return _dia;
    }

    public String getMes() {
        return _mes;
    }

    public int getAno() {
        return _ano;
    }
}