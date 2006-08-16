package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import net.sourceforge.fenixedu.domain.research.result.publication.Unpublished;

public class UnpublishedBean extends ResultPublicationBean implements Serializable{

    public UnpublishedBean() {
    }
    
    public UnpublishedBean(Unpublished unpublished) {
        this.fillCommonFields(unpublished);
        this.setPublicationType(ResultPublicationType.Unpublished);
    }
}
