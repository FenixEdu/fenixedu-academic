package net.sourceforge.fenixedu.domain.studentCurriculum;


public class PropaedeuticsCurriculumGroup extends PropaedeuticsCurriculumGroup_Base {
    
    public  PropaedeuticsCurriculumGroup(final CurriculumGroup curriculumGroup) {
	super();
	init(curriculumGroup);
    }
    
    @Override
    public Integer getChildOrder() {
        return super.getChildOrder() - 1;
    }
    
    @Override
    public NoCourseGroupCurriculumGroupType getNoCourseGroupCurriculumGroupType() {
        return NoCourseGroupCurriculumGroupType.PROPAEDEUTICS;
    }
}
