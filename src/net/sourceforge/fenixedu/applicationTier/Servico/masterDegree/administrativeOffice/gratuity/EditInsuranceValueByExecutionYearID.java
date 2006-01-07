package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.InsuranceValue;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentInsuranceValue;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class EditInsuranceValueByExecutionYearID implements IService {

    public void run(Integer executionYearID, Double annualValue, Date endDate)
            throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final ExecutionYear executionYear = (ExecutionYear) sp.getIPersistentExecutionYear().readByOID(
                ExecutionYear.class, executionYearID);

        final IPersistentInsuranceValue insuranceValueDAO = sp.getIPersistentInsuranceValue();
        InsuranceValue insuranceValue = insuranceValueDAO.readByExecutionYear(executionYear
                .getIdInternal());

        if (insuranceValue != null) {
            insuranceValueDAO.simpleLockWrite(insuranceValue);
            insuranceValue.setEndDate(endDate);
            insuranceValue.setAnnualValue(annualValue);
        } else {
            insuranceValue = DomainFactory.makeInsuranceValue(executionYear, annualValue, endDate);
            insuranceValueDAO.simpleLockWrite(insuranceValue);
        }
    }

}
