/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

/**
 * @author jpvl
 */
public class CreditsServiceWithTeacherIdArgumentAuthorization extends AbstractTeacherDepartmentAuthorization<String> {

    public static final CreditsServiceWithTeacherIdArgumentAuthorization instance =
            new CreditsServiceWithTeacherIdArgumentAuthorization();
    public final static CreditsServiceWithTeacherIdArgumentAuthorization filter =
            new CreditsServiceWithTeacherIdArgumentAuthorization();

    public static CreditsServiceWithTeacherIdArgumentAuthorization getInstance() {
        return filter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization
     * #getTeacherId(java.lang.Object[])
     */
    @Override
    protected String getTeacherId(String teacherId) {
        return teacherId;
    }

}