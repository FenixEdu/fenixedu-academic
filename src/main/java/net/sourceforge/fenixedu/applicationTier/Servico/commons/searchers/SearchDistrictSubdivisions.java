package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.domain.DistrictSubdivision;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AutoCompleteProvider;

public class SearchDistrictSubdivisions implements AutoCompleteProvider<DistrictSubdivision> {

    private static int DEFAULT_SIZE = 50;

    @Override
    public Collection<DistrictSubdivision> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return DistrictSubdivision.findByName(value, getSize(argsMap));
    }

    private int getSize(Map<String, String> arguments) {
        String size = arguments.get("size");
        return size == null ? DEFAULT_SIZE : Integer.parseInt(size);
    }

}
