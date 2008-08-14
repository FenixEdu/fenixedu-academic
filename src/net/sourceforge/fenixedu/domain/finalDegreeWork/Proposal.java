package net.sourceforge.fenixedu.domain.finalDegreeWork;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.coordinator.ExecutionDegreeCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;

public class Proposal extends Proposal_Base {

    public Proposal() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	getAssociatedGroupStudentsSet().clear();
	getBranchesSet().clear();
	removeCoorientator();
	removeGroupAttributed();
	removeGroupAttributedByTeacher();
	for (final GroupProposal groupProposal : getGroupProposalsSet()) {
	    groupProposal.delete();
	}
	removeOrientator();
	removeScheduleing();
	removeRootDomainObject();
	deleteDomainObject();
    }

    public boolean isProposalConfirmedByTeacherAndStudents(final FinalDegreeWorkGroup group) {
	return getGroupAttributedByTeacher() == group && group.isConfirmedByStudents(this);
    }

    public boolean canBeReadBy(final IUserView userView) {
	if (getStatus() != null && getStatus().equals(FinalDegreeWorkProposalStatus.PUBLISHED_STATUS)) {
	    return true;
	}
	if (getOrientator() != null && getOrientator() != null && userView != null
		&& getOrientator().getUsername().equals(userView.getUtilizador())) {
	    return true;
	}
	if (getCoorientator() != null && getCoorientator() != null && userView != null
		&& getCoorientator().getUsername().equals(userView.getUtilizador())) {
	    return true;
	}
	if (userView != null) {
	    final Person person = userView.getPerson();
	    for (final ExecutionDegree executionDegree : getScheduleing().getExecutionDegrees()) {
		for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
		    if (coordinator.getPerson() == person) {
			return true;
		    }
		}

		if (person.getEmployee() != null && person.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)) {
		    final Employee employee = person.getEmployee();
		    final Department department = employee.getCurrentDepartmentWorkingPlace();
		    final Set<CompetenceCourse> competenceCourses = department.getCompetenceCoursesSet();
		    if (hasDissertationCompetenceCourseForDepartment(executionDegree, competenceCourses)
			    || hasDissertationCompetenceCourseForDepartment(executionDegree, department.getDepartmentUnit())) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    protected boolean hasDissertationCompetenceCourseForDepartment(final ExecutionDegree executionDegree,
	    final Set<CompetenceCourse> competenceCourses) {
	for (final CompetenceCourse competenceCourse : competenceCourses) {
	    for (final CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCoursesSet()) {
		if (curricularCourse.getType() == CurricularCourseType.TFC_COURSE || competenceCourse.isDissertation()) {
		    final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
		    if (degreeCurricularPlan.getExecutionDegreesSet().contains(executionDegree)) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    private boolean hasDissertationCompetenceCourseForDepartment(final ExecutionDegree executionDegree, final Unit unit) {
	if (unit.isCompetenceCourseGroupUnit()) {
	    final CompetenceCourseGroupUnit competenceCourseGroupUnit = (CompetenceCourseGroupUnit) unit;
	    if (hasDissertationCompetenceCourseForDepartment(executionDegree, competenceCourseGroupUnit.getCompetenceCoursesSet())) {
		return true;
	    }
	}
	for (final Accountability accountability : unit.getChildsSet()) {
	    final Party party = accountability.getChildParty();
	    if (party.isUnit() && hasDissertationCompetenceCourseForDepartment(executionDegree, (Unit) party)) {
		return true;
	    }
	}
	return false;
    }

}
