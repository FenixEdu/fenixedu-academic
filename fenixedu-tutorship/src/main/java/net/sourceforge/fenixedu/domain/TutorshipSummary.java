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
package org.fenixedu.academic.domain;

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.ui.struts.action.pedagogicalCouncil.CreateSummaryBean;
import org.fenixedu.academic.ui.struts.action.pedagogicalCouncil.TutorshipSummaryRelationBean;

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

}
