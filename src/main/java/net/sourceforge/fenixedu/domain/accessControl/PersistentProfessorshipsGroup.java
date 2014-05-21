package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentProfessorshipsGroup extends PersistentProfessorshipsGroup_Base {

    public PersistentProfessorshipsGroup(Boolean externalAuthorizations, AcademicPeriod period) {
        super();
        setExternalAuthorizations(externalAuthorizations);
        setOnCurrentPeriod(period);
    }

    @Override
    public Group toGroup() {
        return ProfessorshipsGroup.get(getExternalAuthorizations(), getOnCurrentPeriod());
    }

    public static PersistentProfessorshipsGroup getInstance(Boolean externalAuthorization, AcademicPeriod period) {
        return singleton(() -> select(externalAuthorization, period), () -> new PersistentProfessorshipsGroup(
                externalAuthorization, period));
    }

    private static Optional<PersistentProfessorshipsGroup> select(final Boolean externalAuthorization, final AcademicPeriod period) {
        return filter(PersistentProfessorshipsGroup.class).filter(
                group -> group.getExternalAuthorizations().equals(externalAuthorization)
                && group.getOnCurrentPeriod().equals(period)).findAny();
    }
}
