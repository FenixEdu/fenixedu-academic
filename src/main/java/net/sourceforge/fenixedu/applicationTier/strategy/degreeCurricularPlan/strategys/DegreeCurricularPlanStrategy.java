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
package net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys;

import java.util.StringTokenizer;

import net.sourceforge.fenixedu.dataTransferObject.InfoFinalResult;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.util.EvaluationType;
import net.sourceforge.fenixedu.util.MarkType;
import net.sourceforge.fenixedu.util.NumberUtils;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class DegreeCurricularPlanStrategy implements IDegreeCurricularPlanStrategy {

    private DegreeCurricularPlan degreeCurricularPlan = null;

    public DegreeCurricularPlanStrategy(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    @Override
    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    @Override
    public boolean checkMark(String mark) {
        return MarkType.getMarks(degreeCurricularPlan.getMarkType()).contains(mark);
    }

    @Override
    public boolean checkMark(String mark, EvaluationType et) {

        if (et.getType().intValue() == EvaluationType.FINAL) {
            return MarkType.getMarks(degreeCurricularPlan.getMarkType()).contains(mark);
        }

        boolean result = false;
        StringTokenizer st = new StringTokenizer(mark, ".");

        if (st.countTokens() > 0 && st.countTokens() < 3) {
            result = MarkType.getMarksEvaluation(degreeCurricularPlan.getMarkType()).contains(st.nextToken());
            if (result && st.hasMoreTokens()) {
                try {
                    Double markDouble = new Double(mark);
                    if (markDouble.doubleValue() < 0 || markDouble.doubleValue() > 20) {
                        result = false;
                    }
                } catch (NumberFormatException ex) {
                    return false;
                }
            }
        }
        return result;

    }

    @Override
    public Double calculateStudentRegularAverage(StudentCurricularPlan studentCurricularPlan) {
        float marks = 0;
        int numberOfCourses = 0;

        for (Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {

            if (enrolment.isEnrolmentStateApproved()
                    && !enrolment.getCurricularCourse().getType().equals(CurricularCourseType.P_TYPE_COURSE)) {
                if (!enrolment.isExtraCurricular()) {
                    final Grade grade = enrolment.getGrade();
                    if (grade.isNumeric()) {
                        marks += Integer.valueOf(grade.getValue());
                        numberOfCourses++;
                    } else {
                        // This mark will not count for the average
                    }
                }
            }
        }

        if (marks == 0) {
            return new Double(0);
        }

        return NumberUtils.formatNumber(new Double(marks / numberOfCourses), 1);
    }

    @Override
    public Double calculateStudentWeightedAverage(StudentCurricularPlan studentCurricularPlan) {
        float marks = 0;
        float numberOfWeigths = 0;

        for (Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {

            if (enrolment.isEnrolmentStateApproved()
                    && !enrolment.getCurricularCourse().getType().equals(CurricularCourseType.P_TYPE_COURSE)) {
                if (!enrolment.isExtraCurricular()) {
                    final Grade grade = enrolment.getGrade();
                    if (grade.isNumeric()) {
                        int enrolmentMark = Integer.valueOf(grade.getValue());
                        double enrolmentWeight = enrolment.getCurricularCourse().getCredits();

                        if (enrolmentMark > 0) {
                            marks += (enrolmentMark * enrolmentWeight);
                            numberOfWeigths += enrolmentWeight;
                        }
                    } else {
                        // This mark will not count for the average
                    }
                }
            }
        }

        if (marks == 0) {
            return new Double(0);
        }

        return NumberUtils.formatNumber(new Double(marks / numberOfWeigths), 1);
    }

    @Override
    public void calculateStudentAverage(StudentCurricularPlan studentCurricularPlan, InfoFinalResult infoFinalResult) {

        // Degrees that use the Mixed Average (Average between Simple and
        // Weighted average)
        // if
        // ((this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MT02/04")
        // )
        // ||
        // (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MT03/05"))
        // ){
        // Double simpleAverage =
        // this.calculateStudentRegularAverage(studentCurricularPlan);
        // Double weightedAverage =
        // this.calculateStudentWeightedAverage(studentCurricularPlan);
        //			
        // infoFinalResult.setAverageSimple(String.valueOf(simpleAverage));
        // infoFinalResult.setAverageWeighted(String.valueOf(weightedAverage));
        //			
        // infoFinalResult.setFinalAverage(String.valueOf(NumberUtils.formatNumber
        // (new
        // Double((simpleAverage.floatValue()+weightedAverage.floatValue())/2),
        // 0)));
        // return;
        // }

        // Degrees that use the Best Average (Best between Simple and Weighted
        // average)
        if ((this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MB02/04"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MB03/05"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MIOES02/04"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MT02/04"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MT05/07"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MT03/05"))) {
            Double simpleAverage = this.calculateStudentRegularAverage(studentCurricularPlan);
            Double weightedAverage = this.calculateStudentWeightedAverage(studentCurricularPlan);
            infoFinalResult.setAverageSimple(String.valueOf(NumberUtils.formatNumber(simpleAverage, 1).doubleValue()));
            infoFinalResult.setAverageWeighted(String.valueOf(NumberUtils.formatNumber(weightedAverage, 1).doubleValue()));

            if (simpleAverage.floatValue() > weightedAverage.floatValue()) {
                infoFinalResult.setFinalAverage(String.valueOf(NumberUtils.formatNumber(simpleAverage, 0).intValue()));
            } else {
                infoFinalResult.setFinalAverage(String.valueOf(NumberUtils.formatNumber(weightedAverage, 0).intValue()));
            }
            return;
        }

        // Degrees that use the Weighted Average
        if ((this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MEE02/04"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MEE03/05"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MF02/04"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MF03/05"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MC02/04"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MC03/05"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MEMAT02/04"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MEQ03/04"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MSIG02/04"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MCES02/04"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MEIC02/04"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MEIC03/05"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("ML03/05"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("ML02/04"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("ML05/07"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MEE04/06"))
                || (this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MEE05/07"))) {

            Double weightedAverage = this.calculateStudentWeightedAverage(studentCurricularPlan);
            infoFinalResult.setAverageWeighted(String.valueOf(NumberUtils.formatNumber(weightedAverage, 1).doubleValue()));
            infoFinalResult.setFinalAverage(String.valueOf(NumberUtils.formatNumber(weightedAverage, 0).intValue()));
            return;
        }

        // Everything else uses the simple Average

        Double simpleAverage = this.calculateStudentRegularAverage(studentCurricularPlan);

        infoFinalResult.setAverageSimple(String.valueOf(NumberUtils.formatNumber(simpleAverage, 1).doubleValue()));

        infoFinalResult.setFinalAverage(String.valueOf(NumberUtils.formatNumber(simpleAverage, 0).intValue()));

    }

}