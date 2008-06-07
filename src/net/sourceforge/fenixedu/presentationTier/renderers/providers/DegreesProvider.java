package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.apache.commons.collections.comparators.ComparableComparator;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

    	final List<Degree> degrees = new ArrayList<Degree>(RootDomainObject.getInstance().getDegrees());
    	
    	Collections.sort(degrees, new ComparableComparator());
    	
        return degrees; 
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
