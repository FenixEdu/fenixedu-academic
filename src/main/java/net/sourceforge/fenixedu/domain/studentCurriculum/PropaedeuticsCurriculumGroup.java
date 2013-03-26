package net.sourceforge.fenixedu.domain.studentCurriculum;

public class PropaedeuticsCurriculumGroup extends PropaedeuticsCurriculumGroup_Base {

    protected PropaedeuticsCurriculumGroup(final RootCurriculumGroup curriculumGroup) {
        super();
        init(curriculumGroup);
    }

    @Override
    public Integer getChildOrder() {
        return super.getChildOrder() - 2;
    }

    @Override
    public NoCourseGroupCurriculumGroupType getNoCourseGroupCurriculumGroupType() {
        return NoCourseGroupCurriculumGroupType.PROPAEDEUTICS;
    }

    @Override
    final public boolean isPropaedeutic() {
        return true;
    }

}
