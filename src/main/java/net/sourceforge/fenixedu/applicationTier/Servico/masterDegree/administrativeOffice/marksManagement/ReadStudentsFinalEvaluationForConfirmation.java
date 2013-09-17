package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluationWithResponsibleForGrade;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadStudentsFinalEvaluationForConfirmation {

    @Atomic
    public static InfoSiteEnrolmentEvaluation run(String curricularCourseCode, String yearString) throws FenixServiceException {

        List infoEnrolmentEvaluations = new ArrayList();
        InfoTeacher infoTeacher = null;

        final CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseCode);
        final List<Enrolment> enrolments =
                (yearString != null) ? curricularCourse.getEnrolmentsByYear(yearString) : curricularCourse.getEnrolments();

        final List<EnrolmentEvaluation> enrolmentEvaluations = new ArrayList<EnrolmentEvaluation>();
        for (final Enrolment enrolment : enrolments) {
            enrolmentEvaluations.add(enrolment.getLatestEnrolmentEvaluation());
        }

        if (!enrolmentEvaluations.isEmpty()) {

            List temporaryEnrolmentEvaluations = checkForInvalidSituations(enrolmentEvaluations);
            Person person =
                    ((EnrolmentEvaluation) temporaryEnrolmentEvaluations.iterator().next()).getPersonResponsibleForGrade();
            Teacher teacher = Teacher.readTeacherByUsername(person.getUsername());
            infoTeacher = InfoTeacher.newInfoFromDomain(teacher);

            // transform evaluations in databeans
            ListIterator iter = temporaryEnrolmentEvaluations.listIterator();
            while (iter.hasNext()) {
                EnrolmentEvaluation elem = (EnrolmentEvaluation) iter.next();
                InfoEnrolmentEvaluation infoEnrolmentEvaluation =
                        InfoEnrolmentEvaluationWithResponsibleForGrade.newInfoFromDomain(elem);

                infoEnrolmentEvaluation.setInfoEnrolment(InfoEnrolment.newInfoFromDomain(elem.getEnrolment()));
                infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
            }
        }
        if (infoEnrolmentEvaluations.size() == 0) {
            throw new NonExistingServiceException();
        }
        final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(executionSemester);

        InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
        infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
        infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);
        Date evaluationDate = ((InfoEnrolmentEvaluation) infoEnrolmentEvaluations.iterator().next()).getGradeAvailableDate();
        infoSiteEnrolmentEvaluation.setLastEvaluationDate(evaluationDate);
        infoSiteEnrolmentEvaluation.setInfoExecutionPeriod(infoExecutionPeriod);

        return infoSiteEnrolmentEvaluation;
    }

    private static List checkForInvalidSituations(List enrolmentEvaluations) throws ExistingServiceException,
            InvalidSituationServiceException {
        // evaluations can only be confirmated if they are not already
        // confirmated
        List temporaryEnrolmentEvaluations = (List) CollectionUtils.select(enrolmentEvaluations, new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) arg0;
                return enrolmentEvaluation.isTemporary();
            }
        });

        if (temporaryEnrolmentEvaluations == null || temporaryEnrolmentEvaluations.size() == 0) {
            throw new ExistingServiceException();
        }

        List enrolmentEvaluationsWithoutGrade = (List) CollectionUtils.select(temporaryEnrolmentEvaluations, new Predicate() {
            @Override
            public boolean evaluate(Object input) {
                // see if there are evaluations without grade
                EnrolmentEvaluation enrolmentEvaluationInput = (EnrolmentEvaluation) input;
                if (enrolmentEvaluationInput.getGrade().isEmpty()) {
                    return true;
                }
                return false;
            }
        });
        if (enrolmentEvaluationsWithoutGrade != null) {
            if (enrolmentEvaluationsWithoutGrade.size() == temporaryEnrolmentEvaluations.size()) {
                throw new InvalidSituationServiceException();
            }
            temporaryEnrolmentEvaluations =
                    (List) CollectionUtils.subtract(temporaryEnrolmentEvaluations, enrolmentEvaluationsWithoutGrade);
        }

        return temporaryEnrolmentEvaluations;
    }
}