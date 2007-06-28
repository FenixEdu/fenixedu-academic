package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.PedagogicalCouncilMembersGroup;
import net.sourceforge.fenixedu.domain.accessControl.WebSiteManagersGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class PedagogicalCouncilUnit extends PedagogicalCouncilUnit_Base {
    
    protected PedagogicalCouncilUnit() {
        super();
        super.setType(PartyTypeEnum.PEDAGOGICAL_COUNCIL);
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }
        
    @Override
    protected List<IGroup> getDefaultGroups() {
    	List<IGroup> groups = super.getDefaultGroups();
    	
    	groups.add(new PedagogicalCouncilMembersGroup());
    	groups.add(new WebSiteManagersGroup(getSite()));
    	
		return groups;
    }
    
    @Override
    public Collection<Person> getPossibleGroupMembers() {
    	return Role.getRoleByRoleType(RoleType.PEDAGOGICAL_COUNCIL).getAssociatedPersons();
    }
    
}
