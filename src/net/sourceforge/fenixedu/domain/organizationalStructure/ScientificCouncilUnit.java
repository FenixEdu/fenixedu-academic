package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.ScientificCouncilMembersGroup;
import net.sourceforge.fenixedu.domain.accessControl.WebSiteManagersGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class ScientificCouncilUnit extends ScientificCouncilUnit_Base {
    
    protected ScientificCouncilUnit() {
        super();
        super.setType(PartyTypeEnum.SCIENTIFIC_COUNCIL);
    }
    
    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }
    
    @Override
    protected List<IGroup> getDefaultGroups() {
    	List<IGroup> groups = super.getDefaultGroups();
    	
    	groups.add(new ScientificCouncilMembersGroup());
    	groups.add(new WebSiteManagersGroup(getSite()));
    	
		return groups;
    }
    
    @Override
    public Collection<Person> getPossibleGroupMembers() {
    	return Role.getRoleByRoleType(RoleType.SCIENTIFIC_COUNCIL).getAssociatedPersons();
    }

}
