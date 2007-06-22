package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.DegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.RoleTypeGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitClassification;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;

/**
 * Specific site instance that is associated with the unit that represents the
 * Pedagogical Council of the institution.
 * <p>
 * There should exist only one instance of this site (matching the single unit
 * that represents the council). Nevertheless that is not verified.
 * 
 * @author cfgi
 */
public class PedagogicalCouncilSite extends PedagogicalCouncilSite_Base {
    
    public PedagogicalCouncilSite() {
        super();
    }
    
    @Override
    public IGroup getOwner() {
    	return new GroupUnion(
    			new RoleTypeGroup(RoleType.PEDAGOGICAL_COUNCIL), 
    			new FixedSetGroup(getManagers())
		);
    }

    @Override
    public List<IGroup> getContextualPermissionGroups() {
    	List<IGroup> list = super.getContextualPermissionGroups();
    	
    	list.add(new DegreeCoordinatorsGroup());
    	
		return list;
    }
    
    /**
	 * This method searchs for an instance of a PedagogicalCouncilSite and
	 * checks that the unit associated with that site has the correct
	 * {@link UnitClassification#PEDAGOGICAL_COUNCIL} classification. However,
	 * if the situation where more than one site exists is not detected.
	 * 
	 * @return the site associated with the Pedagogical Council or
	 *         <code>null</code> if there is no such site
	 */
    public static PedagogicalCouncilSite getSite() {
		for (Site site : RootDomainObject.getInstance().getSites()) {
			if (site instanceof PedagogicalCouncilSite) {
				PedagogicalCouncilSite pedagogicalCouncilSite = (PedagogicalCouncilSite) site;
				
				UnitClassification classification = pedagogicalCouncilSite.getUnit().getClassification();
				if (classification == null || !classification.equals(UnitClassification.PEDAGOGICAL_COUNCIL)) {
					throw new DomainException("error.site.pedagogicalCouncil.unit.badClassification");
				}
				
				return pedagogicalCouncilSite;
			}
		}
		
		return null;
    }
}
