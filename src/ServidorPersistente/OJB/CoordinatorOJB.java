package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Coordinator;
import Dominio.ICoordinator;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCoordinator;

/**
 * fenix-head ServidorPersistente.OJB
 * 
 * @author João Mota 28/Out/2003
 * 
 * @author Francisco Paulo 27/Out/2004 frnp@mega.ist.utl.pt(edit)
 *  
 * CoordinatorOJB class, implements the read methods available for coordinators
 * 
 */

public class CoordinatorOJB extends PersistentObjectOJB implements
		IPersistentCoordinator {

	public List readExecutionDegreesByTeacher(ITeacher teacher)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
		List coordinators = queryList(Coordinator.class, criteria);
		if (coordinators == null) {
			return null;
		}

		Iterator iter = coordinators.iterator();
		List executionDegrees = new ArrayList();
		while (iter.hasNext()) {
			ICoordinator coordinator = (ICoordinator) iter.next();
			ICursoExecucao executionDegree = coordinator.getExecutionDegree();

			if (!executionDegrees.contains(executionDegree)) {
				executionDegrees.add(executionDegree);
			}
		}
		return executionDegrees;
	}

	public List readCurricularPlansByTeacher(ITeacher teacher)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
		//List containing all the courses the teacher coordinates
		List coordinators = queryList(Coordinator.class, criteria);
		if (coordinators == null) {
			return null;
		}

		Iterator iter = coordinators.iterator();
		List degreeCurricularPlans = new ArrayList();

		ICoordinator coordinator = null;
		ICursoExecucao executionDegree = null;

		while (iter.hasNext()) {
			//get coordinator from the coordinators list
			coordinator = (ICoordinator) iter.next();
			//get the execution degree for that coordinator
			executionDegree = coordinator.getExecutionDegree();
			//get the curricular plan from the execution degree
			IDegreeCurricularPlan curricularPlan = executionDegree
					.getCurricularPlan();

			if (!degreeCurricularPlans.contains(curricularPlan)) {
				degreeCurricularPlans.add(curricularPlan);
			}
		}
		return degreeCurricularPlans;
	}

	public List readCoordinatorsByExecutionDegree(ICursoExecucao executionDegree)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionDegree.idInternal", executionDegree
				.getIdInternal());
		return queryList(Coordinator.class, criteria);

	}

	public ICoordinator readCoordinatorByTeacherAndExecutionDegreeId(
			ITeacher teacher, Integer executionDegreeId)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionDegree.idInternal", executionDegreeId);
		criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
		return (ICoordinator) queryObject(Coordinator.class, criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentCoordinator#readCoordinatorByTeacherAndExecutionDegree(Dominio.ITeacher,
	 *      Dominio.ICursoExecucao)
	 */
	public ICoordinator readCoordinatorByTeacherAndExecutionDegree(
			ITeacher teacher, ICursoExecucao executionDegree)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionDegree.idInternal", executionDegree
				.getIdInternal());
		criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
		return (ICoordinator) queryObject(Coordinator.class, criteria);
	}
	
	public ICoordinator readCoordinatorByTeacherAndDegreeCurricularPlanID(
			ITeacher teacher, Integer  degreeCurricularPlanID)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionDegree.degreeCurricularPlan.idInternal", degreeCurricularPlanID);
		criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
		return (ICoordinator) queryObject(Coordinator.class, criteria);
	}
	
}