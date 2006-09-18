package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;

public class ProceedingsBean extends ConferenceArticlesBean implements Serializable{
    private String address;

    private ProceedingsBean() {
	this.setPublicationType(ResultPublicationType.Proceedings);
	this.setActiveSchema("result.publication.create."+this.getPublicationType());
	this.setParticipationSchema("resultParticipation.simple");
    }

    public ProceedingsBean(Proceedings proceedings) {
	this();
	if(proceedings!=null) {
            this.fillCommonFields(proceedings);
            this.setPublicationType(ResultPublicationType.Proceedings);
            this.setAddress(proceedings.getAddress());
            this.setEvent(proceedings.getEvent());
	}
    }
    
    @Override
    public void setCreateEvent(Boolean createEvent) {
	this.setActiveSchema("result.publication.create.ProceedingsAndEvent");
        super.setCreateEvent(createEvent);
    }
    
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
