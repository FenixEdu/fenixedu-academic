package net.sourceforge.fenixedu.domain.research.activity;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ScientificJournalParticipation extends ScientificJournalParticipation_Base {

    public ScientificJournalParticipation(Party party, ResearchActivityParticipationRole role, ScientificJournal journal,
            MultiLanguageString roleMessage, YearMonthDay begin, YearMonthDay end) {
        super();
        if (alreadyHasParticipation(party, role, journal)) {
            throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
        }
        super.setParty(party);
        super.setRole(role);
        super.setScientificJournal(journal);
        super.setRoleMessage(roleMessage);
        super.setBeginDate(begin);
        super.setEndDate(end);
    }

    private boolean alreadyHasParticipation(Party party, ResearchActivityParticipationRole role, ScientificJournal journal) {
        for (ScientificJournalParticipation participation : party.getScientificJournalParticipations()) {
            if (participation.match(party, role, journal)) {
                return true;
            }
        }
        return false;
    }

    public boolean match(Party party, ResearchActivityParticipationRole role, ScientificJournal journal) {
        return this.getParty().equals(party) && this.getRole().equals(role) && this.getScientificJournal().equals(journal);
    }

    @Override
    public void setScientificJournal(ScientificJournal journal) {
        if (alreadyHasParticipation(getParty(), getRole(), journal)) {
            throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
        }
        super.setScientificJournal(journal);
    }

    @Override
    public void setParty(Party party) {
        if (alreadyHasParticipation(party, getRole(), getScientificJournal())) {
            throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
        }
        super.setParty(party);
    }

    @Override
    public void setRole(ResearchActivityParticipationRole role) {
        if (alreadyHasParticipation(getParty(), role, getScientificJournal())) {
            throw new DomainException("error.researcher.ResearchActivityParticipation.participation.exists");
        }
        super.setRole(role);
    }

    @Override
    public List<ResearchActivityParticipationRole> getAllowedRoles() {
        return ResearchActivityParticipationRole.getAllScientificJournalParticipationRoles();
    }

    @Override
    public String getParticipationName() {
        return this.getScientificJournal().getName();
    }

    @Override
    public boolean isLastParticipation() {
        return this.getScientificJournal().getParticipationsFor(this.getParty()).size() == 1;
    }

    @Override
    public void delete() {
        setScientificJournal(null);
        super.delete();
    }

    @Override
    public Integer getCivilYear() {
        return null;
    }

    @Override
    public boolean scopeMatches(ScopeType type) {
        return this.getScientificJournal().getLocationType().equals(type);
    }
    @Deprecated
    public boolean hasEndDate() {
        return getEndDate() != null;
    }

    @Deprecated
    public boolean hasBeginDate() {
        return getBeginDate() != null;
    }

    @Deprecated
    public boolean hasScientificJournal() {
        return getScientificJournal() != null;
    }

}
