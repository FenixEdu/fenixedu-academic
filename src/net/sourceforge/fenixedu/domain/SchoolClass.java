package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
public class SchoolClass extends SchoolClass_Base {

    public SchoolClass(final ExecutionDegree executionDegree, final ExecutionPeriod executionPeriod,
            final String name, final Integer curricularYear) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setExecutionDegree(executionDegree);
        setExecutionPeriod(executionPeriod);
        setAnoCurricular(curricularYear);
        setNome(name);
    }

    @Override
    public void setNome(String name) {
        final DegreeCurricularPlan degreeCurricularPlan = getExecutionDegree().getDegreeCurricularPlan();
        final Degree degree = degreeCurricularPlan.getDegree();
        super.setNome(constructName(degree, name, getAnoCurricular()));
    }

    protected String constructName(final Degree degree, final String name, final Integer curricularYear) {
        return degree.constructSchoolClassPrefix(curricularYear) + name;
    }

    public void delete() {
        getAssociatedShifts().clear();

        removeExecutionDegree();
        removeExecutionPeriod();
        removeRootDomainObject();

        deleteDomainObject();
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
                for (final ExecutionCourse executionCourse : curricularCourse
                        .getAssociatedExecutionCourses()) {
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
        return StringUtils.substringAfter(getNome(), degree
                .constructSchoolClassPrefix(getAnoCurricular()));
    }

}
