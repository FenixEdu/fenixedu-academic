package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.Atomic;

public class NonRegularTeachingService extends NonRegularTeachingService_Base {

    public NonRegularTeachingService(Professorship professorship, Shift shift, Double percentage) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setPercentage(percentage);
        setProfessorship(professorship);
        setShift(shift);
    }

    @Atomic
    public static void createOrEdit(Professorship professorship, Shift shift, Double percentage) {

        if (percentage != null) {
            if (percentage > 100 || percentage < 0) {
                throw new DomainException("message.invalid.professorship.percentage");
            }
            Double availablePercentage = shift.getAvailableShiftPercentage(professorship);
            if (percentage > availablePercentage) {
                throw new DomainException("message.exceeded.professorship.percentage");
            }
        }

        for (NonRegularTeachingService nonRegularTeachingService : professorship.getNonRegularTeachingServices()) {
            if (nonRegularTeachingService.getShift().equals(shift)) {
                if (percentage == null || percentage.equals(new Double(0.0))) {
                    nonRegularTeachingService.delete();
                } else {
                    nonRegularTeachingService.setPercentage(percentage);
                }
                return;
            }
        }
        if (percentage != null && !percentage.equals(new Double(0.0))) {
            new NonRegularTeachingService(professorship, shift, percentage);
        }
    }

    private void delete() {
        setProfessorship(null);
        setShift(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasPercentage() {
        return getPercentage() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasProfessorship() {
        return getProfessorship() != null;
    }

    @Deprecated
    public boolean hasShift() {
        return getShift() != null;
    }

}
