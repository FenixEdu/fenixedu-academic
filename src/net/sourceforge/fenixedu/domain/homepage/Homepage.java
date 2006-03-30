package net.sourceforge.fenixedu.domain.homepage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;

public class Homepage extends Homepage_Base {

	public static final Comparator HOMEPAGE_COMPARATOR_BY_NAME = new BeanComparator("name");

    private static final Set<String> namePartsToIgnore = new HashSet<String>(5);
    static {
        namePartsToIgnore.add("de");
        namePartsToIgnore.add("da");
        namePartsToIgnore.add("do");
        namePartsToIgnore.add("a");
        namePartsToIgnore.add("e");
        namePartsToIgnore.add("i");
        namePartsToIgnore.add("o");
        namePartsToIgnore.add("u");
    }

	public Homepage() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

    @Override
    public void setName(String name) {
        if (!validHomepageName(name)) {
            throw new DomainException("error.homepage.name.invalid");
        }
        super.setName(name);
    }

    private boolean validHomepageName(final String name) {
        if (name != null && name.length() > 0) {
            final String normalizedName = name.toLowerCase();
            final String normalizedPersonName = getPerson().getName().toLowerCase();

            final String[] nameParts = normalizedName.split(" ");
            final String[] personNameParts = normalizedPersonName.split(" ");
            int matches = 0;
            for (final String namePart : nameParts) {
                if (!contains(personNameParts, namePart)) {
                    return false;
                }
                if (!namePartsToIgnore.contains(namePart)) {
                    matches++;
                }
            }
            if (matches >= 2) {
                return true;
            }
        }
        return false;
    }

    private boolean contains(final String[] strings, final String xpto) {
        if (xpto == null) {
            return false;
        }
        for (final String string : strings) {
            if (string.length() == xpto.length()
                    && string.hashCode() == xpto.hashCode()
                    && string.equals(xpto)) {
                return true;
            }
        }
        return false;
    }

    public SortedSet<ExecutionCourse> getCurrentExecutionCourses() {
        final SortedSet<ExecutionCourse> executionCourses = new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME);
        final Teacher teacher = getPerson().getTeacher();
        if (teacher != null) {
            for (final Professorship professorship : teacher.getProfessorshipsSet()) {
                final ExecutionCourse executionCourse = professorship.getExecutionCourse();
                final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
                final ExecutionPeriod nextExecutionPeriod = executionPeriod.getNextExecutionPeriod();
                if (executionPeriod.getState().equals(PeriodState.CURRENT)
                        || (nextExecutionPeriod != null && nextExecutionPeriod.getState().equals(PeriodState.CURRENT))) {
                    executionCourses.add(executionCourse);
                }
            }
        }
        return executionCourses;
    }

}
