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
package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.joda.time.DateTime;

public class ManageEnrolmentsBean implements Serializable {

    static private final long serialVersionUID = 1L;

    private PhdIndividualProgramProcess process;

    private ExecutionSemester semester;

    private Collection<Enrolment> enrolmentsPerformedByStudent;

    private Collection<Enrolment> remainingEnrolments;

    private Collection<EnrolmentPeriod> enrolmentPeriods;

    private List<Enrolment> enrolmentsToValidate;

    private List<DegreeCurricularPlan> degreeCurricularPlans;

    private CurricularCourse curricularCourse;

    private DateTime startDate, endDate;

    private String mailSubject, mailBody;

    public PhdIndividualProgramProcess getProcess() {
        return process;
    }

    public void setProcess(PhdIndividualProgramProcess process) {
        this.process = process;
    }

    public ExecutionSemester getSemester() {
        return semester;
    }

    public void setSemester(ExecutionSemester semester) {
        this.semester = semester;
    }

    public Collection<Enrolment> getEnrolmentsPerformedByStudent() {
        return enrolmentsPerformedByStudent;
    }

    public void setEnrolmentsPerformedByStudent(Collection<Enrolment> enrolmentsPerformedByStudent) {
        this.enrolmentsPerformedByStudent = enrolmentsPerformedByStudent;
    }

    public Collection<Enrolment> getRemainingEnrolments() {
        return remainingEnrolments;
    }

    public void setRemainingEnrolments(Collection<Enrolment> remainingEnrolments) {
        this.remainingEnrolments = remainingEnrolments;
    }

    public Collection<EnrolmentPeriod> getEnrolmentPeriods() {
        return enrolmentPeriods;
    }

    public void setEnrolmentPeriods(Collection<EnrolmentPeriod> enrolmentPeriods) {
        this.enrolmentPeriods = enrolmentPeriods;
    }

    public List<Enrolment> getEnrolmentsToValidate() {
        return enrolmentsToValidate;
    }

    public void setEnrolmentsToValidate(List<Enrolment> enrolmentsToValidate) {
        this.enrolmentsToValidate = enrolmentsToValidate;
    }

    public List<DegreeCurricularPlan> getDegreeCurricularPlans() {
        return degreeCurricularPlans;
    }

    public void setDegreeCurricularPlans(List<DegreeCurricularPlan> degreeCurricularPlans) {
        this.degreeCurricularPlans = degreeCurricularPlans;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public CurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    public void setCurricularCourse(final CurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    public String getCurricularCourseName() {
        return getCurricularCourse().getName(getSemester());
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailBody() {
        return mailBody;
    }

    public void setMailBody(String mailBody) {
        this.mailBody = mailBody;
    }

}