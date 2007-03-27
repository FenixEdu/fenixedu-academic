package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class Dismissal extends Dismissal_Base {
    
    protected  Dismissal() {
        super();
    }
    
    protected Dismissal(Credits credits, CurriculumGroup curriculumGroup) {
        super();
        if(credits == null || curriculumGroup == null) {
            throw new DomainException("error.dismissal.wrong.arguments");
        }
        setCredits(credits);
        setCurriculumGroup(curriculumGroup);
    }
    
    protected Dismissal(Credits credits, CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
        this(credits, curriculumGroup);
        if( curricularCourse == null) {
            throw new DomainException("error.dismissal.wrong.arguments");
        }
        checkCurriculumGroupCurricularCourse(curriculumGroup, curricularCourse);
        setCurricularCourse(curricularCourse);
    }

    private void checkCurriculumGroupCurricularCourse(CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
	if (!(curriculumGroup instanceof NoCourseGroupCurriculumGroup)) {
	    if(!curriculumGroup.getCurricularCoursesToDismissal().contains(curricularCourse)) {
		throw new DomainException("error.dismissal.invalid.curricular.course.to.dismissal");
	    }
	}
    }
    
    static protected Dismissal createNewDismissal(final Credits credits, final StudentCurricularPlan studentCurricularPlan, final CourseGroup courseGroup) {
	return new Dismissal(credits, findCurriculumGroupForCourseGroup(studentCurricularPlan, courseGroup));
    }
    
    static private CurriculumGroup findCurriculumGroupForCourseGroup(final StudentCurricularPlan studentCurricularPlan, final CourseGroup courseGroup) {
	final CurriculumGroup curriculumGroup = studentCurricularPlan.findCurriculumGroupFor(courseGroup);
	if (curriculumGroup != null) {
	    return curriculumGroup;
	}
	return new CurriculumGroup(getOrCreateExtraCurricularCurriculumGroup(studentCurricularPlan), courseGroup);
    }

    static private NoCourseGroupCurriculumGroup getOrCreateExtraCurricularCurriculumGroup(final StudentCurricularPlan studentCurricularPlan) {
	final NoCourseGroupCurriculumGroup result = studentCurricularPlan.getNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR);
	return (result == null) ? studentCurricularPlan.createNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR) : result; 
    }
    
    static protected Dismissal createNewDismissal(final Credits credits, final StudentCurricularPlan studentCurricularPlan, final CurricularCourse curricularCourse) {
	return new Dismissal(credits, findCurriculumGroupForCurricularCourse(studentCurricularPlan, curricularCourse), curricularCourse);
    }

    static private CurriculumGroup findCurriculumGroupForCurricularCourse(final StudentCurricularPlan studentCurricularPlan, final CurricularCourse curricularCourse) {
	if (curricularCourse.hasOnlyOneParentCourseGroup()) {
	    final CurriculumGroup curriculumGroup = studentCurricularPlan.findCurriculumGroupFor(getFirstParentCourseGroup(curricularCourse));
	    if (curriculumGroup != null && !curriculumGroup.parentCurriculumGroupIsNoCourseGroupCurriculumGroup()) {
		return curriculumGroup;
	    }
	}
	return getOrCreateExtraCurricularCurriculumGroup(studentCurricularPlan);
    }

    private static CourseGroup getFirstParentCourseGroup(final CurricularCourse curricularCourse) {
	return curricularCourse.getParentContexts().get(0).getParentCourseGroup();
    }

    @Override
    public StringBuilder print(String tabs) {
	final StringBuilder builder = new StringBuilder();
	builder.append(tabs);
	builder.append("[D ").append(hasDegreeModule() ? getDegreeModule().getName() :  "").append(" ]\n");
	return builder;
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
    public Double getEnroledEctsCredits(final ExecutionPeriod executionPeriod) {
        return Double.valueOf(0d);
    }
    
    @Override
    public boolean hasDegreeModule(DegreeModule degreeModule) {
	return hasDegreeModule() ? super.hasDegreeModule(degreeModule) : false;
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
    public void collectDismissals(final List<Dismissal> result) {
	result.add(this);
    }
    
    @Override
    public void delete() {
        removeCredits();
        super.delete();
    }
}
