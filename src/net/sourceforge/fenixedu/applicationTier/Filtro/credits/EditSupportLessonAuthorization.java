/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.InfoSupportLesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author jpvl
 */
public class EditSupportLessonAuthorization extends AbstractTeacherDepartmentAuthorization {
    public final static EditSupportLessonAuthorization filter = new EditSupportLessonAuthorization();

    public static EditSupportLessonAuthorization getInstance() {
        return filter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization#getTeacherId(java.lang.Object[])
     */
    protected Integer getTeacherId(Object[] arguments) {
        InfoSupportLesson supportLesson = (InfoSupportLesson) arguments[1];

        Professorship professorship;
        try {
            professorship = (Professorship) persistentObject.readByOID(Professorship.class,
                    supportLesson.getInfoProfessorship().getIdInternal());
        } catch (ExcepcaoPersistencia e) {
            return null;
        }
        return professorship != null ? professorship.getTeacher().getIdInternal() : null;
    }

}