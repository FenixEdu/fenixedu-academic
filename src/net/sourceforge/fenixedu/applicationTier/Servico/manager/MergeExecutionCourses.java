/*
 * Created on 29/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.FinalEvaluation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseForum;
import net.sourceforge.fenixedu.domain.messaging.ForumSubscription;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt"> João Mota </a> 29/Nov/2003
 * 
 */
public class MergeExecutionCourses extends Service {

    public class SourceAndDestinationAreTheSameException extends FenixServiceException {
        private static final long serialVersionUID = 3761968254943244338L;
    }

    public class DuplicateShiftNameException extends FenixServiceException {
        private static final long serialVersionUID = 3761968254943244338L;
    }

    public void run(Integer executionCourseDestinationId, Integer executionCourseSourceId)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (executionCourseDestinationId.equals(executionCourseSourceId)) {
            throw new SourceAndDestinationAreTheSameException();
        }

        final ExecutionCourse executionCourseFrom = rootDomainObject
                .readExecutionCourseByOID(executionCourseSourceId);
        if (executionCourseFrom == null) {
            throw new InvalidArgumentsServiceException();
        }

        final ExecutionCourse executionCourseTo = rootDomainObject
                .readExecutionCourseByOID(executionCourseDestinationId);
        if (executionCourseTo == null) {
            throw new InvalidArgumentsServiceException();
        }

        if (!isMergeAllowed(executionCourseFrom, executionCourseTo)) {
            throw new InvalidArgumentsServiceException();
        }

        if (haveShiftsWithSameName(executionCourseFrom, executionCourseTo)) {
            throw new DuplicateShiftNameException();
        }

        copyShifts(executionCourseFrom, executionCourseTo);
        copyProfessorships(executionCourseFrom, executionCourseTo);
        copyAttends(executionCourseFrom, executionCourseTo);
        copyBibliographicReference(executionCourseFrom, executionCourseTo);
        if (executionCourseFrom.getEvaluationMethod() != null) {
            executionCourseFrom.getEvaluationMethod().delete();
        }
        if (executionCourseFrom.getCourseReport() != null) {
            executionCourseFrom.getCourseReport().delete();
        }
        copySummaries(executionCourseFrom, executionCourseTo);
        copyGroupPropertiesExecutionCourse(executionCourseFrom, executionCourseTo);
        copySite(executionCourseFrom, executionCourseTo);
        removeEvaluations(executionCourseFrom, executionCourseTo);
        copyForuns(executionCourseFrom, executionCourseTo);

        executionCourseTo.getAssociatedCurricularCourses().addAll(
                executionCourseFrom.getAssociatedCurricularCourses());

