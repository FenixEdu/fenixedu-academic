package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.TutorshipParticipationType;
import net.sourceforge.fenixedu.domain.TutorshipSummary;
import net.sourceforge.fenixedu.domain.TutorshipSummaryRelation;

public class TutorshipSummaryRelationBean implements Serializable {

    private static final long serialVersionUID = 161590136110944806L;

    private Tutorship tutorship;
    private TutorshipSummary tutorshipSummary;

    private boolean participationRegularly;
    private boolean participationNone;
    private boolean outOfTouch;
    private boolean highPerformance;
    private boolean lowPerformance;
    private TutorshipParticipationType participationType;

    private TutorshipSummaryRelation tutorshipSummaryRelation;

    public TutorshipSummaryRelationBean(TutorshipSummaryRelation tutorshipSummaryRelation) {
	this.tutorshipSummaryRelation = tutorshipSummaryRelation;

	this.tutorship = tutorshipSummaryRelation.getTutorship();
	this.tutorshipSummary = tutorshipSummaryRelation.getTutorshipSummary();

	this.participationRegularly = tutorshipSummaryRelation.getParticipationRegularly();
	this.participationNone = tutorshipSummaryRelation.getParticipationNone();
	this.outOfTouch = tutorshipSummaryRelation.getOutOfTouch();
	this.highPerformance = tutorshipSummaryRelation.getHighPerformance();
	this.lowPerformance = tutorshipSummaryRelation.getLowPerformance();

	this.participationType = tutorshipSummaryRelation.getParticipationType();
    }

    public TutorshipSummaryRelationBean(Tutorship tutorship) {
	this.tutorship = tutorship;
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