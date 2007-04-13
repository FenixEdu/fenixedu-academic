package net.sourceforge.fenixedu.domain.research.activity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.YearMonthDay;

public class Cooperation extends Cooperation_Base implements ParticipationsInterface {
    
    private Cooperation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public Cooperation(ResearchActivityParticipationRole role, Person person, String name, CooperationType type, Unit unit,
    		YearMonthDay startDate, YearMonthDay endDate) {
    	this();
        setName(name);
        setCooperationType(type);
        setUnit(unit);  
	setStartDate(startDate);
	setEndDate(endDate);
    }
       
    
    public void sweet() {
	if (!hasAnyParticipations()) {
	    delete();
	}
    }
    /**
     * This method is responsible for deleting the object and all its references, particularly
     * Participations
     */
    public void delete(){
        for (;this.hasAnyParticipations(); getParticipations().get(0).delete());
        removeRootDomainObject();
        super.deleteDomainObject();
    }
    
    public List<ResearchActivityParticipationRole> getAllowedRoles(){
    	return this.getCooperationType().getParticipationRoles();
    }
    
    public List<CooperationParticipation> getParticipationsFor(Party party) {
	List<CooperationParticipation> participations = new ArrayList<CooperationParticipation>();
	for(CooperationParticipation participation : getParticipations()) {
	    if(participation.getParty().equals(party)) {
		participations.add(participation);
	    }
	}
	return participations;
    }
    
    public boolean canBeEditedByUser(Person person) {
	return getParticipations().size() == getParticipationsFor(person).size();
    }
    
    public boolean canBeEditedByCurrentUser() {
	return canBeEditedByUser(AccessControl.getPerson());
    }
}
