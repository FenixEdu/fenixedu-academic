package net.sourceforge.fenixedu.dataTransferObject.phd.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.StudentCurriculumGroupBean;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class PhdStudentCurriculumGroupBean extends StudentCurriculumGroupBean {

	static private final long serialVersionUID = 1L;

	public PhdStudentCurriculumGroupBean(CurriculumGroup curriculumGroup, ExecutionSemester executionSemester,
			int[] curricularYears) {
		super(curriculumGroup, executionSemester, curricularYears);
	}

	@Override
	protected List<IDegreeModuleToEvaluate> buildCurricularCoursesToEnrol(CurriculumGroup group, ExecutionSemester semester) {

		final Collection<CompetenceCourse> collection = getCompetenceCoursesAvailableToEnrol();

		final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>();
		for (final Context context : group.getCurricularCourseContextsToEnrol(semester)) {

			if (canBeUsed(collection, context)) {
				result.add(new DegreeModuleToEnrol(group, context, semester));
			}
		}

		return result;
	}

	private boolean canBeUsed(Collection<CompetenceCourse> collection, Context context) {
		final CurricularCourse course = (CurricularCourse) context.getChildDegreeModule();
		return course.isOptionalCurricularCourse()
				|| (course.hasCompetenceCourse() && collection.contains(course.getCompetenceCourse()));
	}

	private Collection<CompetenceCourse> getCompetenceCoursesAvailableToEnrol() {
		return getRegistration().getPhdIndividualProgramProcess().getCompetenceCoursesAvailableToEnrol();
	}

	@Override
	protected List<IDegreeModuleToEvaluate> buildCurricularCoursesToEnrol(CurriculumGroup group,
			ExecutionSemester executionSemester, int[] curricularYears) {
		throw new DomainException("error.PhdStudentCurriculumGroupBean.unexpected.invocation");
	}

	@Override
	protected StudentCurriculumGroupBean createEnroledCurriculumGroupBean(ExecutionSemester executionSemester,
			int[] curricularYears, CurriculumGroup curriculumGroup) {
		return new PhdStudentCurriculumGroupBean(curriculumGroup, executionSemester, curricularYears);
	}
}
