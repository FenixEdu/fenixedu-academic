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

    private DomainReference<Person> participator;

    private ResultParticipationRole resultParticipationRole;

    private String participatorName;

    private String organizationName;

    private boolean beanExternal;

    public ResultParticipationCreationBean(Result result) {
	setResult(new DomainReference<Result>(result));
	setResultParticipationRole(ResultParticipationRole.getDefaultRole());
	setOrganization(null);
	setOrganizationName(null);
	setParticipator(null);
	setParticipatorName(null);
	setBeanExternal(false);
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

    public void setOrganization(Unit organization) {
	this.organization = (organization != null) ? new DomainReference<Unit>(organization) : null;
    }

    public String getOrganizationName() {
	return organizationName;
    }

    public void setOrganizationName(String organizationName) {
	this.organizationName = organizationName;
    }

    public Person getParticipator() {
	return (this.participator == null) ? null : this.participator.getObject();
    }

    public void setParticipator(Person participator) {
	this.participator = (participator != null) ? new DomainReference<Person>(participator) : null;
    }

    public String getParticipatorName() {
	return participatorName;
    }

    public void setParticipatorName(String participatorName) {
	this.participatorName = participatorName;
    }

    public Result getResult() {
	return result.getObject();
    }

    public void setResult(DomainReference<Result> result) {
	this.result = result;
    }

    public ResultParticipationRole getResultParticipationRole() {
	return resultParticipationRole;
    }

    public void setResultParticipationRole(ResultParticipationRole resultParticipationRole) {
	this.resultParticipationRole = resultParticipationRole;
    }

    public ExternalPerson getExternalPerson() {
	return (this.participator == null) ? null : this.participator.getObject().getExternalPerson();
    }

    public void setExternalPerson(ExternalPerson externalPerson) {
	if (externalPerson == null) {
	    this.participator = null;
	} else {
	    setParticipator(externalPerson.getPerson());
	}
    }
}
