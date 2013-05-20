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

    public static final DepartmentInsertProfessorshipAuthorization instance = new DepartmentInsertProfessorshipAuthorization();

    @Override
    protected Integer getTeacherId(Object[] arguments) {
        Teacher teacher = Teacher.readByIstId((String) arguments[1]);
        return (teacher == null) ? null : teacher.getIdInternal();
    }

}