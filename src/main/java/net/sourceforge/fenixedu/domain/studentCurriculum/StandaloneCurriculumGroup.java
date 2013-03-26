package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class StandaloneCurriculumGroup extends StandaloneCurriculumGroup_Base {

    private StandaloneCurriculumGroup() {
        super();
    }

    protected StandaloneCurriculumGroup(final RootCurriculumGroup curriculumGroup) {
        this();
        init(curriculumGroup);
    }

    @Override
    public NoCourseGroupCurriculumGroupType getNoCourseGroupCurriculumGroupType() {
        return NoCourseGroupCurriculumGroupType.STANDALONE;
    }

    @Override
    public boolean canAdd(CurriculumLine curriculumLine) {
        return curriculumLine.isEnrolment();
    }

    @Override
    public Integer getChildOrder() {
        return super.getChildOrder() - 4;
    }

    @Override
    public boolean isStandalone() {
        return true;
    }

    @Override
    public int getNumberOfAllApprovedEnrolments(final ExecutionSemester executionSemester) {
        int result = 0;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            result += curriculumModule.getNumberOfAllApprovedEnrolments(executionSemester);
        }
        return result;
    }

    @Override
    public boolean allowAccumulatedEctsCredits() {
        return true;
    }
}
