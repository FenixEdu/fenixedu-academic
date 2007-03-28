package net.sourceforge.fenixedu.domain.research.activity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;


public class ScientificJournal extends ScientificJournal_Base {
    
    public  ScientificJournal() {
        super();
        setStage(ResearchActivityStage.DRAFT);
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public ScientificJournal(String name, ResearchActivityLocationType type) {
	this();
	this.setName(name);
	this.setLocationType(type);
    }

   
    public void sweep() {
	if (!hasAnyParticipations() && !hasAnyJournalIssues()) {
	    delete();
	}
    }
    
    public void delete(){
        for(;!this.getJournalIssues().isEmpty();this.getJournalIssues().get(0).delete());
        for(;!this.getParticipations().isEmpty();this.getParticipations().get(0).delete())
        removeRootDomainObject();
	super.deleteDomainObject();
    }
    
    public List<ResearchActivityParticipationRole> getAllowedRoles(){
    	return ResearchActivityParticipationRole.getAllScientificJournalParticipationRoles();
    }

    @Override
    public void removeJournalIssues(JournalIssue journalIssues) {
	super.removeJournalIssues(journalIssues);
	if(getJournalIssues().isEmpty() && getParticipations().isEmpty()) {
	    delete();
	}
    }
    
    public List<Article> getArticles() {
	List<Article> articles = new ArrayList<Article>();
	for(JournalIssue issue : getJournalIssues()) {
	    articles.addAll(issue.getArticles());
	}
	return articles;
    }
    
    public List<ScientificJournalParticipation> getParticipationsFor(Party party) {
	List<ScientificJournalParticipation> participations = new ArrayList<ScientificJournalParticipation>();
	for(ScientificJournalParticipation participation : getParticipations()) {
	    if(participation.getParty().equals(party)) {
		participations.add(participation);
	    }
	}
	return participations;
    }
}
