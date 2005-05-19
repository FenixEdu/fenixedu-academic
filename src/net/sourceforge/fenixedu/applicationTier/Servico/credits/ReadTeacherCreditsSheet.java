/*
 * Created on Nov 19, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorshipWithInfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoManagementPositionCreditLine;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoOtherTypeCreditLine;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoServiceExemptionCreditLine;
import net.sourceforge.fenixedu.dataTransferObject.credits.TeacherCreditsSheetDTO;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorshipWithProfessorshipAndExecutionCourseAndShiftAndLessons;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.DetailedProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.InfoSupportLesson;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InfoTeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;
import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ResponsibleFor;
import net.sourceforge.fenixedu.domain.credits.IManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.credits.IOtherTypeCreditLine;
import net.sourceforge.fenixedu.domain.credits.IServiceExemptionCreditLine;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.teacher.workTime.ITeacherInstitutionWorkTime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentServiceExemptionCreditLine;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jpvl
 */
public class ReadTeacherCreditsSheet implements IService {

    private IExecutionPeriod readExecutionPeriod(Integer executionPeriodId,
            IPersistentExecutionPeriod executionPeriodDAO) throws FenixServiceException {
        IExecutionPeriod executionPeriod;
        try {
            if ((executionPeriodId == null) || (executionPeriodId.intValue() == 0)) {
                executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
            } else {
                executionPeriod = (IExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class,
                        executionPeriodId);
            }
        } catch (ExcepcaoPersistencia e1) {

            throw new FenixServiceException("Error getting execution period!", e1);
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

            InfoSupportLesson infoSupportLesson = Cloner
                    .copyISupportLesson2InfoSupportLesson(supportLesson);
            infoSupportLessonList.add(infoSupportLesson);
        }
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
        List teacherInstitutionWorkingTimeList = teacher.getInstitutionWorkTimePeriods();

        List infoTeacherInstitutionWorkingTimeList = new ArrayList();
        for (int i = 0; i < teacherInstitutionWorkingTimeList.size(); i++) {
            ITeacherInstitutionWorkTime item = (ITeacherInstitutionWorkTime) teacherInstitutionWorkingTimeList
                    .get(i);
            if (item.getExecutionPeriod().equals(executionPeriod)) {
                InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime = Cloner
                        .copyITeacherInstitutionWorkingTime2InfoTeacherInstitutionWorkTime(item);
                infoTeacherInstitutionWorkingTimeList.add(infoTeacherInstitutionWorkTime);
            }
        }
        return infoTeacherInstitutionWorkingTimeList;
    }

