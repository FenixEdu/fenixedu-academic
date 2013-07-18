package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public abstract class AbstractSearchObjects<T> {

    protected Collection<T> process(Collection<T> objects, String value, int limit, Map<String, String> arguments) {
        List<T> result;

        String slotName = arguments.get("slot");

        if (value == null) {
            result = (List<T>) objects;
        } else {
            result = new ArrayList<T>();
            String[] values = StringNormalizer.normalize(value).toLowerCase().split("\\p{Space}+");

            outter: for (T object : objects) {
                try {
                    String objectValue = (String) PropertyUtils.getProperty(object, slotName);

                    if (objectValue == null) {
                        continue;
                    }

                    String normalizedValue = StringNormalizer.normalize(objectValue).toLowerCase();

                    for (int i = 0; i < values.length; i++) {
                        String part = values[i];

                        if (!normalizedValue.contains(part)) {
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
