package net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;

import org.joda.time.DateTime;

abstract public class CreatePostingRuleBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7035156267101347173L;

    private DateTime startDate;

    private DomainReference<ServiceAgreementTemplate> serviceAgreementTemplate;

    protected CreatePostingRuleBean(final ServiceAgreementTemplate serviceAgreementTemplate) {
	this(new DateTime(), serviceAgreementTemplate);
    }

    protected CreatePostingRuleBean(final DateTime startDate, final ServiceAgreementTemplate serviceAgreementTemplate) {
	setStartDate(startDate);
	setServiceAgreementTemplate(serviceAgreementTemplate);
    }

    public DateTime getStartDate() {
	return startDate;
    }

    public void setStartDate(DateTime startDate) {
	this.startDate = startDate;
    }

    public ServiceAgreementTemplate getServiceAgreementTemplate() {
	return (this.serviceAgreementTemplate != null) ? this.serviceAgreementTemplate.getObject() : null;
    }

    public void setServiceAgreementTemplate(ServiceAgreementTemplate serviceAgreementTemplate) {
	this.serviceAgreementTemplate = (serviceAgreementTemplate != null) ? new DomainReference<ServiceAgreementTemplate>(
		serviceAgreementTemplate) : null;
    }

}
