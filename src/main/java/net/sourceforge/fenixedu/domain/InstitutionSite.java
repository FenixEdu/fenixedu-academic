package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

public class InstitutionSite extends InstitutionSite_Base {

    protected InstitutionSite() {
        super();
    }

    protected InstitutionSite(Unit unit) {
        super();

        setUnit(unit);
    }

    @Override
    public Group getOwner() {
        RoleType roleType = RoleType.MANAGER;
        return UserGroup.of(Person.convertToUsers(getManagers())).or(RoleGroup.get(roleType));
    }

    /**
     * Initializes the site for the institution unit. If the site was already
     * initialized nothing is done.
     * 
     * @return the site associated with the institution unit
     */
    public static UnitSite initialize() {
        Unit unit = Bennu.getInstance().getInstitutionUnit();

        if (unit.hasSite()) {
            return unit.getSite();
        } else {
            return new InstitutionSite(unit);
        }
    }

}
