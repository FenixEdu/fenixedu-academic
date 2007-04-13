package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.person.PersonName;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

public class ResultParticipationCreationBean implements Serializable {
    private DomainReference<ResearchResult> result;

    private DomainReference<UnitName> organizationNameObject;

    private DomainReference<PersonName> participator;

    private ResultParticipationRole role;

    private String participatorName;

    private String organizationName;

    private ParticipationType personParticipationType;

    private ParticipationType unitParticipationType;

    private String email;

    public ResultParticipationCreationBean(ResearchResult result) {
	setResult(new DomainReference<ResearchResult>(result));
	setRole(ResultParticipationRole.getDefaultRole());
	setOrganizationNameObject(null);
	setOrganizationName(null);
	setParticipator(null);
	setParticipatorName(null);
	setPersonParticipationType(ParticipationType.INTERNAL);
	setUnitParticipationType(ParticipationType.INTERNAL);
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public boolean isBeanExternal() {
	return getPersonParticipationType().equals(ParticipationType.EXTERNAL);
    }

    public boolean isUnitExternal() {
	return getUnitParticipationType().equals(ParticipationType.EXTERNAL);
    }

    public void setOrganization(Unit unit) {
	setOrganizationNameObject((unit != null) ? unit.getUnitName() : null);
    }

    public Unit getOrganization() {
	UnitName unitName = this.getOrganizationNameObject();
	return (unitName == null) ? null : unitName.getUnit();
    }

    public String getOrganizationName() {
	return organizationName;
    }

    public void setOrganizationName(String organizationName) {
	this.organizationName = organizationName;
    }

    public PersonName getParticipator() {
	return participator.getObject();
    }

    public void setParticipator(PersonName participator) {
	this.participator = new DomainReference<PersonName>(participator);
    }

    public String getParticipatorName() {
	return participatorName;
    }

    public void setParticipatorName(String participatorName) {
	this.participatorName = participatorName;
    }

    public ResearchResult getResult() {
	return result.getObject();
    }

    public void setResult(DomainReference<ResearchResult> result) {
	this.result = result;
    }

    public ResultParticipationRole getRole() {
	return role;
    }

    public void setRole(ResultParticipationRole role) {
	this.role = role;
    }

    public ExternalContract getExternalPerson() {
	return (this.participator == null) ? null : this.participator.getObject().getPerson()
		.getExternalPerson();
    }

    public void setExternalPerson(ExternalContract externalPerson) {
	if (externalPerson == null) {
	    this.participator = null;
	} else {
	    setParticipator(externalPerson.getPerson().getPersonName());
	}
    }

    public ParticipationType getPersonParticipationType() {
	return personParticipationType;
    }

    public void setPersonParticipationType(ParticipationType participationType) {
	this.personParticipationType = participationType;
    }

    public boolean hasOrganization() {
	return (getOrganization() != null || (getOrganizationName() != null && getOrganizationName().length() > 0));
    }

    public static enum ParticipationType {
	INTERNAL("Internal"), EXTERNAL("External");

	private String type;

	private ParticipationType(String type) {
	    this.type = type;
	}

	public String getType() {
	    return type;
	}
    }

    public ParticipationType getUnitParticipationType() {
	return unitParticipationType;
    }

    public void setUnitParticipationType(ParticipationType unitParticipationType) {
	this.unitParticipationType = unitParticipationType;
    }

    public void reset() {
	this.setParticipator(null);
	this.setParticipatorName(null);
	this.setOrganizationName(null);
	this.setOrganizationNameObject(null);
    }

    public UnitName getOrganizationNameObject() {
	return organizationNameObject.getObject();
    }

    public void setOrganizationNameObject(UnitName name) {
	organizationNameObject = new DomainReference<UnitName>(name);
    }

}
