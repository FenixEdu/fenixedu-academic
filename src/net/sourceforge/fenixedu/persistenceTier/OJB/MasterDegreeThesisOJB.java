/*
 * Created on Oct 10, 2003
 *
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.IMasterDegreeThesis;
import net.sourceforge.fenixedu.domain.MasterDegreeThesis;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeThesis;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class MasterDegreeThesisOJB extends PersistentObjectOJB implements
		IPersistentMasterDegreeThesis {

	/** Creates a new instance of MasterDegreeCandidateOJB */
	public MasterDegreeThesisOJB() {
	}

	public IMasterDegreeThesis readByStudentCurricularPlan(
			Integer studentCurricularPlanId) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal",
				studentCurricularPlanId);
		IMasterDegreeThesis storedMasterDegreeThesis = (IMasterDegreeThesis) queryObject(
				MasterDegreeThesis.class, criteria);

		return storedMasterDegreeThesis;
	}

}