/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 29/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseLog;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.FinalEvaluation;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.cms.CmsContent;
import net.sourceforge.fenixedu.domain.inquiries.InquiryCourseAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryRegistry;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.ConversationMessage;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseAnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseForum;
import net.sourceforge.fenixedu.domain.messaging.ForumSubscription;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiriesCourse;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UnionGroup;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt"> João Mota </a> 29/Nov/2003
 * 
 */
public class MergeExecutionCourses {

    public class SourceAndDestinationAreTheSameException extends FenixServiceException {
        private static final long serialVersionUID = 3761968254943244338L;
    }

    public class DuplicateShiftNameException extends FenixServiceException {
        private static final long serialVersionUID = 3761968254943244338L;
    }

    protected void run(String executionCourseDestinationId, String executionCourseSourceId) throws FenixServiceException {

        ServiceMonitoring.logService(this.getClass(), executionCourseDestinationId, executionCourseSourceId);

        if (executionCourseDestinationId.equals(executionCourseSourceId)) {
            throw new SourceAndDestinationAreTheSameException();
        }

        final ExecutionCourse executionCourseFrom = FenixFramework.getDomainObject(executionCourseSourceId);

        if (executionCourseFrom == null) {
            throw new InvalidArgumentsServiceException();
        }

        final ExecutionCourse executionCourseTo = FenixFramework.getDomainObject(executionCourseDestinationId);

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
        copyLessonsInstances(executionCourseFrom, executionCourseTo);
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
        copyBoard(executionCourseFrom, executionCourseTo);
        copyInquiries(executionCourseFrom, executionCourseTo);
        copyDistributedTestStuff(executionCourseFrom, executionCourseTo);
        copyVigilantGroups(executionCourseFrom, executionCourseTo);
        copyExecutionCourseLogs(executionCourseFrom, executionCourseTo);
        executionCourseTo.getAssociatedCurricularCourses().addAll(executionCourseFrom.getAssociatedCurricularCourses());

        executionCourseTo.copyLessonPlanningsFrom(executionCourseFrom);

        executionCourseFrom.delete();
    }

    private void copyVigilantGroups(ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) {
        if (!executionCourseTo.hasVigilantGroup()) {
            executionCourseTo.setVigilantGroup(executionCourseFrom.getVigilantGroup());
        }
    }

    private void copyDistributedTestStuff(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
        for (final Metadata metadata : executionCourseFrom.getMetadatasSet()) {
            metadata.setExecutionCourse(executionCourseTo);
        }
        List<DistributedTest> distributedTests = TestScope.readDistributedTestsByTestScope(executionCourseFrom);
        for (final DistributedTest distributedTest : distributedTests) {
            final TestScope testScope = distributedTest.getTestScope();
            testScope.setExecutionCourse(executionCourseTo);
        }
    }

    private void copyInquiries(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
        for (final InquiriesCourse inquiriesCourse : executionCourseFrom.getAssociatedInquiriesCoursesSet()) {
            inquiriesCourse.setExecutionCourse(executionCourseTo);
        }
        for (final InquiriesRegistry inquiriesRegistry : executionCourseFrom.getAssociatedInquiriesRegistries()) {
            inquiriesRegistry.setExecutionCourse(executionCourseTo);
        }
        //new QUC model
        for (final StudentInquiryRegistry studentInquiryRegistry : executionCourseFrom.getStudentsInquiryRegistriesSet()) {
            studentInquiryRegistry.setExecutionCourse(executionCourseTo);
        }
        for (final InquiryResult inquiryResult : executionCourseFrom.getInquiryResultsSet()) {
            inquiryResult.setExecutionCourse(executionCourseTo);
        }
        for (final InquiryCourseAnswer inquiryCourseAnswer : executionCourseFrom.getInquiryCourseAnswersSet()) {
            inquiryCourseAnswer.setExecutionCourse(executionCourseTo);
        }
    }

    private void copyBoard(ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) {
        final ExecutionCourseAnnouncementBoard executionCourseAnnouncementBoardFrom = executionCourseFrom.getBoard();
        final ExecutionCourseAnnouncementBoard executionCourseAnnouncementBoardTo = executionCourseTo.getBoard();

        for (final Iterator<Person> iterator = executionCourseAnnouncementBoardFrom.getBookmarkOwnerSet().iterator(); iterator
                .hasNext(); iterator.remove()) {
            final Person bookmarkOwner = iterator.next();
            executionCourseAnnouncementBoardTo.addBookmarkOwner(bookmarkOwner);
        }

        for (final Announcement announcement : executionCourseAnnouncementBoardFrom.getAnnouncementSet()) {
            executionCourseAnnouncementBoardTo.addAnnouncement(announcement);
        }

        executionCourseAnnouncementBoardFrom.delete();
    }

