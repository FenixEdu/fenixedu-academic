package net.sourceforge.fenixedu.domain;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseAttendsBean;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleTypeGroup;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.executionCourse.SummariesSearchBean;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGlobalComment;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultType;
import net.sourceforge.fenixedu.domain.inquiries.ResultClassification;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseAnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseForum;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult;
import net.sourceforge.fenixedu.domain.oldInquiries.teacher.TeachingInquiry;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.WeeklyWorkLoad;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.tests.NewTestGroup;
import net.sourceforge.fenixedu.domain.tests.TestGroupStatus;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.ExecutionCoursePredicates;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement.ExecutionCourseManagementBean;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.ProposalState;
import net.sourceforge.fenixedu.util.domain.OrderedRelationAdapter;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.StringNormalizer;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;
import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.util.CollectionUtils;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExecutionCourse extends ExecutionCourse_Base {

    public static List<ExecutionCourse> readNotEmptyExecutionCourses() {
        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>(Bennu.getInstance().getExecutionCoursesSet());
        for (ExecutionCourse ec : result) {
            if (ec == null) {
                result.remove(ec);
            }
        }
        return result;
    }

    public static final Comparator<ExecutionCourse> EXECUTION_COURSE_EXECUTION_PERIOD_COMPARATOR =
            new Comparator<ExecutionCourse>() {

                @Override
                public int compare(ExecutionCourse o1, ExecutionCourse o2) {
                    return o1.getExecutionPeriod().compareTo(o2.getExecutionPeriod());
                }

            };

    public static final Comparator<ExecutionCourse> EXECUTION_COURSE_NAME_COMPARATOR = new Comparator<ExecutionCourse>() {

        @Override
        public int compare(ExecutionCourse o1, ExecutionCourse o2) {
            final int c = Collator.getInstance().compare(o1.getNome(), o2.getNome());
            return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
        }

    };

    public static final Comparator<ExecutionCourse> EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME =
            new Comparator<ExecutionCourse>() {

                @Override
                public int compare(ExecutionCourse o1, ExecutionCourse o2) {
                    final int cep = o1.getExecutionPeriod().compareTo(o2.getExecutionPeriod());
                    if (cep != 0) {
                        return cep;
                    }
                    final int c = Collator.getInstance().compare(o1.getNome(), o2.getNome());
                    return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
                }

            };

    public static OrderedRelationAdapter<ExecutionCourse, BibliographicReference> BIBLIOGRAPHIC_REFERENCE_ORDER_ADAPTER;
    public static final List<String> THIRD_CYCLE_AVAILABLE_INQUIRY_DEGREES = new ArrayList<String>();

    static {
        getRelationCurricularCourseExecutionCourse().addListener(new CurricularCourseExecutionCourseListener());

        BIBLIOGRAPHIC_REFERENCE_ORDER_ADAPTER =
                new OrderedRelationAdapter<ExecutionCourse, BibliographicReference>("associatedBibliographicReferences",
                        "referenceOrder");
        getRelationExecutionCourseBibliographicReference().addListener(BIBLIOGRAPHIC_REFERENCE_ORDER_ADAPTER);

        getRelationCurricularCourseExecutionCourse().addListener(new RelationAdapter<ExecutionCourse, CurricularCourse>() {

            @Override
            public void beforeAdd(final ExecutionCourse executionCourse, final CurricularCourse curricularCourse) {
                if (executionCourse != null && curricularCourse != null
                        && executionCourse.getAssociatedCurricularCoursesSet().size() == 0) {
                    ExecutionCourse previous = null;
                    for (final ExecutionCourse otherExecutionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
                        if (previous == null || otherExecutionCourse.getExecutionPeriod().isAfter(previous.getExecutionPeriod())) {
                            previous = otherExecutionCourse;
                        }
                    }
                    if (previous != null) {
                        executionCourse.setProjectTutorialCourse(previous.getProjectTutorialCourse());
                    }
                }
            }

        });

        THIRD_CYCLE_AVAILABLE_INQUIRY_DEGREES.add("deec");
        THIRD_CYCLE_AVAILABLE_INQUIRY_DEGREES.add("deic");
    }

    public ExecutionCourse(final String nome, final String sigla, final ExecutionSemester executionSemester, EntryPhase entryPhase) {
        super();

        setRootDomainObject(Bennu.getInstance());
        addAssociatedEvaluations(new FinalEvaluation());
        setAvailableGradeSubmission(Boolean.TRUE);
        setAvailableForInquiries(Boolean.TRUE);

        setNome(nome);
        setExecutionPeriod(executionSemester);
        setSigla(sigla);
        setComment("");

        createSite();

        createExecutionCourseAnnouncementBoard(nome);

        if (entryPhase == null) {
            entryPhase = EntryPhase.FIRST_PHASE;
        }
        setEntryPhase(entryPhase);
        setProjectTutorialCourse(Boolean.FALSE);
        setUnitCreditValue(null);
    }

    public void editInformation(String nome, String sigla, String comment, Boolean availableGradeSubmission, EntryPhase entryPhase) {
        setNome(nome);
        setSigla(sigla);
        setComment(comment);
        setAvailableGradeSubmission(availableGradeSubmission);
        if (entryPhase != null) {
            setEntryPhase(entryPhase);
        }
    }

    public void editCourseLoad(ShiftType type, BigDecimal unitQuantity, BigDecimal totalQuantity) {
        CourseLoad courseLoad = getCourseLoadByShiftType(type);
        if (courseLoad == null) {
            new CourseLoad(this, type, unitQuantity, totalQuantity);
        } else {
            courseLoad.edit(unitQuantity, totalQuantity);
        }
    }

    public List<Grouping> getGroupings() {
        List<Grouping> result = new ArrayList<Grouping>();
        for (final ExportGrouping exportGrouping : this.getExportGroupings()) {
            if (exportGrouping.getProposalState().getState() == ProposalState.ACEITE
                    || exportGrouping.getProposalState().getState() == ProposalState.CRIADOR) {
                result.add(exportGrouping.getGrouping());
            }
        }
        return result;
    }

    public Grouping getGroupingByName(String groupingName) {
        for (final Grouping grouping : this.getGroupings()) {
            if (grouping.getName().equals(groupingName)) {
                return grouping;
            }
        }
        return null;
    }

    public boolean existsGroupingExecutionCourse(ExportGrouping groupPropertiesExecutionCourse) {
        return getExportGroupings().contains(groupPropertiesExecutionCourse);
    }

    public boolean existsGroupingExecutionCourse() {
        return getExportGroupings().isEmpty();
    }

    public boolean hasProposals() {
        boolean result = false;
        boolean found = false;
        Collection<ExportGrouping> groupPropertiesExecutionCourseList = getExportGroupings();
        Iterator<ExportGrouping> iter = groupPropertiesExecutionCourseList.iterator();
        while (iter.hasNext() && !found) {
            ExportGrouping groupPropertiesExecutionCourseAux = iter.next();
            if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 3) {
                result = true;
                found = true;
            }
        }
        return result;
    }

    public boolean isMasterDegreeDFAOrDEAOnly() {
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            DegreeType degreeType = curricularCourse.getDegreeCurricularPlan().getDegree().getDegreeType();
            if (!degreeType.equals(DegreeType.MASTER_DEGREE) && !degreeType.equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)
                    && !degreeType.equals(DegreeType.BOLONHA_SPECIALIZATION_DEGREE)
                    && !degreeType.equals(DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA)) {
                return false;
            }
        }
        return true;
    }

    public boolean isMasterDegreeDFAOnly() {
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            DegreeType degreeType = curricularCourse.getDegreeCurricularPlan().getDegree().getDegreeType();
            if (!degreeType.equals(DegreeType.MASTER_DEGREE)
                    && !degreeType.equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)
                    && !degreeType.equals(DegreeType.BOLONHA_SPECIALIZATION_DEGREE)
                    && !THIRD_CYCLE_AVAILABLE_INQUIRY_DEGREES.contains(curricularCourse.getDegreeCurricularPlan().getDegree()
                            .getSigla().toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public boolean isBolonhaDegreeOrMasterDegreeOrIntegratedMasterDegree() {
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            DegreeType degreeType = curricularCourse.getDegreeCurricularPlan().getDegree().getDegreeType();
            if (degreeType.equals(DegreeType.BOLONHA_DEGREE) || degreeType.equals(DegreeType.BOLONHA_MASTER_DEGREE)
                    || degreeType.equals(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE)) {
                return true;
            }
            if (THIRD_CYCLE_AVAILABLE_INQUIRY_DEGREES.contains(curricularCourse.getDegreeCurricularPlan().getDegree().getSigla()
                    .toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public ExecutionCourseSite createSite() {
        return new ExecutionCourseSite(this);
    }

    public void copySectionsAndItemsFrom(final ExecutionCourse executionCourseFrom) {
        this.getSite().copySectionsAndItemsFrom(executionCourseFrom.getSite());
        String ecFrom = executionCourseFrom.getName();
        String ecFromPresentation = executionCourseFrom.getDegreePresentationString();

        ContentManagementLog.createLog(this, "resources.MessagingResources", "log.executionCourse.content.section.import",
                ecFrom, ecFromPresentation, this.getName(), this.getDegreePresentationString());
    }

    public void createEvaluationMethod(final MultiLanguageString evaluationElements) {
        if (evaluationElements == null) {
            throw new NullPointerException();
        }

        final EvaluationMethod evaluationMethod = new EvaluationMethod();
        evaluationMethod.setExecutionCourse(this);
        evaluationMethod.setEvaluationElements(evaluationElements);

    }

    public void copyEvaluationMethodFrom(ExecutionCourse executionCourseFrom) {
        if (executionCourseFrom.getEvaluationMethod() != null) {
            final EvaluationMethod evaluationMethodFrom = executionCourseFrom.getEvaluationMethod();
            final EvaluationMethod evaluationMethodTo = this.getEvaluationMethod();
            if (evaluationMethodTo == null) {
                this.createEvaluationMethod(evaluationMethodFrom.getEvaluationElements());
            } else {
                evaluationMethodTo.edit(evaluationMethodFrom.getEvaluationElements());
            }
        }
    }

    public void createBibliographicReference(final String title, final String authors, final String reference, final String year,
            final Boolean optional) {
        if (title == null || authors == null || reference == null || year == null || optional == null) {
            throw new NullPointerException();
        }

        final BibliographicReference bibliographicReference = new BibliographicReference();
        bibliographicReference.setTitle(title);
        bibliographicReference.setAuthors(authors);
        bibliographicReference.setReference(reference);
        bibliographicReference.setYear(year);
        bibliographicReference.setOptional(optional);
        bibliographicReference.setExecutionCourse(this);

        final String type;
        if (optional) {
            type =
                    BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
                            "option.bibliographicReference.optional");
        } else {
            type =
                    BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
                            "option.bibliographicReference.recommended");
        }
        CurricularManagementLog.createLog(this, "resources.MessagingResources",
                "log.executionCourse.curricular.bibliographic.created", type, title, this.getName(),
                this.getDegreePresentationString());
    }

    public List<BibliographicReference> copyBibliographicReferencesFrom(final ExecutionCourse executionCourseFrom) {
        final List<BibliographicReference> notCopiedBibliographicReferences = new ArrayList<BibliographicReference>();

        for (final BibliographicReference bibliographicReference : executionCourseFrom.getAssociatedBibliographicReferences()) {
            if (canAddBibliographicReference(bibliographicReference)) {
                this.createBibliographicReference(bibliographicReference.getTitle(), bibliographicReference.getAuthors(),
                        bibliographicReference.getReference(), bibliographicReference.getYear(),
                        bibliographicReference.getOptional());
            } else {
                notCopiedBibliographicReferences.add(bibliographicReference);
            }
        }

        return notCopiedBibliographicReferences;
    }

    private boolean canAddBibliographicReference(final BibliographicReference bibliographicReferenceToAdd) {
        for (final BibliographicReference bibliographicReference : this.getAssociatedBibliographicReferences()) {
            if (bibliographicReference.getTitle().equals(bibliographicReferenceToAdd.getTitle())) {
                return false;
            }
        }
        return true;
    }

    public CourseReport createCourseReport(String report) {
        if (report == null) {
            throw new NullPointerException();
        }

        final CourseReport courseReport = new CourseReport();
        courseReport.setReport(report);
        courseReport.setLastModificationDateDateTime(new DateTime());
        courseReport.setExecutionCourse(this);

        return courseReport;
    }

    public List<Professorship> responsibleFors() {
        final List<Professorship> res = new ArrayList<Professorship>();
        for (final Professorship professorship : this.getProfessorships()) {
            Boolean responsibleFor = professorship.getResponsibleFor();
            if (responsibleFor != null && responsibleFor.booleanValue()) {
                res.add(professorship);
            }
        }
        return res;
    }

    public Attends getAttendsByStudent(final Registration registration) {
        for (final Attends attends : getAttendsSet()) {
            if (attends.getRegistration() == registration) {
                return attends;
            }
        }
        return null;
    }

    public Attends getAttendsByStudent(final Student student) {
        for (final Attends attends : getAttends()) {
            if (attends.isFor(student)) {
                return attends;
            }
        }
        return null;
    }

    public boolean hasAttendsFor(final Student student) {
        return getAttendsByStudent(student) != null;
    }

    public List<Exam> getAssociatedExams() {
        List<Exam> associatedExams = new ArrayList<Exam>();

        for (Evaluation evaluation : this.getAssociatedEvaluations()) {
            if (evaluation instanceof Exam) {
                associatedExams.add((Exam) evaluation);
            }
        }

        return associatedExams;
    }

    public List<WrittenEvaluation> getAssociatedWrittenEvaluations() {
        Set<WrittenEvaluation> writtenEvaluations = new HashSet<WrittenEvaluation>();
        writtenEvaluations.addAll(this.getAssociatedExams());
        writtenEvaluations.addAll(this.getAssociatedWrittenTests());

        return new ArrayList<WrittenEvaluation>(writtenEvaluations);

    }

    public List<WrittenTest> getAssociatedWrittenTests() {
        List<WrittenTest> associatedWrittenTests = new ArrayList<WrittenTest>();

        for (Evaluation evaluation : this.getAssociatedEvaluations()) {
            if (evaluation instanceof WrittenTest) {
                associatedWrittenTests.add((WrittenTest) evaluation);
            }
        }

        return associatedWrittenTests;
    }

    public List<OnlineTest> getAssociatedOnlineTests() {
        List<OnlineTest> associatedOnlineTests = new ArrayList<OnlineTest>();

        for (Evaluation evaluation : this.getAssociatedEvaluations()) {
            if (evaluation instanceof OnlineTest) {
                associatedOnlineTests.add((OnlineTest) evaluation);
            }
        }

        return associatedOnlineTests;
    }

    // Delete Method

    public void delete() {
        if (canBeDeleted()) {

            if (hasSender()) {
                getSender().getRecipientsSet().clear();
                setSender(null);
            }

            if (hasSite()) {
                getSite().delete();
            }

            if (hasBoard()) {
                getBoard().delete();
            }

            for (; !getMetadatas().isEmpty(); getMetadatas().iterator().next().delete()) {
                ;
            }
            for (; !getTestGroups().isEmpty(); getTestGroups().iterator().next().delete()) {
                ;
            }
            for (; !getExportGroupings().isEmpty(); getExportGroupings().iterator().next().delete()) {
                ;
            }
            for (; !getGroupingSenderExecutionCourse().isEmpty(); getGroupingSenderExecutionCourse().iterator().next().delete()) {
                ;
            }
            for (; !getCourseLoads().isEmpty(); getCourseLoads().iterator().next().delete()) {
                ;
            }
            for (; !getProfessorships().isEmpty(); getProfessorships().iterator().next().delete()) {
                ;
            }
            for (; !getLessonPlannings().isEmpty(); getLessonPlannings().iterator().next().delete()) {
                ;
            }
            for (; !getExecutionCourseProperties().isEmpty(); getExecutionCourseProperties().iterator().next().delete()) {
                ;
            }
            for (; !getAttends().isEmpty(); getAttends().iterator().next().delete()) {
                ;
            }
            for (; !getForuns().isEmpty(); getForuns().iterator().next().delete()) {
                ;
            }
            for (; !getExecutionCourseLogs().isEmpty(); getExecutionCourseLogs().iterator().next().delete()) {
                ;
            }

            removeFinalEvaluations();
            getAssociatedCurricularCourses().clear();
            getNonAffiliatedTeachers().clear();
            setVigilantGroup(null);
            setExecutionPeriod(null);
            setRootDomainObject(null);
            super.deleteDomainObject();

        } else {
            throw new DomainException("error.execution.course.cant.delete");
        }
    }

    private void removeFinalEvaluations() {
        final Iterator<Evaluation> iterator = getAssociatedEvaluationsSet().iterator();
        while (iterator.hasNext()) {
            final Evaluation evaluation = iterator.next();
            if (evaluation.isFinal()) {
                iterator.remove();
                evaluation.delete();
            } else {
                throw new DomainException("error.ExecutionCourse.cannot.remove.non.final.evaluation");
            }
        }
    }

    public boolean canBeDeleted() {

        if (hasAnyAssociatedInquiriesCourses()) {
            throw new DomainException("error.execution.course.cant.delete");
        }
        if (hasAnyAssociatedInquiriesRegistries()) {
            throw new DomainException("error.execution.course.cant.delete");
        }
        if (hasAnyStudentInquiriesCourseResults()) {
            throw new DomainException("error.execution.course.cant.delete");
        }
        if (hasAnyYearDelegateCourseInquiries()) {
            throw new DomainException("error.execution.course.cant.delete");
        }
        if (hasAnyAssociatedSummaries()) {
            throw new DomainException("error.execution.course.cant.delete");
        }
        if (!getGroupings().isEmpty()) {
            throw new DomainException("error.execution.course.cant.delete");
        }
        if (hasAnyAssociatedBibliographicReferences()) {
            throw new DomainException("error.execution.course.cant.delete");
        }
        if (!hasOnlyFinalEvaluations()) {
            throw new DomainException("error.execution.course.cant.delete");
        }
        if (hasEvaluationMethod()) {
            throw new DomainException("error.execution.course.cant.delete");
        }
        if (!getAssociatedShifts().isEmpty()) {
            throw new DomainException("error.execution.course.cant.delete");
        }
        if (hasCourseReport()) {
            throw new DomainException("error.execution.course.cant.delete");
        }
        if (hasAnyAttends()) {
            throw new DomainException("error.execution.course.cant.delete");
        }
        if (hasSite() && !getSite().isDeletable()) {
            throw new DomainException("error.execution.course.cant.delete");
        }
        if (hasBoard() && !getBoard().isDeletable()) {
            throw new DomainException("error.execution.course.cant.delete");
        }

        for (final Professorship professorship : getProfessorships()) {
            if (!professorship.canBeDeleted()) {
                throw new DomainException("error.execution.course.cant.delete");
            }
        }

        for (ExecutionCourseForum forum : getForuns()) {
            if (forum.getConversationThreads().size() != 0) {
                throw new DomainException("error.execution.course.cant.delete");
            }
        }

        if (!getStudentGroupSet().isEmpty()) {
            throw new DomainException("error.executionCourse.cannotDeleteExecutionCourseUsedInAccessControl");
        }
        if (!getSpecialCriteriaOverExecutionCourseGroupSet().isEmpty()) {
            throw new DomainException("error.executionCourse.cannotDeleteExecutionCourseUsedInAccessControl");
        }
        if (!getTeacherGroupSet().isEmpty()) {
            throw new DomainException("error.executionCourse.cannotDeleteExecutionCourseUsedInAccessControl");
        }
        return true;
    }

    private boolean hasOnlyFinalEvaluations() {
        for (final Evaluation evaluation : getAssociatedEvaluationsSet()) {
            if (!evaluation.isFinal()) {
                return false;
            }
        }
        return true;
    }

    public boolean teacherLecturesExecutionCourse(Teacher teacher) {
        for (Professorship professorship : this.getProfessorships()) {
            if (professorship.getTeacher() == teacher) {
                return true;
            }
        }
        return false;
    }

    public List<net.sourceforge.fenixedu.domain.Project> getAssociatedProjects() {
        final List<net.sourceforge.fenixedu.domain.Project> result = new ArrayList<net.sourceforge.fenixedu.domain.Project>();

        for (Evaluation evaluation : this.getAssociatedEvaluations()) {
            if (evaluation instanceof net.sourceforge.fenixedu.domain.Project) {
                result.add((net.sourceforge.fenixedu.domain.Project) evaluation);
            }
        }
        return result;
    }

    private int countAssociatedStudentsByEnrolmentNumber(int enrolmentNumber) {
        int executionCourseAssociatedStudents = 0;
        ExecutionSemester courseExecutionPeriod = getExecutionPeriod();

        for (CurricularCourse curricularCourseFromExecutionCourseEntry : getAssociatedCurricularCourses()) {
            for (Enrolment enrolment : curricularCourseFromExecutionCourseEntry.getEnrolments()) {

                if (enrolment.getExecutionPeriod() == courseExecutionPeriod) {

                    StudentCurricularPlan studentCurricularPlanEntry = enrolment.getStudentCurricularPlan();
                    int numberOfEnrolmentsForThatExecutionCourse = 0;

                    for (Enrolment enrolmentsFromStudentCPEntry : studentCurricularPlanEntry.getEnrolmentsSet()) {
                        if (enrolmentsFromStudentCPEntry.getCurricularCourse() == curricularCourseFromExecutionCourseEntry
                                && (enrolmentsFromStudentCPEntry.getExecutionPeriod().compareTo(courseExecutionPeriod) <= 0)) {
                            ++numberOfEnrolmentsForThatExecutionCourse;
                            if (numberOfEnrolmentsForThatExecutionCourse > enrolmentNumber) {
                                break;
                            }
                        }
                    }

                    if (numberOfEnrolmentsForThatExecutionCourse == enrolmentNumber) {
                        executionCourseAssociatedStudents++;
                    }
                }
            }
        }

        return executionCourseAssociatedStudents;
    }

    public Integer getTotalEnrolmentStudentNumber() {
        int executionCourseStudentNumber = 0;
        for (final CurricularCourse curricularCourseFromExecutionCourseEntry : getAssociatedCurricularCourses()) {
            for (final Enrolment enrolment : curricularCourseFromExecutionCourseEntry.getEnrolments()) {
                if (enrolment.getExecutionPeriod() == getExecutionPeriod()) {
                    executionCourseStudentNumber++;
                }
            }
        }
        return executionCourseStudentNumber;
    }

    public Integer getFirstTimeEnrolmentStudentNumber() {
        return countAssociatedStudentsByEnrolmentNumber(1);
    }

    public Integer getSecondOrMoreTimeEnrolmentStudentNumber() {
        return getTotalEnrolmentStudentNumber() - getFirstTimeEnrolmentStudentNumber();
    }

    public Duration getTotalShiftsDuration() {
        Duration totalDuration = Duration.ZERO;
        for (Shift shift : getAssociatedShifts()) {
            totalDuration = totalDuration.plus(shift.getTotalDuration());
        }
        return totalDuration;
    }

    public BigDecimal getAllShiftUnitHours(ShiftType shiftType) {
        BigDecimal totalTime = BigDecimal.ZERO;
        for (Shift shift : getAssociatedShifts()) {
            if (shift.containsType(shiftType)) {
                totalTime = totalTime.add(shift.getUnitHours());
            }
        }
        return totalTime;
    }

    public BigDecimal getWeeklyCourseLoadTotalQuantityByShiftType(ShiftType type) {
        CourseLoad courseLoad = getCourseLoadByShiftType(type);
        return courseLoad != null ? courseLoad.getWeeklyHours() : BigDecimal.ZERO;
    }

    public Set<Shift> getAssociatedShifts() {
        Set<Shift> result = new HashSet<Shift>();
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            result.addAll(courseLoad.getShiftsSet());
        }
        return result;
    }

    public Set<LessonInstance> getAssociatedLessonInstances() {
        Set<LessonInstance> result = new HashSet<LessonInstance>();
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            result.addAll(courseLoad.getLessonInstances());
        }

        return result;
    }

    public Double getStudentsNumberByShift(ShiftType shiftType) {
        int numShifts = getNumberOfShifts(shiftType);

        if (numShifts == 0) {
            return 0.0;
        } else {
            return (double) getTotalEnrolmentStudentNumber() / numShifts;
        }
    }

    public List<Enrolment> getActiveEnrollments() {
        List<Enrolment> results = new ArrayList<Enrolment>();

        for (CurricularCourse curricularCourse : this.getAssociatedCurricularCourses()) {
            List<Enrolment> enrollments = curricularCourse.getActiveEnrollments(this.getExecutionPeriod());

            results.addAll(enrollments);
        }
        return results;
    }

    public List<Dismissal> getDismissals() {
        List<Dismissal> results = new ArrayList<Dismissal>();

        for (CurricularCourse curricularCourse : this.getAssociatedCurricularCourses()) {
            List<Dismissal> dismissals = curricularCourse.getDismissals(this.getExecutionPeriod());

            results.addAll(dismissals);
        }
        return results;
    }

    public boolean areAllOptionalCurricularCoursesWithLessTenEnrolments() {
        int enrolments = 0;
        for (CurricularCourse curricularCourse : this.getAssociatedCurricularCourses()) {
            if (curricularCourse.getType() != null && curricularCourse.getType().equals(CurricularCourseType.OPTIONAL_COURSE)) {
                enrolments += curricularCourse.getEnrolmentsByExecutionPeriod(this.getExecutionPeriod()).size();
                if (enrolments >= 10) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public static final Comparator<Evaluation> EVALUATION_COMPARATOR = new Comparator<Evaluation>() {

        @Override
        public int compare(Evaluation evaluation1, Evaluation evaluation2) {
            final String evaluation1ComparisonString = evaluationComparisonString(evaluation1);
            final String evaluation2ComparisonString = evaluationComparisonString(evaluation2);
            return evaluation1ComparisonString.compareTo(evaluation2ComparisonString);
        }

        private String evaluationComparisonString(final Evaluation evaluation) {
            final Date evaluationComparisonDate;
            final String evaluationTypeDistinguisher;

            if (evaluation instanceof AdHocEvaluation) {
                evaluationTypeDistinguisher = "0";
                final AdHocEvaluation adHocEvaluation = (AdHocEvaluation) evaluation;
                evaluationComparisonDate = adHocEvaluation.getCreationDateTime().toDate();
            } else if (evaluation instanceof OnlineTest) {
                evaluationTypeDistinguisher = "1";
                final OnlineTest onlineTest = (OnlineTest) evaluation;
                evaluationComparisonDate = onlineTest.getDistributedTest().getBeginDateDate();
            } else if (evaluation instanceof Project) {
                evaluationTypeDistinguisher = "2";
                final Project project = (Project) evaluation;
                evaluationComparisonDate = project.getBegin();
            } else if (evaluation instanceof WrittenEvaluation) {
                evaluationTypeDistinguisher = "3";
                final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;
                evaluationComparisonDate = writtenEvaluation.getDayDate();
            } else if (evaluation instanceof FinalEvaluation) {
                evaluationTypeDistinguisher = "4";
                final ExecutionCourse executionCourse = evaluation.getAssociatedExecutionCourses().iterator().next();
                evaluationComparisonDate = executionCourse.getExecutionPeriod().getEndDate();
            } else {
                throw new DomainException("unknown.evaluation.type", evaluation.getClass().getName());
            }

            return DateFormatUtil.format(evaluationTypeDistinguisher + "_yyyy/MM/dd", evaluationComparisonDate)
                    + evaluation.getExternalId();
        }
    };

    public List<Evaluation> getOrderedAssociatedEvaluations() {
        final List<Evaluation> orderedEvaluations = new ArrayList<Evaluation>(getAssociatedEvaluations());
        Collections.sort(orderedEvaluations, EVALUATION_COMPARATOR);
        return orderedEvaluations;
    }

    public Set<Attends> getOrderedAttends() {
        final Set<Attends> orderedAttends = new TreeSet<Attends>(Attends.COMPARATOR_BY_STUDENT_NUMBER);
        orderedAttends.addAll(getAttends());
        return orderedAttends;
    }

    private static class CurricularCourseExecutionCourseListener extends RelationAdapter<ExecutionCourse, CurricularCourse> {

        @Override
        public void afterAdd(ExecutionCourse execution, CurricularCourse curricular) {
            for (final Enrolment enrolment : curricular.getEnrolments()) {
                if (enrolment.getExecutionPeriod().equals(execution.getExecutionPeriod())) {
                    associateAttend(enrolment, execution);
                }
            }
            fillCourseLoads(execution, curricular);
        }

        @Override
        public void afterRemove(ExecutionCourse execution, CurricularCourse curricular) {
            if (execution != null) {
                for (Attends attends : execution.getAttends()) {
                    if ((attends.getEnrolment() != null) && (attends.getEnrolment().getCurricularCourse().equals(curricular))) {
                        attends.setEnrolment(null);
                    }
                }
            }
        }

        private static void associateAttend(Enrolment enrolment, ExecutionCourse executionCourse) {
            if (!alreadyHasAttend(enrolment, executionCourse.getExecutionPeriod())) {
                Attends attends = executionCourse.getAttendsByStudent(enrolment.getStudentCurricularPlan().getRegistration());
                if (attends == null) {
                    attends = new Attends(enrolment.getStudentCurricularPlan().getRegistration(), executionCourse);
                }
                enrolment.addAttends(attends);
            }
        }

        private static boolean alreadyHasAttend(Enrolment enrolment, ExecutionSemester executionSemester) {
            for (Attends attends : enrolment.getAttends()) {
                if (attends.getExecutionCourse().getExecutionPeriod().equals(executionSemester)) {
                    return true;
                }
            }
            return false;
        }

        private void fillCourseLoads(ExecutionCourse execution, CurricularCourse curricular) {
            for (ShiftType shiftType : ShiftType.values()) {
                BigDecimal totalHours = curricular.getTotalHoursByShiftType(shiftType, execution.getExecutionPeriod());
                if (totalHours != null && totalHours.compareTo(BigDecimal.ZERO) == 1) {
                    CourseLoad courseLoad = execution.getCourseLoadByShiftType(shiftType);
                    if (courseLoad == null) {
                        new CourseLoad(execution, shiftType, null, totalHours);
                    }
                }
            }
        }
    }

    public class WeeklyWorkLoadView {
        final Interval executionPeriodInterval;

        final int numberOfWeeks;

        final Interval[] intervals;

        final int[] numberResponses;

        final int[] contactSum;

        final int[] autonomousStudySum;

        final int[] otherSum;

        final int[] totalSum;

        public WeeklyWorkLoadView(final Interval executionPeriodInterval) {
            this.executionPeriodInterval = executionPeriodInterval;
            final Period period = executionPeriodInterval.toPeriod();
            int extraWeek = period.getDays() > 0 ? 1 : 0;
            numberOfWeeks = (period.getYears() * 12 + period.getMonths()) * 4 + period.getWeeks() + extraWeek + 1;
            intervals = new Interval[numberOfWeeks];
            numberResponses = new int[numberOfWeeks];
            contactSum = new int[numberOfWeeks];
            autonomousStudySum = new int[numberOfWeeks];
            otherSum = new int[numberOfWeeks];
            totalSum = new int[numberOfWeeks];
            for (int i = 0; i < numberOfWeeks; i++) {
                final DateTime start = executionPeriodInterval.getStart().plusWeeks(i);
                final DateTime end = start.plusWeeks(1);
                intervals[i] = new Interval(start, end);
            }
        }

        public void add(final Attends attends) {
            for (final WeeklyWorkLoad weeklyWorkLoad : attends.getWeeklyWorkLoads()) {
                final int weekIndex = weeklyWorkLoad.getWeekOffset();
                if (consistentAnswers(attends, weekIndex)) {
                    numberResponses[weekIndex]++;

                    final Integer contact = weeklyWorkLoad.getContact();
                    contactSum[weekIndex] += contact != null ? contact.intValue() : 0;

                    final Integer autounomousStudy = weeklyWorkLoad.getAutonomousStudy();
                    autonomousStudySum[weekIndex] += autounomousStudy != null ? autounomousStudy.intValue() : 0;

                    final Integer other = weeklyWorkLoad.getOther();
                    otherSum[weekIndex] += other != null ? other.intValue() : 0;

                    totalSum[weekIndex] = contactSum[weekIndex] + autonomousStudySum[weekIndex] + otherSum[weekIndex];
                }
            }
        }

        private boolean consistentAnswers(final Attends attends, final int weekIndex) {
            int weeklyTotal = 0;
            for (final Attends someAttends : attends.getRegistration().getAssociatedAttends()) {
                for (final WeeklyWorkLoad weeklyWorkLoad : someAttends.getWeeklyWorkLoads()) {
                    if (weeklyWorkLoad.getWeekOffset().intValue() == weekIndex) {
                        weeklyTotal += weeklyWorkLoad.getTotal();
                    }
                }
            }
            return weeklyTotal <= 140;
        }

        public Interval[] getIntervals() {
            return intervals;
        }

        public Interval getExecutionPeriodInterval() {
            return executionPeriodInterval;
        }

        public int[] getContactSum() {
            return contactSum;
        }

        public int[] getAutonomousStudySum() {
            return autonomousStudySum;
        }

        public int[] getOtherSum() {
            return otherSum;
        }

        public int[] getNumberResponses() {
            return numberResponses;
        }

        public double[] getContactAverage() {
            return average(getContactSum(), getNumberResponses());
        }

        public double[] getAutonomousStudyAverage() {
            return average(getAutonomousStudySum(), getNumberResponses());
        }

        public double[] getOtherAverage() {
            return average(getOtherSum(), getNumberResponses());
        }

        public double[] getTotalAverage() {
            final double[] valuesAverage = new double[numberOfWeeks];
            for (int i = 0; i < numberOfWeeks; i++) {
                valuesAverage[i] =
                        Math.round((0.0 + getContactSum()[i] + getAutonomousStudySum()[i] + getOtherSum()[i])
                                / getNumberResponses()[i]);
            }
            return valuesAverage;
        }

        private double[] average(final int[] values, final int[] divisor) {
            final double[] valuesAverage = new double[numberOfWeeks];
            for (int i = 0; i < numberOfWeeks; i++) {
                valuesAverage[i] = Math.round((0.0 + values[i]) / divisor[i]);
            }
            return valuesAverage;
        }

        private double add(final double[] values) {
            double total = 0;
            for (double value : values) {
                total += value;
            }
            return total;
        }

        public double getContactAverageTotal() {
            return add(getContactAverage());
        }

        public double getAutonomousStudyAverageTotal() {
            return add(getAutonomousStudyAverage());
        }

        public double getOtherAverageTotal() {
            return add(getOtherAverage());
        }

        public double getTotalAverageTotal() {
            return add(getTotalAverage());
        }

        public int getNumberResponsesTotal() {
            int total = 0;
            for (int i = 0; i < getNumberResponses().length; i++) {
                total += getNumberResponses()[i];
            }
            return total;
        }

        private int getNumberWeeksForAverageCalculation() {
            if (!getAttendsSet().isEmpty()) {
                final Attends attends = findAttendsWithEnrolment();
                if (attends != null) {
                    int currentWeekOffset = attends.calculateCurrentWeekOffset();
                    if (currentWeekOffset > 0 && currentWeekOffset < numberOfWeeks) {
                        return currentWeekOffset;
                    }
                }
            }
            return numberOfWeeks;
        }

        public double getContactAverageTotalAverage() {
            final int numberOfWeeks = getNumberWeeksForAverageCalculation();
            return numberOfWeeks > 0 ? Math.round(getContactAverageTotal() / numberOfWeeks) : 0;
        }

        public double getAutonomousStudyAverageTotalAverage() {
            final int numberOfWeeks = getNumberWeeksForAverageCalculation();
            return numberOfWeeks > 0 ? Math.round(getAutonomousStudyAverageTotal() / getNumberWeeksForAverageCalculation()) : 0;
        }

        public double getOtherAverageTotalAverage() {
            final int numberOfWeeks = getNumberWeeksForAverageCalculation();
            return numberOfWeeks > 0 ? Math.round(getOtherAverageTotal() / getNumberWeeksForAverageCalculation()) : 0;
        }

        public double getTotalAverageTotalAverage() {
            final int numberOfWeeks = getNumberWeeksForAverageCalculation();
            return numberOfWeeks > 0 ? Math.round(getTotalAverageTotal() / getNumberWeeksForAverageCalculation()) : 0;
        }

        public double getNumberResponsesTotalAverage() {
            final int numberOfWeeks = getNumberWeeksForAverageCalculation();
            return numberOfWeeks > 0 ? Math.round((0.0 + getNumberResponsesTotal()) / getNumberWeeksForAverageCalculation()) : 0;
        }

    }

    public Interval getInterval() {
        final ExecutionSemester executionSemester = getExecutionPeriod();
        final DateTime beginningOfSemester = new DateTime(executionSemester.getBeginDateYearMonthDay());
        final DateTime firstMonday = beginningOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1);
        final DateTime endOfSemester = new DateTime(executionSemester.getEndDateYearMonthDay());
        final DateTime nextLastMonday = endOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1).plusWeeks(1);
        return new Interval(firstMonday, nextLastMonday);
    }

    public WeeklyWorkLoadView getWeeklyWorkLoadView() {
        final Attends attends = findAttendsWithEnrolment();
        if (attends != null) {
            final Interval interval = attends.getWeeklyWorkLoadInterval();
            final WeeklyWorkLoadView weeklyWorkLoadView = new WeeklyWorkLoadView(interval);
            for (final Attends attend : getAttends()) {
                weeklyWorkLoadView.add(attend);
            }
            return weeklyWorkLoadView;
        } else {
            return null;
        }
    }

    private Attends findAttendsWithEnrolment() {
        for (final Attends attends : getAttends()) {
            if (attends.getEnrolment() != null) {
                return attends;
            }
        }
        return null;
    }

    public boolean hasGrouping(final Grouping grouping) {
        for (final ExportGrouping exportGrouping : getExportGroupings()) {
            if (grouping == exportGrouping.getGrouping()) {
                return true;
            }
        }
        return false;
    }

    public Shift findShiftByName(final String shiftName) {
        for (final Shift shift : getAssociatedShifts()) {
            if (shift.getNome().equals(shiftName)) {
                return shift;
            }
        }
        return null;
    }

    public Set<Shift> findShiftByType(final ShiftType shiftType) {
        final Set<Shift> shifts = new HashSet<Shift>();
        for (final Shift shift : getAssociatedShifts()) {
            if (shift.containsType(shiftType)) {
                shifts.add(shift);
            }
        }
        return shifts;
    }

    public Set<SchoolClass> findSchoolClasses() {
        final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
        for (final Shift shift : getAssociatedShifts()) {
            schoolClasses.addAll(shift.getAssociatedClasses());
        }
        return schoolClasses;
    }

    public List<Summary> readSummariesOfTeachersWithoutProfessorship() {

        List<Summary> summaries = new ArrayList<Summary>();
        for (Summary summary : this.getAssociatedSummaries()) {
            if (!summary.hasProfessorship()
                    && (summary.hasTeacher() || (summary.getTeacherName() != null && !summary.getTeacherName().equals("")))) {
                summaries.add(summary);
            }
        }
        return summaries;
    }

    public ExportGrouping getExportGrouping(final Grouping grouping) {
        for (final ExportGrouping exportGrouping : this.getExportGroupingsSet()) {
            if (exportGrouping.getGrouping() == grouping) {
                return exportGrouping;
            }
        }
        return null;
    }

    public boolean hasExportGrouping(final Grouping grouping) {
        return getExportGrouping(grouping) != null;
    }

    public boolean hasScopeInGivenSemesterAndCurricularYearInDCP(CurricularYear curricularYear,
            DegreeCurricularPlan degreeCurricularPlan) {
        for (CurricularCourse curricularCourse : this.getAssociatedCurricularCourses()) {
            if (curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(curricularYear, degreeCurricularPlan,
                    getExecutionPeriod())) {
                return true;
            }
        }
        return false;
    }

    public Set<Metadata> findVisibleMetadata() {
        final Set<Metadata> visibleMetadata = new HashSet<Metadata>();
        for (final Metadata metadata : getMetadatasSet()) {
            if (metadata.getVisibility() != null && metadata.getVisibility().booleanValue()) {
                visibleMetadata.add(metadata);
            }
        }
        return visibleMetadata;
    }

    public void createForum(MultiLanguageString name, MultiLanguageString description) {

        if (hasForumWithName(name)) {
            throw new DomainException("executionCourse.already.existing.forum");
        }
        this.addForuns(new ExecutionCourseForum(name, description));
    }

    public ExecutionCourseAnnouncementBoard createExecutionCourseAnnouncementBoard(final String name) {
        return new ExecutionCourseAnnouncementBoard(name, this, new ExecutionCourseTeachersGroup(this), null, new RoleTypeGroup(
                RoleType.MANAGER), ExecutionCourseBoardPermittedGroupType.ECB_EXECUTION_COURSE_TEACHERS,
                ExecutionCourseBoardPermittedGroupType.ECB_PUBLIC, ExecutionCourseBoardPermittedGroupType.ECB_MANAGER);
    }

    public boolean hasForumWithName(MultiLanguageString name) {
        return hasSite() && getForumByName(name) != null;
    }

    public ExecutionCourseForum getForumByName(MultiLanguageString name) {
        if (!hasSite()) {
            return null;
        }

        return getSite().getForumByName(name);
    }

    public void addForuns(ExecutionCourseForum forum) {
        if (!hasSite()) {
            throw new DomainException("error.cannot.add.forum.empty.site");
        }

        getSite().addForum(forum);
    }

    public void checkIfCanAddForum(MultiLanguageString name) {
        if (!hasSite()) {
            throw new DomainException("error.cannot.add.forum.empty.site");
        }
        if (hasForumWithName(name)) {
            throw new DomainException("executionCourse.already.existing.forum");
        }
    }

    public SortedSet<Degree> getDegreesSortedByDegreeName() {
        final SortedSet<Degree> degrees = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            degrees.add(degreeCurricularPlan.getDegree());
        }
        return degrees;
    }

    public SortedSet<CurricularCourse> getCurricularCoursesSortedByDegreeAndCurricularCourseName() {
        final SortedSet<CurricularCourse> curricularCourses =
                new TreeSet<CurricularCourse>(CurricularCourse.CURRICULAR_COURSE_COMPARATOR_BY_DEGREE_AND_NAME);
        curricularCourses.addAll(getAssociatedCurricularCoursesSet());
        return curricularCourses;
    }

    public Set<CompetenceCourse> getCompetenceCourses() {
        final Set<CompetenceCourse> competenceCourses = new HashSet<CompetenceCourse>();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                competenceCourses.add(competenceCourse);
            }
        }
        return competenceCourses;
    }

    public Set<CompetenceCourseInformation> getCompetenceCoursesInformations() {
        final Set<CompetenceCourseInformation> competenceCourseInformations = new HashSet<CompetenceCourseInformation>();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                final CompetenceCourseInformation competenceCourseInformation =
                        competenceCourse.findCompetenceCourseInformationForExecutionPeriod(getExecutionPeriod());
                if (competenceCourseInformation != null) {
                    competenceCourseInformations.add(competenceCourseInformation);
                }
            }
        }
        return competenceCourseInformations;
    }

    public boolean hasAnyDegreeGradeToSubmit(final ExecutionSemester period, final DegreeCurricularPlan degreeCurricularPlan) {
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            if (degreeCurricularPlan == null || degreeCurricularPlan.equals(curricularCourse.getDegreeCurricularPlan())) {
                if (curricularCourse.hasAnyDegreeGradeToSubmit(period)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasAnyDegreeMarkSheetToConfirm(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        for (final CurricularCourse curricularCourse : this.getAssociatedCurricularCourses()) {
            if (degreeCurricularPlan == null || degreeCurricularPlan.equals(curricularCourse.getDegreeCurricularPlan())) {
                if (curricularCourse.hasAnyDegreeMarkSheetToConfirm(period)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String constructShiftName(final Shift shift, final int n) {
        final String number = n < 10 ? "0" + n : Integer.toString(n);
        StringBuilder typesName = new StringBuilder();
        for (ShiftType shiftType : shift.getSortedTypes()) {
            typesName.append(shiftType.getSiglaTipoAula());
        }
        return getSigla() + typesName.toString() + number;
    }

    public SortedSet<Shift> getShiftsByTypeOrderedByShiftName(final ShiftType shiftType) {
        final SortedSet<Shift> shifts = new TreeSet<Shift>(Shift.SHIFT_COMPARATOR_BY_NAME);
        for (final Shift shift : getAssociatedShifts()) {
            if (shift.containsType(shiftType)) {
                shifts.add(shift);
            }
        }
        return shifts;
    }

    public void setShiftNames() {
        final SortedSet<Shift> shifts =
                CollectionUtils.constructSortedSet(getAssociatedShifts(), Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
        int counter = 0;
        for (final Shift shift : shifts) {
            final String name = constructShiftName(shift, ++counter);
            shift.setNome(name);
        }
    }

    public boolean hasProjectsWithOnlineSubmission() {
        for (Project project : getAssociatedProjects()) {
            if (project.getOnlineSubmissionsAllowed() == true) {
                return true;
            }
        }

        return false;
    }

    public List<Project> getProjectsWithOnlineSubmission() {
        List<Project> result = new ArrayList<Project>();
        for (Project project : getAssociatedProjects()) {
            if (project.getOnlineSubmissionsAllowed() == true) {
                result.add(project);
            }
        }

        return result;
    }

    private Set<SchoolClass> getAllSchoolClassesOrBy(DegreeCurricularPlan degreeCurricularPlan) {
        final Set<SchoolClass> result = new HashSet<SchoolClass>();
        for (final Shift shift : getAssociatedShifts()) {
            for (final SchoolClass schoolClass : shift.getAssociatedClassesSet()) {
                if (degreeCurricularPlan == null
                        || schoolClass.getExecutionDegree().getDegreeCurricularPlan() == degreeCurricularPlan) {
                    result.add(schoolClass);
                }
            }
        }
        return result;
    }

    public Set<SchoolClass> getSchoolClassesBy(DegreeCurricularPlan degreeCurricularPlan) {
        return getAllSchoolClassesOrBy(degreeCurricularPlan);
    }

    public Set<SchoolClass> getSchoolClasses() {
        return getAllSchoolClassesOrBy(null);
    }

    public boolean isLecturedIn(final ExecutionYear executionYear) {
        return getExecutionPeriod().getExecutionYear() == executionYear;
    }

    public boolean isLecturedIn(final ExecutionSemester executionSemester) {
        return getExecutionPeriod() == executionSemester;
    }

    public SortedSet<Professorship> getProfessorshipsSortedAlphabetically() {
        final SortedSet<Professorship> professorhips = new TreeSet<Professorship>(Professorship.COMPARATOR_BY_PERSON_NAME);
        professorhips.addAll(getProfessorshipsSet());
        return professorhips;
    }

    public SummariesSearchBean getSummariesSearchBean() {
        return new SummariesSearchBean(this);
    }

    public Set<Lesson> getLessons() {
        final Set<Lesson> lessons = new HashSet<Lesson>();
        for (final Shift shift : getAssociatedShifts()) {
            lessons.addAll(shift.getAssociatedLessonsSet());
        }
        return lessons;
    }

    public boolean hasAnyLesson() {
        for (CourseLoad courseLoad : getCourseLoadsSet()) {
            for (final Shift shift : courseLoad.getShiftsSet()) {
                if (shift.hasAnyAssociatedLessons()) {
                    return true;
                }
            }
        }
        return false;
    }

    public SortedSet<WrittenEvaluation> getWrittenEvaluations() {
        final SortedSet<WrittenEvaluation> writtenEvaluations =
                new TreeSet<WrittenEvaluation>(WrittenEvaluation.COMPARATOR_BY_BEGIN_DATE);
        for (final Evaluation evaluation : getAssociatedEvaluationsSet()) {
            if (evaluation instanceof WrittenEvaluation) {
                writtenEvaluations.add((WrittenEvaluation) evaluation);
            }
        }
        return writtenEvaluations;
    }

    public SortedSet<Shift> getShiftsOrderedByLessons() {
        final SortedSet<Shift> shifts = new TreeSet<Shift>(Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
        shifts.addAll(getAssociatedShifts());
        return shifts;
    }

    public Map<CompetenceCourse, Set<CurricularCourse>> getCurricularCoursesIndexedByCompetenceCourse() {
        final Map<CompetenceCourse, Set<CurricularCourse>> curricularCourseMap =
                new HashMap<CompetenceCourse, Set<CurricularCourse>>();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.isBolonhaDegree()) {
                final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
                if (competenceCourse != null) {
                    final Set<CurricularCourse> curricularCourses;
                    if (curricularCourseMap.containsKey(competenceCourse)) {
                        curricularCourses = curricularCourseMap.get(competenceCourse);
                    } else {
                        curricularCourses =
                                new TreeSet<CurricularCourse>(CurricularCourse.CURRICULAR_COURSE_COMPARATOR_BY_DEGREE_AND_NAME);
                        curricularCourseMap.put(competenceCourse, curricularCourses);
                    }
                    curricularCourses.add(curricularCourse);
                }
            }
        }
        return curricularCourseMap;
    }

    public boolean getHasAnySecondaryBibliographicReference() {
        return hasAnyBibliographicReferenceByBibliographicReferenceType(BibliographicReferenceType.SECONDARY);
    }

    public boolean getHasAnyMainBibliographicReference() {
        return hasAnyBibliographicReferenceByBibliographicReferenceType(BibliographicReferenceType.MAIN);
    }

    private boolean hasAnyBibliographicReferenceByBibliographicReferenceType(BibliographicReferenceType referenceType) {
        for (final BibliographicReference bibliographicReference : getAssociatedBibliographicReferencesSet()) {
            if ((referenceType.equals(BibliographicReferenceType.SECONDARY) && bibliographicReference.getOptional()
                    .booleanValue())
                    || (referenceType.equals(BibliographicReferenceType.MAIN) && !bibliographicReference.getOptional()
                            .booleanValue())) {
                return true;
            }
        }
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                final CompetenceCourseInformation competenceCourseInformation =
                        competenceCourse.findCompetenceCourseInformationForExecutionPeriod(getExecutionPeriod());
                if (competenceCourseInformation != null) {
                    final net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences bibliographicReferences =
                            competenceCourseInformation.getBibliographicReferences();
                    if (bibliographicReferences != null) {
                        for (final net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference bibliographicReference : bibliographicReferences
                                .getBibliographicReferencesList()) {
                            if (bibliographicReference.getType() == referenceType) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public List<LessonPlanning> getLessonPlanningsOrderedByOrder(ShiftType lessonType) {
        final List<LessonPlanning> lessonPlannings = new ArrayList<LessonPlanning>();
        for (LessonPlanning planning : getLessonPlanningsSet()) {
            if (planning.getLessonType().equals(lessonType)) {
                lessonPlannings.add(planning);
            }
        }
        Collections.sort(lessonPlannings, LessonPlanning.COMPARATOR_BY_ORDER);
        return lessonPlannings;
    }

    public LessonPlanning getLessonPlanning(ShiftType lessonType, Integer order) {
        for (LessonPlanning planning : getLessonPlanningsSet()) {
            if (planning.getLessonType().equals(lessonType) && planning.getOrderOfPlanning().equals(order)) {
                return planning;
            }
        }
        return null;
    }

    public Set<ShiftType> getShiftTypes() {
        Set<ShiftType> shiftTypes = new TreeSet<ShiftType>();
        for (CourseLoad courseLoad : getCourseLoads()) {
            shiftTypes.add(courseLoad.getType());
        }
        return shiftTypes;
    }

    public void copyLessonPlanningsFrom(ExecutionCourse executionCourseFrom) {
        Set<ShiftType> shiftTypes = getShiftTypes();
        for (ShiftType shiftType : executionCourseFrom.getShiftTypes()) {
            if (shiftTypes.contains(shiftType)) {
                List<LessonPlanning> lessonPlanningsFrom = executionCourseFrom.getLessonPlanningsOrderedByOrder(shiftType);
                if (!lessonPlanningsFrom.isEmpty()) {
                    for (LessonPlanning planning : lessonPlanningsFrom) {
                        new LessonPlanning(planning.getTitle(), planning.getPlanning(), planning.getLessonType(), this);
                    }
                }
            }
        }
    }

    public void createLessonPlanningsUsingSummariesFrom(Shift shift) {
        List<Summary> summaries = new ArrayList<Summary>();
        summaries.addAll(shift.getAssociatedSummaries());
        Collections.sort(summaries, new ReverseComparator(Summary.COMPARATOR_BY_DATE_AND_HOUR));
        for (Summary summary : summaries) {
            for (ShiftType shiftType : shift.getTypes()) {
                new LessonPlanning(summary.getTitle(), summary.getSummaryText(), shiftType, this);
            }
        }
    }

    public void deleteLessonPlanningsByLessonType(ShiftType shiftType) {
        List<LessonPlanning> lessonPlanningsOrderedByOrder = getLessonPlanningsOrderedByOrder(shiftType);
        for (LessonPlanning planning : lessonPlanningsOrderedByOrder) {
            planning.deleteWithoutReOrder();
        }
    }

    public Integer getNumberOfShifts(ShiftType shiftType) {
        int numShifts = 0;
        for (Shift shiftEntry : getAssociatedShifts()) {
            if (shiftEntry.containsType(shiftType)) {
                numShifts++;
            }
        }
        return numShifts;
    }

    public Double getCurricularCourseEnrolmentsWeight(CurricularCourse curricularCourse) {
        Double totalEnrolmentStudentNumber = new Double(getTotalEnrolmentStudentNumber());
        if (totalEnrolmentStudentNumber > 0d) {
            return curricularCourse.getTotalEnrolmentStudentNumber(getExecutionPeriod()) / totalEnrolmentStudentNumber;
        } else {
            return 0d;
        }
    }

    public Set<ShiftType> getOldShiftTypesToEnrol() {
        final List<ShiftType> validShiftTypes =
                Arrays.asList(new ShiftType[] { ShiftType.TEORICA, ShiftType.PRATICA, ShiftType.LABORATORIAL,
                        ShiftType.TEORICO_PRATICA });

        final Set<ShiftType> result = new HashSet<ShiftType>(4);
        for (final Shift shift : getAssociatedShifts()) {
            for (ShiftType shiftType : shift.getTypes()) {
                if (validShiftTypes.contains(shiftType)) {
                    result.add(shiftType);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Tells if all the associated Curricular Courses load are the same
     */
    public String getEqualLoad() {
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            for (ShiftType type : ShiftType.values()) {
                if (!getEqualLoad(type, curricularCourse)) {
                    return Boolean.FALSE.toString();
                }
            }
        }
        return Boolean.TRUE.toString();
    }

    public boolean getEqualLoad(ShiftType type, CurricularCourse curricularCourse) {
        if (type != null) {
            if (type.equals(ShiftType.DUVIDAS) || type.equals(ShiftType.RESERVA)) {
                return true;
            }
            BigDecimal ccTotalHours = curricularCourse.getTotalHoursByShiftType(type, getExecutionPeriod());
            CourseLoad courseLoad = getCourseLoadByShiftType(type);
            if ((courseLoad == null && ccTotalHours == null)
                    || (courseLoad == null && ccTotalHours != null && ccTotalHours.compareTo(BigDecimal.ZERO) == 0)
                    || (courseLoad != null && ccTotalHours != null && courseLoad.getTotalQuantity().compareTo(ccTotalHours) == 0)) {
                return true;
            }
        }
        return false;
    }

    public List<Summary> getSummariesByShiftType(ShiftType shiftType) {
        List<Summary> summaries = new ArrayList<Summary>();
        for (Summary summary : getAssociatedSummariesSet()) {
            if (summary.getSummaryType() != null && summary.getSummaryType().equals(shiftType)) {
                summaries.add(summary);
            }
        }
        return summaries;
    }

    public List<Summary> getSummariesWithoutProfessorshipButWithTeacher(Teacher teacher) {
        List<Summary> summaries = new ArrayList<Summary>();
        if (teacher != null) {
            for (Summary summary : getAssociatedSummariesSet()) {
                if (summary.getTeacher() != null && summary.getTeacher().equals(teacher)) {
                    summaries.add(summary);
                }
            }
        }
        return summaries;
    }

    public void moveSummariesFromTeacherToProfessorship(Teacher teacher, Professorship professorship) {
        List<Summary> summaries = getSummariesWithoutProfessorshipButWithTeacher(teacher);
        for (Summary summary : summaries) {
            summary.moveFromTeacherToProfessorship(professorship);
        }
    }

    @Override
    public String getNome() {
        if (Language.getUserLanguage() == Language.en && hasAnyAssociatedCurricularCourses()) {
            final StringBuilder stringBuilder = new StringBuilder();

            final Set<String> names = new HashSet<String>();

            for (final CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
                if (!curricularCourse.getActiveDegreeModuleScopesInExecutionPeriod(getExecutionPeriod()).isEmpty()) {
                    final String name = curricularCourse.getNameEn();
                    if (!names.contains(name)) {
                        names.add(name);
                        if (stringBuilder.length() > 0) {
                            stringBuilder.append(" / ");
                        }
                        stringBuilder.append(name);
                    }
                }
            }

            if (stringBuilder.length() > 0) {
                return stringBuilder.toString();
            }

            boolean unique = true;
            final String nameEn = getAssociatedCurricularCourses().iterator().next().getNameEn();

            for (final CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
                if (curricularCourse.getNameEn() == null || !curricularCourse.getNameEn().equals(nameEn)) {
                    unique = false;
                    break;
                }
            }

            if (unique) {
                return nameEn;
            } else {
                return super.getNome();
            }
        }
        return super.getNome();
    }

    public String getName() {
        return getNome();
    }

    public String getDegreePresentationString() {
        SortedSet<Degree> degrees = this.getDegreesSortedByDegreeName();
        String result = "";
        int i = 0;
        for (Degree degree : degrees) {
            if (i > 0) {
                result += ", ";
            }
            result += degree.getSigla();
            i++;
        }
        return result;
    }

    public boolean hasPublishedTestGroups() {
        return this.getPublishedTestGroupsCount() > 0;
    }

    public Integer getPublishedTestGroupsCount() {
        return this.getPublishedTestGroups().size();
    }

    public boolean hasFinishedTestGroups() {
        return this.getFinishedTestGroupsCount() > 0;
    }

    public Integer getFinishedTestGroupsCount() {
        return this.getFinishedTestGroups().size();
    }

    public List<NewTestGroup> getPublishedTestGroups() {
        List<NewTestGroup> testGroups = new ArrayList<NewTestGroup>();

        for (NewTestGroup testGroup : this.getTestGroups()) {
            if (testGroup.getStatus().equals(TestGroupStatus.PUBLISHED)) {
                testGroups.add(testGroup);
            }
        }

        return testGroups;
    }

    public List<NewTestGroup> getFinishedTestGroups() {
        List<NewTestGroup> testGroups = new ArrayList<NewTestGroup>();

        for (NewTestGroup testGroup : this.getTestGroups()) {
            if (testGroup.isCorrected()) {
                testGroups.add(testGroup);
            }
        }

        return testGroups;
    }

    public Registration getRegistration(Person person) {
        for (Registration registration : person.getStudents()) {
            for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlans()) {
                for (Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                    for (ExecutionCourse course : enrolment.getExecutionCourses()) {
                        if (course.equals(this)) {
                            return registration;
                        }
                    }
                }
            }
        }

        return null;
    }

    public ExecutionYear getExecutionYear() {
        return getExecutionPeriod().getExecutionYear();
    }

    public CurricularCourse getCurricularCourseFor(final DegreeCurricularPlan degreeCurricularPlan) {
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.getDegreeCurricularPlan() == degreeCurricularPlan) {
                return curricularCourse;
            }
        }

        return null;
    }

    public SortedSet<BibliographicReference> getOrderedBibliographicReferences() {
        TreeSet<BibliographicReference> references =
                new TreeSet<BibliographicReference>(BibliographicReference.COMPARATOR_BY_ORDER);
        references.addAll(getAssociatedBibliographicReferences());
        return references;
    }

    public void setBibliographicReferencesOrder(List<BibliographicReference> references) {
        BIBLIOGRAPHIC_REFERENCE_ORDER_ADAPTER.updateOrder(this, references);
    }

    public List<BibliographicReference> getMainBibliographicReferences() {
        List<BibliographicReference> references = new ArrayList<BibliographicReference>();

        for (BibliographicReference reference : getAssociatedBibliographicReferences()) {
            if (!reference.isOptional()) {
                references.add(reference);
            }
        }

        return references;
    }

    public List<BibliographicReference> getSecondaryBibliographicReferences() {
        List<BibliographicReference> references = new ArrayList<BibliographicReference>();

        for (BibliographicReference reference : getAssociatedBibliographicReferences()) {
            if (reference.isOptional()) {
                references.add(reference);
            }
        }

        return references;
    }

    public boolean isCompentenceCourseMainBibliographyAvailable() {
        for (CompetenceCourseInformation information : getCompetenceCoursesInformations()) {
            BibliographicReferences bibliographicReferences = information.getBibliographicReferences();
            if (bibliographicReferences != null && !bibliographicReferences.getMainBibliographicReferences().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public boolean isCompentenceCourseSecondaryBibliographyAvailable() {
        for (CompetenceCourseInformation information : getCompetenceCoursesInformations()) {
            BibliographicReferences bibliographicReferences = information.getBibliographicReferences();
            if (bibliographicReferences != null && !bibliographicReferences.getSecondaryBibliographicReferences().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public Collection<Curriculum> getCurriculums(final ExecutionYear executionYear) {
        final Collection<Curriculum> result = new HashSet<Curriculum>();

        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final Curriculum curriculum =
                    executionYear == null ? curricularCourse.findLatestCurriculum() : curricularCourse
                            .findLatestCurriculumModifiedBefore(executionYear.getEndDate());
            if (curriculum != null) {
                result.add(curriculum);
            }
        }

        return result;
    }

    public boolean isInExamPeriod() {
        final YearMonthDay yearMonthDay = new YearMonthDay();
        final ExecutionSemester executionSemester = getExecutionPeriod();
        final ExecutionYear executionYear = getExecutionPeriod().getExecutionYear();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
            final YearMonthDay startExamsPeriod;
            if (executionSemester.getSemester().intValue() == 1) {
                startExamsPeriod = executionDegree.getPeriodExamsFirstSemester().getStartYearMonthDay();
            } else if (executionSemester.getSemester().intValue() == 2) {
                startExamsPeriod = executionDegree.getPeriodExamsSecondSemester().getStartYearMonthDay();
            } else {
                throw new DomainException("unsupported.execution.period.semester");
            }
            if (!startExamsPeriod.minusDays(2).isAfter(yearMonthDay)) {
                return true;
            }
        }

        return false;
    }

    public List<Grouping> getGroupingsToEnrol() {
        final List<Grouping> result = new ArrayList<Grouping>();
        for (final Grouping grouping : getGroupings()) {
            if (checkPeriodEnrollmentFor(grouping)) {
                result.add(grouping);
            }
        }
        return result;
    }

    private boolean checkPeriodEnrollmentFor(final Grouping grouping) {
        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);
        return strategy.checkEnrolmentDate(grouping, Calendar.getInstance());

    }

    public SortedSet<ExecutionDegree> getFirsExecutionDegreesByYearWithExecutionIn(ExecutionYear executionYear) {
        SortedSet<ExecutionDegree> result = new TreeSet<ExecutionDegree>(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
        for (CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            ExecutionDegree executionDegree = curricularCourse.getDegreeCurricularPlan().getExecutionDegreeByYear(executionYear);
            if (executionDegree != null) {
                result.add(executionDegree);
            }
        }
        return result;
    }

    public Set<ExecutionDegree> getExecutionDegrees() {
        Set<ExecutionDegree> result = new HashSet<ExecutionDegree>();
        for (CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            ExecutionDegree executionDegree =
                    curricularCourse.getDegreeCurricularPlan().getExecutionDegreeByYear(getExecutionYear());
            if (executionDegree != null) {
                result.add(executionDegree);
            }
        }
        return result;
    }

    @Override
    public Boolean getAvailableForInquiries() {
        if (isBolonhaDegreeOrMasterDegreeOrIntegratedMasterDegree()) {
            if (super.getAvailableForInquiries() != null) {
                return super.getAvailableForInquiries();
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean getAvailableGradeSubmission() {
        if (super.getAvailableGradeSubmission() != null) {
            return super.getAvailableGradeSubmission();
        }
        return Boolean.TRUE;
    }

    @Override
    public void setUnitCreditValue(BigDecimal unitCreditValue) {
        setUnitCreditValue(unitCreditValue, getUnitCreditValueNotes());
    }

    public void setUnitCreditValue(BigDecimal unitCreditValue, String justification) {
        if (unitCreditValue != null
                && (unitCreditValue.compareTo(BigDecimal.ZERO) < 0 || unitCreditValue.compareTo(BigDecimal.ONE) > 0)) {
            throw new DomainException("error.executionCourse.unitCreditValue.range");
        }
        if (unitCreditValue != null && unitCreditValue.compareTo(BigDecimal.ZERO) != 0 && getEffortRate() == null) {
            throw new DomainException("error.executionCourse.unitCreditValue.noEffortRate");
        }
        if (getEffortRate() != null
                && (unitCreditValue != null
                        && unitCreditValue.compareTo(BigDecimal.valueOf(Math.min(getEffortRate().doubleValue(), 1.0))) < 0 && StringUtils
                            .isBlank(justification))) {
            throw new DomainException("error.executionCourse.unitCreditValue.lower.effortRate.withoutJustification");
        }
        super.setUnitCreditValueNotes(justification);
        super.setUnitCreditValue(unitCreditValue);
    }

    public Set<Department> getDepartments() {
        final ExecutionSemester executionSemester = getExecutionPeriod();
        final Set<Department> departments = new TreeSet<Department>(Department.COMPARATOR_BY_NAME);
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                final DepartmentUnit departmentUnit = competenceCourse.getDepartmentUnit(executionSemester);
                if (departmentUnit != null) {
                    final Department department = departmentUnit.getDepartment();
                    if (department != null) {
                        departments.add(department);
                    }
                }
            }
        }
        return departments;
    }

    public String getDepartmentNames() {
        final ExecutionSemester executionSemester = getExecutionPeriod();
        final Set<String> departments = new TreeSet<String>();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
            if (competenceCourse != null) {
                final DepartmentUnit departmentUnit = competenceCourse.getDepartmentUnit(executionSemester);
                if (departmentUnit != null) {
                    final Department department = departmentUnit.getDepartment();
                    if (department != null) {
                        departments.add(department.getName());
                    }
                }
            }
        }
        return StringUtils.join(departments, ", ");
    }

    public boolean isFromDepartment(final Department departmentToCheck) {
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (departmentToCheck == curricularCourse.getCompetenceCourse().getDepartmentUnit().getDepartment()) {
                return true;
            }
        }
        return false;
    }

    public GenericPair<YearMonthDay, YearMonthDay> getMaxLessonsPeriod() {

        YearMonthDay minBeginDate = null, maxEndDate = null;
        Integer semester = getExecutionPeriod().getSemester();

        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final ExecutionDegree executionDegree = curricularCourse.getExecutionDegreeFor(getExecutionYear());
            if (semester.intValue() == 1) {
                if (minBeginDate == null
                        || minBeginDate.isAfter(executionDegree.getPeriodLessonsFirstSemester().getStartYearMonthDay())) {
                    minBeginDate = executionDegree.getPeriodLessonsFirstSemester().getStartYearMonthDay();
                }
                if (maxEndDate == null
                        || maxEndDate.isBefore(executionDegree.getPeriodLessonsFirstSemester()
                                .getEndYearMonthDayWithNextPeriods())) {
                    maxEndDate = executionDegree.getPeriodLessonsFirstSemester().getEndYearMonthDayWithNextPeriods();
                }
            } else {
                if (minBeginDate == null
                        || minBeginDate.isAfter(executionDegree.getPeriodLessonsSecondSemester().getStartYearMonthDay())) {
                    minBeginDate = executionDegree.getPeriodLessonsSecondSemester().getStartYearMonthDay();
                }
                if (maxEndDate == null
                        || maxEndDate.isBefore(executionDegree.getPeriodLessonsSecondSemester()
                                .getEndYearMonthDayWithNextPeriods())) {
                    maxEndDate = executionDegree.getPeriodLessonsSecondSemester().getEndYearMonthDayWithNextPeriods();
                }
            }
        }

        if (minBeginDate != null && maxEndDate != null) {
            return new GenericPair<YearMonthDay, YearMonthDay>(minBeginDate, maxEndDate);
        }

        return null;
    }

    public Map<ShiftType, CourseLoad> getCourseLoadsMap() {
        Map<ShiftType, CourseLoad> result = new HashMap<ShiftType, CourseLoad>();
        Collection<CourseLoad> courseLoads = getCourseLoads();
        for (CourseLoad courseLoad : courseLoads) {
            result.put(courseLoad.getType(), courseLoad);
        }
        return result;
    }

    public CourseLoad getCourseLoadByShiftType(ShiftType type) {
        if (type != null) {
            for (CourseLoad courseLoad : getCourseLoads()) {
                if (courseLoad.getType().equals(type)) {
                    return courseLoad;
                }
            }
        }
        return null;
    }

    public boolean hasCourseLoadForType(ShiftType type) {
        CourseLoad courseLoad = getCourseLoadByShiftType(type);
        return courseLoad != null && !courseLoad.isEmpty();
    }

    public boolean verifyNameEquality(String[] nameWords) {
        if (nameWords != null) {
            String courseName = getNome() + " " + getSigla();
            if (courseName != null) {
                String[] courseNameWords = StringNormalizer.normalize(courseName).trim().split(" ");
                int j, i;
                for (i = 0; i < nameWords.length; i++) {
                    if (!nameWords[i].equals("")) {
                        for (j = 0; j < courseNameWords.length; j++) {
                            if (courseNameWords[j].equals(nameWords[i])) {
                                break;
                            }
                        }
                        if (j == courseNameWords.length) {
                            return false;
                        }
                    }
                }
                if (i == nameWords.length) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<AllocatableSpace> getAllRooms() {
        Set<AllocatableSpace> result = new HashSet<AllocatableSpace>();
        Set<Lesson> lessons = getLessons();
        for (Lesson lesson : lessons) {
            AllocatableSpace room = lesson.getSala();
            if (room != null) {
                result.add(room);
            }
        }
        return result;
    }

    public String getEvaluationMethodText() {
        if (hasEvaluationMethod()) {
            final MultiLanguageString evaluationElements = getEvaluationMethod().getEvaluationElements();

            return evaluationElements != null && evaluationElements.hasContent(Language.pt) ? evaluationElements
                    .getContent(Language.pt) : !getCompetenceCourses().isEmpty() ? getCompetenceCourses().iterator().next()
                    .getEvaluationMethod() : "";
        } else {
            return !getCompetenceCourses().isEmpty() ? getCompetenceCourses().iterator().next().getEvaluationMethod() : "";
        }
    }

    public String getEvaluationMethodTextEn() {
        if (hasEvaluationMethod()) {
            final MultiLanguageString evaluationElements = getEvaluationMethod().getEvaluationElements();

            return evaluationElements != null && evaluationElements.hasContent(Language.en) ? evaluationElements
                    .getContent(Language.en) : !getCompetenceCourses().isEmpty() ? getCompetenceCourses().iterator().next()
                    .getEvaluationMethod() : "";
        } else {
            return !getCompetenceCourses().isEmpty() ? getCompetenceCourses().iterator().next().getEvaluationMethod() : "";
        }
    }

    public List<ExecutionCourseForum> getForuns() {
        if (hasSite()) {
            return new ArrayList<ExecutionCourseForum>(getSite().getForuns());
        } else {
            return Collections.emptyList();
        }
    }

    public AcademicInterval getAcademicInterval() {
        return getExecutionPeriod().getAcademicInterval();
    }

    public static ExecutionCourse readBySiglaAndExecutionPeriod(final String sigla, ExecutionSemester executionSemester) {
        for (ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
            if (sigla.equalsIgnoreCase(executionCourse.getSigla())) {
                return executionCourse;
            }
        }
        return null;
    }

    public static ExecutionCourse readLastByExecutionYearAndSigla(final String sigla, ExecutionYear executionYear) {
        SortedSet<ExecutionCourse> result = new TreeSet<ExecutionCourse>(EXECUTION_COURSE_EXECUTION_PERIOD_COMPARATOR);
        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            for (ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
                if (sigla.equalsIgnoreCase(executionCourse.getSigla())) {
                    result.add(executionCourse);
                }
            }
        }
        return result.isEmpty() ? null : result.last();
    }

    public static ExecutionCourse readLastBySigla(final String sigla) {
        SortedSet<ExecutionCourse> result = new TreeSet<ExecutionCourse>(EXECUTION_COURSE_EXECUTION_PERIOD_COMPARATOR);
        for (ExecutionCourse executionCourse : Bennu.getInstance().getExecutionCoursesSet()) {
            if (sigla.equalsIgnoreCase(executionCourse.getSigla())) {
                result.add(executionCourse);
            }
        }
        return result.isEmpty() ? null : result.last();
    }

    public static ExecutionCourse readLastByExecutionIntervalAndSigla(final String sigla, ExecutionInterval executionInterval) {
        return executionInterval instanceof ExecutionSemester ? readBySiglaAndExecutionPeriod(sigla,
                (ExecutionSemester) executionInterval) : readLastByExecutionYearAndSigla(sigla, (ExecutionYear) executionInterval);
    }

    @Override
    public void setSigla(String sigla) {
        final String code = sigla.replace(' ', '_').replace('/', '-');
        final String uniqueCode = findUniqueCode(code);
        super.setSigla(uniqueCode);
    }

    private String findUniqueCode(final String code) {
        if (!existsMatchingCode(code)) {
            return code;
        }
        int c;
        for (c = 0; existsMatchingCode(code + "-" + c); c++) {
            ;
        }
        return code + "-" + c;
    }

    private boolean existsMatchingCode(final String code) {
        for (final ExecutionCourse executionCourse : getExecutionPeriod().getAssociatedExecutionCoursesSet()) {
            if (executionCourse != this && executionCourse.getSigla().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    public Collection<MarkSheet> getAssociatedMarkSheets() {
        Collection<MarkSheet> markSheets = new HashSet<MarkSheet>();
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            markSheets.addAll(curricularCourse.getMarkSheetsByPeriod(getExecutionPeriod()));
        }
        return markSheets;
    }

    public Set<Exam> getPublishedExamsFor(final CurricularCourse curricularCourse) {

        final Set<Exam> result = new HashSet<Exam>();
        for (final WrittenEvaluation eachEvaluation : getWrittenEvaluations()) {
            if (eachEvaluation.isExam()) {
                final Exam exam = (Exam) eachEvaluation;
                if (exam.isExamsMapPublished() && exam.contains(curricularCourse)) {
                    result.add(exam);
                }
            }
        }

        return result;

    }

    public List<AdHocEvaluation> getAssociatedAdHocEvaluations() {
        final List<AdHocEvaluation> result = new ArrayList<AdHocEvaluation>();

        for (Evaluation evaluation : this.getAssociatedEvaluations()) {
            if (evaluation instanceof AdHocEvaluation) {
                result.add((AdHocEvaluation) evaluation);
            }
        }
        return result;
    }

    public List<AdHocEvaluation> getOrderedAssociatedAdHocEvaluations() {
        List<AdHocEvaluation> associatedAdHocEvaluations = getAssociatedAdHocEvaluations();
        Collections.sort(associatedAdHocEvaluations, AdHocEvaluation.AD_HOC_EVALUATION_CREATION_DATE_COMPARATOR);
        return associatedAdHocEvaluations;
    }

    public boolean functionsAt(final Campus campus) {
        final ExecutionYear executionYear = getExecutionYear();
        for (final CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                if (executionDegree.getCampus() == campus && executionDegree.getExecutionYear() == executionYear) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<DegreeCurricularPlan> getAttendsDegreeCurricularPlans() {
        final Set<DegreeCurricularPlan> dcps = new HashSet<DegreeCurricularPlan>();
        for (final Attends attends : this.getAttendsSet()) {
            dcps.add(attends.getStudentCurricularPlanFromAttends().getDegreeCurricularPlan());
        }
        return dcps;
    }

    public void searchAttends(SearchExecutionCourseAttendsBean attendsBean) {
        check(this, ExecutionCoursePredicates.executionCourseLecturingTeacherOrDegreeCoordinator);
        final Predicate<Attends> filter = attendsBean.getFilters();
        final Collection<Attends> validAttends = new HashSet<Attends>();
        final Map<Integer, Integer> enrolmentNumberMap = new HashMap<Integer, Integer>();
        for (final Attends attends : getAttends()) {
            if (filter.eval(attends)) {
                validAttends.add(attends);
                addAttendsToEnrolmentNumberMap(attends, enrolmentNumberMap);
            }
        }
        attendsBean.setAttendsResult(validAttends);
        attendsBean.setEnrolmentsNumberMap(enrolmentNumberMap);
    }

    public void addAttendsToEnrolmentNumberMap(final Attends attends, Map<Integer, Integer> enrolmentNumberMap) {
        Integer enrolmentsNumber;
        if (attends.getEnrolment() == null) {
            enrolmentsNumber = 0;
        } else {
            enrolmentsNumber =
                    attends.getEnrolment().getNumberOfTotalEnrolmentsInThisCourse(attends.getEnrolment().getExecutionPeriod());
        }

        Integer mapValue = enrolmentNumberMap.get(enrolmentsNumber);
        if (mapValue == null) {
            mapValue = 1;
        } else {
            mapValue += 1;
        }
        enrolmentNumberMap.put(enrolmentsNumber, mapValue);
    }

    public Collection<DegreeCurricularPlan> getAssociatedDegreeCurricularPlans() {
        Collection<DegreeCurricularPlan> result = new HashSet<DegreeCurricularPlan>();
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            result.add(curricularCourse.getDegreeCurricularPlan());
        }
        return result;
    }

    public List<WrittenEvaluation> getAssociatedWrittenEvaluationsForScopeAndContext(List<Integer> curricularYears,
            DegreeCurricularPlan degreeCurricularPlan) {
        List<WrittenEvaluation> result = new ArrayList<WrittenEvaluation>();
        for (WrittenEvaluation writtenEvaluation : getWrittenEvaluations()) {
            if (writtenEvaluation.hasScopeOrContextFor(curricularYears, degreeCurricularPlan)) {
                result.add(writtenEvaluation);
            }
        }
        return result;
    }

    public static List<ExecutionCourse> filterByAcademicIntervalAndDegreeCurricularPlanAndCurricularYearAndName(
            AcademicInterval academicInterval, DegreeCurricularPlan degreeCurricularPlan, CurricularYear curricularYear,
            String name) {

        // FIXME (PERIODS) must be changed when ExecutionCourse is linked to
        // ExecutionInterval
        ExecutionSemester executionSemester = (ExecutionSemester) ExecutionInterval.getExecutionInterval(academicInterval);

        return executionSemester == null ? Collections.EMPTY_LIST : executionSemester
                .getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(degreeCurricularPlan,
                        curricularYear, name);
    }

    public static Collection<ExecutionCourse> filterByAcademicInterval(AcademicInterval academicInterval) {
        // FIXME (PERIODS) must be changed when ExecutionCourse is linked to
        // ExecutionInterval
        ExecutionSemester executionSemester = (ExecutionSemester) ExecutionInterval.getExecutionInterval(academicInterval);

        return executionSemester == null ? Collections.<ExecutionCourse> emptyList() : executionSemester
                .getAssociatedExecutionCoursesSet();
    }

    public static ExecutionCourse getExecutionCourseByInitials(AcademicInterval academicInterval, String courseInitials) {

        // FIXME (PERIODS) must be changed when ExecutionCourse is linked to
        // ExecutionInterval
        ExecutionSemester executionSemester = (ExecutionSemester) ExecutionInterval.getExecutionInterval(academicInterval);
        return executionSemester.getExecutionCourseByInitials(courseInitials);
    }

    public static List<ExecutionCourse> searchByAcademicIntervalAndExecutionDegreeYearAndName(AcademicInterval academicInterval,
            ExecutionDegree executionDegree, CurricularYear curricularYear, String name) {

        // FIXME (PERIODS) must be changed when ExecutionCourse is linked to
        // ExecutionInterval
        ExecutionSemester executionSemester = (ExecutionSemester) ExecutionInterval.getExecutionInterval(academicInterval);

        return executionSemester.getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(
                executionDegree.getDegreeCurricularPlan(), curricularYear, name);
    }

    public StudentInquiriesCourseResult getStudentInquiriesCourseResult(ExecutionDegree executionDegree) {
        for (StudentInquiriesCourseResult studentInquiriesCourseResult : getStudentInquiriesCourseResults()) {
            if (studentInquiriesCourseResult.getExecutionDegree() == executionDegree) {
                return studentInquiriesCourseResult;
            }
        }
        return null;
    }

    public int getAnsweredTeachingInquiriesCount() {
        int answeredTeachingInquiries = 0;
        for (Professorship professorship : getProfessorships()) {
            if (professorship.hasTeachingInquiry()) {
                answeredTeachingInquiries++;
            }
        }
        return answeredTeachingInquiries;
    }

    public TeachingInquiry getResponsibleTeachingInquiry() {
        for (Professorship professorship : getProfessorships()) {
            if (professorship.isResponsibleFor()) {
                return professorship.getTeachingInquiry();
            }
        }
        return null;
    }

    public boolean isSplittable() {
        if (getAssociatedCurricularCoursesSet().size() < 2) {
            return false;
        }
        return true;
    }

    public boolean isDeletable() {
        try {
            return this.canBeDeleted();
        } catch (DomainException e) {
            return false;
        }
        /**
         * TODO: Refazer a implementacao de canBeDeleted()
         */
    }

    public Professorship getProfessorship(final Person person) {
        for (final Professorship professorship : getProfessorshipsSet()) {
            if (professorship.getPerson() == person) {
                return professorship;
            }
        }
        return null;
    }

    public boolean isHasSender() {
        return hasSender();
    }

    /*
     * This method returns the portuguese name and the english name with the
     * rules implemented in getNome() method
     */
    public MultiLanguageString getNameI18N() {
        MultiLanguageString nameI18N = new MultiLanguageString();
        nameI18N = nameI18N.with(Language.pt, super.getNome());

        final StringBuilder stringBuilder = new StringBuilder();

        final Set<String> names = new HashSet<String>();

        for (final CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            if (!curricularCourse.getActiveDegreeModuleScopesInExecutionPeriod(getExecutionPeriod()).isEmpty()) {
                final String name = curricularCourse.getNameEn();
                if (!names.contains(name)) {
                    names.add(name);
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(" / ");
                    }
                    stringBuilder.append(name);
                }
            }
        }

        if (stringBuilder.length() > 0) {
            nameI18N = nameI18N.with(Language.en, stringBuilder.toString());
            return nameI18N;
        }

        boolean unique = true;
        final String nameEn = getAssociatedCurricularCourses().iterator().next().getNameEn();

        for (final CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            if (curricularCourse.getNameEn() == null || !curricularCourse.getNameEn().equals(nameEn)) {
                unique = false;
                break;
            }
        }

        if (unique) {
            nameI18N = nameI18N.with(Language.en, nameEn);
            return nameI18N;
        } else {
            nameI18N = nameI18N.with(Language.en, super.getNome());
            return nameI18N;
        }
    }

    public Professorship getProfessorshipForCurrentUser() {
        return this.getProfessorship(AccessControl.getPerson());
    }

    public List<InquiryResult> getInquiryResultsByExecutionDegreeAndForTeachers(ExecutionDegree executionDegree) {
        List<InquiryResult> results = new ArrayList<InquiryResult>();
        for (InquiryResult inquiryResult : getInquiryResults()) {
            if (executionDegree == inquiryResult.getExecutionDegree()
                    || (inquiryResult.getExecutionDegree() == null && inquiryResult.getProfessorship() == null)) {
                results.add(inquiryResult);
            }
        }
        return results;
    }

    public Boolean canBeSubjectToQucAudit() {
        for (InquiryResult inquiryResult : getInquiryResults()) {
            if (inquiryResult.getExecutionDegree() != null && InquiryResultType.AUDIT.equals(inquiryResult.getResultType())) {
                return true;
            }
        }
        return false;
    }

    public List<InquiryResult> getInquiryResultsByExecutionDegree(ExecutionDegree executionDegree) {
        List<InquiryResult> results = new ArrayList<InquiryResult>();
        for (InquiryResult inquiryResult : getInquiryResults()) {
            if (executionDegree == inquiryResult.getExecutionDegree()) {
                results.add(inquiryResult);
            }
        }
        return results;
    }

    public boolean hasNotRelevantDataFor(ExecutionDegree executionDegree) {
        for (InquiryResult inquiryResult : getInquiryResults()) {
            if (executionDegree == inquiryResult.getExecutionDegree() && inquiryResult.getInquiryQuestion() == null
                    && !inquiryResult.getResultClassification().isRelevantResult()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyEnrolment(ExecutionDegree executionDegree) {
        for (Attends attend : getAttends()) {
            if (attend.hasEnrolment()) {
                StudentCurricularPlan scp = attend.getRegistration().getStudentCurricularPlan(getExecutionPeriod());
                if (scp != null) {
                    ExecutionDegree studentExecutionDegree =
                            scp.getDegreeCurricularPlan().getExecutionDegreeByYearAndCampus(getExecutionYear(),
                                    scp.getCampus(getExecutionYear()));
                    if (studentExecutionDegree == executionDegree) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ResultClassification getForAudit(ExecutionDegree executionDegree) {
        for (InquiryResult inquiryResult : getInquiryResults()) {
            if (inquiryResult.getExecutionDegree() == executionDegree
                    && InquiryResultType.AUDIT.equals(inquiryResult.getResultType())) {
                return inquiryResult.getResultClassification();
            }
        }
        return null;
    }

    public InquiryGlobalComment getInquiryGlobalComment(ExecutionDegree executionDegree) {
        for (InquiryGlobalComment globalComment : getInquiryGlobalComments()) {
            if (globalComment.getExecutionDegree() == executionDegree) {
                return globalComment;
            }
        }
        return null;
    }

    public boolean hasQucGlobalCommentsMadeBy(Person person, ExecutionDegree executionDegree, ResultPersonCategory personCategory) {
        InquiryGlobalComment inquiryGlobalComment = getInquiryGlobalComment(executionDegree);
        if (inquiryGlobalComment != null) {
            for (InquiryResultComment resultComment : inquiryGlobalComment.getInquiryResultCommentsSet()) {
                if (resultComment.getPerson() == person && personCategory.equals(resultComment.getPersonCategory())
                        && !StringUtils.isEmpty(resultComment.getComment())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAvailableForInquiry() {
        return getAvailableForInquiries() && hasEnrolmentsInAnyCurricularCourse() && !isMasterDegreeDFAOnly();
    }

    public boolean hasEnrolmentsInAnyCurricularCourse() {
        for (CurricularCourse curricularCourse : getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.hasEnrolmentForPeriod(getExecutionPeriod())) {
                return true;
            }
        }
        return false;
    }

    public boolean getHasExecutionCourseAudit() {
        return hasExecutionCourseAudit();
    }

    @Atomic
    public Boolean deleteInquiryResults() {
        boolean deletedResults = false;
        for (InquiryResult inquiryResult : getInquiryResultsSet()) {
            if (inquiryResult.getProfessorship() == null) { // delete only the direct EC results
                inquiryResult.delete();
                deletedResults = true;
            }
        }
        return deletedResults;
    }

    @Atomic
    public Boolean deleteInquiryResults(ExecutionDegree executionDegree, InquiryQuestion inquiryQuestion) {
        boolean deletedResults = false;
        for (InquiryResult inquiryResult : getInquiryResultsByExecutionDegree(executionDegree)) {
            if ((inquiryQuestion == null || inquiryResult.getInquiryQuestion() == inquiryQuestion)
                    && inquiryResult.getProfessorship() == null) { // delete only the direct EC results
                inquiryResult.delete();
                deletedResults = true;
            }
        }
        return deletedResults;
    }

    @Atomic
    public Boolean deleteAllTeachersResults() {
        boolean deletedResults = false;
        for (InquiryResult inquiryResult : getInquiryResultsSet()) {
            if (inquiryResult.getProfessorship() != null) {
                inquiryResult.delete();
                deletedResults = true;
            }
        }
        return deletedResults;
    }

    public int getEnrolmentCount() {
        int result = 0;
        for (final Attends attends : getAttendsSet()) {
            if (attends.hasEnrolment()) {
                result++;
            }
        }
        return result;
    }

    public boolean isDissertation() {
        for (CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            if (curricularCourse.isDissertation()) {
                return true;
            }
        }
        return false;
    }

    @Atomic
    public void changeProjectTutorialCourse() {
        setProjectTutorialCourse(!getProjectTutorialCourse());
    }

    @Override
    public void addAssociatedCurricularCourses(final CurricularCourse curricularCourse) {
        Collection<ExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCourses();

        for (ExecutionCourse executionCourse : executionCourses) {
            if (this != executionCourse && executionCourse.getExecutionPeriod() == getExecutionPeriod()) {
                throw new DomainException("error.executionCourse.curricularCourse.already.associated");
            }
        }

        super.addAssociatedCurricularCourses(curricularCourse);
    }

    @Atomic
    public void associateCurricularCourse(final CurricularCourse curricularCourse) {
        addAssociatedCurricularCourses(curricularCourse);
    }

    @Atomic
    public void dissociateCurricularCourse(final CurricularCourse curricularCourse) {
        super.removeAssociatedCurricularCourses(curricularCourse);
    }

    @Atomic
    public static ExecutionCourse createExecutionCourse(final ExecutionCourseManagementBean bean) {
        final ExecutionSemester executionSemester = bean.getSemester();

        final ExecutionCourse existentExecutionCourse = executionSemester.getExecutionCourseByInitials(bean.getAcronym().trim());
        if (existentExecutionCourse != null) {
            throw new DomainException("error.executionCourse.with.acronym.for.semester.exists");
        }

        final ExecutionCourse executionCourse =
                new ExecutionCourse(bean.getName(), bean.getAcronym().trim(), executionSemester, bean.getEntryPhase());

        if (!bean.getCurricularCourseList().isEmpty()) {
            for (CurricularCourse curricularCourse : bean.getCurricularCourseList()) {
                executionCourse.associateCurricularCourse(curricularCourse);
            }
        }

        executionCourse.createSite();

        return executionCourse;
    }

    public Double getEctsCredits() {
        Double ects = null;
        for (CurricularCourse curricularCourse : getAssociatedCurricularCourses()) {
            if (curricularCourse.isActive(getExecutionPeriod())) {
                if (ects == null) {
                    ects = curricularCourse.getEctsCredits();
                } else if (!ects.equals(curricularCourse.getEctsCredits())) {
                    throw new DomainException("error.invalid.ectsCredits");
                }
            }
        }
        return ects;
    }

    public Set<OccupationPeriod> getLessonPeriods() {
        final Set<OccupationPeriod> result = new TreeSet<OccupationPeriod>(new Comparator<OccupationPeriod>() {
            @Override
            public int compare(final OccupationPeriod op1, final OccupationPeriod op2) {
                final int i = op1.getPeriodInterval().getStart().compareTo(op2.getPeriodInterval().getStart());
                return i == 0 ? op1.getExternalId().compareTo(op2.getExternalId()) : i;
            }
        });
        for (final ExecutionDegree executionDegree : getExecutionDegrees()) {
            result.add(executionDegree.getPeriodLessons(getExecutionPeriod()));
        }
        return result;
    }

    public boolean getHasAnnouncements() {
        ExecutionCourseAnnouncementBoard board = getBoard();
        if (board != null) {
            return !board.getAnnouncements().isEmpty();
        }
        return false;
    }

//  TODO in the new version of the framework, this bug (when creating an object the relations come allwats empty) 
//  will be corrected and should be uncommented   
//  @ConsistencyPredicate
//  public boolean checkFinalEvaluation() {
//      for (Evaluation evaluation : getAssociatedEvaluationsSet()) {
//          if (evaluation instanceof FinalEvaluation) {
//              return true;
//          }
//      }
//      return false;
//  }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryGlobalComment> getInquiryGlobalComments() {
        return getInquiryGlobalCommentsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryGlobalComments() {
        return !getInquiryGlobalCommentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.StudentInquiryRegistry> getStudentsInquiryRegistries() {
        return getStudentsInquiryRegistriesSet();
    }

    @Deprecated
    public boolean hasAnyStudentsInquiryRegistries() {
        return !getStudentsInquiryRegistriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult> getStudentInquiriesCourseResults() {
        return getStudentInquiriesCourseResultsSet();
    }

    @Deprecated
    public boolean hasAnyStudentInquiriesCourseResults() {
        return !getStudentInquiriesCourseResultsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Attends> getAttends() {
        return getAttendsSet();
    }

    @Deprecated
    public boolean hasAnyAttends() {
        return !getAttendsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.Metadata> getMetadatas() {
        return getMetadatasSet();
    }

    @Deprecated
    public boolean hasAnyMetadatas() {
        return !getMetadatasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.YearDelegateCourseInquiry> getYearDelegateCourseInquiries() {
        return getYearDelegateCourseInquiriesSet();
    }

    @Deprecated
    public boolean hasAnyYearDelegateCourseInquiries() {
        return !getYearDelegateCourseInquiriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.classProperties.ExecutionCourseProperty> getExecutionCourseProperties() {
        return getExecutionCoursePropertiesSet();
    }

    @Deprecated
    public boolean hasAnyExecutionCourseProperties() {
        return !getExecutionCoursePropertiesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExportGrouping> getExportGroupings() {
        return getExportGroupingsSet();
    }

    @Deprecated
    public boolean hasAnyExportGroupings() {
        return !getExportGroupingsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CurricularCourse> getAssociatedCurricularCourses() {
        return getAssociatedCurricularCoursesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedCurricularCourses() {
        return !getAssociatedCurricularCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiriesRegistry> getAssociatedInquiriesRegistries() {
        return getAssociatedInquiriesRegistriesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedInquiriesRegistries() {
        return !getAssociatedInquiriesRegistriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionCourseLog> getExecutionCourseLogs() {
        return getExecutionCourseLogsSet();
    }

    @Deprecated
    public boolean hasAnyExecutionCourseLogs() {
        return !getExecutionCourseLogsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Professorship> getProfessorships() {
        return getProfessorshipsSet();
    }

    @Deprecated
    public boolean hasAnyProfessorships() {
        return !getProfessorshipsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.NonAffiliatedTeacher> getNonAffiliatedTeachers() {
        return getNonAffiliatedTeachersSet();
    }

    @Deprecated
    public boolean hasAnyNonAffiliatedTeachers() {
        return !getNonAffiliatedTeachersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.BibliographicReference> getAssociatedBibliographicReferences() {
        return getAssociatedBibliographicReferencesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedBibliographicReferences() {
        return !getAssociatedBibliographicReferencesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.LessonPlanning> getLessonPlannings() {
        return getLessonPlanningsSet();
    }

    @Deprecated
    public boolean hasAnyLessonPlannings() {
        return !getLessonPlanningsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Evaluation> getAssociatedEvaluations() {
        return getAssociatedEvaluationsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedEvaluations() {
        return !getAssociatedEvaluationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Summary> getAssociatedSummaries() {
        return getAssociatedSummariesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedSummaries() {
        return !getAssociatedSummariesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.tests.NewTestGroup> getTestGroups() {
        return getTestGroupsSet();
    }

    @Deprecated
    public boolean hasAnyTestGroups() {
        return !getTestGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiriesCourse> getAssociatedInquiriesCourses() {
        return getAssociatedInquiriesCoursesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedInquiriesCourses() {
        return !getAssociatedInquiriesCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CourseLoad> getCourseLoads() {
        return getCourseLoadsSet();
    }

    @Deprecated
    public boolean hasAnyCourseLoads() {
        return !getCourseLoadsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryCourseAnswer> getInquiryCourseAnswers() {
        return getInquiryCourseAnswersSet();
    }

    @Deprecated
    public boolean hasAnyInquiryCourseAnswers() {
        return !getInquiryCourseAnswersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExportGrouping> getGroupingSenderExecutionCourse() {
        return getGroupingSenderExecutionCourseSet();
    }

    @Deprecated
    public boolean hasAnyGroupingSenderExecutionCourse() {
        return !getGroupingSenderExecutionCourseSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryDelegateAnswer> getInquiryDelegatesAnswers() {
        return getInquiryDelegatesAnswersSet();
    }

    @Deprecated
    public boolean hasAnyInquiryDelegatesAnswers() {
        return !getInquiryDelegatesAnswersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryResult> getInquiryResults() {
        return getInquiryResultsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryResults() {
        return !getInquiryResultsSet().isEmpty();
    }

    @Deprecated
    public boolean hasEntryPhase() {
        return getEntryPhase() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSite() {
        return getSite() != null;
    }

    @Deprecated
    public boolean hasUnitCreditValueNotes() {
        return getUnitCreditValueNotes() != null;
    }

    @Deprecated
    public boolean hasUnitCreditValue() {
        return getUnitCreditValue() != null;
    }

    @Deprecated
    public boolean hasComment() {
        return getComment() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasEffortRate() {
        return getEffortRate() != null;
    }

    @Deprecated
    public boolean hasExecutionCourseAudit() {
        return getExecutionCourseAudit() != null;
    }

    @Deprecated
    public boolean hasProjectTutorialCourse() {
        return getProjectTutorialCourse() != null;
    }

    @Deprecated
    public boolean hasSender() {
        return getSender() != null;
    }

    @Deprecated
    public boolean hasTestScope() {
        return getTestScope() != null;
    }

    @Deprecated
    public boolean hasAvailableForInquiries() {
        return getAvailableForInquiries() != null;
    }

    @Deprecated
    public boolean hasSigla() {
        return getSigla() != null;
    }

    @Deprecated
    public boolean hasCourseReport() {
        return getCourseReport() != null;
    }

    @Deprecated
    public boolean hasBoard() {
        return getBoard() != null;
    }

    @Deprecated
    public boolean hasVigilantGroup() {
        return getVigilantGroup() != null;
    }

    @Deprecated
    public boolean hasEvaluationMethod() {
        return getEvaluationMethod() != null;
    }

    @Deprecated
    public boolean hasNome() {
        return getNome() != null;
    }

    @Deprecated
    public boolean hasAvailableGradeSubmission() {
        return getAvailableGradeSubmission() != null;
    }

}
