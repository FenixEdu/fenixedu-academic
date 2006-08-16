package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public abstract class ResultPublicationService extends Service {

    protected Unit getPublisher(ResultPublicationBean publicationBean)
    {
        Unit publisher = publicationBean.getPublisher();
        if ((publisher == null) && (publicationBean.getPublisherName() != null) && (publicationBean.getPublisherName().length() > 0))
            publisher = Unit.createNewExternalInstitution(publicationBean.getPublisherName());
        return publisher;
    }
    
    protected Unit getOrganization(ResultPublicationBean publicationBean)
    {
        Unit organization = publicationBean.getOrganization();
        if ((organization == null) && (publicationBean.getOrganizationName() != null) && (publicationBean.getOrganizationName().length() > 0))
                organization = Unit.createNewExternalInstitution(publicationBean.getOrganizationName());
        return organization;
    }
    
}
