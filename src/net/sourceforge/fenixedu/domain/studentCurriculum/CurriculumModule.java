package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.YearMonthDay;

public abstract class CurriculumModule extends CurriculumModule_Base {

    static final public Comparator<CurriculumModule> COMPARATOR_BY_NAME_AND_ID = new Comparator<CurriculumModule>() {
	public int compare(CurriculumModule o1, CurriculumModule o2) {
	    int result = o1.getName().compareTo(o2.getName());
	    return (result == 0) ? DomainObject.COMPARATOR_BY_ID.compare(o1, o2) : result;
	}
    };

    public CurriculumModule() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeDegreeModule();
	removeCurriculumGroup();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public RootCurriculumGroup getRootCurriculumGroup() {
	return hasCurriculumGroup() ? getCurriculumGroup().getRootCurriculumGroup()
		: (RootCurriculumGroup) this;
    }

    public StudentCurricularPlan getRootStudentCurricularPlan() {
	return getRootCurriculumGroup().getRootStudentCurricularPlan();
    }

    public abstract boolean isLeaf();

    public abstract boolean isRoot();

    public boolean isCycleCurriculumGroup() {
	return false;
    }

    public boolean isEnrolment() {
	return false;
    }

    public boolean isDismissal() {
	return false;
    }

    abstract public StringBuilder print(String tabs);

    abstract public List<Enrolment> getEnrolments();

    public abstract StudentCurricularPlan getStudentCurricularPlan();

    public DegreeCurricularPlan getDegreeCurricularPlanOfStudent() {
	return getStudentCurricularPlan().getDegreeCurricularPlan();
    }

    public DegreeCurricularPlan getDegreeCurricularPlanOfDegreeModule() {
	return getDegreeModule().getParentDegreeCurricularPlan();
    }

    public MultiLanguageString getName() {
	final MultiLanguageString multiLanguageString = new MultiLanguageString();

	if (this.getDegreeModule().getName() != null && this.getDegreeModule().getName().length() > 0) {
	    multiLanguageString.setContent(Language.pt, this.getDegreeModule().getName());
	}
	if (this.getDegreeModule().getNameEn() != null
		&& this.getDegreeModule().getNameEn().length() > 0) {
	    multiLanguageString.setContent(Language.en, this.getDegreeModule().getNameEn());
	}
	return multiLanguageString;
    }

    public boolean isAproved(final CurricularCourse curricularCourse) {
	return isApproved(curricularCourse, null);
    }

    public boolean hasDegreeModule(final DegreeModule degreeModule) {
	return this.getDegreeModule().equals(degreeModule);
    }

    public boolean hasCurriculumModule(final CurriculumModule curriculumModule) {
	return this.equals(curriculumModule);
    }

    public boolean parentCurriculumGroupIsNoCourseGroupCurriculumGroup() {
	return hasCurriculumGroup() && getCurriculumGroup().isNoCourseGroupCurriculumGroup();
    }

    public Set<ICurricularRule> getCurricularRules(ExecutionPeriod executionPeriod) {
	Set<ICurricularRule> result = null;
	if (this.getCurriculumGroup() != null) {
	    result = this.getCurriculumGroup().getCurricularRules(executionPeriod);
	} else {
	    result = new HashSet<ICurricularRule>();
	}
	result.addAll(this.getDegreeModule().getCurricularRules(executionPeriod));
	return result;
    }

    public String getFullPath() {
	if (isRoot()) {
	    return getName().getContent();
	} else {
	    return getCurriculumGroup().getFullPath() + " > " + getName().getContent();
	}
    }

    public boolean isFor(final DegreeCurricularPlan degreeCurricularPlan) {
	return getDegreeModule().getParentDegreeCurricularPlan() == degreeCurricularPlan;
    }

    abstract public Double getEctsCredits();

    abstract public Double getAprovedEctsCredits();

    abstract public Double getEnroledEctsCredits(final ExecutionPeriod executionPeriod);

    abstract public boolean isApproved(final CurricularCourse curricularCourse,
	    final ExecutionPeriod executionPeriod);

    abstract public boolean isEnroledInExecutionPeriod(final CurricularCourse curricularCourse,
	    final ExecutionPeriod executionPeriod);

    abstract public boolean hasEnrolmentWithEnroledState(final CurricularCourse curricularCourse,
	    final ExecutionPeriod executionPeriod);

    abstract public Enrolment findEnrolmentFor(final CurricularCourse curricularCourse,
	    final ExecutionPeriod executionPeriod);

    abstract public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(
	    final ExecutionPeriod executionPeriod);

    abstract public Enrolment getApprovedEnrolment(final CurricularCourse curricularCourse);

    abstract public Dismissal getDismissal(final CurricularCourse curricularCourse);

    abstract public Collection<Enrolment> getSpecialSeasonEnrolments(ExecutionYear executionYear);

    abstract public void collectDismissals(final List<Dismissal> result);

    abstract public void getAllDegreeModules(Collection<DegreeModule> degreeModules);
    
    abstract public boolean isConcluded(ExecutionYear executionYear);
    
    abstract public YearMonthDay getConclusionDate();
    
    abstract public Double getCreditsConcluded(ExecutionYear executionYear);
    
    abstract public boolean isPropaedeutic();
    
}
