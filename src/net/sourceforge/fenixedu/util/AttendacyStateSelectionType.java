/*
 * Created on Jan 4, 2005
 */
package net.sourceforge.fenixedu.util;


/**
 * @author André Fernandes / João Brito
 */
public class AttendacyStateSelectionType
{
    public static final int ALL_TYPE = 1;
    public static final int ENROLLED_TYPE = 2;
    public static final int NOT_ENROLLED_TYPE = 3;
    public static final int IMPROVEMENT_TYPE = 4;
 
	public static final AttendacyStateSelectionType ALL =  new AttendacyStateSelectionType(AttendacyStateSelectionType.ALL_TYPE);
	public static final AttendacyStateSelectionType ENROLLED =  new AttendacyStateSelectionType(AttendacyStateSelectionType.ENROLLED_TYPE);
	public static final AttendacyStateSelectionType NOT_ENROLLED =  new AttendacyStateSelectionType(AttendacyStateSelectionType.NOT_ENROLLED_TYPE);
	public static final AttendacyStateSelectionType IMPROVEMENT =  new AttendacyStateSelectionType(AttendacyStateSelectionType.IMPROVEMENT_TYPE);

    private Integer selectionType;    
    
    private AttendacyStateSelectionType()
    {
    }
    
    public AttendacyStateSelectionType (String selType)
    {
        setSelectionType(new Integer(Integer.parseInt(selType)));
    }
    
    private AttendacyStateSelectionType(int selType)
    {
        setSelectionType(new Integer(selType));
    }

    private AttendacyStateSelectionType(Integer selType)
    {
        setSelectionType(selType);
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
        if (obj instanceof AttendacyStateSelectionType)
        {
            AttendacyStateSelectionType tipo = (AttendacyStateSelectionType)obj;
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
