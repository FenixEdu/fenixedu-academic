/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class CreditsServiceWithTeacherIdArgumentAuthorization extends
        AbstractTeacherDepartmentAuthorization {
    public final static CreditsServiceWithTeacherIdArgumentAuthorization filter = new CreditsServiceWithTeacherIdArgumentAuthorization();

    public static CreditsServiceWithTeacherIdArgumentAuthorization getInstance() {
        return filter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization#getTeacherId(java.lang.Object[])
     */
    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp) {
        Integer teacherId = (Integer) arguments[0];
        return teacherId;
    }

}