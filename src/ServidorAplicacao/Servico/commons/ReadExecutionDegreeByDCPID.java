package ServidorAplicacao.Servico.commons;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.IExecutionDegree;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadExecutionDegreeByDCPID implements IService {

    /**
     * @param name
     * @param infoExecutionYear
     * @return InfoExecutionDegree
     * @throws FenixServiceException
     *             This method assumes thar there's only one Execution Degree
     *             for each Degree Curricular Plan. This is the case with the
     *             Master Degrees
     */
    public InfoExecutionDegree run(Integer degreeCurricularPlanID) throws FenixServiceException {

        IExecutionDegree executionDegree = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            executionDegree = sp.getIPersistentExecutionDegree().readbyDegreeCurricularPlanID(
                    degreeCurricularPlanID);

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        if (executionDegree == null) {
            throw new NonExistingServiceException();
        }

        return (InfoExecutionDegree) Cloner.get(executionDegree);
    }

}