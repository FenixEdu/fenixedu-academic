package net.sourceforge.fenixedu.domain.enrolment;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class CurriculumModuleMoveWrapper extends EnroledCurriculumModuleWrapper {

    private static final long serialVersionUID = 8766523234444669518L;
    private boolean collectRules;

    public CurriculumModuleMoveWrapper(final CurriculumModule curriculumModule, final ExecutionPeriod executionPeriod) {
	super(curriculumModule, executionPeriod);
	collectRules = curriculumModule.isRoot() ? true : !curriculumModule.isNoCourseGroupCurriculumGroup();
    }

    @Override
    public boolean canCollectRules() {
	return collectRules;
    }

    static public CurriculumModuleMoveWrapper create(final CurriculumGroup parent, final ExecutionPeriod executionPeriod) {
	return new CurriculumModuleMoveWrapper(parent, executionPeriod);
    }
}
