/*
 * Created on Oct 14, 2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IMasterDegreeThesisDataVersion;

/**
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public interface IPersistentMasterDegreeThesisDataVersion extends
		IPersistentObject {
	/* to delete - not used anywhere */
	public IMasterDegreeThesisDataVersion readActiveByMasterDegreeThesis(
			Integer masterDegreeThesisID) throws ExcepcaoPersistencia;

	public IMasterDegreeThesisDataVersion readActiveByStudentCurricularPlan(
			Integer studentCurricularPlanId) throws ExcepcaoPersistencia;

	public IMasterDegreeThesisDataVersion readActiveByDissertationTitle(
			String dissertationTitle) throws ExcepcaoPersistencia;

	public List readNotActivesVersionsByStudentCurricularPlan(
			Integer studentCurricularPlanId) throws ExcepcaoPersistencia;

	public List readActiveByDegreeCurricularPlan(Integer degreeCurricularPlanID)
			throws ExcepcaoPersistencia;

}