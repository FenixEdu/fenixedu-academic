package net.sourceforge.fenixedu.domain.student.curriculum;

import java.util.Collection;
import java.util.HashSet;

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

    public SimpleStudentCurriculum(final Registration registration) {
	super(registration);
    }

    @Override
    final public Collection<CurriculumEntry> getCurriculumEntries(final ExecutionYear executionYear) {
        final StudentCurricularPlan studentCurricularPlan = getRegistration().getStudentCurricularPlan(executionYear);
        if (studentCurricularPlan == null) {
            return null;
        }

        final Collection<CurriculumEntry> result = new HashSet<CurriculumEntry>();

        addCurricularEnrolments(result, studentCurricularPlan, getApprovedEnrolments(executionYear));
        addNotNeedToEnrols(result, studentCurricularPlan);
        addDismissals(result, studentCurricularPlan);
        addCredits(result, studentCurricularPlan);

        return result;
    }

    @Override
    final protected EnrolmentSet getApprovedEnrolments(final ExecutionYear executionYear) {
	final EnrolmentSet result = new EnrolmentSet(executionYear);
	getRegistration().addApprovedEnrolments(result);
        return result;
    }

    final private void addCurricularEnrolments(final Collection<CurriculumEntry> curriculumEntries,
	    final StudentCurricularPlan studentCurricularPlan, final EnrolmentSet approvedEnrolments) {
        for (final Enrolment enrolment : approvedEnrolments) {
            curriculumEntries.add(new NotInDegreeCurriculumCurriculumEntry(enrolment));
        }
    }

    final private void addNotNeedToEnrols(Collection<CurriculumEntry> curriculumEntries, StudentCurricularPlan studentCurricularPlan) {
        for (final NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse : studentCurricularPlan.getNotNeedToEnrollCurricularCoursesSet()) {
            final CurricularCourse curricularCourse = notNeedToEnrollInCurricularCourse.getCurricularCourse();
            curriculumEntries.add(new NotNeedToEnrolCurriculumEntry(curricularCourse, notNeedToEnrollInCurricularCourse));
        }
    }
    
    final private void addCredits(Collection<CurriculumEntry> curriculumEntries, StudentCurricularPlan studentCurricularPlan) {
	for (final CreditsInAnySecundaryArea creditsInAnySecundaryArea : studentCurricularPlan.getCreditsInAnySecundaryAreasSet()) {
	    curriculumEntries.add(new CreditsInAnySecundaryAreaCurriculumEntry(creditsInAnySecundaryArea));
	}
	
	for (CreditsInScientificArea creditsInScientificArea : studentCurricularPlan.getCreditsInScientificAreasSet()) {
	    curriculumEntries.add(new CreditsInScientificAreaCurriculumEntry(creditsInScientificArea));
	}
    }
    
    final private void addDismissals(Collection<CurriculumEntry> curriculumEntries, StudentCurricularPlan studentCurricularPlan) {
	for (final Dismissal dismissal : studentCurricularPlan.getDismissals()) {
            curriculumEntries.add(new DismissalEntry(dismissal));
        }	
    }

}
