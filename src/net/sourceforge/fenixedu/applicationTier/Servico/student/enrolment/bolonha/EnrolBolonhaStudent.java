package net.sourceforge.fenixedu.applicationTier.Servico.student.enrolment.bolonha;

import java.util.HashSet;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResult;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class EnrolBolonhaStudent extends Service {

    public List<RuleResult> run(final Person person, final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionPeriod executionPeriod, final List<IDegreeModuleToEvaluate> degreeModulesToEnrol,
	    final List<CurriculumModule> curriculumModulesToRemove,
	    final CurricularRuleLevel curricularRuleLevel) {
	return studentCurricularPlan.enrol(person, executionPeriod, new HashSet<IDegreeModuleToEvaluate>(
		degreeModulesToEnrol), curriculumModulesToRemove, curricularRuleLevel);
    }
}
