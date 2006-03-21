package net.sourceforge.fenixedu.applicationTier.Servico.credits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorshipWithInfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoManagementPositionCreditLine;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoOtherTypeCreditLine;
import net.sourceforge.fenixedu.dataTransferObject.credits.TeacherCreditsSheetDTO;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorshipWithProfessorshipAndExecutionCourseAndShiftAndLessons;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.DetailedProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.InfoSupportLesson;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InfoTeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.credits.OtherTypeCreditLine;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.teacher.workTime.TeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentManagementPositionCreditLine;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;

public class ReadTeacherCreditsSheet extends Service {

    private ExecutionPeriod readExecutionPeriod(Integer executionPeriodId,
            IPersistentExecutionPeriod executionPeriodDAO) throws FenixServiceException,
            ExcepcaoPersistencia {
        ExecutionPeriod executionPeriod;
        if ((executionPeriodId == null) || (executionPeriodId.intValue() == 0)) {
            executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
        } else {
            executionPeriod = (ExecutionPeriod) persistentObject.readByOID(ExecutionPeriod.class,
                    executionPeriodId);
        }
        return executionPeriod;
    }

    private List readInfoMasterDegreeProfessorships(Teacher teacher, ExecutionPeriod executionPeriod) {
        List professorships = teacher.getProfessorships();

        List infoMasterDegreeProfessorships = new ArrayList();
        for (int i = 0; i < professorships.size(); i++) {
            Professorship professorship = (Professorship) professorships.get(i);
            ExecutionCourse executionCourse = professorship.getExecutionCourse();

            if (executionCourse.getExecutionPeriod().equals(executionPeriod)
                    && executionCourse.isMasterDegreeOnly()) {
                InfoProfessorship infoProfessorship = InfoProfessorshipWithInfoExecutionCourse
                        .newInfoFromDomain(professorship);
                infoMasterDegreeProfessorships.add(infoProfessorship);
            }
        }
        return infoMasterDegreeProfessorships;
    }

    /**
     * @param persistentSupport
     * @param infoCredits
     * @param infoTeacher
     * @param infoExecutionPeriod
     * @return
     */
    private List readInfoShiftProfessorships(Teacher teacher, ExecutionPeriod executionPeriod) {
        List professorships = teacher.getProfessorships();
        List shiftProfessorships = new ArrayList();
        for (int i = 0; i < professorships.size(); i++) {
            Professorship professorship = (Professorship) professorships.get(i);
            ExecutionCourse executionCourse = professorship.getExecutionCourse();
            if (executionCourse.getExecutionPeriod().equals(executionPeriod)) {
                shiftProfessorships.addAll(professorship.getAssociatedShiftProfessorship());
            }
        }

        List infoShiftProfessorships = new ArrayList();
        for (int i = 0; i < shiftProfessorships.size(); i++) {
            ShiftProfessorship shiftProfessorship = (ShiftProfessorship) shiftProfessorships.get(i);
            InfoShiftProfessorship infoShiftProfessorship = InfoShiftProfessorshipWithProfessorshipAndExecutionCourseAndShiftAndLessons
                    .newInfoFromDomain(shiftProfessorship);
            infoShiftProfessorships.add(infoShiftProfessorship);
        }
        return infoShiftProfessorships;
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param infoCredits
     * @param persistentSupport
     * @return
     */
    private List readInfoSupportLessonList(Teacher teacher, ExecutionPeriod executionPeriod) {

        List professorships = teacher.getProfessorships();
        List supportLessonList = new ArrayList();
        for (int i = 0; i < professorships.size(); i++) {
            Professorship professorship = (Professorship) professorships.get(i);
            ExecutionCourse executionCourse = professorship.getExecutionCourse();
            if (executionCourse.getExecutionPeriod().equals(executionPeriod)) {
                supportLessonList.addAll(professorship.getSupportLessons());

            }
        }
        List infoSupportLessonList = new ArrayList();
        for (int i = 0; i < supportLessonList.size(); i++) {
            SupportLesson supportLesson = (SupportLesson) supportLessonList.get(i);

            InfoSupportLesson infoSupportLesson = InfoSupportLesson.newInfoFromDomain(supportLesson);
            infoSupportLessonList.add(infoSupportLesson);
        }

        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("weekDay.diaSemana"));
        comparatorChain.addComparator(new BeanComparator("startTime"));
        Collections.sort(infoSupportLessonList, comparatorChain);
        return infoSupportLessonList;
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param infoCredits
     * @param persistentSupport
     * @return
     */
    private List readInfoTeacherInstitutionWorkingTime(Teacher teacher, ExecutionPeriod executionPeriod) {
        List<InfoTeacherInstitutionWorkTime> infoTeacherInstitutionWorkingTimeList = new ArrayList<InfoTeacherInstitutionWorkTime>();
        for (TeacherInstitutionWorkTime item : teacher.getInstitutionWorkTimePeriods()) {
            if (item.getExecutionPeriod().equals(executionPeriod)) {
                InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime = InfoTeacherInstitutionWorkTime
                        .newInfoFromDomain(item);
                infoTeacherInstitutionWorkingTimeList.add(infoTeacherInstitutionWorkTime);
            }
        }

        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("weekDay.diaSemana"));
        comparatorChain.addComparator(new BeanComparator("startTime"));
        Collections.sort(infoTeacherInstitutionWorkingTimeList, comparatorChain);
        return infoTeacherInstitutionWorkingTimeList;
    }

