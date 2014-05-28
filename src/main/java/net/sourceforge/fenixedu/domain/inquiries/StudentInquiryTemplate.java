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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.fenixedu.bennu.core.domain.Bennu;

public abstract class StudentInquiryTemplate extends StudentInquiryTemplate_Base {

    public static StudentInquiryTemplate getCurrentTemplate() {
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof StudentInquiryTemplate && inquiryTemplate.isOpen()) {
                return (StudentInquiryTemplate) inquiryTemplate;
            }
        }
        return null;
    }

    public static List<StudentInquiryTemplate> getInquiryTemplatesByExecutionPeriod(ExecutionSemester executioPeriod) {
        List<StudentInquiryTemplate> studentInquiryTemplates = new ArrayList<StudentInquiryTemplate>();
        final Collection<InquiryTemplate> inquiryTemplates = Bennu.getInstance().getInquiryTemplatesSet();
        for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
            if (inquiryTemplate instanceof StudentInquiryTemplate && inquiryTemplate.getExecutionPeriod() == executioPeriod) {
                studentInquiryTemplates.add((StudentInquiryTemplate) inquiryTemplate);
            }
        }
        return studentInquiryTemplates;
    }
}
