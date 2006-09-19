package net.sourceforge.fenixedu.domain.vigilancy.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.Convoke;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;

public class ConvokeByPoints extends Strategy {

    public ConvokeByPoints() {
        super();
    }

    public StrategySugestion sugest(List<Vigilant> vigilants, WrittenEvaluation writtenEvaluation) {

        List<Vigilant> vigilantSugestion = new ArrayList<Vigilant>();
        Set<Person> incompatiblePersons = new HashSet<Person>();
        List<Vigilant> unavailableVigilants = new ArrayList<Vigilant>();
        List<Vigilant> teachersThatTeachClass = new ArrayList<Vigilant>();

        if (writtenEvaluation.hasAnyConvokes()) {
            incompatiblePersons.addAll(getIncompatiblePersons(writtenEvaluation));
        }

        final List<ExecutionCourse> executionCourses = writtenEvaluation.getAssociatedExecutionCourses();

        for (Vigilant vigilant : vigilants) {

            if (vigilant.canBeConvokedForWrittenEvaluation(writtenEvaluation)
                    && !incompatiblePersons.contains(vigilant.getIncompatiblePerson())) {
                if (vigilant.getPerson().hasTeacher()) {
                    Teacher teacher = vigilant.getPerson().getTeacher();
                        if (teacher.teachesAny(executionCourses)) {
                            teachersThatTeachClass.add(vigilant);
                        } else {
                            vigilantSugestion.add(vigilant);
                        }
                        incompatiblePersons.add(vigilant.getPerson());
                    
                } else {
                    vigilantSugestion.add(vigilant);
                    incompatiblePersons.add(vigilant.getPerson());
                }
            } else {
                if (!vigilantIsAlreadyConvokedForThisExam(vigilant, writtenEvaluation)) {
                    unavailableVigilants.add(vigilant);
                }
            }
        }

        // ComparatorChain comparator = new ComparatorChain();
        // comparator.addComparator(new VigilantComparator(executionCourses));
        // comparator.addComparator(new PointComparator());
        Collections.sort(vigilantSugestion, new PointComparator());
        Collections.sort(teachersThatTeachClass, new PointComparator());

        return new StrategySugestion(teachersThatTeachClass, vigilantSugestion, unavailableVigilants);
    }

    private boolean vigilantIsAlreadyConvokedForThisExam(Vigilant vigilant,
            WrittenEvaluation writtenEvaluation) {
        List<Convoke> convokes = vigilant.getConvokes();
        for (Convoke convoke : convokes) {
            if (convoke.getWrittenEvaluation().equals(writtenEvaluation))
                return true;
        }
        return false;
    }

    private List<Person> getIncompatiblePersons(WrittenEvaluation writtenEvaluation) {
        List<Convoke> convokes = writtenEvaluation.getConvokes();
        List<Person> people = new ArrayList<Person>();
        for (Convoke convoke : convokes) {
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

            if (teacher1HasThatExecutionCourse && !teacher2HasThatExecutionCourse)
                return -1;
            if ((teacher1HasThatExecutionCourse && teacher2HasThatExecutionCourse)
                    || (!teacher1HasThatExecutionCourse && !teacher2HasThatExecutionCourse))
                return 0;
            if (!teacher1HasThatExecutionCourse && teacher2HasThatExecutionCourse)
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

            return p1.compareTo(p2);

        }

    }
}
