package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

public class PublicationParticipatorBean implements Serializable {
    
    private DomainReference<Person> person;
    private String personName;
    private ResultParticipationRole resultParticipationRole;
    
    private DomainReference<Unit> organization;
    private String organizationName;
    

    public Unit getOrganization() {
        return (this.organization == null) ? null : this.organization.getObject();
    }
    public void setOrganization (Unit organization) {
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
    public ResultParticipationRole getResultParticipationRole() {
        return resultParticipationRole;
    }
    public void setResultParticipationRole(ResultParticipationRole resultParticipationRole) {
        this.resultParticipationRole = resultParticipationRole;
    }
}
