package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGratuitySituation;
import DataBeans.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import Dominio.CursoExecucao;
import Dominio.ExecutionYear;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IGratuitySituation;
import Dominio.IGratuityValues;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.masterDegree.utils.SessionConstants;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.IPersistentGratuityValues;
import ServidorPersistente.IPersistentStudentCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.transactions.IPersistentInsuranceTransaction;
import Util.GratuitySituationType;
import Util.Specialization;
import Util.TipoCurso;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */

public class ReadGratuitySituationListByExecutionDegreeAndSpecialization implements IService {

    /**
     * Constructor
     */
    public ReadGratuitySituationListByExecutionDegreeAndSpecialization() {

    }

    /*
     * Return an hash map with three objects: 1. at first position a list of
     * infoGratuitySituation 2. in second, a double with the total of list's
     * payed values 3. in third, a double with the total of list's remaning
     * values
     */
    public Object run(Integer executionDegreeId, String executionYearName, String specializationName,
            String gratuitySituationTypeName) throws FenixServiceException {

        //at least one of the arguments it's obligator
        if (executionDegreeId == null && executionYearName == null) {
            throw new FenixServiceException(
                    "error.masterDegree.gratuity.impossible.studentsGratuityList");
        }

        HashMap result = null;
        ISuportePersistente sp = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();
            IPersistentGratuityValues gratuityValuesDAO = sp.getIPersistentGratuityValues();
            IPersistentStudentCurricularPlan studentCurricularPlanDAO = sp
                    .getIStudentCurricularPlanPersistente();
            IPersistentGratuitySituation gratuitySituationDAO = sp.getIPersistentGratuitySituation();

            IPersistentInsuranceTransaction insuranceTransactionDAO = sp
                    .getIPersistentInsuranceTransaction();

            List executionDegreeList = new ArrayList();

            if (executionDegreeId != null) {

                ICursoExecucao executionDegree = (ICursoExecucao) executionDegreeDAO.readByOID(
                        CursoExecucao.class, executionDegreeId);
                executionDegreeList.add(executionDegree);

            } else {
                // the execution degree wasn't supplied so
                // we have to show all execution degrees from the choosen year
                IExecutionYear executionYear = new ExecutionYear();
                if (executionYearName != null) {
                    IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
                    executionYear = persistentExecutionYear.readExecutionYearByName(executionYearName);
                    if (executionYear != null) {
                        executionDegreeList = executionDegreeDAO.readByExecutionYearAndDegreeType(
                                executionYear, TipoCurso.MESTRADO_OBJ);
                    }
                }
            }

            if (executionDegreeList == null || executionDegreeList.size() == 0) {
                throw new FenixServiceException(
                        "error.masterDegree.gratuity.impossible.studentsGratuityList");
            }

            //SPECIALIZATION
            Specialization specialization = new Specialization(specializationName);

            //GRATUITY SITUATION
            GratuitySituationType gratuitySituationType = GratuitySituationType
                    .getEnum(gratuitySituationTypeName);

            List infoGratuitySituationList = new ArrayList();
            double totalPayedValue = 0;
            double totalRemaingValue = 0;

            for (Iterator iter = executionDegreeList.iterator(); iter.hasNext();) {
                ICursoExecucao executionDegree = (ICursoExecucao) iter.next();
                IGratuityValues gratuityValues = gratuityValuesDAO
                        .readGratuityValuesByExecutionDegree(executionDegree);

                if (gratuityValues == null) {
                    System.out.println("Gratuity Values Null do Curso: "
                            + executionDegree.getCurricularPlan().getName() + " relativo ao ano "
                            + executionDegree.getExecutionYear().getYear());
                    continue;
                    //                    throw new FenixServiceException(
                    //                            "error.impossible.noGratuityValues.degreeName>"
                    //                                    + executionDegree.getCurricularPlan()
                    //                                            .getName());
                }

                List studentCurricularPlanList = studentCurricularPlanDAO
                        .readAllByDegreeCurricularPlanAndSpecialization(executionDegree
                                .getCurricularPlan(), specialization);

                for (Iterator iterator = studentCurricularPlanList.iterator(); iterator.hasNext();) {
                    IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator
                            .next();

                    IGratuitySituation gratuitySituation = gratuitySituationDAO
                            .readByStudentCurricularPlanAndGratuityValuesAndGratuitySituationType(
                                    studentCurricularPlan, gratuityValues, gratuitySituationType);

                    if (gratuitySituation == null) {
                        //ignore them, because they will be created in the next
                        // day
                        //when the gratuity situation creator scheduled task
                        // runs
                        continue;
                    }

                    InfoGratuitySituation infoGratuitySituation = InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree
                            .newInfoFromDomain(gratuitySituation);

                    fillSituationType(infoGratuitySituation);

                    List insuranceTransactionList = insuranceTransactionDAO
                            .readAllNonReimbursedByExecutionYearAndStudent(executionDegree
                                    .getExecutionYear(), studentCurricularPlan.getStudent());

                    /*
                     * IInsuranceTransaction insuranceTransaction =
                     * insuranceTransactionDAO
                     * .readByExecutionYearAndStudent(executionDegree
                     * .getExecutionYear(), studentCurricularPlan
                     * .getStudent());
                     */

                    if (insuranceTransactionList.size() > 0) {
                        infoGratuitySituation.setInsurancePayed(SessionConstants.PAYED_INSURANCE);
                    } else {
                        infoGratuitySituation.setInsurancePayed(SessionConstants.NOT_PAYED_INSURANCE);
                    }

                    if (infoGratuitySituation.getTotalValue() != null
                            && infoGratuitySituation.getRemainingValue() != null) {

                        double exemption = 0;
                        if (infoGratuitySituation.getExemptionPercentage() != null) {
                            exemption = infoGratuitySituation.getTotalValue().doubleValue()
                                    * infoGratuitySituation.getExemptionPercentage().doubleValue() / 100;
                        }

                        Double payedValue = new Double(infoGratuitySituation.getTotalValue()
                                .doubleValue()
                                - infoGratuitySituation.getRemainingValue().doubleValue() - exemption);
                        infoGratuitySituation.setPayedValue(payedValue);

                        totalPayedValue += payedValue.doubleValue();

                    }

                    infoGratuitySituationList.add(infoGratuitySituation);

                    totalRemaingValue += infoGratuitySituation.getRemainingValue().doubleValue();

                }

            }

            //build the result that is a hash map with a list, total payed and
            // remaining value
            result = new HashMap();
            result.put(new Integer(0), infoGratuitySituationList);
            result.put(new Integer(1), new Double(totalPayedValue));
            result.put(new Integer(2), new Double(totalRemaingValue));
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().length() > 0) {
                throw new FenixServiceException(e.getMessage());
            }
            throw new FenixServiceException(
                    "error.masterDegree.gratuity.impossible.studentsGratuityList");

        }

        return result;
    }

    private void fillSituationType(InfoGratuitySituation infoGratuitySituation) throws Exception { //infoGratuitySituation.getRemainingValue()
        // contains the total value that
        // a student has to

        // payed.
        if (infoGratuitySituation.getRemainingValue().longValue() > 0) {
            infoGratuitySituation.setSituationType(GratuitySituationType.DEBTOR);
        } else if (infoGratuitySituation.getRemainingValue().longValue() == 0) {
            infoGratuitySituation.setSituationType(GratuitySituationType.REGULARIZED);
        } else if (infoGratuitySituation.getRemainingValue().longValue() < 0) {
            infoGratuitySituation.setSituationType(GratuitySituationType.CREDITOR);
        }
    }
}