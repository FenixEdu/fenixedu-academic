package net.sourceforge.fenixedu.domain.research.activity;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

import org.joda.time.YearMonthDay;

public class Cooperation extends Cooperation_Base {
    
    public  Cooperation() {
        super();
        setOjbConcreteClass(getClass().getName());
    }
    
    public Cooperation(ResearchActivityParticipationRole role, Person person, String name, CooperationType type, Unit unit,
    		YearMonthDay startDate, YearMonthDay endDate) {
    	this();
    	if(alreadyHasThatCooperation(role, person, name, type, unit)) {
    		throw new DomainException("error.researcher.ResearchActivityParticipation.cooperation.exists");
    	}   	
        setName(name);
        setCooperationType(type);
        setUnit(unit);  
		setStartDate(startDate);
		setEndDate(endDate);
    }
    
	private boolean alreadyHasThatCooperation(ResearchActivityParticipationRole role, Person person, String name, CooperationType type, Unit unit) {
		for(Participation participation : person.getParticipations()) {
			if(participation.getResearchActivity() instanceof Cooperation) {
				Cooperation cooperation = (Cooperation)participation.getResearchActivity();
	    		if(participation.getRole().equals(role) && cooperation.getCooperationType().equals(type) && cooperation.getUnit().equals(unit)) {
	    			return true;
	    		}				
			}
    	}
		return false;
	}   
    
    /**
     * This method is responsible for deleting the object and all its references, particularly
     * Participations
     */
    public void delete(){
        for (;this.hasAnyParticipations(); getParticipations().get(0).delete());
        super.delete();
    }
    
    @Override
    public List<ResearchActivityParticipationRole> getAllowedRoles(){
    	return this.getCooperationType().getParticipationRoles();
    }
}
