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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class StudentThesisInfo implements Serializable {

    private Student student;
    private Enrolment enrolment;
    private Thesis thesis;
    private ThesisPresentationState state;

    public StudentThesisInfo(Enrolment enrolment) {
        setStudent(enrolment.getStudent());
        setEnrolment(enrolment);
        setThesis(enrolment.getThesis());
    }

    public Student getStudent() {
        return this.student;
    }

    protected void setStudent(Student student) {
        this.student = student;
    }

    public Enrolment getEnrolment() {
        return this.enrolment;
    }

    protected void setEnrolment(Enrolment enrolment) {
        this.enrolment = enrolment;
    }

    public Thesis getThesis() {
        return this.thesis;
    }

    protected void setThesis(Thesis thesis) {
        this.thesis = thesis;

        setState(thesis);
    }

    public String getSemester() {
        final Enrolment enrolment = getEnrolment();
        final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
        return curricularCourse.isAnual() ? "" : enrolment.getExecutionPeriod().getSemester().toString();
    }

    public MultiLanguageString getTitle() {
        return getEnrolment().getPossibleDissertationTitle();
    }

    public ThesisPresentationState getState() {
        return this.state;
    }

    private void setState(Thesis thesis) {
        this.state = ThesisPresentationState.getThesisPresentationState(thesis);
    }

    public String getThesisId() {
        final Thesis thesis = getThesis();

        return thesis == null ? null : thesis.getExternalId();
    }

    public String getEnrolmentOID() {
        final Enrolment enrolment = getEnrolment();
        return enrolment == null ? null : enrolment.getExternalId();
    }

    public boolean isUnassigned() {
        return getThesis() == null;
    }

    public boolean isDraft() {
        return getThesis() != null && getThesis().isDraft();
    }

    public boolean isSubmitted() {
        return getThesis() != null && getThesis().isSubmitted();
    }

    public boolean isWaitingConfirmation() {
        return getThesis() != null && getThesis().isWaitingConfirmation();
    }

    public boolean isConfirmed() {
        return getThesis() != null && getThesis().isConfirmed();
    }

    public boolean isEvaluated() {
        return getThesis() != null && getThesis().isEvaluated();
    }

    public boolean isPreEvaluated() {
        return isEvaluated() && !getThesis().isFinalThesis();
    }

    public boolean isSubmittedAndIsCoordinatorAndNotOrientator() {
        return getThesis() != null && getThesis().isSubmittedAndIsCoordinatorAndNotOrientator();
    }

    public String getProposalYear() {
        String mostRecentYear = "0000";
        for (GroupStudent groupStudent : getEnrolment().getRegistration().getAssociatedGroupStudents()) {
            Proposal proposal = groupStudent.getFinalDegreeWorkProposalConfirmation();
            if (proposal != null && proposal.getAttributionStatus().isFinalAttribution()) {
                String proposalYear = proposal.getScheduleing().getExecutionYearOfOneExecutionDegree().getNextYearsYearString();
                if (Integer.parseInt(mostRecentYear.substring(0, 3)) < Integer.parseInt(proposalYear.substring(0, 3))) {
                    mostRecentYear = proposalYear;
                }
            }
        }
        return (mostRecentYear.equals("0000")) ? "-" : mostRecentYear;
    }

    public boolean getHasMadeProposalPreviousYear() {
        ExecutionYear enrolmentExecutionYear = getEnrolment().getExecutionYear();
        for (GroupStudent groupStudent : getEnrolment().getRegistration().getAssociatedGroupStudents()) {
            Proposal proposal = groupStudent.getFinalDegreeWorkProposalConfirmation();
            if (proposal != null && proposal.isForExecutionYear(enrolmentExecutionYear.getPreviousExecutionYear())
                    && proposal.getAttributionStatus().isFinalAttribution()) {
                return true;
            }
        }
        return false;
    }
}
