package net.sourceforge.fenixedu.applicationTier.Servico.student.enrolment.bolonha;

import java.util.HashSet;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CourseLoadRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ProgramCertificateRequest;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import pt.ist.fenixWebFramework.services.Service;

public class EnrolBolonhaStudentInCurriculumValidationContext {

	@Service
	public static RuleResult run(final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester,
			final List<IDegreeModuleToEvaluate> degreeModulesToEnrol, final List<CurriculumModule> curriculumModulesToRemove,
			final CurricularRuleLevel curricularRuleLevel) {

		for (CurriculumModule module : curriculumModulesToRemove) {
			if (!module.isEnrolment()) {
				continue;
			}

			Enrolment enrolment = (Enrolment) module;

			enrolment.getCourseLoadRequestsSet().retainAll(new HashSet<CourseLoadRequest>());
			enrolment.getProgramCertificateRequestsSet().retainAll(new HashSet<ProgramCertificateRequest>());
		}

		return EnrolBolonhaStudent.run(studentCurricularPlan, executionSemester, degreeModulesToEnrol, curriculumModulesToRemove,
				curricularRuleLevel);
	}
}
