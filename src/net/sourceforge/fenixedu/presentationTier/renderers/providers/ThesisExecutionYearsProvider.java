package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

import org.apache.commons.collections.comparators.ReverseComparator;

public class ThesisExecutionYearsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        SortedSet<ExecutionYear> result = new TreeSet<ExecutionYear>(new ReverseComparator(ExecutionYear.COMPARATOR_BY_YEAR));
        
        for (Thesis thesis : RootDomainObject.getInstance().getTheses()) {
            result.add(thesis.getEnrolment().getExecutionYear());
        }
        
        return result; 
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
