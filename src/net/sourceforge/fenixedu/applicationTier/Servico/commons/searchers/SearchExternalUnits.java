package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class SearchExternalUnits extends Service implements AutoCompleteSearchService {

    public List<Unit> run(Class type, String value, int limit, Map<String, String> arguments){
        List<Unit> result = new ArrayList<Unit>();
        
        String slotName  = arguments.get("slot");
        Collection<Unit> allExternalUnits = UnitUtils.readAllExternalInstitutionUnits();
        
        if (value == null) {
            result.addAll(allExternalUnits);
        }
        else {
            String[] values = StringNormalizer.normalize(value).toLowerCase().split("\\p{Space}+");
            
            outter: 
            for (Unit unit : allExternalUnits) {
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
