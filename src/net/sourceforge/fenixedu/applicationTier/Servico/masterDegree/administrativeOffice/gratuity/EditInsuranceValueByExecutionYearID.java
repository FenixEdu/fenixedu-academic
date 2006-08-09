package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.InsuranceValue;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditInsuranceValueByExecutionYearID extends Service {

    public void run(Integer executionYearID, Double annualValue, Date endDate)
            throws ExcepcaoPersistencia {

        final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);

        InsuranceValue insuranceValue = executionYear.getInsuranceValue();
        
        if (insuranceValue != null) {
            insuranceValue.setEndDate(endDate);
            insuranceValue.setAnnualValue(annualValue);
        } else {
            insuranceValue = new InsuranceValue(executionYear, annualValue, endDate);
        }
    }

}
