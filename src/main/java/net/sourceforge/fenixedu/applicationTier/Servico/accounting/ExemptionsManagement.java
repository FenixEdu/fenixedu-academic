package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AdministrativeOfficeFeeAndInsuranceExemptionBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.InsuranceExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemption;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemptionJustificationType;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeExemption;
import net.sourceforge.fenixedu.domain.accounting.events.InsuranceExemption;
import net.sourceforge.fenixedu.domain.accounting.events.InsuranceExemptionJustificationType;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class ExemptionsManagement {

    @Atomic
    public static void createAdministrativeOfficeFeeAndInsuranceExemption(final Person responsible,
            final AdministrativeOfficeFeeAndInsuranceExemptionBean exemptionBean) {
        AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent =
                exemptionBean.getAdministrativeOfficeFeeAndInsuranceEvent();
        AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType = exemptionBean.getJustificationType();
        String reason = exemptionBean.getReason();
        YearMonthDay dispatchDate = exemptionBean.getDispatchDate();

        switch (exemptionBean.getApplyExemptionOn()) {
        case ADMINISTRATIVE_OFFICE_FEE:
            new AdministrativeOfficeFeeExemption(responsible, administrativeOfficeFeeAndInsuranceEvent, justificationType,
                    reason, dispatchDate);
            return;
        case ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE:
            new AdministrativeOfficeFeeAndInsuranceExemption(responsible, administrativeOfficeFeeAndInsuranceEvent,
                    justificationType, reason, dispatchDate);
            return;
        case INSURANCE_FEE:
            InsuranceExemptionJustificationType insuranceJustificationType = null;
            switch (justificationType) {
            case DIRECTIVE_COUNCIL_AUTHORIZATION:
                insuranceJustificationType = InsuranceExemptionJustificationType.DIRECTIVE_COUNCIL_AUTHORIZATION;
                break;
            case MIT_AGREEMENT:
                insuranceJustificationType = InsuranceExemptionJustificationType.MIT_AGREEMENT;
            }

            new InsuranceExemption(responsible, administrativeOfficeFeeAndInsuranceEvent, insuranceJustificationType, reason,
                    dispatchDate);
        }

    }

    @Atomic
    public static void createInsuranceExemption(final Person responsible, final InsuranceExemptionBean exemptionBean) {
        new InsuranceExemption(responsible, exemptionBean.getInsuranceEvent(), exemptionBean.getJustificationType(),
                exemptionBean.getReason(), exemptionBean.getDispatchDate());
    }

}
