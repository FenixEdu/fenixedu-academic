package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.domain.CurricularCourse;

public class InternalCreditsSourceCurriculumGroup extends InternalCreditsSourceCurriculumGroup_Base {

    protected InternalCreditsSourceCurriculumGroup(final RootCurriculumGroup curriculumGroup) {
        super();
        init(curriculumGroup);
    }

    @Override
    public NoCourseGroupCurriculumGroupType getNoCourseGroupCurriculumGroupType() {
        return NoCourseGroupCurriculumGroupType.INTERNAL_CREDITS_SOURCE_GROUP;
    }

    @Override
    public Integer getChildOrder() {
        return super.getChildOrder() - 1;
    }

    @Override
    public Collection<CurriculumGroup> getCurricularCoursePossibleGroups(final CurricularCourse curricularCourse) {
        return Collections.emptyList();
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
