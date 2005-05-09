/*
 * Created on Oct 13, 2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.IMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.util.State;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class MasterDegreeThesisDataVersionOJB extends PersistentObjectOJB
		implements IPersistentMasterDegreeThesisDataVersion {

	/** Creates a new instance of MasterDegreeProofVersionOJB */
	public MasterDegreeThesisDataVersionOJB() {
	}

	/* to delete - not used anywhere */
	public IMasterDegreeThesisDataVersion readActiveByMasterDegreeThesis(
			Integer masterDegreeThesisId) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();

		criteria.addEqualTo("masterDegreeThesis.idInternal",
				masterDegreeThesisId);
		criteria.addEqualTo("currentState", new Integer(State.ACTIVE));
		IMasterDegreeThesisDataVersion storedMasterDegreeThesisDataVersion = (IMasterDegreeThesisDataVersion) queryObject(
				MasterDegreeThesisDataVersion.class, criteria);

		return storedMasterDegreeThesisDataVersion;
	}

	public IMasterDegreeThesisDataVersion readActiveByStudentCurricularPlan(
			Integer studentCurricularPlanId) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();

		criteria.addEqualTo(
				"masterDegreeThesis.studentCurricularPlan.idInternal",
				studentCurricularPlanId);
		criteria.addEqualTo("currentState", new Integer(State.ACTIVE));
		IMasterDegreeThesisDataVersion storedMasterDegreeThesisDataVersion = (IMasterDegreeThesisDataVersion) queryObject(
				MasterDegreeThesisDataVersion.class, criteria);

		return storedMasterDegreeThesisDataVersion;
	}

	public IMasterDegreeThesisDataVersion readActiveByDissertationTitle(
			String dissertationTitle) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();

		criteria.addEqualTo("dissertationTitle", dissertationTitle);
		criteria.addEqualTo("currentState", new Integer(State.ACTIVE));
		IMasterDegreeThesisDataVersion storedMasterDegreeThesisDataVersion = (IMasterDegreeThesisDataVersion) queryObject(
				MasterDegreeThesisDataVersion.class, criteria);

		return storedMasterDegreeThesisDataVersion;
	}

	public List readNotActivesVersionsByStudentCurricularPlan(
			Integer studentCurricularPlanId) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();

		criteria.addEqualTo(
				"masterDegreeThesis.studentCurricularPlan.idInternal",
				studentCurricularPlanId);
		criteria.addNotEqualTo("currentState", new Integer(State.ACTIVE));
		List result = queryList(MasterDegreeThesisDataVersion.class, criteria,
				"lastModification", false);

		return result;
	}

	public List readActiveByDegreeCurricularPlan(Integer degreeCurricularPlanID)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();

		criteria
				.addEqualTo(
						"masterDegreeThesis.studentCurricularPlan.degreeCurricularPlan.idInternal",
						degreeCurricularPlanID);
		criteria.addEqualTo("currentState", new Integer(State.ACTIVE));

		return queryList(MasterDegreeThesisDataVersion.class, criteria);
	}

}