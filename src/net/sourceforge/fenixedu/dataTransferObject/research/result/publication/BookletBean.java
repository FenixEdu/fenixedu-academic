package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.result.publication.Booklet;

public class BookletBean extends ResultPublicationBean implements Serializable{
    private String address;
    private String howPublished;
    
    private BookletBean() {
	this.setPublicationType(ResultPublicationType.Booklet);
	this.setActiveSchema("result.publication.create."+this.getPublicationType());
	this.setParticipationSchema("resultParticipation.simple");
    }
    
    public BookletBean(Booklet booklet) {
	this();
	if(booklet!=null) {
            this.fillCommonFields(booklet);
            this.setPublicationType(ResultPublicationType.Booklet);
            this.setHowPublished(booklet.getHowPublished());
            this.setAddress(booklet.getAddress());
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
}
