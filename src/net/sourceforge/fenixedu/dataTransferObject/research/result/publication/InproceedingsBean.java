package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;

public class InproceedingsBean extends ConferenceArticlesBean implements Serializable{
    private String address;
    private Integer firstPage;
    private Integer lastPage;
    private String bookTitle;
    private String language;

    public InproceedingsBean() {
    }

    public InproceedingsBean(Inproceedings inproceedings) {
        this.fillCommonFields(inproceedings);
        this.setPublicationType(ResultPublicationType.Inproceedings);

        this.setBookTitle(inproceedings.getBookTitle());
        this.setAddress(inproceedings.getAddress());
        this.setFirstPage(inproceedings.getFirstPage());
        this.setLastPage(inproceedings.getLastPage());
        this.setLanguage(inproceedings.getLanguage());
        this.setEvent(inproceedings.getEvent());
    }
    
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getBookTitle() {
        return bookTitle;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public Integer getFirstPage() {
        return firstPage;
    }
    public void setFirstPage(Integer firstPage) {
        this.firstPage = firstPage;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public Integer getLastPage() {
        return lastPage;
    }
    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }
    
    
}
