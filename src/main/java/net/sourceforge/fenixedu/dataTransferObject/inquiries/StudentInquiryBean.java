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
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.AffiliatedTeacherDTO;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.NonRegularTeachingService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistryState;
import net.sourceforge.fenixedu.domain.inquiries.InquiryCourseAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGradesInterval;
import net.sourceforge.fenixedu.domain.inquiries.InquiryStudentTeacherAnswer;
import net.sourceforge.fenixedu.domain.inquiries.QuestionAnswer;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryExecutionPeriod;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryRegistry;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.StudentTeacherInquiryTemplate;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class StudentInquiryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private DateTime startedWhen;
    private Set<InquiryBlockDTO> curricularCourseBlocks;
    private StudentInquiryRegistry inquiryRegistry;
    Map<AffiliatedTeacherDTO, List<? extends StudentTeacherInquiryBean>> teachersInquiries =
            new TreeMap<AffiliatedTeacherDTO, List<? extends StudentTeacherInquiryBean>>(new BeanComparator("name"));

    public StudentInquiryBean(StudentTeacherInquiryTemplate studentTeacherInquiryTemplate, StudentInquiryRegistry inquiryRegistry) {
        setInquiryRegistry(inquiryRegistry);
        setStartedWhen(new DateTime());
        final ExecutionCourse executionCourse = getInquiryRegistry().getExecutionCourse();
        final Set<ShiftType> shiftTypes = executionCourse.getShiftTypes();

        fillTeachersInquiriesWithTeachers(executionCourse, shiftTypes, studentTeacherInquiryTemplate);
    }

    private void fillTeachersInquiriesWithTeachers(final ExecutionCourse executionCourse, final Set<ShiftType> shiftTypes,
            StudentInquiryTemplate studentTeacherInquiryTemplate) {
        Map<Person, Map<ShiftType, StudentTeacherInquiryBean>> teachersShifts =
                new HashMap<Person, Map<ShiftType, StudentTeacherInquiryBean>>();
        Map<ShiftType, Double> allTeachingServicesShiftType = null;
        Set<ShiftType> nonAssociatedTeachersShiftTypes = null;
        for (final Professorship professorship : executionCourse.getProfessorships()) {

            final Person person = professorship.getPerson();
            if (!teachersShifts.containsKey(person)) {
                teachersShifts.put(person, new HashMap<ShiftType, StudentTeacherInquiryBean>());
            }

            final Map<ShiftType, StudentTeacherInquiryBean> teacherShift = teachersShifts.get(person);
            final AffiliatedTeacherDTO teacherDTO = new AffiliatedTeacherDTO(person);

            //	    Teacher teacher = person.getTeacher();
            //	    boolean mandatoryTeachingService = false;
            //	    if (teacher != null && teacher.isTeacherProfessorCategory(executionCourse.getExecutionPeriod())) {
            //		mandatoryTeachingService = true;
            //	    }

            Map<ShiftType, Double> shiftTypesPercentageMap = new HashMap<ShiftType, Double>();
            for (DegreeTeachingService degreeTeachingService : professorship.getDegreeTeachingServices()) {
                for (ShiftType shiftType : degreeTeachingService.getShift().getTypes()) {
                    Double percentage = shiftTypesPercentageMap.get(shiftType);
                    if (percentage == null) {
                        percentage = degreeTeachingService.getPercentage();
                    } else {
                        percentage += degreeTeachingService.getPercentage();
                    }
                    shiftTypesPercentageMap.put(shiftType, percentage);
                }
            }
            for (NonRegularTeachingService nonRegularTeachingService : professorship.getNonRegularTeachingServicesSet()) {
                for (ShiftType shiftType : nonRegularTeachingService.getShift().getTypes()) {
                    Double percentage = shiftTypesPercentageMap.get(shiftType);
                    if (percentage == null) {
                        percentage = nonRegularTeachingService.getPercentage();
                    } else {
                        percentage += nonRegularTeachingService.getPercentage();
                    }
                    shiftTypesPercentageMap.put(shiftType, percentage);
                }
            }

            for (ShiftType shiftType : shiftTypesPercentageMap.keySet()) {
                Double percentage = shiftTypesPercentageMap.get(shiftType);
                if (percentage >= 20) {
                    if (!teacherShift.containsKey(shiftType)) {
                        teacherShift.put(shiftType, new StudentTeacherInquiryBean(teacherDTO, executionCourse, shiftType,
                                studentTeacherInquiryTemplate));
                    }
                }
            }

            if (shiftTypesPercentageMap.isEmpty() /* && !mandatoryTeachingService */) {
                if (allTeachingServicesShiftType == null) {
                    allTeachingServicesShiftType = getAllDegreeTeachingServices(executionCourse);
                }
                if (nonAssociatedTeachersShiftTypes == null) {
                    nonAssociatedTeachersShiftTypes = getNonAssociatedTeachersShiftTypes(executionCourse);
                }
                for (final ShiftType shiftType : shiftTypes) {
                    Double shiftTypePercentage = allTeachingServicesShiftType.get(shiftType);
                    if (shiftTypePercentage == null || shiftTypePercentage < 100.0
                            || nonAssociatedTeachersShiftTypes.contains(shiftType)) {
                        teacherShift.put(shiftType, new StudentTeacherInquiryBean(teacherDTO, executionCourse, shiftType,
                                studentTeacherInquiryTemplate));
                    }
                }
            }
        }
        for (Entry<Person, Map<ShiftType, StudentTeacherInquiryBean>> entry : teachersShifts.entrySet()) {
            ArrayList<StudentTeacherInquiryBean> studentTeachers =
                    new ArrayList<StudentTeacherInquiryBean>(entry.getValue().values());
            Collections.sort(studentTeachers, new BeanComparator("shiftType"));
            if (!studentTeachers.isEmpty()) {
                getTeachersInquiries().put(new AffiliatedTeacherDTO(entry.getKey()), studentTeachers);
            }
        }
    }

    private Set<ShiftType> getNonAssociatedTeachersShiftTypes(ExecutionCourse executionCourse) {
        Set<ShiftType> nonAssociatedTeachersShiftTypes = new HashSet<ShiftType>();
        for (Shift shift : executionCourse.getAssociatedShifts()) {
            if (shift.getDegreeTeachingServices().isEmpty() && shift.getNonRegularTeachingServices().isEmpty()) {
                nonAssociatedTeachersShiftTypes.addAll(shift.getTypes());
            }
        }
        return nonAssociatedTeachersShiftTypes;
    }

    private Map<ShiftType, Double> getAllDegreeTeachingServices(ExecutionCourse executionCourse) {
        Map<ShiftType, Double> shiftTypesPercentageMap = new HashMap<ShiftType, Double>();
        for (Professorship professorship : executionCourse.getProfessorships()) {
            for (DegreeTeachingService degreeTeachingService : professorship.getDegreeTeachingServices()) {
                for (ShiftType shiftType : degreeTeachingService.getShift().getTypes()) {
                    Double percentage = shiftTypesPercentageMap.get(shiftType);
                    if (percentage == null) {
                        percentage = degreeTeachingService.getPercentage();
                    } else {
                        percentage += degreeTeachingService.getPercentage();
                    }
                    shiftTypesPercentageMap.put(shiftType, percentage);
                }
            }
            for (NonRegularTeachingService nonRegularTeachingService : professorship.getNonRegularTeachingServicesSet()) {
                for (ShiftType shiftType : nonRegularTeachingService.getShift().getTypes()) {
                    Double percentage = shiftTypesPercentageMap.get(shiftType);
                    if (percentage == null) {
                        percentage = nonRegularTeachingService.getPercentage();
                    } else {
                        percentage += nonRegularTeachingService.getPercentage();
                    }
                    shiftTypesPercentageMap.put(shiftType, percentage);
                }
            }
        }
        return shiftTypesPercentageMap;
    }

    public List<AffiliatedTeacherDTO> getOrderedTeachers() {
        List<AffiliatedTeacherDTO> finalResult = new ArrayList<AffiliatedTeacherDTO>();
        Set<AffiliatedTeacherDTO> theoricalShiftType = new TreeSet<AffiliatedTeacherDTO>(new BeanComparator("name"));
        Set<AffiliatedTeacherDTO> praticalShiftType = new TreeSet<AffiliatedTeacherDTO>(new BeanComparator("name"));
        Set<AffiliatedTeacherDTO> laboratoryShiftType = new TreeSet<AffiliatedTeacherDTO>(new BeanComparator("name"));
        Set<AffiliatedTeacherDTO> otherShiftTypes = new TreeSet<AffiliatedTeacherDTO>(new BeanComparator("name"));
        for (AffiliatedTeacherDTO teacherDTO : getTeachersInquiries().keySet()) {
            if (containsShiftType(teacherDTO, ShiftType.TEORICA)) {
                theoricalShiftType.add(teacherDTO);
            } else if (containsShiftType(teacherDTO, ShiftType.PRATICA)) {
                praticalShiftType.add(teacherDTO);
            } else if (containsShiftType(teacherDTO, ShiftType.LABORATORIAL)) {
                laboratoryShiftType.add(teacherDTO);
            } else {
                otherShiftTypes.add(teacherDTO);
            }
        }
        finalResult.addAll(theoricalShiftType);
        finalResult.addAll(praticalShiftType);
        finalResult.addAll(laboratoryShiftType);
        finalResult.addAll(otherShiftTypes);
        return finalResult;
    }

    private boolean containsShiftType(AffiliatedTeacherDTO teacherDTO, ShiftType shiftType) {
        for (StudentTeacherInquiryBean studentTeacherInquiryBean : getTeachersInquiries().get(teacherDTO)) {
            if (studentTeacherInquiryBean.getShiftType() == shiftType) {
                return true;
            }
        }
        return false;
    }

    public String validateCurricularInquiry() {
        String validationResult = null;
        for (InquiryBlockDTO inquiryBlockDTO : getCurricularCourseBlocks()) {
            validationResult = inquiryBlockDTO.validate(getCurricularCourseBlocks());
            if (!Boolean.valueOf(validationResult)) {
                return validationResult;
            }
        }
        return Boolean.toString(true);
    }

    public Set getTeachers() {
        return getTeachersInquiries().entrySet();
    }

    public Set<InquiryBlockDTO> getCurricularCourseBlocks() {
        return curricularCourseBlocks;
    }

    public void setCurricularCourseBlocks(Set<InquiryBlockDTO> curricularCourseBlocks) {
        this.curricularCourseBlocks = curricularCourseBlocks;
    }

    public StudentInquiryRegistry getInquiryRegistry() {
        return inquiryRegistry;
    }

    public void setInquiryRegistry(StudentInquiryRegistry inquiryRegistry) {
        this.inquiryRegistry = inquiryRegistry;
    }

    public Map<AffiliatedTeacherDTO, List<? extends StudentTeacherInquiryBean>> getTeachersInquiries() {
        return teachersInquiries;
    }

    public void setTeachersInquiries(Map<AffiliatedTeacherDTO, List<? extends StudentTeacherInquiryBean>> teachersInquiries) {
        this.teachersInquiries = teachersInquiries;
    }

    public DateTime getStartedWhen() {
        return startedWhen;
    }

    public void setStartedWhen(DateTime startedWhen) {
        this.startedWhen = startedWhen;
    }

    @Atomic
    public void setAnsweredInquiry() {
        if (getInquiryRegistry().getState() == InquiriesRegistryState.ANSWERED) {
            return;
        }
        InquiryCourseAnswer inquiryCourseAnswer = new InquiryCourseAnswer();
        DateTime endTime = new DateTime();
        inquiryCourseAnswer.setAnswerDuration(endTime.getMillis() - getStartedWhen().getMillis());
        inquiryCourseAnswer.setAttendenceClassesPercentage(getInquiryRegistry().getAttendenceClassesPercentage());

        inquiryCourseAnswer.setEntryGrade(InquiryGradesInterval.getInterval(getInquiryRegistry().getRegistration()
                .getEntryGrade()));
        inquiryCourseAnswer.setExecutionCourse(getInquiryRegistry().getExecutionCourse());
        inquiryCourseAnswer.setExecutionDegreeStudent(getInquiryRegistry().getRegistration().getLastStudentCurricularPlan()
                .getDegreeCurricularPlan().getMostRecentExecutionDegree());
        ExecutionDegree executionDegreeCourse =
                ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(getInquiryRegistry().getCurricularCourse()
                        .getDegreeCurricularPlan(), getInquiryRegistry().getExecutionPeriod().getExecutionYear());
        inquiryCourseAnswer.setExecutionDegreeCourse(executionDegreeCourse);
        inquiryCourseAnswer.setExecutionPeriod(getInquiryRegistry().getExecutionPeriod());

        inquiryCourseAnswer.setNumberOfEnrolments(InquiryCourseAnswer.getNumberOfEnrolments(getInquiryRegistry()));
        inquiryCourseAnswer.setResponseDateTime(endTime);
        inquiryCourseAnswer.setStudentType(getInquiryRegistry().getRegistration().getRegistrationAgreement());
        inquiryCourseAnswer.setStudyDaysSpentInExamsSeason(getInquiryRegistry().getStudyDaysSpentInExamsSeason());
        for (Shift enrolledShift : getInquiryRegistry().getRegistration().getShiftsFor(getInquiryRegistry().getExecutionCourse())) {
            inquiryCourseAnswer.addEnrolledShifts(enrolledShift);
        }
        final StudentInquiryExecutionPeriod studentInquiryExecutionPeriod =
                getInquiryRegistry().getRegistration().getStudent()
                        .getStudentInquiryExecutionPeriod(getInquiryRegistry().getExecutionPeriod());
        inquiryCourseAnswer.setWeeklyHoursSpentInAutonomousWork(studentInquiryExecutionPeriod
                .getWeeklyHoursSpentInClassesSeason());
        inquiryCourseAnswer.setWeeklyHoursSpentPercentage(getInquiryRegistry().getWeeklyHoursSpentPercentage());

        inquiryCourseAnswer.setCommittedFraud(Boolean.FALSE);//TODO actualmente não existe registo desta info no fenix
        inquiryCourseAnswer.setGrade(getInquiryRegistry().getLastGradeInterval());
        for (InquiryBlockDTO inquiryBlockDTO : getCurricularCourseBlocks()) {
            for (InquiryGroupQuestionBean groupQuestionBean : inquiryBlockDTO.getInquiryGroups()) {
                for (InquiryQuestionDTO questionDTO : groupQuestionBean.getInquiryQuestions()) {
                    if (!StringUtils.isEmpty(questionDTO.getResponseValue())) {
                        QuestionAnswer questionAnswer =
                                new QuestionAnswer(inquiryCourseAnswer, questionDTO.getInquiryQuestion(),
                                        questionDTO.getFinalValue());
                    }
                }
            }
        }

        for (AffiliatedTeacherDTO teacherDTO : getTeachersInquiries().keySet()) {
            for (StudentTeacherInquiryBean teacherInquiryBean : getTeachersInquiries().get(teacherDTO)) {
                if (teacherInquiryBean.isFilled() && teacherInquiryBean.isInquiryFilledIn()) {
                    InquiryStudentTeacherAnswer inquiryTeacherAnswer = new InquiryStudentTeacherAnswer();

                    inquiryTeacherAnswer.setProfessorship(teacherInquiryBean.getExecutionCourse().getProfessorship(
                            teacherDTO.getTeacher()));

                    inquiryTeacherAnswer.setShiftType(teacherInquiryBean.getShiftType());
                    inquiryTeacherAnswer.setInquiryCourseAnswer(inquiryCourseAnswer);
                    for (InquiryBlockDTO inquiryBlockDTO : teacherInquiryBean.getTeacherInquiryBlocks()) {
                        for (InquiryGroupQuestionBean groupQuestionBean : inquiryBlockDTO.getInquiryGroups()) {
                            for (InquiryQuestionDTO questionDTO : groupQuestionBean.getInquiryQuestions()) {
                                QuestionAnswer questionAnswer =
                                        new QuestionAnswer(inquiryTeacherAnswer, questionDTO.getInquiryQuestion(),
                                                questionDTO.getFinalValue());
                            }
                        }
                    }
                }
            }
        }
        getInquiryRegistry().setState(InquiriesRegistryState.ANSWERED);
    }
}
