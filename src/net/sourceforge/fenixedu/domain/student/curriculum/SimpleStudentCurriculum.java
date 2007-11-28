package net.sourceforge.fenixedu.domain.student.curriculum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.CreditsInAnySecundaryArea;
import net.sourceforge.fenixedu.domain.CreditsInScientificArea;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;

public class SimpleStudentCurriculum extends StudentCurriculumBase {

    public SimpleStudentCurriculum(final Registration registration, ExecutionYear executionYear) {
	super(registration, executionYear);
    }

    @Override
    final public Collection<ICurriculumEntry> getCurriculumEntries() {
        final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan();
        if (studentCurricularPlan == null) {
            return null;
        }

        final List<ICurriculumEntry> result = new ArrayList<ICurriculumEntry>();
        Collections.sort(result, ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);

        addCurricularEnrolments(result, studentCurricularPlan, getApprovedEnrolments());
        addNotNeedToEnrols(result, studentCurricularPlan);
        addDismissals(result, studentCurricularPlan);
        addCredits(result, studentCurricularPlan);

        return result;
    }

    @Override
    final protected EnrolmentSet getApprovedEnrolments() {
	final EnrolmentSet result = new EnrolmentSet(getExecutionYear());
	getRegistration().addApprovedEnrolments(result);
        return result;
    }

    final private void addCurricularEnrolments(final Collection<ICurriculumEntry> curriculumEntries,
	    final StudentCurricularPlan studentCurricularPlan, final EnrolmentSet approvedEnrolments) {
        for (final Enrolment enrolment : approvedEnrolments) {
            curriculumEntries.add(new NotInDegreeCurriculumCurriculumEntry(enrolment));
        }
    }

    final private void addNotNeedToEnrols(Collection<ICurriculumEntry> curriculumEntries, StudentCurricularPlan studentCurricularPlan) {
        for (final NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse : studentCurricularPlan.getNotNeedToEnrollCurricularCoursesSet()) {
            final CurricularCourse curricularCourse = notNeedToEnrollInCurricularCourse.getCurricularCourse();
            curriculumEntries.add(new NotNeedToEnrolCurriculumEntry(curricularCourse, notNeedToEnrollInCurricularCourse));
        }
    }
    
    final private void addCredits(Collection<ICurriculumEntry> curriculumEntries, StudentCurricularPlan studentCurricularPlan) {
	for (final CreditsInAnySecundaryArea creditsInAnySecundaryArea : studentCurricularPlan.getCreditsInAnySecundaryAreasSet()) {
	    curriculumEntries.add(new CreditsInAnySecundaryAreaCurriculumEntry(creditsInAnySecundaryArea));
	}
	
	for (CreditsInScientificArea creditsInScientificArea : studentCurricularPlan.getCreditsInScientificAreasSet()) {
	    curriculumEntries.add(new CreditsInScientificAreaCurriculumEntry(creditsInScientificArea));
	}
    }
    
    final private void addDismissals(Collection<ICurriculumEntry> curriculumEntries, StudentCurricularPlan studentCurricularPlan) {
	for (final Dismissal dismissal : studentCurricularPlan.getDismissals()) {
            curriculumEntries.add(new DismissalEntry(dismissal));
        }	
    }

}
