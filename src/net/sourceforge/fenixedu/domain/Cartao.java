package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public class Cartao {
    //TODO: a data fim do cartão de funcionário deveria ser null

    private int _codigoInterno;

    private int _numCartao;

    private int _chaveFuncionario;

    private Timestamp _dataInicio;

    private Timestamp _dataFim;

    private int _quem;

    private Timestamp _quando;

    private String _estado;

    /* Construtores */
    public Cartao() {

        _codigoInterno = 0;
        _numCartao = 0;
        _chaveFuncionario = 0;
        _dataInicio = null;
        _dataFim = null;
        _quem = 0;
        _quando = null;
        _estado = null;
    }

    public Cartao(int codigoInterno, int numCartao, int chaveFuncionario, Timestamp dataInicio,
            Timestamp dataFim, int quem, Timestamp quando, String estado) {

        _codigoInterno = codigoInterno;
        _numCartao = numCartao;
        _chaveFuncionario = chaveFuncionario;
        _dataInicio = dataInicio;
        _dataFim = dataFim;
        _quem = quem;
        _quando = quando;
        _estado = estado;
    }

    /*
     * construtor usado para a transferência entre o cartão da BD Oracle do
     * Teleponto e a BD do projecto Assiduidade
     */
    public Cartao(int numCartao, int chaveFuncionario, Timestamp dataInicio, Timestamp dataFim,
            int quem, Timestamp quando) {

        _codigoInterno = 0;
        _numCartao = numCartao;
        _chaveFuncionario = chaveFuncionario;
        _dataInicio = dataInicio;
        _dataFim = dataFim;
        _quem = quem;
        _quando = quando;

        if (_numCartao < 900000) {
            /* cartao de funcionario */
            _estado = "atribuido";
        } else {
            /* cartao substituto */
            _estado = "livre";
        }
    }

    /* Selectores */
    public int getCodigoInterno() {
        return _codigoInterno;
    }

    public int getNumCartao() {
        return _numCartao;
    }

    public int getChaveFuncionario() {
        return _chaveFuncionario;
    }

    public Timestamp getDataInicio() {
        return _dataInicio;
    }

    public Timestamp getDataFim() {
        return _dataFim;
    }

    public int getQuem() {
        return _quem;
    }

    public Timestamp getQuando() {
        return _quando;
    }

    public String getEstado() {
        return _estado;
    }

    /* Modificadores */
    public void setCodigoInterno(int codigoInterno) {
        _codigoInterno = codigoInterno;
    }

    public void setNumCartao(int numCartao) {
        _numCartao = numCartao;
    }

    public void setChaveFuncionario(int chaveFuncionario) {
        _chaveFuncionario = chaveFuncionario;
    }

    public void setDataInicio(Timestamp dataInicio) {
        _dataInicio = dataInicio;
    }

    public void setDataFim(Timestamp dataFim) {
        _dataFim = dataFim;
    }

    public void setQuem(int quem) {
        _quem = quem;
    }

    public void setQuando(Timestamp quando) {
        _quando = quando;
    }

    public void setEstado(String estado) {
        _estado = estado;
    }

    /* teste da igualdade */
    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof Cartao) {
            Cartao cartao = (Cartao) obj;

            resultado = ((this.getCodigoInterno() == cartao.getCodigoInterno())
                    && (this.getNumCartao() == cartao.getNumCartao())
                    && (this.getChaveFuncionario() == cartao.getChaveFuncionario())
                    && (this.getDataInicio() == cartao.getDataInicio())
                    && (this.getDataFim() == cartao.getDataFim())
                    && (this.getQuem() == cartao.getQuem()) && (this.getQuando() == cartao.getQuando()) && (this
                    .getEstado() == cartao.getEstado()));
        }
        return resultado;
    }
}