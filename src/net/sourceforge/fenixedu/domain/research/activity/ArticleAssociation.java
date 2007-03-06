package net.sourceforge.fenixedu.domain.research.activity;

import dml.runtime.RelationAdapter;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;

public class ArticleAssociation extends ArticleAssociation_Base {
    
    static {
	JournalIssueArticleAssociation.addListener(new RelationAdapter<ArticleAssociation, JournalIssue>() {

	    @Override
	    public void afterRemove(ArticleAssociation association, JournalIssue issue) {
		super.afterRemove(association, issue);
		
		if (issue != null && association != null && !issue.hasAnyArticleAssociations()) {
		    issue.delete();
		}
	    }
	    
	});
    }
    
    public  ArticleAssociation(JournalIssue journalIssue, Article article, Person creator) {
        super();
        this.setRootDomainObject(RootDomainObject.getInstance());
        this.setJournalIssue(journalIssue);
        this.setArticle(article);
        this.setCreator(creator);
    }

    public void delete() {
	removeJournalIssue();
	removeArticle();
	removeCreator();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
    
}
