package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;


import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class SetRootUnit {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Atomic
    public static void run(final Unit unit, final Boolean institutionUnit) {

        if (unit.isPlanetUnit()) {
            RootDomainObject.getInstance().setEarthUnit(unit);

        } else if (institutionUnit) {
            RootDomainObject.getInstance().setInstitutionUnit(unit);

        } else if (!institutionUnit) {
            RootDomainObject.getInstance().setExternalInstitutionUnit(unit);
        }
    }
}