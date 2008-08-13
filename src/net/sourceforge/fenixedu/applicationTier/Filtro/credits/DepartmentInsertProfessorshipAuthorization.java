/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.domain.Teacher;

/**
 * @author jpvl
 */
public class DepartmentInsertProfessorshipAuthorization extends AbstractTeacherDepartmentAuthorization {

    protected Integer getTeacherId(Object[] arguments) {
	Teacher teacher = Teacher.readByNumber((Integer) arguments[1]);
	return (teacher == null) ? null : teacher.getIdInternal();
    }

}