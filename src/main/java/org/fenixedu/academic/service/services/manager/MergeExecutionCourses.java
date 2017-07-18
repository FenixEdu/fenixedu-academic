/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 29/Nov/2003
 * 
 */
package org.fenixedu.academic.service.services.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.CourseLoad;
import org.fenixedu.academic.domain.Evaluation;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionCourseLog;
import org.fenixedu.academic.domain.ExportGrouping;
import org.fenixedu.academic.domain.FinalEvaluation;
import org.fenixedu.academic.domain.LessonInstance;
import org.fenixedu.academic.domain.Mark;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.academic.domain.Summary;
import org.fenixedu.academic.domain.accessControl.PersistentSpecialCriteriaOverExecutionCourseGroup;
import org.fenixedu.academic.domain.accessControl.PersistentStudentGroup;
import org.fenixedu.academic.domain.accessControl.PersistentTeacherGroup;
import org.fenixedu.academic.domain.messaging.ConversationMessage;
import org.fenixedu.academic.domain.messaging.ConversationThread;
import org.fenixedu.academic.domain.messaging.ExecutionCourseForum;
import org.fenixedu.academic.domain.messaging.ForumSubscription;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.util.email.ExecutionCourseSender;
import org.fenixedu.academic.service.ServiceMonitoring;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt"> João Mota </a> 29/Nov/2003
 * 
 */
public class MergeExecutionCourses {
    public static class SourceAndDestinationAreTheSameException extends FenixServiceException {
        private static final long serialVersionUID = 3761968254943244338L;
    }

    public static class DuplicateShiftNameException extends FenixServiceException {
        private static final long serialVersionUID = 3761968254943244338L;
    }

    public static class MergeNotPossibleException extends FenixServiceException {
        private static final long serialVersionUID = 3761968254943244338L;

        public MergeNotPossibleException(Set<String> blockers) {
            super(blockers.stream().collect(Collectors.joining("; ")));
        }
    }

    @FunctionalInterface
    public static interface SubDomainMergeHandler {
        default public Set<String> mergeBlockers(ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) {
            return Collections.<String> emptySet();
        }

        public void merge(ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) throws FenixServiceException;
    }

    private static final ConcurrentLinkedQueue<SubDomainMergeHandler> handlers = new ConcurrentLinkedQueue<>();

    public static void registerMergeHandler(SubDomainMergeHandler handler) {
        handlers.add(handler);
    }

    static {
        registerMergeHandler(new SubDomainMergeHandler() {
            @Override
            public Set<String> mergeBlockers(ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) {
                if (!isMergeAllowed(executionCourseFrom, executionCourseTo)) {
                    return Collections.<String> singleton("Cannot merge courses of different periods");
                }
                return Collections.<String> emptySet();
            };

            @Override
            public void merge(ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo)
                    throws FenixServiceException {
                copyShifts(executionCourseFrom, executionCourseTo);
            }
        });
        registerMergeHandler(MergeExecutionCourses::copyLessonsInstances);
        registerMergeHandler(MergeExecutionCourses::copyProfessorships);
        registerMergeHandler(MergeExecutionCourses::copyAttends);
        registerMergeHandler(MergeExecutionCourses::copyBibliographicReference);
        registerMergeHandler(MergeExecutionCourses::dropEvaluationMethods);
        registerMergeHandler(MergeExecutionCourses::copySummaries);
        registerMergeHandler(MergeExecutionCourses::copyGroupPropertiesExecutionCourse);
        registerMergeHandler(MergeExecutionCourses::removeEvaluations);
        registerMergeHandler(MergeExecutionCourses::copyForuns);
        registerMergeHandler(MergeExecutionCourses::copyExecutionCourseLogs);
        registerMergeHandler(MergeExecutionCourses::copyPersistentGroups);
        registerMergeHandler(MergeExecutionCourses::copySenderMessages);
        registerMergeHandler((from, to) -> to.getAssociatedCurricularCoursesSet()
                .addAll(from.getAssociatedCurricularCoursesSet()));
        registerMergeHandler((from, to) -> to.copyLessonPlanningsFrom(from));
    }

