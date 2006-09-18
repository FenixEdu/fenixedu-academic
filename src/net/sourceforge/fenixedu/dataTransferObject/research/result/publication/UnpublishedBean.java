package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.result.publication.Unpublished;

public class UnpublishedBean extends ResultPublicationBean implements Serializable{

    private UnpublishedBean() {
	this.setPublicationType(ResultPublicationType.Unpublished);
	this.setActiveSchema("result.publication.create."+this.getPublicationType());
	this.setParticipationSchema("resultParticipation.simple");
    }
    
    public UnpublishedBean(Unpublished unpublished) {
	this();
	if(unpublished!=null) {
	    this.fillCommonFields(unpublished);
	    this.setPublicationType(ResultPublicationType.Unpublished);
	}
    }
}
