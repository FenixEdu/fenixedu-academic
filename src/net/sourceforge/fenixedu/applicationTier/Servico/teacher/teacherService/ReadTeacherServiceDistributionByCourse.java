package net.sourceforge.fenixedu.applicationTier.Servico.teacher.teacherService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByCourseDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByCourseDTO.ExecutionCourseDistributionServiceEntryDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * 
 * @author jpmsit, amak
 */
public class ReadTeacherServiceDistributionByCourse extends Service {

	public List run(String username, Integer executionYearID) throws FenixServiceException, ExcepcaoPersistencia {

		DistributionTeacherServicesByCourseDTO returnDTO = new DistributionTeacherServicesByCourseDTO();

		List<CompetenceCourse> competenceCourseList;

		List<ExecutionPeriod> executionPeriodList;

		Teacher teacher;

		Department department;

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

		IPersistentTeacher persistenceTeacher = persistentSupport.getIPersistentTeacher();

		IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();

		executionPeriodList = persistentExecutionPeriod.readByExecutionYear(executionYearID);

		teacher = persistenceTeacher.readTeacherByUsername(username);

		department = teacher.getLastWorkingDepartment();

		competenceCourseList = department.getCompetenceCourses();

		Map<Integer, Boolean> executionCoursesMap = new HashMap<Integer, Boolean>();

		for (CompetenceCourse cc : competenceCourseList) {
			for (CurricularCourse curricularCourseEntry : cc.getAssociatedCurricularCourses()) {

				for (ExecutionPeriod executionPeriodEntry : executionPeriodList) {

					List<CurricularCourseScope> scopesList = curricularCourseEntry
							.getActiveScopesInExecutionPeriod(executionPeriodEntry);
					Set<String> curricularYearsList = new HashSet<String>();
					for (CurricularCourseScope scopeEntry : scopesList) {
						curricularYearsList.add(curricularCourseEntry
								.getCurricularYearByBranchAndSemester(scopeEntry.getBranch(),
										scopeEntry.getCurricularSemester().getSemester()).getYear()
								.toString());
					}

					for (ExecutionCourse executionCourseEntry : (List<ExecutionCourse>) curricularCourseEntry
							.getExecutionCoursesByExecutionPeriod(executionPeriodEntry)) {

						if (executionCoursesMap.containsKey(executionCourseEntry.getIdInternal())) {
							returnDTO.addDegreeNameToExecutionCourse(executionCourseEntry
									.getIdInternal(), curricularCourseEntry.getDegreeCurricularPlan()
									.getName());
							continue;
						}

						int executionCourseFirstTimeEnrollementStudentNumber = executionCourseEntry
								.getFirstTimeEnrolmentStudentNumber();
						int executionCourseSecondTimeEnrollementStudentNumber = executionCourseEntry
								.getSecondOrMoreTimeEnrolmentStudentNumber();

						String campus = getCampusForCurricularCourseAndExecutionPeriod(
								curricularCourseEntry, executionPeriodEntry);

						returnDTO
								.addExecutionCourse(
										executionCourseEntry.getIdInternal(),
										executionCourseEntry.getNome(),
										campus,
										curricularCourseEntry.getDegreeCurricularPlan().getDegree()
												.getSigla(),
										curricularYearsList,
										executionCourseEntry.getExecutionPeriod().getSemester(),
										executionCourseFirstTimeEnrollementStudentNumber,
										executionCourseSecondTimeEnrollementStudentNumber,
										executionCourseEntry.getTotalHours(ShiftType.TEORICA),
										executionCourseEntry.getTotalHours(ShiftType.PRATICA),
										executionCourseEntry.getTotalHours(ShiftType.LABORATORIAL),
										executionCourseEntry.getTotalHours(ShiftType.TEORICO_PRATICA),
										executionCourseEntry.getStudentsNumberByShift(ShiftType.TEORICA),
										executionCourseEntry.getStudentsNumberByShift(ShiftType.PRATICA),
										executionCourseEntry
												.getStudentsNumberByShift(ShiftType.LABORATORIAL),
										executionCourseEntry
												.getStudentsNumberByShift(ShiftType.TEORICO_PRATICA));

						fillExecutionCourseDTOWithTeachers(returnDTO, executionCourseEntry, department);

						executionCoursesMap.put(executionCourseEntry.getIdInternal(), true);

					}
				}
			}
		}

		ArrayList<ExecutionCourseDistributionServiceEntryDTO> returnArraylist = new ArrayList<ExecutionCourseDistributionServiceEntryDTO>();

		for (ExecutionCourseDistributionServiceEntryDTO teacherDTO : returnDTO.getExecutionCourseMap()
				.values()) {
			returnArraylist.add(teacherDTO);
		}

		Collections.sort(returnArraylist);

		return returnArraylist;
	}

	private void fillExecutionCourseDTOWithTeachers(DistributionTeacherServicesByCourseDTO dto,
			ExecutionCourse executionCourse, Department department) {

		for (Professorship professorShipEntry : executionCourse.getProfessorships()) {
			Teacher teacher = professorShipEntry.getTeacher();

			Integer teacherIdInternal = teacher.getIdInternal();
			String teacherName = teacher.getPerson().getNome();
			Double teacherRequiredHours = StrictMath.ceil(teacher
					.getHoursLecturedOnExecutionCourse(executionCourse));

			boolean teacherBelongsToDepartment = false;

			// TODO: See this because this is not always true. Changes when
			// teacher changes department (e.g. In different
			// execution year/periods he might work in different departments...
			// (see new model)

			if (teacher.getLastWorkingDepartment() == department) {
				teacherBelongsToDepartment = true;
			}

			dto.addTeacherToExecutionCourse(executionCourse.getIdInternal(), teacherIdInternal,
					teacherName, teacherRequiredHours.intValue(), teacherBelongsToDepartment);
		}

	}

	private String getCampusForCurricularCourseAndExecutionPeriod(CurricularCourse curricularCourse,
			ExecutionPeriod executionPeriod) {
		String campus = "";

		for (ExecutionDegree executionDegreeEntry : curricularCourse.getDegreeCurricularPlan()
				.getExecutionDegrees()) {
			if (executionDegreeEntry.getExecutionYear() == executionPeriod.getExecutionYear()) {
				campus = executionDegreeEntry.getCampus().getName();
				break;
			}
		}

		return campus;
	}

}
