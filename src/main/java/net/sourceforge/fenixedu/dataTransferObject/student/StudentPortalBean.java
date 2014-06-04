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
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.EvaluationType;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.YearMonthDay;

public class StudentPortalBean implements Serializable {

    public class ExecutionCoursesAnnouncements {

        public class EvaluationAnnouncement {
            private String evaluationType;
            private String identification;
            private String realization;
            private String register;
            private String enrolment;
            private String room;

            private boolean realizationPast;
            private boolean enrolmentElapsing;
            private boolean enrolmentPast;

            private boolean registered;
            private boolean groupEnrolment;

            public EvaluationAnnouncement(WrittenTest writtenTest) {
                setEvaluationType(writtenTest.getEvaluationType().toString());
                setIdentification(writtenTest.getDescription());
                setRegister(isStudentEnrolled(writtenTest));
                setRealization(writtenTest);
                setEnrolment(writtenTest);
                setRoom(writtenTest);
                setGroupEnrolment(false);
            }

            public EvaluationAnnouncement(Exam exam) {
                setEvaluationType(exam.getEvaluationType().toString());
                setIdentification(exam.getName());
                setRegister(isStudentEnrolled(exam));
                setRealization(exam);
                setEnrolment(exam);
                setRoom(exam);
                setGroupEnrolment(false);
            }

            public EvaluationAnnouncement(Grouping grouping) {
                setEvaluationType(BundleUtil.getString(Bundle.APPLICATION, "label.grouping"));
                setIdentification(grouping.getName());
                setRegister(isStudentEnrolled(grouping));
                setRealization(grouping);
                setEnrolment(grouping);
                setRoom("-");
                setGroupEnrolment(true);
            }

            private boolean isStudentEnrolled(WrittenEvaluation writtenEvaluation) {
                for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : writtenEvaluation
                        .getWrittenEvaluationEnrolments()) {
                    if (writtenEvaluationEnrolment.getStudent() != null
                            && writtenEvaluationEnrolment.getStudent().getStudent() == getStudent()) {
                        return true;
                    }
                }
                return false;
            }

            private boolean isStudentEnrolled(Grouping grouping) {
                for (final StudentGroup studentGroup : grouping.getStudentGroupsSet()) {
                    for (Attends attends : studentGroup.getAttends()) {
                        if (attends.getAluno().getStudent() == getStudent()) {
                            return true;
                        }
                    }
                }
                return false;
            }

            public String getEvaluationType() {
                return evaluationType;
            }

            public String getIdentification() {
                return identification;
            }

            public String getRealization() {
                return realization;
            }

            public String getEnrolment() {
                return enrolment;
            }

            public String getRoom() {
                return room;
            }

            public String getRegister() {
                return register;
            }

            public boolean getRegistered() {
                return registered;
            }

            public boolean getRealizationPast() {
                return realizationPast;
            }

            public boolean getEnrolmentElapsing() {
                return enrolmentElapsing;
            }

            public boolean getEnrolmentPast() {
                return enrolmentPast;
            }

            public String getStatus() {
                /*
                 * <logic:equal name="evaluationAnnouncement"
                 * property="evaluationType" value="Agrupamento"> <logic:equal
                 * name="evaluationAnnouncement" property="registered"
                 * value="true"> <tr> </logic:equal> <logic:equal
                 * name="evaluationAnnouncement" property="registered"
                 * value="false"> <logic:equal name="evaluationAnnouncement"
                 * property="enrolmentPast" value="true"> <tr class="disabled">
                 * </logic:equal> <logic:equal name="evaluationAnnouncement"
                 * property="enrolmentPast" value="false"> <tr> </logic:equal>
                 * </logic:equal> </logic:equal> <logic:notEqual
                 * name="evaluationAnnouncement" property="evaluationType"
                 * value="Agrupamento"> <logic:equal
                 * name="evaluationAnnouncement" property="realizationPast"
                 * value="true"> <tr class="disabled"> </logic:equal>
                 * <logic:equal name="evaluationAnnouncement"
                 * property="realizationPast" value="false"> <logic:equal
                 * name="evaluationAnnouncement" property="registered"
                 * value="true"> <tr> </logic:equal> <logic:equal
                 * name="evaluationAnnouncement" property="registered"
                 * value="false"> <logic:equal name="evaluationAnnouncement"
                 * property="enrolmentElapsing" value="true"> <tr
                 * class="elapsing"> <bean:define id="evaluationElapsing"
                 * value="true" /> </logic:equal> <logic:equal
                 * name="evaluationAnnouncement" property="enrolmentElapsing"
                 * value="false"> <tr> </logic:equal> </logic:equal>
                 * </logic:equal> </logic:notEqual>
                 */

                if (getEvaluationType().equals("Agrupamento")) {
                    if (getRegistered()) {

                    } else {
                        if (getEnrolmentPast()) {
                            return "disabled";
                        } else {

                        }
                    }
                } else {
                    if (getRealizationPast()) {
                        return "disabled";
                    } else {
                        if (getRegistered()) {

                        } else {
                            if (getEnrolmentElapsing()) {
                                return "elapsing";
                            } else {

                            }
                        }
                    }
                }

                return "";
            }

