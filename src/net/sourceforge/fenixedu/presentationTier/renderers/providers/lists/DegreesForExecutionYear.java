package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchStudentsByDegreeParametersBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreesForExecutionYear implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	
	final SortedSet<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
	final SearchStudentsByDegreeParametersBean chooseDegreeBean = (SearchStudentsByDegreeParametersBean) source;

	for (final Degree degree : AccessControl.getPerson().getEmployee().getAdministrativeOffice().getAdministratedDegrees()) {
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

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
