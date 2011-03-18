package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadCompetenceCoursesByDepartment extends FenixService {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static List<InfoCompetenceCourse> run(Integer departmentID) throws NotExistingServiceException {

	final List<InfoCompetenceCourse> result = new ArrayList<InfoCompetenceCourse>();
	if (departmentID != null) {
	    final Department department = rootDomainObject.readDepartmentByOID(departmentID);
	    if (department == null) {
		throw new NotExistingServiceException("error.manager.noDepartment");
	    }
	    for (final CompetenceCourse competenceCourse : department.getCompetenceCourses()) {
		if (competenceCourse.getCurricularStage() == CurricularStage.OLD) {
		    result.add(InfoCompetenceCourse.newInfoFromDomain(competenceCourse));
		}
	    }
	} else {
	    // read competence course with no associated department
	    final List<CompetenceCourse> allCompetenceCourses = CompetenceCourse.readOldCompetenceCourses();
	    final List<CompetenceCourse> noDeptCompetenceCourse = (List<CompetenceCourse>) CollectionUtils.select(
		    allCompetenceCourses, new Predicate() {

			@Override
			public boolean evaluate(Object arg0) {
			    CompetenceCourse competenceCourse = (CompetenceCourse) arg0;
			    return !competenceCourse.hasAnyDepartments();
			}

		    });

	    for (final CompetenceCourse course : noDeptCompetenceCourse) {
		result.add(InfoCompetenceCourse.newInfoFromDomain(course));
	    }
	}
	return result;
    }

}