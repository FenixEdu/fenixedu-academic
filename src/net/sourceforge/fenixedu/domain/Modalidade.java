package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public class Modalidade {
    private int _codigoInterno;

    private String _designacao;

    /* Construtores */
    public Modalidade(String designacao) {
        _codigoInterno = 0;
        _designacao = designacao;
    }

    public Modalidade(int codigoInterno, String designacao) {
        _codigoInterno = codigoInterno;
        _designacao = designacao;
    }

    /* Selectores */
    public int getCodigoInterno() {
        return _codigoInterno;
    }

    public String getDesignacao() {
        return _designacao;
    }

    /* Modificadores */
    public void setCodigoInterno(int codigoInterno) {
        _codigoInterno = codigoInterno;
    }

    public void setDesignacao(String designacao) {
        _designacao = designacao;
    }

    /* teste da igualdade */
    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof Modalidade) {
            Modalidade modalidade = (Modalidade) obj;

            resultado = (this.getCodigoInterno() == modalidade.getCodigoInterno() && this
                    .getDesignacao() == modalidade.getDesignacao());
        }
        return resultado;
    }
}