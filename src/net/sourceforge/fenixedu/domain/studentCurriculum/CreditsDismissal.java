package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class CreditsDismissal extends CreditsDismissal_Base {
    
    public CreditsDismissal() {
        super();
    }
    
    public CreditsDismissal(Credits credits, CurriculumGroup curriculumGroup) {
	init(credits, curriculumGroup);
    }
    
    @Override
    public boolean isApproved(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
	return (executionPeriod == null || getExecutionPeriod().isBeforeOrEquals(executionPeriod)) && hasEquivalentNoEnrolCurricularCourse(curricularCourse);
    }
    
    private boolean hasEquivalentNoEnrolCurricularCourse(CurricularCourse curricularCourse) {
	for (CurricularCourse course : getNoEnrolCurricularCoursesSet()) {
	    if(course.isEquivalent(curricularCourse)) {
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
	multiLanguageString.setContent(Language.pt, ResourceBundle.getBundle("resources/AcademicAdminOffice", new Locale("pt", "PT")).getString("label.group.credits"));
	return multiLanguageString;
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
}
