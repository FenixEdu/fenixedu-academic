package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadPublicExecutionDegreeByDCPID implements IService {

    /**
     * @param name
     * @param infoExecutionYear
     * @return InfoExecutionDegree
     * @throws FenixServiceException
     *             This method assumes thar there's only one Execution Degree
     *             for each Degree Curricular Plan. This is the case with the
     *             Master Degrees
     */

    private static ReadPublicExecutionDegreeByDCPID service = new ReadPublicExecutionDegreeByDCPID();

    public ReadPublicExecutionDegreeByDCPID() {
    }

    public String getNome() {
        return "ReadPublicExecutionDegreeByDCPID";
    }

    public static ReadPublicExecutionDegreeByDCPID getService() {
        return service;
    }

    public List run(Integer degreeCurricularPlanID) throws FenixServiceException {

        List executionDegrees = null;
        List result = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            executionDegrees = sp.getIPersistentExecutionDegree()
                    .readExecutionDegreesbyDegreeCurricularPlanID(degreeCurricularPlanID);

            result = (List) CollectionUtils.collect(executionDegrees, new Transformer() {

                public Object transform(Object input) {
                    ICursoExecucao executionDegree = (ICursoExecucao) input;
                    InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) Cloner
                            .get(executionDegree);
                    return infoExecutionDegree;
                }
            });

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        if (executionDegrees == null) {
            throw new NonExistingServiceException();
        }
        return result;
    }

    public InfoExecutionDegree run(Integer degreeCurricularPlanID, Integer executionYearID)
            throws FenixServiceException {

        ICursoExecucao executionDegrees = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            executionDegrees = sp.getIPersistentExecutionDegree()
                    .readExecutionDegreesbyDegreeCurricularPlanIDAndExecutionYearID(
                            degreeCurricularPlanID, executionYearID);

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        if (executionDegrees == null) {
            return null;
            //throw new NonExistingServiceException();
        }
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) Cloner.get(executionDegrees);
        return infoExecutionDegree;
    }

}