/*
 * TipoCurso.java
 *
 * Created on 20 de Dezembro de 2002, 14:12
 */

package net.sourceforge.fenixedu.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */
public class TipoCurso extends FenixUtil {
    public static final int LICENCIATURA = 1;

    public static final int MESTRADO = 2;

    public static final String LICENCIATURA_STRING = "Licenciatura";

    public static final String MESTRADO_STRING = "Mestrado";

    public static final String DEFAULT = "[Escolha uma Especialização]";

    public static final TipoCurso LICENCIATURA_OBJ = new TipoCurso(TipoCurso.LICENCIATURA);

    public static final TipoCurso MESTRADO_OBJ = new TipoCurso(TipoCurso.MESTRADO);

    private Integer tipoCurso;

    public TipoCurso() {
    }

    public TipoCurso(int tipoCurso) {
        this.tipoCurso = new Integer(tipoCurso);
    }

    public TipoCurso(Integer tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    public TipoCurso(String tipoCurso) {
        if (tipoCurso.equals(TipoCurso.LICENCIATURA_STRING))
            this.tipoCurso = new Integer(TipoCurso.LICENCIATURA);
        if (tipoCurso.equals(TipoCurso.MESTRADO_STRING))
            this.tipoCurso = new Integer(TipoCurso.MESTRADO);
    }

    public static List toArrayList() {
        List result = new ArrayList();
        result.add(new LabelValueBean(TipoCurso.DEFAULT, null));
        result.add(new LabelValueBean(TipoCurso.LICENCIATURA_STRING, TipoCurso.LICENCIATURA_STRING));
        result.add(new LabelValueBean(TipoCurso.MESTRADO_STRING, TipoCurso.MESTRADO_STRING));
        return result;
    }

    public static List toLabelValueBeanList() {
        List result = new ArrayList();
        result.add(new LabelValueBean(TipoCurso.LICENCIATURA_STRING, String
                .valueOf(TipoCurso.LICENCIATURA)));
        result.add(new LabelValueBean(TipoCurso.MESTRADO_STRING, String.valueOf(TipoCurso.MESTRADO)));
        return result;
    }

    public Integer getTipoCurso() {
        return this.tipoCurso;
    }

    public void setTipoCurso(Integer tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof TipoCurso) {
            TipoCurso tipo = (TipoCurso) obj;
            resultado = (getTipoCurso() != null) && (getTipoCurso().equals(tipo.getTipoCurso()));
        }
        return resultado;
    }

    public String toString() {
        int value = this.tipoCurso.intValue();
        switch (value) {
        case LICENCIATURA:
            return LICENCIATURA_STRING;
        case MESTRADO:
            return MESTRADO_STRING;
        }
        return "Error: Invalid course type";
    }

}