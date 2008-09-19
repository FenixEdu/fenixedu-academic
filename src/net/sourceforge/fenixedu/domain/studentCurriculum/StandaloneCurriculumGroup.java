package net.sourceforge.fenixedu.domain.studentCurriculum;

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
	return true;
    }
    
    @Override
    public Integer getChildOrder() {
	return super.getChildOrder() - 1;
    }
    
    @Override
    public boolean isStandalone() {
        return true;
    }
}
