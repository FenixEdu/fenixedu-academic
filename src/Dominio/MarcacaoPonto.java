package Dominio;

import java.sql.Timestamp;

/**
 * 
 * @author Fernanda Quitério e Tânia Pousão
 */
public class MarcacaoPonto {
    private int _codigoInterno;

    private int _unidade;

    private String _siglaUnidade;

    private Timestamp _data;

    private int _numCartao;

    private int _numFuncionario; //ATENÇÃO: Devia ser chave do funcionário e

    // não número mecanográfico

    private String _estado;

    /*
     * public MarcacaoPonto() { _codigoInterno = 0; _unidade = 0; _siglaUnidade =
     * ""; _data = null; _horas = null; _numCartao = 0; _numFuncionario = 0;
     * _estado = ""; }
     */
    public MarcacaoPonto(int unidade, Timestamp data, int numCartao) {
        _codigoInterno = 0;
        _unidade = unidade;
        _siglaUnidade = "";
        _data = data;
        _numCartao = numCartao;
        _numFuncionario = 0;
        _estado = "valida";
    }

    public MarcacaoPonto(int unidade, Timestamp data, int numCartao, int numFuncionario) {
        _codigoInterno = 0;
        _unidade = unidade;
        _siglaUnidade = "";
        _data = data;
        _numCartao = numCartao;
        _numFuncionario = numFuncionario;
        _estado = "valida";
    }

    public MarcacaoPonto(int unidade, Timestamp data, int numCartao, String estado) {
        _codigoInterno = 0;
        _unidade = unidade;
        _siglaUnidade = "";
        _data = data;
        _numCartao = numCartao;
        _numFuncionario = 0;
        _estado = estado;
    }

    public MarcacaoPonto(int unidade, Timestamp data, int numCartao, int numFuncionario, String estado) {
        _codigoInterno = 0;
        _unidade = unidade;
        _siglaUnidade = "";
        _data = data;
        _numCartao = numCartao;
        _numFuncionario = numFuncionario;
        _estado = estado;
    }

    public MarcacaoPonto(int unidade, String sigla, Timestamp data, int numCartao, int numFuncionario,
            String estado) {
        _codigoInterno = 0;
        _unidade = unidade;
        _siglaUnidade = sigla;
        _data = data;
        _numCartao = numCartao;
        _numFuncionario = numFuncionario;
        _estado = estado;
    }

    public MarcacaoPonto(int codigoInterno, int unidade, String sigla, Timestamp data, int numCartao,
            int numFuncionario, String estado) {
        _codigoInterno = codigoInterno;
        _unidade = unidade;
        _siglaUnidade = sigla;
        _data = data;
        _numCartao = numCartao;
        _numFuncionario = numFuncionario;
        _estado = estado;
    }

    /*
     * construtor usado para a transferência entre o cartão da BD Oracle do
     * Teleponto e a BD do projecto Assiduidade
     */
    public MarcacaoPonto(int codigoInterno, String sigla, Timestamp data, int numCartao,
            int numFuncionario) {
        _codigoInterno = codigoInterno;
        _unidade = 0;
        _siglaUnidade = sigla;
        _data = data;
        _numCartao = numCartao;
        _numFuncionario = numFuncionario;
        _estado = "";
    }

    public MarcacaoPonto(int codigoInterno, int unidade, String sigla, Timestamp data, int numCartao,
            int numFuncionario) {
        _codigoInterno = codigoInterno;
        _unidade = unidade;
        _siglaUnidade = sigla;
        _data = data;
        _numCartao = numCartao;
        _numFuncionario = numFuncionario;
        _estado = "valida";
    }

    public int getCodigoInterno() {
        return _codigoInterno;
    }

    public int getUnidade() {
        return _unidade;
    }

    public String getSiglaUnidade() {
        return _siglaUnidade;
    }

    public Timestamp getData() {
        return _data;
    }

    public int getNumCartao() {
        return _numCartao;
    }

    public int getNumFuncionario() {
        return _numFuncionario;
    }

    public String getEstado() {
        return _estado;
    }

    public void setCodigoInterno(int codigoInterno) {
        _codigoInterno = codigoInterno;
    }

    public void setUnidade(int unidade) {
        _unidade = unidade;
    }

    public void setSiglaUnidade(String siglaUnidade) {
        _siglaUnidade = siglaUnidade;
    }

    public void setData(Timestamp data) {
        _data = data;
    }

    public void setNumCartao(int numCartao) {
        _numCartao = numCartao;
    }

    public void setNumFuncionario(int numFuncionario) {
        _numFuncionario = numFuncionario;
    }

    public void setEstado(String estado) {
        _estado = estado;
    }

    /* teste da igualdade */
    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof MarcacaoPonto) {
            MarcacaoPonto marcacaoPonto = (MarcacaoPonto) obj;

            resultado = ((this.getCodigoInterno() == marcacaoPonto.getCodigoInterno())
                    && (this.getUnidade() == marcacaoPonto.getUnidade())
                    && (this.getSiglaUnidade() == marcacaoPonto.getSiglaUnidade())
                    && (this.getData() == marcacaoPonto.getData())
                    && (this.getNumCartao() == marcacaoPonto.getNumCartao())
                    && (this.getNumFuncionario() == marcacaoPonto.getNumFuncionario()) && (this
                    .getEstado() == marcacaoPonto.getEstado()));
        }
        return resultado;
    }

    public String toString() {
        return "\n[Marcacao Ponto - " + "\ncodigoInterno=" + _codigoInterno + "\nunidade=" + _unidade
                + "\nsiglaUnidade=" + _siglaUnidade + "\ndata=" + _data + "\ncartao=" + _numCartao
                + "\nfuncionario=" + _numFuncionario + "\nestado=" + _estado + "]";
    }
}