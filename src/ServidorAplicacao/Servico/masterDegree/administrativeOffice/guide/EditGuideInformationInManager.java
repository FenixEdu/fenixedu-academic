package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Guide;
import Dominio.IExecutionDegree;
import Dominio.IGuide;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class EditGuideInformationInManager implements IService {

    public void run(Integer guideID, Integer degreeCurricularPlanID, String executionYear)
            throws ExcepcaoPersistencia {

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        IGuide guide = (IGuide) sp.getIPersistentGuide().readByOID(Guide.class, guideID, true);
        IExecutionDegree cursoExecucao = sp.getIPersistentExecutionDegree()
                .readByDegreeCurricularPlanIDAndExecutionYear(degreeCurricularPlanID, executionYear);

        guide.setExecutionDegree(cursoExecucao);

    }

}