    private ITeacher readTeacher(Integer teacherNumber, IPersistentTeacher teacherDAO)
            throws FenixServiceException {
        ITeacher teacher;
        try {
            teacher = teacherDAO.readByNumber(teacherNumber);
        } catch (ExcepcaoPersistencia e2) {
            throw new FenixServiceException("Error getting teacher!", e2);
        }
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

        List teacherDegreeFinalProjectStudentList = teacher.getDegreeFinalProjectStudents();

        List infoList = new ArrayList();
        for (int i = 0; i < teacherDegreeFinalProjectStudentList.size(); i++) {
            ITeacherDegreeFinalProjectStudent student = (ITeacherDegreeFinalProjectStudent) teacherDegreeFinalProjectStudentList
                    .get(i);
            if (student.getExecutionPeriod().equals(executionPeriod)) {
                InfoTeacherDegreeFinalProjectStudent infoStudent = Cloner
                        .copyITeacherDegreeFinalProjectStudent2InfoTeacherDegreeFinalProjectStudent(student);
                infoList.add(infoStudent);
            }
        }
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

        List infoServiceExemptions = readServiceExcemptions(teacher, executionPeriod, sp);

        InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(executionPeriod);

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

    private List readServiceExcemptions(ITeacher teacher, IExecutionPeriod executionPeriod,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IPersistentServiceExemptionCreditLine serviceExemptionCreditLineDAO = sp
                .getIPersistentServiceExemptionCreditLine();

        List serviceExemptions = serviceExemptionCreditLineDAO.readByTeacherAndExecutionPeriod(teacher,
                executionPeriod);

        List infoServiceExemptions = (List) CollectionUtils.collect(serviceExemptions,
                new Transformer() {

                    public Object transform(Object input) {
                        IServiceExemptionCreditLine serviceExemptionCreditLine = (IServiceExemptionCreditLine) input;
                        InfoServiceExemptionCreditLine infoServiceExemptionCreditLine = Cloner
                                .copyIServiceExemptionCreditLine2InfoServiceExemptionCreditLine(serviceExemptionCreditLine);
                        return infoServiceExemptionCreditLine;
                    }
                });

        return infoServiceExemptions;
    }

    private List readManagementPositions(ITeacher teacher, IExecutionPeriod executionPeriod,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IPersistentManagementPositionCreditLine managementPosistionCreditLineDAO = sp
                .getIPersistentManagementPositionCreditLine();

        List managementPositions = managementPosistionCreditLineDAO.readByTeacherAndExecutionPeriod(
                teacher, executionPeriod);

        List infoManagementPositions = (List) CollectionUtils.collect(managementPositions,
                new Transformer() {

                    public Object transform(Object input) {
                        IManagementPositionCreditLine managementPositionCreditLine = (IManagementPositionCreditLine) input;
                        InfoManagementPositionCreditLine infoManagementPositionCreditLine = Cloner
                                .copyIManagementPositionCreditLine2InfoManagementPositionCreditLine(managementPositionCreditLine);
                        return infoManagementPositionCreditLine;
                    }
                });

        return infoManagementPositions;
    }

    private List readOtherTypeCreditLine(ITeacher teacher, IExecutionPeriod executionPeriod) {

        List otherCreditLines = teacher.getOtherTypeCreditLines();
        List infoOtherCreditLines = new ArrayList();
        for (int i = 0; i < otherCreditLines.size(); i++) {
            IOtherTypeCreditLine otherTypeCreditLine = (IOtherTypeCreditLine) otherCreditLines.get(i);
            if (otherTypeCreditLine.getExecutionPeriod().equals(executionPeriod)) {
                InfoOtherTypeCreditLine infoOtherTypeCreditLine = Cloner
                        .copyIOtherTypeCreditLine2InfoOtherCreditLine(otherTypeCreditLine);
                infoOtherCreditLines.add(infoOtherTypeCreditLine);
            }
        }
        return infoOtherCreditLines;
    }

    private List readDetailedProfessorships(ITeacher teacher, IExecutionPeriod executionPeriod,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
        IPersistentResponsibleFor responsibleForDAO = sp.getIPersistentResponsibleFor();

        List professorshipList = professorshipDAO.readByTeacherAndExecutionPeriod(teacher
                .getIdInternal(), executionPeriod.getIdInternal());
        final List responsibleFors = responsibleForDAO.readByTeacher(teacher.getIdInternal());

        List detailedProfessorshipList = (List) CollectionUtils.collect(professorshipList,
                new Transformer() {

                    public Object transform(Object input) {
                        IProfessorship professorship = (IProfessorship) input;
                        InfoProfessorship infoProfessorShip = InfoProfessorshipWithInfoExecutionCourse
                                .newInfoFromDomain(professorship);

                        DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

                        List executionCourseCurricularCoursesList = getInfoCurricularCourses(
                                detailedProfessorship, professorship.getExecutionCourse());

                        IResponsibleFor responsibleFor = new ResponsibleFor();
                        responsibleFor.setExecutionCourse(professorship.getExecutionCourse());
                        responsibleFor.setTeacher(professorship.getTeacher());
                        detailedProfessorship.setResponsibleFor(Boolean.valueOf(responsibleFors
                                .contains(responsibleFor)));

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
                                InfoCurricularCourse infoCurricularCourse = Cloner
                                        .copyCurricularCourse2InfoCurricularCourse(curricularCourse);
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