package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.precedences.RestrictionByCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author David Santos Jan 26, 2004
 * @author João Mota Jul 23, 2004
 */
public class DeleteEnrolment extends Service {

    // some of these arguments may be null. they are only needed for filter
    public void run(Integer executionDegreeId, Registration registration, Integer enrolmentID)
            throws FenixServiceException, DomainException, ExcepcaoPersistencia {

        final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
        final Enrolment enrollment1 = studentCurricularPlan.findEnrolmentByEnrolmentID(enrolmentID);

        Set<Enrolment> enrollments2Delete = new HashSet<Enrolment>();
        List<Enrolment> studentEnrolledEnrollmentsInExecutionPeriod = enrollment1.getStudentCurricularPlan()
                .getAllStudentEnrolledEnrollmentsInExecutionPeriod(enrollment1.getExecutionPeriod());
        Set finalEnrollments2Delete = new HashSet();
        enrollments2Delete.addAll(CollectionUtils.select(studentEnrolledEnrollmentsInExecutionPeriod,
                new Predicate() {

                    public boolean evaluate(Object arg0) {
                        Enrolment enrollment2 = (Enrolment) arg0;
                        return enrollment2.getCurricularCourse().getCurricularYearByBranchAndSemester(
                                enrollment2.getStudentCurricularPlan().getBranch(),
                                enrollment2.getExecutionPeriod().getSemester()).getYear().intValue() > enrollment1
                                .getCurricularCourse().getCurricularYearByBranchAndSemester(
                                        enrollment1.getStudentCurricularPlan().getBranch(),
                                        enrollment1.getExecutionPeriod().getSemester()).getYear()
                                .intValue();
                    }
                }));

        if (enrollment1 != null) {
        	List<RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse> restrictions = enrollment1.getCurricularCourse().getRestrictionsHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse();
            if (restrictions != null) {
                Iterator<RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse> iter = restrictions.iterator();
                while (iter.hasNext()) {
                    final RestrictionByCurricularCourse restriction = (RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse) iter
                            .next();
                    if (enrollment1.getStudentCurricularPlan().isCurricularCourseEnrolled(
                            restriction.getPrecedence().getCurricularCourse())) {
                        Enrolment enrollment2Delete = (Enrolment) CollectionUtils.find(
                                studentEnrolledEnrollmentsInExecutionPeriod, new Predicate() {

                                    public boolean evaluate(Object arg0) {
                                        Enrolment enrollment = (Enrolment) arg0;
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
        
        //Código q apaga as cadeiras do 5º ano, caso seja apagada uma cadeira do 5º ano
        CurricularYear curricularYear = enrollment1.getCurricularCourse().getCurricularYearByBranchAndSemester(enrollment1.getStudentCurricularPlan().getBranch(), enrollment1.getExecutionPeriod().getSemester());
        if(curricularYear.getYear().equals(5)) {
            for (Enrolment enrolment : studentEnrolledEnrollmentsInExecutionPeriod) {
        	if(enrolment.getCurricularCourse().getCurricularYearByBranchAndSemester(enrolment.getStudentCurricularPlan().getBranch(), enrolment.getExecutionPeriod().getSemester()).getYear().equals(5)){
        	    finalEnrollments2Delete.add(enrolment);
        	}
	    }
        }
        //
        
        finalEnrollments2Delete.add(enrollment1);
        finalEnrollments2Delete.addAll(enrollments2Delete);
        Iterator<Enrolment> iter = finalEnrollments2Delete.iterator();
        while (iter.hasNext()) {
            iter.next().unEnroll();
        }

    }

}