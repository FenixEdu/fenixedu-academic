/*
 * Created on Dec 17, 2003 by jpvl
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
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.ResponsibleFor;
import Dominio.Teacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class ReadDetailedTeacherProfessorshipsAbstractService implements IService
{
    /**
	 * @author jpvl
	 */
    public class NotFoundTeacher extends FenixServiceException
    {

    }
    protected List getDetailedProfessorships(List professorships, final List responsibleFors,
            ISuportePersistente sp)
    {

        List detailedProfessorshipList = (List) CollectionUtils.collect(professorships,
                new Transformer()
                {

                    public Object transform(Object input)
                    {
                        IProfessorship professorship = (IProfessorship) input;
                        InfoProfessorship infoProfessorShip = Cloner
                                .copyIProfessorship2InfoProfessorship(professorship);

                        List executionCourseCurricularCoursesList = getInfoCurricularCourses(professorship
                                .getExecutionCourse());

                        DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

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

    protected ISuportePersistente getDAOFactory() throws ExcepcaoPersistencia
    {
        return SuportePersistenteOJB.getInstance();
    }

    /**
	 * @param teacherId
	 * @return
	 */
    protected ITeacher readTeacher(Integer teacherId, IPersistentTeacher teacherDAO)
            throws NotFoundTeacher
    {
        ITeacher teacher = (ITeacher) teacherDAO.readByOId(new Teacher(teacherId), false);
        if (teacher == null)
        {
            throw new NotFoundTeacher();
        }
        return teacher;
    }
}