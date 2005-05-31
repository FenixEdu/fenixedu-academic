/*
 * Created on May 30, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.EmployeeHistoric;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IEmployeeHistoric;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author jdnf
 *  
 */
public class TeacherVO extends VersionedObjectsBase implements IPersistentTeacher {

    public ITeacher readTeacherByUsername(final String userName) throws ExcepcaoPersistencia {
        final Collection<ITeacher> teachers = readAll(Teacher.class);
        for (final ITeacher teacher : teachers) {
            if (teacher.getPerson().getUsername().equals(userName))
                return teacher;
        }
        return null;
    }

    public List readByDepartment(String departmentCode) throws ExcepcaoPersistencia {

        final List<ITeacher> result = new ArrayList<ITeacher>();
        final List<IEmployee> employees = getEmployees(departmentCode);

        for (final IEmployee employee : employees) {
            final List<ITeacher> teachers = employee.getPerson().getAssociatedTeachers();
            if (teachers != null) {
                for (final ITeacher teacher : teachers) {
                    if (teacher.getTeacherNumber().equals(employee.getEmployeeNumber())) {
                        result.add(teacher);
                    }
                }
            }

        }
        return result;
    }

    public ITeacher readByNumber(final Integer teacherNumber) throws ExcepcaoPersistencia {
        final Collection<ITeacher> teachers = readAll(Teacher.class);
        for (final ITeacher teacher : teachers) {
            if (teacher.getTeacherNumber().equals(teacherNumber))
                return teacher;
        }
        return null;
    }

    private List<IEmployee> getEmployees(final String departmentCode) {
        final String lowerCaseDepartmentCode = departmentCode.toLowerCase();
        final Date actualDate = Calendar.getInstance().getTime();
        final List<IEmployee> selectedEmployee = new ArrayList<IEmployee>();

        final Collection<IEmployeeHistoric> employeeHistorics = readAll(EmployeeHistoric.class);
        for (final IEmployeeHistoric employeeHistoric : employeeHistorics) {
            if (employeeHistoric.getEmployee().getActive().booleanValue()
                    && ((criteriaWorkingPlaceCostCenter(employeeHistoric, lowerCaseDepartmentCode) && criteriaBeginDate(
                            employeeHistoric, actualDate)) || criteriaAlternativeDate(employeeHistoric,
                            actualDate))) {

                selectedEmployee.add(employeeHistoric.getEmployee());
            }
        }
        return selectedEmployee;
    }

    private boolean criteriaAlternativeDate(final IEmployeeHistoric employeeHistoric,
            final Date actualDate) {
        return (employeeHistoric.getBeginDate().before(actualDate) || employeeHistoric.getBeginDate()
                .equals(actualDate))
                && (employeeHistoric.getEndDate() != null)
                && (employeeHistoric.getEndDate().equals(actualDate) || employeeHistoric.getEndDate()
                        .after(actualDate));
    }

    private boolean criteriaWorkingPlaceCostCenter(final IEmployeeHistoric employeeHistoric,
            final String lowerCaseDepartmentCode) {
        return employeeHistoric.getWorkingPlaceCostCenter().getCode().toLowerCase().startsWith(
                lowerCaseDepartmentCode);
    }

    private boolean criteriaBeginDate(final IEmployeeHistoric employeeHistoric, final Date actualDate) {
        return (employeeHistoric.getBeginDate().before(actualDate) || employeeHistoric.getBeginDate()
                .equals(actualDate))
                && (employeeHistoric.getEndDate() == null);
    }

    public Collection<ITeacher> readByNumbers(final Collection<Integer> teacherNumbers)
            throws ExcepcaoPersistencia {

        final Collection<ITeacher> result = new ArrayList<ITeacher>();
        final Collection<ITeacher> teachers = readAll(Teacher.class);

        for (final Integer teacherNumber : teacherNumbers) {
            for (final ITeacher teacher : teachers) {
                if (teacher.getTeacherNumber().equals(teacherNumber)) {
                    result.add(teacher);
                    break;
                }
            }
        }
        return result;
    }
}
