package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class CreditsDismissal extends CreditsDismissal_Base {

    public CreditsDismissal() {
	super();
    }

    public CreditsDismissal(Credits credits, CurriculumGroup curriculumGroup,
	    Collection<CurricularCourse> noEnrolCurricularCourses) {
	checkIfCanCreate(credits, curriculumGroup);
	init(credits, curriculumGroup);
	checkParameters(credits);
	if (noEnrolCurricularCourses != null) {
	    getNoEnrolCurricularCourses().addAll(noEnrolCurricularCourses);
	}
    }

    private void checkIfCanCreate(final Credits credits, CurriculumGroup curriculumGroup) {

    }

    private void checkParameters(final Credits credits) {
	if (credits.getGivenCredits() == null) {
	    throw new DomainException("error.CreditsDismissal.invalid.credits");
	}
    }

    @Override
    public boolean isApproved(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
	return (executionPeriod == null || getExecutionPeriod().isBeforeOrEquals(executionPeriod))
		&& hasEquivalentNoEnrolCurricularCourse(curricularCourse);
    }

    private boolean hasEquivalentNoEnrolCurricularCourse(CurricularCourse curricularCourse) {
	for (CurricularCourse course : getNoEnrolCurricularCoursesSet()) {
	    if (course.isEquivalent(curricularCourse)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public Double getEctsCredits() {
	return getCredits().getGivenCredits();
    }

    @Override
    public boolean hasDegreeModule(DegreeModule degreeModule) {
	return false;
    }

    @Override
    public MultiLanguageString getName() {
	final MultiLanguageString multiLanguageString = new MultiLanguageString();
	multiLanguageString.setContent(Language.pt, ResourceBundle.getBundle("resources/AcademicAdminOffice",
		new Locale("pt", "PT")).getString("label.group.credits"));
	return multiLanguageString;
    }

    @Override
    public Dismissal getDismissal(final CurricularCourse curricularCourse) {
	return hasNoEnrolCurricularCourses(curricularCourse) ? this : null;
    }

    @Override
    public void delete() {
	getNoEnrolCurricularCourses().clear();
	super.delete();
    }

    @Override
    public boolean isCreditsDismissal() {
	return true;
    }

    @Override
    public boolean isSimilar(final Dismissal dismissal) {
	return dismissal.isCreditsDismissal() && hasSameSourceIEnrolments(dismissal)
		&& hasSameNoEnrolCurricularCourses((CreditsDismissal) dismissal)
		&& hasSameEctsCredits((CreditsDismissal) dismissal);
    }

    private boolean hasSameNoEnrolCurricularCourses(final CreditsDismissal dismissal) {
	return getNoEnrolCurricularCourses().containsAll(dismissal.getNoEnrolCurricularCourses())
		&& getNoEnrolCurricularCoursesCount() == dismissal.getNoEnrolCurricularCoursesCount();
    }

    private boolean hasSameEctsCredits(final CreditsDismissal dismissal) {
	return getEctsCredits().equals(dismissal.getEctsCredits());
    }

}
