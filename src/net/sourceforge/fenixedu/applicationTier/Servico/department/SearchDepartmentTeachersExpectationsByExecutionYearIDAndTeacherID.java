package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author naat
 * 
 */
public class SearchDepartmentTeachersExpectationsByExecutionYearIDAndTeacherID extends Service {

    public List<TeacherPersonalExpectation> run(Integer departmentID, Integer executionYearID, Integer teacherID)
	    throws FenixServiceException {
	Department department = rootDomainObject.readDepartmentByOID(departmentID);

	List<TeacherPersonalExpectation> result = new ArrayList<TeacherPersonalExpectation>();

	ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);

	if (teacherID != null) {
	    Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);
	    TeacherPersonalExpectation teacherPersonalExpectation = teacher
		    .getTeacherPersonalExpectationByExecutionYear(executionYear);

	    if (teacherPersonalExpectation != null) {
		result.add(teacherPersonalExpectation);
	    }

	} else {
	    result = department.getTeachersPersonalExpectationsByExecutionYear(executionYear);
	}

	return result;
    }
}