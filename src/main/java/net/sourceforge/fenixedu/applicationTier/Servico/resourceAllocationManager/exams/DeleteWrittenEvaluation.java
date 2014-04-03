package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.EditWrittenEvaluationAuthorization;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ResourceAllocationManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExamDateCertificateRequest;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteWrittenEvaluation {

    /**
     * @param Integer
     *            executionCourseOID used in filtering
     *            (ExecutionCourseLecturingTeacherAuthorizationFilter)
     */
    protected void run(String executionCourseOID, String writtenEvaluationOID) throws FenixServiceException {
        final WrittenEvaluation writtenEvaluationToDelete =
                (WrittenEvaluation) FenixFramework.getDomainObject(writtenEvaluationOID);
        if (writtenEvaluationToDelete == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }
        if (writtenEvaluationToDelete instanceof Exam) {
            disconnectExamCertificateRequests(writtenEvaluationToDelete);
        }
        if (writtenEvaluationToDelete.hasAnyVigilancies()) {
            notifyVigilants(writtenEvaluationToDelete);
        }
        writtenEvaluationToDelete.delete();
    }

    private void disconnectExamCertificateRequests(WrittenEvaluation writtenEvaluationToDelete) {
        Exam examToDelete = (Exam) writtenEvaluationToDelete;
        for(ExamDateCertificateRequest examDateCertificateRequest : examToDelete.getExamDateCertificateRequestsSet()){
            examDateCertificateRequest.removeExams(examToDelete);
        }
    }

    private void notifyVigilants(WrittenEvaluation writtenEvaluation) {

        final Set<Person> tos = new HashSet<Person>();

        for (VigilantGroup group : writtenEvaluation.getAssociatedVigilantGroups()) {
            tos.clear();
            DateTime date = writtenEvaluation.getBeginningDateTime();
            String time = writtenEvaluation.getBeginningDateHourMinuteSecond().toString();
            String beginDateString = date.getDayOfMonth() + "-" + date.getMonthOfYear() + "-" + date.getYear();

            String subject =
                    BundleUtil.getStringFromResourceBundle("resources.VigilancyResources", "email.convoke.subject", new String[] {
                            writtenEvaluation.getName(), group.getName(), beginDateString, time });
            String body =
                    BundleUtil.getStringFromResourceBundle("resources.VigilancyResources",
                            "label.writtenEvaluationDeletedMessage", new String[] { writtenEvaluation.getName(), beginDateString,
                                    time });
            for (Vigilancy vigilancy : writtenEvaluation.getVigilancies()) {
                Person person = vigilancy.getVigilantWrapper().getPerson();
                tos.add(person);
            }
            Sender sender = Bennu.getInstance().getSystemSender();
            new Message(sender, new ConcreteReplyTo(group.getContactEmail()).asCollection(),
                    new Recipient(new FixedSetGroup(tos)).asCollection(), subject, body, "");

        }
    }

    // Service Invokers migrated from Berserk

    private static final DeleteWrittenEvaluation serviceInstance = new DeleteWrittenEvaluation();

    @Atomic
    public static void runDeleteWrittenEvaluation(String executionCourseOID, String writtenEvaluationOID)
            throws FenixServiceException, NotAuthorizedException {
        EditWrittenEvaluationAuthorization.instance.execute(writtenEvaluationOID);
        try {
            ResourceAllocationManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(executionCourseOID, writtenEvaluationOID);
        } catch (NotAuthorizedException ex1) {
            try {
                ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseOID);
                serviceInstance.run(executionCourseOID, writtenEvaluationOID);
            } catch (NotAuthorizedException ex2) {
                try {
                    ExecutionCourseCoordinatorAuthorizationFilter.instance.execute(executionCourseOID);
                    serviceInstance.run(executionCourseOID, writtenEvaluationOID);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}