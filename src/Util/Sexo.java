/*
 * Sexo.java
 *
 * Created on 13 de Novembro de 2002, 22:17
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

public class Sexo extends FenixUtil {

    public static final int MASCULINO = 1;
    public static final int FEMININO = 2;
    
    public static final String MASCULINO_STRING = "Masculino";
    public static final String FEMININO_STRING = "Feminino";
	public static final String DEFAULT = "[Escolha o Sexo]";
    
    private Integer sexo;

    /** Creates a new instance of Sexo */
    public Sexo() {
    }
    
    public Sexo(int sexo) {
        this.sexo = new Integer(sexo);
    }

    public Sexo(Integer sexo) {
        this.sexo = sexo;
    }

    public Sexo(String sexo) {
		if (sexo.equals(Sexo.MASCULINO_STRING)) this.sexo = new Integer(Sexo.MASCULINO);
		if (sexo.equals(Sexo.FEMININO_STRING)) this.sexo = new Integer(Sexo.FEMININO);
    }

    public boolean equals(Object o) {
        if(o instanceof Sexo) {
            Sexo aux = (Sexo) o;
            return this.sexo.equals(aux.getSexo());
        }
      
            return false;
        
    }

    public ArrayList toArrayList() {
		ArrayList result = new ArrayList();
		result.add(new LabelValueBean(Sexo.DEFAULT, null));
	    result.add(new LabelValueBean(Sexo.MASCULINO_STRING, Sexo.MASCULINO_STRING));
		result.add(new LabelValueBean(Sexo.FEMININO_STRING, Sexo.FEMININO_STRING));
		return result;	
    }
    
    public String toString() {
		if (sexo.intValue()== Sexo.MASCULINO) return Sexo.MASCULINO_STRING;
	    if (sexo.intValue()== Sexo.FEMININO) return Sexo.FEMININO_STRING;
	    return ""; // Nunca e atingido
    }


    /** Getter for property sexo.
     * @return Value of property sexo.
     *
     */
    public java.lang.Integer getSexo() {
        return sexo;
    }
    
    /** Setter for property sexo.
     * @param sexo New value of property sexo.
     *
     */
    public void setSexo(java.lang.Integer sexo) {
        this.sexo = sexo;
    }


}
