package net.sourceforge.fenixedu.domain;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriodType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultType;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiryResponsePeriod;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Mota
 */
public class Professorship extends Professorship_Base implements ICreditsEventOriginator {

    public static final Comparator<Professorship> COMPARATOR_BY_PERSON_NAME = new BeanComparator("person.name",
            Collator.getInstance());

    public Professorship() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        new ProfessorshipPermissions(this);
    }

    @Override
    public boolean belongsToExecutionPeriod(ExecutionSemester executionSemester) {
        return this.getExecutionCourse().getExecutionPeriod().equals(executionSemester);
    }

    public static Professorship create(Boolean responsibleFor, ExecutionCourse executionCourse, Teacher teacher, Double hours)
            throws MaxResponsibleForExceed, InvalidCategory {

        for (final Professorship otherProfessorship : executionCourse.getProfessorshipsSet()) {
            if (teacher == otherProfessorship.getTeacher()) {
                throw new DomainException("error.teacher.already.associated.to.professorship");
            }
        }

        if (responsibleFor == null || executionCourse == null || teacher == null) {
            throw new NullPointerException();
        }

        Professorship professorShip = new Professorship();
        professorShip.setHours((hours == null) ? new Double(0.0) : hours);
        professorShip.setExecutionCourse(executionCourse);
        professorShip.setPerson(teacher.getPerson());
        professorShip.setCreator(AccessControl.getPerson());

        professorShip.setResponsibleFor(responsibleFor);
        executionCourse.moveSummariesFromTeacherToProfessorship(teacher, professorShip);

        return professorShip;
    }

    @Service
    public static Professorship create(Boolean responsibleFor, ExecutionCourse executionCourse, Person person, Double hours)
            throws MaxResponsibleForExceed, InvalidCategory {

        for (final Professorship otherProfessorship : executionCourse.getProfessorshipsSet()) {
            if (person == otherProfessorship.getPerson()) {
                throw new DomainException("error.teacher.already.associated.to.professorship");
            }
        }

        if (responsibleFor == null || executionCourse == null || person == null) {
            throw new NullPointerException();
        }

        Professorship professorShip = new Professorship();
        professorShip.setHours((hours == null) ? new Double(0.0) : hours);
        professorShip.setExecutionCourse(executionCourse);
        professorShip.setPerson(person);
        professorShip.setCreator(AccessControl.getPerson());

        if (responsibleFor.booleanValue() && professorShip.getPerson().getTeacher() != null) {
            ResponsibleForValidator.getInstance().validateResponsibleForList(professorShip.getPerson().getTeacher(),
                    professorShip.getExecutionCourse(), professorShip);
            professorShip.setResponsibleFor(Boolean.TRUE);
        } else {
            professorShip.setResponsibleFor(Boolean.FALSE);
        }
        if (person.getTeacher() != null) {
            executionCourse.moveSummariesFromTeacherToProfessorship(person.getTeacher(), professorShip);
        }

        ProfessorshipManagementLog.createLog(professorShip.getExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.professorship.added", professorShip.getPerson().getPresentationName(), professorShip
                        .getExecutionCourse().getNome(), professorShip.getExecutionCourse().getDegreePresentationString());
        return professorShip;
    }

    public void delete() {
        if (canBeDeleted()) {
            ProfessorshipManagementLog.createLog(getExecutionCourse(), "resources.MessagingResources",
                    "log.executionCourse.professorship.removed", getPerson().getPresentationName(), getExecutionCourse()
                            .getNome(), getExecutionCourse().getDegreePresentationString());
            removeExecutionCourse();
            removePerson();
            if (super.getPermissions() != null) {
                getPermissions().delete();
            }
            removeRootDomainObject();
            removeCreator();
            deleteDomainObject();
        }
    }

    public boolean canBeDeleted() {
        if (hasAnyAssociatedSummaries()) {
            throw new DomainException("error.remove.professorship.hasAnyAssociatedSummaries");
        }
        if (hasAnyAssociatedShiftProfessorship()) {
            throw new DomainException("error.remove.professorship.hasAnyAssociatedShiftProfessorship");
        }
        if (hasAnySupportLessons()) {
            throw new DomainException("error.remove.professorship.hasAnySupportLessons");
        }
        if (hasAnyDegreeTeachingServices()) {
            throw new DomainException("error.remove.professorship.hasAnyDegreeTeachingServices");
        }
        if (hasAnyTeacherMasterDegreeServices()) {
            throw new DomainException("error.remove.professorship.hasAnyTeacherMasterDegreeServices");
        }
        if (hasTeachingInquiry()) {
            throw new DomainException("error.remove.professorship.hasTeachingInquiry");
        }
        if (hasAnyStudentInquiriesTeachingResults()) {
            throw new DomainException("error.remove.professorship.hasAnyStudentInquiriesTeachingResults");
        }
        if (hasAnyInquiryStudentTeacherAnswers()) {
            throw new DomainException("error.remove.professorship.hasAnyInquiryStudentTeacherAnswers");
        }
        if (hasAnyInquiryResults()) {
            throw new DomainException("error.remove.professorship.hasAnyInquiryResults");
        }
        if (hasInquiryTeacherAnswer()) {
            throw new DomainException("error.remove.professorship.hasInquiryTeacherAnswer");
        }
        if (hasInquiryRegentAnswer()) {
            throw new DomainException("error.remove.professorship.hasInquiryRegentAnswer");
        }
        if (hasAnyDegreeProjectTutorialServices()) {
            throw new DomainException("error.remove.professorship.hasAnyDegreeProjectTutorialServices");
        }
        return true;
    }

    public boolean isResponsibleFor() {
        return getResponsibleFor().booleanValue();
    }

    public static List<Professorship> readByDegreeCurricularPlanAndExecutionYear(DegreeCurricularPlan degreeCurricularPlan,
            ExecutionYear executionYear) {

        Set<Professorship> professorships = new HashSet<Professorship>();
        for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
            for (ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionYear(executionYear)) {
                professorships.addAll(executionCourse.getProfessorships());
            }
        }
        return new ArrayList<Professorship>(professorships);
    }

    public static List<Professorship> readByDegreeCurricularPlanAndExecutionYearAndBasic(
            DegreeCurricularPlan degreeCurricularPlan, ExecutionYear executionYear, Boolean basic) {

        Set<Professorship> professorships = new HashSet<Professorship>();
        for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
            if (curricularCourse.getBasic().equals(basic)) {
                for (ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionYear(executionYear)) {
                    professorships.addAll(executionCourse.getProfessorships());
                }
            }
        }
        return new ArrayList<Professorship>(professorships);
    }

    public static List<Professorship> readByDegreeCurricularPlanAndExecutionPeriod(DegreeCurricularPlan degreeCurricularPlan,
            ExecutionSemester executionSemester) {

        Set<Professorship> professorships = new HashSet<Professorship>();
        for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
            for (ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester)) {
                professorships.addAll(executionCourse.getProfessorships());
            }
        }
        return new ArrayList<Professorship>(professorships);
    }

    public static List<Professorship> readByDegreeCurricularPlansAndExecutionYearAndBasic(
            List<DegreeCurricularPlan> degreeCurricularPlans, ExecutionYear executionYear, Boolean basic) {

        Set<Professorship> professorships = new HashSet<Professorship>();
        for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
            for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
                if (curricularCourse.getBasic() == null || curricularCourse.getBasic().equals(basic)) {
                    if (executionYear != null) {
                        for (ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionYear(executionYear)) {
                            professorships.addAll(executionCourse.getProfessorships());
                        }
                    } else {
                        for (ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
                            professorships.addAll(executionCourse.getProfessorships());
                        }
                    }
                }
            }
        }
        return new ArrayList<Professorship>(professorships);
    }

    public static List<Professorship> readByDegreeCurricularPlansAndExecutionYear(
            List<DegreeCurricularPlan> degreeCurricularPlans, ExecutionYear executionYear) {

        Set<Professorship> professorships = new HashSet<Professorship>();
        for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
            for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
                if (executionYear != null) {
                    for (ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionYear(executionYear)) {
                        professorships.addAll(executionCourse.getProfessorships());
                    }
                } else {
                    for (ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
                        professorships.addAll(executionCourse.getProfessorships());
                    }
                }
            }
        }
        return new ArrayList<Professorship>(professorships);
    }

    public SortedSet<DegreeTeachingService> getDegreeTeachingServicesOrderedByShift() {
        final SortedSet<DegreeTeachingService> degreeTeachingServices =
                new TreeSet<DegreeTeachingService>(DegreeTeachingService.DEGREE_TEACHING_SERVICE_COMPARATOR_BY_SHIFT);
        degreeTeachingServices.addAll(getDegreeTeachingServicesSet());
        return degreeTeachingServices;
    }

    public DegreeTeachingService getDegreeTeachingServiceByShift(Shift shift) {
        for (DegreeTeachingService degreeTeachingService : getDegreeTeachingServicesSet()) {
            if (degreeTeachingService.getShift() == shift) {
                return degreeTeachingService;
            }
        }
        return null;
    }

    public SortedSet<SupportLesson> getSupportLessonsOrderedByStartTimeAndWeekDay() {
        final SortedSet<SupportLesson> supportLessons =
                new TreeSet<SupportLesson>(SupportLesson.SUPPORT_LESSON_COMPARATOR_BY_HOURS_AND_WEEK_DAY);
        supportLessons.addAll(getSupportLessonsSet());
        return supportLessons;
    }

    public boolean isTeachingInquiriesToAnswer() {
        final ExecutionCourse executionCourse = this.getExecutionCourse();
        final InquiryResponsePeriod responsePeriod =
                executionCourse.getExecutionPeriod().getInquiryResponsePeriod(InquiryResponsePeriodType.TEACHING);
        if (responsePeriod == null || !responsePeriod.isOpen() || !executionCourse.isAvailableForInquiry()
                || executionCourse.getStudentInquiriesCourseResults().isEmpty()
                || (!isResponsibleFor() && !hasAssociatedLessonsInTeachingServices())) {
            return false;
        }

        return true;
    }

    public StudentInquiriesTeachingResult getStudentInquiriesTeachingResult(final ExecutionDegree executionDegree,
            final ShiftType shiftType) {
        for (StudentInquiriesTeachingResult result : getStudentInquiriesTeachingResults()) {
            if (result.getExecutionDegree() == executionDegree && result.getShiftType() == shiftType) {
                return result;
            }
        }
        return null;
    }

    public boolean hasAssociatedLessonsInTeachingServices() {
        for (final DegreeTeachingService degreeTeachingService : getDegreeTeachingServicesSet()) {
            if (!degreeTeachingService.getShift().getAssociatedLessons().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public Teacher getTeacher() {
        return getPerson().getTeacher();
    }

    public void setTeacher(Teacher teacher) {
        setPerson(teacher.getPerson());
    }

    public boolean hasTeacher() {
        return hasPerson() && getPerson().hasTeacher();
    }

    public void removeTeacher() {
        removePerson();
    }

    public String getDegreeSiglas() {
        Set<String> degreeSiglas = new HashSet<String>();
        for (CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCourses()) {
            degreeSiglas.add(curricularCourse.getDegreeCurricularPlan().getDegree().getSigla());
        }
        return StringUtils.join(degreeSiglas, ", ");
    }

    public String getDegreePlanNames() {
        Set<String> degreeSiglas = new HashSet<String>();
        for (CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCourses()) {
            degreeSiglas.add(curricularCourse.getDegreeCurricularPlan().getName());
        }
        return StringUtils.join(degreeSiglas, ", ");
    }

    public List<InquiryResult> getInquiryResults(ShiftType shiftType) {
        List<InquiryResult> inquiryResults = new ArrayList<InquiryResult>();
        for (InquiryResult inquiryResult : getInquiryResultsSet()) {
            if (inquiryResult.getShiftType().equals(shiftType)) {
                inquiryResults.add(inquiryResult);
            }
        }
        return inquiryResults;
    }

    public boolean hasMandatoryCommentsToMake() {
        List<InquiryResult> inquiryResults = getInquiryResults();
        for (InquiryResult inquiryResult : inquiryResults) {
            if (inquiryResult.getResultClassification() != null) {
                if (inquiryResult.getResultClassification().isMandatoryComment()
                        && !inquiryResult.getInquiryQuestion().isResultQuestion(inquiryResult.getExecutionPeriod())) {
                    InquiryResultComment inquiryResultComment =
                            inquiryResult.getInquiryResultComment(getPerson(), ResultPersonCategory.TEACHER);
                    if (inquiryResultComment == null || StringUtils.isEmpty(inquiryResultComment.getComment())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean hasMandatoryCommentsToMakeAsResponsible() {
        for (Professorship professorship : getExecutionCourse().getProfessorships()) {
            List<InquiryResult> inquiryResults = professorship.getInquiryResults();
            for (InquiryResult inquiryResult : inquiryResults) {
                if (inquiryResult.getResultClassification() != null) {
                    if (inquiryResult.getResultClassification().isMandatoryComment()
                            && !inquiryResult.getInquiryQuestion().isResultQuestion(inquiryResult.getExecutionPeriod())) {
                        InquiryResultComment inquiryResultComment =
                                inquiryResult.getInquiryResultComment(getPerson(), ResultPersonCategory.REGENT);
                        if (inquiryResultComment == null || StringUtils.isEmpty(inquiryResultComment.getComment())) {
                            inquiryResultComment =
                                    inquiryResult.getInquiryResultComment(getPerson(), ResultPersonCategory.TEACHER);
                            if (inquiryResultComment == null || StringUtils.isEmpty(inquiryResultComment.getComment())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean hasResultsToImprove() {
        for (InquiryResult inquiryResult : getInquiryResults()) {
            if (InquiryResultType.TEACHER_SHIFT_TYPE.equals(inquiryResult.getResultType())
                    && inquiryResult.getResultClassification().isMandatoryComment()) {
                return true;
            }
        }
        return false;
    }

    @Service
    public Boolean deleteInquiryResults() {
        boolean deletedResults = false;
        for (InquiryResult inquiryResult : getInquiryResultsSet()) {
            inquiryResult.delete();
            deletedResults = true;
        }
        return deletedResults;
    }

    @Service
    public Boolean deleteInquiryResults(ShiftType shiftType, InquiryQuestion inquiryQuestion) {
        boolean deletedResults = false;
        for (InquiryResult inquiryResult : getInquiryResults(shiftType)) {
            if (inquiryQuestion == null || inquiryResult.getInquiryQuestion() == inquiryQuestion) {
                inquiryResult.delete();
                deletedResults = true;
            }
        }
        return deletedResults;
    }

    public int getDegreeTeachingServiceLessonRows() {
        int lessonNumber = 0;
        for (DegreeTeachingService degreeTeachingService : getDegreeTeachingServicesSet()) {
            int associatedLessonsCount = degreeTeachingService.getShift().getAssociatedLessonsCount();
            if (associatedLessonsCount == 0) {
                lessonNumber += 1;
            }
            lessonNumber += associatedLessonsCount;
        }
        lessonNumber += getSupportLessonsCount();
        return lessonNumber;
    }

}
