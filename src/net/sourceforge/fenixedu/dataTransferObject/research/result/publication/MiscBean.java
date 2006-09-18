package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.result.publication.Misc;

public class MiscBean extends ResultPublicationBean implements Serializable{
    private String address;
    private Integer numberPages;
    private String language;
    private String howPublished;
    private String otherPublicationType;
    
    private MiscBean() {
	this.setPublicationType(ResultPublicationType.Misc);
	this.setActiveSchema("result.publication.create."+this.getPublicationType());
	this.setParticipationSchema("resultParticipation.simple");
    }
    
    public MiscBean(Misc misc) {
	this();
	if(misc!=null) {
	    this.fillCommonFields(misc);
	    this.setPublicationType(ResultPublicationType.Misc);
	    this.setHowPublished(misc.getHowPublished());
	    this.setOtherPublicationType(misc.getOtherPublicationType());
	    this.setNumberPages(misc.getNumberPages());
	    this.setLanguage(misc.getLanguage());
	    this.setAddress(misc.getAddress());
	}
    }
    
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getHowPublished() {
        return howPublished;
    }
    public void setHowPublished(String howPublished) {
        this.howPublished = howPublished;
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
    public String getOtherPublicationType() {
        return otherPublicationType;
    }
    public void setOtherPublicationType(String otherPublicationType) {
        this.otherPublicationType = otherPublicationType;
    }
}
