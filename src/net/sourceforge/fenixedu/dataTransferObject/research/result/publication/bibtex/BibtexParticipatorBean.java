package net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationCreationBean.ParticipationType;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import bibtex.dom.BibtexPerson;

public class BibtexParticipatorBean implements Serializable {

    private String bibtexPerson;

    private DomainReference<Person> person;

    private String personName;

    private ResultParticipationRole personRole;

    private DomainReference<Unit> organization;

    private String organizationName;

    private String activeSchema;

    private boolean createExternalPerson = false;

    private boolean participatorProcessed = false;

    private List<ParticipatorBean> personsFound = new ArrayList<ParticipatorBean>();

    private ParticipatorBean personChosen;

    private ParticipationType personType;
    
    private ParticipationType organizationType;
       
    public BibtexParticipatorBean() {
    	setPerson(null);
    	setOrganization(null);
    	setPersonType(ParticipationType.INTERNAL);
    	setOrganizationType(ParticipationType.INTERNAL);
    }
    /**/

	public Unit getOrganization() {
	return (this.organization == null) ? null : this.organization.getObject();
    }

    public void setOrganization(Unit organization) {
	this.organization = (organization != null) ? new DomainReference<Unit>(organization) : null;
    }

    public String getOrganizationName() {
	return organizationName;
    }

    public void setOrganizationName(String organizationName) {
	this.organizationName = organizationName;
    }

    public Person getPerson() {
    	return person.getObject();
    }

    public void setPerson(Person person) {
    	this.person = new DomainReference<Person>(person);
    }

    public String getPersonName() {
	return personName;
    }

    public void setPersonName(String personName) {
	this.personName = personName;
    }

    public ResultParticipationRole getPersonRole() {
	return personRole;
    }

    public void setPersonRole(ResultParticipationRole personRole) {
	this.personRole = personRole;
    }

    public String getBibtexPerson() {
	return bibtexPerson;
    }

    public void setBibtexPerson(String bibtexPerson) {
	this.bibtexPerson = bibtexPerson;
    }

    public String getActiveSchema() {
	return activeSchema;
    }

    public void setActiveSchema(String activeSchema) {
	this.activeSchema = activeSchema;
    }

    public boolean isCreateExternalPerson() {
	return createExternalPerson;
    }

    public void setCreateExternalPerson(boolean createExternalPerson) {
	this.createExternalPerson = createExternalPerson;
    }

    public String getPersonDescription() {
	String personDescription = "";
	if (person != null)
	    personDescription = personDescription + getPerson().getName();
	else {
	    personDescription = personDescription + personName;
	    if ((organizationName != null) && (organizationName.length() > 0))
		personDescription = personDescription + " - " + organizationName;
	}
	return personDescription;
    }

    public void setBibtexPerson(BibtexPerson bp) {
	String fullName = "";
	if (bp.getFirst() != null)
	    fullName = fullName + bp.getFirst();
	if (bp.getPreLast() != null)
	    fullName = fullName + ' ' + bp.getPreLast();
	if (bp.getLast() != null)
	    fullName = fullName + ' ' + bp.getLast();
	if (bp.getLineage() != null)
	    fullName = fullName + ' ' + bp.getLineage();
	setBibtexPerson(fullName);
    }

    public List<ParticipatorBean> getPersonsFound() {
	return personsFound;
    }

    public void setPersonsFound(List<ParticipatorBean> personsFound) {
	this.personsFound = personsFound;
    }

    public ParticipatorBean getPersonChosen() {
	return personChosen;
    }

    public void setPersonChosen(ParticipatorBean personChosen) {
	this.personChosen = personChosen;
    }

    public boolean isParticipatorProcessed() {
	return participatorProcessed;
    }

    public void setParticipatorProcessed(boolean participatorProcessed) {
	this.participatorProcessed = participatorProcessed;
    }

	public ParticipationType getPersonType() {
		return personType;
	}

	public void setPersonType(ParticipationType personType) {
		this.personType = personType;
	}

	public ParticipationType getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(ParticipationType organizationType) {
		this.organizationType = organizationType;
	}
	
	public boolean isExternalUnit() {
		return this.organizationType.equals(ParticipationType.EXTERNAL);
	}
	
	public boolean isExternalPerson() {
		return this.personType.equals(ParticipationType.EXTERNAL);
	}
	
	public void reset() {
		this.setParticipatorProcessed(false);
		this.setActiveSchema("bibtex.participator");
		this.setPersonType(ParticipationType.INTERNAL);
		this.setOrganizationType(ParticipationType.INTERNAL);
		this.setPerson(null);
		this.setOrganization(null);
		this.setOrganizationName(null);
		this.setPersonName(null);
	}
	
	public String getName() {
		if(getPerson()!=null) { 
			return getPerson().getName(); 
		}
		else return personName;
	}
}
