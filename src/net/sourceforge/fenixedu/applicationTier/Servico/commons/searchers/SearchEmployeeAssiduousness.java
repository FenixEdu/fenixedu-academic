package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class SearchEmployeeAssiduousness extends Service implements AutoCompleteSearchService {

    public List<Employee> run(Class type, String value, int limit, Map<String, String> arguments){
        List<Employee> result = new ArrayList<Employee>();
        
        String slotName  = arguments.get("slot");
        Collection<Assiduousness> allAssiduousnesses = rootDomainObject.getAssiduousnesss();
        
        if (value == null) {
            for (Assiduousness assiduousness : allAssiduousnesses) {
                result.add(assiduousness.getEmployee());
            }
        }
        else {
            String[] values = StringNormalizer.normalize(value).toLowerCase().split("\\p{Space}+");
            
            outter: 
            for (Assiduousness assiduousness : allAssiduousnesses) {
                try {
                    Object objectValue = (Object) PropertyUtils.getProperty(assiduousness.getEmployee(), slotName);
                    
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

                    result.add(assiduousness.getEmployee());
                    
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
