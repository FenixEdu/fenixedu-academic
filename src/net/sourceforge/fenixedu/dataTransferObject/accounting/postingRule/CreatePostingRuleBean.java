package net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.accounting.PaymentsBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;

import org.joda.time.DateTime;

abstract public class CreatePostingRuleBean implements Serializable, PaymentsBean {

    static private final long serialVersionUID = -7035156267101347173L;

    private ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

    private DateTime startDate;

    private ServiceAgreementTemplate serviceAgreementTemplate;

    private Class<? extends PostingRule> rule;

    public CreatePostingRuleBean(final ServiceAgreementTemplate serviceAgreementTemplate) {
        this(new DateTime(), serviceAgreementTemplate);
    }

    public CreatePostingRuleBean(final DateTime startDate, final ServiceAgreementTemplate serviceAgreementTemplate) {
        this();
        setStartDate(startDate);
        setServiceAgreementTemplate(serviceAgreementTemplate);
    }

    @Override
    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
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
        return this.serviceAgreementTemplate;
    }

    public void setServiceAgreementTemplate(ServiceAgreementTemplate serviceAgreementTemplate) {
        this.serviceAgreementTemplate = serviceAgreementTemplate;
    }

    public Class<? extends PostingRule> getRule() {
        return rule;
    }

    public void setRule(Class<? extends PostingRule> rule) {
        this.rule = rule;
    }

}
