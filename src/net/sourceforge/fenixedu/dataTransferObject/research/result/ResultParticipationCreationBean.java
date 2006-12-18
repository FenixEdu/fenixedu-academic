package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

public class ResultParticipationCreationBean implements Serializable {
	private DomainReference<ResearchResult> result;

	private DomainReference<Unit> organization;

	private DomainReference<Person> participator;

	private ResultParticipationRole role;

	private String participatorName;

	private String organizationName;

	private ParticipationType personParticipationType;

	private ParticipationType unitParticipationType;
	
	public ResultParticipationCreationBean(ResearchResult result) {
		setResult(new DomainReference<ResearchResult>(result));
		setRole(ResultParticipationRole.getDefaultRole());
		setOrganization(null);
		setOrganizationName(null);
		setParticipator(null);
		setParticipatorName(null);
		setPersonParticipationType(ParticipationType.INTERNAL);
		setUnitParticipationType(ParticipationType.INTERNAL);
	}

	public boolean isBeanExternal() {
		return getPersonParticipationType().equals(ParticipationType.EXTERNAL);
	}

	public boolean isUnitExternal() {
		return getUnitParticipationType().equals(ParticipationType.EXTERNAL);
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
		return participator.getObject();
	}

	public void setParticipator(Person participator) {
		this.participator = new DomainReference<Person>(participator);
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
		return (this.participator == null) ? null : this.participator.getObject().getExternalPerson();
	}

	public void setExternalPerson(ExternalContract externalPerson) {
		if (externalPerson == null) {
			this.participator = null;
		} else {
			setParticipator(externalPerson.getPerson());
		}
	}

	public ParticipationType getPersonParticipationType() {
		return personParticipationType;
	}

	public void setPersonParticipationType(ParticipationType participationType) {
		this.personParticipationType = participationType;
	}

	public boolean hasOrganization() {
		return (getOrganization()!=null || (getOrganizationName()!=null && getOrganizationName().length()>0)) ;
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

	
}
