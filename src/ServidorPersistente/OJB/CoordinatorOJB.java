package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Coordinator;
import Dominio.ICoordinator;
import Dominio.ICurso;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCoordinator;

/**
 *fenix-head
 *ServidorPersistente.OJB
 * @author João Mota
 *28/Out/2003
 *
 */

public class CoordinatorOJB
	extends ObjectFenixOJB
	implements IPersistentCoordinator {

	public List readDegreesByCoordinator(ITeacher teacher)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
		List coordinators = queryList(Coordinator.class, criteria);
		if (coordinators == null) {
			return null;
		}
		Iterator iter = coordinators.iterator();
		List degrees = new ArrayList();
		while (iter.hasNext()) {
			ICoordinator coordinator = (ICoordinator) iter.next();
			ICurso degree =
				coordinator
					.getExecutionDegree()
					.getCurricularPlan()
					.getDegree();
			if (!degrees.contains(degree)) {
				degrees.add(degree);
			}
		}
		return degrees;
	}

}
