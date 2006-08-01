package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.SpecialSeasonCode;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

public class SpecialSeasonCodesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

    	final List<SpecialSeasonCode> specialSeasonCodes = new ArrayList<SpecialSeasonCode>(RootDomainObject.getInstance().getSpecialSeasonCodesSet());
    	
    	Collections.sort(specialSeasonCodes, new BeanComparator("code"));
    	
        return specialSeasonCodes; 
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
