/*
 * Created on Oct 13, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IMasterDegreeThesis;
import Dominio.IMasterDegreeThesisDataVersion;
import Dominio.IStudentCurricularPlan;
import Dominio.MasterDegreeThesisDataVersion;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMasterDegreeThesisDataVersion;
import Util.State;

/**
 * @author
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */
public class MasterDegreeThesisDataVersionOJB extends ObjectFenixOJB implements IPersistentMasterDegreeThesisDataVersion {

	/** Creates a new instance of MasterDegreeProofVersionOJB */
	public MasterDegreeThesisDataVersionOJB() {
	}

	public IMasterDegreeThesisDataVersion readActiveByMasterDegreeThesis(IMasterDegreeThesis masterDegreeThesis) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();

		criteria.addEqualTo("masterDegreeThesis.idInternal", masterDegreeThesis.getIdInternal());
		criteria.addEqualTo("currentState", new Integer(State.ACTIVE));
		IMasterDegreeThesisDataVersion storedMasterDegreeThesisDataVersion =
			(IMasterDegreeThesisDataVersion) queryObject(MasterDegreeThesisDataVersion.class, criteria);

		return storedMasterDegreeThesisDataVersion;
	}

	public IMasterDegreeThesisDataVersion readActiveByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();

		criteria.addEqualTo("masterDegreeThesis.studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
		criteria.addEqualTo("currentState", new Integer(State.ACTIVE));
		IMasterDegreeThesisDataVersion storedMasterDegreeThesisDataVersion =
			(IMasterDegreeThesisDataVersion) queryObject(MasterDegreeThesisDataVersion.class, criteria);

		return storedMasterDegreeThesisDataVersion;
	}
}
