package Dominio;

import java.sql.Timestamp;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public class ParamJustificacao {
    private int codigoInterno;

    private String sigla;

    private String descricao;

    private String tipo;

    private String tipoDias;

    private String grupo;

    private int quem;

    private Timestamp quando;

    /* Construtores */
    public ParamJustificacao(String sigla, String descricao, String tipo, String grupo, int quem,
            Timestamp quando) {
        this.codigoInterno = 0;
        this.sigla = sigla;
        this.descricao = descricao;
        this.tipo = tipo;
        this.grupo = grupo;
        this.quem = quem;
        this.quando = quando;
    }

    public ParamJustificacao(String sigla, String descricao, String tipo, String tipoDias, String grupo,
            int quem, Timestamp quando) {
        this.codigoInterno = 0;
        this.sigla = sigla;
        this.descricao = descricao;
        this.tipo = tipo;
        this.tipoDias = tipoDias;
        this.grupo = grupo;
        this.quem = quem;
        this.quando = quando;
    }

    public ParamJustificacao(int codigoInterno, String sigla, String descricao, String tipo,
            String grupo, int quem, Timestamp quando) {
        this.codigoInterno = codigoInterno;
        this.sigla = sigla;
        this.descricao = descricao;
        this.tipo = tipo;
        this.grupo = grupo;
        this.quem = quem;
        this.quando = quando;
    }

    public ParamJustificacao(int codigoInterno, String sigla, String descricao, String tipo,
            String tipoDias, String grupo, int quem, Timestamp quando) {
        this.codigoInterno = codigoInterno;
        this.sigla = sigla;
        this.descricao = descricao;
        this.tipo = tipo;
        this.tipoDias = tipoDias;
        this.grupo = grupo;
        this.quem = quem;
        this.quando = quando;
    }

    /* teste da igualdade */
    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof ParamJustificacao) {
            ParamJustificacao paramJustificacao = (ParamJustificacao) obj;

            resultado = ((this.getCodigoInterno() == paramJustificacao.getCodigoInterno())
                    && (this.getSigla() == paramJustificacao.getSigla())
                    && (this.getDescricao() == paramJustificacao.getDescricao())
                    && (this.getTipo() == paramJustificacao.getTipo())
                    && (this.getTipoDias() == paramJustificacao.getTipoDias())
                    && (this.getGrupo() == paramJustificacao.getGrupo())
                    && (this.getQuem() == paramJustificacao.getQuem()) && (this.getQuando() == paramJustificacao
                    .getQuando()));
        }
        return resultado;
    }

    /**
     * @return int
     */
    public int getCodigoInterno() {
        return codigoInterno;
    }

    /**
     * @return String
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @return String
     */
    public String getGrupo() {
        return grupo;
    }

    /**
     * @return Timestamp
     */
    public Timestamp getQuando() {
        return quando;
    }

    /**
     * @return int
     */
    public int getQuem() {
        return quem;
    }

    /**
     * @return String
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * @return String
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @return String
     */
    public String getTipoDias() {
        return tipoDias;
    }

    /**
     * Sets the codigoInterno.
     * 
     * @param codigoInterno
     *            The codigoInterno to set
     */
    public void setCodigoInterno(int codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    /**
     * Sets the descricao.
     * 
     * @param descricao
     *            The descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Sets the grupo.
     * 
     * @param grupo
     *            The grupo to set
     */
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    /**
     * Sets the quando.
     * 
     * @param quando
     *            The quando to set
     */
    public void setQuando(Timestamp quando) {
        this.quando = quando;
    }

    /**
     * Sets the quem.
     * 
     * @param quem
     *            The quem to set
     */
    public void setQuem(int quem) {
        this.quem = quem;
    }

    /**
     * Sets the sigla.
     * 
     * @param sigla
     *            The sigla to set
     */
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    /**
     * Sets the tipo.
     * 
     * @param tipo
     *            The tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Sets the tipoDias.
     * 
     * @param tipoDias
     *            The tipoDias to set
     */
    public void setTipoDias(String tipoDias) {
        this.tipoDias = tipoDias;
    }

}