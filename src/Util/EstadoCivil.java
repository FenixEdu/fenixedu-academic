/*
 * EstadoCivil.java
 *
 * Created on 14 de Novembro de 2002, 10:08
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package Util;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

public class EstadoCivil implements Serializable {

    public static final int SOLTEIRO = 1;
    public static final int CASADO = 2;
    public static final int DIVORCIADO = 3;
    public static final int VIUVO = 4;
    public static final int SEPARADO = 5;
    public static final int UNIAO_DE_FACTO = 6;
    public static final int DESCONHECIDO = 7;
    
    public static final String SOLTEIRO_STRING = "Solteiro";
    public static final String CASADO_STRING = "Casado";
    public static final String DIVORCIADO_STRING = "Divorciado";
    public static final String VIUVO_STRING = "Viúvo";
    public static final String SEPARADO_STRING = "Separado";
	public static final String UNIAO_DE_FACTO_STRING = "União de Facto";
	public static final String DESCONHECIDO_STRING = "Desconhecido";
	public static final String DEFAULT = "";
    
    private Integer estadoCivil;

    /** Creates a new instance of EstadoCivil */
    public EstadoCivil() {
    }
    
    public EstadoCivil(int estadoCivil) {
        this.estadoCivil = new Integer(estadoCivil);
    }

    public EstadoCivil(Integer estadoCivil) {
        this.estadoCivil = estadoCivil;
    }
    
    public boolean equals(Object o) {
        if(o instanceof EstadoCivil) {
            EstadoCivil aux = (EstadoCivil) o;
            return this.estadoCivil.equals(aux.getEstadoCivil());
        }
        else {
            return false;
        }
    }

    public EstadoCivil(String estadoCivil) {
		if (estadoCivil.equals(EstadoCivil.SOLTEIRO_STRING)) this.estadoCivil = new Integer(EstadoCivil.SOLTEIRO);
		else if (estadoCivil.equals(EstadoCivil.CASADO_STRING)) this.estadoCivil = new Integer(EstadoCivil.CASADO);
		else if (estadoCivil.equals(EstadoCivil.DIVORCIADO_STRING)) this.estadoCivil = new Integer(EstadoCivil.DIVORCIADO);
		else if (estadoCivil.equals(EstadoCivil.VIUVO_STRING)) this.estadoCivil = new Integer(EstadoCivil.VIUVO);
		else if (estadoCivil.equals(EstadoCivil.SEPARADO_STRING)) this.estadoCivil = new Integer(EstadoCivil.SEPARADO);
		else if (estadoCivil.equals(EstadoCivil.UNIAO_DE_FACTO_STRING)) this.estadoCivil = new Integer(EstadoCivil.UNIAO_DE_FACTO);
		else if (estadoCivil.equals(EstadoCivil.DESCONHECIDO_STRING)) this.estadoCivil = new Integer(EstadoCivil.DESCONHECIDO);
		else this.estadoCivil = new Integer(EstadoCivil.SOLTEIRO);
    }

    public ArrayList toArrayList() {
		ArrayList result = new ArrayList();
		result.add(new LabelValueBean(EstadoCivil.DEFAULT, null));
		result.add(new LabelValueBean(EstadoCivil.SOLTEIRO_STRING, EstadoCivil.SOLTEIRO_STRING));
		result.add(new LabelValueBean(EstadoCivil.CASADO_STRING, EstadoCivil.CASADO_STRING));
		result.add(new LabelValueBean(EstadoCivil.DIVORCIADO_STRING, EstadoCivil.DIVORCIADO_STRING));
		result.add(new LabelValueBean(EstadoCivil.VIUVO_STRING, EstadoCivil.VIUVO_STRING));
		result.add(new LabelValueBean(EstadoCivil.SEPARADO_STRING, EstadoCivil.SEPARADO_STRING));
		result.add(new LabelValueBean(EstadoCivil.UNIAO_DE_FACTO_STRING, EstadoCivil.UNIAO_DE_FACTO_STRING));
		result.add(new LabelValueBean(EstadoCivil.DESCONHECIDO_STRING, EstadoCivil.DESCONHECIDO_STRING));
		return result;	
    }
    
    public String toString() {
		if (estadoCivil.intValue()== EstadoCivil.SOLTEIRO) return EstadoCivil.SOLTEIRO_STRING;
	    if (estadoCivil.intValue()== EstadoCivil.CASADO) return EstadoCivil.CASADO_STRING;
	    if (estadoCivil.intValue()== EstadoCivil.DIVORCIADO) return EstadoCivil.DIVORCIADO_STRING;
	    if (estadoCivil.intValue()== EstadoCivil.VIUVO) return EstadoCivil.VIUVO_STRING;
	    if (estadoCivil.intValue()== EstadoCivil.SEPARADO) return EstadoCivil.SEPARADO_STRING;
		if (estadoCivil.intValue()== EstadoCivil.UNIAO_DE_FACTO) return EstadoCivil.UNIAO_DE_FACTO_STRING;
		if (estadoCivil.intValue()== EstadoCivil.DESCONHECIDO) return EstadoCivil.DESCONHECIDO_STRING;
	    return null; 
    }        
    
    /** Getter for property estadoCivil.
     * @return Value of property estadoCivil.
     *
     */
    public java.lang.Integer getEstadoCivil() {
        return estadoCivil;
    }
    
    /** Setter for property estadoCivil.
     * @param estadoCivil New value of property estadoCivil.
     *
     */
    public void setEstadoCivil(java.lang.Integer estadoCivil) {
        this.estadoCivil = estadoCivil;
    }


}
