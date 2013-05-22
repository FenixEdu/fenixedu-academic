/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

/**
 * @author jpvl
 */
public class CreditsServiceWithTeacherIdInSecondArgumentAuthorization extends AbstractTeacherDepartmentAuthorization<Integer> {

    public static final CreditsServiceWithTeacherIdInSecondArgumentAuthorization instance =
            new CreditsServiceWithTeacherIdInSecondArgumentAuthorization();

    /*
     * (non-Javadoc)
     * 
     * @see
     * ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization
     * #getTeacherId(java.lang.Object[])
     */
    @Override
    protected Integer getTeacherId(Integer teacherId) {
        return teacherId;
    }

    /**
     *  
     */
    public CreditsServiceWithTeacherIdInSecondArgumentAuthorization() {
        super();
    }
}