/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.credits;

import DataBeans.teacher.professorship.InfoSupportLesson;
import Dominio.IProfessorship;
import Dominio.Professorship;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.ISuportePersistente;

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