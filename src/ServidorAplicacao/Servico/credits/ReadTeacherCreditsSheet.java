/*
 * Created on Nov 19, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.credits;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoProfessorship;
import DataBeans.InfoProfessorshipWithInfoExecutionCourse;
import DataBeans.InfoTeacher;
import DataBeans.credits.InfoCredits;
import DataBeans.credits.InfoManagementPositionCreditLine;
import DataBeans.credits.InfoOtherTypeCreditLine;
import DataBeans.credits.InfoServiceExemptionCreditLine;
import DataBeans.credits.TeacherCreditsSheetDTO;
import DataBeans.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import DataBeans.teacher.credits.InfoShiftProfessorship;
import DataBeans.teacher.professorship.DetailedProfessorship;
import DataBeans.teacher.professorship.InfoSupportLesson;
import DataBeans.teacher.workTime.InfoTeacherInstitutionWorkTime;
import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.IShiftProfessorship;
import Dominio.ISupportLesson;
import Dominio.ITeacher;
import Dominio.ResponsibleFor;
import Dominio.credits.IManagementPositionCreditLine;
import Dominio.credits.IOtherTypeCreditLine;
import Dominio.credits.IServiceExemptionCreditLine;
import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.credits.IPersistentManagementPositionCreditLine;
import ServidorPersistente.credits.IPersistentServiceExemptionCreditLine;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class ReadTeacherCreditsSheet implements IService {

    public ReadTeacherCreditsSheet() {
    }

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

    /**
     * @param sp
     * @param infoCredits
     * @param infoTeacher
     * @param infoExecutionPeriod
     * @return
     */
    private List readInfoMasterDegreeProfessorships(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws FenixServiceException {
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
    private List readInfoShiftProfessorships(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
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
            InfoShiftProfessorship infoShiftProfessorship = Cloner
                    .copyIShiftProfessorship2InfoShiftProfessorship(shiftProfessorship);
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
    private List readInfoSupportLessonList(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {

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
    private List readInfoTeacherInstitutionWorkingTime(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
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
            e2.printStackTrace(System.out);
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
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {

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
            throws FenixServiceException {

        TeacherCreditsSheetDTO teacherCreditsSheetDTO = new TeacherCreditsSheetDTO();

        ISuportePersistente sp;
        try {
            sp = SuportePersistenteOJB.getInstance();

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

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems with database!", e);
        }

        return teacherCreditsSheetDTO;
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param sp
     * @return
     */
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

    /**
     * @param teacher
     * @param executionPeriod
     * @param sp
     * @return
     */
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

    /**
     * @param teacher
     * @param executionPeriod
     * @param infoCredits
     * @param sp
     * @return
     */
    private List readOtherTypeCreditLine(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {

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

    /**
     * @param teacher
     * @param executionPeriod
     * @param sp
     * @return
     */
    //    private InfoCredits readCredits(ITeacher teacher, IExecutionPeriod
    // executionPeriod,
    //            ISuportePersistente sp) throws ExcepcaoPersistencia {
    //        IPersistentCredits creditsDAO = sp.getIPersistentCredits();
    //        ICredits credits = creditsDAO.readByTeacherAndExecutionPeriod(teacher,
    // executionPeriod);
    //        return credits == null ? null : Cloner.copyICredits2InfoCredits(credits);
    //    }
    /**
     * @param teacher
     * @param executionPeriod
     * @param sp
     * @return
     */
    private List readDetailedProfessorships(ITeacher teacher, IExecutionPeriod executionPeriod,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
        IPersistentResponsibleFor responsibleForDAO = sp.getIPersistentResponsibleFor();

        List professorshipList = professorshipDAO.readByTeacherAndExecutionPeriod(teacher,
                executionPeriod);
        final List responsibleFors = responsibleForDAO.readByTeacher(teacher);

        List detailedProfessorshipList = (List) CollectionUtils.collect(professorshipList,
                new Transformer() {

                    public Object transform(Object input) {
                        IProfessorship professorship = (IProfessorship) input;
                        InfoProfessorship infoProfessorShip = Cloner
                                .copyIProfessorship2InfoProfessorship(professorship);

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
                                        .getTipoCurso().equals(TipoCurso.LICENCIATURA_OBJ)) {
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