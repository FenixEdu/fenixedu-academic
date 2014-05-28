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

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.CreateSummaryBean;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorshipSummaryRelationBean;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class TutorshipSummary extends TutorshipSummary_Base {

    public TutorshipSummary() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public boolean isActive() {
        if (getSemester().getTutorshipSummaryPeriod() == null) {
            return false;
        }

        LocalDate curDate = new LocalDate();
        LocalDate beginDate = getSemester().getTutorshipSummaryPeriod().getBeginDate();
        LocalDate endDate = getSemester().getTutorshipSummaryPeriod().getEndDate();

        return !(curDate.isBefore(beginDate) || curDate.isAfter(endDate));
    }

    @Atomic
    public void update(final CreateSummaryBean bean, boolean relations) {
        setHowManyContactsEmail(bean.getHowManyContactsEmail());
        setHowManyContactsPhone(bean.getHowManyContactsPhone());
        setHowManyReunionsGroup(bean.getHowManyReunionsGroup());
        setHowManyReunionsIndividual(bean.getHowManyReunionsIndividual());

        setTutorshipSummarySatisfaction(bean.getTutorshipSummarySatisfaction());
        setTutorshipSummaryProgramAssessment(bean.getTutorshipSummaryProgramAssessment());
        setDifficulties(bean.getDifficulties());
        setGains(bean.getGains());
        setSuggestions(bean.getSuggestions());

        setProblemsR1(bean.getProblemsR1());
        setProblemsR2(bean.getProblemsR2());
        setProblemsR3(bean.getProblemsR3());
        setProblemsR4(bean.getProblemsR4());
        setProblemsR5(bean.getProblemsR5());
        setProblemsR6(bean.getProblemsR6());
        setProblemsR7(bean.getProblemsR7());
        setProblemsR8(bean.getProblemsR8());
        setProblemsR9(bean.getProblemsR9());
        setProblemsR10(bean.getProblemsR10());
        setProblemsOther(bean.getProblemsOther());

        setGainsR1(bean.getGainsR1());
        setGainsR2(bean.getGainsR2());
        setGainsR3(bean.getGainsR3());
        setGainsR4(bean.getGainsR4());
        setGainsR5(bean.getGainsR5());
        setGainsR6(bean.getGainsR6());
        setGainsR7(bean.getGainsR7());
        setGainsR8(bean.getGainsR8());
        setGainsR9(bean.getGainsR9());
        setGainsR10(bean.getGainsR10());
        setGainsOther(bean.getGainsOther());

        if (relations) {
            for (TutorshipSummaryRelationBean relationBean : bean.getTutorshipRelations()) {
                relationBean.save();
            }
        }
    }

    @Atomic
    static public TutorshipSummary create(final CreateSummaryBean bean) {
        TutorshipSummary tutorshipSummary = new TutorshipSummary();

        tutorshipSummary.setDegree(bean.getDegree());
        tutorshipSummary.setTeacher(bean.getTeacher());
        tutorshipSummary.setSemester(bean.getExecutionSemester());

        tutorshipSummary.update(bean, false);

        for (TutorshipSummaryRelationBean relationBean : bean.getTutorshipRelations()) {
            relationBean.setTutorshipSummary(tutorshipSummary);

            relationBean.save();
        }

        return tutorshipSummary;
    }

    public static Set<ExecutionSemester> getActivePeriods() {
        Set<ExecutionSemester> semesters = new HashSet<ExecutionSemester>();
        for (TutorshipSummaryPeriod tutorshipPeriod : Bennu.getInstance().getTutorshipSummaryPeriodsSet()) {
            if (tutorshipPeriod.isOpenNow()) {
                semesters.add(tutorshipPeriod.getExecutionSemester());
            }
        }
        return semesters;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TutorshipSummaryRelation> getTutorshipSummaryRelations() {
        return getTutorshipSummaryRelationsSet();
    }

    @Deprecated
    public boolean hasAnyTutorshipSummaryRelations() {
        return !getTutorshipSummaryRelationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDifficulties() {
        return getDifficulties() != null;
    }

    @Deprecated
    public boolean hasHowManyReunionsGroup() {
        return getHowManyReunionsGroup() != null;
    }

    @Deprecated
    public boolean hasSuggestions() {
        return getSuggestions() != null;
    }

    @Deprecated
    public boolean hasGainsR6() {
        return getGainsR6() != null;
    }

    @Deprecated
    public boolean hasProblemsR4() {
        return getProblemsR4() != null;
    }

    @Deprecated
    public boolean hasGainsR7() {
        return getGainsR7() != null;
    }

    @Deprecated
    public boolean hasProblemsR5() {
        return getProblemsR5() != null;
    }

    @Deprecated
    public boolean hasProblemsR10() {
        return getProblemsR10() != null;
    }

    @Deprecated
    public boolean hasGainsR10() {
        return getGainsR10() != null;
    }

    @Deprecated
    public boolean hasGainsR8() {
        return getGainsR8() != null;
    }

    @Deprecated
    public boolean hasProblemsR2() {
        return getProblemsR2() != null;
    }

    @Deprecated
    public boolean hasSemester() {
        return getSemester() != null;
    }

    @Deprecated
    public boolean hasGainsR9() {
        return getGainsR9() != null;
    }

    @Deprecated
    public boolean hasProblemsR3() {
        return getProblemsR3() != null;
    }

    @Deprecated
    public boolean hasGains() {
        return getGains() != null;
    }

    @Deprecated
    public boolean hasHowManyReunionsIndividual() {
        return getHowManyReunionsIndividual() != null;
    }

    @Deprecated
    public boolean hasProblemsR1() {
        return getProblemsR1() != null;
    }

    @Deprecated
    public boolean hasHowManyContactsEmail() {
        return getHowManyContactsEmail() != null;
    }

    @Deprecated
    public boolean hasProblemsOther() {
        return getProblemsOther() != null;
    }

    @Deprecated
    public boolean hasGainsR1() {
        return getGainsR1() != null;
    }

    @Deprecated
    public boolean hasProblemsR8() {
        return getProblemsR8() != null;
    }

    @Deprecated
    public boolean hasGainsR2() {
        return getGainsR2() != null;
    }

    @Deprecated
    public boolean hasProblemsR9() {
        return getProblemsR9() != null;
    }

    @Deprecated
    public boolean hasGainsR3() {
        return getGainsR3() != null;
    }

    @Deprecated
    public boolean hasTutorshipSummarySatisfaction() {
        return getTutorshipSummarySatisfaction() != null;
    }

    @Deprecated
    public boolean hasProblemsR6() {
        return getProblemsR6() != null;
    }

    @Deprecated
    public boolean hasGainsR4() {
        return getGainsR4() != null;
    }

    @Deprecated
    public boolean hasGainsR5() {
        return getGainsR5() != null;
    }

    @Deprecated
    public boolean hasProblemsR7() {
        return getProblemsR7() != null;
    }

    @Deprecated
    public boolean hasGainsOther() {
        return getGainsOther() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasTutorshipSummaryProgramAssessment() {
        return getTutorshipSummaryProgramAssessment() != null;
    }

    @Deprecated
    public boolean hasHowManyContactsPhone() {
        return getHowManyContactsPhone() != null;
    }

}
