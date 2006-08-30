package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ConferenceArticlesBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InproceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ProceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;

public abstract class ResultPublicationService extends Service {

    protected Unit getPublisher(ResultPublicationBean publicationBean) {
	Unit publisher = publicationBean.getPublisher();
	if ((publisher == null) && (publicationBean.getPublisherName() != null)
		&& (publicationBean.getPublisherName().length() > 0))
	    publisher = Unit.createNewExternalInstitution(publicationBean.getPublisherName());
	return publisher;
    }

    protected Unit getOrganization(ResultPublicationBean publicationBean) {
	Unit organization = publicationBean.getOrganization();
	if ((organization == null) && (publicationBean.getOrganizationName() != null)
		&& (publicationBean.getOrganizationName().length() > 0))
	    organization = Unit.createNewExternalInstitution(publicationBean.getOrganizationName());
	return organization;
    }
    
    protected Event getEventFromBean(ConferenceArticlesBean bean) {
	Event event = bean.getEvent();
	if (event == null) {
	    event = new Event(bean.getEventEndDate(), bean.getEventStartDate(), bean.getEventLocal(),
		    bean.getEventFee(), bean.getEventType(), bean.getEventName());
	}
	return event;
    }

    protected ResultPublication getResultPublication(ResultPublicationBean bean)
	    throws FenixServiceException {
	if ((bean == null) || (bean.getIdInternal() == null))
	    throw new FenixServiceException();

	final ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(bean
		.getIdInternal());
	if (publication == null)
	    throw new FenixServiceException();
	return publication;
    }
}