    @Atomic(mode = TxMode.WRITE)
    public static void merge(ExecutionCourse executionCourseTo, ExecutionCourse executionCourseFrom) throws FenixServiceException {
        if (executionCourseFrom == null) {
            throw new InvalidArgumentsServiceException();
        }

        if (executionCourseTo == null) {
            throw new InvalidArgumentsServiceException();
        }

        ServiceMonitoring.logService(MergeExecutionCourses.class, executionCourseTo.getExternalId(),
                executionCourseFrom.getExternalId());

        if (executionCourseTo.equals(executionCourseFrom)) {
            throw new SourceAndDestinationAreTheSameException();
        }

        if (haveShiftsWithSameName(executionCourseFrom, executionCourseTo)) {
            throw new DuplicateShiftNameException();
        }

        for (SubDomainMergeHandler handler : handlers) {
            Set<String> blockers = handler.mergeBlockers(executionCourseFrom, executionCourseTo);
            if (blockers.isEmpty()) {
                handler.merge(executionCourseFrom, executionCourseTo);
            } else {
                throw new MergeNotPossibleException(blockers);
            }
        }

        executionCourseFrom.delete();
    }

    private static boolean haveShiftsWithSameName(final ExecutionCourse executionCourseFrom,
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

    private static boolean isMergeAllowed(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
        return executionCourseTo != null && executionCourseFrom != null
                && executionCourseFrom.getExecutionPeriod().equals(executionCourseTo.getExecutionPeriod())
                && executionCourseFrom != executionCourseTo;
    }

    private static void copySummaries(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
        final List<Summary> associatedSummaries = new ArrayList<Summary>();
        associatedSummaries.addAll(executionCourseFrom.getAssociatedSummariesSet());
        for (final Summary summary : associatedSummaries) {
            summary.setExecutionCourse(executionCourseTo);
        }
    }

    private static void copyGroupPropertiesExecutionCourse(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) {
        final List<ExportGrouping> associatedGroupPropertiesExecutionCourse = new ArrayList<ExportGrouping>();
        associatedGroupPropertiesExecutionCourse.addAll(executionCourseFrom.getExportGroupingsSet());

        for (final ExportGrouping groupPropertiesExecutionCourse : associatedGroupPropertiesExecutionCourse) {
            if (executionCourseTo.hasGrouping(groupPropertiesExecutionCourse.getGrouping())) {
                groupPropertiesExecutionCourse.delete();
            } else {
                groupPropertiesExecutionCourse.setExecutionCourse(executionCourseTo);
            }
        }
    }

    private static void removeEvaluations(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo)
            throws FenixServiceException {
        while (!executionCourseFrom.getAssociatedEvaluationsSet().isEmpty()) {
            final Evaluation evaluation = executionCourseFrom.getAssociatedEvaluationsSet().iterator().next();
            if (evaluation instanceof FinalEvaluation) {
                final FinalEvaluation finalEvaluationFrom = (FinalEvaluation) evaluation;
                if (!finalEvaluationFrom.getMarksSet().isEmpty()) {
                    throw new FenixServiceException("error.merge.execution.course.final.evaluation.exists");
                } else {
                    finalEvaluationFrom.delete();
                }
            } else {
                executionCourseTo.getAssociatedEvaluationsSet().add(evaluation);
                executionCourseFrom.getAssociatedEvaluationsSet().remove(evaluation);
            }
        }
    }

    private static void copyBibliographicReference(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) {
        for (; !executionCourseFrom.getAssociatedBibliographicReferencesSet().isEmpty(); executionCourseTo
                .getAssociatedBibliographicReferencesSet().add(
                        executionCourseFrom.getAssociatedBibliographicReferencesSet().iterator().next())) {
            ;
        }
    }

    private static void copyShifts(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
        final List<Shift> associatedShifts = new ArrayList<Shift>(executionCourseFrom.getAssociatedShifts());
        for (final Shift shift : associatedShifts) {
            List<CourseLoad> courseLoadsFrom = new ArrayList<CourseLoad>(shift.getCourseLoadsSet());
            for (Iterator<CourseLoad> iter = courseLoadsFrom.iterator(); iter.hasNext();) {
                CourseLoad courseLoadFrom = iter.next();
                CourseLoad courseLoadTo = executionCourseTo.getCourseLoadByShiftType(courseLoadFrom.getType());
                if (courseLoadTo == null) {
                    courseLoadTo =
                            new CourseLoad(executionCourseTo, courseLoadFrom.getType(), courseLoadFrom.getUnitQuantity(),
                                    courseLoadFrom.getTotalQuantity());
                }
                iter.remove();
                shift.removeCourseLoads(courseLoadFrom);
                shift.addCourseLoads(courseLoadTo);
            }
        }
    }

    private static void copyLessonsInstances(ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) {
        final List<LessonInstance> associatedLessons =
                new ArrayList<LessonInstance>(executionCourseFrom.getAssociatedLessonInstances());
        for (final LessonInstance lessonInstance : associatedLessons) {
            CourseLoad courseLoadFrom = lessonInstance.getCourseLoad();
            CourseLoad courseLoadTo = executionCourseTo.getCourseLoadByShiftType(courseLoadFrom.getType());
            if (courseLoadTo == null) {
                courseLoadTo =
                        new CourseLoad(executionCourseTo, courseLoadFrom.getType(), courseLoadFrom.getUnitQuantity(),
                                courseLoadFrom.getTotalQuantity());
            }
            lessonInstance.setCourseLoad(courseLoadTo);
        }
    }

    private static void copyAttends(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo)
            throws FenixServiceException {
        for (Attends attends : executionCourseFrom.getAttendsSet()) {
            final Attends otherAttends = executionCourseTo.getAttendsByStudent(attends.getRegistration());
            if (otherAttends == null) {
                attends.setDisciplinaExecucao(executionCourseTo);
            } else {
                if (attends.getEnrolment() != null && otherAttends.getEnrolment() == null) {
                    otherAttends.setEnrolment(attends.getEnrolment());
                } else if (otherAttends.getEnrolment() != null && attends.getEnrolment() == null) {
                    // do nothing.
                } else if (otherAttends.getEnrolment() != null && attends.getEnrolment() != null) {
                    throw new FenixServiceException("Unable to merge execution courses. Registration "
                            + attends.getRegistration().getNumber() + " has an enrolment in both.");
                }
                for (Mark mark : attends.getAssociatedMarksSet()) {
                    otherAttends.addAssociatedMarks(mark);
                }
                for (StudentGroup group : attends.getAllStudentGroups()) {
                    otherAttends.addStudentGroups(group);
                }
                attends.delete();
            }
        }

        final Iterator<Attends> associatedAttendsFromDestination = executionCourseTo.getAttendsSet().iterator();
        final Map<String, Attends> alreadyAttendingDestination = new HashMap<String, Attends>();
        while (associatedAttendsFromDestination.hasNext()) {
            Attends attend = associatedAttendsFromDestination.next();
            Registration registration = attend.getRegistration();
            if (registration == null) {
                // !!! Yup it's true this actually happens!!!
                attend.delete();
            } else {
                Integer number = registration.getNumber();
                alreadyAttendingDestination.put(number.toString(), attend);
            }
        }
        final List<Attends> associatedAttendsFromSource = new ArrayList<Attends>();
        associatedAttendsFromSource.addAll(executionCourseFrom.getAttendsSet());
        for (final Attends attend : associatedAttendsFromSource) {
            if (!alreadyAttendingDestination.containsKey(attend.getRegistration().getNumber().toString())) {
                attend.setDisciplinaExecucao(executionCourseTo);
            }
        }
    }

    private static void copyProfessorships(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
        for (Professorship professorship : executionCourseFrom.getProfessorshipsSet()) {
            Professorship otherProfessorship = findProfessorShip(executionCourseTo, professorship.getPerson());
            if (otherProfessorship == null) {
                otherProfessorship =
                        Professorship.create(professorship.getResponsibleFor(), executionCourseTo, professorship.getPerson());
            }
            for (; !professorship.getAssociatedSummariesSet().isEmpty(); otherProfessorship.addAssociatedSummaries(professorship
                    .getAssociatedSummariesSet().iterator().next())) {
                ;
            }
            for (; !professorship.getAssociatedShiftProfessorshipSet().isEmpty(); otherProfessorship
                    .addAssociatedShiftProfessorship(professorship.getAssociatedShiftProfessorshipSet().iterator().next())) {
                ;
            }
        }
    }

    private static Professorship findProfessorShip(final ExecutionCourse executionCourseTo, final Person person) {
        for (final Professorship professorship : executionCourseTo.getProfessorshipsSet()) {
            if (professorship.getPerson() == person) {
                return professorship;
            }
        }
        return null;
    }

    private static void copyForuns(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo)
            throws FenixServiceException {

        while (!executionCourseFrom.getForuns().isEmpty()) {
            ExecutionCourseForum sourceForum = executionCourseFrom.getForuns().iterator().next();
            LocalizedString forumName = sourceForum.getName();

            ExecutionCourseForum targetForum = executionCourseTo.getForumByName(forumName);
            if (targetForum == null) {
                sourceForum.setExecutionCourse(executionCourseTo);
            } else {
                copyForumSubscriptions(sourceForum, targetForum);
                copyThreads(sourceForum, targetForum);
                executionCourseFrom.removeForum(sourceForum);
                sourceForum.delete();
            }

        }
    }

    private static void copyForumSubscriptions(ExecutionCourseForum sourceForum, ExecutionCourseForum targetForum) {

        while (!sourceForum.getForumSubscriptionsSet().isEmpty()) {
            ForumSubscription sourceForumSubscription = sourceForum.getForumSubscriptionsSet().iterator().next();
            Person sourceForumSubscriber = sourceForumSubscription.getPerson();
            ForumSubscription targetForumSubscription = targetForum.getPersonSubscription(sourceForumSubscriber);

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

    private static void copyThreads(ExecutionCourseForum sourceForum, ExecutionCourseForum targetForum) {

        while (!sourceForum.getConversationThreadSet().isEmpty()) {
            ConversationThread sourceConversationThread = sourceForum.getConversationThreadSet().iterator().next();

            if (!targetForum.hasConversationThreadWithSubject(sourceConversationThread.getTitle())) {
                sourceConversationThread.setForum(targetForum);
            } else {
                ConversationThread targetConversionThread =
                        targetForum.getConversationThreadBySubject(sourceConversationThread.getTitle());
                for (ConversationMessage message : sourceConversationThread.getMessageSet()) {
                    message.setConversationThread(targetConversionThread);
                }
                sourceForum.removeConversationThread(sourceConversationThread);
                sourceConversationThread.delete();
            }
        }
    }

    private static void copyExecutionCourseLogs(ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) {
        for (ExecutionCourseLog executionCourseLog : executionCourseFrom.getExecutionCourseLogsSet()) {
            executionCourseLog.setExecutionCourse(executionCourseTo);
        }

    }

    private static void copyPersistentGroups(ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) {
        for (PersistentStudentGroup group : executionCourseFrom.getStudentGroupSet()) {
            group.setExecutionCourse(executionCourseTo);
        }
        for (PersistentSpecialCriteriaOverExecutionCourseGroup group : executionCourseFrom
                .getSpecialCriteriaOverExecutionCourseGroupSet()) {
            group.setExecutionCourse(executionCourseTo);
        }
        for (PersistentTeacherGroup group : executionCourseFrom.getTeacherGroupSet()) {
            group.setExecutionCourse(executionCourseTo);
        }
    }

    private static void copySenderMessages(ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) {
        if (executionCourseFrom.getSender() != null) {
            ExecutionCourseSender courseSenderTo = ExecutionCourseSender.newInstance(executionCourseTo);
            courseSenderTo.getMessagesSet().addAll(executionCourseFrom.getSender().getMessagesSet());
        }
    }

    private static void dropEvaluationMethods(ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) {
        if (executionCourseFrom.getEvaluationMethod() != null) {
            executionCourseFrom.getEvaluationMethod().delete();
        }
    }
}
