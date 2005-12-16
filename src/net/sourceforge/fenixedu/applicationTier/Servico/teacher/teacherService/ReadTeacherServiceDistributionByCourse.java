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
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ShiftType;
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
public class ReadTeacherServiceDistributionByCourse implements IService {

    public List run(String username, Integer executionYearID) throws FenixServiceException {

        DistributionTeacherServicesByCourseDTO returnDTO = new DistributionTeacherServicesByCourseDTO();

        List<ICompetenceCourse> competenceCourseList;

        List<IExecutionPeriod> executionPeriodList;

        ITeacher teacher;

        IDepartment department;

        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentTeacher persistenceTeacher = sp.getIPersistentTeacher();

            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

            executionPeriodList = persistentExecutionPeriod.readByExecutionYear(executionYearID);

            teacher = persistenceTeacher.readTeacherByUsername(username);

            department = teacher.getLastWorkingDepartment();

            competenceCourseList = department.getCompetenceCourses();

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        Map<Integer, Boolean> executionCoursesMap = new HashMap<Integer, Boolean>();

        for (ICompetenceCourse cc : competenceCourseList) {
            for (ICurricularCourse curricularCourseEntry : cc.getAssociatedCurricularCourses()) {

                for (IExecutionPeriod executionPeriodEntry : executionPeriodList) {

                    List<ICurricularCourseScope> scopesList = curricularCourseEntry
                            .getActiveScopesInExecutionPeriod(executionPeriodEntry);
                    Set<String> curricularYearsList = new HashSet<String>();
                    for (ICurricularCourseScope scopeEntry : scopesList) {
                        curricularYearsList.add(curricularCourseEntry
                                .getCurricularYearByBranchAndSemester(scopeEntry.getBranch(),
                                        scopeEntry.getCurricularSemester().getSemester()).getYear()
                                .toString());
                    }

                    for (IExecutionCourse executionCourseEntry : (List<IExecutionCourse>) curricularCourseEntry
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
            IExecutionCourse executionCourse, IDepartment department) {

        for (IProfessorship professorShipEntry : executionCourse.getProfessorships()) {
            ITeacher teacher = professorShipEntry.getTeacher();

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

    private String getCampusForCurricularCourseAndExecutionPeriod(ICurricularCourse curricularCourse,
            IExecutionPeriod executionPeriod) {
        String campus = "";

        for (IExecutionDegree executionDegreeEntry : curricularCourse.getDegreeCurricularPlan()
                .getExecutionDegrees()) {
            if (executionDegreeEntry.getExecutionYear() == executionPeriod.getExecutionYear()) {
                campus = executionDegreeEntry.getCampus().getName();
                break;
            }
        }

        return campus;
    }

}
