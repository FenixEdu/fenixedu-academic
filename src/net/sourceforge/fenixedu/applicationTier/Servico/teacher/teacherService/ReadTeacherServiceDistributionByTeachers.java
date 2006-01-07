package net.sourceforge.fenixedu.applicationTier.Servico.teacher.teacherService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByTeachersDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByTeachersDTO.TeacherDistributionServiceEntryDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author jpmsit, amak
 */
public class ReadTeacherServiceDistributionByTeachers implements IService {

	public List run(String username, Integer executionYearID) throws FenixServiceException, ExcepcaoPersistencia {

		DistributionTeacherServicesByTeachersDTO returnDTO = new DistributionTeacherServicesByTeachersDTO();

		List<Teacher> teachers;

		List<ExecutionPeriod> executionPeriodList;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

		IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

		executionPeriodList = persistentExecutionPeriod.readByExecutionYear(executionYearID);

		Department department = persistentTeacher.readTeacherByUsername(username)
				.getLastWorkingDepartment();

		teachers = department.getCurrentTeachers();

		/* Information about teacher */
		Double hoursSpentByTeacher;

		for (Teacher teacher : teachers) {
			returnDTO.addTeacher(teacher.getIdInternal(), teacher.getTeacherNumber(), teacher
					.getCategory().getCode(), teacher.getPerson().getNome(), 0, 0d);

			for (Professorship professorShip : teacher.getProfessorships()) {
				ExecutionCourse executionCourse = professorShip.getExecutionCourse();

				if (!executionPeriodList.contains(executionCourse.getExecutionPeriod())) {
					continue;
				}

				Set<String> curricularYears = new HashSet<String>();
				Set<String> degreeNames = new HashSet<String>();
				for (CurricularCourse curricularCourse : executionCourse
						.getAssociatedCurricularCourses()) {
					degreeNames.add(curricularCourse.getDegreeCurricularPlan().getDegree().getSigla());

					for (CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
						CurricularSemester curricularSemester = curricularCourseScope
								.getCurricularSemester();
						curricularYears.add(curricularSemester.getCurricularYear().getYear().toString());
					}
				}

				hoursSpentByTeacher = StrictMath.ceil(teacher
						.getHoursLecturedOnExecutionCourse(executionCourse));

				returnDTO.addExecutionCourseToTeacher(teacher.getIdInternal(), executionCourse
						.getIdInternal(), executionCourse.getNome(), hoursSpentByTeacher.intValue(),
						degreeNames, curricularYears, executionCourse.getExecutionPeriod().getName());

			}
		}

		ArrayList<TeacherDistributionServiceEntryDTO> returnArraylist = new ArrayList<TeacherDistributionServiceEntryDTO>();

		for (TeacherDistributionServiceEntryDTO teacher : returnDTO.getTeachersMap().values()) {
			returnArraylist.add(teacher);
		}

		Collections.sort(returnArraylist);

		return returnArraylist;
	}

}
