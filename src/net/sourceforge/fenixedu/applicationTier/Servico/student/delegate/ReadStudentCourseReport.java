/*
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.delegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteEvaluationStatistics;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoSiteStudentCourseReport;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoStudentCourseReport;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.gesdis.IStudentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentStudentCourseReport;
import net.sourceforge.fenixedu.util.EnrollmentState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public class ReadStudentCourseReport implements IService {

    public ReadStudentCourseReport() {
    }

    public InfoSiteStudentCourseReport run(Integer objectId) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            IPersistentStudentCourseReport persistentStudentCourseReport = sp
                    .getIPersistentStudentCourseReport();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, objectId);
            IStudentCourseReport studentCourseReport = persistentStudentCourseReport
                    .readByCurricularCourse(curricularCourse);

            List infoScopes = (List) CollectionUtils.collect(curricularCourse.getScopes(),
                    new Transformer() {

                        public Object transform(Object arg0) {
                            ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                            return Cloner
                                    .copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
                        }

                    });

            InfoStudentCourseReport infoStudentCourseReport = null;
            if (studentCourseReport == null) {
                infoStudentCourseReport = new InfoStudentCourseReport();
                InfoCurricularCourse infoCurricularCourse = Cloner
                        .copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                infoCurricularCourse.setInfoScopes(infoScopes);
                infoStudentCourseReport.setInfoCurricularCourse(infoCurricularCourse);
            } else {
                infoStudentCourseReport = Cloner
                        .copyIStudentCourseReport2InfoStudentCourseReport(studentCourseReport);
                InfoCurricularCourse infoCurricularCourse = infoStudentCourseReport
                        .getInfoCurricularCourse();
                infoCurricularCourse.setInfoScopes(infoScopes);
            }

            InfoSiteStudentCourseReport infoSiteStudentCourseReport = new InfoSiteStudentCourseReport();
            List executionPeriods = persistentExecutionPeriod.readAllExecutionPeriod();

            Collections.sort(executionPeriods, new Comparator() {

                public int compare(Object o1, Object o2) {
                    IExecutionPeriod executionPeriod1 = (IExecutionPeriod) o1;
                    IExecutionPeriod executionPeriod2 = (IExecutionPeriod) o2;
                    return executionPeriod2.getEndDate().compareTo(executionPeriod1.getEndDate());
                }
            });

            IExecutionPeriod executionPeriod = null;
            // read the second element of the list corresponding to the last
            // executionPeriod
            if ((executionPeriods != null) && (executionPeriods.size() >= 2))
                executionPeriod = (IExecutionPeriod) executionPeriods.get(1);

            List infoSiteEvaluationHistory = getInfoSiteEvaluationsHistory(
                    executionPeriod, curricularCourse, sp);

            InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = getInfoSiteEvaluationStatistics(
                    executionPeriod, curricularCourse, sp);

            infoSiteStudentCourseReport.setInfoStudentCourseReport(infoStudentCourseReport);
            infoSiteStudentCourseReport.setInfoSiteEvaluationHistory(infoSiteEvaluationHistory);
            infoSiteStudentCourseReport.setInfoSiteEvaluationStatistics(infoSiteEvaluationStatistics);

            return infoSiteStudentCourseReport;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }
    }

    /**
     * @param period
     * @param curricularCourses
     * @param sp
     * @return
     */
    private InfoSiteEvaluationStatistics getInfoSiteEvaluationStatistics(
            IExecutionPeriod executionPeriod, ICurricularCourse curricularCourse, ISuportePersistente sp)
            throws ExcepcaoPersistencia {

        InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = new InfoSiteEvaluationStatistics();
        List enrolled = getEnrolled(executionPeriod, curricularCourse, sp);
        infoSiteEvaluationStatistics.setEnrolled(new Integer(enrolled.size()));
        infoSiteEvaluationStatistics.setEvaluated(getEvaluated(enrolled));
        infoSiteEvaluationStatistics.setApproved(getApproved(enrolled));
        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(executionPeriod);
        infoSiteEvaluationStatistics.setInfoExecutionPeriod(infoExecutionPeriod);

        return infoSiteEvaluationStatistics;
    }

    /**
     * @param executionPeriod
     * @param curricularCourse
     * @param sp
     * @return
     */
    private List getInfoSiteEvaluationsHistory(IExecutionPeriod executionPeriodToTest, ICurricularCourse curricularCourse,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        List infoSiteEvaluationsHistory = new ArrayList();
        List executionPeriods = (List) CollectionUtils.collect(curricularCourse
                .getAssociatedExecutionCourses(), new Transformer() {
            public Object transform(Object arg0) {
                IExecutionCourse executionCourse = (IExecutionCourse) arg0;
                return executionCourse.getExecutionPeriod();
            }

        });
        // filter the executionPeriods by semester
        // also, information regarding execution years after the course's execution year must not be shown
        final IExecutionPeriod historyExecutionPeriod = executionPeriodToTest;
        executionPeriods = (List) CollectionUtils.select(executionPeriods, new Predicate() {
            public boolean evaluate(Object arg0) {
                IExecutionPeriod executionPeriod = (IExecutionPeriod) arg0;
                return (executionPeriod.getSemester().equals(historyExecutionPeriod.getSemester()) && executionPeriod.getExecutionYear().getBeginDate().before(historyExecutionPeriod.getExecutionYear().getBeginDate()));
            }
        });
        Collections.sort(executionPeriods, new Comparator() {
            public int compare(Object o1, Object o2) {
                IExecutionPeriod executionPeriod1 = (IExecutionPeriod) o1;
                IExecutionPeriod executionPeriod2 = (IExecutionPeriod) o2;
                return executionPeriod1.getExecutionYear().getYear().compareTo(
                        executionPeriod2.getExecutionYear().getYear());
            }
        });
        Iterator iter = executionPeriods.iterator();
        while (iter.hasNext()) {
            IExecutionPeriod executionPeriod = (IExecutionPeriod) iter.next();

            InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = new InfoSiteEvaluationStatistics();
            infoSiteEvaluationStatistics.setInfoExecutionPeriod((InfoExecutionPeriod) Cloner
                    .get(executionPeriod));
            List enrolled = getEnrolled(executionPeriod, curricularCourse, sp);
            infoSiteEvaluationStatistics.setEnrolled(new Integer(enrolled.size()));
            infoSiteEvaluationStatistics.setEvaluated(getEvaluated(enrolled));
            infoSiteEvaluationStatistics.setApproved(getApproved(enrolled));
            infoSiteEvaluationsHistory.add(infoSiteEvaluationStatistics);
        }

        return infoSiteEvaluationsHistory;
    }

    /**
     * @param curricularCourses
     * @param sp
     * @return
     */
    private Integer getApproved(List enrolments) {
        int approved = 0;
        Iterator iter = enrolments.iterator();
        while (iter.hasNext()) {
            IEnrollment enrolment = (IEnrollment) iter.next();
            EnrollmentState enrollmentState = enrolment.getEnrollmentState();
            if (enrollmentState.equals(EnrollmentState.APROVED)) {
                approved++;
            }
        }
        return new Integer(approved);
    }

    /**
     * @param curricularCourses
     * @param sp
     * @return
     */
    private Integer getEvaluated(List enrolments) {
        int evaluated = 0;
        Iterator iter = enrolments.iterator();
        while (iter.hasNext()) {
            IEnrollment enrolment = (IEnrollment) iter.next();
            EnrollmentState enrollmentState = enrolment.getEnrollmentState();
            if (enrollmentState.equals(EnrollmentState.APROVED)
                    || enrollmentState.equals(EnrollmentState.NOT_APROVED)) {
                evaluated++;
            }
        }
        return new Integer(evaluated);
    }

    /**
     * @param curricularCourses
     * @param sp
     * @return
     */
    private List getEnrolled(IExecutionPeriod executionPeriod, ICurricularCourse curricularCourse,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();
        List enrolments = persistentEnrolment.readByCurricularCourseAndExecutionPeriod(curricularCourse,
                executionPeriod);
        return enrolments;
    }

}