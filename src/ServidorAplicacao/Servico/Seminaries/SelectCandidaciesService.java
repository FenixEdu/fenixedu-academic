/*
 * Created on Aug 24, 2004
 *
 */
package ServidorAplicacao.Servico.Seminaries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.Seminaries.CandidacyDTO;
import DataBeans.Seminaries.InfoClassification;
import DataBeans.Seminaries.InfoSeminary;
import DataBeans.Seminaries.SelectCandidaciesDTO;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.Seminaries.ICandidacy;
import Dominio.Seminaries.ISeminary;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminary;

/**
 * @author João Mota
 *  
 */
public class SelectCandidaciesService implements IService {

    /**
     *  
     */
    public SelectCandidaciesService() {
    }

    public SelectCandidaciesDTO run(Boolean inEnrollmentPeriod, Integer seminaryID)
            throws FenixServiceException {
        SelectCandidaciesDTO result = new SelectCandidaciesDTO();
        ISuportePersistente persistenceSupport;
        try {
            persistenceSupport = SuportePersistenteOJB.getInstance();

            IPersistentObject persistentObject = persistenceSupport.getIPersistentObject();
            IPersistentSeminary persistentSeminary = persistenceSupport.getIPersistentSeminary();
            List seminaries = persistentSeminary.readAll();
            List infoSeminaries = getSeminaries(inEnrollmentPeriod, seminaries);
            result.setSeminaries(infoSeminaries);
            List candidacies = getCandidacies(seminaryID, seminaries, persistentObject);
            Iterator iter = candidacies.iterator();
            List infoCandidacies = new ArrayList();
            while (iter.hasNext()) {
                ICandidacy candidacy = (ICandidacy) iter.next();
                IStudent student = candidacy.getStudent();
                IStudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(student);
                List enrollments = studentCurricularPlan.getEnrolments();

                CandidacyDTO candidacyDTO = new CandidacyDTO();
                candidacyDTO.setNumber(student.getNumber());
                candidacyDTO.setName(student.getPerson().getNome());
                candidacyDTO.setUsername(student.getPerson().getUsername());
                candidacyDTO.setEmail(student.getPerson().getNome());
                candidacyDTO.setInfoClassification(getInfoClassification(enrollments));
                candidacyDTO.setCandidacyId(candidacy.getIdInternal());
                if (candidacy.getApproved() != null) {
                    candidacyDTO.setApproved(candidacy.getApproved());
                } else {
                    candidacyDTO.setApproved(Boolean.FALSE);
                }
                infoCandidacies.add(candidacyDTO);
            }

            result.setCandidacies(infoCandidacies);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return result;
    }

    /**
     * @param enrollments
     * @param infoClassification
     */
    private InfoClassification getInfoClassification(List enrollments) {
        InfoClassification infoClassification = new InfoClassification();
        int auxInt = 0;
        float acc = 0;
        float grade = 0;
        for (Iterator iter1 = enrollments.iterator(); iter1.hasNext();) {
            IEnrollment enrollment = (IEnrollment) iter1.next();
            List enrollmentEvaluations = enrollment.getEvaluations();
            IEnrolmentEvaluation enrollmentEvaluation = null;
            if (enrollmentEvaluations != null && !enrollmentEvaluations.isEmpty()) {
                Collections.sort(enrollmentEvaluations);
                Collections.reverse(enrollmentEvaluations);
                enrollmentEvaluation = (IEnrolmentEvaluation) enrollmentEvaluations.get(0);
            }

            String stringGrade;
            if (enrollmentEvaluation != null) {

                stringGrade = enrollmentEvaluation.getGrade();
            } else {
                stringGrade = "NA";
            }

            if (stringGrade != null && !stringGrade.equals("RE") && !stringGrade.equals("NA")) {
                Float gradeObject = new Float(stringGrade);
                grade = gradeObject.floatValue();
                acc += grade;
                auxInt++;
            }

        }
        if (auxInt != 0) {
            String value = new DecimalFormat("#0.0").format(acc / auxInt);
            infoClassification.setAritmeticClassification(value);
        }
        infoClassification.setCompletedCourses(new Integer(auxInt).toString());
        return infoClassification;
    }

    /**
     * @param student
     * @return
     */
    private IStudentCurricularPlan getStudentCurricularPlan(IStudent student) {
        List curricularPlans = student.getStudentCurricularPlans();
        long startDate = Long.MAX_VALUE;
        IStudentCurricularPlan selectedSCP = null;
        for (Iterator iter = curricularPlans.iterator(); iter.hasNext();) {
            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iter.next();
            if (studentCurricularPlan.getStartDate().getTime() < startDate) {
                startDate = studentCurricularPlan.getStartDate().getTime();
                selectedSCP = studentCurricularPlan;
            }
        }
        return selectedSCP;
    }

    /**
     * @param seminaryID
     * @param seminaries
     * @param persistentObject
     * @return
     */
    private List getCandidacies(final Integer seminaryID, List seminaries,
            IPersistentObject persistentObject) {
        ISeminary seminary = (ISeminary) CollectionUtils.find(seminaries, new Predicate() {

            public boolean evaluate(Object arg0) {
                ISeminary seminary = (ISeminary) arg0;
                return seminary.getIdInternal().equals(seminaryID);
            }
        });

        return seminary.getCandidacies();
    }

    /**
     * @param persistentSeminary
     * @param inEnrollmentPeriod
     * @return @throws
     *         ExcepcaoPersistencia
     */
    private List getSeminaries(Boolean inEnrollmentPeriod, List seminaries) throws ExcepcaoPersistencia {
        List result = new ArrayList();

        for (Iterator iterator = seminaries.iterator(); iterator.hasNext();) {
            InfoSeminary infoSeminary = InfoSeminary.newInfoFromDomain((ISeminary) iterator.next());

            Calendar now = new GregorianCalendar();
            Calendar endDate = new GregorianCalendar();
            Calendar beginDate = new GregorianCalendar();
            endDate.setTimeInMillis(infoSeminary.getEnrollmentEndTime().getTimeInMillis()
                    + infoSeminary.getEnrollmentEndDate().getTimeInMillis());
            beginDate.setTimeInMillis(infoSeminary.getEnrollmentBeginTime().getTimeInMillis()
                    + infoSeminary.getEnrollmentBeginDate().getTimeInMillis());
            if ((!inEnrollmentPeriod.booleanValue()) || (endDate.after(now) && beginDate.before(now)))
                result.add(infoSeminary);
        }
        return result;
    }

}