    private boolean haveShiftsWithSameName(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
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

    private boolean isMergeAllowed(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
        return executionCourseTo != null && executionCourseFrom != null
                && executionCourseFrom.getExecutionPeriod().equals(executionCourseTo.getExecutionPeriod())
                && executionCourseFrom != executionCourseTo;
    }

    private void copySummaries(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
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

    private void removeEvaluations(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo)
            throws FenixServiceException {
        while (!executionCourseFrom.getAssociatedEvaluations().isEmpty()) {
            final Evaluation evaluation = executionCourseFrom.getAssociatedEvaluations().iterator().next();
            if (evaluation instanceof FinalEvaluation) {
                final FinalEvaluation finalEvaluationFrom = (FinalEvaluation) evaluation;
                if (finalEvaluationFrom.hasAnyMarks()) {
                    throw new FenixServiceException("Cannot merge execution courses: marks exist for final evaluation.");
                } else {
                    finalEvaluationFrom.delete();
                }
            } else {
                executionCourseTo.getAssociatedEvaluations().add(evaluation);
                executionCourseFrom.getAssociatedEvaluations().remove(evaluation);
            }
        }
    }

    private void copyBibliographicReference(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
        for (; !executionCourseFrom.getAssociatedBibliographicReferences().isEmpty(); executionCourseTo
                .getAssociatedBibliographicReferences().add(
                        executionCourseFrom.getAssociatedBibliographicReferences().iterator().next())) {
            ;
        }
    }

    private void copyShifts(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
        final List<Shift> associatedShifts = new ArrayList<Shift>(executionCourseFrom.getAssociatedShifts());
        for (final Shift shift : associatedShifts) {
            List<CourseLoad> courseLoadsFrom = new ArrayList<CourseLoad>(shift.getCourseLoads());
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

    private void copyLessonsInstances(ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) {
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

    private void copyAttends(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo)
            throws FenixServiceException {
        for (Attends attends : executionCourseFrom.getAttendsSet()) {
            final Attends otherAttends = executionCourseTo.getAttendsByStudent(attends.getRegistration());
            if (otherAttends == null) {
                attends.setDisciplinaExecucao(executionCourseTo);
            } else {
                if (attends.hasEnrolment() && !otherAttends.hasEnrolment()) {
                    otherAttends.setEnrolment(attends.getEnrolment());
                } else if (otherAttends.hasEnrolment() && !attends.hasEnrolment()) {
                    // do nothing.
                } else if (otherAttends.hasEnrolment() && attends.hasEnrolment()) {
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

        final Iterator<Attends> associatedAttendsFromDestination = executionCourseTo.getAttends().iterator();
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
        associatedAttendsFromSource.addAll(executionCourseFrom.getAttends());
        for (final Attends attend : associatedAttendsFromSource) {
            if (!alreadyAttendingDestination.containsKey(attend.getRegistration().getNumber().toString())) {
                attend.setDisciplinaExecucao(executionCourseTo);
            }
        }
    }

    private void copySite(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
        final Site siteFrom = executionCourseFrom.getSite();
        final Site siteTo = executionCourseTo.getSite();

        if (siteFrom != null) {
            copyContents(executionCourseTo, siteTo, siteFrom);
            siteFrom.delete();
        }
    }

    private void copyContents(final ExecutionCourse executionCourseTo, final Site siteTo, Site siteFrom) {
        final Set<CmsContent> transferedContents = new HashSet<>();

        for (final Section section : siteFrom.getAssociatedSectionSet()) {
            section.setSite(siteTo);
            changeGroups(executionCourseTo, section, transferedContents);
        }

        if (transferedContents.size() > 0) {
            final Set<String> bccs = createListOfEmailAddresses(executionCourseTo);
            final StringBuilder message = new StringBuilder();
            message.append(BundleUtil.getString(Bundle.GLOBAL, "mergeExecutionCourses.email.body"));

            for (final CmsContent content : transferedContents) {
                message.append("\n\t");
                message.append(content.getName());
            }

            message.append(BundleUtil.getString(Bundle.GLOBAL, "mergeExecutionCourses.email.greetings"));
            SystemSender systemSender = Bennu.getInstance().getSystemSender();
            new Message(systemSender, systemSender.getConcreteReplyTos(), Collections.EMPTY_LIST, BundleUtil.getString(
                    Bundle.GLOBAL, "mergeExecutionCourses.email.subject", new String[] { executionCourseTo.getNome() }),
                    message.toString(), bccs);
        }
    }

    private void changeGroups(final ExecutionCourse executionCourseTo, final CmsContent content,
            final Set<CmsContent> transferedContents) {
        content.setPermittedGroup(createExecutionCourseResponsibleTeachersGroup(executionCourseTo));
        transferedContents.add(content);
        if (content instanceof Section) {
            final Section container = (Section) content;
            for (final CmsContent node : container.getChildSet()) {
                changeGroups(executionCourseTo, node, transferedContents);
            }
        }

        for (FileContent file : content.getFileContentSet()) {
            if (file.isPrivate()) {
                file.setPermittedGroup(createExecutionCourseResponsibleTeachersGroup(executionCourseTo));
            }
        }
    }

    private Set<String> createListOfEmailAddresses(final ExecutionCourse executionCourseTo) {
        final Set<String> emails = new HashSet<String>();
        for (final Professorship professorship : executionCourseTo.getProfessorshipsSet()) {
            emails.add(professorship.getPerson().getEmail());
        }
        return emails;
    }

    private Group createExecutionCourseResponsibleTeachersGroup(final ExecutionCourse executionCourseTo) {
        final Set<Group> groups = new HashSet<Group>();
        for (final Professorship professorship : executionCourseTo.getProfessorshipsSet()) {
            if (professorship.isResponsibleFor()) {
                groups.add(UserGroup.of(professorship.getPerson().getUser()));
            }
        }
        return groups.isEmpty() ? null : UnionGroup.of(groups);
    }

    private void copyProfessorships(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
        for (; !executionCourseFrom.getProfessorships().isEmpty();) {
            final Professorship professorship = executionCourseFrom.getProfessorships().iterator().next();
            final Professorship otherProfessorship = findProfessorShip(executionCourseTo, professorship.getPerson());
            if (otherProfessorship == null) {
                professorship.setExecutionCourse(executionCourseTo);
            } else {
                for (; !professorship.getAssociatedSummaries().isEmpty(); otherProfessorship.addAssociatedSummaries(professorship
                        .getAssociatedSummaries().iterator().next())) {
                    ;
                }
                for (; !professorship.getAssociatedShiftProfessorship().isEmpty(); otherProfessorship
                        .addAssociatedShiftProfessorship(professorship.getAssociatedShiftProfessorship().iterator().next())) {
                    ;
                }
                for (; !professorship.getSupportLessons().isEmpty(); otherProfessorship.addSupportLessons(professorship
                        .getSupportLessons().iterator().next())) {
                    ;
                }
                for (; !professorship.getDegreeTeachingServices().isEmpty(); otherProfessorship
                        .addDegreeTeachingServices(professorship.getDegreeTeachingServices().iterator().next())) {
                    ;
                }
                for (; !professorship.getTeacherMasterDegreeServices().isEmpty(); otherProfessorship
                        .addTeacherMasterDegreeServices(professorship.getTeacherMasterDegreeServices().iterator().next())) {
                    ;
                }
                professorship.delete();
            }
        }
    }

    private Professorship findProfessorShip(final ExecutionCourse executionCourseTo, final Person person) {
        for (final Professorship professorship : executionCourseTo.getProfessorships()) {
            if (professorship.getPerson() == person) {
                return professorship;
            }
        }
        return null;
    }

    private void copyForuns(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo)
            throws FenixServiceException {

        while (!executionCourseFrom.getForuns().isEmpty()) {
            ExecutionCourseForum sourceForum = executionCourseFrom.getForuns().iterator().next();
            MultiLanguageString forumName = sourceForum.getName();

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

    private void copyForumSubscriptions(ExecutionCourseForum sourceForum, ExecutionCourseForum targetForum) {

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

    private void copyThreads(ExecutionCourseForum sourceForum, ExecutionCourseForum targetForum) {

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

    private void copyExecutionCourseLogs(ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) {
        for (ExecutionCourseLog executionCourseLog : executionCourseFrom.getExecutionCourseLogs()) {
            executionCourseLog.setExecutionCourse(executionCourseTo);
        }

    }

    // Service Invokers migrated from Berserk

    private static final MergeExecutionCourses serviceInstance = new MergeExecutionCourses();

    @Atomic
    public static void runMergeExecutionCourses(String executionCourseDestinationId, String executionCourseSourceId)
            throws FenixServiceException {
        serviceInstance.run(executionCourseDestinationId, executionCourseSourceId);
    }

}
