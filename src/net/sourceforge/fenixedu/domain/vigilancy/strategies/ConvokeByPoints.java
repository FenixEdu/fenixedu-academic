package net.sourceforge.fenixedu.domain.vigilancy.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailableTypes;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;

import org.apache.commons.collections.comparators.ComparatorChain;

public class ConvokeByPoints extends Strategy {

	public ConvokeByPoints() {
		super();
	}

	public StrategySugestion sugest(List<Vigilant> vigilants, WrittenEvaluation writtenEvaluation) {

		List<Vigilant> teachersSugestion = new ArrayList<Vigilant>();
		List<Vigilant> vigilantSugestion = new ArrayList<Vigilant>();
		Set<Person> incompatiblePersons = new HashSet<Person>();
		List<UnavailableInformation> unavailableVigilants = new ArrayList<UnavailableInformation>();

		if (writtenEvaluation.hasAnyVigilancies()) {
			incompatiblePersons.addAll(getIncompatiblePersons(writtenEvaluation));
		}

		final List<ExecutionCourse> executionCourses = writtenEvaluation.getAssociatedExecutionCourses();
	
		for (Vigilant vigilant : vigilants) {

			if (vigilant.canBeConvokedForWrittenEvaluation(writtenEvaluation)
					&& !incompatiblePersons.contains(vigilant.getIncompatiblePerson())) {
				Teacher teacher = vigilant.getTeacher();
				if (teacher != null && teacher.teachesAny(executionCourses)) {
					teachersSugestion.add(vigilant);
					incompatiblePersons.add(vigilant.getPerson());
				} else {
					vigilantSugestion.add(vigilant);
				}

			} else {
				if (!vigilantIsAlreadyConvokedForThisExam(vigilant, writtenEvaluation)) {
					UnavailableTypes reason;
					if (incompatiblePersons.contains(vigilant.getIncompatiblePerson())) {
						reason = UnavailableTypes.INCOMPATIBLE_PERSON;
					} else {
						reason = vigilant.getWhyIsUnavailabeFor(writtenEvaluation);
					}
					unavailableVigilants.add(new UnavailableInformation(vigilant, reason));

				}
			}
		}

		ComparatorChain comparator = new ComparatorChain();
		comparator.addComparator(Vigilant.ESTIMATED_POINTS_COMPARATOR);
		//comparator.addComparator(new ConvokeComparator());
		comparator.addComparator(Vigilant.CATEGORY_COMPARATOR);
		comparator.addComparator(Vigilant.USERNAME_COMPARATOR);

		Collections.sort(vigilantSugestion, comparator);
		Collections.sort(teachersSugestion, comparator);
		return new StrategySugestion(teachersSugestion, vigilantSugestion, unavailableVigilants);
	}

	private boolean vigilantIsAlreadyConvokedForThisExam(Vigilant vigilant,
			WrittenEvaluation writtenEvaluation) {
		List<Vigilancy> convokes = vigilant.getVigilancies();
		for (Vigilancy convoke : convokes) {
			if (convoke.getWrittenEvaluation().equals(writtenEvaluation) && convoke.isActive())
				return true;
		}
		return false;
	}

	private List<Person> getIncompatiblePersons(WrittenEvaluation writtenEvaluation) {
		List<Vigilancy> convokes = writtenEvaluation.getVigilancies();
		List<Person> people = new ArrayList<Person>();
		for (Vigilancy convoke : convokes) {
			Vigilant vigilant = convoke.getVigilant();
			people.add(vigilant.getPerson());
		}
		return people;
	}

}
