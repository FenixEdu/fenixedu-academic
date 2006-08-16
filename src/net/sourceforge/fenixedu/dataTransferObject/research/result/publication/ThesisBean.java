package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis.ThesisType;

public class ThesisBean extends ResultPublicationBean implements Serializable{
    private String address;
    private Integer numberPages;
    private String language;
    private ThesisType thesisType;
    
    public ThesisBean() {
    }
    
    public ThesisBean(Thesis thesis) {
        this.fillCommonFields(thesis);
        this.setPublicationType(ResultPublicationType.Thesis);

        this.setThesisType(thesis.getThesisType());
        this.setAddress(thesis.getAddress());
        this.setNumberPages(thesis.getNumberPages());
        this.setLanguage(thesis.getLanguage());
    }
    
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public Integer getNumberPages() {
        return numberPages;
    }
    public void setNumberPages(Integer numberPages) {
        this.numberPages = numberPages;
    }
    public ThesisType getThesisType() {
        return thesisType;
    }
    public void setThesisType(ThesisType thesisType) {
        this.thesisType = thesisType;
    }
}
