/*
 * Created on Oct 20, 2004
 *
 */
package Util;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Andre Fernandes / Joao Brito
 * 
 */

public class EnrollmentStateSelectionType extends FenixUtil {
    public static final int ALL_TYPE = 1;
    public static final int APPROVED_TYPE = 2;
    public static final int NONE_TYPE = 3;
 
    public static final String ALL_STRING = "Todas";
    public static final String APPROVED_STRING = "Aprovado / Inscrito";
	public static final String NONE_STRING = "Nenhuma disciplina";
	
	public static final EnrollmentStateSelectionType ALL =  new EnrollmentStateSelectionType(EnrollmentStateSelectionType.ALL_TYPE);
	public static final EnrollmentStateSelectionType APPROVED =  new EnrollmentStateSelectionType(EnrollmentStateSelectionType.APPROVED_TYPE);
	public static final EnrollmentStateSelectionType NONE =  new EnrollmentStateSelectionType(EnrollmentStateSelectionType.NONE_TYPE);

    private Integer selectionType;    
    
    private EnrollmentStateSelectionType()
    {
    }
    
    public EnrollmentStateSelectionType (String selType)
    {
        setSelectionType(new Integer(Integer.parseInt(selType)));
    }
    
    private EnrollmentStateSelectionType(int selType)
    {
        setSelectionType(new Integer(selType));
    }

    private EnrollmentStateSelectionType(Integer selType)
    {
        setSelectionType(selType);
    }


	public static List getLabelValueBeanList()
	{
		ArrayList result = new ArrayList();
		result.add(new LabelValueBean(EnrollmentStateSelectionType.ALL_STRING, EnrollmentStateSelectionType.ALL.toString()));
		result.add(new LabelValueBean(EnrollmentStateSelectionType.APPROVED_STRING, EnrollmentStateSelectionType.APPROVED.toString()));
		return result;	
	}
    
    public Integer getSelectionType()
    {
        return this.selectionType;
    }    

    private void setSelectionType(Integer selType) 
    {
        this.selectionType = selType;
    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;
        if (obj instanceof EnrollmentStateSelectionType)
        {
            EnrollmentStateSelectionType tipo = (EnrollmentStateSelectionType)obj;
            resultado = (getSelectionType() != null) &&
            			(getSelectionType().equals(tipo.getSelectionType()));
        }
        return resultado;
    }
    
    public String toString()
    {
    	return getSelectionType().toString();
    }
    
}
