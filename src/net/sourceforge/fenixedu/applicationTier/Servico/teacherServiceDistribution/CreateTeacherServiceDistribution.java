package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.util.LanguageUtils;

public class CreateTeacherServiceDistribution extends Service {
	
	public TeacherServiceDistribution run(List<ExecutionPeriod> executionPeriodList,
			Department department, Person creator, String name) {

		ResourceBundle rb = ResourceBundle
				.getBundle("resources.DepartmentMemberResources", LanguageUtils.getLocale());

		TeacherServiceDistribution teacherServiceDistribution = new TeacherServiceDistribution(
				department, executionPeriodList, creator, name,
				rb.getString("label.teacherServiceDistribution.initialPhase"));

		return teacherServiceDistribution;
	}

}
