package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;

import org.apache.ojb.broker.query.Criteria;

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
			IExecutionDegree executionDegree = coordinator.getExecutionDegree();

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
		IExecutionDegree executionDegree = null;

		while (iter.hasNext()) {
			//get coordinator from the coordinators list
			coordinator = (ICoordinator) iter.next();
			//get the execution degree for that coordinator
			executionDegree = coordinator.getExecutionDegree();
			//get the curricular plan from the execution degree
			IDegreeCurricularPlan curricularPlan = executionDegree
					.getDegreeCurricularPlan();

			if (!degreeCurricularPlans.contains(curricularPlan)) {
				degreeCurricularPlans.add(curricularPlan);
			}
		}
		return degreeCurricularPlans;
	}

	public List readCoordinatorsByExecutionDegree(IExecutionDegree executionDegree)
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
	 *      Dominio.IExecutionDegree)
	 */
	public ICoordinator readCoordinatorByTeacherAndExecutionDegree(
			ITeacher teacher, IExecutionDegree executionDegree)
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