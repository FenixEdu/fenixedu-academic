package net.sourceforge.fenixedu.domain.research.activity;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EventParticipation extends EventParticipation_Base {

    public EventParticipation(Party party, ResearchActivityParticipationRole role, ResearchEvent event,
            MultiLanguageString roleMessage) {
        super();
        if (alreadyHasParticipation(party, role, event)) {
            throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
        }
        super.setParty(party);
        super.setRole(role);
        super.setEvent(event);
        super.setRoleMessage(roleMessage);
    }

    private boolean alreadyHasParticipation(Party party, ResearchActivityParticipationRole role, ResearchEvent event) {
        for (EventParticipation participation : party.getEventParticipations()) {
            if (participation.match(party, role, event)) {
                return true;
            }
        }
        return false;
    }

    public boolean match(Party party, ResearchActivityParticipationRole role, ResearchEvent event) {
        return this.getParty().equals(party) && this.getRole().equals(role) && this.getEvent().equals(event);
    }

    @Override
    public void setEvent(ResearchEvent event) {
        if (alreadyHasParticipation(getParty(), getRole(), event)) {
            throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
        }
        super.setEvent(event);
    }

    @Override
    public void setParty(Party party) {
        if (alreadyHasParticipation(party, getRole(), getEvent())) {
            throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
        }
        super.setParty(party);
    }

    @Override
    public void setRole(ResearchActivityParticipationRole role) {
        if (alreadyHasParticipation(getParty(), role, getEvent())) {
            throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
        }
        super.setRole(role);
    }

    @Override
    public List<ResearchActivityParticipationRole> getAllowedRoles() {
        return ResearchActivityParticipationRole.getAllEventParticipationRoles();
    }

    @Override
    public String getParticipationName() {
        return this.getEvent().getName();
    }

    @Override
    public boolean isLastParticipation() {
        return this.getEvent().getParticipationsFor(this.getParty()).size() == 1;
    }

    @Override
    public void delete() {
        setEvent(null);
        super.delete();
    }

    @Override
    public Integer getCivilYear() {
        return null;
    }

    @Override
    public boolean scopeMatches(ScopeType type) {
        return this.getEvent().getLocationType().equals(type);
    }
    @Deprecated
    public boolean hasEvent() {
        return getEvent() != null;
    }

}
