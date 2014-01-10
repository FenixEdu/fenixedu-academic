package net.sourceforge.fenixedu.domain.util.icalendar;

import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

import org.joda.time.DateTime;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;

public class EvaluationEventBean extends EventBean {

    Set<ExecutionCourse> course;

    public EvaluationEventBean(String title, DateTime begin, DateTime end, boolean allDay, Set<AllocatableSpace> rooms,
            String url, String note, Set<ExecutionCourse> course) {
        super(title, begin, end, allDay, rooms, url, note);
        setCourses(course);
    }

    @Override
    public String getTitle() {
        final ImmutableSet<String> acronyms =
                FluentIterable.from(getCourses()).transform(new Function<ExecutionCourse, String>() {

                    @Override
                    public String apply(ExecutionCourse input) {
                        return input.getSigla();
                    }
                }).toSet();
        return super.getTitle() + " : " + Joiner.on("; ").join(acronyms);
    }

    @Override
    public String getOriginalTitle() {
        return super.getTitle();
    }

    public Set<ExecutionCourse> getCourses() {
        return course;
    }

    public void setCourses(Set<ExecutionCourse> course) {
        this.course = course;
    }
}
