package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class SetRootUnit {

    @Atomic
    public static void run(final Unit unit, final Boolean institutionUnit) {
        check(RolePredicates.MANAGER_PREDICATE);

        if (unit.isPlanetUnit()) {
            Bennu.getInstance().setEarthUnit(unit);

        } else if (institutionUnit) {
            Bennu.getInstance().setInstitutionUnit(unit);

        } else if (!institutionUnit) {
            Bennu.getInstance().setExternalInstitutionUnit(unit);
        }
    }
}