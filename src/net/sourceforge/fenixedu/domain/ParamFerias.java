package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class ParamFerias {
    private int _codigoInterno;

    private String _sigla;

    private String _designacao;

    public ParamFerias() {
        _codigoInterno = 0;
        _sigla = null;
        _designacao = null;
    }

    public ParamFerias(int codigoInterno, String sigla, String designacao) {
        _codigoInterno = codigoInterno;
        _sigla = sigla;
        _designacao = designacao;
    }

    /* Selectores */
    public int getCodigoInterno() {
        return _codigoInterno;
    }

    public String getSigla() {
        return _sigla;
    }

    public String getDesignacao() {
        return _designacao;
    }

    /* Modificadores */
    public void setCodigoInterno(int codigoInterno) {
        _codigoInterno = codigoInterno;
    }

    public void setSigla(String sigla) {
        _sigla = sigla;
    }

    public void setDesignacao(String designacao) {
        _designacao = designacao;
    }
}