            public void setEvaluationType(String evaluationType) {
                this.evaluationType = evaluationType;
            }

            public void setIdentification(String identification) {
                this.identification = identification;
            }

            public void setRealization(WrittenEvaluation writtenEvaluation) {
                this.realizationPast = writtenEvaluation.getBeginningDateTime().isBeforeNow();

                this.realization =
                        YearMonthDay.fromDateFields(writtenEvaluation.getBeginningDateTime().toDate()).toString()
                                + " "
                                + writtenEvaluation.getBeginningDateTime().getHourOfDay()
                                + ":"
                                + (writtenEvaluation.getBeginningDateTime().getMinuteOfHour() == 0 ? "00" : writtenEvaluation
                                        .getBeginningDateTime().getMinuteOfHour());
            }

            public void setRealization(Grouping grouping) {
                this.realization = "-";
            }

            private DateTime toDateTime(Calendar date, Calendar time) {
                if (date == null || time == null) {
                    return new DateTime((Calendar) null);
                }
                return new DateTime(date.getTimeInMillis()).withFields(new LocalTime(time.getTimeInMillis()));
            }

            public void setEnrolment(WrittenEvaluation writtenEvaluation) {
                final Calendar endDay = writtenEvaluation.getEnrollmentEndDay();

                this.enrolmentPast = new DateTime(endDay).isBeforeNow();

                final Calendar beginDay = writtenEvaluation.getEnrollmentBeginDay();

                final Calendar beginTime = writtenEvaluation.getEnrollmentBeginTime();
                final Calendar endTime = writtenEvaluation.getEnrollmentEndTime();

                final DateTime beginDateTime = toDateTime(beginDay, beginTime);
                final DateTime endDateTime = toDateTime(endDay, endTime);

                Interval interval = new Interval(beginDateTime, endDateTime);

                this.enrolmentElapsing = interval.containsNow();

                if (writtenEvaluation.getEnrollmentBeginDayDateYearMonthDay() != null
                        && writtenEvaluation.getEnrollmentEndDayDateYearMonthDay() != null) {
                    this.enrolment =
                            writtenEvaluation.getEnrollmentBeginDayDateYearMonthDay().toString() + " "
                                    + BundleUtil.getString(Bundle.STUDENT, "message.out.until") + " "
                                    + writtenEvaluation.getEnrollmentEndDayDateYearMonthDay().toString();
                } else {
                    this.enrolment = "-";
                    this.register = "-";
                }
            }

            public void setEnrolment(Grouping grouping) {
                this.enrolmentPast = new DateTime(grouping.getEnrolmentEndDay()).isBeforeNow();
                this.enrolmentElapsing =
                        new DateTime(grouping.getEnrolmentBeginDay()).isBeforeNow()
                                && new DateTime(grouping.getEnrolmentEndDay()).isAfterNow();

                this.enrolment =
                        YearMonthDay.fromDateFields(grouping.getEnrolmentBeginDayDate()).toString() + " "
                                + BundleUtil.getString(Bundle.STUDENT, "message.out.until") + " "
                                + YearMonthDay.fromDateFields(grouping.getEnrolmentEndDayDate()).toString();
            }

            public void setRoom(WrittenEvaluation writtenEvaluation) {
                for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : writtenEvaluation
                        .getWrittenEvaluationEnrolments()) {
                    if (writtenEvaluationEnrolment.getStudent() != null
                            && writtenEvaluationEnrolment.getStudent().getStudent() == getStudent()) {
                        if (writtenEvaluationEnrolment.getRoom() != null) {
                            this.room = writtenEvaluationEnrolment.getRoom().getName();
                            return;
                        } else {
                            break;
                        }
                    }
                }
                if (writtenEvaluation.getAssociatedRooms().isEmpty() == false) {
                    this.room = writtenEvaluation.getAssociatedRoomsAsString();
                } else {
                    this.room = "-";
                }
            }

