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

import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorshipSummaryRelationBean;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class TutorshipSummaryRelation extends TutorshipSummaryRelation_Base {

    public TutorshipSummaryRelation(final Tutorship tutorship, final TutorshipSummary tutorshipSummary) {
        super();
        setRootDomainObject(Bennu.getInstance());
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

    @Deprecated
    public boolean hasTutorship() {
        return getTutorship() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasLowPerformance() {
        return getLowPerformance() != null;
    }

    @Deprecated
    public boolean hasOutOfTouch() {
        return getOutOfTouch() != null;
    }

    @Deprecated
    public boolean hasHighPerformance() {
        return getHighPerformance() != null;
    }

    @Deprecated
    public boolean hasWithoutEnrolments() {
        return getWithoutEnrolments() != null;
    }

    @Deprecated
    public boolean hasTutorshipSummary() {
        return getTutorshipSummary() != null;
    }

    @Deprecated
    public boolean hasParticipationRegularly() {
        return getParticipationRegularly() != null;
    }

    @Deprecated
    public boolean hasParticipationType() {
        return getParticipationType() != null;
    }

    @Deprecated
    public boolean hasParticipationNone() {
        return getParticipationNone() != null;
    }

}
