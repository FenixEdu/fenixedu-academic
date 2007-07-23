package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.ScientificCouncilSite;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.accessControl.PersonsInFunctionGroup;
import net.sourceforge.fenixedu.domain.accessControl.ScientificCouncilMembersGroup;
import net.sourceforge.fenixedu.domain.accessControl.WebSiteManagersGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class ScientificCouncilUnit extends ScientificCouncilUnit_Base {
    
    private static final String COORDINATION_COMMITTEE_NAME = "Comissão Coordenadora";
	private static final String COORDINATION_COMMITTEE_MEMBER_NAME = "Membro";	

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
    	
    	Function function = getCoordinationCommitteeMembersFunction();
    	if (function != null) {
    		groups.add(new PersonsInFunctionGroup(function));
    	}

    	return groups;
    }
    
    private Function getCoordinationCommitteeMembersFunction() {
    	Unit subUnit = getSubUnitWithName(COORDINATION_COMMITTEE_NAME);
    	
    	if (subUnit == null) {
    		return null;
    	}
    	else {
    		return getFunctionWithName(subUnit, COORDINATION_COMMITTEE_MEMBER_NAME);
    	}
	}

	private Unit getSubUnitWithName(String name) {
		for (Unit sub : getSubUnits()) {
			if (sub.getName().equals(name)) {
				return sub;
			}
		}
		
		return null;
	}

	private Function getFunctionWithName(Unit unit, String name) {
		for (Function function : unit.getFunctions()) {
			if (function.getName().equals(name)) {
				return function;
			}
		}
		
		return null;
	}
    
    @Override
    public Collection<Person> getPossibleGroupMembers() {
    	return Role.getRoleByRoleType(RoleType.SCIENTIFIC_COUNCIL).getAssociatedPersons();
    }

    @Override
    protected ScientificCouncilSite createSite() {
    	return new ScientificCouncilSite(this);
    }
}
