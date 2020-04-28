package org.fenixedu.academic.domain.schedule.shiftCapacity;

import java.util.stream.Stream;

import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.bennu.core.domain.Bennu;

public abstract class ShiftCapacityStrategy extends ShiftCapacityStrategy_Base {

    public ShiftCapacityStrategy() {
        super();
        setRoot(Bennu.getInstance());
    }

    public abstract String getTypeName();

    protected abstract boolean accepts(final Registration registration, final Shift shift);

    public void delete() {
        if (!getTypesSet().isEmpty()) {
            throw new DomainException("error.ShiftCapacityStrategy.delete.associatedTypesNotEmpty");
        }

        setRoot(null);
        super.deleteDomainObject();
    }

    public static Stream<ShiftCapacityStrategy> findAll() {
        return Bennu.getInstance().getShiftCapacityStrategiesSet().stream();
    }

}
