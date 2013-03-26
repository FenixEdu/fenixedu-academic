package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collection;
import java.util.Map;

public interface AutoCompleteProvider {

    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount);

}
