package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimitInExternalCycle;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ExternalCurriculumGroup extends ExternalCurriculumGroup_Base {

    public ExternalCurriculumGroup() {
        super();
    }

    public ExternalCurriculumGroup(final RootCurriculumGroup rootCurriculumGroup, final CycleCourseGroup cycleCourseGroup) {
        this();
        init(rootCurriculumGroup, cycleCourseGroup);
    }

    public ExternalCurriculumGroup(final RootCurriculumGroup rootCurriculumGroup, final CycleCourseGroup cycleCourseGroup,
            final ExecutionSemester executionSemester) {
        this();
        init(rootCurriculumGroup, cycleCourseGroup, executionSemester);
    }

    @Override
    protected void checkInitConstraints(final CurriculumGroup parent, final CourseGroup courseGroup) {
        super.checkInitConstraints(parent, courseGroup);

        if (parent.getDegreeCurricularPlanOfStudent() == courseGroup.getParentDegreeCurricularPlan()) {
            throw new DomainException(
                    "error.studentCurriculum.CurriculumGroup.courseGroup.must.have.different.degreeCurricularPlan");
        }

        checkIfCycleCourseGroupIsInDestinationAffinitiesOfSource(parent.getStudentCurricularPlan(), courseGroup);

    }

    private void checkIfCycleCourseGroupIsInDestinationAffinitiesOfSource(final StudentCurricularPlan studentCurricularPlan,
            final CourseGroup courseGroup) {

        final CycleCourseGroup cycleCourseGroup = (CycleCourseGroup) courseGroup;
        final CycleCourseGroup sourceAffinityCycleCourseGroup =
                studentCurricularPlan.getDegreeCurricularPlan().getCycleCourseGroup(
                        cycleCourseGroup.getCycleType().getSourceCycleAffinity());

        if (!sourceAffinityCycleCourseGroup.getDestinationAffinities().contains(cycleCourseGroup)) {
            throw new DomainException(
                    "error.studentCurriculum.ExternalCurriculumGroup.cycle.course.group.does.not.belong.to.afinity.of.source");
        }
    }

    @Override
    public Integer getChildOrder() {
        return Integer.MAX_VALUE - 5;
    }

    @Override
    public Integer getChildOrder(ExecutionSemester executionSemester) {
        return getChildOrder();
    }

    @Override
    public MultiLanguageString getName() {
        MultiLanguageString multiLanguageString = new MultiLanguageString();

        if (!StringUtils.isEmpty(getDegreeModule().getName())) {
            multiLanguageString =
                    multiLanguageString.with(Language.pt, getDegreeModule().getName() + " ("
                            + getDegreeCurricularPlanOfDegreeModule().getName() + ")");
        }

        if (!StringUtils.isEmpty(getDegreeModule().getNameEn())) {
            multiLanguageString =
                    multiLanguageString.with(Language.en, getDegreeModule().getNameEn() + " ("
                            + getDegreeCurricularPlanOfDegreeModule().getName() + ")");
        }

        return multiLanguageString;
    }

    @Override
    public Set<ICurricularRule> getCurricularRules(ExecutionSemester executionSemester) {
        final Set<ICurricularRule> result = super.getCurricularRules(executionSemester);
        result.add(new CreditsLimitInExternalCycle(getRootCurriculumGroup().getCycleCurriculumGroup(
                getCycleType().getSourceCycleAffinity()), this));

        return result;
    }

    @Override
    public boolean isExternal() {
        return true;
    }

    @Override
    final public Curriculum getCurriculum(final DateTime when, final ExecutionYear executionYear) {
        return Curriculum.createEmpty(this, executionYear);
    }

    final public Curriculum getCurriculumInAdvance() {
        return getCurriculum(new DateTime());
    }

    final public Curriculum getCurriculumInAdvance(final DateTime when) {
        return super.getCurriculum(when, (ExecutionYear) null);
    }

    @Override
    @Checked("RegistrationPredicates.MANAGE_CONCLUSION_PROCESS")
    public void conclude() {
        throw new DomainException("error.ExternalCurriculumGroup.cannot.conclude.external.curriculumGroups");
    }

    @Override
    public boolean isConclusionProcessed() {
        return false;
    }

}
