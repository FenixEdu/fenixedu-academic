/*
 * Created on Nov 19, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.credits;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoProfessorship;
import DataBeans.InfoTeacher;
import DataBeans.credits.InfoManagementPositionCreditLine;
import DataBeans.credits.InfoOtherTypeCreditLine;
import DataBeans.credits.InfoServiceExemptionCreditLine;
import DataBeans.credits.TeacherCreditsSheetDTO;
import DataBeans.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import DataBeans.teacher.credits.InfoCredits;
import DataBeans.teacher.credits.InfoShiftProfessorship;
import DataBeans.teacher.professorship.DetailedProfessorship;
import DataBeans.teacher.professorship.InfoSupportLesson;
import DataBeans.teacher.workTime.InfoTeacherInstitutionWorkTime;
import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.ICredits;
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
import ServidorPersistente.IPersistentShiftProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.credits.IPersistentCredits;
import ServidorPersistente.credits.IPersistentManagementPositionCreditLine;
import ServidorPersistente.credits.IPersistentOtherTypeCreditLine;
import ServidorPersistente.credits.IPersistentServiceExemptionCreditLine;
import ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;
import ServidorPersistente.teacher.professorship.IPersistentSupportLesson;
import ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class ReadTeacherCreditsSheet implements IService
{

    public ReadTeacherCreditsSheet()
    {
    }

    private IExecutionPeriod readExecutionPeriod(
        Integer executionPeriodId,
        IPersistentExecutionPeriod executionPeriodDAO)
        throws FenixServiceException
    {
        IExecutionPeriod executionPeriod;
        try
        {
            if ((executionPeriodId == null) || (executionPeriodId.intValue() == 0))
            {
                executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
            } else
            {
                executionPeriod =
                    (IExecutionPeriod) executionPeriodDAO.readByOId(
                        new ExecutionPeriod(executionPeriodId),
                        false);
            }
        } catch (ExcepcaoPersistencia e1)
        {
            e1.printStackTrace(System.out);
            throw new FenixServiceException("Error getting execution period!", e1);
        }
        return executionPeriod;
    }
    /**
     * @param sp
     * @param infoTeacher
     * @param infoExecutionPeriod
     * @return
     */
    private List readInfoMasterDegreeProfessorships(
        ISuportePersistente sp,
        ITeacher teacher,
        IExecutionPeriod executionPeriod)
        throws FenixServiceException
    {
        IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
        List masterDegreeProfessorshipList;
        try
        {
            masterDegreeProfessorshipList =
                professorshipDAO.readByTeacherAndTypeOfDegree(teacher, TipoCurso.MESTRADO_OBJ);
        } catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems getting master degree professorships!", e);
        }
        List infoMasterDegreeProfessorships =
            (List) CollectionUtils.collect(masterDegreeProfessorshipList, new Transformer()
        {

            public Object transform(Object input)
            {
                IProfessorship professorship = (IProfessorship) input;
                InfoProfessorship infoProfessorship =
                    Cloner.copyIProfessorship2InfoProfessorship(professorship);
                return infoProfessorship;
            }
        });

        return infoMasterDegreeProfessorships;
    }
    /**
     * @param sp
     * @param infoTeacher
     * @param infoExecutionPeriod
     * @return
     */
    private List readInfoShiftProfessorships(
        ISuportePersistente sp,
        ITeacher teacher,
        IExecutionPeriod executionPeriod)
        throws ExcepcaoPersistencia
    {
        IPersistentShiftProfessorship shiftProfessorshipDAO = sp.getIPersistentShiftProfessorship();

        List shiftProfessorships =
            shiftProfessorshipDAO.readByTeacherAndExecutionPeriodAndDegreeType(
                teacher,
                executionPeriod,
                TipoCurso.LICENCIATURA_OBJ);

        List infoShiftProfessorships =
            (List) CollectionUtils.collect(shiftProfessorships, new Transformer()
        {

            public Object transform(Object input)
            {
                IShiftProfessorship shiftProfessorship = (IShiftProfessorship) input;
                InfoShiftProfessorship infoShiftProfessorship =
                    Cloner.copyIShiftProfessorship2InfoShiftProfessorship(shiftProfessorship);
                return infoShiftProfessorship;
            }
        });

        return infoShiftProfessorships;
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param sp
     * @return
     */
    private List readInfoSupportLessonList(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();
        List supportLessonList =
            supportLessonDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);
        List infoSupportLessonList = (List) CollectionUtils.collect(supportLessonList, new Transformer()
        {

            public Object transform(Object input)
            {
                ISupportLesson supportLesson = (ISupportLesson) input;
                InfoSupportLesson infoSupportLesson =
                    Cloner.copyISupportLesson2InfoSupportLesson(supportLesson);
                return infoSupportLesson;
            }
        });

        return infoSupportLessonList;
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param sp
     * @return
     */
    private List readInfoTeacherInstitutionWorkingTime(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO =
            sp.getIPersistentTeacherInstitutionWorkingTime();
        List teacherInstitutionWorkingTimeList =
            teacherInstitutionWorkingTimeDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);

        List infoTeacherInstitutionWorkingTimeList =
            (List) CollectionUtils.collect(teacherInstitutionWorkingTimeList, new Transformer()
        {

            public Object transform(Object input)
            {
                ITeacherInstitutionWorkTime teacherInstitutionWorkTime =
                    (ITeacherInstitutionWorkTime) input;
                InfoTeacherInstitutionWorkTime infoTeacherInstitutionWorkTime =
                    Cloner.copyITeacherInstitutionWorkingTime2InfoTeacherInstitutionWorkTime(
                        teacherInstitutionWorkTime);
                return infoTeacherInstitutionWorkTime;
            }
        });
        return infoTeacherInstitutionWorkingTimeList;
    }

    private ITeacher readTeacher(Integer teacherNumber, IPersistentTeacher teacherDAO)
        throws FenixServiceException
    {
        ITeacher teacher;
        try
        {
            teacher = teacherDAO.readByNumber(teacherNumber);
        } catch (ExcepcaoPersistencia e2)
        {
            e2.printStackTrace(System.out);
            throw new FenixServiceException("Error getting teacher!", e2);
        }
        return teacher;
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param sp
     * @return
     */
    private List readTeacherDegreeFinalProjectStudentList(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentTeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudentDAO =
            sp.getIPersistentTeacherDegreeFinalProjectStudent();

        List teacherDegreeFinalProjectStudentList =
            teacherDegreeFinalProjectStudentDAO.readByTeacherAndExecutionPeriod(
                teacher,
                executionPeriod);
        List infoTeacherDegreeFinalProjectStudentList =
            (List) CollectionUtils.collect(teacherDegreeFinalProjectStudentList, new Transformer()
        {

            public Object transform(Object input)
            {
                ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent =
                    (ITeacherDegreeFinalProjectStudent) input;
                InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent =
                    Cloner.copyITeacherDegreeFinalProjectStudent2InfoTeacherDegreeFinalProjectStudent(
                        teacherDegreeFinalProjectStudent);
                return infoTeacherDegreeFinalProjectStudent;
            }
        });

        return infoTeacherDegreeFinalProjectStudentList;
    }

    public TeacherCreditsSheetDTO run(InfoTeacher infoTeacherParam, Integer executionPeriodId)
        throws FenixServiceException
    {

        TeacherCreditsSheetDTO teacherCreditsSheetDTO = new TeacherCreditsSheetDTO();

        ISuportePersistente sp;
        try
        {
            sp = SuportePersistenteOJB.getInstance();

            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod =
                readExecutionPeriod(executionPeriodId, executionPeriodDAO);
            ITeacher teacher = readTeacher(infoTeacherParam.getTeacherNumber(), teacherDAO);

            List infoMasterDegreeProfessorships =
                readInfoMasterDegreeProfessorships(sp, teacher, executionPeriod);

            List infoShiftProfessorships = readInfoShiftProfessorships(sp, teacher, executionPeriod);

            List infoTimeInstitutionWorkingTimeList =
                readInfoTeacherInstitutionWorkingTime(teacher, executionPeriod, sp);

            List infoSupportLessonList = readInfoSupportLessonList(teacher, executionPeriod, sp);

            List infoTeacherDegreeFinalProjectStudentList =
                readTeacherDegreeFinalProjectStudentList(teacher, executionPeriod, sp);

            List detailedProfessorshipList = readDetailedProfessorships(teacher, executionPeriod, sp);

            List otherTypeCreditLineList = readOtherTypeCreditLine(teacher, executionPeriod, sp);

            List infoManagementPositions = readManagementPositions(teacher, executionPeriod, sp);
            List infoServiceExemptions = readServiceExcemptions(teacher, executionPeriod, sp);

            InfoCredits infoCredits = readCredits(teacher, executionPeriod, sp);

            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(executionPeriod);

            teacherCreditsSheetDTO.setInfoTeacher(infoTeacher);
            teacherCreditsSheetDTO.setInfoExecutionPeriod(infoExecutionPeriod);
            teacherCreditsSheetDTO.setInfoCredits(infoCredits);

            teacherCreditsSheetDTO.setInfoMasterDegreeProfessorships(infoMasterDegreeProfessorships);

            teacherCreditsSheetDTO.setInfoTeacherInstitutionWorkingTimeList(
                infoTimeInstitutionWorkingTimeList);

            teacherCreditsSheetDTO.setInfoShiftProfessorshipList(infoShiftProfessorships);

            teacherCreditsSheetDTO.setInfoSupportLessonList(infoSupportLessonList);

            teacherCreditsSheetDTO.setInfoTeacherDegreeFinalProjectStudentList(
                infoTeacherDegreeFinalProjectStudentList);

            teacherCreditsSheetDTO.setDetailedProfessorshipList(detailedProfessorshipList);

            teacherCreditsSheetDTO.setInfoTeacherOtherTypeCreditLineList(otherTypeCreditLineList);
            
            teacherCreditsSheetDTO.setInfoManagementPositions(infoManagementPositions);
            teacherCreditsSheetDTO.setInfoServiceExemptions(infoServiceExemptions);

        } catch (ExcepcaoPersistencia e)
        {
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
    private List readServiceExcemptions(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        IPersistentServiceExemptionCreditLine serviceExemptionCreditLineDAO =
            sp.getIPersistentServiceExemptionCreditLine();
        
        List serviceExemptions = serviceExemptionCreditLineDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);
        
        List infoServiceExemptions =
            (List) CollectionUtils.collect(serviceExemptions, new Transformer()
                    {

                public Object transform(Object input)
                {
                    IServiceExemptionCreditLine serviceExemptionCreditLine =
                        (IServiceExemptionCreditLine) input;
                    InfoServiceExemptionCreditLine infoServiceExemptionCreditLine =
                        Cloner.copyIServiceExemptionCreditLine2InfoServiceExemptionCreditLine(serviceExemptionCreditLine);
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
    private List readManagementPositions(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        IPersistentManagementPositionCreditLine managementPosistionCreditLineDAO =
            sp.getIPersistentManagementPositionCreditLine();
        
        List managementPositions = managementPosistionCreditLineDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);
        
        List infoManagementPositions =
            (List) CollectionUtils.collect(managementPositions, new Transformer()
                    {

                public Object transform(Object input)
                {
                    IManagementPositionCreditLine managementPositionCreditLine =
                        (IManagementPositionCreditLine) input;
                    InfoManagementPositionCreditLine infoManagementPositionCreditLine =
                        Cloner.copyIManagementPositionCreditLine2InfoManagementPositionCreditLine(managementPositionCreditLine);
                    return infoManagementPositionCreditLine;
                }
            });
        
        return infoManagementPositions;
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param sp
     * @return
     */
    private List readOtherTypeCreditLine(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentOtherTypeCreditLine otherTypeCreditLineDAO = sp.getIPersistentOtherTypeCreditLine();

        List otherCreditLines =
            otherTypeCreditLineDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);

        List infoOtherCreditLines = (List) CollectionUtils.collect(otherCreditLines, new Transformer()
        {

            public Object transform(Object input)
            {
                IOtherTypeCreditLine otherTypeCreditLine = (IOtherTypeCreditLine) input;
                InfoOtherTypeCreditLine infoOtherTypeCreditLine =
                    Cloner.copyIOtherTypeCreditLine2InfoOtherCreditLine(otherTypeCreditLine);
                return infoOtherTypeCreditLine;
            }
        });
        return infoOtherCreditLines;
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param sp
     * @return
     */
    private InfoCredits readCredits(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentCredits creditsDAO = sp.getIPersistentCredits();
        ICredits credits = creditsDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);
        return credits == null ? null : Cloner.copyICredits2InfoCredits(credits);
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param sp
     * @return
     */
    private List readDetailedProfessorships(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
        IPersistentResponsibleFor responsibleForDAO = sp.getIPersistentResponsibleFor();

        List professorshipList =
            professorshipDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);
        final List responsibleFors = responsibleForDAO.readByTeacher(teacher);

        List detailedProfessorshipList =
            (List) CollectionUtils.collect(professorshipList, new Transformer()
        {

            public Object transform(Object input)
            {
                IProfessorship professorship = (IProfessorship) input;
                InfoProfessorship infoProfessorShip =
                    Cloner.copyIProfessorship2InfoProfessorship(professorship);

                List executionCourseCurricularCoursesList =
                    getInfoCurricularCourses(professorship.getExecutionCourse());

                DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

                IResponsibleFor responsibleFor = new ResponsibleFor();
                responsibleFor.setExecutionCourse(professorship.getExecutionCourse());
                responsibleFor.setTeacher(professorship.getTeacher());
                detailedProfessorship.setResponsibleFor(
                    Boolean.valueOf(responsibleFors.contains(responsibleFor)));

                detailedProfessorship.setInfoProfessorship(infoProfessorShip);
                detailedProfessorship.setExecutionCourseCurricularCoursesList(
                    executionCourseCurricularCoursesList);

                return detailedProfessorship;
            }

            private List getInfoCurricularCourses(IExecutionCourse executionCourse)
            {

                List infoCurricularCourses =
                    (
                        List) CollectionUtils
                            .collect(executionCourse.getAssociatedCurricularCourses(), new Transformer()
                {

                    public Object transform(Object input)
                    {
                        ICurricularCourse curricularCourse = (ICurricularCourse) input;
                        InfoCurricularCourse infoCurricularCourse =
                            Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                        return infoCurricularCourse;
                    }
                });
                return infoCurricularCourses;
            }
        });

        return detailedProfessorshipList;
    }
}