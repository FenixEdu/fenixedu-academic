package Dominio;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public class Justificacao {
    private int _codigoInterno;

    private int _chaveParamJustificacao;

    private int _chaveFuncionario;

    private java.util.Date _diaInicio;

    private Time _horaInicio;

    private java.util.Date _diaFim;

    private Time _horaFim;

    private String _observacao;

    private int _quem;

    private Timestamp _quando;

    /* Construtores */
    public Justificacao() {

        _codigoInterno = 0;
        _chaveParamJustificacao = 0;
        _chaveFuncionario = 0;
        _diaInicio = null;
        _horaInicio = null;
        _diaFim = null;
        _horaFim = null;
        _observacao = null;
        _quem = 0;
        _quando = null;
    }

    public Justificacao(int chaveParamJustificacao, int chaveFuncionario, java.util.Date diaInicio,
            Time horaInicio, Time horaFim, String observacao, int quem, Timestamp quando) {
        _codigoInterno = 0;
        _chaveParamJustificacao = chaveParamJustificacao;
        _chaveFuncionario = chaveFuncionario;
        _diaInicio = diaInicio;
        _horaInicio = horaInicio;
        _diaFim = diaInicio; // para o caso em que a justificacao é apenas de
        // horas no mesmo dia
        _horaFim = horaFim;
        _observacao = observacao;
        _quem = quem;
        _quando = quando;
    }

    public Justificacao(java.util.Date diaInicio, Time horaInicio, java.util.Date diaFim, Time horaFim,
            String observacao) {
        _codigoInterno = 0;
        _chaveParamJustificacao = 0;
        _chaveFuncionario = 0;
        _diaInicio = diaInicio;
        _horaInicio = horaInicio;
        _diaFim = diaFim;
        _horaFim = horaFim;
        _observacao = observacao;
        _quem = 0;
        _quando = null;
    }

    public Justificacao(int chaveParamJustificacao, java.util.Date diaInicio, Time horaInicio,
            java.util.Date diaFim, Time horaFim, String observacao) {
        _codigoInterno = 0;
        _chaveParamJustificacao = chaveParamJustificacao;
        _chaveFuncionario = 0;
        _diaInicio = diaInicio;
        _horaInicio = horaInicio;
        _diaFim = diaFim;
        _horaFim = horaFim;
        _observacao = observacao;
        _quem = 0;
        _quando = null;
    }

    public Justificacao(int chaveParamJustificacao, int chaveFuncionario, java.util.Date diaInicio,
            Time horaInicio, java.util.Date diaFim, Time horaFim, String observacao, int quem,
            Timestamp quando) {
        _codigoInterno = 0;
        _chaveParamJustificacao = chaveParamJustificacao;
        _chaveFuncionario = chaveFuncionario;
        _diaInicio = diaInicio;
        _horaInicio = horaInicio;
        _diaFim = diaFim;
        _horaFim = horaFim;
        _observacao = observacao;
        _quem = quem;
        _quando = quando;
    }

    public Justificacao(int codigoInterno, int chaveParamJustificacao, int chaveFuncionario,
            java.util.Date diaInicio, Time horaInicio, java.util.Date diaFim, Time horaFim,
            String observacao, int quem, Timestamp quando) {

        _codigoInterno = codigoInterno;
        _chaveParamJustificacao = chaveParamJustificacao;
        _chaveFuncionario = chaveFuncionario;
        _diaInicio = diaInicio;
        _horaInicio = horaInicio;
        _diaFim = diaFim;
        _horaFim = horaFim;
        _observacao = observacao;
        _quem = quem;
        _quando = quando;
    }

    /* Selectores */
    public int getCodigoInterno() {
        return _codigoInterno;
    }

    public int getChaveParamJustificacao() {
        return _chaveParamJustificacao;
    }

    public int getChaveFuncionario() {
        return _chaveFuncionario;
    }

    public java.util.Date getDiaInicio() {
        return _diaInicio;
    }

    public Time getHoraInicio() {
        return _horaInicio;
    }

    public java.util.Date getDiaFim() {
        return _diaFim;
    }

    public Time getHoraFim() {
        return _horaFim;
    }

    public String getObservacao() {
        return _observacao;
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

    public void setChaveParamJustificacao(int chaveParamJustificacao) {
        _chaveParamJustificacao = chaveParamJustificacao;
    }

    public void setChaveFuncionario(int chaveFuncionario) {
        _chaveFuncionario = chaveFuncionario;
    }

    public void setDiaInicio(java.util.Date diaInicio) {
        _diaInicio = diaInicio;
    }

    public void setHoraInicio(Time horaInicio) {
        _horaInicio = horaInicio;
    }

    public void setDiaFim(java.util.Date diaFim) {
        _diaFim = diaFim;
    }

    public void setHoraFim(Time horaFim) {
        _horaFim = horaFim;
    }

    public void setObservacao(String observacao) {
        _observacao = observacao;
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

        if (obj instanceof Justificacao) {
            Justificacao justificacao = (Justificacao) obj;

            resultado = ((this.getCodigoInterno() == justificacao.getCodigoInterno())
                    && (this.getChaveParamJustificacao() == justificacao.getChaveParamJustificacao())
                    && (this.getChaveFuncionario() == justificacao.getChaveFuncionario())
                    && (this.getDiaInicio() == justificacao.getDiaInicio())
                    && (this.getHoraInicio() == justificacao.getHoraInicio())
                    && (this.getDiaFim() == justificacao.getDiaFim())
                    && (this.getHoraFim() == justificacao.getHoraFim())
                    && (this.getObservacao() == justificacao.getObservacao())
                    && (this.getQuem() == justificacao.getQuem()) && (this.getQuando() == justificacao
                    .getQuando()));
        }
        return resultado;
    }
}