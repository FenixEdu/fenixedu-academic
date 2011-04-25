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
