/*
 * Created on 22/Jan/2004
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentWithExecutionPeriodAndYear;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoGratuitySituation;
import DataBeans.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;
import DataBeans.InfoGuideWithGuideEntries;
import Dominio.CursoExecucao;
import Dominio.ExecutionYear;
import Dominio.GratuitySituation;
import Dominio.GuideEntry;
import Dominio.ICursoExecucao;
import Dominio.IEnrollment;
import Dominio.IExecutionYear;
import Dominio.IGratuitySituation;
import Dominio.IGratuityValues;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import Dominio.IStudentCurricularPlan;
import Dominio.reimbursementGuide.IReimbursementGuide;
import Dominio.reimbursementGuide.IReimbursementGuideEntry;
import Dominio.reimbursementGuide.IReimbursementGuideSituation;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.masterDegree.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.IPersistentGratuityValues;
import ServidorPersistente.IPersistentGuide;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.guide.IPersistentReimbursementGuideEntry;
import Util.DocumentType;
import Util.GratuitySituationType;
import Util.ReimbursementGuideState;
import Util.Specialization;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

/**
 * @author Tânia Pousão
 */
public class ReadGratuitySituationListByExecutionDegreeAndSpecialization
        implements IService {

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
    public Object run(Integer executionDegreeId, String executionYearName,
            String specializationName, String gratuitySituationTypeName)
            throws FenixServiceException {

        //at least one of the arguments it's obligator
        if (executionDegreeId == null && executionYearName == null) {
            throw new FenixServiceException(
                    "error.masterDegree.gratuity.impossible.studentsGratuityList");
        }

        //		if (specializationName == null)
        //		{
        //			throw new
        // FenixServiceException("error.masterDegree.gratuity.impossible.studentsGratuityList");
        //		}
        //
        //		if (gratuitySituationTypeName == null)
        //		{
        //			throw new
        // FenixServiceException("error.masterDegree.gratuity.impossible.studentsGratuityList");
        //		}

        HashMap result = null;
        ISuportePersistente sp = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            //Find the execution degree chosen or
            //all execution degrees belong to the execution year chosen
            List executionDegreesList = new ArrayList();
            ICursoExecucaoPersistente persistentExecutionDegree = sp
                    .getICursoExecucaoPersistente();
            if (executionDegreeId != null) {
                ICursoExecucao executionDegree = (ICursoExecucao) persistentExecutionDegree
                        .readByOID(CursoExecucao.class, executionDegreeId);

                executionDegreesList.add(executionDegree);
            } else {
                IExecutionYear executionYear = new ExecutionYear();
                if (executionYearName != null) {
                    IPersistentExecutionYear persistentExecutionYear = sp
                            .getIPersistentExecutionYear();
                    executionYear = persistentExecutionYear
                            .readExecutionYearByName(executionYearName);
                    if (executionYear != null) {
                        executionDegreesList = persistentExecutionDegree.readByExecutionYearAndDegreeType(executionYear, TipoCurso.MESTRADO_OBJ);
                    }
                }
            }
            if (executionDegreesList == null
                    || executionDegreesList.size() == 0) {
                throw new FenixServiceException(
                        "error.masterDegree.gratuity.impossible.studentsGratuityList");
            }

            //SPECIALIZATION
            Specialization specialization = new Specialization(
                    specializationName);

            //GRATUITY SITUATION
            GratuitySituationType gratuitySituationType = GratuitySituationType
                    .getEnum(gratuitySituationTypeName);

            List infoGratuitySituationList = new ArrayList();
            double totalPayedValue = 0;
            double totalRemaingValue = 0;

            //For each execution degree
            //Read all student curricular plan belong to the degree curricular
            // plan
            ListIterator executionDegreeIterator = executionDegreesList
                    .listIterator();
            IStudentCurricularPlanPersistente persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();
            while (executionDegreeIterator.hasNext()) {
                ICursoExecucao executionDegree = (ICursoExecucao) executionDegreeIterator
                        .next();
                //Read Gratuity Value for this Execution Degree
                IPersistentGratuityValues persistentGratuityValues = sp
                        .getIPersistentGratuityValues();
                IGratuityValues gratuityValues = persistentGratuityValues
                        .readGratuityValuesByExecutionDegree(executionDegree);
                if (gratuityValues == null) {
                    System.out.println("Gratuity Values Null");
                    throw new FenixServiceException(
                            "error.impossible.noGratuityValues.degreeName>"
                                    + executionDegree.getCurricularPlan()
                                            .getName());
                }

                List studentCurricularPlanList = persistentStudentCurricularPlan
                        .readAllByDegreeCurricularPlanAndSpecialization(
                                executionDegree.getCurricularPlan(),
                                specialization);
                if (studentCurricularPlanList != null
                        && studentCurricularPlanList.size() > 0) {
                    //Read gratuity situation for each student curricular plan
                    //While each gratuity situation is cloner
                    // and add to the result list
                    // it is calculate the total values of payed and remaning
                    // values.
                    IPersistentGratuitySituation persistentGratuitySituation = sp
                            .getIPersistentGratuitySituation();
                    ListIterator studentIterator = studentCurricularPlanList
                            .listIterator();
                    while (studentIterator.hasNext()) {
                        IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) studentIterator
                                .next();

                        IGratuitySituation gratuitySituation = persistentGratuitySituation
                                .readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(
                                        studentCurricularPlan, gratuityValues);
                        if (gratuitySituation == null) {
                            //If the student curricular plan doesn't have the
                            // correspond
                            // gratuity situation
                            //it is created with null values
                            gratuitySituation = new GratuitySituation();
                            gratuitySituation.setGratuityValues(gratuityValues);
                            gratuitySituation
                                    .setStudentCurricularPlan(studentCurricularPlan);

                            persistentGratuitySituation
                                    .simpleLockWrite(gratuitySituation);
                        }
                        //CLONER
                        //InfoGratuitySituation infoGratuitySituation = Cloner
                        //.copyIGratuitySituation2InfoGratuitySituation(gratuitySituation);
                        InfoGratuitySituation infoGratuitySituation = InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree
                                .newInfoFromDomain(gratuitySituation);
                        infoGratuitySituation.getInfoStudentCurricularPlan()
                                .setInfoEnrolments(
                                        clonerEnrolments(gratuitySituation
                                                .getStudentCurricularPlan()
                                                .getEnrolments()));

                        //find gratuity's payed value
                        calculateGratuityPayedValue(sp, infoGratuitySituation);

                        //find gratuity's total value with exemption
                        calculateGratuityRemainingValue(sp,
                                infoGratuitySituation);

                        fillSituationType(infoGratuitySituation);

                        if (gratuitySituationType == null
                                || infoGratuitySituation.getSituationType()
                                        .equals(gratuitySituationType)) {
                            if (verifySpecializationOfThisYear(
                                    infoGratuitySituation, executionDegree
                                            .getExecutionYear())) {
                                infoGratuitySituationList
                                        .add(infoGratuitySituation);

                                //add all value for find the total
                                totalPayedValue = totalPayedValue
                                        + infoGratuitySituation.getPayedValue()
                                                .doubleValue();
                                totalRemaingValue = totalRemaingValue
                                        + infoGratuitySituation
                                                .getRemainingValue()
                                                .doubleValue();
                            }
                        }
                    } //studentIterator
                }
            } //executionDegreeIterator

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

    /**
     * @param infoGratuitySituation
     * @return
     */
    private boolean verifySpecializationOfThisYear(
            InfoGratuitySituation infoGratuitySituation,
            IExecutionYear executionYear) {

        boolean result = true;

        if (infoGratuitySituation.getInfoStudentCurricularPlan()
                .getSpecialization().equals(Specialization.ESPECIALIZACAO_TYPE)) {
            List enrolmentsList = infoGratuitySituation
                    .getInfoStudentCurricularPlan().getInfoEnrolments();
            InfoExecutionPeriod executionPeriodEnrollment = null;
            if (enrolmentsList != null && enrolmentsList.size() > 0) {
                executionPeriodEnrollment = ((InfoEnrolment) enrolmentsList
                        .get(0)).getInfoExecutionPeriod();
                if (executionPeriodEnrollment.getInfoExecutionYear() != null
                        && executionPeriodEnrollment.getInfoExecutionYear()
                                .getYear() != null
                        && !executionPeriodEnrollment.getInfoExecutionYear()
                                .getYear().equals(executionYear.getYear())) {
                    result = false;
                }
            } else {
                result = true; //hasn't enrolments, but appear in list
            }
        }
        return result;
    }

    /**
     * Clone the enrolments´s list
     * 
     * @param list
     * @return
     */
    private List clonerEnrolments(List enrolments) {

        List infoEnrolments = null;

        if (enrolments != null || enrolments.size() > 0) {
            infoEnrolments = new ArrayList();
            ListIterator iterator = enrolments.listIterator();
            while (iterator.hasNext()) {
                IEnrollment enrolment = (IEnrollment) iterator.next();
                try {
                    //CLONER
                    //infoEnrolments.add(Cloner
                    //.copyIEnrolment2InfoEnrolment(enrolment));
                    infoEnrolments.add(InfoEnrolmentWithExecutionPeriodAndYear
                            .newInfoFromDomain(enrolment));
                } catch (Exception exception) {
                    System.out.println("-->"
                            + enrolment.getStudentCurricularPlan().getStudent()
                                    .getNumber());
                    exception.printStackTrace();
                }
            }

        }

        return infoEnrolments;
    }

    /**
     * @param sp
     * @param infoGratuitySituation
     */
    private void calculateGratuityPayedValue(ISuportePersistente sp,
            InfoGratuitySituation infoGratuitySituation)
            throws ExcepcaoPersistencia, Exception {

        //all guides of this student that aren't annulled but payed
        IPersistentGuide persistentGuide = sp.getIPersistentGuide();
        List guideList = persistentGuide
                .readNotAnnulledAndPayedByPersonAndExecutionDegree(
                        infoGratuitySituation.getInfoStudentCurricularPlan()
                                .getInfoStudent().getInfoPerson()
                                .getIdInternal(), infoGratuitySituation
                                .getInfoGratuityValues()
                                .getInfoExecutionDegree().getIdInternal());
        List infoGuideList = (List) CollectionUtils.collect(guideList,
                new Transformer() {

                    public Object transform(Object arg0) {

                        IGuide guide = (IGuide) arg0;
                        //CLONER
                        //InfoGuide infoGuide = Cloner
                        //.copyIGuide2InfoGuide(guide);
                        InfoGuide infoGuide = InfoGuideWithGuideEntries
                                .newInfoFromDomain(guide);
                        return infoGuide;
                    }
                });

        addPayedGuide(sp, infoGratuitySituation, infoGuideList);
    }

    /**
     * @param infoGuideList
     * @return
     */
    private void addPayedGuide(ISuportePersistente sp,
            InfoGratuitySituation infoGratuitySituation, List infoGuideList)
            throws Exception {

        double payedValue = 0;
        ListIterator iterator = infoGuideList.listIterator();
        while (iterator.hasNext()) {
            InfoGuide infoGuide = (InfoGuide) iterator.next();
            List infoGuideEntriesList = infoGuide.getInfoGuideEntries();
            if (infoGuideEntriesList != null
                    && infoGuideEntriesList.size() >= 0) {
                ListIterator iteratorGuideEntries = infoGuideEntriesList
                        .listIterator();
                while (iteratorGuideEntries.hasNext()) {
                    InfoGuideEntry infoGuideEntry = (InfoGuideEntry) iteratorGuideEntries
                            .next();
                    if (infoGuideEntry.getDocumentType().equals(
                            DocumentType.GRATUITY_TYPE)) {

                        payedValue = payedValue
                                + (infoGuideEntry.getPrice().doubleValue() * infoGuideEntry
                                        .getQuantity().intValue());

                        IPersistentReimbursementGuideEntry persistentReimbursementGuideEntry = sp
                                .getIPersistentReimbursementGuideEntry();
                        IGuideEntry guideEntry = new GuideEntry();
                        guideEntry
                                .setIdInternal(infoGuideEntry.getIdInternal());
                        List reimbursementGuideEntries = persistentReimbursementGuideEntry
                                .readByGuideEntry(guideEntry);
                        IReimbursementGuideEntry reimbursementGuideEntry = null;

                        Iterator reimbursementGuideEntriesIterator = reimbursementGuideEntries
                                .iterator();
                        while (reimbursementGuideEntriesIterator.hasNext()) {
                            reimbursementGuideEntry = (IReimbursementGuideEntry) reimbursementGuideEntriesIterator
                                    .next();

                            IReimbursementGuide reimbursementGuide = reimbursementGuideEntry
                                    .getReimbursementGuide();
                            IReimbursementGuideSituation reimbursementGuideSituation = reimbursementGuide
                                    .getActiveReimbursementGuideSituation();
                            ReimbursementGuideState reimbursementGuideState = reimbursementGuideSituation
                                    .getReimbursementGuideState();

                            if (reimbursementGuideState
                                    .equals(ReimbursementGuideState.PAYED)) {
                                payedValue = payedValue
                                        - reimbursementGuideEntry.getValue()
                                                .doubleValue();
                            }
                        }
                    } else if (infoGuideEntry.getDocumentType().equals(
                            DocumentType.INSURANCE_TYPE)) {
                        infoGratuitySituation
                                .setInsurancePayed(SessionConstants.PAYED_INSURANCE);
                    }
                }
            }
        }

        BigDecimal roundedSum = new BigDecimal(payedValue);
        infoGratuitySituation.setPayedValue(new Double(roundedSum.setScale(2,
                BigDecimal.ROUND_HALF_UP).doubleValue()));
    }

    /**
     * @param sp
     * @param infoGratuitySituation
     */
    private void calculateGratuityRemainingValue(ISuportePersistente sp,
            InfoGratuitySituation infoGratuitySituation) throws Exception {

        //first find the total value that it will be pay
        if (infoGratuitySituation.getInfoStudentCurricularPlan()
                .getSpecialization() != null
                && (infoGratuitySituation.getInfoStudentCurricularPlan()
                        .getSpecialization().equals(
                                Specialization.MESTRADO_TYPE) || infoGratuitySituation
                        .getInfoStudentCurricularPlan().getSpecialization()
                        .equals(Specialization.INTEGRADO_TYPE))) {
            if (infoGratuitySituation.getInfoGratuityValues().getAnualValue() != null
                    && infoGratuitySituation.getInfoGratuityValues()
                            .getAnualValue().doubleValue() > 0) {
                infoGratuitySituation.setRemainingValue(infoGratuitySituation
                        .getInfoGratuityValues().getAnualValue());
            } else if (infoGratuitySituation.getInfoGratuityValues()
                    .getScholarShipValue() != null
                    && infoGratuitySituation.getInfoGratuityValues()
                            .getScholarShipValue().doubleValue() > 0
                    && infoGratuitySituation.getInfoGratuityValues()
                            .getFinalProofValue() != null
                    && infoGratuitySituation.getInfoGratuityValues()
                            .getFinalProofValue().doubleValue() > 0) {
                valueToPayedForEnrolment(infoGratuitySituation);

                if (infoGratuitySituation
                        .getInfoStudentCurricularPlan()
                        .getCurrentState()
                        .equals(
                                StudentCurricularPlanState.SCHOOLPARTCONCLUDED_OBJ)) {
                    double totalToPay = 0;
                    if (infoGratuitySituation.getRemainingValue() != null
                            && verifyIfAlreadyPayedEnrolments(sp,
                                    infoGratuitySituation) < infoGratuitySituation
                                    .getRemainingValue().doubleValue()) {
                        totalToPay = infoGratuitySituation.getRemainingValue()
                                .doubleValue();
                    }

                    totalToPay = totalToPay
                            + infoGratuitySituation.getInfoGratuityValues()
                                    .getFinalProofValue().doubleValue();
                    infoGratuitySituation.setRemainingValue(new Double(
                            totalToPay));
                }
            }
        } else if (infoGratuitySituation.getInfoStudentCurricularPlan()
                .getSpecialization() != null
                && (infoGratuitySituation.getInfoStudentCurricularPlan()
                        .getSpecialization()
                        .equals(Specialization.ESPECIALIZACAO_TYPE))) {

            valueToPayedForEnrolment(infoGratuitySituation);
        }

        //discount exemption
        if (infoGratuitySituation.getExemptionPercentage() != null
                && infoGratuitySituation.getExemptionPercentage().doubleValue() > 0) {
            double exemptionDiscount = infoGratuitySituation
                    .getRemainingValue().doubleValue()
                    * (infoGratuitySituation.getExemptionPercentage()
                            .doubleValue() / 100.0);
            double newValue = infoGratuitySituation.getRemainingValue()
                    .doubleValue()
                    - exemptionDiscount;
            infoGratuitySituation.setRemainingValue(new Double(newValue));
        } //now find the remaining value to pay
        double remainingValue = infoGratuitySituation.getRemainingValue()
                .doubleValue()
                - infoGratuitySituation.getPayedValue().doubleValue();

        BigDecimal roundedSum = new BigDecimal(remainingValue);
        infoGratuitySituation.setRemainingValue(new Double(roundedSum.setScale(
                2, BigDecimal.ROUND_HALF_UP).doubleValue()));
    }

    private double verifyIfAlreadyPayedEnrolments(ISuportePersistente sp,
            InfoGratuitySituation infoGratuitySituation)
            throws ExcepcaoPersistencia {
        //all guides of this student that are payed
        IPersistentGuide persistentGuide = sp.getIPersistentGuide();
        List guideList = persistentGuide
                .readNotAnnulledAndPayedByPerson(infoGratuitySituation
                        .getInfoStudentCurricularPlan().getInfoStudent()
                        .getInfoPerson().getIdInternal());
        List infoGuideList = (List) CollectionUtils.collect(guideList,
                new Transformer() {

                    public Object transform(Object arg0) {

                        IGuide guide = (IGuide) arg0;
                        //CLONER
                        //InfoGuide infoGuide = Cloner
                        //.copyIGuide2InfoGuide(guide);
                        InfoGuide infoGuide = InfoGuideWithGuideEntries
                                .newInfoFromDomain(guide);
                        return infoGuide;
                    }
                });

        ListIterator iterator = infoGuideList.listIterator();
        double payedValue = 0;
        while (iterator.hasNext()) {
            InfoGuide infoGuide = (InfoGuide) iterator.next();
            List infoGuideEntriesList = infoGuide.getInfoGuideEntries();
            if (infoGuideEntriesList != null
                    && infoGuideEntriesList.size() >= 0) {
                ListIterator iteratorGuideEntries = infoGuideEntriesList
                        .listIterator();
                while (iteratorGuideEntries.hasNext()) {
                    InfoGuideEntry infoGuideEntry = (InfoGuideEntry) iteratorGuideEntries
                            .next();
                    if (infoGuideEntry.getDocumentType().equals(
                            DocumentType.GRATUITY_TYPE)) {
                        payedValue = payedValue
                                + (infoGuideEntry.getPrice().doubleValue() * infoGuideEntry
                                        .getQuantity().intValue());
                    }
                }
            }
        }

        return payedValue;
    }

    private void valueToPayedForEnrolment(
            InfoGratuitySituation infoGratuitySituation) {
        if (infoGratuitySituation.getInfoStudentCurricularPlan()
                .getInfoEnrolments() != null
                && infoGratuitySituation.getInfoStudentCurricularPlan()
                        .getInfoEnrolments().size() > 0) {
            if (infoGratuitySituation.getInfoGratuityValues().getCourseValue() != null
                    && infoGratuitySituation.getInfoGratuityValues()
                            .getCourseValue().doubleValue() != 0) {
                infoGratuitySituation.setRemainingValue(new Double(
                        infoGratuitySituation.getInfoGratuityValues()
                                .getCourseValue().doubleValue()
                                * infoGratuitySituation
                                        .getInfoStudentCurricularPlan()
                                        .getInfoEnrolments().size()));
            } else {
                double totalToPay = 0;
                Iterator iterCourse = infoGratuitySituation
                        .getInfoStudentCurricularPlan().getInfoEnrolments()
                        .iterator();
                while (iterCourse.hasNext()) {
                    InfoEnrolment infoEnrolment = (InfoEnrolment) iterCourse
                            .next();
                    totalToPay += infoEnrolment.getInfoCurricularCourse()
                            .getCredits().doubleValue()
                            * infoGratuitySituation.getInfoGratuityValues()
                                    .getCreditValue().doubleValue();
                }
                infoGratuitySituation.setRemainingValue(new Double(totalToPay));
            }
        } else {
            infoGratuitySituation.setRemainingValue(new Double(0));
        }
    }

    private void fillSituationType(InfoGratuitySituation infoGratuitySituation)
            throws Exception { //infoGratuitySituation.getRemainingValue()
        // contains the total value that
        // a student has to

        // payed.
        if (infoGratuitySituation.getRemainingValue().longValue() > 0) {
            infoGratuitySituation
                    .setSituationType(GratuitySituationType.DEBTOR);
        } else if (infoGratuitySituation.getRemainingValue().longValue() == 0) {
            infoGratuitySituation
                    .setSituationType(GratuitySituationType.REGULARIZED);
        } else if (infoGratuitySituation.getRemainingValue().longValue() < 0) {
            infoGratuitySituation
                    .setSituationType(GratuitySituationType.CREDITOR);
        }
    }
}