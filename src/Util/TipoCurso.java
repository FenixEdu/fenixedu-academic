/*
 * TipoCurso.java
 *
 * Created on 20 de Dezembro de 2002, 14:12
 */

package Util;

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
	

    private Integer tipoCurso;    
    
    public TipoCurso() {
    }
    
    public TipoCurso(int tipoCurso) {
        this.tipoCurso = new Integer(tipoCurso);
    }

    public TipoCurso(Integer tipoCurso) {
        this.tipoCurso = tipoCurso;
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
            resultado = getTipoCurso().equals(tipo.getTipoCurso());
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