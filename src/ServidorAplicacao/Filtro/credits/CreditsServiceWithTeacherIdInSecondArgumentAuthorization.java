/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.credits;

import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class CreditsServiceWithTeacherIdInSecondArgumentAuthorization extends
        AbstractTeacherDepartmentAuthorization {
    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization#getTeacherId(java.lang.Object[])
     */
    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp) {
        Integer teacherId = (Integer) arguments[1];
        return teacherId;
    }

    /**
     *  
     */
    public CreditsServiceWithTeacherIdInSecondArgumentAuthorization() {
        super();
    }
}