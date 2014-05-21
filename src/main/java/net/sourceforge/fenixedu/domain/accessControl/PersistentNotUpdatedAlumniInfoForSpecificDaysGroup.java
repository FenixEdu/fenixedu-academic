package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import org.fenixedu.bennu.core.groups.Group;

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
        return singleton(
                () -> select(daysNotUpdated, checkJobNotUpdated, checkFormationNotUpdated, checkPersonalDataNotUpdated),
                () -> new PersistentNotUpdatedAlumniInfoForSpecificDaysGroup(daysNotUpdated, checkJobNotUpdated,
                        checkFormationNotUpdated, checkPersonalDataNotUpdated));
    }

    private static Optional<PersistentNotUpdatedAlumniInfoForSpecificDaysGroup> select(final Integer daysNotUpdated,
            final Boolean checkJobNotUpdated, final Boolean checkFormationNotUpdated, final Boolean checkPersonalDataNotUpdated) {
        return filter(PersistentNotUpdatedAlumniInfoForSpecificDaysGroup.class).filter(
                group -> group.getDaysNotUpdated() == daysNotUpdated && group.getCheckJobNotUpdated() == checkJobNotUpdated
                && group.getCheckFormationNotUpdated() == checkFormationNotUpdated
                && group.getCheckPersonalDataNotUpdated() == checkPersonalDataNotUpdated).findAny();
    }
}
