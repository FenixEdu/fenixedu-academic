package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public abstract class NoCourseGroupCurriculumGroup extends NoCourseGroupCurriculumGroup_Base {
    
    protected  NoCourseGroupCurriculumGroup() {
        super();
    }
    
    protected void init(final CurriculumGroup curriculumGroup) {
	if(!curriculumGroup.isRoot()) {
	    throw new DomainException("error.no.root.curriculum.group");
	}
	
	this.setCurriculumGroup(curriculumGroup);
    }
    
    public static NoCourseGroupCurriculumGroup createNewNoCourseGroupCurriculumGroup(final NoCourseGroupCurriculumGroupType groupType, final CurriculumGroup curriculumGroup) {
	switch (groupType) {
	case PROPAEDEUTICS:
	    return new PropaedeuticsCurriculumGroup(curriculumGroup);
	case EXTRA_CURRICULAR:
	    return new ExtraCurriculumGroup(curriculumGroup);
	default:
	    throw new DomainException("error.unknown.NoCourseGroupCurriculumGroupType");
	}
    }
    
    @Override
    public boolean isNoCourseGroupCurriculumGroup() {
        return true;
    }
    
    @Override
    public MultiLanguageString getName() {
	final MultiLanguageString multiLanguageString = new MultiLanguageString();
	
	multiLanguageString.setContent(Language.pt, ResourceBundle.getBundle("resources/AcademicAdminOffice", new Locale("pt", "PT")).getString(getNoCourseGroupCurriculumGroupType().toString()));
        
	return multiLanguageString;
    }
    
    @Override
    public List<Context> getCurricularCourseContextsToEnrol(ExecutionPeriod executionPeriod) {
        return Collections.emptyList();
    }
    
    @Override
    public List<Context> getCourseGroupContextsToEnrol(ExecutionPeriod executionPeriod) {
        return Collections.emptyList();
    }
    
    @Override
    public Collection<CurricularCourse> getCurricularCoursesToDismissal() {
        return Collections.EMPTY_LIST;
    }
    
    @Override
    public boolean hasDegreeModule(DegreeModule degreeModule) {
	for (final CurriculumModule curriculumModule : this.getCurriculumModules()) {
	    if (curriculumModule.hasDegreeModule(degreeModule)) {
		return true;
	    }
	}
	return false;
    }
    
    @Override
    public boolean hasCourseGroup(CourseGroup courseGroup) {
	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (!curriculumModule.isLeaf()) {
		final CurriculumGroup group = (CurriculumGroup) curriculumModule;
		if (group.hasCourseGroup(courseGroup)) {
		    return true;
		}
	    }
	}
	
	return false;
    }
    
    /**
     *  Flat structure below NoCourseGroupCurriculumGroup
     */
    @Override
    public CurriculumModule findCurriculumModuleFor(final DegreeModule degreeModule) {
        for (final CurriculumModule each : getCurriculumModulesSet()) {
            if (each.getDegreeModule() == degreeModule) {
        	return each;
            }
        }
        return null;
    }
    
    @Override
    public Integer getChildOrder(ExecutionPeriod executionPeriod) {
        return Integer.MAX_VALUE;
    }
    
    @Override
    protected Integer searchChildOrderForChild(final CurriculumGroup child, final ExecutionPeriod executionPeriod) {
	final List<CurriculumModule> result = new ArrayList<CurriculumModule>(getCurriculumModulesSet());
	Collections.sort(result, CurriculumModule.COMPARATOR_BY_NAME);
        return result.indexOf(child);
    }
    
    public abstract NoCourseGroupCurriculumGroupType getNoCourseGroupCurriculumGroupType();
}
