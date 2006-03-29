package net.sourceforge.fenixedu.domain.homepage;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanComparator;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.util.PeriodState;

public class Homepage extends Homepage_Base {

	public static final Comparator HOMEPAGE_COMPARATOR_BY_NAME = new BeanComparator("name");

	public Homepage() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	@Override
	public void setMyUrl(String myUrl) {
		for (final Person person : Party.readAllPersons()) {
			final Homepage homepage = person.getHomepage();
			if (homepage != null && homepage.getMyUrl().equalsIgnoreCase(myUrl)) {
				throw new DomainException("homepage.url.exists");
			}
		}
		super.setMyUrl(myUrl);
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
