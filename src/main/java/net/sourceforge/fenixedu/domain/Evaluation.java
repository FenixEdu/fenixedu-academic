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

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.EvaluationType;

import org.fenixedu.bennu.core.domain.Bennu;

public abstract class Evaluation extends Evaluation_Base {

    public Evaluation() {
        super();
        setGradeScale(GradeScale.TYPE20);
        setRootDomainObject(Bennu.getInstance());
    }

    public List<ExecutionCourse> getAttendingExecutionCoursesFor(final Registration registration) {
        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (final ExecutionCourse executionCourse : this.getAssociatedExecutionCourses()) {
            if (registration.attends(executionCourse)) {
                result.add(executionCourse);
            }
        }
        if (result.isEmpty()) { // Then user does not attend any executioncourse
            result.addAll(this.getAssociatedExecutionCourses());
        }
        return result;
    }

    public void delete() {
        this.getAssociatedExecutionCourses().clear();
        for (; !getMarks().isEmpty(); getMarks().iterator().next().delete()) {
            ;
        }
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public Mark addNewMark(Attends attends, String markValue) {
        if (attends.getMarkByEvaluation(this) != null) {
            throw new DomainException("error.Evaluation.attend.already.has.mark.for.evaluation");
        }
        return new Mark(attends, this, markValue);
    }

    public abstract EvaluationType getEvaluationType();

    public Mark getMarkByAttend(Attends attends) {
        for (Mark mark : getMarks()) {
            if (mark.getAttend().equals(attends)) {
                return mark;
            }
        }
        return null;
    }

    public boolean isFinal() {
        return false;
    }

    public String getPresentationName() {
        return null;
    }

    protected void logCreate() {
        logAuxBasic("log.executionCourse.evaluation.generic.created");
    }

    protected void logEdit() {
        logAuxBasic("log.executionCourse.evaluation.generic.edited");
    }

    protected void logRemove() {
        logAuxBasic("log.executionCourse.evaluation.generic.removed");
    }

    private void logAuxBasic(String key) {
        for (ExecutionCourse ec : getAssociatedExecutionCourses()) {
            EvaluationManagementLog.createLog(ec, Bundle.MESSAGING, key, getPresentationName(), ec.getName(),
                    ec.getDegreePresentationString());
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Mark> getMarks() {
        return getMarksSet();
    }

    @Deprecated
    public boolean hasAnyMarks() {
        return !getMarksSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionCourse> getAssociatedExecutionCourses() {
        return getAssociatedExecutionCoursesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedExecutionCourses() {
        return !getAssociatedExecutionCoursesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPublishmentMessage() {
        return getPublishmentMessage() != null;
    }

    @Deprecated
    public boolean hasGradeScale() {
        return getGradeScale() != null;
    }

}
