/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.credits;

import DataBeans.InfoTeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class CreditsServiceWithInfoTeacherArgumentAuthorization extends AbstractTeacherDepartmentAuthorization
{
	public final static CreditsServiceWithInfoTeacherArgumentAuthorization filter = new CreditsServiceWithInfoTeacherArgumentAuthorization();

	public static CreditsServiceWithInfoTeacherArgumentAuthorization getInstance()
	{
		return filter;
	}

    
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization#getTeacherId(java.lang.Object[])
	 */
    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp)
            throws FenixServiceException
    {
		InfoTeacher infoTeacher = (InfoTeacher) arguments[0];
        
		return infoTeacher != null ? infoTeacher.getIdInternal() : null;    
    }

}