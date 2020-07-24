/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto.accounting.postingRule;

import java.io.Serializable;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.dto.accounting.PaymentsBean;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

abstract public class CreatePostingRuleBean implements Serializable, PaymentsBean {

    static private final long serialVersionUID = -7035156267101347173L;

    private ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

    private DateTime startDate;

    private LocalDate startLocalDate;

    private ServiceAgreementTemplate serviceAgreementTemplate;

    private Class<? extends PostingRule> rule;

    public CreatePostingRuleBean(final ServiceAgreementTemplate serviceAgreementTemplate) {
        this(new DateTime(), serviceAgreementTemplate);
    }

    public CreatePostingRuleBean(final DateTime startDate, final ServiceAgreementTemplate serviceAgreementTemplate) {
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

    protected CreatePostingRuleBean() {}

    public DateTime getStartDate() {
        return startDate;
    }

    public LocalDate getStartLocalDate() {
        return startLocalDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public void setStartLocalDate(LocalDate startLocalDate) {
        this.startLocalDate = startLocalDate;
        if (startLocalDate == null) {
            this.setStartDate(null);
        } else {
            this.setStartDate(startLocalDate.toDateTimeAtStartOfDay());
        }
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
