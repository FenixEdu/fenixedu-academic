package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class Ferias {
    private int _codigoInterno;

    private int _chaveFuncionario;

    private int _anoCorrente;

    private int _diasNormais;

    private int _diasEpocaBaixa;

    private int _diasHorasExtras;

    private int _diasAntiguidade;

    private int _diasMeioDia;

    private int _diasDispensaServico;

    private int _diasTolerancia;

    private int _diasTransferidos;

    private int _diasTransHorasExtras;

    private int _diasTransAntiguidade;

    public Ferias() {
        _codigoInterno = 0;
        _chaveFuncionario = 0;
        _anoCorrente = 0;
        _diasNormais = 0;
        _diasEpocaBaixa = 0;
        _diasHorasExtras = 0;
        _diasAntiguidade = 0;
        _diasMeioDia = 0;
        _diasDispensaServico = 0;
        _diasTolerancia = 0;
        _diasTransferidos = 0;
        _diasTransHorasExtras = 0;
        _diasTransAntiguidade = 0;
    }

    public Ferias(int codigoInterno, int chaveFuncionario, int anoCorrente, int diasNormais,
            int diasEpocaBaixa, int diasHorasExtras, int diasAntiguidade, int diasMeioDia,
            int diasDispensaServico, int diasTolerancia, int diasTransferidos, int diasTransHorasExtras,
            int diasTransAntiguidade) {
        _codigoInterno = codigoInterno;
        _chaveFuncionario = chaveFuncionario;
        _anoCorrente = anoCorrente;
        _diasNormais = diasNormais;
        _diasEpocaBaixa = diasEpocaBaixa;
        _diasHorasExtras = diasHorasExtras;
        _diasAntiguidade = diasAntiguidade;
        _diasMeioDia = diasMeioDia;
        _diasDispensaServico = diasDispensaServico;
        _diasTolerancia = diasTolerancia;
        _diasTransferidos = diasTransferidos;
        _diasTransHorasExtras = diasTransHorasExtras;
        _diasTransAntiguidade = diasTransAntiguidade;
    }

    public int getCodigoInterno() {
        return _codigoInterno;
    }

    public int getChaveFuncionario() {
        return _chaveFuncionario;
    }

    public int getAnoCorrente() {
        return _anoCorrente;
    }

    public int getDiasNormais() {
        return _diasNormais;
    }

    public int getDiasEpocaBaixa() {
        return _diasEpocaBaixa;
    }

    public int getDiasHorasExtras() {
        return _diasHorasExtras;
    }

    public int getDiasAntiguidade() {
        return _diasAntiguidade;
    }

    public int getDiasMeioDia() {
        return _diasMeioDia;
    }

    public int getDiasDispensaServico() {
        return _diasDispensaServico;
    }

    public int getDiasTolerancia() {
        return _diasTolerancia;
    }

    public int getDiasTransferidos() {
        return _diasTransferidos;
    }

    public int getDiasTransHorasExtras() {
        return _diasTransHorasExtras;
    }

    public int getDiasTransAntiguidade() {
        return _diasTransAntiguidade;
    }

    public void setCodigoInterno(int codigoInterno) {
        _codigoInterno = codigoInterno;
    }

    public void setChaveFuncionario(int chaveFuncionario) {
        _chaveFuncionario = chaveFuncionario;
    }

    public void setAnoCorrente(int anoCorrente) {
        _anoCorrente = anoCorrente;
    }

    public void setDiasNormais(int diasNormais) {
        _diasNormais = diasNormais;
    }

    public void setDiasEpocaBaixa(int diasEpocaBaixa) {
        _diasEpocaBaixa = diasEpocaBaixa;
    }

    public void setDiasHorasExtras(int diasHorasExtras) {
        _diasHorasExtras = diasHorasExtras;
    }

    public void setDiasAntiguidade(int diasAntiguidade) {
        _diasAntiguidade = diasAntiguidade;
    }

    public void setDiasMeioDia(int diasMeioDia) {
        _diasMeioDia = diasMeioDia;
    }

    public void setDiasDispensaServico(int diasDispensaServico) {
        _diasDispensaServico = diasDispensaServico;
    }

    public void setDiasTolerancia(int diasTolerancia) {
        _diasTolerancia = diasTolerancia;
    }

    public void setDiasTransferidos(int diasTransferidos) {
        _diasTransferidos = diasTransferidos;
    }

    public void setDiasTransHorasExtras(int diasTransHorasExtras) {
        _diasTransHorasExtras = diasTransHorasExtras;
    }

    public void setDiasTransAntiguidade(int diasTransAntiguidade) {
        _diasTransAntiguidade = diasTransAntiguidade;
    }
}