package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.domain.research.activity.ParticipationsInterface;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class ParticipantBean implements Serializable {

    private Person person;

    private Unit unit;

    private ResearchActivityParticipationRole role;

    private String personName;

    private String unitName;

    private ParticipationType participationType;

    private MultiLanguageString roleMessage;

    private YearMonthDay beginDate;

    private YearMonthDay endDate;

    public YearMonthDay getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(YearMonthDay beginDate) {
        this.beginDate = beginDate;
    }

    public YearMonthDay getEndDate() {
        return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
        this.endDate = endDate;
    }

    public MultiLanguageString getRoleMessage() {
        return roleMessage;
    }

    public void setRoleMessage(MultiLanguageString roleMessage) {
        this.roleMessage = roleMessage;
    }

    public ParticipantBean() {
        setPerson(null);
        setUnit(null);
    }

    public ResearchActivityParticipationRole getRole() {
        return role;
    }

    public void setRole(ResearchActivityParticipationRole role) {
        this.role = role;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
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
        return this.unit;
    }

    public void setUnit(Unit organization) {
        this.unit = organization;
    }

    public ExternalContract getExternalPerson() {
        return person != null ? person.getExternalContract() : null;
    }

    public void setExternalPerson(ExternalContract externalPerson) {
        if (externalPerson == null) {
            this.person = null;
        } else {
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

    public static enum ParticipationType {
        INTERNAL_PARTICIPANT("Internal"), EXTERNAL_PARTICIPANT("External");

        private String type;

        private ParticipationType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public abstract List<ResearchActivityParticipationRole> getAllowedRoles();

    public abstract DomainObject getActivity();

    public static ParticipantBean getParticipantBean(ParticipationsInterface objectWithParticipations) {

        ParticipantBean bean = null;
        if (objectWithParticipations instanceof ResearchEvent) {
            bean = new EventParticipantBean();
            ((EventParticipantBean) bean).setEvent((ResearchEvent) objectWithParticipations);
        }
        if (objectWithParticipations instanceof EventEdition) {
            bean = new EventEditionParticipantBean();
            ((EventEditionParticipantBean) bean).setEventEdition((EventEdition) objectWithParticipations);
        }
        if (objectWithParticipations instanceof ScientificJournal) {
            bean = new ScientificJournalParticipantBean();
            ((ScientificJournalParticipantBean) bean).setScientificJournal((ScientificJournal) objectWithParticipations);
        }
        if (objectWithParticipations instanceof JournalIssue) {
            bean = new JournalIssueParticipantBean();
            ((JournalIssueParticipantBean) bean).setJournalIssue((JournalIssue) objectWithParticipations);
        }
        if (objectWithParticipations instanceof Cooperation) {
            bean = new CooperationParticipantBean();
            ((CooperationParticipantBean) bean).setCooperation((Cooperation) objectWithParticipations);
        }
        return bean;
    }
}
