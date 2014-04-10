package net.sourceforge.fenixedu.domain.accessControl;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

public class PersistentNotUpdatedAlumniInfoForSpecificDaysGroup extends PersistentNotUpdatedAlumniInfoForSpecificDaysGroup_Base {
    protected PersistentNotUpdatedAlumniInfoForSpecificDaysGroup(Integer daysNotUpdated, Boolean checkJobNotUpdated,
            Boolean checkFormationNotUpdated, Boolean checkPersonalDataNotUpdated) {
        super();
        setDaysNotUpdated(daysNotUpdated);
        setCheckJobNotUpdated(checkJobNotUpdated);
        setCheckFormationNotUpdated(checkFormationNotUpdated);
        setCheckPersonalDataNotUpdated(checkPersonalDataNotUpdated);
    }

    @Override
    public Group toGroup() {
        return NotUpdatedAlumniInfoForSpecificDaysGroup.get(getDaysNotUpdated(), getCheckJobNotUpdated(),
                getCheckFormationNotUpdated(), getCheckPersonalDataNotUpdated());
    }

    public static PersistentNotUpdatedAlumniInfoForSpecificDaysGroup getInstance(final Integer daysNotUpdated,
            final Boolean checkJobNotUpdated, final Boolean checkFormationNotUpdated, final Boolean checkPersonalDataNotUpdated) {
        Optional<PersistentNotUpdatedAlumniInfoForSpecificDaysGroup> instance =
                select(daysNotUpdated, checkJobNotUpdated, checkFormationNotUpdated, checkPersonalDataNotUpdated);
        return instance.isPresent() ? instance.get() : create(daysNotUpdated, checkJobNotUpdated, checkFormationNotUpdated,
                checkPersonalDataNotUpdated);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentNotUpdatedAlumniInfoForSpecificDaysGroup create(final Integer daysNotUpdated,
            final Boolean checkJobNotUpdated, final Boolean checkFormationNotUpdated, final Boolean checkPersonalDataNotUpdated) {
        Optional<PersistentNotUpdatedAlumniInfoForSpecificDaysGroup> instance =
                select(daysNotUpdated, checkJobNotUpdated, checkFormationNotUpdated, checkPersonalDataNotUpdated);
        return instance.isPresent() ? instance.get() : new PersistentNotUpdatedAlumniInfoForSpecificDaysGroup(daysNotUpdated,
                checkJobNotUpdated, checkFormationNotUpdated, checkPersonalDataNotUpdated);
    }

    private static Optional<PersistentNotUpdatedAlumniInfoForSpecificDaysGroup> select(final Integer daysNotUpdated,
            final Boolean checkJobNotUpdated, final Boolean checkFormationNotUpdated, final Boolean checkPersonalDataNotUpdated) {
        return filter(PersistentNotUpdatedAlumniInfoForSpecificDaysGroup.class).firstMatch(
                new Predicate<PersistentNotUpdatedAlumniInfoForSpecificDaysGroup>() {
                    @Override
                    public boolean apply(PersistentNotUpdatedAlumniInfoForSpecificDaysGroup group) {
                        return group.getDaysNotUpdated() == daysNotUpdated && group.getCheckJobNotUpdated() == checkJobNotUpdated
                                && group.getCheckFormationNotUpdated() == checkFormationNotUpdated
                                && group.getCheckPersonalDataNotUpdated() == checkPersonalDataNotUpdated;
                    }
                });
    }
}
