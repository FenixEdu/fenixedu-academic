/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.credits;

import Dominio.ISupportLesson;
import Dominio.SupportLesson;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.teacher.professorship.IPersistentSupportLesson;

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
    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        Integer supportLessonId = (Integer) arguments[0];
        IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();

        ISupportLesson supportLesson = (ISupportLesson) supportLessonDAO.readByOID(SupportLesson.class,
                supportLessonId, false);
        return supportLesson != null ? supportLesson.getProfessorship().getTeacher().getIdInternal()
                : null;
    }

}