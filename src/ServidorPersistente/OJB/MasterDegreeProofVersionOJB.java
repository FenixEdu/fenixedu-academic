/*
 * Created on Oct 13, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IMasterDegreeProofVersion;
import Dominio.IMasterDegreeThesis;
import Dominio.IStudentCurricularPlan;
import Dominio.MasterDegreeProofVersion;
import Dominio.MasterDegreeThesisDataVersion;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMasterDegreeProofVersion;
import Util.State;

/**
 * @author
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */
public class MasterDegreeProofVersionOJB
	extends ObjectFenixOJB
	implements IPersistentMasterDegreeProofVersion
{

	/** Creates a new instance of MasterDegreeProofVersionOJB */
	public MasterDegreeProofVersionOJB()
	{
	}

	public IMasterDegreeProofVersion readActiveByMasterDegreeThesis(IMasterDegreeThesis masterDegreeThesis)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();

		criteria.addEqualTo("masterDegreeThesis.idInternal", masterDegreeThesis.getIdInternal());
		criteria.addEqualTo("currentState", new Integer(State.ACTIVE));
		IMasterDegreeProofVersion storedMasterDegreeProofVersion =
			(IMasterDegreeProofVersion) queryObject(MasterDegreeThesisDataVersion.class, criteria);

		return storedMasterDegreeProofVersion;
	}

	public IMasterDegreeProofVersion readActiveByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();

		criteria.addEqualTo(
			"masterDegreeThesis.studentCurricularPlan.idInternal",
			studentCurricularPlan.getIdInternal());
		criteria.addEqualTo("currentState", new Integer(State.ACTIVE));
		IMasterDegreeProofVersion storedMasterDegreeProofVersion =
			(IMasterDegreeProofVersion) queryObject(MasterDegreeProofVersion.class, criteria);

		return storedMasterDegreeProofVersion;
	}

	public List readNotActiveByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();

		criteria.addEqualTo(
			"masterDegreeThesis.studentCurricularPlan.idInternal",
			studentCurricularPlan.getIdInternal());
		criteria.addNotEqualTo("currentState", new Integer(State.ACTIVE));
		List result = queryList(MasterDegreeProofVersion.class, criteria, "lastModification",false);

		return result;
	}
}