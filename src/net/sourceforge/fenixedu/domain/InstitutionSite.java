package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.RoleTypeGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class InstitutionSite extends InstitutionSite_Base {
    
    protected InstitutionSite() {
        super();
    }

    protected InstitutionSite(Unit unit) {
        super();
        
        setUnit(unit);
    }
    
    @Override
    public IGroup getOwner() {
        return new GroupUnion(
                new FixedSetGroup(getManagers()),
                new RoleTypeGroup(RoleType.MANAGER)
        );
    }

    /**
     * Initializes the site for the institution unit. If the site
     * was already initialized nothing is done.
     * 
     * @return the site associated with the institution unit
     */
    public static UnitSite initialize() {
        Unit unit = RootDomainObject.getInstance().getInstitutionUnit();
        
        if (unit.hasSite()) {
            return unit.getSite();
        }
        else {
            return new InstitutionSite(unit);
        }
    }
    
}
