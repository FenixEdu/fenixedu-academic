package net.sourceforge.fenixedu.applicationTier.Servico.student.enrolment.bolonha;

import java.util.HashSet;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class EnrolBolonhaStudent extends Service {

    public void run(final Person person, final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionPeriod executionPeriod, final List<DegreeModuleToEnrol> degreeModulesToEnrol,
	    final List<CurriculumModule> curriculumModulesToRemove) {
	studentCurricularPlan.enrol(person, executionPeriod, new HashSet<DegreeModuleToEnrol>(
		degreeModulesToEnrol), curriculumModulesToRemove);
    }
}
