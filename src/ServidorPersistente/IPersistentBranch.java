package ServidorPersistente;

import java.util.List;

import Dominio.IBranch;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */

public interface IPersistentBranch extends IPersistentObject {



	
    public List readAll() throws ExcepcaoPersistencia;
   
    public void delete(IBranch branch) throws ExcepcaoPersistencia;
    
	
	
	/**
	 * @param execucao
	 * @return
	 */
	public List readByExecutionDegree(ICursoExecucao execucao) throws ExcepcaoPersistencia;
	public List readByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @param degreeCurricularPlan
	 * @param branchName
	 * @return IBranch
	 * @throws ExcepcaoPersistencia
	 */
	public IBranch readByDegreeCurricularPlanAndBranchName(IDegreeCurricularPlan degreeCurricularPlan, String branchName) throws ExcepcaoPersistencia;
	public IBranch readByDegreeCurricularPlanAndCode(IDegreeCurricularPlan degreeCurricularPlan, String code) throws ExcepcaoPersistencia;
}
