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
package net.sourceforge.fenixedu.dataTransferObject.directiveCouncil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;

public class ExecutionCourseSummaryElement implements Serializable {

    private ExecutionCourse executionCourse;
    private BigDecimal numberOfLessonInstances;
    private BigDecimal numberOfLessonInstancesWithSummary;
    // percentage value like xx.yy%
    private BigDecimal percentageOfLessonsWithSummary;
    private BigDecimal numberOfLessonInstancesWithNotTaughtSummary;
    private BigDecimal percentageOfLessonsWithNotTaughtSummary;

    public ExecutionCourseSummaryElement(ExecutionCourse executionCourse, BigDecimal numberOfLessonInstance,
            BigDecimal numberOfLessonInstanceWithSummary, BigDecimal percentageOfLessonsWithSummary,
            BigDecimal numberOfLessonInstancesWithNotTaughtSummary, BigDecimal percentageOfLessonsWithNotTaughtSummary) {
        setExecutionCourse(executionCourse);
        setNumberOfLessonInstances(numberOfLessonInstance);
        setNumberOfLessonInstancesWithSummary(numberOfLessonInstanceWithSummary);
        setPercentageOfLessonsWithSummary(percentageOfLessonsWithSummary);
        setNumberOfLessonInstancesWithNotTaughtSummary(numberOfLessonInstancesWithNotTaughtSummary);
        setPercentageOfLessonsWithNotTaughtSummary(percentageOfLessonsWithNotTaughtSummary);
    }

    public Set<Person> getPersons() {
        Set<Person> persons = new HashSet<Person>();
        for (Professorship professorship : getExecutionCourse().getProfessorships()) {
            persons.add(professorship.getPerson());
        }
        return persons;
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    public BigDecimal getNumberOfLessonInstances() {
        return numberOfLessonInstances;
    }

    public void setNumberOfLessonInstances(BigDecimal numberOfLessonInstances) {
        this.numberOfLessonInstances = numberOfLessonInstances;
    }

    public BigDecimal getNumberOfLessonInstancesWithSummary() {
        return numberOfLessonInstancesWithSummary;
    }

    public void setNumberOfLessonInstancesWithSummary(BigDecimal numberOfLessonInstancesWithSummary) {
        this.numberOfLessonInstancesWithSummary = numberOfLessonInstancesWithSummary;
    }

    public BigDecimal getPercentageOfLessonsWithSummary() {
        return percentageOfLessonsWithSummary.setScale(2);
    }

    public void setPercentageOfLessonsWithSummary(BigDecimal percentageOfLessonsWithSummary) {
        this.percentageOfLessonsWithSummary = percentageOfLessonsWithSummary;
    }

    public void setNumberOfLessonInstancesWithNotTaughtSummary(BigDecimal numberOfLessonInstancesWithNotTaughtSummary) {
        this.numberOfLessonInstancesWithNotTaughtSummary = numberOfLessonInstancesWithNotTaughtSummary;
    }

    public BigDecimal getNumberOfLessonInstancesWithNotTaughtSummary() {
        return numberOfLessonInstancesWithNotTaughtSummary;
    }

    public void setPercentageOfLessonsWithNotTaughtSummary(BigDecimal percentageOfLessonsWithNotTaughtSummary) {
        this.percentageOfLessonsWithNotTaughtSummary = percentageOfLessonsWithNotTaughtSummary;
    }

    public BigDecimal getPercentageOfLessonsWithNotTaughtSummary() {
        return percentageOfLessonsWithNotTaughtSummary.setScale(2);
    }
}
