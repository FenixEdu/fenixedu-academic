package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

public class ResultParticipationCreationBean implements Serializable {
    private DomainReference<Result> result;
    private DomainReference<Unit> organization;
    private DomainReference<Person> person;
    private ResultParticipationRole resultParticipationRole;
    private String personName;
    private String organizationName;
    private boolean beanExternal;
    
    public ResultParticipationCreationBean(Result result) {
        this.result = new DomainReference<Result>(result);
        this.resultParticipationRole = ResultParticipationRole.getDefaultResultParticipationRole();
        this.organization = null;
        this.person = null;
        this.personName = null;
        this.organizationName = null;
        this.beanExternal = false;
    }
    
    public boolean isBeanExternal() {
        return beanExternal;
    }
    public void setBeanExternal(boolean beanExternal) {
        this.beanExternal = beanExternal;
    }
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
    public Result getResult() {
        return (this.result == null) ? null : this.result.getObject();
    }
    public void setResult(Result result) {
        this.result = (result != null) ? new DomainReference<Result>(result) : null;
    }
    public ResultParticipationRole getResultParticipationRole() {
        return resultParticipationRole;
    }
    public void setResultParticipationRole(ResultParticipationRole resultParticipationRole) {
        this.resultParticipationRole = resultParticipationRole;
    }
    public ExternalPerson getExternalPerson() {
        return (this.person == null) ? null : this.person.getObject().getExternalPerson();
    }
    public void setExternalPerson(ExternalPerson externalPerson) {
        if (externalPerson == null) {
            this.person = null;
        }
        else {
            setPerson(externalPerson.getPerson());
        }
    }
}
