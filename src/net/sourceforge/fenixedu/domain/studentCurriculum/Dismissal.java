package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class Dismissal extends Dismissal_Base {
    
    protected  Dismissal() {
        super();
    }
    
    protected  Dismissal(Credits credits, CurriculumGroup curriculumGroup) {
        super();
        if(credits == null || curriculumGroup == null) {
            throw new DomainException("error.dismissal.wrong.arguments");
        }
        setCredits(credits);
        setCurriculumGroup(curriculumGroup);
    }
    
    protected  Dismissal(Credits credits, CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
        this(credits, curriculumGroup);
        if( curricularCourse == null) {
            throw new DomainException("error.dismissal.wrong.arguments");
        }
        checkCurriculumGroupCurricularCourse(curriculumGroup, curricularCourse);
        setCurricularCourse(curricularCourse);
    }

    private void checkCurriculumGroupCurricularCourse(CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
	if(!curriculumGroup.getCurricularCoursesToDismissal().contains(curricularCourse)) {
	    throw new DomainException("error.dismissal.invalid.curricular.course.to.dismissal");
	}
    }

    @Override
    public StringBuilder print(String tabs) {
	return null;
    }
    
    @Override
    public boolean isAproved(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
        if(getCurricularCourse() != null) {
            return getCurricularCourse().isEquivalent(curricularCourse);
        } else {
            return false;
        }
    }
    
    @Override
    public Double getEctsCredits() {
        if(getCurricularCourse() == null) {
            return getCredits().getGivenCredits();
        } 
        return getCurricularCourse().getEctsCredits();
    }
    
    @Override
    public Double getAprovedEctsCredits() {
        return getEctsCredits();
    }
    
    @Override
    public boolean hasDegreModule(DegreeModule degreeModule) {
        if(hasDegreeModule()) {
            return super.hasDegreModule(degreeModule);
        } else {
            return false;
        }
    }
    
    @Override
    public MultiLanguageString getName() {
	if(hasDegreeModule()) {
	    return super.getName();
	} else {
	    final MultiLanguageString multiLanguageString = new MultiLanguageString();
	    multiLanguageString.setContent(Language.pt, ResourceBundle.getBundle("resources/AcademicAdminOffice", new Locale("pt", "PT")).getString("label.group.credits"));
	    return multiLanguageString;
	}
	
    }
    
    @Override
    public void delete() {
        removeCredits();
        super.delete();
    }
}
