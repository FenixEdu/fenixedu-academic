package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.GratuitySituation;
import Dominio.ICursoExecucao;
import Dominio.IEnrollment;
import Dominio.IExecutionYear;
import Dominio.IGratuitySituation;
import Dominio.IGratuityValues;
import Dominio.IStudentCurricularPlan;
import Dominio.reimbursementGuide.IReimbursementGuideEntry;
import Dominio.transactions.GratuityTransaction;
import Dominio.transactions.IGratuityTransaction;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.IPersistentGratuityValues;
import ServidorPersistente.IPersistentStudentCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.ReimbursementGuideState;
import Util.Specialization;
import Util.TipoCurso;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class CreateGratuitySituationsForCurrentExecutionYear implements IService {

    /**
     * Constructor
     */
    public CreateGratuitySituationsForCurrentExecutionYear() {
    }

    public void run() throws FenixServiceException {
        try {

            System.out.println("createGratuitySituation task running!!!");

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionYear executionYear = (IExecutionYear) sp.getIPersistentExecutionYear()
                    .readCurrentExecutionYear();

            IPersistentGratuityValues gratuityValuesDAO = sp.getIPersistentGratuityValues();

            IPersistentStudentCurricularPlan studentCurricularPlanDAO = sp
                    .getIStudentCurricularPlanPersistente();

            IPersistentGratuitySituation gratuitySituationDAO = sp.getIPersistentGratuitySituation();

            // read master degree and specialization execution degrees
            List executionDegreeList = sp.getIPersistentExecutionDegree()
                    .readByExecutionYearAndDegreeType(executionYear, TipoCurso.MESTRADO_OBJ);

            for (Iterator iter = executionDegreeList.iterator(); iter.hasNext();) {

                ICursoExecucao executionDegree = (ICursoExecucao) iter.next();
                IGratuityValues gratuityValues = gratuityValuesDAO
                        .readGratuityValuesByExecutionDegree(executionDegree);

                if (gratuityValues == null) {
                    System.out.println("GRATUITY VALUES NOT DEFINED FOR EXECUTION DEGREE "
                            + executionDegree.getCurricularPlan() + " - "
                            + executionDegree.getExecutionYear().getYear());
                    continue;
                }

                List studentCurricularPlanList = studentCurricularPlanDAO
                        .readByDegreeCurricularPlan(executionDegree.getCurricularPlan());

                for (Iterator iterator = studentCurricularPlanList.iterator(); iterator.hasNext();) {

                    IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator
                            .next();

                    IGratuitySituation gratuitySituation = gratuitySituationDAO
                            .readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(
                                    studentCurricularPlan, gratuityValues);

                    if (gratuitySituation == null) {
                        createGratuitySituation(executionYear, gratuityValues, studentCurricularPlan,
                                gratuitySituationDAO);
                    } else {
                        updateGratuitySituation(executionYear, gratuityValues, gratuitySituation,
                                studentCurricularPlan, gratuitySituationDAO);
                    }

                }

            }

            System.out.println("createGratuitySituation task finnished!!!");

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }

    /**
     * @param gratuitySituation
     * @param gratuitySituationDAO
     */
    private void updateGratuitySituation(IExecutionYear executionYear, IGratuityValues gratuityValues,
            IGratuitySituation gratuitySituation, IStudentCurricularPlan studentCurricularPlan,
            IPersistentGratuitySituation gratuitySituationDAO) throws ExcepcaoPersistencia {

        Double totalValue = null;

        if (studentCurricularPlan.getSpecialization().equals(Specialization.ESPECIALIZACAO_TYPE)) {

            totalValue = calculateTotalValueForSpecialization(executionYear, gratuityValues,
                    studentCurricularPlan);

            if (totalValue == null) {
                //nothing to do
                return;
            }

        } else {

            totalValue = calculateTotalValueForMasterDegree(gratuityValues);

            /* to fix inconsistent data, we are always updating total value */
            //            if ((totalValue == null) ||
            // (totalValue.equals(gratuitySituation.getTotalValue()))) {
            if (totalValue == null) {

                //nothing to do
                return;

            }

        }
        gratuitySituationDAO.simpleLockWrite(gratuitySituation);
        gratuitySituation.setTotalValue(totalValue);

        // calculate the value to subtract (Exemption + Payed Value)
        double valueToSubtract = calculateValueToSubtract(gratuitySituation);

        Double remainingValue = new Double(totalValue.doubleValue() - valueToSubtract);

        gratuitySituation.setRemainingValue(remainingValue);

        // }

    }

    /**
     * Calculates the value to subtract (Exemption + payed value) to total value
     * 
     * @param gratuitySituation
     * @return
     */
    public double calculateValueToSubtract(IGratuitySituation gratuitySituation) {

        double exemptionValue = 0;

        if (gratuitySituation.getExemptionPercentage() != null) {
            exemptionValue = gratuitySituation.getTotalValue().doubleValue()
                    * (gratuitySituation.getExemptionPercentage().doubleValue() / 100.0);
        }

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

        return (exemptionValue + payedValue - reimbursedValue);
    }

    /**
     * @param gratuityValues
     * @param studentCurricularPlan
     * @throws ExcepcaoPersistencia
     */
    private void createGratuitySituation(IExecutionYear executionYear, IGratuityValues gratuityValues,
            IStudentCurricularPlan studentCurricularPlan,
            IPersistentGratuitySituation gratuitySituationDAO) throws ExcepcaoPersistencia {

        IGratuitySituation gratuitySituation = new GratuitySituation();

        gratuitySituation.setGratuityValues(gratuityValues);
        gratuitySituation.setStudentCurricularPlan(studentCurricularPlan);
        gratuitySituation.setWhen(Calendar.getInstance().getTime());
        Double totalValue = null;

        if (studentCurricularPlan.getSpecialization().equals(Specialization.MESTRADO_TYPE)) {
            totalValue = calculateTotalValueForMasterDegree(gratuityValues);
        }
        //        else if
        // (studentCurricularPlan.getSpecialization().equals(Specialization.ESPECIALIZACAO_TYPE))
        // {
        //            totalValue = calculateTotalValueForSpecialization(executionYear,
        // gratuityValues,
        //                    studentCurricularPlan);
        //        }

        if ((totalValue == null)) {
            //the student has nothing to pay
            System.out.println("DATABASE IS INCONSISTENT. TOTAL VALUE SHOULD NEVER BE NULL!!!!");
            return;
        }

        gratuitySituation.setRemainingValue(totalValue);
        gratuitySituation.setTotalValue(totalValue);

        gratuitySituationDAO.simpleLockWrite(gratuitySituation);

    }

    /**
     * @param gratuityValues
     * @return
     */
    private Double calculateTotalValueForMasterDegree(IGratuityValues gratuityValues) {
        Double totalValue = null;

        Double annualValue = gratuityValues.getAnualValue();

        if ((annualValue != null) && (annualValue.doubleValue() != 0)) {
            //we have data to calculate using annual value
            totalValue = annualValue;
        } else {
            //we have to use the components (scholarship + final proof)
            // information
            totalValue = new Double(gratuityValues.getScholarShipValue().doubleValue()
                    + (gratuityValues.getFinalProofValue() == null ? 0 : gratuityValues
                            .getFinalProofValue().doubleValue()));

        }

        return totalValue;
    }

    /**
     * @param gratuityValues
     * @return
     */
    private Double calculateTotalValueForSpecialization(IExecutionYear executionYear,
            IGratuityValues gratuityValues, IStudentCurricularPlan studentCurricularPlan) {

        Double totalValue = null;

        if ((gratuityValues.getCourseValue() != null)
                && (gratuityValues.getCourseValue().doubleValue() != 0)) {

            // calculate using value per course
            double valuePerCourse = gratuityValues.getCourseValue().doubleValue();

            int totalCourses = 0;

            for (Iterator iter = studentCurricularPlan.getEnrolments().iterator(); iter.hasNext();) {

                IEnrollment enrolment = (IEnrollment) iter.next();

                if (enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {

                    totalCourses++;
                }
            }

            totalValue = new Double(totalCourses * valuePerCourse);

        } else if ((gratuityValues.getCreditValue() != null)
                && (gratuityValues.getCreditValue().doubleValue() != 0)) {

            // calculate using value per credit
            double valuePerCredit = gratuityValues.getCreditValue().doubleValue();

            double totalCredits = 0;

            for (Iterator iter = studentCurricularPlan.getEnrolments().iterator(); iter.hasNext();) {

                IEnrollment enrolment = (IEnrollment) iter.next();

                if (enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {

                    totalCredits += enrolment.getCurricularCourse().getCredits().doubleValue();
                }
            }

            totalValue = new Double(totalCredits * valuePerCredit);

        }

        return totalValue;
    }
}