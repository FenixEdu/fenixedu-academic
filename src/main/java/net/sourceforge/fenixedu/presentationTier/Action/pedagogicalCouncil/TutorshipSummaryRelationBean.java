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
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.io.Serializable;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.TutorshipParticipationType;
import net.sourceforge.fenixedu.domain.TutorshipSummary;
import net.sourceforge.fenixedu.domain.TutorshipSummaryRelation;

public class TutorshipSummaryRelationBean implements Serializable {

    private static final long serialVersionUID = 161590136110944806L;

    private Tutorship tutorship;
    private TutorshipSummary tutorshipSummary;

    ExecutionSemester executionSemester;

    private boolean participationRegularly;
    private boolean participationNone;
    private boolean outOfTouch;
    private boolean highPerformance;
    private boolean lowPerformance;
    private TutorshipParticipationType participationType;
    private boolean withoutEnrolments;

    private TutorshipSummaryRelation tutorshipSummaryRelation;

    public TutorshipSummaryRelationBean(TutorshipSummaryRelation tutorshipSummaryRelation) {
        this.tutorshipSummaryRelation = tutorshipSummaryRelation;

        this.tutorship = tutorshipSummaryRelation.getTutorship();
        this.tutorshipSummary = tutorshipSummaryRelation.getTutorshipSummary();
        this.executionSemester = tutorshipSummaryRelation.getTutorshipSummary().getSemester();

        this.participationRegularly = tutorshipSummaryRelation.getParticipationRegularly();
        this.participationNone = tutorshipSummaryRelation.getParticipationNone();
        this.outOfTouch = tutorshipSummaryRelation.getOutOfTouch();
        this.highPerformance = tutorshipSummaryRelation.getHighPerformance();
        this.lowPerformance = tutorshipSummaryRelation.getLowPerformance();

        this.participationType = tutorshipSummaryRelation.getParticipationType();

        initPerformance();
    }

    public TutorshipSummaryRelationBean(Tutorship tutorship, ExecutionSemester executionSemester) {
        this.tutorship = tutorship;
        this.executionSemester = executionSemester;

        initPerformance();
    }

    public void save() {
        if (isPersisted()) {
            tutorshipSummaryRelation.update(this);
        } else {
            TutorshipSummaryRelation.create(this);
        }
    }

    public Tutorship getTutorship() {
        return tutorship;
    }

    public void setTutorship(Tutorship tutorship) {
        this.tutorship = tutorship;
    }

    public TutorshipSummary getTutorshipSummary() {
        return tutorshipSummary;
    }

    public void setTutorshipSummary(TutorshipSummary tutorshipSummary) {
        this.tutorshipSummary = tutorshipSummary;
    }

    public StudentCurricularPlan getStudentPlan() {
        return getTutorship().getStudentCurricularPlan();
    }

    public TutorshipParticipationType getParticipationType() {
        return participationType;
    }

    public void setParticipationType(TutorshipParticipationType participationType) {
        this.participationType = participationType;
    }

    public boolean isParticipationRegularly() {
        return participationRegularly;
    }

    public void setParticipationRegularly(boolean participationRegularly) {
        this.participationRegularly = participationRegularly;
    }

    public boolean isParticipationNone() {
        return participationNone;
    }

    public void setParticipationNone(boolean participationNone) {
        this.participationNone = participationNone;
    }

    public boolean isOutOfTouch() {
        return outOfTouch;
    }

    public void setOutOfTouch(boolean outOfTouch) {
        this.outOfTouch = outOfTouch;
    }

    public boolean isHighPerformance() {
        return highPerformance;
    }

    public boolean isWithoutEnrolments() {
        return withoutEnrolments;
    }

    public void initPerformance() {
        double totalEcts = 0;
        double approvedEcts = 0;

        Collection<Enrolment> enrolments = getTutorship().getStudent().getEnrolments(executionSemester);

        if (enrolments.isEmpty()) {
            highPerformance = false;
            lowPerformance = false;
            withoutEnrolments = true;
            return;
        }

        for (Enrolment enrolment : enrolments) {
            totalEcts += enrolment.getEctsCredits();

            if (enrolment.isApproved()) {
                approvedEcts += enrolment.getEctsCredits();
            }
        }

        this.highPerformance = (approvedEcts >= totalEcts);
        this.lowPerformance = (approvedEcts < (totalEcts / 2));
    }

    public void setHighPerformance(boolean highPerformance) {
        this.highPerformance = highPerformance;
    }

    public boolean isLowPerformance() {
        return lowPerformance;
    }

    public void setLowPerformance(boolean lowPerformance) {
        this.lowPerformance = lowPerformance;
    }

    public String getExternalId() {
        return tutorshipSummaryRelation.getExternalId();
    }

    public boolean isPersisted() {
        return tutorshipSummaryRelation != null;
    }
}
