package net.sourceforge.fenixedu.domain.student.curriculum;

import java.util.Collection;
import java.util.HashSet;

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
    public Collection<CurriculumEntry> getCurriculumEntries(final ExecutionYear executionYear) {
        final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(executionYear);
        if (studentCurricularPlan == null) {
            return null;
        }

        final EnrolmentSet approvedEnrolments = getApprovedEnrolments(executionYear);

        final Collection<CurriculumEntry> curriculumEntries = new HashSet<CurriculumEntry>();

        addCurricularEnrolments(curriculumEntries, studentCurricularPlan, approvedEnrolments);

        addNotNeedToEnrols(curriculumEntries, studentCurricularPlan);
        addDismissals(curriculumEntries, studentCurricularPlan);

        return curriculumEntries;
    }

    protected EnrolmentSet getApprovedEnrolments(final ExecutionYear executionYear) {
	final EnrolmentSet approvedEnrolments = new EnrolmentSet(executionYear);
	
	final Registration registration = getRegistration();
        
        registration.addApprovedEnrolments(approvedEnrolments);
        return approvedEnrolments;
    }

    private void addCurricularEnrolments(final Collection<CurriculumEntry> curriculumEntries,
	    final StudentCurricularPlan studentCurricularPlan, final EnrolmentSet approvedEnrolments) {
        for (final Enrolment enrolment : approvedEnrolments) {
            curriculumEntries.add(new NotInDegreeCurriculumCurriculumEntry(enrolment));
        }
    }

    private void addNotNeedToEnrols(Collection<CurriculumEntry> curriculumEntries, StudentCurricularPlan studentCurricularPlan) {
        for (final NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse : studentCurricularPlan.getNotNeedToEnrollCurricularCoursesSet()) {
            final CurricularCourse curricularCourse = notNeedToEnrollInCurricularCourse.getCurricularCourse();
            curriculumEntries.add(new NotNeedToEnrolCurriculumEntry(curricularCourse, notNeedToEnrollInCurricularCourse));
        }
    }
    
    private void addDismissals(Collection<CurriculumEntry> curriculumEntries, StudentCurricularPlan studentCurricularPlan) {
	for (final Dismissal dismissal : studentCurricularPlan.getDismissals()) {
            curriculumEntries.add(new DismissalEntry(dismissal));
        }	
    }

}
