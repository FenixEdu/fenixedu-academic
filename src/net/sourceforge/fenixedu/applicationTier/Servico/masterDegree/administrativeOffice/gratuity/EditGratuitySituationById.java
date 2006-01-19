package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithAll;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class EditGratuitySituationById extends Service {

    public Object run(InfoGratuitySituation infoGratuitySituation) throws FenixServiceException,
            ExcepcaoPersistencia {
        if (infoGratuitySituation == null) {
            throw new FenixServiceException();
        }

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final GratuitySituation gratuitySituation = sp.getIPersistentGratuitySituation()
                .readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(
                        infoGratuitySituation.getInfoStudentCurricularPlan().getIdInternal(),
                        infoGratuitySituation.getInfoGratuityValues().getIdInternal());
        if (gratuitySituation == null) {
            throw new NonExistingServiceException("Gratuity Situation not exist yet.");
        }

        // set employee who made register
        final Person person = sp.getIPessoaPersistente().lerPessoaPorUsername(
                infoGratuitySituation.getInfoEmployee().getPerson().getUsername());
        if (person != null) {
            gratuitySituation.setEmployee(person.getEmployee());
        }

        // Update Remaining Value
        updateRemainingValue(infoGratuitySituation, gratuitySituation);

        gratuitySituation.setWhen(Calendar.getInstance().getTime());
        gratuitySituation.setExemptionDescription(infoGratuitySituation.getExemptionDescription());
        gratuitySituation.setExemptionPercentage(infoGratuitySituation.getExemptionPercentage());
        gratuitySituation.setExemptionValue(infoGratuitySituation.getExemptionValue());
        gratuitySituation.setExemptionType(infoGratuitySituation.getExemptionType());

        return InfoGratuitySituationWithAll.newInfoFromDomain(gratuitySituation);
    }

    private void updateRemainingValue(InfoGratuitySituation infoGratuitySituation,
            final GratuitySituation gratuitySituation) {
        double exemptionValue = gratuitySituation.getTotalValue()
                * (infoGratuitySituation.getExemptionPercentage() / 100.0);

        if (infoGratuitySituation.getExemptionValue() != null) {
            exemptionValue += infoGratuitySituation.getExemptionValue();
        }

        double newRemainingValue = gratuitySituation.getTotalValue() - exemptionValue;
        double payedValue = 0;
        double reimbursedValue = 0;
        for (GratuityTransaction gratuityTransaction : gratuitySituation.getTransactionList()) {
            payedValue += gratuityTransaction.getValue();

            if (gratuityTransaction.getGuideEntry() != null) {

                List<ReimbursementGuideEntry> reimbursementGuideEntries = gratuityTransaction
                        .getGuideEntry().getReimbursementGuideEntries();
                if (reimbursementGuideEntries != null) {

                    for (ReimbursementGuideEntry reimbursementGuideEntry : reimbursementGuideEntries) {
                        
                        if (reimbursementGuideEntry.getReimbursementGuide()
                                .getActiveReimbursementGuideSituation().getReimbursementGuideState()
                                .equals(ReimbursementGuideState.PAYED)) {
                            reimbursedValue += reimbursementGuideEntry.getValue();
                        }
                    }
                }
            }

        }
        gratuitySituation.setRemainingValue(newRemainingValue - payedValue + reimbursedValue);
    }

}
