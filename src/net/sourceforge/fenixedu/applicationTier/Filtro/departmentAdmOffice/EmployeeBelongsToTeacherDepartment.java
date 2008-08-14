package net.sourceforge.fenixedu.applicationTier.Filtro.departmentAdmOffice;

import java.util.HashMap;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author mrsp and jdnf
 * 
 */
public class EmployeeBelongsToTeacherDepartment extends Filtro {

    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException, Exception {

	final IUserView userView = getRemoteUser(request);
	final Object[] argumentos = getServiceCallArguments(request);

	final Department teacherDepartment = getTeacherDepartment(argumentos);
	final Department employeeDepartment = getEmployeeDepartment(userView);

	if (!employeeDepartment.getName().equals(teacherDepartment.getName())) {
	    throw new NotAuthorizedFilterException();
	}
    }

    protected Department getEmployeeDepartment(IUserView userView) throws NotAuthorizedFilterException {
	final Person person = userView.getPerson();
	if (person == null) {
	    throw new NotAuthorizedFilterException("error.noPerson");
	}
	final Employee employee = person.getEmployee();
	if (employee == null) {
	    throw new NotAuthorizedFilterException("Não existe funcionario");
	}
	return getDepartment(employee);
    }

    protected Department getTeacherDepartment(Object[] argumentos) throws NotAuthorizedFilterException {
	return getDepartment(getTeacher(argumentos));
    }

    protected Department getDepartment(Teacher teacher) throws NotAuthorizedFilterException {
	final Department department = teacher.getCurrentWorkingDepartment();
	if (department == null) {
	    throw new NotAuthorizedFilterException("error.noDepartment");
	}
	return department;
    }

    protected Department getDepartment(Employee employee) throws NotAuthorizedFilterException {
	final Department department = employee.getCurrentDepartmentWorkingPlace();
	if (department == null) {
	    throw new NotAuthorizedFilterException("error.noDepartment");
	}
	return department;
    }

    protected Teacher getTeacher(Object[] argumentos) throws NotAuthorizedFilterException {
	final String teacherNumber = getTeacherNumber(argumentos);
	final Teacher teacher = Teacher.readByNumber(new Integer(teacherNumber));
	if (teacher == null) {
	    throw new NotAuthorizedFilterException("error.teacher.not.found");
	}
	return teacher;
    }

    protected String getTeacherNumber(Object[] argumentos) {
	final String teacherNumber;
	if (argumentos.length == 1 && argumentos[0] instanceof HashMap) {
	    HashMap hashMap = (HashMap) argumentos[0];
	    teacherNumber = (String) hashMap.get("teacherNumber");
	} else {
	    teacherNumber = argumentos[0].toString();
	}
	return teacherNumber;
    }

}
