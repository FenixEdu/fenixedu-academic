package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Date;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IInsuranceValue;
import net.sourceforge.fenixedu.domain.InsuranceValue;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentInsuranceValue;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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