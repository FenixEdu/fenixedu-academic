package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Date;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ExecutionYear;
import Dominio.IExecutionYear;
import Dominio.IInsuranceValue;
import Dominio.InsuranceValue;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentInsuranceValue;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class EditInsuranceValueByExecutionYearID implements IService {

    /**
     * Constructor
     */
    public EditInsuranceValueByExecutionYearID() {
    }

    public void run(Integer executionYearID, Double annualValue, Date endDate)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionYear executionYear = (IExecutionYear) sp.getIPersistentExecutionYear().readByOID(
                    ExecutionYear.class, executionYearID);

            IPersistentInsuranceValue insuranceValueDAO = sp.getIPersistentInsuranceValue();

            IInsuranceValue insuranceValue = insuranceValueDAO.readByExecutionYear(executionYear);

            if (insuranceValue != null) {
                insuranceValueDAO.simpleLockWrite(insuranceValue);
                insuranceValue.setEndDate(endDate);
                insuranceValue.setAnnualValue(annualValue);
            } else {
                insuranceValue = new InsuranceValue(executionYear, annualValue, endDate);
                insuranceValueDAO.simpleLockWrite(insuranceValue);
            }

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }
}