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
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
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
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideSituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGuide;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuide;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuideEntry;
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
     */

    public Integer run(Integer guideId, String remarks, List infoReimbursementGuideEntries,
            IUserView userView) throws FenixServiceException {
        try {
            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentGuide persistentGuide = ps.getIPersistentGuide();
            IPersistentReimbursementGuide persistentReimbursementGuide = ps
                    .getIPersistentReimbursementGuide();
            IPersistentObject persistentObject = ps.getIPersistentObject();

            IPersistentReimbursementGuideEntry persistentReimbursementGuideEntry = ps
                    .getIPersistentReimbursementGuideEntry();

            IGuide guide = (IGuide) persistentGuide.readByOID(Guide.class, guideId, true);

            if (!guide.getActiveSituation().getSituation().equals(GuideState.PAYED)) {
                throw new InvalidGuideSituationServiceException(
                        "error.exception.masterDegree.invalidGuideSituation");
            }

            List reimbursementGuideEntries = Cloner
                    .copyListInfoReimbursementGuideEntries2ListIReimbursementGuideEntries(infoReimbursementGuideEntries);

            Iterator it = reimbursementGuideEntries.iterator();
            IReimbursementGuideEntry reimbursementGuideEntry = null;

            while (it.hasNext()) {
                reimbursementGuideEntry = (IReimbursementGuideEntry) it.next();

                if (reimbursementGuideEntry.getJustification().length() == 0)
                    throw new RequiredJustificationServiceException(
                            "error.exception.masterDegree.requiredJustification");

                if (checkReimbursementGuideEntriesSum(reimbursementGuideEntry, ps) == false)
                    throw new InvalidReimbursementValueServiceException(
                            "error.exception.masterDegree.invalidReimbursementValue");
            }

            // reimbursement Guide
            Integer reimbursementGuideNumber = persistentReimbursementGuide
                    .generateReimbursementGuideNumber();

            IReimbursementGuide reimbursementGuide = new ReimbursementGuide();
            persistentReimbursementGuide.simpleLockWrite(reimbursementGuide);

            reimbursementGuide.setCreationDate(Calendar.getInstance());
            reimbursementGuide.setNumber(reimbursementGuideNumber);
            reimbursementGuide.setGuide(guide);
            guide.getReimbursementGuides().add(reimbursementGuide);

            // read employee
            IPersistentEmployee persistentEmployee = ps.getIPersistentEmployee();
            IPessoaPersistente persistentPerson = ps.getIPessoaPersistente();
            IPerson person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
            IEmployee employee = persistentEmployee.readByPerson(person);

            // reimbursement Guide Situation
            IReimbursementGuideSituation reimbursementGuideSituation = new ReimbursementGuideSituation();
            persistentObject.simpleLockWrite(reimbursementGuideSituation);
            reimbursementGuideSituation.setEmployee(employee);
            reimbursementGuideSituation.setModificationDate(Calendar.getInstance());
            reimbursementGuideSituation.setOfficialDate(Calendar.getInstance());
            reimbursementGuideSituation.setReimbursementGuide(reimbursementGuide);
            reimbursementGuideSituation.setReimbursementGuideState(ReimbursementGuideState.ISSUED);
            reimbursementGuideSituation.setRemarks(remarks);
            reimbursementGuideSituation.setState(new State(State.ACTIVE));

            List reimbursementGuideSituations = new ArrayList();
            reimbursementGuideSituations.add(reimbursementGuideSituation);
            reimbursementGuide.setReimbursementGuideSituations(reimbursementGuideSituations);

            // reimbursement Guide Entries
            it = reimbursementGuideEntries.iterator();
            List reimbursementGuideEntriesList = new ArrayList();
            while (it.hasNext()) {
                reimbursementGuideEntry = (IReimbursementGuideEntry) it.next();

                if (reimbursementGuideEntry.getGuideEntry().getDocumentType().equals(
                        DocumentType.INSURANCE)
                        && !reimbursementGuideEntry.getValue().equals(
                                reimbursementGuideEntry.getGuideEntry().getPrice())) {
                    throw new InvalidReimbursementValueServiceException(
                            "error.exception.masterDegree.invalidInsuranceReimbursementValue");
                }

                reimbursementGuideEntry.setReimbursementGuide(reimbursementGuide);
                reimbursementGuideEntriesList.add(reimbursementGuideEntry);

                IGuideEntry guideEntry = (IGuideEntry) persistentObject.readByOID(GuideEntry.class,
                        reimbursementGuideEntry.getGuideEntry().getIdInternal());

                reimbursementGuideEntry.setGuideEntry(guideEntry);
                guideEntry.getReimbursementGuideEntries().add(reimbursementGuideEntry);

                persistentReimbursementGuideEntry.simpleLockWrite(reimbursementGuideEntry);
            }
            reimbursementGuide.setReimbursementGuideEntries(reimbursementGuideEntriesList);
            guide.getReimbursementGuides().add(reimbursementGuide);
            return reimbursementGuide.getIdInternal();

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    /**
     * @param newReimbursementGuideEntry
     * @param suportePersistente
     * @return true if the sum of existents reeimbursement guide entries of a
     *         guide entry with the new reimbursement guide entry is less or
     *         equal than their guide entry
     */
    private boolean checkReimbursementGuideEntriesSum(
            IReimbursementGuideEntry newReimbursementGuideEntry, ISuportePersistente suportePersistente)
            throws FenixServiceException {
        IGuideEntry guideEntry = newReimbursementGuideEntry.getGuideEntry();
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