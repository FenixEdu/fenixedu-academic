package net.sourceforge.fenixedu.domain.enrolment;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class CurriculumModuleMoveWrapper extends EnroledCurriculumModuleWrapper {

    private static final long serialVersionUID = 8766523234444669518L;
    private boolean collectRules;

    public CurriculumModuleMoveWrapper(final CurriculumModule curriculumModule, final ExecutionSemester executionPeriod) {
	super(curriculumModule, executionPeriod);
	checkParameters(curriculumModule, executionPeriod);
	collectRules = curriculumModule.isRoot() ? true : !curriculumModule.isNoCourseGroupCurriculumGroup();
    }

    private void checkParameters(final CurriculumModule curriculumModule, final ExecutionSemester executionPeriod) {
	if (curriculumModule == null) {
	    throw new DomainException("error.CurriculumModuleMoveWrapper.invalid.curriculumModule");
	}
	if (executionPeriod == null) {
	    throw new DomainException("error.CurriculumModuleMoveWrapper.invalid.executionPeriod");
	}
    }

    @Override
    public boolean canCollectRules() {
	return collectRules;
    }

    static public CurriculumModuleMoveWrapper create(final CurriculumGroup parent, final ExecutionSemester executionPeriod) {
	return new CurriculumModuleMoveWrapper(parent, executionPeriod);
    }
}
