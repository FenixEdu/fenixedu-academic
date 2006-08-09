package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.StringNormalizer;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;

public class SearchObjectsByMultiLanguageString extends Service implements AutoCompleteSearchService {

    public List<DomainObject> run(Class type, String value, int limit, Map<String, String> arguments) {
    	List<DomainObject> result = new ArrayList<DomainObject>();
        
        String slotName  = arguments.get("slot");
        Collection<DomainObject> objects = RootDomainObject.readAllDomainObjects(type);
        
        if (value == null) {
            result.addAll(objects);
        }
        else {
            String[] values = StringNormalizer.normalize(value).toLowerCase().split("\\p{Space}+");
            
            for (DomainObject object : objects) {
                try {
                    MultiLanguageString objectMLS = (MultiLanguageString) PropertyUtils.getProperty(object, slotName);
                    
                    if (objectMLS == null || objectMLS.getAllContents().size() == 0) {
                        continue;
                    }

                    for (Language language : objectMLS.getAllLanguages()) {
                        String objectValue = objectMLS.getContent(language);

                        String normalizedValue = StringNormalizer.normalize(objectValue).toLowerCase();
                        
                        boolean matches = true;
                        for (int i = 0; i < values.length; i++) {
                            String part = values[i];
                            
                            if (! normalizedValue.contains(part)) {
                                matches = false;
                            }
                        }
                        
                        if (matches) {
                            result.add(object);
                            break;
                        }
                    }
                    
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
        
        Collections.sort(result, new BeanComparator(slotName + ".content"));
        return result;
    }
}
