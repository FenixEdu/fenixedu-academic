package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;

/**
 * 
 * @author naat
 * 
 */
public class ReadDepartmentTeachersByDepartmentIDAndExecutionYearID extends FenixService {

	public List<Teacher> run(Integer departmentID, Integer executionYearID) throws FenixServiceException {
		Department department = rootDomainObject.readDepartmentByOID(departmentID);

		List<Teacher> teachersFromDepartment = new ArrayList<Teacher>();

		if (executionYearID != null) {
			ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);

			teachersFromDepartment =
					department.getAllTeachers(executionYear.getBeginDateYearMonthDay(), executionYear.getEndDateYearMonthDay());

		} else {
			teachersFromDepartment = department.getAllTeachers();
		}

		return teachersFromDepartment;
	}
}