/*
 * Created on Nov 21, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.professorship;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoProfessorship;
import DataBeans.teacher.professorship.DetailedProfessorship;
import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class ReadDetailedTeacherProfessorshipsByExecutionPeriod implements IService
{
    public ReadDetailedTeacherProfessorshipsByExecutionPeriod()
    {
    }

    public List run(Integer teacherOID, Integer executionPeriodOID) throws FenixServiceException
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = null;
            if (executionPeriodOID == null)
            {
                executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
            }
            else
            {
                executionPeriod = (IExecutionPeriod) executionPeriodDAO.readByOId(new ExecutionPeriod(
                        executionPeriodOID),
                        false);
            }

            ITeacher teacher = (ITeacher) teacherDAO.readByOId(new Teacher(teacherOID), false);

            IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();

            List professorships = professorshipDAO.readByTeacherAndExecutionPeriod(teacher,
                    executionPeriod);

            List detailedProfessorshipList = (List) CollectionUtils.collect(professorships,
                    new Transformer()
                    {

                        public Object transform(Object input)
                        {
                            IProfessorship professorship = (IProfessorship) input;
                            InfoProfessorship infoProfessorShip = Cloner
                                    .copyIProfessorship2InfoProfessorship(professorship);

                            List executionCourseCurricularCoursesList = getInfoCurricularCourses(
                                    professorship.getExecutionCourse());
                            
                            DetailedProfessorship detailedProfessorship = new DetailedProfessorship();
                            
                            detailedProfessorship.setInfoProfessorship(infoProfessorShip);
                            detailedProfessorship.setExecutionCourseCurricularCoursesList(
                                    executionCourseCurricularCoursesList);

                            return detailedProfessorship;
                        }

                        private List getInfoCurricularCourses(IExecutionCourse executionCourse)
                        {

                            List infoCurricularCourses = (List) CollectionUtils.collect(executionCourse
                                    .getAssociatedCurricularCourses(),
                                    new Transformer()
                                    {

                                        public Object transform(Object input)
                                        {
                                            ICurricularCourse curricularCourse = (ICurricularCourse) input;
                                            InfoCurricularCourse infoCurricularCourse = Cloner
                                                    .copyCurricularCourse2InfoCurricularCourse(
                                                            curricularCourse);
                                            return infoCurricularCourse;
                                        }
                                    });
                            return infoCurricularCourses;
                        }
                    });

            return detailedProfessorshipList;
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!", e);
        }

    }
}