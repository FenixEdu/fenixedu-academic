/*
 * TipoCurso.java
 *
 * Created on 20 de Dezembro de 2002, 14:12
 */

package Util;

import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author  Nuno Nunes & Joana Mota
 */
public class TipoCurso {
    public static final int LICENCIATURA = 1;
    public static final int MESTRADO = 2;
    public static final int MESTRADO_INTEGRADO = 3;
    public static final int ESPECIALIZACAO = 4;
    public static final int DOUTORAMENTO = 5;
    

    public static final String LICENCIATURA_STRING = "Licenciatura";
    public static final String MESTRADO_STRING = "Mestrado";
    public static final String MESTRADO_INTEGRADO_STRING = "Mestrado Integrado";
    public static final String ESPECIALIZACAO_STRING = "Especialização";
    public static final String DOUTORAMENTO_STRING = "Doutoramento";
	public static final String DEFAULT = "[Escolha uma Especialização]";	

    private Integer tipoCurso;    
    
    public TipoCurso() {
    }
    
    public TipoCurso(int tipoCurso) {
        this.tipoCurso = new Integer(tipoCurso);
    }

    public TipoCurso(Integer tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

	public TipoCurso(String tipoCurso){
		if (tipoCurso.equals(TipoCurso.LICENCIATURA_STRING)) this.tipoCurso = new Integer(TipoCurso.LICENCIATURA);
		if (tipoCurso.equals(TipoCurso.MESTRADO_STRING)) this.tipoCurso = new Integer(TipoCurso.MESTRADO);
		if (tipoCurso.equals(TipoCurso.MESTRADO_INTEGRADO_STRING)) this.tipoCurso = new Integer(TipoCurso.MESTRADO_INTEGRADO);
		if (tipoCurso.equals(TipoCurso.ESPECIALIZACAO_STRING)) this.tipoCurso = new Integer(TipoCurso.ESPECIALIZACAO);
		if (tipoCurso.equals(TipoCurso.DOUTORAMENTO_STRING)) this.tipoCurso = new Integer(TipoCurso.DOUTORAMENTO);
	}
    
    public static ArrayList toArrayList() {
		ArrayList result = new ArrayList();
		result.add(new LabelValueBean(TipoCurso.DEFAULT, null));
		result.add(new LabelValueBean(TipoCurso.LICENCIATURA_STRING, TipoCurso.LICENCIATURA_STRING));
		result.add(new LabelValueBean(TipoCurso.DOUTORAMENTO_STRING, TipoCurso.DOUTORAMENTO_STRING));
		result.add(new LabelValueBean(TipoCurso.MESTRADO_STRING, TipoCurso.MESTRADO_STRING));
		result.add(new LabelValueBean(TipoCurso.MESTRADO_INTEGRADO_STRING, TipoCurso.MESTRADO_INTEGRADO_STRING));
		result.add(new LabelValueBean(TipoCurso.ESPECIALIZACAO_STRING, TipoCurso.ESPECIALIZACAO_STRING));

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
            TipoCurso tipo = (TipoCurso)obj;
            resultado = (getTipoCurso() != null) && (getTipoCurso().equals(tipo.getTipoCurso()));
        }
        return resultado;
    }
    
    public String toString() {
    	int value = this.tipoCurso.intValue();
    	switch (value) {
    		case LICENCIATURA : return LICENCIATURA_STRING;
    		case MESTRADO : return MESTRADO_STRING;
    		case MESTRADO_INTEGRADO : return MESTRADO_INTEGRADO_STRING;
    		case ESPECIALIZACAO : return ESPECIALIZACAO_STRING;
    		case DOUTORAMENTO : return DOUTORAMENTO_STRING;
    	}
        return "Error: Invalid course type";
    }
    
}