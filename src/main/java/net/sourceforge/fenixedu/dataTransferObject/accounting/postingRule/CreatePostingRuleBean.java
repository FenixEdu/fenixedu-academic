/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
