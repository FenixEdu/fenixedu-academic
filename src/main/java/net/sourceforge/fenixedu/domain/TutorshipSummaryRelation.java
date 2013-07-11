package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorshipSummaryRelationBean;
import pt.ist.fenixframework.Atomic;

public class TutorshipSummaryRelation extends TutorshipSummaryRelation_Base {

    public TutorshipSummaryRelation(final Tutorship tutorship, final TutorshipSummary tutorshipSummary) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setTutorship(tutorship);
        setTutorshipSummary(tutorshipSummary);
    }

    public StudentCurricularPlan getStudentPlan() {
        return getTutorship().getStudentCurricularPlan();
    }

    @Atomic
    public void update(final TutorshipSummaryRelationBean bean) {
        setParticipationType(bean.getParticipationType());
        setWithoutEnrolments(bean.isWithoutEnrolments());
        setHighPerformance(bean.isHighPerformance());
        setHighPerformance(bean.isHighPerformance());
        setLowPerformance(bean.isLowPerformance());
        setOutOfTouch(bean.isOutOfTouch());
        setParticipationNone(bean.isParticipationNone());
        setParticipationRegularly(bean.isParticipationRegularly());
    }

    @Atomic
    static public TutorshipSummaryRelation create(final TutorshipSummaryRelationBean bean) {
        TutorshipSummaryRelation tutorshipSummaryRelation =
                new TutorshipSummaryRelation(bean.getTutorship(), bean.getTutorshipSummary());

        tutorshipSummaryRelation.setParticipationType(bean.getParticipationType());
        tutorshipSummaryRelation.setWithoutEnrolments(bean.isWithoutEnrolments());
        tutorshipSummaryRelation.setHighPerformance(bean.isHighPerformance());
        tutorshipSummaryRelation.setLowPerformance(bean.isLowPerformance());
        tutorshipSummaryRelation.setOutOfTouch(bean.isOutOfTouch());
        tutorshipSummaryRelation.setParticipationNone(bean.isParticipationNone());
        tutorshipSummaryRelation.setParticipationRegularly(bean.isParticipationRegularly());

        return tutorshipSummaryRelation;
    }

}
