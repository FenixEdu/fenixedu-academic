package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivity;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;

public class ResearchActivityParticipantBean implements Serializable {
	private DomainReference<ResearchActivity> activity;
	private DomainReference<Person> person;
    private DomainReference<Unit> unit;
    private ResearchActivityParticipationRole role;
    private String personName;
    private String unitName;
    private ParticipationType participationType;
    
    public ResearchActivityParticipantBean() {
    	setPerson(null);
    	setUnit(null);
    	setResearchActivity(null);
    }
    
    public ResearchActivity getResearchActivity() {
		return activity.getObject();
	}

	public void setResearchActivity(ResearchActivity activity) {
		this.activity = new DomainReference<ResearchActivity>(activity);
	}

	public ResearchActivityParticipationRole getRole() {
        return role;
    }

    public void setRole(ResearchActivityParticipationRole role) {
        this.role = role;
    }
    
    public Person getPerson() {
        return this.person.getObject();
    }
    
    public void setPerson(Person person) {
        this.person = new DomainReference<Person>(person);
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String name) {
        this.personName = name;
    }


    public void setUnitName(String name) {
        this.unitName = name;
    }
    
    public String getUnitName() {
    	return unitName;
    }
    
    public Unit getUnit() {
        return this.unit.getObject();
    }
    
    public void setUnit (Unit organization) {
        this.unit = new DomainReference<Unit>(organization);
    }
    
    public ExternalContract getExternalPerson() {
    	if(!person.isNullReference())
    		return person.getObject().getExternalPerson();
    	else
    		return new DomainReference<ExternalContract>(null).getObject();
    }

    public void setExternalPerson(ExternalContract externalPerson) {
        if (externalPerson == null) {
            this.person = new DomainReference<Person>(null);
        }
        else {
            setPerson(externalPerson.getPerson());
        }
    }
    
    public ParticipationType getParticipationType() {
		return participationType;
	}

	public void setParticipationType(ParticipationType participationType) {
		this.participationType = participationType;
	}
	
	public boolean isExternalParticipation() {
		return this.participationType.equals(ParticipationType.EXTERNAL_PARTICIPANT);
	}
    
    public static enum ParticipationType{
    	INTERNAL_PARTICIPANT ("Internal"),
    	EXTERNAL_PARTICIPANT ("External");
    	
    	private String type;
    	private ParticipationType(String type) {
    		this.type = type;
    	}
    	
    	public String getType() {
    		return type;
    	}
    }
}
