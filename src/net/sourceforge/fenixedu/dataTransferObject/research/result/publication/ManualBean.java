package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;

public class ManualBean extends ResultPublicationBean implements Serializable{
    private String address;
    private String edition;
    
    public ManualBean() {
    }

    public ManualBean(Manual manual) {
        this.fillCommonFields(manual);
        this.setPublicationType(ResultPublicationType.Book);
        this.setPublicationType(ResultPublicationType.Manual);

        this.setAddress(manual.getAddress());
        this.setEdition(manual.getEdition());
    }
    
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getEdition() {
        return edition;
    }
    public void setEdition(String edition) {
        this.edition = edition;
    }
}
