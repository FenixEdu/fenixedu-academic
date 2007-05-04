package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteLink;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class UnitSitePredicates {

    public static final AccessControlPredicate<UnitSite> managers = new AccessControlPredicate<UnitSite>() {

        public boolean evaluate(UnitSite site) {
            Person person = AccessControl.getPerson();
            return person.hasRole(RoleType.MANAGER) || site.hasManagers(person);
        }
        
    };

    public static final AccessControlPredicate<UnitSiteLink> linkSiteManagers = new AccessControlPredicate<UnitSiteLink>() {
        
        public boolean evaluate(UnitSiteLink link) {
            UnitSite site = nullOr(link.getTopUnitSite(), link.getFooterUnitSite());
			return site == null ? true : managers.evaluate(site);
        }
        
    };
    
    private static <T> T nullOr(T ... args) {
        for (T value : args) {
            if (value != null) {
                return value;
            }
        }
        
        return null;
    }
}
