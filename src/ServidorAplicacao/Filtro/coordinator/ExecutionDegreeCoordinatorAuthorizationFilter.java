/*
 * Created on Dec 21, 2003
 *  
 */
package ServidorAplicacao.Filtro.coordinator;

import java.util.List;

import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ExecutionDegreeCoordinatorAuthorizationFilter extends DomainObjectAuthorizationFilter
{
    /**
	 *  
	 */
    public ExecutionDegreeCoordinatorAuthorizationFilter()
    {
        super();
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

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter#verifyCondition(ServidorAplicacao.IUserView,
	 *      java.lang.Integer)
	 */
    protected boolean verifyCondition(IUserView id, Integer objectId)
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            ICursoExecucao executionDegree = (ICursoExecucao) persistentExecutionDegree.readByOId(new CursoExecucao(objectId), false);
            ITeacher coordinator = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            List executionDegrees = persistentExecutionDegree.readByTeacher(coordinator);

            return executionDegrees.contains(executionDegree);
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
}
