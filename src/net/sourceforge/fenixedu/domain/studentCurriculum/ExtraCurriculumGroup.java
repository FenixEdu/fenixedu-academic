package net.sourceforge.fenixedu.domain.studentCurriculum;

public class ExtraCurriculumGroup extends ExtraCurriculumGroup_Base {

	protected ExtraCurriculumGroup(final RootCurriculumGroup curriculumGroup) {
		super();
		init(curriculumGroup);
	}

	@Override
	public Integer getChildOrder() {
		return super.getChildOrder() - 3;
	}

	@Override
	public NoCourseGroupCurriculumGroupType getNoCourseGroupCurriculumGroupType() {
		return NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR;
	}

	@Override
	public boolean canAdd(CurriculumLine curriculumLine) {
		return true;
	}

	@Override
	public boolean isExtraCurriculum() {
		return true;
	}
}
