package net.sourceforge.fenixedu.domain.log;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import net.sourceforge.fenixedu.util.EnrolmentAction;
import pt.utl.ist.fenix.tools.util.i18n.Language;

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
        return ResourceBundle.getBundle("resources.ApplicationResources", Language.getDefaultLocale()).getString(
                "label.dismissal");
    }

}
