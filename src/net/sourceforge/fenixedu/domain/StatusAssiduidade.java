package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;

/**
 * 
 * @author Fernanda Quitério & Tânia Pousão
 */
public class StatusAssiduidade extends DomainObject {
    private int _codigoInterno;

    private String _sigla;

    private String _designacao;

    private String _estado;

    private String _assiduidade;

    private int _quem;

    private Timestamp _quando;

    /* Construtores */
    public StatusAssiduidade() {
        _codigoInterno = 0;
        _sigla = null;
        _designacao = null;
        _estado = "inactivo";
        _assiduidade = "false";
        _quem = 0;
        _quando = null;
    }

    public StatusAssiduidade(String sigla, String designacao, String estado, String assiduidade) {
        _codigoInterno = 0;
        _sigla = sigla;
        _designacao = designacao;
        _estado = estado;
        _assiduidade = assiduidade;
        _quem = 0;
        _quando = null;
    }

    public StatusAssiduidade(String sigla, String designacao, String estado, String assiduidade,
            int quem, Timestamp quando) {
        _codigoInterno = 0;
        _sigla = sigla;
        _designacao = designacao;
        _estado = estado;
        _assiduidade = assiduidade;
        _quem = quem;
        _quando = quando;
    }

    public StatusAssiduidade(int codigoInterno, String sigla, String designacao, String estado,
            String assiduidade) {
        _codigoInterno = codigoInterno;
        _sigla = sigla;
        _designacao = designacao;
        _estado = estado;
        _assiduidade = assiduidade;
        _quem = 0;
        _quando = null;
    }

    public StatusAssiduidade(int codigoInterno, String sigla, String designacao, String estado,
            String assiduidade, int quem, Timestamp quando) {
        _codigoInterno = codigoInterno;
        _sigla = sigla;
        _designacao = designacao;
        _estado = estado;
        _assiduidade = assiduidade;
        _quem = quem;
        _quando = quando;
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

    public String getEstado() {
        return _estado;
    }

    public String getAssiduidade() {
        return _assiduidade;
    }

    public int getQuem() {
        return _quem;
    }

    public Timestamp getQuando() {
        return _quando;
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

    public void setEstado(String estado) {
        _estado = estado;
    }

    public void setAssiduidade(String assiduidade) {
        _assiduidade = assiduidade;
    }

    public void setQuem(int quem) {
        _quem = quem;
    }

    public void setQuando(Timestamp quando) {
        _quando = quando;
    }

    /* teste da igualdade */
    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof StatusAssiduidade) {
            StatusAssiduidade status = (StatusAssiduidade) obj;

            resultado = (this.getCodigoInterno() == status.getCodigoInterno()
                    && this.getSigla() == status.getSigla()
                    && this.getDesignacao() == status.getDesignacao()
                    && this.getEstado() == status.getEstado() && this.getAssiduidade() == status
                    .getAssiduidade());
        }
        return resultado;
    }
}