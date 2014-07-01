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
package net.sourceforge.fenixedu.domain;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
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
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

/**
 * @author João Mota
 */
public class Professorship extends Professorship_Base implements ICreditsEventOriginator {

    public static final Comparator<Professorship> COMPARATOR_BY_PERSON_NAME = new BeanComparator("person.name",
            Collator.getInstance());

    public Professorship() {
        super();
        setRootDomainObject(Bennu.getInstance());
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

    @Atomic
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

        ProfessorshipManagementLog.createLog(professorShip.getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.professorship.added", professorShip.getPerson().getPresentationName(), professorShip
                        .getExecutionCourse().getNome(), professorShip.getExecutionCourse().getDegreePresentationString());
        return professorShip;
    }

    public void delete() {
        if (canBeDeleted()) {
            ProfessorshipManagementLog.createLog(getExecutionCourse(), Bundle.MESSAGING,
                    "log.executionCourse.professorship.removed", getPerson().getPresentationName(), getExecutionCourse()
                            .getNome(), getExecutionCourse().getDegreePresentationString());
            setExecutionCourse(null);
            setPerson(null);
            if (super.getPermissions() != null) {
                getPermissions().delete();
            }
            setRootDomainObject(null);
            setCreator(null);
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
        setPerson(null);
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
        Collection<InquiryResult> inquiryResults = getInquiryResults();
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
            Collection<InquiryResult> inquiryResults = professorship.getInquiryResults();
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

    @Atomic
    public Boolean deleteInquiryResults() {
        boolean deletedResults = false;
        for (InquiryResult inquiryResult : getInquiryResultsSet()) {
            inquiryResult.delete();
            deletedResults = true;
        }
        return deletedResults;
    }

    @Atomic
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
            int associatedLessonsCount = degreeTeachingService.getShift().getAssociatedLessonsSet().size();
            if (associatedLessonsCount == 0) {
                lessonNumber += 1;
            }
            lessonNumber += associatedLessonsCount;
        }
        if (lessonNumber == 0) {
            lessonNumber += 1;
        }
        lessonNumber += getSupportLessonsSet().size();
        return lessonNumber;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryStudentTeacherAnswer> getInquiryStudentTeacherAnswers() {
        return getInquiryStudentTeacherAnswersSet();
    }

    @Deprecated
    public boolean hasAnyInquiryStudentTeacherAnswers() {
        return !getInquiryStudentTeacherAnswersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.NonRegularTeachingService> getNonRegularTeachingServices() {
        return getNonRegularTeachingServicesSet();
    }

    @Deprecated
    public boolean hasAnyNonRegularTeachingServices() {
        return !getNonRegularTeachingServicesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.DegreeProjectTutorialService> getDegreeProjectTutorialServices() {
        return getDegreeProjectTutorialServicesSet();
    }

    @Deprecated
    public boolean hasAnyDegreeProjectTutorialServices() {
        return !getDegreeProjectTutorialServicesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.DegreeTeachingServiceCorrection> getDegreeTeachingServiceCorrections() {
        return getDegreeTeachingServiceCorrectionsSet();
    }

    @Deprecated
    public boolean hasAnyDegreeTeachingServiceCorrections() {
        return !getDegreeTeachingServiceCorrectionsSet().isEmpty();
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
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.TeacherMasterDegreeService> getTeacherMasterDegreeServices() {
        return getTeacherMasterDegreeServicesSet();
    }

    @Deprecated
    public boolean hasAnyTeacherMasterDegreeServices() {
        return !getTeacherMasterDegreeServicesSet().isEmpty();
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
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult> getStudentInquiriesTeachingResults() {
        return getStudentInquiriesTeachingResultsSet();
    }

    @Deprecated
    public boolean hasAnyStudentInquiriesTeachingResults() {
        return !getStudentInquiriesTeachingResultsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ShiftProfessorship> getAssociatedShiftProfessorship() {
        return getAssociatedShiftProfessorshipSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedShiftProfessorship() {
        return !getAssociatedShiftProfessorshipSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiriesTeacher> getAssociatedInquiriesTeacher() {
        return getAssociatedInquiriesTeacherSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedInquiriesTeacher() {
        return !getAssociatedInquiriesTeacherSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.SupportLesson> getSupportLessons() {
        return getSupportLessonsSet();
    }

    @Deprecated
    public boolean hasAnySupportLessons() {
        return !getSupportLessonsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService> getDegreeTeachingServices() {
        return getDegreeTeachingServicesSet();
    }

    @Deprecated
    public boolean hasAnyDegreeTeachingServices() {
        return !getDegreeTeachingServicesSet().isEmpty();
    }

    @Deprecated
    public boolean hasInquiryRegentAnswer() {
        return getInquiryRegentAnswer() != null;
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasInquiryTeacherAnswer() {
        return getInquiryTeacherAnswer() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCreator() {
        return getCreator() != null;
    }

    @Deprecated
    public boolean hasHours() {
        return getHours() != null;
    }

    @Deprecated
    public boolean hasTeachingInquiry() {
        return getTeachingInquiry() != null;
    }

    @Deprecated
    public boolean hasResponsibleFor() {
        return getResponsibleFor() != null;
    }

    @Deprecated
    public boolean hasPermissions() {
        return getPermissions() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
