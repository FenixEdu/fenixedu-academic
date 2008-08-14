package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
public class SchoolClass extends SchoolClass_Base {

    @Checked("ResourceAllocationRolePredicates.checkPermissionsToManageSchoolClass")
    public SchoolClass(final ExecutionDegree executionDegree, final ExecutionSemester executionSemester, final String name,
	    final Integer curricularYear) {
	super();

	checkIfExistsSchoolClassWithSameName(executionDegree, executionSemester, curricularYear, name);

	setRootDomainObject(RootDomainObject.getInstance());
	setExecutionDegree(executionDegree);
	setExecutionPeriod(executionSemester);
	setAnoCurricular(curricularYear);
	setNome(name);
    }

    @Checked("ResourceAllocationRolePredicates.checkPermissionsToManageSchoolClass")
    public void edit(String name) {
	if (name != null && !StringUtils.isEmpty(name.trim())) {
	    final SchoolClass otherClassWithSameNewName = getExecutionDegree().findSchoolClassesByExecutionPeriodAndName(
		    getExecutionPeriod(), name.trim());
	    if (otherClassWithSameNewName != null && !otherClassWithSameNewName.equals(this)) {
		throw new DomainException("Duplicate Entry: " + otherClassWithSameNewName.getNome());
	    }
	}
	setNome(name);
    }

    @Checked("ResourceAllocationRolePredicates.checkPermissionsToManageSchoolClass")
    public void delete() {
	getAssociatedShifts().clear();
	super.setExecutionDegree(null);
	super.setExecutionPeriod(null);
	removeRootDomainObject();
	deleteDomainObject();
    }

    @Override
    public void setExecutionDegree(ExecutionDegree executionDegree) {
	if (executionDegree == null) {
	    throw new DomainException("error.SchoolClass.empty.executionDegree");
	}
	super.setExecutionDegree(executionDegree);
    }

    @Override
    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	if (executionSemester == null) {
	    throw new DomainException("error.SchoolClass.empty.executionPeriod");
	}
	super.setExecutionPeriod(executionSemester);
    }

    @Override
    public void setAnoCurricular(Integer anoCurricular) {
	if (anoCurricular == null || anoCurricular.intValue() < 1) {
	    throw new DomainException("error.SchoolClass.invalid.curricularYear");
	}
	super.setAnoCurricular(anoCurricular);
    }

    @Override
    public void setNome(String name) {
	if (name == null || StringUtils.isEmpty(name.trim())) {
	    throw new DomainException("error.SchoolClass.empty.name");
	}
	final DegreeCurricularPlan degreeCurricularPlan = getExecutionDegree().getDegreeCurricularPlan();
	final Degree degree = degreeCurricularPlan.getDegree();
	super.setNome(constructName(degree, name.trim(), getAnoCurricular()));
    }

    private void checkIfExistsSchoolClassWithSameName(ExecutionDegree executionDegree, ExecutionSemester executionSemester,
	    Integer curricularYear, String className) {

	if (executionDegree != null && executionSemester != null && curricularYear != null && className != null) {

	    final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
	    final Set<SchoolClass> classes = executionDegree.findSchoolClassesByExecutionPeriodAndCurricularYear(
		    executionSemester, curricularYear);
	    final Degree degree = degreeCurricularPlan.getDegree();
	    final String schoolClassName = degree.constructSchoolClassPrefix(curricularYear) + className;

	    for (final SchoolClass schoolClass : classes) {
		if (!schoolClass.equals(this) && schoolClassName.equalsIgnoreCase(schoolClass.getNome())) {
		    throw new DomainException("Duplicate Entry: " + className + " for curricular year " + curricularYear
			    + " and degree curricular plan " + degreeCurricularPlan.getName());
		}
	    }
	}
    }

    protected String constructName(final Degree degree, final String name, final Integer curricularYear) {
	return degree.constructSchoolClassPrefix(curricularYear) + name;
    }

    public void associateShift(Shift shift) {
	if (shift == null) {
	    throw new NullPointerException();
	}
	if (!this.getAssociatedShifts().contains(shift)) {
	    this.getAssociatedShifts().add(shift);
	}
	if (!shift.getAssociatedClasses().contains(this)) {
	    shift.getAssociatedClasses().add(this);
	}
    }

    public Set<Shift> findAvailableShifts() {
	final ExecutionDegree executionDegree = getExecutionDegree();
	final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

	final Set<Shift> shifts = new HashSet<Shift>();
	for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
	    if (curricularCourse.hasScopeForCurricularYear(getAnoCurricular(), getExecutionPeriod())) {
		for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
		    if (executionCourse.getExecutionPeriod() == getExecutionPeriod()) {
			shifts.addAll(executionCourse.getAssociatedShifts());
		    }
		}
	    }
	}
	shifts.removeAll(getAssociatedShifts());
	return shifts;
    }

    public Object getEditablePartOfName() {
	final DegreeCurricularPlan degreeCurricularPlan = getExecutionDegree().getDegreeCurricularPlan();
	final Degree degree = degreeCurricularPlan.getDegree();
	return StringUtils.substringAfter(getNome(), degree.constructSchoolClassPrefix(getAnoCurricular()));
    }

}
