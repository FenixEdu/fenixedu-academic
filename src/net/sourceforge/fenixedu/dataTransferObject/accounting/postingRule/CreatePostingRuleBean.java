package net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;

import org.joda.time.DateTime;

abstract public class CreatePostingRuleBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7035156267101347173L;

    private DateTime startDate;

    private DomainReference<ServiceAgreementTemplate> serviceAgreementTemplate;

    private Class<? extends PostingRule> rule;

    public CreatePostingRuleBean(final ServiceAgreementTemplate serviceAgreementTemplate) {
	this(new DateTime(), serviceAgreementTemplate);
    }

    public CreatePostingRuleBean(final DateTime startDate, final ServiceAgreementTemplate serviceAgreementTemplate) {
	this();
	setStartDate(startDate);
	setServiceAgreementTemplate(serviceAgreementTemplate);
    }

    protected CreatePostingRuleBean() {
	this.startDate = new DateTime();
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

    public Class<? extends PostingRule> getRule() {
	return rule;
    }

    public void setRule(Class<? extends PostingRule> rule) {
	this.rule = rule;
    }

}
