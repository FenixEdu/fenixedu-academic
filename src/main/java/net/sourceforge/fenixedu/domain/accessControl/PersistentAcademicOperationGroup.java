package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType.Scope;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

public class PersistentAcademicOperationGroup extends PersistentAcademicOperationGroup_Base {
    protected PersistentAcademicOperationGroup(AcademicOperationType operation, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices, Scope scope) {
        super();
        setOperation(operation);
        if (programs != null) {
            getProgramSet().addAll(programs);
        }
        if (offices != null) {
            getOfficeSet().addAll(offices);
        }
        setScope(scope);
    }

    @Override
    public Group toGroup() {
        return AcademicAuthorizationGroup.get(getOperation(), getProgramSet(), getOfficeSet(), getScope());
    }

    @Override
    protected void gc() {
        getProgramSet().clear();
        getOfficeSet().clear();
        super.gc();
    }

    public static PersistentAcademicOperationGroup getInstance(final AcademicOperationType operation, final Scope scope) {
        return getInstance(operation, null, null, scope);
    }

    public static PersistentAcademicOperationGroup getInstance(final AcademicOperationType operation,
            final Set<AcademicProgram> programs, final Set<AdministrativeOffice> offices, final Scope scope) {
        Optional<PersistentAcademicOperationGroup> instance = select(operation, programs, offices, scope);
        return instance.isPresent() ? instance.get() : create(operation, programs, offices, scope);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentAcademicOperationGroup create(final AcademicOperationType operation,
            final Set<AcademicProgram> programs, final Set<AdministrativeOffice> offices, final Scope scope) {
        Optional<PersistentAcademicOperationGroup> instance = select(operation, programs, offices, scope);
        return instance.isPresent() ? instance.get() : new PersistentAcademicOperationGroup(operation, programs, offices, scope);
    }

    private static Optional<PersistentAcademicOperationGroup> select(final AcademicOperationType operation,
            final Set<AcademicProgram> programs, final Set<AdministrativeOffice> offices, final Scope scope) {
        return filter(PersistentAcademicOperationGroup.class).firstMatch(new Predicate<PersistentAcademicOperationGroup>() {
            @Override
            public boolean apply(PersistentAcademicOperationGroup group) {
                return Objects.equal(group.getOperation(), operation) && collectionEquals(group.getProgramSet(), programs)
                        && collectionEquals(group.getOfficeSet(), offices) && Objects.equal(group.getScope(), scope);
            }
        });
    }

    private static boolean collectionEquals(Set<?> one, Set<?> another) {
        //This could be made more efficient once issue #187 is fixed.
        return Sets.symmetricDifference(Objects.firstNonNull(one, Collections.emptySet()),
                Objects.firstNonNull(another, Collections.emptySet())).isEmpty();
    }
}
