package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithAll;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.transactions.IGratuityTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditGratuitySituationById implements IService {

    public Object run(InfoGratuitySituation infoGratuitySituation) throws FenixServiceException,
            ExcepcaoPersistencia {
        if (infoGratuitySituation == null) {
            throw new FenixServiceException();
        }

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final IGratuitySituation gratuitySituation = sp.getIPersistentGratuitySituation()
                .readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(
                        infoGratuitySituation.getInfoStudentCurricularPlan().getIdInternal(),
                        infoGratuitySituation.getInfoGratuityValues().getIdInternal());
        if (gratuitySituation == null) {
            throw new NonExistingServiceException("Gratuity Situation not exist yet.");
        }

        // set employee who made register
        final IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(
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
            final IGratuitySituation gratuitySituation) {
        double exemptionValue = gratuitySituation.getTotalValue()
                * (infoGratuitySituation.getExemptionPercentage() / 100.0);

        if (infoGratuitySituation.getExemptionValue() != null) {
            exemptionValue += infoGratuitySituation.getExemptionValue();
        }

        double newRemainingValue = gratuitySituation.getTotalValue() - exemptionValue;
        double payedValue = 0;
        double reimbursedValue = 0;
        for (IGratuityTransaction gratuityTransaction : gratuitySituation.getTransactionList()) {
            payedValue += gratuityTransaction.getValue();

            if (gratuityTransaction.getGuideEntry() != null) {

                List<IReimbursementGuideEntry> reimbursementGuideEntries = gratuityTransaction
                        .getGuideEntry().getReimbursementGuideEntries();
                if (reimbursementGuideEntries != null) {

                    for (IReimbursementGuideEntry reimbursementGuideEntry : reimbursementGuideEntries) {
                        
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
