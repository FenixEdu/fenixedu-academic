package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class SearchExternalUnits extends Service implements AutoCompleteSearchService {

    public Collection run(Class type, String value, int limit, Map<String, String> arguments){
        List<Unit> result;
        
        String slotName  = arguments.get("slot");
        
        if (value == null) {
            result = UnitUtils.readAllExternalInstitutionUnits();
        }
        else {
            result = new ArrayList<Unit>();
        	String[] values = StringNormalizer.normalize(value).toLowerCase().split("\\p{Space}+");
            
            outter: 
            for (Unit unit : UnitUtils.readExternalInstitutionUnit().getActiveSubUnits(new YearMonthDay())) {
                try {
                    String objectValue = (String) PropertyUtils.getProperty(unit, slotName);
                    
                    if (objectValue == null) {
                        continue;
                    }
                    
                    String normalizedValue = StringNormalizer.normalize(objectValue).toLowerCase();
                    
                    for (int i = 0; i < values.length; i++) {
                        String part = values[i];
                        
                        if (! normalizedValue.contains(part)) {
                            continue outter;
                        }
                    }

                    result.add(unit);
                    
                    if (result.size() >= limit) {
                        break;
                    }
                    
                } catch (IllegalAccessException e) {
                    throw new DomainException("searchObject.type.notFound", e);
                } catch (InvocationTargetException e) {
                    throw new DomainException("searchObject.failed.read", e);
                } catch (NoSuchMethodException e) {
                    throw new DomainException("searchObject.failed.read", e);
                }
            }
        }
        
        Collections.sort(result, new BeanComparator(slotName));
        return result;
    }
}
