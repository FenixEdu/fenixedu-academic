package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoInsuranceValue;
import Dominio.ExecutionYear;
import Dominio.IExecutionYear;
import Dominio.IInsuranceValue;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ReadInsuranceValueByExecutionYearID implements IService {

    /**
     * Constructor
     */
    public ReadInsuranceValueByExecutionYearID() {
    }

    public InfoInsuranceValue run(Integer executionYearID) throws FenixServiceException {

        InfoInsuranceValue infoInsuranceValue = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionYear executionYear = (IExecutionYear) sp.getIPersistentExecutionYear().readByOID(
                    ExecutionYear.class, executionYearID);

            IInsuranceValue insuranceValue = sp.getIPersistentInsuranceValue().readByExecutionYear(
                    executionYear);

            if (insuranceValue != null) {
                infoInsuranceValue = InfoInsuranceValue.newInfoFromDomain(insuranceValue);
            }

            return infoInsuranceValue;

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }
}