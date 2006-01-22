/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author jpvl
 */
public class ReadDeleteSupportLessonAuthorization extends AbstractTeacherDepartmentAuthorization {
    public final static ReadDeleteSupportLessonAuthorization filter = new ReadDeleteSupportLessonAuthorization();

    public static ReadDeleteSupportLessonAuthorization getInstance() {
        return filter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization#getTeacherId(java.lang.Object[])
     */
    protected Integer getTeacherId(Object[] arguments)
            throws ExcepcaoPersistencia {
        Integer supportLessonId = (Integer) arguments[0];

        SupportLesson supportLesson = (SupportLesson) persistentObject.readByOID(SupportLesson.class,
                supportLessonId);
        return supportLesson != null ? supportLesson.getProfessorship().getTeacher().getIdInternal()
                : null;
    }

}