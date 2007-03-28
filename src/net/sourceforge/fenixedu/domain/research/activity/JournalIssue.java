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
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class JournalIssue extends JournalIssue_Base {

    public JournalIssue(ScientificJournal journal) {
	super();
	this.setRootDomainObject(RootDomainObject.getInstance());
	this.setScientificJournal(journal);
    }

    @Override
    public void addArticleAssociations(ArticleAssociation articleAssociations) {
	if(!containsArticle(articleAssociations.getArticle())) {
	    super.addArticleAssociations(articleAssociations);
	}
	else {
	    throw new DomainException("error.articleAlreadyAssociated");
	}
    }

    
    public boolean containsArticle(Article article) { 
	for(ArticleAssociation association : this.getArticleAssociations()) {
	    if(association.getArticle().equals(article)) return true;
	}
	return false;
    }
    
    public Set<Person> getPeopleWhoHaveAssociatedArticles() {
	Set<Person> people = new HashSet<Person> ();
	for(ArticleAssociation association : this.getArticleAssociations()) {
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
	for (; !this.getArticleAssociations().isEmpty(); this.getArticleAssociations().get(0).delete());
	
	removeScientificJournal();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public boolean canBeEditedByCurrentUser() {
	Set<Person> people = getPeopleWhoHaveAssociatedArticles(); 
	Person currentUser = AccessControl.getPerson();
	return people.size()==1 && people.contains(currentUser); 
    }
    
    public ResearchActivityStage getStage() {
	return getScientificJournal().getStage();
    }
    
    public List<JournalIssueParticipation> getParticipationsFor(Party party) {
	List<JournalIssueParticipation> participations = new ArrayList<JournalIssueParticipation>();
	for(JournalIssueParticipation participation : getParticipations()) {
	    if(participation.getParty().equals(party)) {
		participations.add(participation);
	    }
	}
	return participations;
    }
}
