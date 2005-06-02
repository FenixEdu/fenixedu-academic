/*
 * Created on 14/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuityValues;
import net.sourceforge.fenixedu.dataTransferObject.InfoPaymentPhase;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IGratuityValues;
import net.sourceforge.fenixedu.domain.IPaymentPhase;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.PaymentPhase;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuityValues;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPaymentPhase;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 * 
 */
public class InsertGratuityData implements IService {

    public Object run(InfoGratuityValues infoGratuityValues) throws FenixServiceException {
        if (infoGratuityValues == null) {
            throw new FenixServiceException("impossible.insertGratuityValues");
        }
        if (infoGratuityValues.getInfoExecutionDegree() == null
                || infoGratuityValues.getInfoExecutionDegree().getIdInternal() == null
                || infoGratuityValues.getInfoExecutionDegree().getIdInternal().intValue() <= 0) {
            throw new FenixServiceException("impossible.insertGratuityValues");
        }

        if (infoGratuityValues.getInfoEmployee() == null
                || infoGratuityValues.getInfoEmployee().getPerson() == null
                || infoGratuityValues.getInfoEmployee().getPerson().getUsername() == null
                || infoGratuityValues.getInfoEmployee().getPerson().getUsername().length() <= 0) {
            throw new FenixServiceException("impossible.insertGratuityValues");
        }

        ISuportePersistente sp = null;

        validateGratuity(sp, infoGratuityValues);

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentGratuityValues persistentGratuityValues = sp.getIPersistentGratuityValues();

            IGratuityValues gratuityValues = persistentGratuityValues
                    .readGratuityValuesByExecutionDegree(infoGratuityValues.getInfoExecutionDegree()
                            .getIdInternal());
            boolean isNew = false;
            if (gratuityValues == null) // it doesn't exist in database, then
            // write it
            {
                gratuityValues = new GratuityValues();
                persistentGratuityValues.simpleLockWrite(gratuityValues);
                isNew = true;

                // execution Degree
                IPersistentExecutionDegree persistentExecutionDegree = sp
                        .getIPersistentExecutionDegree();

                IExecutionDegree executionDegree = (IExecutionDegree) persistentExecutionDegree
                        .readByOID(ExecutionDegree.class, infoGratuityValues.getInfoExecutionDegree()
                                .getIdInternal());
                gratuityValues.setExecutionDegree(executionDegree);

            }
            if (!isNew) {
                persistentGratuityValues.simpleLockWrite(gratuityValues);
            }
            validatePaymentPhasesWithTransaction(sp, gratuityValues);
            // validateGratuitySituationWithTransaction(sp, gratuityValues);

            registerWhoAndWhen(sp, infoGratuityValues, gratuityValues);

            gratuityValues.setAnualValue(infoGratuityValues.getAnualValue());
            gratuityValues.setScholarShipValue(infoGratuityValues.getScholarShipValue());
            gratuityValues.setFinalProofValue(infoGratuityValues.getFinalProofValue());
            gratuityValues.setCreditValue(infoGratuityValues.getCreditValue());
            gratuityValues.setCourseValue(infoGratuityValues.getCourseValue());
            gratuityValues.setProofRequestPayment(infoGratuityValues.getProofRequestPayment());
            gratuityValues.setStartPayment(infoGratuityValues.getStartPayment());
            gratuityValues.setEndPayment(infoGratuityValues.getEndPayment());

            // write all payment phases
            writePaymentPhases(sp, infoGratuityValues, gratuityValues);

            // update gratuity values in all student curricular plan that belong
            // to this execution
            // degree
            // updateStudentsGratuitySituation(sp, gratuityValues,
            // gratuityValue);
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("impossible.insertGratuityValues");
        } catch (FenixServiceException fenixServiceException) {
            throw fenixServiceException;
        }