            public void setRoom(String room) {
                this.room = room;
            }

            public void setRegister(Boolean registered) {
                final String label = registered ? "label.enroled" : "message.out.not.enrolled";
                this.register = BundleUtil.getString(Bundle.STUDENT, label);
                setRegistered(registered);
            }

            public void setRegistered(Boolean registered) {
                this.registered = registered;
            }

            public void setGroupEnrolment(boolean groupEnrolment) {
                this.groupEnrolment = groupEnrolment;
            }

            public boolean isGroupEnrolment() {
                return groupEnrolment;
            }
        }

        private ExecutionCourse executionCourse;
        private List<EvaluationAnnouncement> evaluationAnnouncements;

        public ExecutionCoursesAnnouncements(ExecutionCourse executionCourse) {
            setExecutionCourse(executionCourse);
            setEvaluationAnnouncements(new ArrayList<EvaluationAnnouncement>());
            for (Evaluation evaluation : executionCourse.getOrderedAssociatedEvaluations()) {
                if (evaluation.getEvaluationType() == EvaluationType.TEST_TYPE) {
                    addEvaluationAnnouncement(new EvaluationAnnouncement((WrittenTest) evaluation));
                } else if (evaluation.getEvaluationType() == EvaluationType.EXAM_TYPE) {
                    addEvaluationAnnouncement(new EvaluationAnnouncement((Exam) evaluation));
                }
            }
            for (Grouping grouping : executionCourse.getGroupings()) {
                addEvaluationAnnouncement(new EvaluationAnnouncement(grouping));
            }
        }

        public ExecutionCourse getExecutionCourse() {
            return executionCourse;
        }

        public List<EvaluationAnnouncement> getEvaluationAnnouncements() {
            Collections.sort(evaluationAnnouncements, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    EvaluationAnnouncement e1 = (EvaluationAnnouncement) o1;
                    EvaluationAnnouncement e2 = (EvaluationAnnouncement) o2;

                    if (!e1.getStatus().equals("disabled") && e2.getStatus().equals("disabled")) {
                        return -1;
                    }

                    return 1;
                }
            });

            return evaluationAnnouncements;
        }

        public void setEvaluationAnnouncements(List<EvaluationAnnouncement> evaluationAnnouncements) {
            this.evaluationAnnouncements = evaluationAnnouncements;
        }

        public void addEvaluationAnnouncement(EvaluationAnnouncement evaluationAnnouncement) {
            getEvaluationAnnouncements().add(evaluationAnnouncement);
        }

        public void setExecutionCourse(ExecutionCourse executionCourse) {
            this.executionCourse = executionCourse;
        }
    }

    private Degree degree;
    private Student student;
    private List<ExecutionCoursesAnnouncements> executionCoursesAnnouncements;

    public StudentPortalBean(final Degree degree, final Student student, final Set<ExecutionCourse> executionCourses,
            final DegreeCurricularPlan activeDegreeCurricularPlan) {
        super();
        setDegree(degree);
        setStudent(student);
        setExecutionCoursesAnnouncements(new ArrayList<ExecutionCoursesAnnouncements>());
        for (ExecutionCourse executionCourse : executionCourses) {
            addExecutionCoursesAnnouncement(new ExecutionCoursesAnnouncements(executionCourse));
        }
        Collections.sort(getExecutionCoursesAnnouncements(), new BeanComparator("executionCourse.name"));
    }

    public Degree getDegree() {
        return degree;
    }

    public Student getStudent() {
        return student;
    }

    public List<ExecutionCoursesAnnouncements> getExecutionCoursesAnnouncements() {
        return executionCoursesAnnouncements;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setExecutionCoursesAnnouncements(List<ExecutionCoursesAnnouncements> executionCoursesAnnouncements) {
        this.executionCoursesAnnouncements = executionCoursesAnnouncements;
    }

    public void addExecutionCoursesAnnouncement(ExecutionCoursesAnnouncements executionCoursesAnnouncement) {
        getExecutionCoursesAnnouncements().add(executionCoursesAnnouncement);
    }
}
