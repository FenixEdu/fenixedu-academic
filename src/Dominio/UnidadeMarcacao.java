package Dominio;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */

public class UnidadeMarcacao {
    private int _codigoInterno;

    private String _sigla;

    private String _descricao;

    private int _id;

    public UnidadeMarcacao(int codigoInterno, String sigla, String descricao, int id) {
        _codigoInterno = codigoInterno;
        _sigla = sigla;
        _descricao = descricao;
        _id = id;
    }

    public UnidadeMarcacao(String sigla, String descricao, int id) {
        _codigoInterno = 0;
        _sigla = sigla;
        _descricao = descricao;
        _id = id;
    }

    public int getCodigoInterno() {
        return _codigoInterno;
    }

    public String getSigla() {
        return _sigla;
    }

    public String getDescricao() {
        return _descricao;
    }

    public int getID() {
        return _id;
    }

    public void setCodigoInterno(int codigoInterno) {
        _codigoInterno = codigoInterno;
    }

    public void setSigla(String sigla) {
        _sigla = sigla;
    }

    public void setDescricao(String descricao) {
        _descricao = descricao;
    }

    public void setID(int id) {
        _id = id;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof Regime) {
            UnidadeMarcacao unidadeMarcacao = (UnidadeMarcacao) obj;

            resultado = (this.getCodigoInterno() == unidadeMarcacao.getCodigoInterno()
                    && this.getSigla() == unidadeMarcacao.getSigla()
                    && this.getDescricao() == unidadeMarcacao.getDescricao() && this.getID() == unidadeMarcacao
                    .getID());
        }
        return resultado;
    }
}