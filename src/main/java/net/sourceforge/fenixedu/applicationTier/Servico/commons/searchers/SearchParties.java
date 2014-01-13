package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

public abstract class SearchParties<T> implements AutoCompleteProvider<T> {

    private static int DEFAULT_SIZE = 50;

    @Override
    public Collection<T> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        int size = getSize(argsMap);
        return search(value, size);
    }

    protected abstract Collection<T> search(String value, int size);

    private int getSize(Map<String, String> arguments) {
        String size = arguments.get("size");

        if (size == null) {
            return DEFAULT_SIZE;
        } else {
            return Integer.parseInt(size);
        }
    }

}
