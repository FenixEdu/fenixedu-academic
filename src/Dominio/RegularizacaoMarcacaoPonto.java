package Dominio;

import java.sql.Timestamp;

/**
 * 
 * @author Fernanda Quitério e Tânia Pousão
 */
public class RegularizacaoMarcacaoPonto {
    private int _codigoInterno;

    private int _chaveMarcacaoPonto;

    private int _chaveParamRegularizacao;

    private int _quem;

    private Timestamp _quando;

    /* construtores */
    public RegularizacaoMarcacaoPonto(int chaveMarcacaoPonto) {
        _codigoInterno = 0;
        _chaveMarcacaoPonto = chaveMarcacaoPonto;
        _chaveParamRegularizacao = 0;
        _quem = 0;
        _quando = null;
    }

    public RegularizacaoMarcacaoPonto(int chaveMarcacaoPonto, int chaveParamRegularizacao) {
        _codigoInterno = 0;
        _chaveMarcacaoPonto = chaveMarcacaoPonto;
        _chaveParamRegularizacao = chaveParamRegularizacao;
        _quem = 0;
        _quando = null;
    }

    public RegularizacaoMarcacaoPonto(int codigoInterno, int chaveMarcacaoPonto,
            int chaveParamRegularizacao, int quem, Timestamp quando) {
        _codigoInterno = codigoInterno;
        _chaveMarcacaoPonto = chaveMarcacaoPonto;
        _chaveParamRegularizacao = chaveParamRegularizacao;
        _quem = quem;
        _quando = quando;
    }

    public int getCodigoInterno() {
        return _codigoInterno;
    }

    public int getChaveMarcacaoPonto() {
        return _chaveMarcacaoPonto;
    }

    public int getChaveParamRegularizacao() {
        return _chaveParamRegularizacao;
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

    public void setChaveMarcacaoPonto(int chaveMarcacaoPonto) {
        _chaveMarcacaoPonto = chaveMarcacaoPonto;
    }

    public void setChaveParamRegularizacao(int chaveParamRegularizacao) {
        _chaveParamRegularizacao = chaveParamRegularizacao;
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

        if (obj instanceof RegularizacaoMarcacaoPonto) {
            RegularizacaoMarcacaoPonto regularizacaoMarcacaoPonto = (RegularizacaoMarcacaoPonto) obj;

            resultado = ((this.getCodigoInterno() == regularizacaoMarcacaoPonto.getCodigoInterno())
                    && (this.getChaveMarcacaoPonto() == regularizacaoMarcacaoPonto
                            .getChaveMarcacaoPonto())
                    && (this.getChaveParamRegularizacao() == regularizacaoMarcacaoPonto
                            .getChaveParamRegularizacao())
                    && (this.getQuem() == regularizacaoMarcacaoPonto.getQuem()) && (this.getQuando() == regularizacaoMarcacaoPonto
                    .getQuando()));
        }
        return resultado;
    }
}