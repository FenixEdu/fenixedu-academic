package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;


import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class SetRootUnit {

    @Atomic
    public static void run(final Unit unit, final Boolean institutionUnit) {
        check(RolePredicates.MANAGER_PREDICATE);

        if (unit.isPlanetUnit()) {
            RootDomainObject.getInstance().setEarthUnit(unit);

        } else if (institutionUnit) {
            RootDomainObject.getInstance().setInstitutionUnit(unit);

        } else if (!institutionUnit) {
            RootDomainObject.getInstance().setExternalInstitutionUnit(unit);
        }
    }
}