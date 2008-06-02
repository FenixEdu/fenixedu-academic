package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class CreditsDismissal extends CreditsDismissal_Base {

    public CreditsDismissal() {
	super();
    }

    public CreditsDismissal(Credits credits, CurriculumGroup curriculumGroup,
	    Collection<CurricularCourse> noEnrolCurricularCourses) {
	checkIfCanCreate(credits, noEnrolCurricularCourses, curriculumGroup);
	init(credits, curriculumGroup);
	checkParameters(credits);
	if (noEnrolCurricularCourses != null) {
	    getNoEnrolCurricularCourses().addAll(noEnrolCurricularCourses);
	}
    }

    private void checkIfCanCreate(final Credits credits, final Collection<CurricularCourse> noEnrolCurricularCourses,
	    final CurriculumGroup curriculumGroup) {

	for (final Dismissal dismissal : curriculumGroup.getChildDismissals()) {
	    if (dismissal.isCreditsDismissal()) {
		final CreditsDismissal creditsDismissal = (CreditsDismissal) dismissal;
		if (isSimilar(credits, noEnrolCurricularCourses, creditsDismissal)) {
		    throw new DomainException("error.CreditsDismissal.already.exists.similar", curriculumGroup.getName()
			    .getContent());
		}
	    }
	}
    }

    private boolean isSimilar(final Credits credits, final Collection<CurricularCourse> curricularCourses,
	    final CreditsDismissal creditsDismissalToCheck) {
	boolean result = true;
	result &= hasSameEctsCredits(credits.getGivenCredits(), creditsDismissalToCheck);
	result &= hasSameSourceIEnrolments(credits.getIEnrolments(), creditsDismissalToCheck);
	result &= curricularCourses == null || hasSameNoEnrolCurricularCourses(curricularCourses, creditsDismissalToCheck);
	return result;
    }

    private void checkParameters(final Credits credits) {
	if (credits.getGivenCredits() == null) {
	    throw new DomainException("error.CreditsDismissal.invalid.credits");
	}
    }

    @Override
    public boolean isApproved(CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
	return (executionSemester == null || getExecutionPeriod().isBeforeOrEquals(executionSemester))
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
	return dismissal.isCreditsDismissal() && hasSameSourceIEnrolments(getSourceIEnrolments(), dismissal)
		&& hasSameNoEnrolCurricularCourses(getNoEnrolCurricularCourses(), (CreditsDismissal) dismissal)
		&& hasSameEctsCredits(getEctsCredits(), (CreditsDismissal) dismissal);
    }

    private boolean hasSameNoEnrolCurricularCourses(final Collection<CurricularCourse> curricularCourses,
	    final CreditsDismissal dismissal) {
	return curricularCourses.containsAll(dismissal.getNoEnrolCurricularCourses())
		&& curricularCourses.size() == dismissal.getNoEnrolCurricularCoursesCount();
    }

    private boolean hasSameEctsCredits(final Double ectsCredits, final CreditsDismissal dismissal) {
	return ectsCredits.equals(dismissal.getEctsCredits());
    }

}
