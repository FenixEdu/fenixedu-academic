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
package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class DelegateInquiryTemplate extends DelegateInquiryTemplate_Base {

    public DelegateInquiryTemplate(DateTime begin, DateTime end) {
        super();
        init(begin, end);
    }

    public static DelegateInquiryTemplate getCurrentTemplate() {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof DelegateInquiryTemplate && inquiryTemplate.isOpen()) {
                return (DelegateInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public static DelegateInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof DelegateInquiryTemplate && executionSemester == inquiryTemplate.getExecutionPeriod()) {
                return (DelegateInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }
}
