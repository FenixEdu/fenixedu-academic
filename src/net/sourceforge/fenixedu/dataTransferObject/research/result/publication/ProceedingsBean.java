package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;

public class ProceedingsBean extends ConferenceArticlesBean implements Serializable{
    private String address;

    public ProceedingsBean() {
    }

    public ProceedingsBean(Proceedings proceedings) {
        this.fillCommonFields(proceedings);
        this.setPublicationType(ResultPublicationType.Proceedings);

        this.setAddress(proceedings.getAddress());
        this.setEvent(proceedings.getEvent());
    }
    
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
