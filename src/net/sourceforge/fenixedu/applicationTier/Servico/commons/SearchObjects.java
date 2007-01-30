package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class SearchObjects extends Service implements AutoCompleteSearchService {

    public List<DomainObject> run(Class type, String value, int limit, Map<String, String> arguments) {
        List<DomainObject> result = new ArrayList<DomainObject>();
        
        String slotName  = arguments.get("slot");
        Collection<DomainObject> objects = RootDomainObject.readAllDomainObjects(type);
        
        if (value == null) {
            result.addAll(objects);
        }
        else {
            String[] values = StringNormalizer.normalize(value).toLowerCase().split("\\p{Space}+");
            
            outter: 
            for (DomainObject object : objects) {
                try {
                    Object objectValue = (Object) PropertyUtils.getProperty(object, slotName);
                    
                    if (objectValue == null) {
                        continue;
                    }
                    
                    String normalizedValue = StringNormalizer.normalize(objectValue.toString()).toLowerCase();
                    
                    for (int i = 0; i < values.length; i++) {
                        String part = values[i];
                        
                        if (! normalizedValue.contains(part)) {
                            continue outter;
                        }
                    }

                    result.add(object);
                    
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
