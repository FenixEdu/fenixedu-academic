package net.sourceforge.fenixedu.domain.research.activity;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class ScientificJournal extends ScientificJournal_Base implements ParticipationsInterface {

    public ScientificJournal() {
        super();
        setStage(ResearchActivityStage.DRAFT);
        setRootDomainObject(Bennu.getInstance());
    }

    public ScientificJournal(String name, ScopeType type) {
        this();
        this.setName(name);
        this.setLocationType(type);
    }

    public void sweep() {
        if (!hasAnyParticipations() && !hasAnyJournalIssues()) {
            delete();
        }
    }

    public void delete() {
        for (; !this.getJournalIssues().isEmpty(); this.getJournalIssues().iterator().next().delete()) {
            ;
        }
        for (; !this.getParticipations().isEmpty(); this.getParticipations().iterator().next().delete()) {
            ;
        }
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public List<ResearchActivityParticipationRole> getAllowedRoles() {
        return ResearchActivityParticipationRole.getAllScientificJournalParticipationRoles();
    }

    @Override
    public void removeJournalIssues(JournalIssue journalIssues) {
        super.removeJournalIssues(journalIssues);
        if (getJournalIssues().isEmpty() && getParticipations().isEmpty()) {
            delete();
        }
    }

    public List<Article> getArticles() {
        List<Article> articles = new ArrayList<Article>();
        for (JournalIssue issue : getJournalIssues()) {
            articles.addAll(issue.getArticles());
        }
        return articles;
    }

    @Override
    public List<ScientificJournalParticipation> getParticipationsFor(Party party) {
        List<ScientificJournalParticipation> participations = new ArrayList<ScientificJournalParticipation>();
        for (ScientificJournalParticipation participation : getParticipations()) {
            if (participation.getParty().equals(party)) {
                participations.add(participation);
            }
        }
        return participations;
    }

    @Override
    public boolean canBeEditedByUser(Person person) {
        for (JournalIssue issue : getJournalIssues()) {
            if (!issue.canBeEditedByUser(person)) {
                return false;
            }
        }
        return getParticipations().size() == getParticipationsFor(person).size();

    }

    @Override
    public boolean canBeEditedByCurrentUser() {
        return canBeEditedByUser(AccessControl.getPerson());
    }

    @Override
    public void addUniqueParticipation(Participation participation) {
        if (participation instanceof ScientificJournalParticipation) {
            ScientificJournalParticipation scientificJournalParticipation = (ScientificJournalParticipation) participation;
            for (ScientificJournalParticipation scientificJournalParticipation2 : getParticipationsSet()) {
                if (scientificJournalParticipation2.getParty().equals(scientificJournalParticipation.getParty())
                        && scientificJournalParticipation2.getRole().equals(scientificJournalParticipation.getRole())) {
                    return;
                }
            }
            addParticipations(scientificJournalParticipation);
        }
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.ScientificJournalParticipation> getParticipations() {
        return getParticipationsSet();
    }

    @Deprecated
    public boolean hasAnyParticipations() {
        return !getParticipationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.JournalIssue> getJournalIssues() {
        return getJournalIssuesSet();
    }

    @Deprecated
    public boolean hasAnyJournalIssues() {
        return !getJournalIssuesSet().isEmpty();
    }

    @Deprecated
    public boolean hasPublisher() {
        return getPublisher() != null;
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasUrl() {
        return getUrl() != null;
    }

    @Deprecated
    public boolean hasLocationType() {
        return getLocationType() != null;
    }

    @Deprecated
    public boolean hasIssn() {
        return getIssn() != null;
    }

    @Deprecated
    public boolean hasStage() {
        return getStage() != null;
    }

}