        return Boolean.TRUE;
    }

    private Double validateGratuity(ISuportePersistente sp, InfoGratuityValues infoGratuityValues)
            throws FenixServiceException {
        // find the gratuity's value
        Double gratuityValue = null;

        if (infoGratuityValues.getAnualValue() != null) {
            gratuityValue = infoGratuityValues.getAnualValue();
        } else {
            if (infoGratuityValues.getProofRequestPayment() != null
                    && infoGratuityValues.getProofRequestPayment().equals(Boolean.TRUE)) {
                gratuityValue = infoGratuityValues.getScholarShipValue();
            } else {
                gratuityValue = new Double(infoGratuityValues.getScholarShipValue().doubleValue()
                        + infoGratuityValues.getFinalProofValue().doubleValue());
            }
        }

        List paymentPhasesList = infoGratuityValues.getInfoPaymentPhases();
        if (paymentPhasesList != null && paymentPhasesList.size() > 0) {
            // verify if total of all payment phases isn't greater then anual
            // value
            ListIterator iterator = paymentPhasesList.listIterator();
            double totalValuePaymentPhases = 0;
            while (iterator.hasNext()) {
                InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();
                totalValuePaymentPhases += infoPaymentPhase.getValue().floatValue();
            }
            if (totalValuePaymentPhases > gratuityValue.doubleValue()) {
                throw new FenixServiceException("error.masterDegree.gatuyiuty.totalValuePaymentPhases");
            }
            // verify if all payment phases's dates are correct
            validateDatesOfPaymentPhases(paymentPhasesList);

            // registration Payment
            if (infoGratuityValues.getRegistrationPayment() != null
                    && infoGratuityValues.getRegistrationPayment().equals(Boolean.TRUE)) {
                iterator = paymentPhasesList.listIterator();
                InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();
                infoPaymentPhase.setDescription(SessionConstants.REGISTRATION_PAYMENT_KEY);
            }
        }
        return gratuityValue;
    }

    private void validateDatesOfPaymentPhases(List paymentPhasesList) throws FenixServiceException {
        Collections.sort(paymentPhasesList, new BeanComparator("endDate"));

        ListIterator iterator = paymentPhasesList.listIterator();
        while (iterator.hasNext()) {
            InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();

            if (iterator.hasNext()) {
                InfoPaymentPhase infoPaymentPhase2Compare = (InfoPaymentPhase) iterator.next();
                if ((infoPaymentPhase2Compare.getStartDate() != null && !infoPaymentPhase.getEndDate()
                        .before(infoPaymentPhase2Compare.getStartDate()))
                        || (infoPaymentPhase2Compare.getStartDate() == null && !infoPaymentPhase
                                .getEndDate().before(infoPaymentPhase2Compare.getEndDate()))) {
                    throw new FenixServiceException("error.impossible.paymentPhaseWithWrongDates");
                }
                iterator.previous();
            }
        }
    }

    private void validatePaymentPhasesWithTransaction(ISuportePersistente sp,
            IGratuityValues gratuityValues) throws FenixServiceException {
        // verify if any payment phase has any transaction associated
        if (gratuityValues.getPaymentPhaseList() != null
                && gratuityValues.getPaymentPhaseList().size() > 0) {
            ListIterator iterator = gratuityValues.getPaymentPhaseList().listIterator();
            while (iterator.hasNext()) {
                IPaymentPhase paymentPhase = (IPaymentPhase) iterator.next();

                if (paymentPhase.getTransactionList() != null
                        && paymentPhase.getTransactionList().size() > 0) {
                    throw new FenixServiceException("error.impossible.paymentPhaseWithTransactional");
                }
            }
        }
    }

    // private void validateGratuitySituationWithTransaction(
    // ISuportePersistente sp,
    // IGratuityValues gratuityValues)
    // throws FenixServiceException
    // {
    // //verify if gratuity has any transaction associated
    // IGratuityTransaction persistentGratuityTransaction =
    // sp.getIPersistentGratuityTransaction();
    //
    // List gratuityTransactions =
    // persistentGratuityTransaction.readByGratuityValues(gratuityValues);
    //
    // if(gratuityTransactions != null && gratuityTransactions.size() > 0){
    // throw new
    // FenixServiceException("error.impossible.gratuityWithTransactions");
    // }
    //
    // }

    private void registerWhoAndWhen(ISuportePersistente sp, InfoGratuityValues infoGratuityValues,
            IGratuityValues gratuityValues) throws ExcepcaoPersistencia {
        // employee who made register
        IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
        IPerson person = persistentPerson.lerPessoaPorUsername(infoGratuityValues.getInfoEmployee()
                .getPerson().getUsername());
        if (person != null) {
            IPersistentEmployee persistentEmployee = sp.getIPersistentEmployee();
            IEmployee employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());
            gratuityValues.setEmployee(employee);
        }

        Calendar now = Calendar.getInstance();
        gratuityValues.setWhen(now.getTime());
    }

    private void writePaymentPhases(ISuportePersistente sp, InfoGratuityValues infoGratuityValues,
            IGratuityValues gratuityValues) throws FenixServiceException {
        IPersistentPaymentPhase persistentPaymentPhase = sp.getIPersistentPaymentPhase();

        try {
            if (gratuityValues.getPaymentPhaseList() != null
                    && gratuityValues.getPaymentPhaseList().size() > 0) {
                for (IPaymentPhase paymentPhase : (List<IPaymentPhase>) gratuityValues
                        .getPaymentPhaseList()) {
                    persistentPaymentPhase.deleteByOID(PaymentPhase.class, paymentPhase.getIdInternal());
                }
                gratuityValues.getPaymentPhaseList().clear();
            }

            if (infoGratuityValues.getInfoPaymentPhases() != null
                    && infoGratuityValues.getInfoPaymentPhases().size() > 0) {
                ListIterator iterator = infoGratuityValues.getInfoPaymentPhases().listIterator();
                while (iterator.hasNext()) {
                    InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) iterator.next();

                    IPaymentPhase paymentPhase = new PaymentPhase();
                    paymentPhase.setStartDate(infoPaymentPhase.getStartDate());
                    paymentPhase.setEndDate(infoPaymentPhase.getEndDate());
                    paymentPhase.setValue(infoPaymentPhase.getValue());
                    if (infoPaymentPhase.getDescription() == null) {
                        infoPaymentPhase.setDescription(String.valueOf(iterator.previousIndex()));
                    }
                    paymentPhase.setDescription(infoPaymentPhase.getDescription());

                    paymentPhase.setGratuityValues(gratuityValues);

                    persistentPaymentPhase.simpleLockWrite(paymentPhase);
                }
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("impossible.insertGratuityValues");
        }
    }
}
