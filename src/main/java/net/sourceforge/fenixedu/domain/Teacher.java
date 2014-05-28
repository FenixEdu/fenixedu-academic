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

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalExemption;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import net.sourceforge.fenixedu.domain.teacher.DegreeProjectTutorialService;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.Orientation;
import net.sourceforge.fenixedu.domain.teacher.PublicationsNumber;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.util.OrientationType;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.PublicationType;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;

public class Teacher extends Teacher_Base {

    public static final Comparator<Teacher> TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER = new Comparator<Teacher>() {

        @Override
        public int compare(Teacher teacher1, Teacher teacher2) {
            final int teacherIdCompare = teacher1.getPerson().getIstUsername().compareTo(teacher2.getPerson().getIstUsername());

            if (teacher1.getCategory() == null && teacher2.getCategory() == null) {
                return teacherIdCompare;
            } else if (teacher1.getCategory() == null) {
                return 1;
            } else if (teacher2.getCategory() == null) {
                return -1;
            } else {
                final int categoryCompare = teacher1.getCategory().compareTo(teacher2.getCategory());
                return categoryCompare == 0 ? teacherIdCompare : categoryCompare;
            }
        }

    };

    public Teacher(Person person) {
        super();
        setPerson(person);
        setRootDomainObject(Bennu.getInstance());
    }

    public String getTeacherId() {
        return getPerson().getIstUsername();
    }

    public static Teacher readByIstId(String istId) {
        User user = User.findByUsername(istId);
        if (user != null) {
            return user.getPerson().getTeacher();
        } else {
            return null;
        }
    }

    @Override
    public void setPerson(Person person) {
        if (person == null) {
            throw new DomainException("error.teacher.no.person");
        }
        super.setPerson(person);
    }

    /***************************************************************************
     * BUSINESS SERVICES *
     **************************************************************************/

    public List<Professorship> responsibleFors() {
        final List<Professorship> result = new ArrayList<Professorship>();
        for (final Professorship professorship : this.getProfessorships()) {
            if (professorship.isResponsibleFor()) {
                result.add(professorship);
            }
        }
        return result;
    }

    public Professorship isResponsibleFor(ExecutionCourse executionCourse) {
        for (final Professorship professorship : this.getProfessorships()) {
            if (professorship.getResponsibleFor() && professorship.getExecutionCourse() == executionCourse) {
                return professorship;
            }
        }
        return null;
    }

    public void updateResponsabilitiesFor(String executionYearId, List<Integer> executionCourses) throws MaxResponsibleForExceed,
            InvalidCategory {

        if (executionYearId == null || executionCourses == null) {
            throw new NullPointerException();
        }

        boolean responsible;
        for (final Professorship professorship : this.getProfessorships()) {
            final ExecutionCourse executionCourse = professorship.getExecutionCourse();
            if (executionCourse.getExecutionPeriod().getExecutionYear().getExternalId().equals(executionYearId)) {
                responsible = executionCourses.contains(executionCourse.getExternalId());
                if (!professorship.getResponsibleFor().equals(Boolean.valueOf(responsible))) {
                    ResponsibleForValidator.getInstance().validateResponsibleForList(this, executionCourse, professorship);
                    professorship.setResponsibleFor(responsible);
                }
            }
        }
    }

    public Unit getCurrentWorkingUnit() {
        Employee employee = this.getPerson().getEmployee();
        return (employee != null) ? employee.getCurrentWorkingPlace() : null;
    }

    public Unit getLastWorkingUnit() {
        Employee employee = this.getPerson().getEmployee();
        return (employee != null) ? employee.getLastWorkingPlace() : null;
    }

    public Unit getLastWorkingUnit(YearMonthDay begin, YearMonthDay end) {
        Employee employee = this.getPerson().getEmployee();
        return (employee != null) ? employee.getLastWorkingPlace(begin, end) : null;
    }

    public Department getCurrentWorkingDepartment() {
        Employee employee = this.getPerson().getEmployee();
        if (employee != null) {
            Department currentDepartmentWorkingPlace = employee.getCurrentDepartmentWorkingPlace();
            if (currentDepartmentWorkingPlace != null) {
                return currentDepartmentWorkingPlace;
            }
        }

        TeacherAuthorization teacherAuthorization = getTeacherAuthorization(ExecutionSemester.readActualExecutionSemester());
        return teacherAuthorization != null && teacherAuthorization instanceof ExternalTeacherAuthorization ? ((ExternalTeacherAuthorization) teacherAuthorization)
                .getDepartment() : null;
    }

    public Department getLastWorkingDepartment(YearMonthDay begin, YearMonthDay end) {
        Employee employee = this.getPerson().getEmployee();
        if (employee != null) {
            Department lastDepartmentWorkingPlace = employee.getLastDepartmentWorkingPlace(begin, end);
            if (lastDepartmentWorkingPlace != null) {
                return lastDepartmentWorkingPlace;
            }

        }
        List<ExecutionSemester> executionSemesters =
                ExecutionSemester.readExecutionPeriodsInTimePeriod(begin.toLocalDate(), end.toLocalDate());
        Collections.sort(executionSemesters, new ReverseComparator(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR));
        for (ExecutionSemester executionSemester : executionSemesters) {
            TeacherAuthorization teacherAuthorization = getTeacherAuthorization(executionSemester);
            if (teacherAuthorization != null && teacherAuthorization instanceof ExternalTeacherAuthorization) {
                return ((ExternalTeacherAuthorization) teacherAuthorization).getDepartment();
            }
        }
        return null;
    }

    public Department getLastWorkingDepartment() {
        Employee employee = this.getPerson().getEmployee();
        if (employee != null) {
            Department lastDepartmentWorkingPlace = employee.getLastDepartmentWorkingPlace();
            if (lastDepartmentWorkingPlace != null) {
                return lastDepartmentWorkingPlace;
            }
        }

        TeacherAuthorization teacherAuthorization = getLastTeacherAuthorization();
        return teacherAuthorization != null && teacherAuthorization instanceof ExternalTeacherAuthorization ? ((ExternalTeacherAuthorization) teacherAuthorization)
                .getDepartment() : null;
    }

    public List<Unit> getWorkingPlacesByPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
        List<Unit> workingPlaces = new ArrayList<Unit>();
        Employee employee = this.getPerson().getEmployee();
        if (employee != null) {
            workingPlaces.addAll(employee.getWorkingPlaces(beginDate, endDate));
        }

        for (TeacherAuthorization ta : getAuthorization()) {

            if (ta instanceof ExternalTeacherAuthorization && ((ExternalTeacherAuthorization) ta).getActive()
                    && ta.getExecutionSemester().isInTimePeriod(beginDate, endDate)
                    && !workingPlaces.contains(((ExternalTeacherAuthorization) ta).getDepartment().getDepartmentUnit())) {
                workingPlaces.add(((ExternalTeacherAuthorization) ta).getDepartment().getDepartmentUnit());
            }
        }

