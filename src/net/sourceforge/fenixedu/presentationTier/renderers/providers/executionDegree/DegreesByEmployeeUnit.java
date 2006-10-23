package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionDegree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class DegreesByEmployeeUnit implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	final List<Degree> result = new ArrayList<Degree>();
	Collection<Party> childParties = (Collection<Party>) AccessControl.getUserView().getPerson()
		.getEmployee().getCurrentWorkingPlace().getChildParties(
			AccountabilityTypeEnum.ACADEMIC_STRUCTURE, Unit.class);
	for (Party party : childParties) {
	    final Degree degree = ((Unit) party).getDegree();
	    if (degree.getDegreeType().canCreateStudent()
		    && !degree.getDegreeType().canCreateStudentOnlyWithCandidacy()) {
		result.add(degree);
	    }
	}

	Collections.sort(result, new BeanComparator("name"));
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
