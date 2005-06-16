package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IGratuityValues;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.gratuity.GratuitySituationType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuityValues;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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

        // at least one of the arguments it's obligator
        if (executionDegreeId == null && executionYearName == null) {
            throw new FenixServiceException(
                    "error.masterDegree.gratuity.impossible.studentsGratuityList");
        }

        HashMap result = null;
        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();
            IPersistentGratuityValues gratuityValuesDAO = sp.getIPersistentGratuityValues();
            IPersistentStudentCurricularPlan studentCurricularPlanDAO = sp
                    .getIStudentCurricularPlanPersistente();
            IPersistentGratuitySituation gratuitySituationDAO = sp.getIPersistentGratuitySituation();

            IPersistentInsuranceTransaction insuranceTransactionDAO = sp
                    .getIPersistentInsuranceTransaction();

            List executionDegreeList = new ArrayList();

            if (executionDegreeId != null) {

                IExecutionDegree executionDegree = (IExecutionDegree) executionDegreeDAO.readByOID(
                        ExecutionDegree.class, executionDegreeId);
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
                                executionYear.getYear(), DegreeType.MASTER_DEGREE);
                    }
                }
            }

            if (executionDegreeList == null || executionDegreeList.size() == 0) {
                throw new FenixServiceException(
                        "error.masterDegree.gratuity.impossible.studentsGratuityList");
            }

            // SPECIALIZATION
            Specialization specialization = null;
            if (!specializationName.equals("all")) {
                specialization = Specialization.valueOf(specializationName);
            }

            // GRATUITY SITUATION
            GratuitySituationType gratuitySituationType = null;
            if (!gratuitySituationTypeName.equals("all")) {
                gratuitySituationType = GratuitySituationType.valueOf(gratuitySituationTypeName);
            }

            List infoGratuitySituationList = new ArrayList();
            double totalPayedValue = 0;
            double totalRemaingValue = 0;

            for (Iterator iter = executionDegreeList.iterator(); iter.hasNext();) {
                IExecutionDegree executionDegree = (IExecutionDegree) iter.next();
                IGratuityValues gratuityValues = executionDegree.getGratuityValues();

                if (gratuityValues == null) {
                    continue;
                }

                List allStudentCurricularPlans = executionDegree.getDegreeCurricularPlan().getStudentCurricularPlans();
                List filteredStudentCurricularPlans = (List) CollectionUtils.select(allStudentCurricularPlans,new Predicate(){

                    public boolean evaluate(Object arg0) {
                        IStudentCurricularPlan scp = (IStudentCurricularPlan) arg0;
                        return scp.getSpecialization() != null;
                    }});

                for (Iterator iterator = filteredStudentCurricularPlans.iterator(); iterator.hasNext();) {
                    IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator
                            .next();

                    IGratuitySituation gratuitySituation = gratuitySituationDAO
                            .readByStudentCurricularPlanAndGratuityValuesAndGratuitySituationType(
                                    studentCurricularPlan.getIdInternal(), gratuityValues
                                            .getIdInternal(), gratuitySituationType);

                    if (gratuitySituation == null) {
                        // ignore them, because they will be created in the next
                        // day
                        // when the gratuity situation creator scheduled task
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

                        if (infoGratuitySituation.getExemptionValue() != null) {
                            exemption += infoGratuitySituation.getExemptionValue().doubleValue();
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

            // build the result that is a hash map with a list, total payed and
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

    private void fillSituationType(InfoGratuitySituation infoGratuitySituation) throws Exception { // infoGratuitySituation.getRemainingValue()
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