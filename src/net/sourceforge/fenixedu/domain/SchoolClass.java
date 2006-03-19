package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
public class SchoolClass extends SchoolClass_Base {

    public SchoolClass() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
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
			if (curricularCourse.hasScopeForCurricularYear(getAnoCurricular())) {
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

}
