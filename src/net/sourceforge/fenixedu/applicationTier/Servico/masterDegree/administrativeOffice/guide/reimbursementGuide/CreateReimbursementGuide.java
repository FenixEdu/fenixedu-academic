/*
 * Created on 21/Mar/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.guide.InvalidGuideSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.guide.InvalidReimbursementValueServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.guide.RequiredJustificationServiceException;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuide;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideSituation;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideSituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a> <br>
 *         <strong>Description: </strong> <br>
 *         This service creates a reimbursement guide and associates it with a
 *         payment guide. It also creates the reimbursement guide situation and
 *         sets it to the ISSUED state. If any problem occurs during the
 *         execution of the service a FenixServiceException is thrown. If the
 *         payment guide doesn't have a PAYED state active an
 *         InvalidGuideSituationServiceException is thrown. If the value of the
 *         reimbursement exceeds the total of the payment guide an
 *         InvalidReimbursementValueServiceException is thrown and if the sum of
 *         all the reimbursements associated with the payment guide exceeds the
 *         total an InvalidReimbursementValueSumServiceException is thrown. <br>
 *         The service also generates the number of the new reimbursement guide
 *         using a sequential method.
 */
public class CreateReimbursementGuide implements IService {

    /**
     * @throws FenixServiceException,
     *             InvalidReimbursementValueServiceException,
     *             InvalidGuideSituationServiceException,
     *             InvalidReimbursementValueSumServiceException
     * @throws ExcepcaoPersistencia
     */

    public Integer run(Integer guideId, String remarks, List infoReimbursementGuideEntries,
            IUserView userView) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IGuide guide = (IGuide) ps.getIPersistentGuide().readByOID(Guide.class, guideId, true);

        if (!guide.getActiveSituation().getSituation().equals(GuideState.PAYED)) {
            throw new InvalidGuideSituationServiceException(
                    "error.exception.masterDegree.invalidGuideSituation");
        }

        IReimbursementGuide reimbursementGuide = new ReimbursementGuide();

        // List reimbursementGuideEntries = Cloner
        // .copyListInfoReimbursementGuideEntries2ListIReimbursementGuideEntries(infoReimbursementGuideEntries);
        List<IReimbursementGuideEntry> reimbursementGuideEntries = new ArrayList<IReimbursementGuideEntry>(
                infoReimbursementGuideEntries.size());
        for (InfoReimbursementGuideEntry infoReimbursementGuideEntry : (List<InfoReimbursementGuideEntry>) infoReimbursementGuideEntries) {

            // check pre-conditions
            if (infoReimbursementGuideEntry.getJustification().length() == 0)
                throw new RequiredJustificationServiceException(
                        "error.exception.masterDegree.requiredJustification");

            IGuideEntry guideEntry = (IGuideEntry) ps.getIPersistentGuideEntry().readByOID(
                    GuideEntry.class, infoReimbursementGuideEntry.getInfoGuideEntry().getIdInternal());
            if (checkReimbursementGuideEntriesSum(infoReimbursementGuideEntry, guideEntry) == false)
                throw new InvalidReimbursementValueServiceException(
                        "error.exception.masterDegree.invalidReimbursementValue");

            if (guideEntry.getDocumentType().equals(DocumentType.INSURANCE)
                    && !infoReimbursementGuideEntry.getValue().equals(guideEntry.getPrice())) {
                throw new InvalidReimbursementValueServiceException(
                        "error.exception.masterDegree.invalidInsuranceReimbursementValue");
            }

            // create new reimbursement entry
            IReimbursementGuideEntry newReimbursementGuideEntry = new ReimbursementGuideEntry();
            ps.getIPersistentReimbursementGuideEntry().simpleLockWrite(newReimbursementGuideEntry);
            newReimbursementGuideEntry.setGuideEntry(guideEntry);
            newReimbursementGuideEntry.setJustification(infoReimbursementGuideEntry.getJustification());
            newReimbursementGuideEntry.setReimbursementGuide(reimbursementGuide);
            newReimbursementGuideEntry.setValue(infoReimbursementGuideEntry.getValue());

        }

        // reimbursement Guide
        ps.getIPersistentReimbursementGuide().simpleLockWrite(reimbursementGuide);
        reimbursementGuide.setCreationDate(Calendar.getInstance());
        Integer reimbursementGuideNumber = ps.getIPersistentReimbursementGuide()
                .generateReimbursementGuideNumber();
        reimbursementGuide.setNumber(reimbursementGuideNumber);
        reimbursementGuide.setGuide(guide);

        // read employee
        IPerson person = ps.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
        IEmployee employee = ps.getIPersistentEmployee().readByPerson(person);

        // reimbursement Guide Situation
        IReimbursementGuideSituation reimbursementGuideSituation = new ReimbursementGuideSituation();
        ps.getIPersistentObject().simpleLockWrite(reimbursementGuideSituation);
        reimbursementGuideSituation.setEmployee(employee);
        reimbursementGuideSituation.setModificationDate(Calendar.getInstance());
        reimbursementGuideSituation.setOfficialDate(Calendar.getInstance());
        reimbursementGuideSituation.setReimbursementGuide(reimbursementGuide);
        reimbursementGuideSituation.setReimbursementGuideState(ReimbursementGuideState.ISSUED);
        reimbursementGuideSituation.setRemarks(remarks);
        reimbursementGuideSituation.setState(new State(State.ACTIVE));

        return reimbursementGuide.getIdInternal();

    }

    /**
     * @param newReimbursementGuideEntry
     * @param suportePersistente
     * @return true if the sum of existents reeimbursement guide entries of a
     *         guide entry with the new reimbursement guide entry is less or
     *         equal than their guide entry
     */
    private boolean checkReimbursementGuideEntriesSum(
            InfoReimbursementGuideEntry newReimbursementGuideEntry, IGuideEntry guideEntry) {

        Double guideEntryValue = new Double(guideEntry.getPrice().doubleValue()
                * guideEntry.getQuantity().intValue());
        Double sum = new Double(newReimbursementGuideEntry.getValue().doubleValue());

        List reimbursementGuideEntries = guideEntry.getReimbursementGuideEntries();

        if (reimbursementGuideEntries == null) {
            return isGreaterThan(guideEntryValue, sum);
        }

        Iterator it = reimbursementGuideEntries.iterator();
        while (it.hasNext()) {
            IReimbursementGuideEntry reimbursementGuideEntry = (IReimbursementGuideEntry) it.next();
            if (reimbursementGuideEntry.getReimbursementGuide().getActiveReimbursementGuideSituation()
                    .getReimbursementGuideState().equals(ReimbursementGuideState.PAYED)) {
                sum = new Double(sum.doubleValue() + reimbursementGuideEntry.getValue().doubleValue());
            }
        }

        return isGreaterThan(guideEntryValue, sum);

    }

    private boolean isGreaterThan(Double guideEntryValue, Double sum) {
        if (sum.doubleValue() > guideEntryValue.doubleValue()) {
            return false;
        }
        return true;
    }

}