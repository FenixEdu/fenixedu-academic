package net.sourceforge.fenixedu.domain.research.activity;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class ArticleAssociation extends ArticleAssociation_Base {

    static {
        getRelationJournalIssueArticleAssociation().addListener(new RelationAdapter<JournalIssue, ArticleAssociation>() {

            @Override
            public void afterRemove(JournalIssue issue, ArticleAssociation association) {
                super.afterRemove(issue, association);

                if (issue != null && association != null && !issue.hasAnyArticleAssociations() && !issue.hasAnyParticipations()) {
                    issue.delete();
                }
            }

        });
    }

    public ArticleAssociation(JournalIssue journalIssue, Article article, Person creator) {
        super();
        this.setRootDomainObject(RootDomainObject.getInstance());
        this.setJournalIssue(journalIssue);
        this.setArticle(article);
        this.setCreator(creator);
    }

    public void delete() {
        setJournalIssue(null);
        setArticle(null);
        setCreator(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasJournalIssue() {
        return getJournalIssue() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCreator() {
        return getCreator() != null;
    }

    @Deprecated
    public boolean hasArticle() {
        return getArticle() != null;
    }

}
