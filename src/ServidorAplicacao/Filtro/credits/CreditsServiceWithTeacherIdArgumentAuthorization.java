/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.credits;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class CreditsServiceWithTeacherIdArgumentAuthorization
        extends
            AbstractTeacherDepartmentAuthorization
{
    public final static CreditsServiceWithTeacherIdArgumentAuthorization filter = new CreditsServiceWithTeacherIdArgumentAuthorization();

    public static CreditsServiceWithTeacherIdArgumentAuthorization getInstance()
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
        Integer teacherId = (Integer) arguments[0];
        return teacherId;
    }

}