        return workingPlaces;
    }

    public ProfessionalCategory getCategory() {
        ProfessionalCategory category = getCurrentCategory();
        if (category == null) {
            PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
            return personProfessionalData == null ? null : personProfessionalData
                    .getLastProfessionalCategoryByCategoryType(CategoryType.TEACHER);
        }
        return category;
    }

    public ProfessionalCategory getCurrentCategory() {
        ProfessionalCategory professionalCategory = null;
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            professionalCategory =
                    personProfessionalData.getProfessionalCategoryByCategoryType(CategoryType.TEACHER, new LocalDate());
        }
        if (professionalCategory == null) {
            TeacherAuthorization teacherAuthorization = getTeacherAuthorization(ExecutionSemester.readActualExecutionSemester());
            if (teacherAuthorization != null) {
                professionalCategory = teacherAuthorization.getProfessionalCategory();
            }
        }
        return professionalCategory;
    }

    public ProfessionalCategory getLastCategory(LocalDate begin, LocalDate end) {
        ProfessionalCategory professionalCategory = null;
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            professionalCategory =
                    personProfessionalData.getLastProfessionalCategoryByCategoryType(CategoryType.TEACHER, begin, end);
        }
        if (professionalCategory == null) {
            List<ExecutionSemester> executionSemesters = ExecutionSemester.readExecutionPeriodsInTimePeriod(begin, end);
            Collections.sort(executionSemesters, new ReverseComparator(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR));
            for (ExecutionSemester executionSemester : executionSemesters) {
                TeacherAuthorization teacherAuthorization = getTeacherAuthorization(executionSemester);
                if (teacherAuthorization != null && teacherAuthorization.getProfessionalCategory() != null) {
                    return teacherAuthorization.getProfessionalCategory();
                }
            }
        }
        return professionalCategory;
    }

    public ProfessionalCategory getCategoryByPeriod(ExecutionSemester executionSemester) {
        OccupationPeriod lessonsPeriod = executionSemester.getLessonsPeriod();
        return getLastCategory(lessonsPeriod.getStartYearMonthDay().toLocalDate(), lessonsPeriod
                .getEndYearMonthDayWithNextPeriods().toLocalDate());
    }

    public PersonContractSituation getCurrentTeacherContractSituation() {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        return personProfessionalData != null ? personProfessionalData
                .getCurrentPersonContractSituationByCategoryType(CategoryType.TEACHER) : null;
    }

    public PersonContractSituation getCurrentOrLastTeacherContractSituation() {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        return personProfessionalData != null ? personProfessionalData
                .getCurrentOrLastPersonContractSituationByCategoryType(CategoryType.TEACHER) : null;
    }

    public PersonContractSituation getCurrentOrLastTeacherContractSituation(LocalDate begin, LocalDate end) {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        return personProfessionalData != null ? personProfessionalData.getCurrentOrLastPersonContractSituationByCategoryType(
                CategoryType.TEACHER, begin, end) : null;
    }

    public PersonContractSituation getDominantTeacherContractSituation(Interval interval) {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        return personProfessionalData != null ? personProfessionalData.getDominantPersonContractSituationByCategoryType(
                CategoryType.TEACHER, interval) : null;
    }

    public boolean hasAnyTeacherContractSituation() {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            return !personProfessionalData.getPersonContractSituationsByCategoryType(CategoryType.TEACHER).isEmpty();
        }
        return false;
    }

    public boolean hasAnyTeacherContractSituation(LocalDate beginDate, LocalDate endDate) {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            for (PersonContractSituation personContractSituation : personProfessionalData
                    .getPersonContractSituationsByCategoryType(CategoryType.TEACHER)) {
                if (personContractSituation.betweenDates(beginDate, endDate)) {
                    return true;
                }
            }
        }
        return false;
    }

    public TeacherPersonalExpectation getTeacherPersonalExpectationByExecutionYear(ExecutionYear executionYear) {
        TeacherPersonalExpectation result = null;
        Collection<TeacherPersonalExpectation> teacherPersonalExpectations = this.getTeacherPersonalExpectations();
        for (TeacherPersonalExpectation teacherPersonalExpectation : teacherPersonalExpectations) {
            if (teacherPersonalExpectation.getExecutionYear().equals(executionYear)) {
                result = teacherPersonalExpectation;
                break;
            }
        }
        return result;
    }

    public List<Proposal> getFinalDegreeWorksByExecutionYear(ExecutionYear executionYear) {
        List<Proposal> proposalList = new ArrayList<Proposal>();
        for (Proposal proposal : getPerson().getAssociatedProposalsByOrientator()) {
            if (proposal.getScheduleing().getExecutionDegreesSet().iterator().next().getExecutionYear().equals(executionYear)) {
                // if it was attributed by the coordinator the proposal is
                // efective
                if (proposal.getGroupAttributed() != null) {
                    proposalList.add(proposal);
                }
                // if not, we have to verify if the teacher has proposed it to
                // any student(s) and if that(those) student(s) has(have)
                // accepted it
                else {
                    FinalDegreeWorkGroup attributedGroupByTeacher = proposal.getGroupAttributedByTeacher();
                    if (attributedGroupByTeacher != null) {
                        boolean toAdd = false;
                        for (GroupStudent groupStudent : attributedGroupByTeacher.getGroupStudents()) {
                            Proposal studentProposal = groupStudent.getFinalDegreeWorkProposalConfirmation();
                            if (studentProposal != null && studentProposal.equals(proposal)) {
                                toAdd = true;
                            } else {
                                toAdd = false;
                            }
                        }
                        if (toAdd) {
                            proposalList.add(proposal);
                        }
                    }
                }
            }
        }
        return proposalList;
    }

    public List<ExecutionCourse> getLecturedExecutionCoursesByExecutionYear(ExecutionYear executionYear) {
        List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
            executionCourses.addAll(getLecturedExecutionCoursesByExecutionPeriod(executionSemester));
        }
        return executionCourses;
    }

    public List<ExecutionCourse> getLecturedExecutionCoursesByExecutionPeriod(final ExecutionSemester executionSemester) {
        List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        for (Professorship professorship : getProfessorships()) {
            ExecutionCourse executionCourse = professorship.getExecutionCourse();

            if (executionCourse.getExecutionPeriod().equals(executionSemester)) {
                executionCourses.add(executionCourse);
            }
        }
        return executionCourses;
    }

    public List<ExecutionCourse> getAllLecturedExecutionCourses() {
        List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        for (Professorship professorship : this.getProfessorships()) {
            executionCourses.add(professorship.getExecutionCourse());
        }
        return executionCourses;
    }

    public Double getHoursLecturedOnExecutionCourse(ExecutionCourse executionCourse) {
        double returnValue = 0;
        Professorship professorship = getProfessorshipByExecutionCourse(executionCourse);
        TeacherService teacherService = getTeacherServiceByExecutionPeriod(executionCourse.getExecutionPeriod());
        if (teacherService != null) {
            List<DegreeTeachingService> teachingServices = teacherService.getDegreeTeachingServiceByProfessorship(professorship);
            for (DegreeTeachingService teachingService : teachingServices) {
                returnValue +=
                        ((teachingService.getPercentage() / 100) * teachingService.getShift().getUnitHours().doubleValue());
            }
        }
        return returnValue;
    }

    public Duration getLecturedDurationOnExecutionCourse(ExecutionCourse executionCourse) {
        Duration duration = Duration.ZERO;
        Professorship professorship = getProfessorshipByExecutionCourse(executionCourse);
        TeacherService teacherService = getTeacherServiceByExecutionPeriod(executionCourse.getExecutionPeriod());
        if (teacherService != null) {
            List<DegreeTeachingService> teachingServices = teacherService.getDegreeTeachingServiceByProfessorship(professorship);
            for (DegreeTeachingService teachingService : teachingServices) {
                duration =
                        duration.plus(new Duration(new Double((teachingService.getPercentage() / 100)
                                * teachingService.getShift().getCourseLoadWeeklyAverage().doubleValue() * 3600 * 1000)
                                .longValue()));
            }
        }
        return duration;
    }

    public TeacherService getTeacherServiceByExecutionPeriod(final ExecutionSemester executionSemester) {
        return (TeacherService) CollectionUtils.find(getTeacherServices(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                TeacherService teacherService = (TeacherService) arg0;
                return teacherService.getExecutionPeriod() == executionSemester;
            }
        });
    }

    public Professorship getProfessorshipByExecutionCourse(final ExecutionCourse executionCourse) {
        return (Professorship) CollectionUtils.find(getProfessorships(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                Professorship professorship = (Professorship) arg0;
                return professorship.getExecutionCourse() == executionCourse;
            }
        });
    }

    public boolean hasProfessorshipForExecutionCourse(final ExecutionCourse executionCourse) {
        return (getProfessorshipByExecutionCourse(executionCourse) != null);
    }

    public List<Professorship> getDegreeProfessorshipsByExecutionPeriod(final ExecutionSemester executionSemester) {
        return (List<Professorship>) CollectionUtils.select(getProfessorships(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                Professorship professorship = (Professorship) arg0;
                return professorship.getExecutionCourse().getExecutionPeriod() == executionSemester
                        && !professorship.getExecutionCourse().isMasterDegreeDFAOrDEAOnly();
            }
        });
    }

    /***************************************************************************
     * PRIVATE METHODS *
     **************************************************************************/

    public List<MasterDegreeThesisDataVersion> getGuidedMasterDegreeThesisByExecutionYear(ExecutionYear executionYear) {
        List<MasterDegreeThesisDataVersion> guidedThesis = new ArrayList<MasterDegreeThesisDataVersion>();

        for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : this.getMasterDegreeThesisGuider()) {

            if (masterDegreeThesisDataVersion.getCurrentState().getState() == State.ACTIVE) {

                Collection<ExecutionDegree> executionDegrees =
                        masterDegreeThesisDataVersion.getMasterDegreeThesis().getStudentCurricularPlan()
                                .getDegreeCurricularPlan().getExecutionDegrees();

                for (ExecutionDegree executionDegree : executionDegrees) {
                    if (executionDegree.getExecutionYear().equals(executionYear)) {
                        guidedThesis.add(masterDegreeThesisDataVersion);
                    }
                }

            }
        }

        return guidedThesis;
    }

    public List<MasterDegreeThesisDataVersion> getAllGuidedMasterDegreeThesis() {
        List<MasterDegreeThesisDataVersion> guidedThesis = new ArrayList<MasterDegreeThesisDataVersion>();

        for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : getMasterDegreeThesisGuider()) {
            if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                guidedThesis.add(masterDegreeThesisDataVersion);
            }
        }

        return guidedThesis;
    }

    public List<PersonFunction> getPersonFuntions(YearMonthDay beginDate, YearMonthDay endDate) {
        return getPerson().getPersonFuntions(beginDate, endDate);
    }

    public double getThesesCredits(ExecutionSemester executionSemester) {
        double totalCredits = 0.0;

        for (ThesisEvaluationParticipant participant : this.getPerson().getThesisEvaluationParticipants(executionSemester)) {
            totalCredits += participant.getParticipationCredits();
        }

        return round(totalCredits);
    }

    public BigDecimal getMasterDegreeThesesCredits(ExecutionYear executionYear) {
        double totalThesisValue = 0.0;
        if (!executionYear.getYear().equals("2011/2012")) {
            for (ThesisEvaluationParticipant participant : getPerson().getThesisEvaluationParticipants()) {
                Thesis thesis = participant.getThesis();
                if (thesis.isEvaluated()
                        && thesis.hasFinalEnrolmentEvaluation()
                        && thesis.getEvaluation().getYear() == executionYear.getBeginCivilYear()
                        && (participant.getType() == ThesisParticipationType.ORIENTATOR || participant.getType() == ThesisParticipationType.COORIENTATOR)) {
                    totalThesisValue = totalThesisValue + participant.getParticipationCredits();
                }
            }
        }
        return (BigDecimal.valueOf(5).min(new BigDecimal(totalThesisValue * 0.5))).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPhdDegreeThesesCredits(ExecutionYear executionYear) {
        ExecutionYear previousExecutionYear = executionYear.getPreviousExecutionYear();
        int guidedThesesNumber = 0;
        double assistantGuidedTheses = 0.0;

        if (!executionYear.getYear().equals("2011/2012")) {
            for (InternalPhdParticipant internalPhdParticipant : getPerson().getInternalParticipants()) {
                ExecutionYear conclusionYear = internalPhdParticipant.getIndividualProcess().getConclusionYear();
                if (conclusionYear != null && conclusionYear.equals(previousExecutionYear)) {
                    if (internalPhdParticipant.getProcessForGuiding() != null) {
                        guidedThesesNumber++;
                    } else if (internalPhdParticipant.getProcessForAssistantGuiding() != null) {
                        assistantGuidedTheses =
                                assistantGuidedTheses
                                        + (0.5 / internalPhdParticipant.getProcessForAssistantGuiding().getAssistantGuidingsSet()
                                                .size());
                    }

                }
            }
        }
        return BigDecimal.valueOf(2 * (guidedThesesNumber + assistantGuidedTheses)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getProjectsTutorialsCredits(ExecutionYear executionYear) {
        BigDecimal result = BigDecimal.ZERO;
        for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
            TeacherService teacherService = getTeacherServiceByExecutionPeriod(executionSemester);
            if (teacherService != null) {
                for (DegreeProjectTutorialService degreeProjectTutorialService : teacherService
                        .getDegreeProjectTutorialServices()) {
                    result = result.add(degreeProjectTutorialService.getDegreeProjectTutorialServiceCredits());
                }
            }
        }
        return result.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public double getManagementFunctionsCredits(ExecutionSemester executionSemester) {
        double totalCredits = 0.0;
        for (PersonFunction personFunction : this.getPerson().getPersonFunctions()) {
            if (personFunction.belongsToPeriod(executionSemester.getBeginDateYearMonthDay(),
                    executionSemester.getEndDateYearMonthDay())
                    && !personFunction.getFunction().isVirtual()) {
                totalCredits = (personFunction.getCredits() != null) ? totalCredits + personFunction.getCredits() : totalCredits;
            }
        }
        return round(totalCredits);
    }

    public Set<PersonContractSituation> getValidTeacherServiceExemptions(Interval interval) {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            return personProfessionalData.getValidPersonProfessionalExemptionByCategoryType(CategoryType.TEACHER, interval);
        }
        return new HashSet<PersonContractSituation>();
    }

    public PersonContractSituation getDominantTeacherServiceExemption(ExecutionSemester executionSemester) {
        PersonContractSituation dominantExemption = null;
        int daysInDominantExemption = 0;
        Interval semesterInterval =
                new Interval(executionSemester.getBeginDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(),
                        executionSemester.getEndDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay());
        for (PersonContractSituation personContractSituation : getValidTeacherServiceExemptions(executionSemester)) {
            int daysInInterval = personContractSituation.getDaysInInterval(semesterInterval);
            if (dominantExemption == null || daysInInterval > daysInDominantExemption) {
                dominantExemption = personContractSituation;
                daysInDominantExemption = daysInInterval;
            }
        }

        return dominantExemption;
    }

    public Set<PersonContractSituation> getValidTeacherServiceExemptions(ExecutionSemester executionSemester) {
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        Interval semesterInterval =
                new Interval(executionSemester.getBeginDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(),
                        executionSemester.getEndDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay());
        if (semesterInterval != null && personProfessionalData != null) {
            return personProfessionalData.getValidPersonProfessionalExemptionByCategoryType(CategoryType.TEACHER,
                    semesterInterval);
        }
        return new HashSet<PersonContractSituation>();
    }

    public double getServiceExemptionCredits(ExecutionSemester executionSemester) {
        Set<PersonContractSituation> personProfessionalExemptions = getValidTeacherServiceExemptions(executionSemester);
        Interval semesterInterval =
                new Interval(executionSemester.getBeginDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(),
                        executionSemester.getEndDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay());
        int lessonsDays = semesterInterval.toPeriod(PeriodType.days()).getDays();

        List<Interval> notYetOverlapedIntervals = new ArrayList<Interval>();
        List<Interval> newIntervals = new ArrayList<Interval>();
        notYetOverlapedIntervals.add(semesterInterval);

        Double mandatoryLessonHours = getMandatoryLessonHours(executionSemester);
        Double maxSneHours = mandatoryLessonHours;
        TeacherService teacherService = getTeacherServiceByExecutionPeriod(executionSemester);
        if (teacherService != null && teacherService.getReductionService() != null) {
            maxSneHours = Math.max(0, (mandatoryLessonHours - teacherService.getReductionServiceCredits().doubleValue()));
        }

        for (PersonContractSituation personContractSituation : personProfessionalExemptions) {
            LocalDate exemptionEnd =
                    personContractSituation.getServiceExemptionEndDate() == null ? semesterInterval.getEnd().toLocalDate() : personContractSituation
                            .getServiceExemptionEndDate();

            Interval exemptionInterval =
                    new Interval(personContractSituation.getBeginDate().toDateTimeAtStartOfDay(),
                            exemptionEnd.toDateTimeAtStartOfDay());

            PersonProfessionalExemption personProfessionalExemption = personContractSituation.getPersonProfessionalExemption();
            if (personContractSituation.countForCredits(semesterInterval)) {
                if (personProfessionalExemption != null) {
                    exemptionEnd =
                            personProfessionalExemption.getEndDate() == null ? semesterInterval.getEnd().toLocalDate() : personProfessionalExemption
                                    .getEndDate();
                    exemptionInterval =
                            new Interval(personProfessionalExemption.getBeginDate().toDateTimeAtStartOfDay(),
                                    exemptionEnd.toDateTimeAtStartOfDay());
                    if (personProfessionalExemption.getIsSabaticalOrEquivalent()) {
                        if (isSabbaticalForSemester(exemptionInterval, semesterInterval)) {
                            return maxSneHours;
                        } else {
                            continue;
                        }
                    }
                }
                for (Interval notYetOverlapedInterval : notYetOverlapedIntervals) {
                    Interval overlapInterval = exemptionInterval.overlap(notYetOverlapedInterval);
                    if (overlapInterval != null) {
                        newIntervals.addAll(getNotOverlapedIntervals(overlapInterval, notYetOverlapedInterval));
                    } else {
                        newIntervals.add(notYetOverlapedInterval);
                    }
                }
                notYetOverlapedIntervals.clear();
                notYetOverlapedIntervals.addAll(newIntervals);
                newIntervals.clear();
            }
        }

        int notOverlapedDays = 0;
        for (Interval interval : notYetOverlapedIntervals) {
            notOverlapedDays += interval.toPeriod(PeriodType.days()).getDays();
        }
        int overlapedDays = lessonsDays - notOverlapedDays;
        Double overlapedPercentage = round(Double.valueOf(overlapedDays) / Double.valueOf(lessonsDays));
        return round(overlapedPercentage * maxSneHours);

    }

    private List<Interval> getNotOverlapedIntervals(Interval overlapInterval, Interval notYetOverlapedInterval) {
        List<Interval> intervals = new ArrayList<Interval>();
        LocalDate overlapIntervalStart = overlapInterval.getStart().toLocalDate();
        LocalDate overlapIntervalEnd = overlapInterval.getEnd().toLocalDate();
        LocalDate notYetOverlapedIntervalStart = notYetOverlapedInterval.getStart().toLocalDate();
        LocalDate notYetOverlapedIntervalEnd = notYetOverlapedInterval.getEnd().toLocalDate();

        if (overlapIntervalStart.equals(notYetOverlapedIntervalStart) && !overlapIntervalEnd.equals(notYetOverlapedIntervalEnd)) {
            intervals.add(new Interval(overlapInterval.getEnd().plusDays(1), notYetOverlapedInterval.getEnd()));

        } else if (!overlapIntervalStart.equals(notYetOverlapedIntervalStart)
                && overlapIntervalEnd.equals(notYetOverlapedIntervalEnd)) {
            intervals.add(new Interval(notYetOverlapedInterval.getStart(), overlapInterval.getStart().minusDays(1)));

        } else if (!overlapIntervalStart.equals(notYetOverlapedIntervalStart)
                && !overlapIntervalEnd.equals(notYetOverlapedIntervalEnd)) {
            intervals.add(new Interval(notYetOverlapedInterval.getStart(), overlapInterval.getStart().minusDays(1)));
            intervals.add(new Interval(overlapInterval.getEnd().plusDays(1), notYetOverlapedInterval.getEnd()));
        }

        return intervals;
    }

    private boolean isSabbaticalForSemester(Interval exemptionInterval, Interval semesterPeriod) {
        double overlapPercentageThisSemester =
                calculateLessonsIntervalAndExemptionOverlapPercentage(semesterPeriod, exemptionInterval);
        if (overlapPercentageThisSemester == 1) {
            return true;
        }
        if (semesterPeriod.contains(exemptionInterval.getStart())) {
            return overlapPercentageThisSemester >= 0.5;
        }
        ExecutionSemester firstExecutionPeriod = ExecutionSemester.readByDateTime(exemptionInterval.getStart());
        Interval firstExecutionPeriodInterval =
                new Interval(firstExecutionPeriod.getBeginDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(),
                        firstExecutionPeriod.getEndDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay());
        double overlapPercentageFirstSemester =
                calculateLessonsIntervalAndExemptionOverlapPercentage(firstExecutionPeriodInterval, exemptionInterval);
        return overlapPercentageFirstSemester < 0.5;
    }

    private double calculateLessonsIntervalAndExemptionOverlapPercentage(Interval lessonsInterval, Interval exemptionInterval) {
        if (lessonsInterval != null) {
            Interval overlapInterval = lessonsInterval.overlap(exemptionInterval);
            if (overlapInterval != null) {
                int intersectedDays = overlapInterval.toPeriod(PeriodType.days()).getDays();
                return round(Double.valueOf(intersectedDays)
                        / Double.valueOf(lessonsInterval.toPeriod(PeriodType.days()).getDays()));
            }
        }
        return 0.0;
    }

    private Double round(double n) {
        return Math.round((n * 100.0)) / 100.0;
    }

    public boolean isActive() {
        PersonContractSituation situation = getCurrentTeacherContractSituation();
        return situation != null && situation.isActive(new LocalDate());
    }

    public boolean isActiveForSemester(ExecutionSemester executionSemester) {
        int minimumWorkingDays = 90;
        int activeDays = 0;
        Interval semesterInterval =
                new Interval(executionSemester.getBeginDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(),
                        executionSemester.getEndDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay());
        PersonProfessionalData personProfessionalData = getPerson().getPersonProfessionalData();
        if (personProfessionalData != null) {
            GiafProfessionalData giafProfessionalData = personProfessionalData.getGiafProfessionalData();
            if (giafProfessionalData != null) {
                for (final PersonContractSituation situation : giafProfessionalData.getValidPersonContractSituations()) {
                    if (situation.overlaps(semesterInterval) && situation.getProfessionalCategory() != null
                            && situation.getProfessionalCategory().getCategoryType().equals(CategoryType.TEACHER)) {
                        LocalDate beginDate =
                                situation.getBeginDate().isBefore(semesterInterval.getStart().toLocalDate()) ? semesterInterval
                                        .getStart().toLocalDate() : situation.getBeginDate();
                        LocalDate endDate =
                                situation.getEndDate() == null
                                        || situation.getEndDate().isAfter(semesterInterval.getEnd().toLocalDate()) ? semesterInterval
                                        .getEnd().toLocalDate() : situation.getEndDate();
                        int days =
                                new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay()).toPeriod(
                                        PeriodType.days()).getDays() + 1;
                        activeDays = activeDays + days;
                    }
                }
            }
        }
        return activeDays >= minimumWorkingDays;
    }

    public boolean isActiveOrHasAuthorizationForSemester(ExecutionSemester executionSemester) {
        if (isActiveForSemester(executionSemester) || getTeacherAuthorization(executionSemester) != null) {
            return true;
        }
        return false;
    }

    public boolean isInactive(ExecutionSemester executionSemester) {
        return !isActiveForSemester(executionSemester);
    }

    public boolean isMonitor(ExecutionSemester executionSemester) {
        if (executionSemester != null) {
            ProfessionalCategory category = getCategoryByPeriod(executionSemester);
            return (category != null && category.isTeacherMonitorCategory());
        }
        return false;
    }

    public boolean isAssistant(ExecutionSemester executionSemester) {
        if (executionSemester != null) {
            ProfessionalCategory category = getCategoryByPeriod(executionSemester);
            return (category != null && category.isTeacherAssistantCategory());
        }
        return false;
    }

    public boolean isTeacherCareerCategory(ExecutionSemester executionSemester) {
        if (executionSemester != null) {
            ProfessionalCategory category = getCategoryByPeriod(executionSemester);
            return (category != null && category.isTeacherCareerCategory());
        }
        return false;
    }

    public boolean isTeacherProfessorCategory(ExecutionSemester executionSemester) {
        if (executionSemester != null) {
            ProfessionalCategory category = getCategoryByPeriod(executionSemester);
            return (category != null && category.isTeacherProfessorCategory());
        }
        return false;
    }

    public List<Advise> getAdvisesByAdviseTypeAndExecutionYear(AdviseType adviseType, ExecutionYear executionYear) {

        List<Advise> result = new ArrayList<Advise>();
        Date executionYearStartDate = executionYear.getBeginDate();
        Date executionYearEndDate = executionYear.getEndDate();

        for (Advise advise : this.getAdvises()) {
            if ((advise.getAdviseType() == adviseType)) {
                Date adviseStartDate = advise.getStartExecutionPeriod().getBeginDate();
                Date adviseEndDate = advise.getEndExecutionPeriod().getEndDate();

                if (((executionYearStartDate.compareTo(adviseStartDate) < 0) && (executionYearEndDate.compareTo(adviseStartDate) < 0))
                        || ((executionYearStartDate.compareTo(adviseEndDate) > 0) && (executionYearEndDate
                                .compareTo(adviseEndDate) > 0))) {
                    continue;
                }
                result.add(advise);
            }
        }

        return result;
    }

    public List<Advise> getAdvisesByAdviseType(AdviseType adviseType) {

        List<Advise> result = new ArrayList<Advise>();
        for (Advise advise : this.getAdvises()) {
            if (advise.getAdviseType() == adviseType) {
                result.add(advise);
            }
        }

        return result;
    }

    public double getBalanceOfCreditsUntil(ExecutionSemester executionSemester) throws ParseException {

        double balanceCredits = 0.0;
        ExecutionSemester firstExecutionPeriod = ExecutionSemester.readStartExecutionSemesterForCredits();

        TeacherService firstTeacherService = getTeacherServiceByExecutionPeriod(firstExecutionPeriod);
        if (firstTeacherService != null) {
            balanceCredits = firstTeacherService.getPastServiceCredits();
        }

        if (executionSemester != null && executionSemester.isAfter(firstExecutionPeriod)) {
            balanceCredits =
                    sumCreditsBetweenPeriods(firstExecutionPeriod.getNextExecutionPeriod(), executionSemester, balanceCredits);
        }
        return balanceCredits;
    }

    private double sumCreditsBetweenPeriods(ExecutionSemester startPeriod, ExecutionSemester endExecutionPeriod,
            double totalCredits) throws ParseException {
        ExecutionSemester lastExecutionSemester = ExecutionSemester.readLastExecutionSemesterForCredits();

        ExecutionSemester executionPeriodAfterEnd = endExecutionPeriod.getNextExecutionPeriod();
        while (startPeriod != executionPeriodAfterEnd && endExecutionPeriod.isBeforeOrEquals(lastExecutionSemester)) {
            TeacherCredits teacherCredits = TeacherCredits.readTeacherCredits(startPeriod, this);
            if (teacherCredits != null && teacherCredits.getTeacherCreditsState().isCloseState()) {
                totalCredits += teacherCredits.getTotalCredits().subtract(teacherCredits.getMandatoryLessonHours()).doubleValue();
            } else if (!isMonitor(startPeriod)) {
                TeacherService teacherService = getTeacherServiceByExecutionPeriod(startPeriod);
                if (teacherService != null) {
                    totalCredits += teacherService.getCredits();
                }
                totalCredits += getThesesCredits(startPeriod);
                totalCredits += getManagementFunctionsCredits(startPeriod);
                totalCredits += getServiceExemptionCredits(startPeriod);
                totalCredits -= getMandatoryLessonHours(startPeriod);
            }
            startPeriod = startPeriod.getNextExecutionPeriod();
        }
        return totalCredits;
    }

    public Double getMandatoryLessonHours(ExecutionSemester executionSemester) {
        PersonContractSituation teacherContractSituation = null;
        Interval semesterInterval =
                new Interval(executionSemester.getBeginDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay(),
                        executionSemester.getEndDateYearMonthDay().toLocalDate().toDateTimeAtStartOfDay());
        if (isActiveForSemester(executionSemester)) {
            teacherContractSituation = getDominantTeacherContractSituation(semesterInterval);
            PersonContractSituation personContractSituation = getDominantTeacherServiceExemption(executionSemester);
            if (personContractSituation != null && !personContractSituation.countForCredits(semesterInterval)) {
                teacherContractSituation = personContractSituation;
            }
        } else if (getTeacherAuthorization(executionSemester) != null) {
            TeacherService teacherService = getTeacherServiceByExecutionPeriod(executionSemester);
            return teacherService == null ? 0 : teacherService.getTeachingDegreeHours();
        }
        return teacherContractSituation == null ? 0 : teacherContractSituation.getWeeklyLessonHours(semesterInterval);
    }

    public List<PersonFunction> getManagementFunctions(ExecutionSemester executionSemester) {
        List<PersonFunction> personFunctions = new ArrayList<PersonFunction>();
        for (PersonFunction personFunction : this.getPerson().getPersonFunctions()) {
            if (personFunction.belongsToPeriod(executionSemester.getBeginDateYearMonthDay(),
                    executionSemester.getEndDateYearMonthDay())) {
                personFunctions.add(personFunction);
            }
        }
        return personFunctions;
    }

    public static Teacher readTeacherByUsername(final String userName) {
        final Person person = Person.readPersonByUsername(userName);
        return (person.getTeacher() != null) ? person.getTeacher() : null;
    }

    public static List<Teacher> readByNumbers(Collection<String> teacherId) {
        List<Teacher> selectedTeachers = new ArrayList<Teacher>();
        for (final Teacher teacher : Bennu.getInstance().getTeachersSet()) {
            if (teacherId.contains(teacher.getPerson().getIstUsername())) {
                selectedTeachers.add(teacher);
            }
            // This isn't necessary, its just a fast optimization.
            if (teacherId.size() == selectedTeachers.size()) {
                break;
            }
        }
        return selectedTeachers;
    }

    public List<Professorship> getProfessorships(ExecutionSemester executionSemester) {
        return getPerson().getProfessorships(executionSemester);
    }

    public List<Professorship> getProfessorships(ExecutionYear executionYear) {
        return getPerson().getProfessorships(executionYear);
    }

    public Set<TeacherDegreeFinalProjectStudent> findTeacherDegreeFinalProjectStudentsByExecutionPeriod(
            final ExecutionSemester executionSemester) {
        final Set<TeacherDegreeFinalProjectStudent> teacherDegreeFinalProjectStudents =
                new HashSet<TeacherDegreeFinalProjectStudent>();
        for (final TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent : getDegreeFinalProjectStudents()) {
            if (executionSemester == teacherDegreeFinalProjectStudent.getExecutionPeriod()) {
                teacherDegreeFinalProjectStudents.add(teacherDegreeFinalProjectStudent);
            }
        }
        return teacherDegreeFinalProjectStudents;
    }

    public List<ManagementPositionCreditLine> getManagementPositionsFor(ExecutionSemester executionSemester) {
        final List<ManagementPositionCreditLine> result = new ArrayList<ManagementPositionCreditLine>();
        for (final ManagementPositionCreditLine managementPositionCreditLine : this.getManagementPositions()) {
            if (managementPositionCreditLine.getStart().isBefore(executionSemester.getEndDateYearMonthDay())
                    && managementPositionCreditLine.getEnd().isAfter(executionSemester.getBeginDateYearMonthDay())) {
                result.add(managementPositionCreditLine);
            }
        }
        return result;
    }

    public Orientation readOrientationByType(OrientationType orientationType) {
        for (final Orientation orientation : this.getAssociatedOrientationsSet()) {
            if (orientation.getOrientationType().equals(orientationType)) {
                return orientation;
            }
        }
        return null;
    }

    public PublicationsNumber readPublicationsNumberByType(PublicationType publicationType) {
        for (final PublicationsNumber publicationsNumber : this.getAssociatedPublicationsNumbersSet()) {
            if (publicationsNumber.getPublicationType().equals(publicationType)) {
                return publicationsNumber;
            }
        }
        return null;
    }

    public SortedSet<ExecutionCourse> getCurrentExecutionCourses() {
        final SortedSet<ExecutionCourse> executionCourses =
                new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME);
        final ExecutionSemester currentExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
        final ExecutionSemester previousExecutionPeriod = currentExecutionPeriod.getPreviousExecutionPeriod();
        for (final Professorship professorship : getProfessorshipsSet()) {
            final ExecutionCourse executionCourse = professorship.getExecutionCourse();
            final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
            if (executionSemester == currentExecutionPeriod || executionSemester == previousExecutionPeriod) {
                executionCourses.add(executionCourse);
            }
        }
        return executionCourses;
    }

    public boolean isResponsibleFor(CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
        for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
            if (executionCourse.getExecutionPeriod() == executionSemester) {
                if (isResponsibleFor(executionCourse) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public Double getHoursLecturedOnExecutionCourseByShiftType(ExecutionCourse executionCourse, ShiftType shiftType) {
        double returnValue = 0;
        Professorship professorship = getProfessorshipByExecutionCourse(executionCourse);
        TeacherService teacherService = getTeacherServiceByExecutionPeriod(executionCourse.getExecutionPeriod());
        if (teacherService != null) {
            List<DegreeTeachingService> teachingServices = teacherService.getDegreeTeachingServiceByProfessorship(professorship);
            for (DegreeTeachingService teachingService : teachingServices) {
                if (teachingService.getShift().containsType(shiftType)) {
                    returnValue +=
                            ((teachingService.getPercentage() / 100) * teachingService.getShift().getUnitHours().doubleValue());
                }
            }
        }
        return returnValue;
    }

    public boolean teachesAny(final Collection<ExecutionCourse> executionCourses, ExecutionYear executionYear) {
        for (final Professorship professorship : getProfessorships(executionYear)) {
            if (executionCourses.contains(professorship.getExecutionCourse())) {
                return true;
            }
        }
        return false;
    }

    public boolean teachesAny(final Collection<ExecutionCourse> executionCourses) {
        return getPerson().teachesAny(executionCourses);
    }

    public void delete() {
        super.setPerson(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public boolean hasLessons(DateTime begin, DateTime end) {
        return hasLessons(begin, end, ExecutionYear.readCurrentExecutionYear());
    }

    public boolean hasLessons(DateTime begin, DateTime end, ExecutionYear executionYear) {
        final Interval interval = new Interval(begin, end);
        for (Professorship professorship : getProfessorships(executionYear)) {
            Set<Shift> associatedShifts = professorship.getExecutionCourse().getAssociatedShifts();
            for (Shift shift : associatedShifts) {
                Collection<Lesson> associatedLessons = shift.getAssociatedLessons();
                for (Lesson lesson : associatedLessons) {
                    if (lesson.contains(interval)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<ExpectationEvaluationGroup> getEvaluatedExpectationEvaluationGroups(ExecutionYear executionYear) {
        List<ExpectationEvaluationGroup> result = new ArrayList<ExpectationEvaluationGroup>();
        for (ExpectationEvaluationGroup expectationEvaluationGroup : getEvaluatedExpectationEvaluationGroups()) {
            if (expectationEvaluationGroup.getExecutionYear().equals(executionYear)) {
                result.add(expectationEvaluationGroup);
            }
        }
        return result;
    }

    public List<ExpectationEvaluationGroup> getAppraiserExpectationEvaluationGroups(ExecutionYear executionYear) {
        List<ExpectationEvaluationGroup> result = new ArrayList<ExpectationEvaluationGroup>();
        for (ExpectationEvaluationGroup expectationEvaluationGroup : getAppraiserExpectationEvaluationGroups()) {
            if (expectationEvaluationGroup.getExecutionYear().equals(executionYear)) {
                result.add(expectationEvaluationGroup);
            }
        }
        return result;
    }

    public boolean hasExpectationEvaluatedTeacher(Teacher teacher, ExecutionYear executionYear) {
        for (ExpectationEvaluationGroup group : getEvaluatedExpectationEvaluationGroups()) {
            if (group.getExecutionYear().equals(executionYear) && group.getEvaluated().equals(teacher)) {
                return true;
            }
        }
        return false;
    }

    public Unit getCurrentSectionOrScientificArea() {
        final Employee employee = getPerson().getEmployee();
        if (employee != null) {
            final Unit unit = employee.getCurrentSectionOrScientificArea();
            if (unit != null) {
                return unit;
            }
        }

        final TeacherAuthorization teacherAuthorization =
                getTeacherAuthorization(ExecutionSemester.readActualExecutionSemester());
        if (teacherAuthorization != null && teacherAuthorization instanceof ExternalTeacherAuthorization) {
            final ExternalTeacherAuthorization externalTeacherAuthorization = (ExternalTeacherAuthorization) teacherAuthorization;
            final Department department = externalTeacherAuthorization.getDepartment();
            return department.getDepartmentUnit();
        }
        return null;
    }

    public List<Tutorship> getActiveTutorshipsByStudentsEntryYearAndDegree(ExecutionYear entryYear, Degree degree) {
        return getTutorshipsByStudentsEntryYearAndDegree(this.getActiveTutorships(), entryYear, degree);
    }

    public List<Tutorship> getPastTutorshipsByStudentsEntryYearAndDegree(ExecutionYear entryYear, Degree degree) {
        return getTutorshipsByStudentsEntryYearAndDegree(this.getPastTutorships(), entryYear, degree);
    }

    private List<Tutorship> getTutorshipsByStudentsEntryYearAndDegree(List<Tutorship> tutorshipsList, ExecutionYear entryYear,
            Degree degree) {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();
        for (Tutorship tutorship : tutorshipsList) {
            StudentCurricularPlan studentCurricularPlan = tutorship.getStudentCurricularPlan();
            ExecutionYear studentEntryYear =
                    ExecutionYear.getExecutionYearByDate(studentCurricularPlan.getRegistration().getStartDate());
            if (studentEntryYear.equals(entryYear) && studentCurricularPlan.getDegree().equals(degree)
                    && !studentCurricularPlan.getRegistration().isCanceled()) {
                tutorships.add(tutorship);
            }
        }

        return tutorships;
    }

    public List<Tutorship> getPastTutorships() {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();
        for (Tutorship tutorship : getTutorships()) {
            if (!tutorship.isActive()) {
                tutorships.add(tutorship);
            }
        }
        return tutorships;
    }

    public List<Tutorship> getActiveTutorships() {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();
        for (Tutorship tutorship : getTutorships()) {
            if (tutorship.isActive()) {
                tutorships.add(tutorship);
            }
        }
        return tutorships;
    }

    public List<Tutorship> getActiveTutorships(AcademicInterval semester) {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();
        for (Tutorship tutorship : getTutorships()) {
            if (tutorship.isActive(semester)) {
                tutorships.add(tutorship);
            }
        }
        return tutorships;
    }

    public Integer getNumberOfPastTutorships() {
        return this.getPastTutorships().size();
    }

    public Integer getNumberOfActiveTutorships() {
        return this.getActiveTutorships().size();
    }

    public Integer getNumberOfTutorships() {
        return this.getTutorships().size();
    }

    public boolean canBeTutorOfDepartment(Department department) {
        if (getCurrentWorkingDepartment() != null && getCurrentWorkingDepartment().equals(department)) {
            return true;
        }
        return false;
    }

    public List<Tutorship> getTutorshipsByStudentsEntryYear(ExecutionYear entryYear) {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();

        for (Tutorship tutorship : this.getTutorships()) {
            StudentCurricularPlan studentCurricularPlan = tutorship.getStudentCurricularPlan();
            ExecutionYear studentEntryYear =
                    ExecutionYear.getExecutionYearByDate(studentCurricularPlan.getRegistration().getStartDate());
            if (studentEntryYear.equals(entryYear)) {
                tutorships.add(tutorship);
            }
        }

        return tutorships;
    }

    public List<Tutorship> getActiveTutorshipsByStudentsEntryYear(ExecutionYear entryYear) {
        List<Tutorship> tutorships = new ArrayList<Tutorship>();

        for (Tutorship tutorship : this.getTutorships()) {
            StudentCurricularPlan studentCurricularPlan = tutorship.getStudentCurricularPlan();
            ExecutionYear studentEntryYear =
                    ExecutionYear.getExecutionYearByDate(studentCurricularPlan.getRegistration().getStartDate());
            if (studentEntryYear.equals(entryYear) && tutorship.isActive()) {
                tutorships.add(tutorship);
            }
        }

        return tutorships;
    }

    public Collection<? extends Forum> getForuns(final ExecutionSemester executionSemester) {
        final Collection<Forum> res = new HashSet<Forum>();
        for (Professorship professorship : getProfessorshipsSet()) {
            if (professorship.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                res.addAll(professorship.getExecutionCourse().getForuns());
            }
        }
        return res;
    }

    private RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public boolean teachesAt(final Space campus) {
        for (final Professorship professorship : getProfessorshipsSet()) {
            final ExecutionCourse executionCourse = professorship.getExecutionCourse();
            if (executionCourse.getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
                return executionCourse.functionsAt(campus);
            }
        }
        return false;
    }

    public Set<Professorship> getProfessorshipsSet() {
        return getPerson().getProfessorshipsSet();
    }

    public void addProfessorships(Professorship professorship) {
        getPerson().addProfessorships(professorship);
    }

    public void removeProfessorships(Professorship professorship) {
        getPerson().removeProfessorships(professorship);
    }

    public Collection<Professorship> getProfessorships() {
        return getPerson().getProfessorships();
    }

    public Iterator<Professorship> getProfessorshipsIterator() {
        return getPerson().getProfessorships().iterator();
    }

    public TeacherCredits getTeacherCredits(ExecutionSemester executionSemester) {
        for (TeacherCredits teacherCredits : getTeacherCredits()) {
            if (teacherCredits.getTeacherCreditsState().getExecutionSemester().equals(executionSemester)) {
                return teacherCredits;
            }
        }
        return null;
    }

    public boolean hasTeacherCredits(ExecutionSemester executionSemester) {
        for (TeacherCredits teacherCredits : getTeacherCredits()) {
            if (teacherCredits.getTeacherCreditsState().getExecutionSemester().equals(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasTeacherAuthorization(ExecutionSemester executionSemester) {
        for (TeacherAuthorization ta : getAuthorization()) {
            if (ta instanceof ExternalTeacherAuthorization && ((ExternalTeacherAuthorization) ta).getActive()
                    && ((ExternalTeacherAuthorization) ta).getExecutionSemester().equals(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    public TeacherAuthorization getTeacherAuthorization(ExecutionSemester executionSemester) {
        for (TeacherAuthorization ta : getAuthorization()) {
            if (ta instanceof ExternalTeacherAuthorization && ((ExternalTeacherAuthorization) ta).getActive()
                    && ((ExternalTeacherAuthorization) ta).getExecutionSemester().equals(executionSemester)) {
                return ta;
            } else if (ta instanceof AplicaTeacherAuthorization) {
                return ta;
            }
        }
        return null;
    }

    public TeacherAuthorization getLastTeacherAuthorization() {
        LocalDate today = new LocalDate();
        TeacherAuthorization lastTeacherAuthorization = null;
        for (TeacherAuthorization ta : getAuthorization()) {
            if ((lastTeacherAuthorization == null || ta.getExecutionSemester().getEndDateYearMonthDay()
                    .isAfter(lastTeacherAuthorization.getExecutionSemester().getEndDateYearMonthDay()))
                    && ta.getExecutionSemester().getEndDateYearMonthDay().isAfter(today)
                    && (ta instanceof AplicaTeacherAuthorization || ((ExternalTeacherAuthorization) ta).getActive())) {
                lastTeacherAuthorization = ta;
            }
        }
        return lastTeacherAuthorization;
    }

    public boolean isErasmusCoordinator() {
        return !getMobilityCoordinations().isEmpty();
    }

    public boolean hasTutorshipIntentionFor(ExecutionDegree executionDegree) {
        for (TutorshipIntention intention : getTutorshipIntentionSet()) {
            if (intention.getAcademicInterval().equals(executionDegree.getAcademicInterval())
                    && intention.getDegreeCurricularPlan().equals(executionDegree.getDegreeCurricularPlan())) {
                return true;
            }
        }
        return false;
    }

    public List<ExecutionCourseAudit> getExecutionCourseAudits(ExecutionSemester executionSemester) {
        List<ExecutionCourseAudit> result = new ArrayList<ExecutionCourseAudit>();
        for (ExecutionCourseAudit executionCourseAudit : getExecutionCourseAudits()) {
            if (executionCourseAudit.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                result.add(executionCourseAudit);
            }
        }
        return result;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TutorshipIntention> getTutorshipIntention() {
        return getTutorshipIntentionSet();
    }

    @Deprecated
    public boolean hasAnyTutorshipIntention() {
        return !getTutorshipIntentionSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TeacherAuthorization> getAuthorization() {
        return getAuthorizationSet();
    }

    @Deprecated
    public boolean hasAnyAuthorization() {
        return !getAuthorizationSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.TeacherService> getTeacherServices() {
        return getTeacherServicesSet();
    }

    @Deprecated
    public boolean hasAnyTeacherServices() {
        return !getTeacherServicesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.Orientation> getAssociatedOrientations() {
        return getAssociatedOrientationsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedOrientations() {
        return !getAssociatedOrientationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent> getDegreeFinalProjectStudents() {
        return getDegreeFinalProjectStudentsSet();
    }

    @Deprecated
    public boolean hasAnyDegreeFinalProjectStudents() {
        return !getDegreeFinalProjectStudentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.workTime.TeacherInstitutionWorkTime> getInstitutionWorkTimePeriods() {
        return getInstitutionWorkTimePeriodsSet();
    }

    @Deprecated
    public boolean hasAnyInstitutionWorkTimePeriods() {
        return !getInstitutionWorkTimePeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MasterDegreeCandidate> getMasterDegreeCandidates() {
        return getMasterDegreeCandidatesSet();
    }

    @Deprecated
    public boolean hasAnyMasterDegreeCandidates() {
        return !getMasterDegreeCandidatesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine> getManagementPositions() {
        return getManagementPositionsSet();
    }

    @Deprecated
    public boolean hasAnyManagementPositions() {
        return !getManagementPositionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MarkSheet> getMarkSheets() {
        return getMarkSheetsSet();
    }

    @Deprecated
    public boolean hasAnyMarkSheets() {
        return !getMarkSheetsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.credits.AnnualTeachingCredits> getAnnualTeachingCredits() {
        return getAnnualTeachingCreditsSet();
    }

    @Deprecated
    public boolean hasAnyAnnualTeachingCredits() {
        return !getAnnualTeachingCreditsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.PublicationsNumber> getAssociatedPublicationsNumbers() {
        return getAssociatedPublicationsNumbersSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedPublicationsNumbers() {
        return !getAssociatedPublicationsNumbersSet().isEmpty();
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
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.Career> getAssociatedCareers() {
        return getAssociatedCareersSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedCareers() {
        return !getAssociatedCareersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation> getTeacherPersonalExpectations() {
        return getTeacherPersonalExpectationsSet();
    }

    @Deprecated
    public boolean hasAnyTeacherPersonalExpectations() {
        return !getTeacherPersonalExpectationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.ExternalActivity> getAssociatedExternalActivities() {
        return getAssociatedExternalActivitiesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedExternalActivities() {
        return !getAssociatedExternalActivitiesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit> getExecutionCourseAudits() {
        return getExecutionCourseAuditsSet();
    }

    @Deprecated
    public boolean hasAnyExecutionCourseAudits() {
        return !getExecutionCourseAuditsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion> getMasterDegreeThesisGuider() {
        return getMasterDegreeThesisGuiderSet();
    }

    @Deprecated
    public boolean hasAnyMasterDegreeThesisGuider() {
        return !getMasterDegreeThesisGuiderSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.Advise> getAdvises() {
        return getAdvisesSet();
    }

    @Deprecated
    public boolean hasAnyAdvises() {
        return !getAdvisesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion> getMasterDegreeThesisAssistentGuider() {
        return getMasterDegreeThesisAssistentGuiderSet();
    }

    @Deprecated
    public boolean hasAnyMasterDegreeThesisAssistentGuider() {
        return !getMasterDegreeThesisAssistentGuiderSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TeacherCredits> getTeacherCredits() {
        return getTeacherCreditsSet();
    }

    @Deprecated
    public boolean hasAnyTeacherCredits() {
        return !getTeacherCreditsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MasterDegreeProofVersion> getMasterDegreeProofsJury() {
        return getMasterDegreeProofsJurySet();
    }

    @Deprecated
    public boolean hasAnyMasterDegreeProofsJury() {
        return !getMasterDegreeProofsJurySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Tutorship> getTutorships() {
        return getTutorshipsSet();
    }

    @Deprecated
    public boolean hasAnyTutorships() {
        return !getTutorshipsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.OldPublication> getAssociatedOldPublications() {
        return getAssociatedOldPublicationsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedOldPublications() {
        return !getAssociatedOldPublicationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup> getAppraiserExpectationEvaluationGroups() {
        return getAppraiserExpectationEvaluationGroupsSet();
    }

    @Deprecated
    public boolean hasAnyAppraiserExpectationEvaluationGroups() {
        return !getAppraiserExpectationEvaluationGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.ThesisSubject> getThesisSubjects() {
        return getThesisSubjectsSet();
    }

    @Deprecated
    public boolean hasAnyThesisSubjects() {
        return !getThesisSubjectsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesTeachersRes> getAssociatedOldInquiriesTeachersRes() {
        return getAssociatedOldInquiriesTeachersResSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedOldInquiriesTeachersRes() {
        return !getAssociatedOldInquiriesTeachersResSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TutorshipSummary> getTutorshipSummaries() {
        return getTutorshipSummariesSet();
    }

    @Deprecated
    public boolean hasAnyTutorshipSummaries() {
        return !getTutorshipSummariesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityCoordinator> getMobilityCoordinations() {
        return getMobilityCoordinationsSet();
    }

    @Deprecated
    public boolean hasAnyMobilityCoordinations() {
        return !getMobilityCoordinationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup> getEvaluatedExpectationEvaluationGroups() {
        return getEvaluatedExpectationEvaluationGroupsSet();
    }

    @Deprecated
    public boolean hasAnyEvaluatedExpectationEvaluationGroups() {
        return !getEvaluatedExpectationEvaluationGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.credits.OtherTypeCreditLine> getOtherTypeCreditLines() {
        return getOtherTypeCreditLinesSet();
    }

    @Deprecated
    public boolean hasAnyOtherTypeCreditLines() {
        return !getOtherTypeCreditLinesSet().isEmpty();
    }

    @Deprecated
    public boolean hasWeeklyOcupation() {
        return getWeeklyOcupation() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasServiceProviderRegime() {
        return getServiceProviderRegime() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
