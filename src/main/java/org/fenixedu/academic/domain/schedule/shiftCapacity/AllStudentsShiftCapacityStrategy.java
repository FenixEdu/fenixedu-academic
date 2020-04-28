package org.fenixedu.academic.domain.schedule.shiftCapacity;

import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.student.Registration;

public class AllStudentsShiftCapacityStrategy extends AllStudentsShiftCapacityStrategy_Base {

    public AllStudentsShiftCapacityStrategy() {
        super();
    }

    @Override
    public String getTypeName() {
        return "All Students";
    }

    @Override
    public boolean accepts(Registration registration, Shift shift) {
        return true;
    }
}
