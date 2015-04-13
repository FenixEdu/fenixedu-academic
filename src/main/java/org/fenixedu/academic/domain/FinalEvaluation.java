/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.InfoEvaluation;
import org.fenixedu.academic.dto.InfoFinalEvaluation;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.EvaluationType;
import org.fenixedu.bennu.core.i18n.BundleUtil;

/**
 * @author T�nia Pous�o
 * 
 */
public class FinalEvaluation extends FinalEvaluation_Base {

    public FinalEvaluation() {
        super();
        this.setGradeScale(GradeScale.TYPE20);
    }

    public Integer getGradesListVersion() {
        int lastVersion = 0;
        for (Mark mark : getMarksSet()) {
            FinalMark finalMark = (FinalMark) mark;
            if (finalMark.getGradeListVersion() > lastVersion) {
                lastVersion = finalMark.getGradeListVersion();
            }
        }
        if (lastVersion == 99) {
            throw new DomainException("grades list version cannot be higher than 99");
        }
        return Integer.valueOf(++lastVersion);
    }

    @Override
    public FinalMark addNewMark(Attends attends, String markValue) {
        if (attends.getMarkByEvaluation(this) != null) {
            throw new DomainException("error.Evaluation.attend.already.has.mark.for.evaluation");
        }
        return new FinalMark(attends, this, markValue);
    }

    @Override
    public EvaluationType getEvaluationType() {
        return EvaluationType.FINAL_TYPE;
    }

    public List<FinalMark> getAlreadySubmitedMarks(ExecutionCourse executionCourse) {
        List<FinalMark> result = new ArrayList<FinalMark>();
        for (Mark mark : getMarksSet()) {
            FinalMark finalMark = (FinalMark) mark;
            if (finalMark.getAttend().getExecutionCourse().equals(executionCourse) && finalMark.getGradeListVersion() != 0
                    && finalMark.getSubmitedMark() != null) {
                result.add(finalMark);
            }
        }
        return result;
    }

    public List<Attends> getNotSubmitedMarkAttends(ExecutionCourse executionCourse) {
        List<Attends> result = new ArrayList<Attends>();

        for (Attends attends : executionCourse.getAttendsSet()) {
            if (attends.getEnrolment() != null && attends.getRegistration().getDegreeType().isPreBolonhaDegree()) {
                FinalMark mark = getFinalMark(attends);
                if (mark == null || (mark.getGradeListVersion() == 0 && mark.getSubmitedMark() == null)) {
                    result.add(attends);
                }
            }
        }
        return result;
    }

    private FinalMark getFinalMark(Attends attends) {
        for (Mark mark : attends.getAssociatedMarksSet()) {
            if (mark.getEvaluation().equals(this)) {
                return (FinalMark) mark;
            }
        }
        return null;
    }

    private void checkRulesToDelete() {
        if (!getMarksSet().isEmpty()) {
            throw new DomainException("error.existing.marks");
        }
    }

    @Override
    public void delete() {
        checkRulesToDelete();
        super.delete();
    }

    @Override
    public boolean isFinal() {
        return true;
    }

    @Override
    public String getPresentationName() {
        return BundleUtil.getString(Bundle.APPLICATION, "label.final.evaluation");
    }

    @Override
    public Date getEvaluationDate() {
        return getAssociatedExecutionCoursesSet().iterator().next().getExecutionPeriod().getEndDate();
    }

    @Override
    public InfoEvaluation newInfoFromDomain() {
        InfoFinalEvaluation infoFinalEvaluation = new InfoFinalEvaluation();
        infoFinalEvaluation.copyFromDomain(this);
        return infoFinalEvaluation;
    }
}