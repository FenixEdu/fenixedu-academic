package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.collections.comparators.ComparableComparator;

public class Over23IndividualCandidacyDegreesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
    	final List<Degree> degrees = new ArrayList<Degree>(Degree.readBolonhaDegrees(AdministrativeOfficeType.DEGREE));
    	Collections.sort(degrees, new ComparableComparator());
        return degrees; 
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
