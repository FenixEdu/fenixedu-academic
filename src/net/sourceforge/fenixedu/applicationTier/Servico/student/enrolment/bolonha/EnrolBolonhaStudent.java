package net.sourceforge.fenixedu.applicationTier.Servico.student.enrolment.bolonha;

import java.util.HashSet;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import pt.ist.fenixWebFramework.services.Service;

public class EnrolBolonhaStudent {

    @Service
    public static RuleResult run(final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester,
	    final List<IDegreeModuleToEvaluate> degreeModulesToEnrol, final List<CurriculumModule> curriculumModulesToRemove,
	    final CurricularRuleLevel curricularRuleLevel) {
	return studentCurricularPlan.enrol(executionSemester, new HashSet<IDegreeModuleToEvaluate>(degreeModulesToEnrol),
		curriculumModulesToRemove, curricularRuleLevel);
    }
}
