package net.sourceforge.fenixedu.domain.research.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class JournalIssue extends JournalIssue_Base implements ParticipationsInterface {

    static {
        getRelationJournalIssueScientificJournal().addListener(new RelationAdapter<ScientificJournal, JournalIssue>() {

            @Override
            public void afterRemove(ScientificJournal journal, JournalIssue issue) {
                super.afterRemove(journal, issue);
                if (issue != null && journal != null && !journal.hasAnyParticipations() && !journal.hasAnyJournalIssues()) {
                    journal.delete();
                }
            }

        });

    }

    public JournalIssue(ScientificJournal journal) {
        super();
        this.setRootDomainObject(RootDomainObject.getInstance());
        this.setScientificJournal(journal);
    }

    @Override
    public void addArticleAssociations(ArticleAssociation articleAssociations) {
        if (!containsArticle(articleAssociations.getArticle())) {
            super.addArticleAssociations(articleAssociations);
        } else {
            throw new DomainException("error.articleAlreadyAssociated");
        }
    }

    public boolean containsArticle(Article article) {
        for (ArticleAssociation association : this.getArticleAssociations()) {
            if (association.getArticle().equals(article)) {
                return true;
            }
        }
        return false;
    }

    public Set<Person> getPeopleWhoHaveAssociatedArticles() {
        Set<Person> people = new HashSet<Person>();
        for (ArticleAssociation association : this.getArticleAssociations()) {
            people.add(association.getCreator());
        }
        return people;
    }

    public List<Article> getArticles() {
        List<Article> articles = new ArrayList<Article>();
        for (ArticleAssociation association : this.getArticleAssociations()) {
            articles.add(association.getArticle());
        }
        return articles;
    }

    public void sweep() {
        if (!hasAnyParticipations() && !hasAnyArticleAssociations()) {
            delete();
        }
    }

    public void delete() {
        for (; !this.getArticleAssociations().isEmpty(); this.getArticleAssociations().iterator().next().delete()) {
            ;
        }
        for (; !this.getParticipations().isEmpty(); this.getParticipations().get(0).delete()) {
            ;
        }
        setScientificJournal(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Override
    public boolean canBeEditedByUser(Person person) {
        Set<Person> people = getPeopleWhoHaveAssociatedArticles();
        people.addAll(getPeopleWhoHaveParticipations());
        return people.size() == 1 && people.contains(person);
    }

    public Set<Person> getPeopleWhoHaveParticipations() {
        Set<Person> people = new HashSet<Person>();
        for (JournalIssueParticipation participation : getParticipations()) {
            if (participation.getParty().isPerson()) {
                people.add((Person) participation.getParty());
            }
        }
        return people;
    }

    @Override
    public boolean canBeEditedByCurrentUser() {
        return canBeEditedByUser(AccessControl.getPerson());
    }

    public ResearchActivityStage getStage() {
        return getScientificJournal().getStage();
    }

    @Override
    public List<JournalIssueParticipation> getParticipationsFor(Party party) {
        List<JournalIssueParticipation> participations = new ArrayList<JournalIssueParticipation>();
        for (JournalIssueParticipation participation : getParticipations()) {
            if (participation.getParty().equals(party)) {
                participations.add(participation);
            }
        }
        return participations;
    }

    public ScopeType getLocationType() {
        return getScientificJournal().getLocationType();
    }

    public String getNameWithScientificJournal() {
        return this.getScientificJournal().getName() + " - " + this.getVolume() + " (" + this.getNumber() + ")";
    }

    public String getPublisher() {
        return this.getScientificJournal().getPublisher();
    }

    @Override
    public void addUniqueParticipation(Participation participation) {
        if (participation instanceof JournalIssueParticipation) {
            JournalIssueParticipation journalIssueParticipation = (JournalIssueParticipation) participation;
            for (JournalIssueParticipation journalIssueParticipation2 : getParticipationsSet()) {
                if (journalIssueParticipation2.getParty().equals(journalIssueParticipation.getParty())
                        && journalIssueParticipation2.getRole().equals(journalIssueParticipation.getRole())) {
                    return;
                }
            }
            addParticipations(journalIssueParticipation);
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.ArticleAssociation> getArticleAssociations() {
        return getArticleAssociationsSet();
    }

    @Deprecated
    public boolean hasAnyArticleAssociations() {
        return !getArticleAssociationsSet().isEmpty();
    }

    @Override
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.research.activity.JournalIssueParticipation> getParticipations() {
        return getParticipationsSet();
    }

    @Deprecated
    public boolean hasAnyParticipations() {
        return !getParticipationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

    @Deprecated
    public boolean hasSpecialIssueComment() {
        return getSpecialIssueComment() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasUrl() {
        return getUrl() != null;
    }

    @Deprecated
    public boolean hasVolume() {
        return getVolume() != null;
    }

    @Deprecated
    public boolean hasNumber() {
        return getNumber() != null;
    }

    @Deprecated
    public boolean hasScientificJournal() {
        return getScientificJournal() != null;
    }

    @Deprecated
    public boolean hasSpecialIssue() {
        return getSpecialIssue() != null;
    }

    @Deprecated
    public boolean hasMonth() {
        return getMonth() != null;
    }

}
