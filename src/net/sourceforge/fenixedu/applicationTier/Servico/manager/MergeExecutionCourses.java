/*
 * Created on 29/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.FinalEvaluation;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.functionalities.GroupAvailability;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesCourse;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseAnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseForum;
import net.sourceforge.fenixedu.domain.messaging.ForumSubscription;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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

    public void run(Integer executionCourseDestinationId, Integer executionCourseSourceId) throws FenixServiceException {

	if (executionCourseDestinationId.equals(executionCourseSourceId)) {
	    throw new SourceAndDestinationAreTheSameException();
	}

	final ExecutionCourse executionCourseFrom = rootDomainObject.readExecutionCourseByOID(executionCourseSourceId);
	if (executionCourseFrom == null) {
	    throw new InvalidArgumentsServiceException();
	}

	final ExecutionCourse executionCourseTo = rootDomainObject.readExecutionCourseByOID(executionCourseDestinationId);
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

	executionCourseTo.getAssociatedCurricularCourses().addAll(executionCourseFrom.getAssociatedCurricularCourses());

	executionCourseTo.copyLessonPlanningsFrom(executionCourseFrom);

	executionCourseFrom.delete();
    }

    private void copyInquiries(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
	for (final InquiriesCourse inquiriesCourse : executionCourseFrom.getAssociatedInquiriesCoursesSet()) {
	    inquiriesCourse.setExecutionCourse(executionCourseTo);
	}
	for (final InquiriesRegistry inquiriesRegistry : executionCourseFrom.getAssociatedInquiriesRegistries()) {
	    inquiriesRegistry.setExecutionCourse(executionCourseTo);
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

	for (final Announcement announcement : executionCourseAnnouncementBoardFrom.getAnnouncements()) {
	    executionCourseAnnouncementBoardTo.addAnnouncements(announcement);
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

	boolean distributedTestAuthorization = false;

	List<Metadata> metadatas = executionCourseFrom.getMetadatas();
	List<DistributedTest> distributedTests = TestScope.readDistributedTestsByTestScope(executionCourseFrom.getClass(),
		executionCourseFrom.getIdInternal());
	distributedTestAuthorization = (metadatas == null || metadatas.isEmpty())
		&& (distributedTests == null || distributedTests.isEmpty());

	return executionCourseTo != null && executionCourseFrom != null
		&& executionCourseFrom.getExecutionPeriod().equals(executionCourseTo.getExecutionPeriod())
		&& executionCourseFrom != executionCourseTo && distributedTestAuthorization;
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
	    final Evaluation evaluation = executionCourseFrom.getAssociatedEvaluations().get(0);
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
		.getAssociatedBibliographicReferences().add(executionCourseFrom.getAssociatedBibliographicReferences().get(0)))
	    ;
    }

    private void copyShifts(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
	final List<Shift> associatedShifts = new ArrayList<Shift>(executionCourseFrom.getAssociatedShifts());
	for (final Shift shift : associatedShifts) {
	    List<CourseLoad> courseLoadsFrom = new ArrayList<CourseLoad>(shift.getCourseLoads());
	    for (Iterator<CourseLoad> iter = courseLoadsFrom.iterator(); iter.hasNext();) {
		CourseLoad courseLoadFrom = iter.next();
		CourseLoad courseLoadTo = executionCourseTo.getCourseLoadByShiftType(courseLoadFrom.getType());
		if (courseLoadTo == null) {
		    courseLoadTo = new CourseLoad(executionCourseTo, courseLoadFrom.getType(), courseLoadFrom.getUnitQuantity(),
			    courseLoadFrom.getTotalQuantity());
		}
		iter.remove();
		shift.removeCourseLoads(courseLoadFrom);
		shift.addCourseLoads(courseLoadTo);
	    }
	}
    }

    private void copyLessonsInstances(ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) {
	final List<LessonInstance> associatedLessons = new ArrayList<LessonInstance>(executionCourseFrom
		.getAssociatedLessonInstances());
	for (final LessonInstance lessonInstance : associatedLessons) {
	    CourseLoad courseLoadFrom = lessonInstance.getCourseLoad();
	    CourseLoad courseLoadTo = executionCourseTo.getCourseLoadByShiftType(courseLoadFrom.getType());
	    if (courseLoadTo == null) {
		courseLoadTo = new CourseLoad(executionCourseTo, courseLoadFrom.getType(), courseLoadFrom.getUnitQuantity(),
			courseLoadFrom.getTotalQuantity());
	    }
	    lessonInstance.setCourseLoad(courseLoadTo);
	}
    }

    private void copyAttends(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo)
	    throws FenixServiceException {
	while (!executionCourseFrom.getAttends().isEmpty()) {
	    final Attends attends = executionCourseFrom.getAttends().get(0);
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
		for (; !attends.getAssociatedMarks().isEmpty(); otherAttends.addAssociatedMarks(attends.getAssociatedMarks().get(
			0)))
		    ;
		for (; !attends.getStudentGroups().isEmpty(); otherAttends.addStudentGroups(attends.getStudentGroups().get(0)))
		    ;
		attends.delete();
	    }
	}

	final Iterator<Attends> associatedAttendsFromDestination = executionCourseTo.getAttendsIterator();
	final Map<String, Attends> alreadyAttendingDestination = new HashMap<String, Attends>();
	while (associatedAttendsFromDestination.hasNext()) {
	    Attends attend = associatedAttendsFromDestination.next();
	    alreadyAttendingDestination.put(attend.getRegistration().getNumber().toString(), attend);
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
	    copyContents(executionCourseTo, siteTo, siteFrom.getOrderedDirectChildren());
	    siteFrom.delete();
	}
    }

    private void copyContents(final ExecutionCourse executionCourseTo, final Container parentTo, final Collection<Node> nodes) {
	final Set<Content> transferedContents = new HashSet<Content>();

	for (final Node node : nodes) {
	    final Content content = node.getChild();
	    final ExplicitOrderNode explicitOrderNode = new ExplicitOrderNode(parentTo, content);
	    if (node instanceof ExplicitOrderNode) {
		explicitOrderNode.setNodeOrder(((ExplicitOrderNode) node).getNodeOrder());
	    }
	    explicitOrderNode.setVisible(node.getVisible());
	    node.delete();

	    changeGroups(executionCourseTo, content, transferedContents);
	}

	if (transferedContents.size() > 0) {
	    final Collection<String> tos = createListOfEmailAddresses(executionCourseTo);
	    final StringBuilder message = new StringBuilder();
	    message.append("Viva, \n\n");
	    message.append("Devido à junção da disciplina acima referida entre vários cursos, não foi possível manter os ");
	    message.append("previlégios de acesso aos seguintes conteúdos: ");
	    for (final Content content : transferedContents) {
		message.append("\n\t");
		message.append(content.getName());
	    }
	    message.append("\n\nAgradecemos que actualize os previlégios ");
	    message.append("dos conteúdos logo que possível.\n\n");
	    message.append("Os melhores cumprimentos,\n\nO sistema Fénix.");

	    new Email("Sistema Fénix", "no-reply@ist.utl.pt", new String[] {}, tos, Collections.EMPTY_LIST,
		    Collections.EMPTY_LIST, "Junção de disciplinas: " + executionCourseTo.getNome(), message.toString());
	}
    }

    private void changeGroups(final ExecutionCourse executionCourseTo, final Content content,
	    final Set<Content> transferedContents) {
	if (content.getAvailabilityPolicy() != null) {
	    content.getAvailabilityPolicy().delete();
	    final Group group = createExecutionCourseResponsibleTeachersGroup(executionCourseTo);
	    if (group != null) {
		new GroupAvailability(content, group);
	    }
	    transferedContents.add(content);
	}
	if (content.isContainer()) {
	    final Container container = (Container) content;
	    for (final Node node : container.getOrderedDirectChildren()) {
		changeGroups(executionCourseTo, node.getChild(), transferedContents);
	    }
	}
    }

    private Collection<String> createListOfEmailAddresses(final ExecutionCourse executionCourseTo) {
	final Collection<String> emails = new ArrayList<String>();
	for (final Professorship professorship : executionCourseTo.getProfessorshipsSet()) {
	    emails.add(professorship.getTeacher().getPerson().getEmail());
	}
	return emails;
    }

    private Group createExecutionCourseResponsibleTeachersGroup(final ExecutionCourse executionCourseTo) {
	final Set<IGroup> groups = new HashSet<IGroup>();
	for (final Professorship professorship : executionCourseTo.getProfessorshipsSet()) {
	    if (professorship.isResponsibleFor()) {
		groups.add(new PersonGroup(professorship.getTeacher().getPerson()));
	    }
	}
	return groups.isEmpty() ? null : new GroupUnion(groups);
    }

    private void copyProfessorships(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo) {
	for (; !executionCourseFrom.getProfessorships().isEmpty();) {
	    final Professorship professorship = executionCourseFrom.getProfessorships().get(0);
	    final Professorship otherProfessorship = findProfessorShip(executionCourseTo, professorship.getTeacher());
	    if (otherProfessorship == null) {
		professorship.setExecutionCourse(executionCourseTo);
	    } else {
		for (; !professorship.getAssociatedSummaries().isEmpty(); otherProfessorship.addAssociatedSummaries(professorship
			.getAssociatedSummaries().get(0)))
		    ;
		for (; !professorship.getAssociatedShiftProfessorship().isEmpty(); otherProfessorship
			.addAssociatedShiftProfessorship(professorship.getAssociatedShiftProfessorship().get(0)))
		    ;
		for (; !professorship.getSupportLessons().isEmpty(); otherProfessorship.addSupportLessons(professorship
			.getSupportLessons().get(0)))
		    ;
		for (; !professorship.getDegreeTeachingServices().isEmpty(); otherProfessorship
			.addDegreeTeachingServices(professorship.getDegreeTeachingServices().get(0)))
		    ;
		for (; !professorship.getTeacherMasterDegreeServices().isEmpty(); otherProfessorship
			.addTeacherMasterDegreeServices(professorship.getTeacherMasterDegreeServices().get(0)))
		    ;
		professorship.delete();
	    }
	}
    }

    private Professorship findProfessorShip(final ExecutionCourse executionCourseTo, final Teacher teacher) {
	for (final Professorship professorship : executionCourseTo.getProfessorships()) {
	    if (professorship.getTeacher() == teacher) {
		return professorship;
	    }
	}
	return null;
    }

    private void copyForuns(final ExecutionCourse executionCourseFrom, final ExecutionCourse executionCourseTo)
	    throws FenixServiceException {

	if (!executionCourseTo.hasSite()) {
	    throw new FenixServiceException("Unable to copy foruns, destination doesn't have site");
	}

	while (!executionCourseFrom.getForuns().isEmpty()) {
	    ExecutionCourseForum sourceForum = executionCourseFrom.getForuns().get(0);
	    MultiLanguageString forumName = sourceForum.getName();

	    ExecutionCourseForum targetForum = executionCourseTo.getForumByName(forumName);
	    if (targetForum == null) {
		Node childNode = executionCourseFrom.getSite().getChildNode(sourceForum);
		childNode.setParent(executionCourseTo.getSite());
	    } else {
		copyForumSubscriptions(sourceForum, targetForum);
		copyThreads(sourceForum, targetForum);
		executionCourseFrom.getSite().removeForum(sourceForum);
		sourceForum.delete();
	    }

	}
    }

    private void copyForumSubscriptions(ExecutionCourseForum sourceForum, ExecutionCourseForum targetForum) {

	while (!sourceForum.getForumSubscriptions().isEmpty()) {
	    ForumSubscription sourceForumSubscription = sourceForum.getForumSubscriptions().get(0);
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

	while (!sourceForum.getConversationThreads().isEmpty()) {
	    ConversationThread sourceConversationThread = sourceForum.getConversationThreads().get(0);

	    if (!targetForum.hasConversationThreadWithSubject(sourceConversationThread.getTitle())) {
		sourceConversationThread.setForum(targetForum);
	    } else {
		ConversationThread targetConversionThread = targetForum.getConversationThreadBySubject(sourceConversationThread
			.getTitle());
		for (Node child : sourceConversationThread.getChildren()) {
		    child.setParent(targetConversionThread);
		}
		sourceForum.removeConversationThreads(sourceConversationThread);
		sourceConversationThread.delete();
	    }
	}
    }

}
