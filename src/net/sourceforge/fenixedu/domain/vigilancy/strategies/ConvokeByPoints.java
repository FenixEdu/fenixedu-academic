package net.sourceforge.fenixedu.domain.vigilancy.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilancyWithCredits;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailableTypes;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;

public class ConvokeByPoints extends Strategy {

	public ConvokeByPoints() {
		super();
	}

	public StrategySugestion sugest(List<Vigilant> vigilants,
			WrittenEvaluation writtenEvaluation) {

		List<Vigilant> teachersSugestion = new ArrayList<Vigilant>();
		List<Vigilant> vigilantSugestion = new ArrayList<Vigilant>();
		Set<Person> incompatiblePersons = new HashSet<Person>();
		List<UnavailableInformation> unavailableVigilants = new ArrayList<UnavailableInformation>();

		if (writtenEvaluation.hasAnyVigilancys()) {
			incompatiblePersons
					.addAll(getIncompatiblePersons(writtenEvaluation));
		}


		
		final List<ExecutionCourse> executionCourses = writtenEvaluation.getAssociatedExecutionCourses();
		Collections.sort(vigilants, new VigilantComparator(executionCourses));
		
		for (Vigilant vigilant : vigilants) {

			if (vigilant.canBeConvokedForWrittenEvaluation(writtenEvaluation)
					&& !incompatiblePersons.contains(vigilant
							.getIncompatiblePerson())) {
					Teacher teacher = vigilant.getTeacher();
					if(teacher!=null && teacher.teachesAny(executionCourses))  {
						teachersSugestion.add(vigilant);
					}
					else {
						vigilantSugestion.add(vigilant);
					}
					incompatiblePersons.add(vigilant.getPerson());
			} else {
				if (!vigilantIsAlreadyConvokedForThisExam(vigilant,
						writtenEvaluation)) {
					UnavailableTypes reason;
					if (incompatiblePersons.contains(vigilant
							.getIncompatiblePerson())) {
						reason = UnavailableTypes.INCOMPATIBLE_PERSON;
					} else {
						reason = vigilant
								.getWhyIsUnavailabeFor(writtenEvaluation);
					}
					unavailableVigilants.add(new UnavailableInformation(vigilant, reason));
					
				}
			}
		}

		 ComparatorChain comparator = new ComparatorChain();
		 comparator.addComparator(new PointComparator());
		 comparator.addComparator(Vigilant.CATEGORY_COMPARATOR);
		 comparator.addComparator(new ReverseComparator(Vigilant.USERNAME_COMPARATOR));
		 
		 Collections.sort(vigilantSugestion, comparator);
		 Collections.sort(teachersSugestion, comparator);
		return new StrategySugestion(teachersSugestion, vigilantSugestion,
				unavailableVigilants);
	}

	private boolean vigilantIsAlreadyConvokedForThisExam(Vigilant vigilant,
			WrittenEvaluation writtenEvaluation) {
		List<Vigilancy> convokes = vigilant.getVigilancys();
		for (Vigilancy convoke : convokes) {
			if (convoke.getWrittenEvaluation().equals(writtenEvaluation))
				return true;
		}
		return false;
	}

	private List<Person> getIncompatiblePersons(
			WrittenEvaluation writtenEvaluation) {
		List<Vigilancy> convokes = writtenEvaluation.getVigilancys();
		List<Person> people = new ArrayList<Person>();
		for (Vigilancy convoke : convokes) {
			Vigilant vigilant = convoke.getVigilant();
			people.add(vigilant.getPerson());
		}
		return people;
	}

	class VigilantComparator implements Comparator {

		private List<ExecutionCourse> executionCourses;

		public VigilantComparator(List<ExecutionCourse> executionCourses) {
			this.executionCourses = executionCourses;
		}

		public int compare(Object o1, Object o2) {
			Vigilant vigilant1 = (Vigilant) o1;
			Vigilant vigilant2 = (Vigilant) o2;
			Teacher teacher1, teacher2;

			teacher1 = vigilant1.getTeacher();
			teacher2 = vigilant2.getTeacher();

			final boolean teacher1HasThatExecutionCourse = teacher1 != null
					&& teacher1.teachesAny(executionCourses);
			final boolean teacher2HasThatExecutionCourse = teacher2 != null
					&& teacher2.teachesAny(executionCourses);

			if (teacher1HasThatExecutionCourse
					&& !teacher2HasThatExecutionCourse)
				return -1;
			if ((teacher1HasThatExecutionCourse && teacher2HasThatExecutionCourse)
					|| (!teacher1HasThatExecutionCourse && !teacher2HasThatExecutionCourse))
				return 0;
			if (!teacher1HasThatExecutionCourse
					&& teacher2HasThatExecutionCourse)
				return 1;

			return 0;
		}

	}

	class PointComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			Vigilant v1 = (Vigilant) o1;
			Vigilant v2 = (Vigilant) o2;

			Integer p1 = v1.getPoints();
			Integer p2 = v2.getPoints();

			p1 += v1.getActiveConvokesAfterCurrentDate().size();
			p2 += v2.getActiveConvokesAfterCurrentDate().size();
			
			return p1.compareTo(p2);

		}

	}
}
