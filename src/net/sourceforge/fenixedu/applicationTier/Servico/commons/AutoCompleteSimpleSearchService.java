package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.applicationTier.IService;
import sun.text.Normalizer;

public class AutoCompleteSimpleSearchService implements IService {

    public List run(Map<String, String> arguments) throws ExcepcaoPersistencia, FenixServiceException {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentObject persistentObject = sp.getIPersistentObject();

        final Class clazz;
        String className = arguments.get("class");
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new FenixServiceException("Error loading class " + className);
        }

        List result = new ArrayList(persistentObject.readAll(clazz));

        final String compareField = arguments.get("compareField");
        final String valueToSearch = arguments.get("value");

        result = filterResult(result, compareField, valueToSearch);

        final String sortField = arguments.get("sortField");

        if (sortField != null) {
            String ascendentOrderString = arguments.get("ascendentOrder");

            final boolean ascendentOrder = (ascendentOrderString != null) ? Boolean
                    .valueOf(ascendentOrderString) : true;

            result = sortResult(result, sortField, ascendentOrder);
        }

        return result;
    }

    private List sortResult(List resultToSort, final String sortField, final boolean ascendentOrder) {
        BeanComparator sortComparator = new BeanComparator(sortField);
        List result = new ArrayList(resultToSort);

        Collections.sort(result, sortComparator);

        if (ascendentOrder == false) {
            Collections.reverse(result);
        }

        return result;
    }

    private List filterResult(List result, final String fieldToCompare, final String valueToSearch) {
        Predicate predicate = new Predicate() {

            public boolean evaluate(Object object) {
                boolean result = false;
                // WrapDynaBean wrapDynaBean = new WrapDynaBean(object);

                String fieldValue;
                try {
                    fieldValue = BeanUtils.getProperty(object, fieldToCompare);
                } catch (Exception ex) {
                    throw new RuntimeException("Error getting field " + fieldToCompare
                            + " from result object");

                }

                String normalizedFieldValue = normalize(fieldValue);
                String normalizedValueToSearch = normalize(valueToSearch);

                if (normalizedFieldValue.toUpperCase().indexOf(normalizedValueToSearch.toUpperCase()) != -1) {
                    result = true;
                }

                return result;
            }
        };

        result = new ArrayList(CollectionUtils.select(result, predicate));
        return result;
    }

    private String normalize(String stringToNormalize) {
        return Normalizer.normalize(stringToNormalize, Normalizer.DECOMP, Normalizer.DONE).replaceAll(
                "[^\\p{ASCII}]", "").toLowerCase();
    }

}