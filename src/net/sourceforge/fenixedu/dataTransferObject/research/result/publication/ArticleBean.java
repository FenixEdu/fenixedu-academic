package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication.ScopeType;

public class ArticleBean extends ResultPublicationBean implements Serializable{
    private String volume;
    private String language;
    private ScopeType scope;
    private String journal;
    private String number;
    private Integer firstPage;
    private Integer lastPage;
    private Integer issn;
    
    private ArticleBean() {
	this.setPublicationType(ResultPublicationType.Article);
	this.setActiveSchema("result.publication.create."+this.getPublicationType());
	this.setParticipationSchema("resultParticipation.simple");
    }

    public ArticleBean(Article article) {
	this();
	if(article!=null) {
            this.fillCommonFields(article);
            this.setPublicationType(ResultPublicationType.Article);
            this.setJournal(article.getJournal());
            this.setVolume(article.getVolume());
            this.setNumber(article.getNumber());
            this.setFirstPage(article.getFirstPage());
            this.setLastPage(article.getLastPage());
            this.setIssn(article.getIssn());
            this.setLanguage(article.getLanguage());
            this.setScope(article.getScope());
	}
    }
    
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public ScopeType getScope() {
        return scope;
    }
    public void setScope(ScopeType scope) {
        this.scope = scope;
    }
    public String getVolume() {
        return volume;
    }
    public void setVolume(String volume) {
        this.volume = volume;
    }
    public Integer getFirstPage() {
        return firstPage;
    }
    public void setFirstPage(Integer firstPage) {
        this.firstPage = firstPage;
    }
    public Integer getIssn() {
        return issn;
    }
    public void setIssn(Integer issn) {
        this.issn = issn;
    }
    public String getJournal() {
        return journal;
    }
    public void setJournal(String journal) {
        this.journal = journal;
    }
    public Integer getLastPage() {
        return lastPage;
    }
    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
}
