/*
 * Attends.java
 *
 * Created on 20 de Outubro de 2002, 14:42
 */

package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.WeeklyWorkLoad;

import org.joda.time.DateMidnight;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

/**
 * 
 * @author tfc130
 */
public class Attends extends Attends_Base {

	public static final Comparator<Attends> ATTENDS_COMPARATOR = new Comparator<Attends>() {
		public int compare(final Attends attends1, final Attends attends2) {
			final ExecutionCourse executionCourse1 = attends1.getDisciplinaExecucao();
			final ExecutionCourse executionCourse2 = attends2.getDisciplinaExecucao();
			if (executionCourse1 == executionCourse2) {
				final Student student1 = attends1.getAluno();
				final Student student2 = attends2.getAluno();
				return student1.getNumber().compareTo(student2.getNumber());
			} else {
				final ExecutionPeriod executionPeriod1 = executionCourse1.getExecutionPeriod();
				final ExecutionPeriod executionPeriod2 = executionCourse2.getExecutionPeriod();
				if (executionPeriod1 == executionPeriod2) {
					return executionCourse1.getNome().compareTo(executionCourse2.getNome());
				} else {
					return executionPeriod1.compareTo(executionPeriod2);
				}
			}
		}
	};

    public Attends() {}
	
	public Attends (Student student, ExecutionCourse executionCourse) {
		setAluno(student);
		setDisciplinaExecucao(executionCourse);
	}
	
    public String toString() {
        String result = "[ATTEND";
        result += ", codigoInterno=" + getIdInternal();
        result += ", Student=" + getAluno();
        result += ", ExecutionCourse=" + getDisciplinaExecucao();
        result += ", Enrolment=" + getEnrolment();
        result += "]";
        return result;
    }

	public void delete() throws DomainException {
		
		if (!hasAnyShiftEnrolments() && !hasAnyStudentGroups() && !hasAnyAssociatedMarks()) {
			removeAluno();
			removeDisciplinaExecucao();
			removeEnrolment();
			super.deleteDomainObject();
		}
		else
			throw new DomainException("error.attends.cant.delete");
	}
	
	private boolean hasAnyShiftEnrolments() {
	    for (Shift shift : this.getDisciplinaExecucao().getAssociatedShifts()) {
            if (shift.getStudents().contains(this.getAluno())) {
                return true;
            }
        }
        return false;
    }

    public FinalMark getFinalMark() {
		for (Mark mark : getAssociatedMarks()) {
			if(mark instanceof FinalMark) {
				return (FinalMark) mark;
			}
		}
		return null;
	}

	public Mark getMarkByEvaluation(Evaluation evaluation) {
		for (Mark mark : getAssociatedMarks()) {
			if(mark.getEvaluation().equals(evaluation)) {
				return mark;
			}
		}
		return null;
	}

    public List<Mark> getAssociatedMarksOrderedByEvaluationDate() {
        final List<Evaluation> orderedEvaluations = getDisciplinaExecucao().getOrderedAssociatedEvaluations();
        final List<Mark> orderedMarks = new ArrayList<Mark>(orderedEvaluations.size());
        for (int i = 0; i < orderedEvaluations.size(); i++) {
            orderedMarks.add(null);
        }
        for (final Mark mark : getAssociatedMarks()) {
            final Evaluation evaluation = mark.getEvaluation();
            orderedMarks.set(orderedEvaluations.indexOf(evaluation), mark);
        }
        return orderedMarks;
    }

    public WeeklyWorkLoad createWeeklyWorkLoad(final Integer contact, final Integer autonomousStudy, final Integer other) {
        if (getEnrolment() == null) {
            throw new DomainException("weekly.work.load.creation.requires.enrolment");
        }

        final int currentWeekOffset = calculateCurrentWeekOffset();
        if (currentWeekOffset < 1 || getEndOfSemester().plusDays(7).isBefore(new YearMonthDay())) {
            throw new DomainException("outside.weekly.work.load.response.period");
        }

        final int previousWeekOffset = currentWeekOffset - 1;

        final WeeklyWorkLoad lastExistentWeeklyWorkLoad = getWeeklyWorkLoads().isEmpty() ?
                null : Collections.max(getWeeklyWorkLoads());
        if (lastExistentWeeklyWorkLoad != null && lastExistentWeeklyWorkLoad.getWeekOffset().intValue() == previousWeekOffset) {
            throw new DomainException("weekly.work.load.for.previous.week.already.exists");
        }

        return new WeeklyWorkLoad(this, Integer.valueOf(previousWeekOffset), contact, autonomousStudy, other);
    }

    public WeeklyWorkLoad getWeeklyWorkLoadOfPreviousWeek() {
        final int currentWeekOffset = calculateCurrentWeekOffset();
        if (currentWeekOffset < 1 || getEndOfSemester().plusDays(7).isBefore(new YearMonthDay())) {
            throw new DomainException("outside.weekly.work.load.response.period");
        }
        final int previousWeekOffset = currentWeekOffset - 1;
        for (final WeeklyWorkLoad weeklyWorkLoad : getWeeklyWorkLoads()) {
        	if (weeklyWorkLoad.getWeekOffset().intValue() == previousWeekOffset) {
        		return weeklyWorkLoad;
        	}
        }
        return null;
    }

    private int calculateCurrentWeekOffset() {
        final DateMidnight beginningOfSemester = getBeginningOfSemester().toDateMidnight();
        final DateMidnight firstMonday = beginningOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1);
        final DateMidnight now = new DateMidnight();
        final Period period = new Period(firstMonday, now);

        int extraWeek = period.getDays() > 0 ? 1 : 0;
        return (period.getYears() * 12 + period.getMonths()) * 4 + period.getWeeks() + extraWeek;
    }

    private YearMonthDay getBeginningOfSemester() {
        final ExecutionPeriod executionPeriod = getDisciplinaExecucao().getExecutionPeriod();
        return new YearMonthDay(executionPeriod.getBeginDate());
    }

    private YearMonthDay getEndOfSemester() {
        final ExecutionPeriod executionPeriod = getDisciplinaExecucao().getExecutionPeriod();
        return new YearMonthDay(executionPeriod.getEndDate());
    }

    public Set<WeeklyWorkLoad> getSortedWeeklyWorkLoads() {
    	return new TreeSet<WeeklyWorkLoad>(getWeeklyWorkLoads());
    }

}
