/*
 * Created on 10/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGratuitySituation;
import DataBeans.util.Cloner;
import Dominio.GratuityValues;
import Dominio.IEmployee;
import Dominio.IGratuitySituation;
import Dominio.IGratuityValues;
import Dominio.IPessoa;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import Dominio.reimbursementGuide.IReimbursementGuideEntry;
import Dominio.transactions.GratuityTransaction;
import Dominio.transactions.IGratuityTransaction;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.ReimbursementGuideState;

/**
 * @author Tânia Pousão
 *  
 */
public class EditGratuitySituationById implements IService {

    public Object run(InfoGratuitySituation infoGratuitySituation) throws FenixServiceException {
        ISuportePersistente sp = null;
        try {
            if (infoGratuitySituation == null) {
                throw new FenixServiceException();
            }

            sp = SuportePersistenteOJB.getInstance();
            IPersistentGratuitySituation persistentGratuitySituation = sp
                    .getIPersistentGratuitySituation();

            IGratuityValues gratuityValues = new GratuityValues();
            gratuityValues.setIdInternal(infoGratuitySituation.getInfoGratuityValues().getIdInternal());

            IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
            studentCurricularPlan.setIdInternal(infoGratuitySituation.getInfoStudentCurricularPlan()
                    .getIdInternal());

            IGratuitySituation gratuitySituation = persistentGratuitySituation
                    .readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(
                            studentCurricularPlan, gratuityValues);
            if (gratuitySituation == null) {
                throw new NonExistingServiceException("Gratuity Situation not exist yet.");
            }

            //employee who made register
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPessoa person = persistentPerson.lerPessoaPorUsername(infoGratuitySituation
                    .getInfoEmployee().getPerson().getUsername());
            if (person != null) {
                IPersistentEmployee persistentEmployee = sp.getIPersistentEmployee();
                IEmployee employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());
                gratuitySituation.setEmployee(employee);
            }

            persistentGratuitySituation.simpleLockWrite(gratuitySituation);

            Calendar now = Calendar.getInstance();
            gratuitySituation.setWhen(now.getTime());

            //Update remaining value
            double exemptionValue = gratuitySituation.getTotalValue().doubleValue()
                    * (infoGratuitySituation.getExemptionPercentage().doubleValue() / 100.0);

            double newRemainingValue = gratuitySituation.getTotalValue().doubleValue() - exemptionValue;

            List transactionList = gratuitySituation.getTransactionList();
            List reimbursementGuideEntries = null;
            Iterator it = transactionList.iterator();
            IGratuityTransaction gratuityTransaction = null;
            double payedValue = 0;
            double reimbursedValue = 0;

            while (it.hasNext()) {
                gratuityTransaction = (GratuityTransaction) it.next();
                payedValue += gratuityTransaction.getValue().doubleValue();

                if (gratuityTransaction.getGuideEntry() != null) {

                    reimbursementGuideEntries = gratuityTransaction.getGuideEntry()
                            .getReimbursementGuideEntries();

                    if (reimbursementGuideEntries != null) {

                        Iterator reimbursementIterator = reimbursementGuideEntries.iterator();
                        IReimbursementGuideEntry reimbursementGuideEntry = null;

                        while (reimbursementIterator.hasNext()) {
                            reimbursementGuideEntry = (IReimbursementGuideEntry) reimbursementIterator
                                    .next();
                            if (reimbursementGuideEntry.getReimbursementGuide()
                                    .getActiveReimbursementGuideSituation().getReimbursementGuideState()
                                    .equals(ReimbursementGuideState.PAYED)) {

                                reimbursedValue += reimbursementGuideEntry.getValue().doubleValue();
                            }
                        }
                    }
                }

            }

            gratuitySituation.setRemainingValue(new Double(newRemainingValue - payedValue
                    + reimbursedValue));

            gratuitySituation.setExemptionDescription(infoGratuitySituation.getExemptionDescription());
            gratuitySituation.setExemptionPercentage(infoGratuitySituation.getExemptionPercentage());
            gratuitySituation.setExemptionType(infoGratuitySituation.getExemptionType());

            infoGratuitySituation = Cloner
                    .copyIGratuitySituation2InfoGratuitySituation(gratuitySituation);

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException();
        }

        return infoGratuitySituation;
    }
}