package ServidorAplicacao.strategy.degreeCurricularPlan;

import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.DegreeCurricularPlanStrategy;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.MasterDegreeCurricularPlanStrategy;
import Util.TipoCurso;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class DegreeCurricularPlanStrategyFactory implements IDegreeCurricularPlanStrategyFactory {

    private static DegreeCurricularPlanStrategyFactory instance = null;

    private DegreeCurricularPlanStrategyFactory() {
    }

    public static synchronized DegreeCurricularPlanStrategyFactory getInstance() {
        if (instance == null) {
            instance = new DegreeCurricularPlanStrategyFactory();
        }
        return instance;
    }

    public static synchronized void resetInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public IDegreeCurricularPlanStrategy getDegreeCurricularPlanStrategy(
            IDegreeCurricularPlan degreeCurricularPlan) {

        IDegreeCurricularPlanStrategy strategyInstance = null;

        if (degreeCurricularPlan == null)
            throw new IllegalArgumentException("Must initialize Degree Curricular Plan!");

        if (degreeCurricularPlan.getDegree().getTipoCurso().equals(TipoCurso.LICENCIATURA_OBJ)) {
            strategyInstance = new DegreeCurricularPlanStrategy(degreeCurricularPlan);
        } else if (degreeCurricularPlan.getDegree().getTipoCurso().equals(TipoCurso.MESTRADO_OBJ)) {
            strategyInstance = new MasterDegreeCurricularPlanStrategy(degreeCurricularPlan);
        }
        return strategyInstance;
    }

}