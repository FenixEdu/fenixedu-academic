package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import pt.ist.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;

import pt.utl.ist.fenix.tools.util.StringNormalizer;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class SearchEventEditionByMultiLanguageString implements AutoCompleteProvider<EventEdition> {

    @Override
    public Collection<EventEdition> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        List<EventEdition> result = new ArrayList<EventEdition>();

        String slotName = argsMap.get("slot");
        Collection<EventEdition> objects = Bennu.getInstance().getEventEditionsSet();

        if (value == null) {
            result.addAll(objects);
        } else {
            String[] values = StringNormalizer.normalize(value).toLowerCase().split("\\p{Space}+");

            for (EventEdition object : objects) {
                try {
                    MultiLanguageString objectMLS = (MultiLanguageString) PropertyUtils.getProperty(object, slotName);

                    if (objectMLS == null || objectMLS.getAllContents().size() == 0) {
                        continue;
                    }

                    for (Language language : objectMLS.getAllLanguages()) {
                        String objectValue = objectMLS.getContent(language);

                        String normalizedValue = StringNormalizer.normalize(objectValue).toLowerCase();

                        boolean matches = true;
                        for (String value2 : values) {
                            String part = value2;

                            if (!normalizedValue.contains(part)) {
                                matches = false;
                            }
                        }

                        if (matches) {
                            result.add(object);
                            break;
                        }
                    }

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

        Collections.sort(result, new BeanComparator(slotName + ".content"));
        return result;
    }

}
