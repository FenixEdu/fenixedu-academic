package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.student.SearchStudentsWithEnrolmentsByDepartment;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class DegreeSelectManySearchStudentsWithEnrolmentsByDepartment implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        final SearchStudentsWithEnrolmentsByDepartment bean = (SearchStudentsWithEnrolmentsByDepartment) source;
        final Department department = bean.getDepartment();
        final SortedSet<Degree> degrees = new TreeSet<Degree>(Degree.DEGREE_COMPARATOR_BY_NAME_AND_DEGREE_TYPE);
        degrees.addAll(department.getDegreesSet());
        return degrees;
    }

    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

}