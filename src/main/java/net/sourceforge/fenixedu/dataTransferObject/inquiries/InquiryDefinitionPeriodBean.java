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
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.CurricularCourseInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriodType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryTemplate;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class InquiryDefinitionPeriodBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private InquiryTemplate inquiryTemplate;
    private DateTime begin;
    private DateTime end;
    private MultiLanguageString message;
    private Locale selectedLanguage;
    private Locale previousLanguage;
    private ExecutionSemester executionPeriod;
    private InquiryResponsePeriodType responsePeriodType;
    private boolean changedLanguage = false;

    public InquiryDefinitionPeriodBean() {
        setMessage(new MultiLanguageString());
        setSelectedLanguage(MultiLanguageString.pt);
        setResponsePeriodType(InquiryResponsePeriodType.STUDENT);
    }

    public String getDisplayMessage() {
        if (getMessage() == null) {
            return null;
        }
        return getMessage().getContent(getSelectedLanguage());
    }

    public void setDisplayMessage(String displayMessage) {
        if (getMessage() == null) {
            setMessage(new MultiLanguageString());
        }
        if (isChangedLanguage()) {
            setMessage(getMessage().with(getPreviousLanguage(), displayMessage));
        } else {
            setMessage(getMessage().with(getSelectedLanguage(), displayMessage));
        }
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

    public void setSelectedLanguage(Locale selectedLanguage) {
        setChangedLanguage(false);
        this.previousLanguage = this.selectedLanguage;
        if (this.selectedLanguage != null && this.selectedLanguage != selectedLanguage) {
            setChangedLanguage(true);
        }
        this.selectedLanguage = selectedLanguage;
    }

    public Locale getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setChangedLanguage(boolean changedLanguage) {
        this.changedLanguage = changedLanguage;
    }

    public boolean isChangedLanguage() {
        return changedLanguage;
    }

    public Locale getPreviousLanguage() {
        return previousLanguage;
    }

    public void setInquiryTemplate(InquiryTemplate inquiryTemplate) {
        this.inquiryTemplate = inquiryTemplate;
    }

    public InquiryTemplate getInquiryTemplate() {
        return inquiryTemplate;
    }

    public static class InquiryPeriodLanguageProvider implements DataProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            List<Locale> languages = new ArrayList<Locale>();
            languages.add(MultiLanguageString.pt);
            languages.add(MultiLanguageString.en);
            return languages;
        }

        @Override
        public Converter getConverter() {
            return new Converter() {
                @Override
                public Object convert(Class type, Object value) {
                    return new Locale.Builder().setLanguageTag((String) value).build();

                }
            };
        }
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
