package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.InsuranceValue;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class EditInsuranceValueByExecutionYearID implements IService {

    public void run(Integer executionYearID, Double annualValue, Date endDate)
            throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final ExecutionYear executionYear = (ExecutionYear) sp.getIPersistentObject().readByOID(
                ExecutionYear.class, executionYearID);

        InsuranceValue insuranceValue = executionYear.getInsuranceValue();
        
        if (insuranceValue != null) {
            insuranceValue.setEndDate(endDate);
            insuranceValue.setAnnualValue(annualValue);
        } else {
            insuranceValue = DomainFactory.makeInsuranceValue(executionYear, annualValue, endDate);
        }
    }

}
