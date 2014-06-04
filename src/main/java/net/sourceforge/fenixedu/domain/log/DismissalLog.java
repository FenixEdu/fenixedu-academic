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
package net.sourceforge.fenixedu.domain.log;

import java.math.BigDecimal;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.EnrolmentAction;

import org.fenixedu.bennu.core.i18n.BundleUtil;

public class DismissalLog extends DismissalLog_Base {

    protected DismissalLog() {
        super();
    }

    public DismissalLog(final EnrolmentAction action, final Registration registration, final CurricularCourse curricularCourse,
            final Credits credits, final ExecutionSemester executionSemester, final String who) {
        this();
        init(action, registration, curricularCourse, executionSemester, who);
        setCredits(BigDecimal.valueOf(credits.getGivenCredits()));
        setSourceDescription(buildSourceDescription(credits));
    }

    protected String buildSourceDescription(final Credits credits) {
        final StringBuilder result = new StringBuilder();
        final Iterator<IEnrolment> enrolments = credits.getIEnrolments().iterator();
        while (enrolments.hasNext()) {
            result.append(enrolments.next().getName().getContent());
            result.append(enrolments.hasNext() ? ", " : "");
        }
        return result.toString();
    }

    @Override
    public String getDescription() {
        final StringBuilder description = new StringBuilder();
        description.append(getLabel()).append(": ");
        if (hasDegreeModule()) {
            description.append(getDegreeModuleName());
        }
        if (hasCredits()) {
            description.append(" ; ").append(getCredits().toString());
        }
        if (hasSourceDescription()) {
            description.append(" ; ").append(getSourceDescription());
        }
        return description.toString();
    }

    private boolean hasCredits() {
        return getCredits() != null;
    }

    private boolean hasSourceDescription() {
        return getSourceDescription() != null && !getSourceDescription().isEmpty();
    }

    protected String getDegreeModuleName() {
        return ((CurricularCourse) getDegreeModule()).getName(getExecutionPeriod());
    }

    protected String getLabel() {
        return BundleUtil.getString(Bundle.APPLICATION, "label.dismissal");
    }

}
