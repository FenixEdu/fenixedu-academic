package net.sourceforge.fenixedu.applicationTier.Servico.credits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;
import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.credits.IManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.credits.IOtherTypeCreditLine;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.teacher.workTime.ITeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentManagementPositionCreditLine;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadTeacherCreditsSheet implements IService {

    private IExecutionPeriod readExecutionPeriod(Integer executionPeriodId,
            IPersistentExecutionPeriod executionPeriodDAO) throws FenixServiceException,
            ExcepcaoPersistencia {
        IExecutionPeriod executionPeriod;
        if ((executionPeriodId == null) || (executionPeriodId.intValue() == 0)) {
            executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
        } else {
            executionPeriod = (IExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class,
                    executionPeriodId);
        }
        return executionPeriod;
    }

    private List readInfoMasterDegreeProfessorships(ITeacher teacher, IExecutionPeriod executionPeriod) {
        List professorships = teacher.getProfessorships();

        List infoMasterDegreeProfessorships = new ArrayList();
        for (int i = 0; i < professorships.size(); i++) {
            IProfessorship professorship = (IProfessorship) professorships.get(i);
            IExecutionCourse executionCourse = professorship.getExecutionCourse();

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
     * @param sp
     * @param infoCredits
     * @param infoTeacher
     * @param infoExecutionPeriod
     * @return
     */
    private List readInfoShiftProfessorships(ITeacher teacher, IExecutionPeriod executionPeriod) {
        List professorships = teacher.getProfessorships();
        List shiftProfessorships = new ArrayList();
        for (int i = 0; i < professorships.size(); i++) {
            IProfessorship professorship = (IProfessorship) professorships.get(i);
            IExecutionCourse executionCourse = professorship.getExecutionCourse();
            if (executionCourse.getExecutionPeriod().equals(executionPeriod)) {
                shiftProfessorships.addAll(professorship.getAssociatedShiftProfessorship());
            }
        }

        List infoShiftProfessorships = new ArrayList();
        for (int i = 0; i < shiftProfessorships.size(); i++) {
            IShiftProfessorship shiftProfessorship = (IShiftProfessorship) shiftProfessorships.get(i);
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
     * @param sp
     * @return
     */
    private List readInfoSupportLessonList(ITeacher teacher, IExecutionPeriod executionPeriod) {

        List professorships = teacher.getProfessorships();
        List supportLessonList = new ArrayList();
        for (int i = 0; i < professorships.size(); i++) {
            IProfessorship professorship = (IProfessorship) professorships.get(i);
            IExecutionCourse executionCourse = professorship.getExecutionCourse();
            if (executionCourse.getExecutionPeriod().equals(executionPeriod)) {
                supportLessonList.addAll(professorship.getSupportLessons());

            }
        }
        List infoSupportLessonList = new ArrayList();
        for (int i = 0; i < supportLessonList.size(); i++) {
            ISupportLesson supportLesson = (ISupportLesson) supportLessonList.get(i);

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
     * @param sp
     * @return
     */
    private List readInfoTeacherInstitutionWorkingTime(ITeacher teacher, IExecutionPeriod executionPeriod) {
        List<InfoTeacherInstitutionWorkTime> infoTeacherInstitutionWorkingTimeList = new ArrayList<InfoTeacherInstitutionWorkTime>();
        for (ITeacherInstitutionWorkTime item : teacher.getInstitutionWorkTimePeriods()) {
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

    private ITeacher readTeacher(Integer teacherNumber, IPersistentTeacher teacherDAO)
            throws FenixServiceException, ExcepcaoPersistencia {
        ITeacher teacher;
        teacher = teacherDAO.readByNumber(teacherNumber);
        return teacher;
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param infoCredits
     * @param sp
     * @return
     */
    private List readTeacherDegreeFinalProjectStudentList(ITeacher teacher,
            IExecutionPeriod executionPeriod) {

        List<InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson> infoList = new ArrayList<InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson>();
        for (ITeacherDegreeFinalProjectStudent student : teacher.getDegreeFinalProjectStudents()) {
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

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
        IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
        IExecutionPeriod executionPeriod = readExecutionPeriod(executionPeriodId, executionPeriodDAO);
        ITeacher teacher = readTeacher(infoTeacherParam.getTeacherNumber(), teacherDAO);

        List infoMasterDegreeProfessorships = readInfoMasterDegreeProfessorships(teacher,
                executionPeriod);

        List infoShiftProfessorships = readInfoShiftProfessorships(teacher, executionPeriod);

        List infoTimeInstitutionWorkingTimeList = readInfoTeacherInstitutionWorkingTime(teacher,
                executionPeriod);

        List infoSupportLessonList = readInfoSupportLessonList(teacher, executionPeriod);

        List infoTeacherDegreeFinalProjectStudentList = readTeacherDegreeFinalProjectStudentList(
                teacher, executionPeriod);

        List detailedProfessorshipList = readDetailedProfessorships(teacher, executionPeriod, sp);

        List otherTypeCreditLineList = readOtherTypeCreditLine(teacher, executionPeriod);

        List infoManagementPositions = readManagementPositions(teacher, executionPeriod, sp);

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

    private List readManagementPositions(ITeacher teacher, IExecutionPeriod executionPeriod,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IPersistentManagementPositionCreditLine managementPosistionCreditLineDAO = sp
                .getIPersistentManagementPositionCreditLine();

        List managementPositions = managementPosistionCreditLineDAO.readByTeacherAndExecutionPeriod(
                teacher.getIdInternal(), executionPeriod.getBeginDate(), executionPeriod.getEndDate());

        List infoManagementPositions = (List) CollectionUtils.collect(managementPositions,
                new Transformer() {

                    public Object transform(Object input) {
                        IManagementPositionCreditLine managementPositionCreditLine = (IManagementPositionCreditLine) input;
                        InfoManagementPositionCreditLine infoManagementPositionCreditLine = InfoManagementPositionCreditLine
                                .newInfoFromDomain(managementPositionCreditLine);
                        return infoManagementPositionCreditLine;
                    }
                });

        Collections.sort(infoManagementPositions, new BeanComparator("start"));
        return infoManagementPositions;
    }

    private List readOtherTypeCreditLine(ITeacher teacher, IExecutionPeriod executionPeriod) {

        List otherCreditLines = teacher.getOtherTypeCreditLines();
        List infoOtherCreditLines = new ArrayList();
        for (int i = 0; i < otherCreditLines.size(); i++) {
            IOtherTypeCreditLine otherTypeCreditLine = (IOtherTypeCreditLine) otherCreditLines.get(i);
            if (otherTypeCreditLine.getExecutionPeriod().equals(executionPeriod)) {
                InfoOtherTypeCreditLine infoOtherTypeCreditLine = InfoOtherTypeCreditLine
                        .newInfoFromDomain(otherTypeCreditLine);
                infoOtherCreditLines.add(infoOtherTypeCreditLine);
            }
        }

        Collections.sort(infoOtherCreditLines, new BeanComparator("infoExecutionPeriod.beginDate"));
        return infoOtherCreditLines;
    }

    private List readDetailedProfessorships(ITeacher teacher, IExecutionPeriod executionPeriod,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();

        List professorshipList = professorshipDAO.readByTeacherAndExecutionPeriod(teacher
                .getIdInternal(), executionPeriod.getIdInternal());

        final List responsibleFors = teacher.responsibleFors();

        List detailedProfessorshipList = (List) CollectionUtils.collect(professorshipList,
                new Transformer() {

                    public Object transform(Object input) {
                        IProfessorship professorship = (IProfessorship) input;
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
                            IExecutionCourse executionCourse) {

                        List infoCurricularCourses = (List) CollectionUtils.collect(executionCourse
                                .getAssociatedCurricularCourses(), new Transformer() {

                            public Object transform(Object input) {
                                ICurricularCourse curricularCourse = (ICurricularCourse) input;
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
