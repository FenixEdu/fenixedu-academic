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
package org.fenixedu.academic.dto.inquiries;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.io.Serializable;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.inquiries.CurricularCourseInquiryTemplate;
import org.fenixedu.academic.domain.inquiries.InquiryResponsePeriodType;
import org.fenixedu.academic.domain.inquiries.InquiryTemplate;
import org.fenixedu.academic.domain.inquiries.StudentInquiryTemplate;
import org.fenixedu.academic.predicate.RolePredicates;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class InquiryDefinitionPeriodBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private InquiryTemplate inquiryTemplate;
    private DateTime begin;
    private DateTime end;
    private MultiLanguageString message;
    private ExecutionSemester executionPeriod;
    private InquiryResponsePeriodType responsePeriodType;
    private boolean changedLanguage = false;

    public InquiryDefinitionPeriodBean() {
        setMessage(new MultiLanguageString());
        setResponsePeriodType(InquiryResponsePeriodType.STUDENT);
    }

    public DateTime getBegin() {
        return begin;
    }

    public void setBegin(DateTime begin) {
        this.begin = begin;
    }

    public DateTime getEnd() {
        return end;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }

    public void setMessage(MultiLanguageString message) {
        this.message = message;
    }

    public MultiLanguageString getMessage() {
        return message;
    }

    public ExecutionSemester getExecutionPeriod() {
        return executionPeriod;
    }

    public void setExecutionPeriod(ExecutionSemester executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    public InquiryResponsePeriodType getResponsePeriodType() {
        return responsePeriodType;
    }

    public void setResponsePeriodType(InquiryResponsePeriodType responsePeriodType) {
        this.responsePeriodType = responsePeriodType;
    }

    public void setChangedLanguage(boolean changedLanguage) {
        this.changedLanguage = changedLanguage;
    }

    public boolean isChangedLanguage() {
        return changedLanguage;
    }

    public void setInquiryTemplate(InquiryTemplate inquiryTemplate) {
        this.inquiryTemplate = inquiryTemplate;
    }

    public InquiryTemplate getInquiryTemplate() {
        return inquiryTemplate;
    }

    @Atomic
    public void writePeriodAndMessage() {
        check(this, RolePredicates.GEP_PREDICATE);
        if (!getEnd().isAfter(getBegin())) {
            throw new DomainException("error.inquiry.endDateMustBeAfterBeginDate");
        }
        if (getInquiryTemplate() instanceof CurricularCourseInquiryTemplate) {
            for (StudentInquiryTemplate studentInquiryTemplate : StudentInquiryTemplate
                    .getInquiryTemplatesByExecutionPeriod(getInquiryTemplate().getExecutionPeriod())) {
                studentInquiryTemplate.setResponsePeriodBegin(getBegin());
                studentInquiryTemplate.setResponsePeriodEnd(getEnd());
            }
        } else {
            getInquiryTemplate().setResponsePeriodBegin(getBegin());
            getInquiryTemplate().setResponsePeriodEnd(getEnd());
        }
        getInquiryTemplate().setInquiryMessage(getMessage());
    }
}
