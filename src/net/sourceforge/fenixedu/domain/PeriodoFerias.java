package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class PeriodoFerias {
    private int _codigoInterno;

    private int _chaveFuncionario;

    private Date _dataInicio;

    private Date _dataFim;

    private int _numDiasUteis;

    private int _tipoFerias;

    private int _quem;

    private Timestamp _quando;

    public PeriodoFerias() {
        _codigoInterno = 0;
        _chaveFuncionario = 0;
        _dataInicio = null;
        _dataFim = null;
        _numDiasUteis = 0;
        _tipoFerias = 0;

        _quem = 0;
        _quando = null;
    }

    public PeriodoFerias(int codigoInterno, int chaveFuncionario, Date dataInicio, Date dataFim,
            int numDiasUteis, int tipoFerias, int quem, Timestamp quando) {
        _codigoInterno = codigoInterno;
        _chaveFuncionario = chaveFuncionario;
        _dataInicio = dataInicio;
        _dataFim = dataFim;
        _numDiasUteis = numDiasUteis;
        _tipoFerias = tipoFerias;

        _quem = quem;
        _quando = quando;
    }

    public int getCodigoInterno() {
        return _codigoInterno;
    }

    public int getChaveFuncionario() {
        return _chaveFuncionario;
    }

    public Date getDataInicio() {
        return _dataInicio;
    }

    public Date getDataFim() {
        return _dataFim;
    }

    public int getNumDiasUteis() {
        return _numDiasUteis;
    }

    public int getTipoFerias() {
        return _tipoFerias;
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

    public void setChaveFuncionario(int chaveFuncionario) {
        _chaveFuncionario = chaveFuncionario;
    }

    public void setDataInicio(Date dataInicio) {
        _dataInicio = dataInicio;
    }

    public void setDataFim(Date dataFim) {
        _dataFim = dataFim;
    }

    public void setNumDiasUteis(int numDiasUteis) {
        _numDiasUteis = numDiasUteis;
    }

    public void setTipoFerias(int tipoFerias) {
        _tipoFerias = tipoFerias;
    }

    public void setquemQuem(int quem) {
        _quem = quem;
    }

    public void getQuando(Timestamp quando) {
        _quando = quando;
    }
}