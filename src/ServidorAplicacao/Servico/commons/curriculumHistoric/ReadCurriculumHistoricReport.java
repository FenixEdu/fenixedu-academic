/*
 * Created on Oct 11, 2004
 */
package ServidorAplicacao.Servico.commons.curriculumHistoric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseWithInfoDegree;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlanAndInfoStudent;
import DataBeans.InfoExecutionYear;
import DataBeans.commons.curriculumHistoric.InfoCurriculumHistoricReport;
import Dominio.CurricularCourse;
import Dominio.ExecutionYear;
import Dominio.ICurricularCourse;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.commons.student.GetEnrolmentGrade;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrollmentState;
import Util.EnrolmentEvaluationType;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadCurriculumHistoricReport implements IService {

    public InfoCurriculumHistoricReport run(Integer curricularCourseID, Integer semester,
            Integer executionYearID) throws FenixServiceException {
        try {
            ISuportePersistente suportePersistente = SuportePersistenteOJB.getInstance();
            //read ExecutionYear
            IPersistentExecutionYear persistentExecutionYear = suportePersistente
                    .getIPersistentExecutionYear();
            IExecutionYear executionYear = (ExecutionYear) persistentExecutionYear.readByOID(
                    ExecutionYear.class, executionYearID, false);

            //read ExecutionPeriod
            IPersistentExecutionPeriod persistentExecutionPeriod = suportePersistente
                    .getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readBySemesterAndExecutionYear(
                    semester, executionYear);

            //read CurricularCourse
            IPersistentCurricularCourse persistentCurricularCourse = suportePersistente
                    .getIPersistentCurricularCourse();
            ICurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseID);
            //read all enrollments
            IPersistentEnrollment persistentEnrollment = suportePersistente.getIPersistentEnrolment();
            List enrollments = persistentEnrollment.readByCurricularCourseAndExecutionPeriod(
                    curricularCourse, executionPeriod);

            InfoCurriculumHistoricReport infoCurriculumHistoricReport = createInfoCurriculumHistoricReport(enrollments);

            infoCurriculumHistoricReport.setSemester(semester);

            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
                    .newInfoFromDomain(curricularCourse);
            infoCurriculumHistoricReport.setInfoCurricularCourse(infoCurricularCourse);

            InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionYear);
            infoCurriculumHistoricReport.setInfoExecutionYear(infoExecutionYear);

            return infoCurriculumHistoricReport;
        } catch (ExcepcaoPersistencia ep) {
            throw new FenixServiceException(ep);
        }

    }

    /**
     * @param enrollments
     * @return
     */
    private InfoCurriculumHistoricReport createInfoCurriculumHistoricReport(List enrollments)
            throws FenixServiceException {

        List notAnulledEnrollments = (List) CollectionUtils.select(enrollments, new Predicate() {

            public boolean evaluate(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                if (!enrollment.getEnrollmentState().equals(EnrollmentState.ANNULED)) {
                    return true;
                }
                return false;
            }
        });

        List evaluatedEnrollments = (List) CollectionUtils.select(notAnulledEnrollments,
                new Predicate() {

                    public boolean evaluate(Object obj) {
                        IEnrollment enrollment = (IEnrollment) obj;
                        if (enrollment.getEnrollmentState().equals(EnrollmentState.APROVED)
                                || enrollment.getEnrollmentState().equals(EnrollmentState.NOT_APROVED)) {
                            return true;
                        }
                        return false;
                    }
                });

        List aprovedEnrollments = (List) CollectionUtils.select(evaluatedEnrollments, new Predicate() {

            public boolean evaluate(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                if (enrollment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
                    return true;
                }
                return false;
            }
        });

        InfoCurriculumHistoricReport infoCurriculumHistoricReport = new InfoCurriculumHistoricReport();
        infoCurriculumHistoricReport.setEnrolled(new Integer(notAnulledEnrollments.size()));
        infoCurriculumHistoricReport.setEvaluated(new Integer(evaluatedEnrollments.size()));
        infoCurriculumHistoricReport.setAproved(new Integer(aprovedEnrollments.size()));

        GetEnrolmentGrade getEnrollmentGrade = new GetEnrolmentGrade();
        Iterator iterator = notAnulledEnrollments.iterator();
        List infoEnrollments = new ArrayList();
        while (iterator.hasNext()) {
            IEnrollment enrolmentTemp = (IEnrollment) iterator.next();

            InfoEnrolmentEvaluation infoEnrolmentEvaluation = getEnrollmentGrade.run(enrolmentTemp);

            InfoEnrolment infoEnrolment = InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlanAndInfoStudent
                    .newInfoFromDomain(enrolmentTemp);

            infoEnrolment.setInfoEnrolmentEvaluation(infoEnrolmentEvaluation);
            
            setInfoEnrolmentByEnrolmentEvaluationType(infoEnrolment, enrolmentTemp.getEvaluations());

            infoEnrollments.add(infoEnrolment);
        }

        infoCurriculumHistoricReport.setEnrollments(infoEnrollments);

        return infoCurriculumHistoricReport;
    }

    /**
     * @param infoEnrolment
     * @param evaluations
     */
    private void setInfoEnrolmentByEnrolmentEvaluationType(InfoEnrolment infoEnrolment, List evaluations) {
        List normalEnrolmentEvaluations = new ArrayList();
        List specialSeasonEnrolmentEvaluations = new ArrayList();
        List improvmentEnrolmentEvaluations = new ArrayList();
        List equivalenceEnrolmentEvaluations = new ArrayList();
        
        Iterator iterator = evaluations.iterator();
        while(iterator.hasNext()) {
            IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator.next();
            if(enrolmentEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.NORMAL_OBJ)
                    || enrolmentEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.FIRST_SEASON_OBJ)
                    || enrolmentEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.SECOND_SEASON_OBJ)
                    || enrolmentEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.NO_SEASON_OBJ))
                normalEnrolmentEvaluations.add(enrolmentEvaluation);
            
            if(enrolmentEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT_OBJ))
                improvmentEnrolmentEvaluations.add(enrolmentEvaluation);
            
            if(enrolmentEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.SPECIAL_SEASON_OBJ))
                specialSeasonEnrolmentEvaluations.add(enrolmentEvaluation);
            
            if(enrolmentEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.EQUIVALENCE_OBJ))
                equivalenceEnrolmentEvaluations.add(enrolmentEvaluation);
        }
        
        infoEnrolment.setInfoNormalEnrolmentEvaluation(getLatestInfoEnrolmentEvaluation(normalEnrolmentEvaluations));
        infoEnrolment.setInfoImprovmentEnrolmentEvaluation(getLatestInfoEnrolmentEvaluation(improvmentEnrolmentEvaluations));
        infoEnrolment.setInfoSpecialSeasonEnrolmentEvaluation(getLatestInfoEnrolmentEvaluation(specialSeasonEnrolmentEvaluations));
        infoEnrolment.setInfoEquivalenceEnrolmentEvaluation(getLatestInfoEnrolmentEvaluation(equivalenceEnrolmentEvaluations));
    }

    /**
     * @param normalEnrolmentEvaluations
     * @return
     */
    private InfoEnrolmentEvaluation getLatestInfoEnrolmentEvaluation(List enrolmentEvaluations) {
        
        if(enrolmentEvaluations.isEmpty())
            return null;
        
        // This sorts the list ascendingly so we need to reverse it to get
        // the first object.
        Collections.sort(enrolmentEvaluations);
        Collections.reverse(enrolmentEvaluations);
        
        return InfoEnrolmentEvaluation.newInfoFromDomain((IEnrolmentEvaluation) enrolmentEvaluations.get(0));
    }

}
