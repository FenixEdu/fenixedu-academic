/*
 * TipoDocumentoIdentificacao.java
 *
 * Created on 12 de Novembro de 2002, 17:27
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */
package Util;

import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

public class TipoDocumentoIdentificacao extends FenixUtil {

    public static final int BILHETE_DE_IDENTIDADE = 1;

    public static final int PASSAPORTE = 2;

    public static final int BILHETE_DE_IDENTIDADE_DE_CIDADAO_ESTRANGEIRO = 3;

    public static final int BILHETE_DE_IDENTIDADE_DO_PAIS_DE_ORIGEM = 4;

    public static final int BILHETE_DE_IDENTIDADE_DA_MARINHA = 5;

    public static final int BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA = 6;

    public static final int OUTRO = 7;

    public static final int EXTERNO = 8;

    public static final String BILHETE_DE_IDENTIDADE_STRING = "Bilhete de Identidade";

    public static final String PASSAPORTE_STRING = "Passaporte";

    public static final String BILHETE_DE_IDENTIDADE_DE_CIDADAO_ESTRANGEIRO_STRING = "Bilhete de Identidade de Cidadão Estrangeiro";

    public static final String BILHETE_DE_IDENTIDADE_DO_PAIS_DE_ORIGEM_STRING = "Bilhete de Identidade do País de Origem";

    public static final String BILHETE_DE_IDENTIDADE_DA_MARINHA_STRING = "Bilhete de Identidade da Marinha";

    public static final String BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA_STRING = "Bilhete de Identidade da Força Aérea";

    public static final String OUTRO_STRING = "Outro";

    public static final String EXTERNO_STRING = "Externo";

    public static final String DEFAULT = "[Escolha um Tipo]";

    private Integer tipo;

    /** Creates a new instance of TipoDocumentoIdentificacao */
    public TipoDocumentoIdentificacao() {
    }

    public TipoDocumentoIdentificacao(int tipo) {
        this.tipo = new Integer(tipo);
    }

    public TipoDocumentoIdentificacao(Integer tipo) {
        this.tipo = tipo;
    }

    public TipoDocumentoIdentificacao(String tipo) {
        if (tipo
                .equals(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_STRING))
            this.tipo = new Integer(
                    TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);
        if (tipo.equals(TipoDocumentoIdentificacao.PASSAPORTE_STRING))
            this.tipo = new Integer(TipoDocumentoIdentificacao.PASSAPORTE);
        if (tipo
                .equals(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DE_CIDADAO_ESTRANGEIRO_STRING))
            this.tipo = new Integer(
                    TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DE_CIDADAO_ESTRANGEIRO);
        if (tipo
                .equals(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DO_PAIS_DE_ORIGEM_STRING))
            this.tipo = new Integer(
                    TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DO_PAIS_DE_ORIGEM);
        if (tipo
                .equals(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_MARINHA_STRING))
            this.tipo = new Integer(
                    TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_MARINHA);
        if (tipo
                .equals(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA_STRING))
            this.tipo = new Integer(
                    TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA);
        if (tipo.equals(TipoDocumentoIdentificacao.OUTRO_STRING))
            this.tipo = new Integer(TipoDocumentoIdentificacao.OUTRO);
        if (tipo.equals(TipoDocumentoIdentificacao.EXTERNO_STRING))
            this.tipo = new Integer(TipoDocumentoIdentificacao.EXTERNO);

    }

