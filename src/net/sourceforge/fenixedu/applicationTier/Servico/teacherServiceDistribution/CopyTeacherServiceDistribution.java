package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class CopyTeacherServiceDistribution extends Service {
	public TeacherServiceDistribution run(List<ExecutionPeriod> executionPeriodList, TeacherServiceDistribution teacherServiceDistribution, Person creator, String name) {
		
		return TeacherServiceDistribution.copyTeacherServiceDistribution(teacherServiceDistribution, executionPeriodList, name, creator);
		
	}
}
