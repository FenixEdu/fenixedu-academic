/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.credits;

import DataBeans.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class EditTeacherDegreeFinalProjectStudentAuthorization extends
        AbstractTeacherDepartmentAuthorization {
    public final static EditTeacherDegreeFinalProjectStudentAuthorization filter = new EditTeacherDegreeFinalProjectStudentAuthorization();

    public static EditTeacherDegreeFinalProjectStudentAuthorization getInstance() {
        return filter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization#getTeacherId(java.lang.Object[])
     */
    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp) {
        InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = (InfoTeacherDegreeFinalProjectStudent) arguments[1];
        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

        ITeacher teacher;
        try {
            teacher = (ITeacher) teacherDAO.readByOID(Teacher.class,
                    infoTeacherDegreeFinalProjectStudent.getInfoTeacher()
                            .getIdInternal());
        } catch (ExcepcaoPersistencia e) {
            return null;
        }
        return teacher != null ? teacher.getIdInternal() : null;
    }

}