/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

/**
 * @author jpvl
 */
public class CreditsServiceWithTeacherIdInSecondArgumentAuthorization extends AbstractTeacherDepartmentAuthorization {

    public static final CreditsServiceWithTeacherIdInSecondArgumentAuthorization instance = new CreditsServiceWithTeacherIdInSecondArgumentAuthorization();
    /*
     * (non-Javadoc)
     * 
     * @see
     * ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization
     * #getTeacherId(java.lang.Object[])
     */
    @Override
    protected Integer getTeacherId(Object[] arguments) {
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