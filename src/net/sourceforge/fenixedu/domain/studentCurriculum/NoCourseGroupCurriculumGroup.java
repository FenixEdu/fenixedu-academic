package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class NoCourseGroupCurriculumGroup extends NoCourseGroupCurriculumGroup_Base {

    protected NoCourseGroupCurriculumGroup() {
	super();
    }

    protected void init(final CurriculumGroup curriculumGroup) {
	if (!curriculumGroup.isRoot()) {
	    throw new DomainException("error.no.root.curriculum.group");
	}

	this.setCurriculumGroup(curriculumGroup);
    }

    public static NoCourseGroupCurriculumGroup createNewNoCourseGroupCurriculumGroup(
	    final NoCourseGroupCurriculumGroupType groupType, final CurriculumGroup curriculumGroup) {
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

	multiLanguageString.setContent(Language.pt, ResourceBundle.getBundle("resources/AcademicAdminOffice",
		new Locale("pt", "PT")).getString(getNoCourseGroupCurriculumGroupType().toString()));

	return multiLanguageString;
    }

    @Override
    public List<Context> getCurricularCourseContextsToEnrol(ExecutionSemester executionSemester) {
	return Collections.emptyList();
    }

    @Override
    public List<Context> getCourseGroupContextsToEnrol(ExecutionSemester executionSemester) {
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
     * Flat structure below NoCourseGroupCurriculumGroup
     */
    @Override
    public CurriculumGroup findCurriculumGroupFor(final CourseGroup courseGroup) {
	for (final CurriculumModule each : getCurriculumModulesSet()) {
	    if (!each.isLeaf() && each.getDegreeModule() == courseGroup) {
		return (CurriculumGroup) each;
	    }
	}
	return null;
    }

    @Override
    public Integer getChildOrder(final ExecutionSemester executionSemester) {
	return Integer.MAX_VALUE;
    }

    @Override
    protected Integer searchChildOrderForChild(final CurriculumGroup child, final ExecutionSemester executionSemester) {
	final List<CurriculumModule> result = new ArrayList<CurriculumModule>(getCurriculumModulesSet());
	Collections.sort(result, CurriculumModule.COMPARATOR_BY_NAME_AND_ID);
	return result.indexOf(child);
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(ExecutionSemester executionSemester) {
	return Collections.EMPTY_SET;
    }

    @Override
    public Set<ICurricularRule> getCurricularRules(ExecutionSemester executionSemester) {
	return Collections.emptySet();
    }

    @Override
    public ConclusionValue isConcluded(ExecutionYear executionYear) {
	return ConclusionValue.CONCLUDED;
    }

    @Override
    public Curriculum getCurriculum(final ExecutionYear executionYear) {
	return Curriculum.createEmpty(this, executionYear);
    }

    @Override
    public Double getCreditsConcluded(ExecutionYear executionYear) {
	return Double.valueOf(0d);
    }

    @Override
    public boolean canAdd(CurriculumLine curriculumLine) {
	return false;
    }

    @Override
    public Collection<? extends CurriculumGroup> getCurricularCoursePossibleGroups(CurricularCourse curricularCourse) {
	return Collections.singleton(this);
    }
    
    @Override
    public Collection<CurriculumGroup> getCurricularCoursePossibleGroupsWithoutNoCourseGroupCurriculumGroups(CurricularCourse curricularCourse) {
        return Collections.emptyList();
    }

    @Override
    public Double getAprovedEctsCredits() {
	return Double.valueOf(0d);
    }

    @Override
    public Collection<NoCourseGroupCurriculumGroup> getNoCourseGroupCurriculumGroups() {
	Collection<NoCourseGroupCurriculumGroup> res = new HashSet<NoCourseGroupCurriculumGroup>();
	res.add(this);
	res.addAll(super.getNoCourseGroupCurriculumGroups());
	return res;
    }

    @Override
    public ICurricularRule getMostRecentActiveCurricularRule(final CurricularRuleType ruleType, final ExecutionYear executionYear) {
	return null;
    }

    abstract public NoCourseGroupCurriculumGroupType getNoCourseGroupCurriculumGroupType();
    
    @Override
    public int getNumberOfAllApprovedCurriculumLines() {
        return 0;
    }
}
