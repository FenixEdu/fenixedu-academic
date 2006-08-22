/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author jpvl
 */
public class DepartmentInsertProfessorshipAuthorization extends AbstractTeacherDepartmentAuthorization {

    protected Integer getTeacherId(Object[] arguments) throws ExcepcaoPersistencia {
        Teacher teacher = Teacher.readByNumber((Integer) arguments[1]);
        return (teacher == null) ? null : teacher.getIdInternal();
    }

}