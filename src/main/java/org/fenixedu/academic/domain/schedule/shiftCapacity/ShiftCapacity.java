package org.fenixedu.academic.domain.schedule.shiftCapacity;

import java.util.Comparator;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.bennu.core.domain.Bennu;

public class ShiftCapacity extends ShiftCapacity_Base {

    public final static Comparator<ShiftCapacity> TYPE_EVALUATION_PRIORITY_COMPARATOR =
            (sc1, sc2) -> Integer.compare(sc1.getType().getEvaluationPriority(), sc2.getType().getEvaluationPriority());

    protected ShiftCapacity() {
        super();
        setRoot(Bennu.getInstance());
    }

    public ShiftCapacity(final Shift shift, final ShiftCapacityType type, final Integer capacity) {
        this();
        setShift(shift);
        setType(type);
        setCapacity(capacity);
    }

    @Override
    public void setCapacity(Integer capacity) {
        if (capacity != null && !getShiftEnrolmentsSet().isEmpty() && capacity < getShiftEnrolmentsSet().size()) {
            throw new DomainException("error.ShiftCapacity.setCapacity.capacityCannotBeSmallerThenExistingEnrolments");
        }
        super.setCapacity(capacity);
    }

    public boolean isFree() {
        return getCapacity() - getShiftEnrolmentsSet().size() > 0;
    }

    public boolean isFor(final DegreeCurricularPlan dcp) {
        return getDegreeCurricularPlansSet().isEmpty() || getDegreeCurricularPlansSet().contains(dcp);
    }

    public boolean isFor(final SchoolClass schoolClass) {
        return getSchoolClassesSet().isEmpty() || getSchoolClassesSet().contains(schoolClass);
    }

    public boolean accepts(final Registration registration) {
        final boolean dcpsDefined = !getDegreeCurricularPlansSet().isEmpty();
        final boolean schoolClassesDefined = !getSchoolClassesSet().isEmpty();

        if (schoolClassesDefined && dcpsDefined) { // if both relations defined, must accept one or other
            if (!acceptsSchoolClass(registration) && !acceptsDegreeCurricularPlan(registration)) {
                return false;
            }
        } else {
            if (schoolClassesDefined && !acceptsSchoolClass(registration)) { // must accept school class
                return false;
            } else if (dcpsDefined && !acceptsDegreeCurricularPlan(registration)) { // must accept dcps
                return false;
            }
        }

        return getType().getStrategy().accepts(registration, getShift());
    }

    private boolean acceptsSchoolClass(final Registration registration) {
        final ExecutionInterval executionInterval = getShift().getExecutionPeriod();
        return registration.findSchoolClass(executionInterval).map(sc -> getSchoolClassesSet().contains(sc)).orElse(false);
    }

    private boolean acceptsDegreeCurricularPlan(final Registration registration) {
        return getDegreeCurricularPlansSet().contains(registration.getLastDegreeCurricularPlan());
    }

    public void delete() {
        if (!getShiftEnrolmentsSet().isEmpty()) {
            throw new DomainException("error.ShiftCapacity.delete.associatedShiftEnrolmentsNotEmpty");
        }

        getSchoolClassesSet().clear();
        getDegreeCurricularPlansSet().clear();
        setType(null);
        setShift(null);

        setRoot(null);
        super.deleteDomainObject();
    }

    public static int getTotalCapacity(final Shift shift) {
        return shift.getShiftCapacitiesSet().stream().mapToInt(sc -> sc.getCapacity()).sum();
    }

    public static int getTotalCapacity(final Shift shift, final ShiftCapacityType type) {
        return shift.getShiftCapacitiesSet().stream().filter(sc -> sc.getType() == type).mapToInt(sc -> sc.getCapacity()).sum();
    }

}