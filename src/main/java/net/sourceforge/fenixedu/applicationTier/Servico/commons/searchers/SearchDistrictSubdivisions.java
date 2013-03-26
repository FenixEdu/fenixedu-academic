package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.DistrictSubdivision;

public class SearchDistrictSubdivisions extends FenixService implements AutoCompleteSearchService {

    private static int DEFAULT_SIZE = 50;

    @Override
    public Collection run(Class type, String value, int limit, Map<String, String> arguments) {
        return DistrictSubdivision.findByName(value, getSize(arguments));
    }

    private int getSize(Map<String, String> arguments) {
        String size = arguments.get("size");
        return size == null ? DEFAULT_SIZE : Integer.parseInt(size);
    }

}
