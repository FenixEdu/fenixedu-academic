package net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import bibtex.dom.BibtexPerson;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

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
	return (this.person == null) ? null : this.person.getObject();
    }

    public void setPerson(Person person) {
	this.person = (person != null) ? new DomainReference<Person>(person) : null;
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
}
