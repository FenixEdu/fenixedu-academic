package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public class Feriado {
    private int _codigoInterno;

    private String _tipoFeriado;

    private String _descricao;

    private Date _data;

    private int _quem;

    private Timestamp _quando;

    // construtores
    public Feriado(int codigoInterno, String tipoFeriado, String descricao, Date data) {
        _codigoInterno = codigoInterno;
        _tipoFeriado = tipoFeriado;
        _descricao = descricao;
        _data = data;
        _quem = 0;
        _quando = null;
    }

    //construtores
    public Feriado(int codigoInterno, String tipoFeriado, String descricao, Date data, int quem,
            Timestamp quando) {
        _codigoInterno = codigoInterno;
        _tipoFeriado = tipoFeriado;
        _descricao = descricao;
        _data = data;
        _quem = quem;
        _quando = quando;
    }

    // construtores
    public Feriado(String tipoFeriado, String descricao, Date data) {
        _codigoInterno = 0;
        _tipoFeriado = tipoFeriado;
        _descricao = descricao;
        _data = data;
        _quem = 0;
        _quando = null;
    }

    //construtores
    public Feriado(String tipoFeriado, String descricao, Date data, int quem, Timestamp quando) {
        _codigoInterno = 0;
        _tipoFeriado = tipoFeriado;
        _descricao = descricao;
        _data = data;
        _quem = quem;
        _quando = quando;
    }

    public int getCodigoInterno() {
        return _codigoInterno;
    }

    public String getTipoFeriado() {
        return _tipoFeriado;
    }

    public String getDescricao() {
        return _descricao;
    }

    public Date getData() {
        return _data;
    }

    public int getQuem() {
        return _quem;
    }

    public Timestamp getQuando() {
        return _quando;
    }

    public void setCodigoInterno(int codigoInterno) {
        _codigoInterno = codigoInterno;
    }

    public void setTipoFeriado(String tipoFeriado) {
        _tipoFeriado = tipoFeriado;
    }

    public void setDescricao(String descricao) {
        _descricao = descricao;
    }

    public void setData(Date data) {
        _data = data;
    }

    public void setQuem(int quem) {
        _quem = quem;
    }

    public void setQuando(Timestamp quando) {
        _quando = quando;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof Feriado) {
            Feriado feriado = (Feriado) obj;

            resultado = ((this.getCodigoInterno() == feriado.getCodigoInterno())
                    && (this.getTipoFeriado() == feriado.getTipoFeriado())
                    && (this.getDescricao() == feriado.getDescricao()) && (this.getData() == feriado
                    .getData()));
        }
        return resultado;
    }
}