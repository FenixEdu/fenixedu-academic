package net.sourceforge.fenixedu.domain.research.activity;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;

public class ArticleAssociation extends ArticleAssociation_Base {
    
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
