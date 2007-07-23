package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.RoleTypeGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificCouncilUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class ScientificCouncilSite extends ScientificCouncilSite_Base {
    
	public ScientificCouncilSite(ScientificCouncilUnit scientificCouncil) {
        super();
        
        setUnit(scientificCouncil);
    }
    
    @Override
    public IGroup getOwner() {
    	return new GroupUnion(
    			new RoleTypeGroup(RoleType.SCIENTIFIC_COUNCIL), 
    			new FixedSetGroup(getManagers())
		);
    }

	/**
	 * This method searchs for the first instance of a ScientificCouncilSite.
	 * 
	 * @return the site associated with the Scientific Council or
	 *         <code>null</code> if there is no such site
	 */
    public static ScientificCouncilSite getSite() {
		for (Site site : RootDomainObject.getInstance().getSites()) {
			if (site instanceof ScientificCouncilSite) {
				return (ScientificCouncilSite) site;
			}
		}
		
		return null;
    }

}
