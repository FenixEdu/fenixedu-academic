package net.sourceforge.fenixedu.domain.student.curriculum;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

public class DegreeCurricularPlanBasedStudentCurriculum extends StudentCurriculumBase {

    public DegreeCurricularPlanBasedStudentCurriculum(final Registration registration) {
	super(registration);
    }

    @Override
    public Collection<CurriculumEntry> getCurriculumEntries(final ExecutionYear executionYear) {
	final StudentCurricularPlan studentCurricularPlan = getRegistration().getStudentCurricularPlan(executionYear);
        if (studentCurricularPlan == null) {
            return null;
        }

        final EnrolmentSet approvedEnrolments = getApprovedEnrolments(executionYear);

        final Collection<CurriculumEntry> entries = new HashSet<CurriculumEntry>();

        addCurricularEnrolments(entries, studentCurricularPlan, executionYear, approvedEnrolments);

        addNotInDegreeCurriculumEnrolments(entries, studentCurricularPlan, approvedEnrolments);

        return entries;
    }

    protected EnrolmentSet getApprovedEnrolments(final ExecutionYear executionYear) {
	final EnrolmentSet approvedEnrolments = new EnrolmentSet(executionYear);
	
	final Registration registration = getRegistration();
	final Student student = registration.getStudent();
        
        student.addApprovedEnrolments(approvedEnrolments);
        return approvedEnrolments;
    }

    private void addCurricularEnrolments(final Collection<CurriculumEntry> curriculumEntries, final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionYear executionYear, final EnrolmentSet approvedEnrolments) {

        final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
            if (curricularCourse.isActive(executionYear)) {
        	final SimpleCurriculumEntry simpleEntry = getSimpleEntry(curriculumEntries, studentCurricularPlan, approvedEnrolments, curricularCourse);
        	if (simpleEntry == null) {
        	    final Set<SimpleCurriculumEntry> simpleEntries = findEquivalentEnrolments(curriculumEntries, studentCurricularPlan,
        		    approvedEnrolments, curricularCourse);
        	    if (simpleEntries != null && !simpleEntries.isEmpty()) {
        		curriculumEntries.add(new EquivalanteCurriculumEntry(curricularCourse, simpleEntries));
        		for (final SimpleCurriculumEntry otherSimpleEntry : simpleEntries) {
        		    removeProcessedEnrolments(approvedEnrolments, otherSimpleEntry);
        		}
        	    }        	    
        	} else {
        	    curriculumEntries.add(simpleEntry);
        	    removeProcessedEnrolments(approvedEnrolments, simpleEntry);
        	}
            }
        }
    }

    private SimpleCurriculumEntry getSimpleEntry(final Collection<CurriculumEntry> entries, final StudentCurricularPlan studentCurricularPlan,
	    final EnrolmentSet approvedEnrolments, final CurricularCourse curricularCourse) {
        final Enrolment enrolment = findEnrolmentForSameCompetence(approvedEnrolments, curricularCourse);
        if (enrolment != null) {
            return new EnrolmentCurriculumEntry(curricularCourse, enrolment);
        } else {
            final NotNeedToEnrollInCurricularCourse notNeedToEnrol = studentCurricularPlan.findNotNeddToEnrol(curricularCourse);
            if (notNeedToEnrol != null && !hasBeenProcessed(entries, notNeedToEnrol)) {
        	return new NotNeedToEnrolCurriculumEntry(curricularCourse, notNeedToEnrol);
            }
        }
        return null;
    }

    private Enrolment findEnrolmentForSameCompetence(final EnrolmentSet approvedEnrolments, final CurricularCourse curricularCourse) {
        final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
        for (final Enrolment enrolment : approvedEnrolments) {
            final CurricularCourse enrolmentCurricularCourse = enrolment.getCurricularCourse();
            final CompetenceCourse enrolmentCompetenceCourse = enrolmentCurricularCourse.getCompetenceCourse();
            if (curricularCourse == enrolmentCurricularCourse || (competenceCourse != null && competenceCourse == enrolmentCompetenceCourse)) {
                return enrolment;
            }
        }
        return null;
    }

    private boolean hasBeenProcessed(final Collection<CurriculumEntry> entries, final NotNeedToEnrollInCurricularCourse notNeedToEnrol) {
	for (final CurriculumEntry entry : entries) {
	    if (entry.isNotNeedToEnrolEntry()) {
		final NotNeedToEnrolCurriculumEntry notNeedToEnrolEntry = (NotNeedToEnrolCurriculumEntry) entry;
		if (notNeedToEnrol == notNeedToEnrolEntry.getNotNeedToEnrol()) {
		    return true;
		}
	    } else if (entry.isEquivalentEnrolmentEntry()) {
		final EquivalanteCurriculumEntry equivalentEnrolmentEntry = (EquivalanteCurriculumEntry) entry;
		if (hasBeenProcessed((Collection) equivalentEnrolmentEntry.getEntries(), notNeedToEnrol)) {
		    return true;
		}
	    }
	}
	return false;
    }

    private Set<SimpleCurriculumEntry> findEquivalentEnrolments(final Collection<CurriculumEntry> entries, final StudentCurricularPlan studentCurricularPlan,
	    final EnrolmentSet approvedEnrolments, final CurricularCourse curricularCourse) {
        for (final CurricularCourseEquivalence curricularCourseEquivalence : curricularCourse.getCurricularCourseEquivalencesSet()) {
            final Set<SimpleCurriculumEntry> simpleEntries = new HashSet<SimpleCurriculumEntry>();
            final EnrolmentSet workingApprovedEnrolments = approvedEnrolments.clone();
            final Set<CurricularCourse> oldCurricularCourses = curricularCourseEquivalence.getOldCurricularCoursesSet();
            for (final CurricularCourse oldCurricularCourse : oldCurricularCourses) {
        	final SimpleCurriculumEntry simpleEntry = getSimpleEntry(entries, studentCurricularPlan, workingApprovedEnrolments, oldCurricularCourse);
        	if (simpleEntry == null) {
        	    break;
        	} else {
        	    simpleEntries.add(simpleEntry);
        	    removeProcessedEnrolments(workingApprovedEnrolments, simpleEntry);
        	}
            }

            if (oldCurricularCourses.size() == simpleEntries.size()) {
        	return simpleEntries;
            }
        }
        return null;
    }

    private void removeProcessedEnrolments(final EnrolmentSet approvedEnrolments, final SimpleCurriculumEntry simpleEntry) {
	if (simpleEntry.isEnrolmentEntry()) {
	    final EnrolmentCurriculumEntry enrolmentEntry = (EnrolmentCurriculumEntry) simpleEntry;
	    approvedEnrolments.remove(enrolmentEntry.getEnrolment());
	}
    }

    private void addNotInDegreeCurriculumEnrolments(final Collection<CurriculumEntry> entries, final StudentCurricularPlan studentCurricularPlan,
	    final Set<Enrolment> approvedEnrolments) {
	for (final Enrolment enrolment : approvedEnrolments) {
	    if (enrolment.getStudentCurricularPlan().getDegree() == studentCurricularPlan.getDegree()) {
	    	entries.add(new NotInDegreeCurriculumCurriculumEntry(enrolment));
	    }
	}
    }

}
