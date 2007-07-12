package net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;

public class CreateReferenceBean implements Serializable {

    private String year;
    private String title;
    private String authors;
    private String reference;
    private String url;
    private BibliographicReferenceType type;
    
    public String getAuthors() {
        return authors;
    }
    public void setAuthors(String authors) {
        this.authors = authors;
    }
    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public BibliographicReferenceType getType() {
        return type;
    }
    public void setType(BibliographicReferenceType type) {
        this.type = type;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    
    
}
