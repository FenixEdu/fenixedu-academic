/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.credits;

import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import Dominio.degree.finalProject.TeacherDegreeFinalProjectStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;

/**
 * @author jpvl
 */
public class ReadDeleteTeacherDegreeFinalProjectStudentAuthorization extends
        AbstractTeacherDepartmentAuthorization {
    public final static ReadDeleteTeacherDegreeFinalProjectStudentAuthorization filter = new ReadDeleteTeacherDegreeFinalProjectStudentAuthorization();

    public static ReadDeleteTeacherDegreeFinalProjectStudentAuthorization getInstance() {
        return filter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization#getTeacherId(java.lang.Object[])
     */
    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        Integer teacherDegreeFinalProjectStudentId = (Integer) arguments[0];
        IPersistentTeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudentDAO = sp
                .getIPersistentTeacherDegreeFinalProjectStudent();

        ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = (ITeacherDegreeFinalProjectStudent) teacherDegreeFinalProjectStudentDAO
                .readByOID(TeacherDegreeFinalProjectStudent.class, teacherDegreeFinalProjectStudentId,
                        false);
        return teacherDegreeFinalProjectStudent != null ? teacherDegreeFinalProjectStudent.getTeacher()
                .getIdInternal() : null;
    }

}