package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.academicAdministration.DegreeByExecutionYearBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreesForExecutionYear implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        final SortedSet<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        final DegreeByExecutionYearBean chooseDegreeBean = (DegreeByExecutionYearBean) source;

        for (final Degree degree : chooseDegreeBean.getAdministratedDegrees()) {
            if (matchesExecutionYear(degree, chooseDegreeBean.getExecutionYear())
                    && matchesDegreeType(degree, chooseDegreeBean.getDegreeType())) {
                result.add(degree);
            }
        }

        return result;

    }

    private boolean matchesDegreeType(Degree degree, DegreeType degreeType) {
        return degreeType == null || degree.getDegreeType() == degreeType;
    }

    private boolean matchesExecutionYear(Degree degree, ExecutionYear executionYear) {
        if (executionYear == null) {
            return true;
        }

        for (final ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
            if (executionDegree.getDegree() == degree) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
