/*
 * Created on 27/Mai/2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoProfessorship;
import DataBeans.teacher.professorship.DetailedProfessorship;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.ResponsibleFor;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class ReadProfessorships implements IService
{
    public ReadProfessorships()
    {
    }

    public List run(IUserView userView) throws FenixServiceException
    {
        try
        {
            ISuportePersistente persistentSuport;
            persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentResponsibleFor responsibleForDAO = persistentSuport
                    .getIPersistentResponsibleFor();

            IPersistentProfessorship persistentProfessorship = persistentSuport
                    .getIPersistentProfessorship();
            IPersistentTeacher teacherDAO = persistentSuport.getIPersistentTeacher();
            ITeacher teacher = teacherDAO.readTeacherByUsername(userView.getUtilizador());

            List professorships = persistentProfessorship.readByTeacher(teacher);
            final List responsibleFors = responsibleForDAO.readByTeacher(teacher);

            List detailedProfessorshipList = (List) CollectionUtils.collect(professorships,
                    new Transformer()
                    {

                        public Object transform(Object input)
                        {
                            IProfessorship professorship = (IProfessorship) input;
                            InfoProfessorship infoProfessorShip = Cloner
                                    .copyIProfessorship2InfoProfessorship(professorship);
                            IResponsibleFor responsibleFor = new ResponsibleFor();

                            responsibleFor.setExecutionCourse(professorship.getExecutionCourse());
                            responsibleFor.setTeacher(professorship.getTeacher());

                            

                            List executionCourseCurricularCoursesList = getInfoCurricularCourses(professorship
                                    .getExecutionCourse());

                            DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

                            detailedProfessorship.setResponsibleFor(Boolean.valueOf(responsibleFors.contains(responsibleFor)));
                            
                            detailedProfessorship.setInfoProfessorship(infoProfessorShip);
                            detailedProfessorship
                                    .setExecutionCourseCurricularCoursesList(executionCourseCurricularCoursesList);

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
                                                    .copyCurricularCourse2InfoCurricularCourse(curricularCourse);
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