        executionCourseFrom.delete();
    }

    private boolean haveShiftsWithSameName(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) {
        final Set<String> shiftNames = new HashSet<String>();
        for (final Shift shift : executionCourseFrom.getAssociatedShifts()) {
            shiftNames.add(shift.getNome());
        }
        for (final Shift shift : executionCourseTo.getAssociatedShifts()) {
            if (shiftNames.contains(shift.getNome())) {
                return true;
            }
        }
        return false;
    }

    private boolean isMergeAllowed(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) {

        boolean distributedTestAuthorization = false;

        List<Metadata> metadatas = executionCourseFrom.getMetadatas();
        List<DistributedTest> distributedTests = TestScope.readDistributedTestsByTestScope(
                executionCourseFrom.getClass(), executionCourseFrom.getIdInternal());
        distributedTestAuthorization = (metadatas == null || metadatas.isEmpty())
                && (distributedTests == null || distributedTests.isEmpty());

        return executionCourseTo != null
                && executionCourseFrom != null
                && executionCourseFrom.getExecutionPeriod().equals(
                        executionCourseTo.getExecutionPeriod())
                && executionCourseFrom != executionCourseTo && distributedTestAuthorization;
    }

    private void copySummaries(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) {
        final List<Summary> associatedSummaries = new ArrayList<Summary>();
        associatedSummaries.addAll(executionCourseFrom.getAssociatedSummaries());
        for (final Summary summary : associatedSummaries) {
            summary.setExecutionCourse(executionCourseTo);
        }
    }

    private void copyGroupPropertiesExecutionCourse(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) {
        final List<ExportGrouping> associatedGroupPropertiesExecutionCourse = new ArrayList<ExportGrouping>();
        associatedGroupPropertiesExecutionCourse.addAll(executionCourseFrom.getExportGroupings());

        for (final ExportGrouping groupPropertiesExecutionCourse : associatedGroupPropertiesExecutionCourse) {
            if (executionCourseTo.hasGrouping(groupPropertiesExecutionCourse.getGrouping())) {
                groupPropertiesExecutionCourse.delete();
            } else {
                groupPropertiesExecutionCourse.setExecutionCourse(executionCourseTo);
            }
        }
    }

    private void removeEvaluations(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) throws ExcepcaoPersistencia, FenixServiceException {
        while (!executionCourseFrom.getAssociatedEvaluations().isEmpty()) {
            final Evaluation evaluation = executionCourseFrom.getAssociatedEvaluations().get(0);
            if (evaluation instanceof FinalEvaluation) {
                final FinalEvaluation finalEvaluationFrom = (FinalEvaluation) evaluation;
                if (finalEvaluationFrom.hasAnyMarks()) {
                    throw new FenixServiceException(
                            "Cannot merge execution courses: marks exist for final evaluation.");
                } else {
                    finalEvaluationFrom.delete();
                }
            } else {
                executionCourseTo.getAssociatedEvaluations().add(evaluation);
                executionCourseFrom.getAssociatedEvaluations().remove(evaluation);
            }
        }
    }

    private void copyBibliographicReference(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) {
        for (; !executionCourseFrom.getAssociatedBibliographicReferences().isEmpty(); executionCourseTo
                .getAssociatedBibliographicReferences().add(
                        executionCourseFrom.getAssociatedBibliographicReferences().get(0)))
            ;
    }

    private void copyShifts(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) {
        final List<Shift> associatedShifts = new ArrayList<Shift>(executionCourseFrom
                .getAssociatedShifts());
        for (final Shift shift : associatedShifts) {
            shift.setDisciplinaExecucao(executionCourseTo);
        }
    }

    private void copyAttends(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) throws ExcepcaoPersistencia, FenixServiceException {
        while (!executionCourseFrom.getAttends().isEmpty()) {
            final Attends attends = executionCourseFrom.getAttends().get(0);
            final Attends otherAttends = executionCourseTo.getAttendsByStudent(attends.getAluno());
            if (otherAttends == null) {
                attends.setDisciplinaExecucao(executionCourseTo);
            } else {
                if (attends.hasEnrolment() && !otherAttends.hasAluno()) {
                    otherAttends.setEnrolment(attends.getEnrolment());
                } else if (otherAttends.hasEnrolment() && attends.hasAluno()) {
                    throw new FenixServiceException("Unable to merge execution courses. Registration "
                            + attends.getAluno().getNumber() + " has an enrolment in both.");
                }
                for (; !attends.getAssociatedMarks().isEmpty(); otherAttends.addAssociatedMarks(attends
                        .getAssociatedMarks().get(0)))
                    ;
                for (; !attends.getStudentGroups().isEmpty(); otherAttends.addStudentGroups(attends
                        .getStudentGroups().get(0)))
                    ;
                attends.delete();
            }
        }

        final Iterator associatedAttendsFromDestination = executionCourseTo.getAttendsIterator();
        final Map<String, Attends> alreadyAttendingDestination = new HashMap<String, Attends>();
        while (associatedAttendsFromDestination.hasNext()) {
            Attends attend = (Attends) associatedAttendsFromDestination.next();
            alreadyAttendingDestination.put(attend.getAluno().getNumber().toString(), attend);
        }
        final List<Attends> associatedAttendsFromSource = new ArrayList<Attends>();
        associatedAttendsFromSource.addAll(executionCourseFrom.getAttends());
        for (final Attends attend : associatedAttendsFromSource) {
            if (!alreadyAttendingDestination.containsKey(attend.getAluno().getNumber().toString())) {
                attend.setDisciplinaExecucao(executionCourseTo);
            }
        }
    }

    private void copySite(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) {
        final Site siteFrom = executionCourseFrom.getSite();
        final Site siteTo = executionCourseTo.getSite();

        if (siteFrom != null) {            
            for (; !siteFrom.getAssociatedSections().isEmpty(); siteTo.addAssociatedSections(siteFrom
                    .getAssociatedSections().get(0)))
                ;
            siteFrom.delete();
        }
    }

    private void copyProfessorships(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) {
        for (; !executionCourseFrom.getProfessorships().isEmpty();) {
            final Professorship professorship = executionCourseFrom.getProfessorships().get(0);
            final Professorship otherProfessorship = findProfessorShip(executionCourseTo, professorship
                    .getTeacher());
            if (otherProfessorship == null) {
                professorship.setExecutionCourse(executionCourseTo);
            } else {
                for (; !professorship.getAssociatedSummaries().isEmpty(); otherProfessorship
                        .addAssociatedSummaries(professorship.getAssociatedSummaries().get(0)))
                    ;
                for (; !professorship.getAssociatedShiftProfessorship().isEmpty(); otherProfessorship
                        .addAssociatedShiftProfessorship(professorship.getAssociatedShiftProfessorship()
                                .get(0)))
                    ;
                for (; !professorship.getSupportLessons().isEmpty(); otherProfessorship
                        .addSupportLessons(professorship.getSupportLessons().get(0)))
                    ;
                professorship.delete();
            }
        }
    }

    private Professorship findProfessorShip(final ExecutionCourse executionCourseTo,
            final Teacher teacher) {
        for (final Professorship professorship : executionCourseTo.getProfessorships()) {
            if (professorship.getTeacher() == teacher) {
                return professorship;
            }
        }
        return null;
    }

    private void copyForuns(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) {

        while (!executionCourseFrom.getForuns().isEmpty()) {
            ExecutionCourseForum sourceForum = executionCourseFrom.getForuns().get(0);
            String forumName = sourceForum.getName();

            if (!executionCourseTo.hasForumWithName(forumName)) {
                sourceForum.setExecutionCourse(executionCourseTo);
            } else {
                ExecutionCourseForum targetForum = executionCourseTo.getForumByName(forumName);
                copyForumSubscriptions(sourceForum, targetForum);
                copyThreads(sourceForum, targetForum);
                executionCourseFrom.removeForuns(sourceForum);
                sourceForum.delete();
            }

        }
    }

    private void copyForumSubscriptions(ExecutionCourseForum sourceForum,
            ExecutionCourseForum targetForum) {

        while (!sourceForum.getForumSubscriptions().isEmpty()) {
            ForumSubscription sourceForumSubscription = sourceForum.getForumSubscriptions().get(0);
            Person sourceForumSubscriber = sourceForumSubscription.getPerson();
            ForumSubscription targetForumSubscription = targetForum
                    .getPersonSubscription(sourceForumSubscriber);

            if (targetForumSubscription == null) {
                sourceForumSubscription.setForum(targetForum);
            } else {
                if (sourceForumSubscription.getReceivePostsByEmail() == true) {
                    targetForumSubscription.setReceivePostsByEmail(true);
                }

                if (sourceForumSubscription.getFavorite() == true) {
                    targetForumSubscription.setFavorite(true);
                }
                sourceForum.removeForumSubscriptions(sourceForumSubscription);
                sourceForumSubscription.delete();
            }

        }
    }

    private void copyThreads(ExecutionCourseForum sourceForum, ExecutionCourseForum targetForum) {

        while (!sourceForum.getConversationThreads().isEmpty()) {
            ConversationThread sourceConversationThread = sourceForum.getConversationThreads().get(0);

            if (!targetForum.hasConversationThreadWithSubject(sourceConversationThread.getSubject())) {
                sourceConversationThread.setForum(targetForum);
            } else {
                ConversationThread targetConversationThread = targetForum
                        .getConversationThreadBySubject(sourceConversationThread.getSubject());
                targetConversationThread.getConversationMessages().addAll(
                        sourceConversationThread.getConversationMessages());
                sourceConversationThread.getConversationMessages().clear();
                sourceForum.removeConversationThreads(sourceConversationThread);
                sourceConversationThread.delete();
            }
        }
    }

}
