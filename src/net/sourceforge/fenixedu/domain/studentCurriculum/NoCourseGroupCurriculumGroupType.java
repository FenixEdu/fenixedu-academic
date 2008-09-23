package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;

public enum NoCourseGroupCurriculumGroupType {

    PROPAEDEUTICS(CurricularRuleLevel.PROPAEUDEUTICS_ENROLMENT),

    EXTRA_CURRICULAR(CurricularRuleLevel.EXTRA_ENROLMENT),

    STANDALONE(CurricularRuleLevel.STANDALONE_ENROLMENT);

    private CurricularRuleLevel level;

    private NoCourseGroupCurriculumGroupType() {
	level = null;
    }

    private NoCourseGroupCurriculumGroupType(final CurricularRuleLevel level) {
	this.level = level;
    }

    public CurricularRuleLevel getCurricularRuleLevel() {
	return level;
    }
}
