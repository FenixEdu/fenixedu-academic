package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.utils.enrolment.DeleteEnrolmentUtils;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionByCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRestriction;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author David Santos Jan 26, 2004
 * @author João Mota Jul 23, 2004
 */
public class DeleteEnrolment implements IService {
    public DeleteEnrolment() {
    }

    // some of these arguments may be null. they are only needed for filter
    public void run(Integer executionDegreeId, Integer studentCurricularPlanId, Integer enrolmentID)
            throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentEnrollment enrolmentDAO = persistentSuport.getIPersistentEnrolment();
            IPersistentEnrolmentEvaluation enrolmentEvaluationDAO = persistentSuport
                    .getIPersistentEnrolmentEvaluation();
            IPersistentRestriction persistentRestriction = persistentSuport.getIPersistentRestriction();
            final IEnrollment enrollment1 = (IEnrollment) enrolmentDAO.readByOID(Enrolment.class,
                    enrolmentID);

            List enrollments2Delete = new ArrayList();
            List studentEnrolledEnrollmentsInExecutionPeriod = enrollment1.getStudentCurricularPlan()
                    .getAllStudentEnrolledEnrollmentsInExecutionPeriod(enrollment1.getExecutionPeriod());
            List finalEnrollments2Delete = new ArrayList();
            enrollments2Delete.addAll(CollectionUtils.select(
                    studentEnrolledEnrollmentsInExecutionPeriod, new Predicate() {

                        public boolean evaluate(Object arg0) {
                            IEnrollment enrollment2 = (IEnrollment) arg0;
                            return enrollment2.getCurricularCourse()
                                    .getCurricularYearByBranchAndSemester(
                                            enrollment2.getStudentCurricularPlan().getBranch(),
                                            enrollment2.getExecutionPeriod().getSemester()).getYear()
                                    .intValue() > enrollment1.getCurricularCourse()
                                    .getCurricularYearByBranchAndSemester(
                                            enrollment1.getStudentCurricularPlan().getBranch(),
                                            enrollment1.getExecutionPeriod().getSemester()).getYear()
                                    .intValue();
                        }
                    }));

            if (enrollment1 != null) {
                List restrictions = persistentRestriction.readByCurricularCourseAndRestrictionClass(
                        enrollment1.getCurricularCourse(),

                        RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse.class);
                if (restrictions != null) {
                    Iterator iter = restrictions.iterator();
                    while (iter.hasNext()) {
                        final IRestrictionByCurricularCourse restriction = (RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse) iter
                                .next();
                        if (enrollment1.getStudentCurricularPlan().isCurricularCourseEnrolled(
                                restriction.getPrecedence().getCurricularCourse())) {
                            IEnrollment enrollment2Delete = (IEnrollment) CollectionUtils.find(
                                    studentEnrolledEnrollmentsInExecutionPeriod, new Predicate() {

                                        public boolean evaluate(Object arg0) {
                                            IEnrollment enrollment = (IEnrollment) arg0;
                                            return enrollment.getCurricularCourse().equals(
                                                    restriction.getPrecedence().getCurricularCourse());
                                        }
                                    });
                            if (enrollment2Delete != null
                                    && !finalEnrollments2Delete.contains(enrollment2Delete)) {
                                finalEnrollments2Delete.add(enrollment2Delete);
                            }
                        }
                    }
                }
            }
            finalEnrollments2Delete.add(enrollment1);
            finalEnrollments2Delete.addAll(enrollments2Delete);
            Iterator iter = finalEnrollments2Delete.iterator();
            while (iter.hasNext()) {
                DeleteEnrolmentUtils.deleteEnrollment(enrolmentDAO, enrolmentEvaluationDAO, (IEnrollment) iter.next());
            }

        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException(e);
        }
    }


}