package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

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
        Optional<PersistentProfessorshipsGroup> instance = select(externalAuthorization, period);
        return instance.isPresent() ? instance.get() : create(externalAuthorization, period);
    }

    private static Optional<PersistentProfessorshipsGroup> select(final Boolean externalAuthorization, final AcademicPeriod period) {
        return filter(PersistentProfessorshipsGroup.class).firstMatch(new Predicate<PersistentProfessorshipsGroup>() {
            @Override
            public boolean apply(PersistentProfessorshipsGroup group) {
                return group.getExternalAuthorizations().equals(externalAuthorization)
                        && group.getOnCurrentPeriod().equals(period);
            }
        });
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentProfessorshipsGroup create(Boolean externalAuthorization, AcademicPeriod period) {
        Optional<PersistentProfessorshipsGroup> instance = select(externalAuthorization, period);
        return instance.isPresent() ? instance.get() : new PersistentProfessorshipsGroup(externalAuthorization, period);
    }
}
