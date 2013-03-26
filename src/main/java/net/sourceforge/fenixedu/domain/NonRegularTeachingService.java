package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;

public class NonRegularTeachingService extends NonRegularTeachingService_Base {

    public NonRegularTeachingService(Professorship professorship, Shift shift, Double percentage) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setPercentage(percentage);
        setProfessorship(professorship);
        setShift(shift);
    }

    @Service
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
        removeProfessorship();
        removeShift();
        removeRootDomainObject();
        deleteDomainObject();
    }

}
