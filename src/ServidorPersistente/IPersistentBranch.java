package ServidorPersistente;

import java.util.List;

import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Util.BranchType;

/**
 * 
 * @author dcs-rjao
 * 
 * 25/Mar/2003
 */

public interface IPersistentBranch extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;

    public void delete(IBranch branch) throws ExcepcaoPersistencia;

    public List readByExecutionDegree(ICursoExecucao execucao) throws ExcepcaoPersistencia;

    public List readByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
            throws ExcepcaoPersistencia;

    public IBranch readByDegreeCurricularPlanAndBranchName(IDegreeCurricularPlan degreeCurricularPlan,
            String branchName) throws ExcepcaoPersistencia;

    public IBranch readByDegreeCurricularPlanAndCode(IDegreeCurricularPlan degreeCurricularPlan,
            String code) throws ExcepcaoPersistencia;

    public List readAllByCurricularCourse(ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia;

    public List readAllByDegreeCurricularPlanAndBranchType(IDegreeCurricularPlan degreeCurricularPlan,
            BranchType branchType) throws ExcepcaoPersistencia;
}