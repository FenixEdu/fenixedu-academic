/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.InfoSupportLesson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

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
    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp) {
        InfoSupportLesson supportLesson = (InfoSupportLesson) arguments[1];
        IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();

        IProfessorship professorship;
        try {
            professorship = (IProfessorship) professorshipDAO.readByOID(Professorship.class,
                    supportLesson.getInfoProfessorship().getIdInternal());
        } catch (ExcepcaoPersistencia e) {
            return null;
        }
        return professorship != null ? professorship.getTeacher().getIdInternal() : null;
    }

}