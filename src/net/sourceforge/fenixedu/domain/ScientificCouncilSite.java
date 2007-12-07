package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.RoleTypeGroup;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificCouncilUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.util.MultiLanguageString;

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
		for (Content content : RootDomainObject.getInstance().getContents()) {
			if (content instanceof ScientificCouncilSite) {
				return (ScientificCouncilSite) content;
			}
		}
		
		return null;
    }
    
    @Override
    public MultiLanguageString getName() {
	return MultiLanguageString.i18n().add("pt","").finish();
    }

    @Override
    public void appendReversePathPart(final StringBuilder stringBuilder) {
    }

}
