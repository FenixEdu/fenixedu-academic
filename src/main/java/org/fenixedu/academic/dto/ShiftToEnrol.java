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
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;

public class ShiftToEnrol extends DataTranferObject {

    private ExecutionCourse executionCourse;

    private ShiftType theoricType;

    private ShiftType praticType;

    private ShiftType laboratoryType;

    private ShiftType theoricoPraticType;

    private ShiftType fieldWorkType;
    private ShiftType problemsType;
    private ShiftType seminaryType;
    private ShiftType trainingType;
    private ShiftType tutorialOrientationType;

    private Shift theoricShift;

    private Shift praticShift;

    private Shift laboratoryShift;

    private Shift theoricoPraticShift;

    private Shift fieldWorkShift;
    private Shift problemsShift;
    private Shift seminaryShift;
    private Shift trainingShift;
    private Shift tutorialOrientationShift;

    private boolean enrolled;

    public boolean isEnrolled() {
        return enrolled;
    }

    public void setEnrolled(boolean enrolled) {
        this.enrolled = enrolled;
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    public Shift getLaboratoryShift() {
        return laboratoryShift;
    }

    public void setLaboratoryShift(Shift laboratoryShift) {
        this.laboratoryShift = laboratoryShift;
    }

    public ShiftType getLaboratoryType() {
        return laboratoryType;
    }

    public void setLaboratoryType(ShiftType laboratoryType) {
        this.laboratoryType = laboratoryType;
    }

    public Shift getPraticShift() {
        return praticShift;
    }

    public void setPraticShift(Shift praticShift) {
        this.praticShift = praticShift;
    }

    public ShiftType getPraticType() {
        return praticType;
    }

    public void setPraticType(ShiftType praticType) {
        this.praticType = praticType;
    }

    public Shift getTheoricoPraticShift() {
        return theoricoPraticShift;
    }

    public void setTheoricoPraticShift(Shift theoricoPraticShift) {
        this.theoricoPraticShift = theoricoPraticShift;
    }

    public ShiftType getTheoricoPraticType() {
        return theoricoPraticType;
    }

    public void setTheoricoPraticType(ShiftType theoricoPraticType) {
        this.theoricoPraticType = theoricoPraticType;
    }

    public Shift getTheoricShift() {
        return theoricShift;
    }

    public void setTheoricShift(Shift theoricShift) {
        this.theoricShift = theoricShift;
    }

    public ShiftType getTheoricType() {
        return theoricType;
    }

    public void setTheoricType(ShiftType theoricType) {
        this.theoricType = theoricType;
    }

    public ShiftType getFieldWorkType() {
        return fieldWorkType;
    }

    public void setFieldWorkType(ShiftType fieldWorkType) {
        this.fieldWorkType = fieldWorkType;
    }

    public ShiftType getProblemsType() {
        return problemsType;
    }

    public void setProblemsType(ShiftType problemsType) {
        this.problemsType = problemsType;
    }

    public ShiftType getSeminaryType() {
        return seminaryType;
    }

    public void setSeminaryType(ShiftType seminaryType) {
        this.seminaryType = seminaryType;
    }

    public ShiftType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(ShiftType trainingType) {
        this.trainingType = trainingType;
    }

    public ShiftType getTutorialOrientationType() {
        return tutorialOrientationType;
    }

    public void setTutorialOrientationType(ShiftType tutorialOrientationType) {
        this.tutorialOrientationType = tutorialOrientationType;
    }

    public Shift getFieldWorkShift() {
        return fieldWorkShift;
    }

    public void setFieldWorkShift(Shift fieldWorkShift) {
        this.fieldWorkShift = fieldWorkShift;
    }

    public Shift getProblemsShift() {
        return problemsShift;
    }

    public void setProblemsShift(Shift problemsShift) {
        this.problemsShift = problemsShift;
    }

    public Shift getSeminaryShift() {
        return seminaryShift;
    }

    public void setSeminaryShift(Shift seminaryShift) {
        this.seminaryShift = seminaryShift;
    }

    public Shift getTrainingShift() {
        return trainingShift;
    }

    public void setTrainingShift(Shift trainingShift) {
        this.trainingShift = trainingShift;
    }

    public Shift getTutorialOrientationShift() {
        return tutorialOrientationShift;
    }

    public void setTutorialOrientationShift(Shift tutorialOrientationShift) {
        this.tutorialOrientationShift = tutorialOrientationShift;
    }

}
