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
package net.sourceforge.fenixedu.domain.vigilancy.strategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailableTypes;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;

import org.apache.commons.collections.comparators.ComparatorChain;

public class ConvokeByPoints extends Strategy {

    public ConvokeByPoints() {
        super();
    }

    @Override
    public StrategySugestion sugest(List<VigilantWrapper> vigilants, WrittenEvaluation writtenEvaluation) {

        List<VigilantWrapper> teachersSugestion = new ArrayList<VigilantWrapper>();
        List<VigilantWrapper> vigilantSugestion = new ArrayList<VigilantWrapper>();
        Set<Person> incompatiblePersons = new HashSet<Person>();
        List<UnavailableInformation> unavailableVigilants = new ArrayList<UnavailableInformation>();

        if (writtenEvaluation.hasAnyVigilancies()) {
            incompatiblePersons.addAll(getIncompatiblePersons(writtenEvaluation));
        }

        final Collection<ExecutionCourse> executionCourses = writtenEvaluation.getAssociatedExecutionCourses();

        for (VigilantWrapper vigilant : vigilants) {

            Person vigilantPerson = vigilant.getPerson();

            if (vigilant.canBeConvokedForWrittenEvaluation(writtenEvaluation)
                    && !incompatiblePersons.contains(vigilantPerson.getIncompatibleVigilantPerson())) {

                if (vigilantPerson.teachesAny(executionCourses)) {
                    teachersSugestion.add(vigilant);
                    incompatiblePersons.add(vigilant.getPerson());
                } else {
                    vigilantSugestion.add(vigilant);
                }

            } else {
                if (!vigilantIsAlreadyConvokedForThisExam(vigilant, writtenEvaluation)) {
                    UnavailableTypes reason;
                    if (incompatiblePersons.contains(vigilant.getPerson().getIncompatibleVigilant())) {
                        reason = UnavailableTypes.INCOMPATIBLE_PERSON;
                    } else {
                        reason = vigilant.getWhyIsUnavailabeFor(writtenEvaluation);
                    }
                    unavailableVigilants.add(new UnavailableInformation(vigilant, reason));

                }
            }
        }

        ComparatorChain comparator = new ComparatorChain();
        comparator.addComparator(VigilantWrapper.ESTIMATED_POINTS_COMPARATOR);
        // comparator.addComparator(new ConvokeComparator());
        comparator.addComparator(VigilantWrapper.CATEGORY_COMPARATOR);
        comparator.addComparator(VigilantWrapper.USERNAME_COMPARATOR);

        Collections.sort(vigilantSugestion, comparator);
        Collections.sort(teachersSugestion, comparator);
        return new StrategySugestion(teachersSugestion, vigilantSugestion, unavailableVigilants);
    }

    private boolean vigilantIsAlreadyConvokedForThisExam(VigilantWrapper vigilant, WrittenEvaluation writtenEvaluation) {
        Collection<Vigilancy> convokes = vigilant.getVigilanciesSet();
        for (Vigilancy convoke : convokes) {
            if (convoke.getWrittenEvaluation().equals(writtenEvaluation) && convoke.isActive()) {
                return true;
            }
        }
        return false;
    }

    private List<Person> getIncompatiblePersons(WrittenEvaluation writtenEvaluation) {
        Collection<Vigilancy> convokes = writtenEvaluation.getVigilancies();
        List<Person> people = new ArrayList<Person>();
        for (Vigilancy convoke : convokes) {
            VigilantWrapper vigilant = convoke.getVigilantWrapper();
            people.add(vigilant.getPerson());
        }
        return people;
    }
}
