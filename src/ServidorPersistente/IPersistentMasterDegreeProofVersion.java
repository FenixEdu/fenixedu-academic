/*
 * Created on Oct 14, 2003
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IMasterDegreeProofVersion;
import Dominio.IMasterDegreeThesis;
import Dominio.IStudentCurricularPlan;

/**
 * @author :
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */
public interface IPersistentMasterDegreeProofVersion extends IPersistentObject
{
	public IMasterDegreeProofVersion readActiveByMasterDegreeThesis(IMasterDegreeThesis masterDegreeThesis)
		throws ExcepcaoPersistencia;
	public IMasterDegreeProofVersion readActiveByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
		throws ExcepcaoPersistencia;
	public List readNotActiveByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
		throws ExcepcaoPersistencia;
}