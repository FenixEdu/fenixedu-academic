/*
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

public class GuideRequester extends FenixUtil {

    public static final int CANDIDATE = 1;
    public static final int STUDENT = 2;
    
	public static final GuideRequester CANDIDATE_TYPE = new GuideRequester(GuideRequester.CANDIDATE);
	public static final GuideRequester STUDENT_TYPE = new GuideRequester(GuideRequester.STUDENT);
	
    
    public static final String CANDIDATE_STRING = "Candidato";
    public static final String STUDENT_STRING = "Aluno";
	public static final String DEFAULT = "[Escolha o tipo]";
    
    private Integer type;

    public GuideRequester() {
    }
    
    public GuideRequester(int type) {
        this.type = new Integer(type);
    }

    public GuideRequester(Integer type) {
        this.type = type;
    }

    public boolean equals(Object o) {
        if(o instanceof GuideRequester) {
            GuideRequester aux = (GuideRequester) o;
            return this.type.equals(aux.getType());
        }
       
            return false;
        
    }

    public static ArrayList toArrayList() {
		ArrayList result = new ArrayList();
		result.add(new LabelValueBean(GuideRequester.DEFAULT, null));
	    result.add(new LabelValueBean(GuideRequester.CANDIDATE_STRING, GuideRequester.CANDIDATE_STRING));
		result.add(new LabelValueBean(GuideRequester.STUDENT_STRING, GuideRequester.STUDENT_STRING));
		return result;	
    }
    
    public String toString() {
		if (type.intValue()== GuideRequester.CANDIDATE) return GuideRequester.CANDIDATE_STRING;
	    if (type.intValue()== GuideRequester.STUDENT) return GuideRequester.STUDENT_STRING;
	    return "ERRO!"; // Nunca e atingido
    }


    /** Getter for property type.
     * @return Value of property type.
     *
     */
    public java.lang.Integer getType() {
        return type;
    }
    
    /** Setter for property type.
     * @param type New value of property type.
     *
     */
    public void setType(java.lang.Integer type) {
        this.type = type;
    }


}
