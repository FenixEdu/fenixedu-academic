package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public class ParamRegularizacao {
    private int _codigoInterno;

    private String _sigla;

    private String _descricao;

    private int _quem;

    private Timestamp _quando;

    /* Construtores */
    public ParamRegularizacao(String sigla, String descricao, int quem, Timestamp quando) {
        _codigoInterno = 0;
        _sigla = sigla;
        _descricao = descricao;
        _quem = quem;
        _quando = quando;
    }

    public ParamRegularizacao(int codigoInterno, String sigla, String descricao, int quem,
            Timestamp quando) {
        _codigoInterno = codigoInterno;
        _sigla = sigla;
        _descricao = descricao;
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

    public String getDescricao() {
        return _descricao;
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

    public void setDescricao(String descricao) {
        _descricao = descricao;
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

        if (obj instanceof ParamRegularizacao) {
            ParamRegularizacao paramRegularizacao = (ParamRegularizacao) obj;

            resultado = ((this.getCodigoInterno() == paramRegularizacao.getCodigoInterno())
                    && (this.getSigla() == paramRegularizacao.getSigla())
                    && (this.getDescricao() == paramRegularizacao.getDescricao())
                    && (this.getQuem() == paramRegularizacao.getQuem()) && (this.getQuando() == paramRegularizacao
                    .getQuando()));
        }
        return resultado;
    }
}