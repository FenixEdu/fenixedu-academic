package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourseWithCurricularCourses;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateEditCompetenceCourse {

    @Atomic
    public static InfoCompetenceCourse run(String competenceCourseID, String code, String name, String[] departmentIDs)
            throws NonExistingServiceException, InvalidArgumentsServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        List<Department> departments = new ArrayList<Department>();
        for (String departmentID : departmentIDs) {
            Department department = FenixFramework.getDomainObject(departmentID);
            if (department == null) {
                throw new NonExistingServiceException("error.manager.noDepartment");
            }
            departments.add(department);
        }

        try {
            CompetenceCourse competenceCourse = null;
            if (StringUtils.isEmpty(competenceCourseID)) {
                competenceCourse = new CompetenceCourse(code, name, departments);
            } else {
                competenceCourse = FenixFramework.getDomainObject(competenceCourseID);
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