    public static ArrayList toArrayList() {
        ArrayList result = new ArrayList();
        result
                .add(new LabelValueBean(TipoDocumentoIdentificacao.DEFAULT,
                        null));
        result.add(new LabelValueBean(
                TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_STRING,
                TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_STRING));
        result.add(new LabelValueBean(
                TipoDocumentoIdentificacao.PASSAPORTE_STRING,
                TipoDocumentoIdentificacao.PASSAPORTE_STRING));
        result
                .add(new LabelValueBean(
                        TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DE_CIDADAO_ESTRANGEIRO_STRING,
                        TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DE_CIDADAO_ESTRANGEIRO_STRING));
        result
                .add(new LabelValueBean(
                        TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DO_PAIS_DE_ORIGEM_STRING,
                        TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DO_PAIS_DE_ORIGEM_STRING));
        result
                .add(new LabelValueBean(
                        TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_MARINHA_STRING,
                        TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_MARINHA_STRING));
        result
                .add(new LabelValueBean(
                        TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA_STRING,
                        TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA_STRING));
        result.add(new LabelValueBean(TipoDocumentoIdentificacao.OUTRO_STRING,
                TipoDocumentoIdentificacao.OUTRO_STRING));
        result.add(new LabelValueBean(
                TipoDocumentoIdentificacao.EXTERNO_STRING,
                TipoDocumentoIdentificacao.EXTERNO_STRING));

        return result;
    }

    public static ArrayList toIntegerArrayList() {
        ArrayList result = new ArrayList();
        result
                .add(new LabelValueBean(TipoDocumentoIdentificacao.DEFAULT,
                        null));
        result
                .add(new LabelValueBean(
                        TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_STRING,
                        String
                                .valueOf(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE)));
        result.add(new LabelValueBean(
                TipoDocumentoIdentificacao.PASSAPORTE_STRING, String
                        .valueOf(TipoDocumentoIdentificacao.PASSAPORTE)));
        result
                .add(new LabelValueBean(
                        TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DE_CIDADAO_ESTRANGEIRO_STRING,
                        String
                                .valueOf(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DE_CIDADAO_ESTRANGEIRO)));
        result
                .add(new LabelValueBean(
                        TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DO_PAIS_DE_ORIGEM_STRING,
                        String
                                .valueOf(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DO_PAIS_DE_ORIGEM)));
        result
                .add(new LabelValueBean(
                        TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_MARINHA_STRING,
                        String
                                .valueOf(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_MARINHA)));
        result
                .add(new LabelValueBean(
                        TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA_STRING,
                        String
                                .valueOf(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA)));
        result.add(new LabelValueBean(TipoDocumentoIdentificacao.OUTRO_STRING,
                String.valueOf(TipoDocumentoIdentificacao.OUTRO)));
        result.add(new LabelValueBean(
                TipoDocumentoIdentificacao.EXTERNO_STRING, String
                        .valueOf(TipoDocumentoIdentificacao.EXTERNO)));
        return result;
    }

    public boolean equals(Object o) {
        if (o instanceof TipoDocumentoIdentificacao) {
            TipoDocumentoIdentificacao aux = (TipoDocumentoIdentificacao) o;
            return this.tipo.equals(aux.getTipo());
        }

        return false;

    }

    public String toString() {
        if (tipo.intValue() == TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE)
            return TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_STRING;
        if (tipo.intValue() == TipoDocumentoIdentificacao.PASSAPORTE)
            return TipoDocumentoIdentificacao.PASSAPORTE_STRING;
        if (tipo.intValue() == TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DE_CIDADAO_ESTRANGEIRO)
            return TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DE_CIDADAO_ESTRANGEIRO_STRING;
        if (tipo.intValue() == TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DO_PAIS_DE_ORIGEM)
            return TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DO_PAIS_DE_ORIGEM_STRING;
        if (tipo.intValue() == TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_MARINHA)
            return TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_MARINHA_STRING;
        if (tipo.intValue() == TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA)
            return TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA_STRING;
        if (tipo.intValue() == TipoDocumentoIdentificacao.OUTRO)
            return TipoDocumentoIdentificacao.OUTRO_STRING;
        if (tipo.intValue() == TipoDocumentoIdentificacao.EXTERNO)
            return TipoDocumentoIdentificacao.EXTERNO_STRING;

        return "ERRO!"; // Nunca e atingido
    }

    /**
     * Getter for property tipo.
     * 
     * @return Value of property tipo.
     *  
     */
    public java.lang.Integer getTipo() {
        return tipo;
    }

    /**
     * Setter for property tipo.
     * 
     * @param tipo
     *            New value of property tipo.
     *  
     */
    public void setTipo(java.lang.Integer tipo) {
        this.tipo = tipo;
    }

}