    private Teacher readTeacher(Integer teacherNumber)
            throws FenixServiceException, ExcepcaoPersistencia {
        Teacher teacher;
        teacher = Teacher.readByNumber(teacherNumber);
        return teacher;
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param infoCredits
     * @param persistentSupport
     * @return
     */
    private List readTeacherDegreeFinalProjectStudentList(Teacher teacher,
            ExecutionPeriod executionPeriod) {

        List<InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson> infoList = new ArrayList<InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson>();
        for (TeacherDegreeFinalProjectStudent student : teacher.getDegreeFinalProjectStudents()) {
            if (student.getExecutionPeriod().equals(executionPeriod)) {
                infoList.add(InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson
                        .newInfoFromDomain(student));
            }
        }

        Collections.sort(infoList, new BeanComparator("infoStudent.number"));
        return infoList;
    }

    public TeacherCreditsSheetDTO run(InfoTeacher infoTeacherParam, Integer executionPeriodId)
            throws FenixServiceException, ExcepcaoPersistencia {

        TeacherCreditsSheetDTO teacherCreditsSheetDTO = new TeacherCreditsSheetDTO();

        IPersistentExecutionPeriod executionPeriodDAO = persistentSupport.getIPersistentExecutionPeriod();
        ExecutionPeriod executionPeriod = readExecutionPeriod(executionPeriodId, executionPeriodDAO);
        Teacher teacher = readTeacher(infoTeacherParam.getTeacherNumber());

        List infoMasterDegreeProfessorships = readInfoMasterDegreeProfessorships(teacher,
                executionPeriod);

        List infoShiftProfessorships = readInfoShiftProfessorships(teacher, executionPeriod);

        List infoTimeInstitutionWorkingTimeList = readInfoTeacherInstitutionWorkingTime(teacher,
                executionPeriod);

        List infoSupportLessonList = readInfoSupportLessonList(teacher, executionPeriod);

        List infoTeacherDegreeFinalProjectStudentList = readTeacherDegreeFinalProjectStudentList(
                teacher, executionPeriod);

        List detailedProfessorshipList = readDetailedProfessorships(teacher, executionPeriod);

        List otherTypeCreditLineList = readOtherTypeCreditLine(teacher, executionPeriod);

        List infoManagementPositions = readManagementPositions(teacher, executionPeriod);

        List infoServiceExemptions = teacher.getServiceExemptionSituations(executionPeriod
                .getBeginDate(), executionPeriod.getEndDate());

        InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriodWithInfoExecutionYear
                .newInfoFromDomain(executionPeriod);

        teacherCreditsSheetDTO.setInfoTeacher(infoTeacher);
        teacherCreditsSheetDTO.setInfoExecutionPeriod(infoExecutionPeriod);

        InfoCredits infoCredits = teacher.getExecutionPeriodCredits(executionPeriod);

        teacherCreditsSheetDTO.setInfoCredits(infoCredits);

        teacherCreditsSheetDTO.setInfoMasterDegreeProfessorships(infoMasterDegreeProfessorships);

        teacherCreditsSheetDTO
                .setInfoTeacherInstitutionWorkingTimeList(infoTimeInstitutionWorkingTimeList);

        teacherCreditsSheetDTO.setInfoShiftProfessorshipList(infoShiftProfessorships);

        teacherCreditsSheetDTO.setInfoSupportLessonList(infoSupportLessonList);

        teacherCreditsSheetDTO
                .setInfoTeacherDegreeFinalProjectStudentList(infoTeacherDegreeFinalProjectStudentList);

        teacherCreditsSheetDTO.setDetailedProfessorshipList(detailedProfessorshipList);

        teacherCreditsSheetDTO.setInfoTeacherOtherTypeCreditLineList(otherTypeCreditLineList);

        teacherCreditsSheetDTO.setInfoManagementPositions(infoManagementPositions);
        teacherCreditsSheetDTO.setInfoServiceExemptions(infoServiceExemptions);

        return teacherCreditsSheetDTO;
    }

    private List readManagementPositions(Teacher teacher, ExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        IPersistentManagementPositionCreditLine managementPosistionCreditLineDAO = persistentSupport
                .getIPersistentManagementPositionCreditLine();

        List managementPositions = managementPosistionCreditLineDAO.readByTeacherAndExecutionPeriod(
                teacher.getIdInternal(), executionPeriod.getBeginDate(), executionPeriod.getEndDate());

        List infoManagementPositions = (List) CollectionUtils.collect(managementPositions,
                new Transformer() {

                    public Object transform(Object input) {
                        ManagementPositionCreditLine managementPositionCreditLine = (ManagementPositionCreditLine) input;
                        InfoManagementPositionCreditLine infoManagementPositionCreditLine = InfoManagementPositionCreditLine
                                .newInfoFromDomain(managementPositionCreditLine);
                        return infoManagementPositionCreditLine;
                    }
                });

        Collections.sort(infoManagementPositions, new BeanComparator("start"));
        return infoManagementPositions;
    }

    private List readOtherTypeCreditLine(Teacher teacher, ExecutionPeriod executionPeriod) {

        List otherCreditLines = teacher.getOtherTypeCreditLines();
        List infoOtherCreditLines = new ArrayList();
        for (int i = 0; i < otherCreditLines.size(); i++) {
            OtherTypeCreditLine otherTypeCreditLine = (OtherTypeCreditLine) otherCreditLines.get(i);
            if (otherTypeCreditLine.getExecutionPeriod().equals(executionPeriod)) {
                InfoOtherTypeCreditLine infoOtherTypeCreditLine = InfoOtherTypeCreditLine
                        .newInfoFromDomain(otherTypeCreditLine);
                infoOtherCreditLines.add(infoOtherTypeCreditLine);
            }
        }

        Collections.sort(infoOtherCreditLines, new BeanComparator("infoExecutionPeriod.beginDate"));
        return infoOtherCreditLines;
    }

    private List readDetailedProfessorships(Teacher teacher, ExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {

        List professorshipList = teacher.getProfessorships(executionPeriod);
        final List responsibleFors = teacher.responsibleFors();

        List detailedProfessorshipList = (List) CollectionUtils.collect(professorshipList,
                new Transformer() {

                    public Object transform(Object input) {
                        Professorship professorship = (Professorship) input;
                        InfoProfessorship infoProfessorShip = InfoProfessorshipWithInfoExecutionCourse
                                .newInfoFromDomain(professorship);

                        DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

                        List executionCourseCurricularCoursesList = getInfoCurricularCourses(
                                detailedProfessorship, professorship.getExecutionCourse());

                        detailedProfessorship.setResponsibleFor(Boolean.valueOf(responsibleFors
                                .contains(professorship)));

                        detailedProfessorship.setInfoProfessorship(infoProfessorShip);
                        detailedProfessorship
                                .setExecutionCourseCurricularCoursesList(executionCourseCurricularCoursesList);

                        return detailedProfessorship;
                    }

                    private List getInfoCurricularCourses(
                            final DetailedProfessorship detailedProfessorship,
                            ExecutionCourse executionCourse) {

                        List infoCurricularCourses = (List) CollectionUtils.collect(executionCourse
                                .getAssociatedCurricularCourses(), new Transformer() {

                            public Object transform(Object input) {
                                CurricularCourse curricularCourse = (CurricularCourse) input;
                                InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
                                        .newInfoFromDomain(curricularCourse);
                                if (curricularCourse.getDegreeCurricularPlan().getDegree()
                                        .getTipoCurso().equals(DegreeType.DEGREE)) {
                                    detailedProfessorship.setMasterDegreeOnly(Boolean.FALSE);
                                }
                                return infoCurricularCourse;
                            }
                        });
                        return infoCurricularCourses;
                    }
                });

        return detailedProfessorshipList;
    }

}
