package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionSemester;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionDegreeForExecutionPeriodAcademicAdminProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();

        final HasExecutionSemester hasExecutionSemester = (HasExecutionSemester) source;
        final ExecutionSemester executionPeriod = hasExecutionSemester.getExecutionPeriod();
        if (executionPeriod != null) {
            final ExecutionYear executionYear = executionPeriod.getExecutionYear();
            executionDegrees.addAll(executionYear.getExecutionDegreesSet());
        }

        TreeSet<ExecutionDegree> result =
                new TreeSet<ExecutionDegree>(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);

        // ist150958: eliminate degrees for which there are no permissions
        for (ExecutionDegree executionDegree : executionDegrees) {
            if (AcademicPredicates.MANAGE_EXECUTION_COURSES.evaluate(executionDegree.getDegree())) {
                result.add(executionDegree);
            }
        }
        return result;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
