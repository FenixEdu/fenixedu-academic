package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourseWithCurricularCourses;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateEditCompetenceCourse extends FenixService {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static InfoCompetenceCourse run(Integer competenceCourseID, String code, String name, Integer[] departmentIDs)
	    throws NonExistingServiceException, InvalidArgumentsServiceException {
	List<Department> departments = new ArrayList<Department>();
	for (Integer departmentID : departmentIDs) {
	    Department department = rootDomainObject.readDepartmentByOID(departmentID);
	    if (department == null) {
		throw new NonExistingServiceException("error.manager.noDepartment");
	    }
	    departments.add(department);
	}

	try {
	    CompetenceCourse competenceCourse = null;
	    if (competenceCourseID == null) {
		competenceCourse = new CompetenceCourse(code, name, departments);
	    } else {
		competenceCourse = rootDomainObject.readCompetenceCourseByOID(competenceCourseID);
		if (competenceCourse == null) {
		    throw new NonExistingServiceException("error.manager.noCompetenceCourse");
		}
		competenceCourse.edit(code, name, departments);
	    }
	    return InfoCompetenceCourseWithCurricularCourses.newInfoFromDomain(competenceCourse);
	} catch (DomainException domainException) {
	    throw new InvalidArgumentsServiceException(domainException.getMessage());
	}
    }
}