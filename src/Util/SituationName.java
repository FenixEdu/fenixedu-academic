/*
 * Situacao.java
 *
 * Created on 19 de Novembro de 2002, 17:14
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package Util;

import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

public class SituationName {
 
    public static final int PENDENTE = 1;
    public static final int ADMITIDO = 2;
    public static final int SUPLENTE = 3;
    public static final int NAO_ACEITE = 4;
    public static final int DESISTIU = 5;
    public static final int SUPRA_NUMERARIO = 6;
    public static final int EXTRAORDINARIO = 7;
    public static final int DOCENTE_ENSINO_SUPERIOR = 8;
    
    public static final String PENDENTE_STRING = "Pendente";
    public static final String ADMITIDO_STRING = "Admitido";
    public static final String SUPLENTE_STRING = "Suplente";
    public static final String NAO_ACEITE_STRING = "Não Aceite";
    public static final String DESISTIU_STRING = "Desistiu";
    public static final String SUPRA_NUMERARIO_STRING = "Supra Numerário";
    public static final String EXTRAORDINARIO_STRING = "Extraordinário";
    public static final String DOCENTE_ENSINO_SUPERIOR_STRING = "Docento do Ensino Superior";
    
    
    
    private Integer situationName;

    /** Creates a new instance of SituationName */
    public SituationName() {
    }
    
    public SituationName(int nomeSituacao) {
        this.situationName = new Integer(nomeSituacao);
    }

    public SituationName(Integer nomeSituacao) {
        this.situationName = nomeSituacao;
    }


    public boolean equals(Object o) {
        if(o instanceof SituationName) {
            SituationName aux = (SituationName) o;
            return this.situationName.equals(aux.getSituationName());
        }
        else {
            return false;
        }
    }
    
    public SituationName(String nomeSituacao) {
    	if (nomeSituacao.equals(SituationName.PENDENTE_STRING)) this.situationName = new Integer(SituationName.PENDENTE);
    	if (nomeSituacao.equals(SituationName.ADMITIDO_STRING)) this.situationName = new Integer(SituationName.ADMITIDO);
    	if (nomeSituacao.equals(SituationName.SUPLENTE_STRING)) this.situationName = new Integer(SituationName.SUPLENTE);
    	if (nomeSituacao.equals(SituationName.NAO_ACEITE_STRING)) this.situationName = new Integer(SituationName.NAO_ACEITE);
    	if (nomeSituacao.equals(SituationName.DESISTIU_STRING)) this.situationName = new Integer(SituationName.DESISTIU);
    	if (nomeSituacao.equals(SituationName.SUPRA_NUMERARIO_STRING)) this.situationName = new Integer(SituationName.SUPRA_NUMERARIO);
    	if (nomeSituacao.equals(SituationName.EXTRAORDINARIO_STRING)) this.situationName = new Integer(SituationName.EXTRAORDINARIO);
    	if (nomeSituacao.equals(SituationName.DOCENTE_ENSINO_SUPERIOR_STRING)) this.situationName = new Integer(SituationName.DOCENTE_ENSINO_SUPERIOR);
    }
    
    public String toString() {
		if (situationName.intValue()== SituationName.PENDENTE) return SituationName.PENDENTE_STRING;
		if (situationName.intValue()== SituationName.ADMITIDO) return SituationName.ADMITIDO_STRING;
		if (situationName.intValue()== SituationName.SUPLENTE) return SituationName.SUPLENTE_STRING;
		if (situationName.intValue()== SituationName.NAO_ACEITE) return SituationName.NAO_ACEITE_STRING;
		if (situationName.intValue()== SituationName.DESISTIU) return SituationName.DESISTIU_STRING;
		if (situationName.intValue()== SituationName.SUPRA_NUMERARIO) return SituationName.SUPRA_NUMERARIO_STRING;
		if (situationName.intValue()== SituationName.EXTRAORDINARIO) return SituationName.EXTRAORDINARIO_STRING;
		if (situationName.intValue()== SituationName.DOCENTE_ENSINO_SUPERIOR) return SituationName.DOCENTE_ENSINO_SUPERIOR_STRING;
	    return "ERRO!"; // Nunca e atingido
    }
    
    public ArrayList toArrayList() {
		ArrayList result = new ArrayList();
	    result.add(new LabelValueBean(SituationName.PENDENTE_STRING , SituationName.PENDENTE_STRING));
		result.add(new LabelValueBean(SituationName.ADMITIDO_STRING , SituationName.ADMITIDO_STRING));
		result.add(new LabelValueBean(SituationName.SUPLENTE_STRING , SituationName.SUPLENTE_STRING));
		result.add(new LabelValueBean(SituationName.NAO_ACEITE_STRING , SituationName.NAO_ACEITE_STRING));
		result.add(new LabelValueBean(SituationName.DESISTIU_STRING , SituationName.DESISTIU_STRING));
		result.add(new LabelValueBean(SituationName.SUPRA_NUMERARIO_STRING , SituationName.SUPRA_NUMERARIO_STRING));
		result.add(new LabelValueBean(SituationName.EXTRAORDINARIO_STRING , SituationName.EXTRAORDINARIO_STRING));
		result.add(new LabelValueBean(SituationName.DOCENTE_ENSINO_SUPERIOR_STRING , SituationName.DOCENTE_ENSINO_SUPERIOR_STRING));
		return result;	
    }


	/**
	 * Returns the situationName.
	 * @return Integer
	 */
	public Integer getSituationName() {
		return situationName;
	}

	/**
	 * Sets the situationName.
	 * @param situationName The situationName to set
	 */
	public void setSituationName(Integer nomeSituacao) {
		this.situationName = nomeSituacao;
	}

}
