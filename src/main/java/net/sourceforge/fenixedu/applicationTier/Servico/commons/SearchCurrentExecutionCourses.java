package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class SearchCurrentExecutionCourses implements AutoCompleteProvider<ExecutionCourse> {

    @Override
    public Collection<ExecutionCourse> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();

        String slotName = argsMap.get("slot");
        Collection<ExecutionCourse> objects = ExecutionSemester.readActualExecutionSemester().getAssociatedExecutionCourses();

        if (value == null) {
            result.addAll(objects);
        } else {
            String[] values = StringNormalizer.normalize(value).toLowerCase().split("\\p{Space}+");

            outter: for (ExecutionCourse object : objects) {
                try {
                    Object objectValue = PropertyUtils.getProperty(object, slotName);

                    if (objectValue == null) {
                        continue;
                    }

                    String normalizedValue = StringNormalizer.normalize(objectValue.toString()).toLowerCase();

                    for (int i = 0; i < values.length; i++) {
                        String part = values[i];

                        if (!normalizedValue.contains(part)) {
                            continue outter;
                        }
                    }

                    result.add(object);

                    if (result.size() >= maxCount) {
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
