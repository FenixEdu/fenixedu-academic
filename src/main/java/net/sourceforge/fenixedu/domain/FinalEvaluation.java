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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.EvaluationType;

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
        for (Mark mark : getMarks()) {
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
        for (Mark mark : getMarks()) {
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

        for (Attends attends : executionCourse.getAttends()) {
            if (attends.getEnrolment() != null && attends.getRegistration().getDegreeType().equals(DegreeType.DEGREE)) {
                FinalMark mark = getFinalMark(attends);
                if (mark == null || (mark.getGradeListVersion() == 0 && mark.getSubmitedMark() == null)) {
                    result.add(attends);
                }
            }
        }
        return result;
    }

    private FinalMark getFinalMark(Attends attends) {
        for (Mark mark : attends.getAssociatedMarks()) {
            if (mark.getEvaluation().equals(this)) {
                return (FinalMark) mark;
            }
        }
        return null;
    }

    private void checkRulesToDelete() {
        if (hasAnyMarks()) {
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
}