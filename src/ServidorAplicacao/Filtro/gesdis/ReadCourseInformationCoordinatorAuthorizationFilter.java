/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Filtro.gesdis;

import java.util.List;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCourseInformationCoordinatorAuthorizationFilter extends DomainObjectAuthorizationFilter
{
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.framework.DomainObjectTeacherAuthorizationFilter#domainObjectBelongsToTeacher(ServidorAplicacao.IUserView,
	 *      java.lang.Integer)
	 */
    protected boolean verifyCondition(IUserView id, Integer objectId)
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();

            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            IExecutionCourse executionCourse =
                (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, objectId);
            List executionDegrees =
                persistentExecutionDegree.readByExecutionCourseAndByTeacher(executionCourse, teacher);

            return !executionDegrees.isEmpty();
        } catch (ExcepcaoPersistencia e)
        {
            System.out.println("Filter error(ExcepcaoPersistente): " + e.getMessage());
            return false;
        } catch (Exception e)
        {
            System.out.println("Filter error(Unknown): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter#getRoleType()
	 */
    protected RoleType getRoleType()
    {
        return RoleType.COORDINATOR;
    }
}
