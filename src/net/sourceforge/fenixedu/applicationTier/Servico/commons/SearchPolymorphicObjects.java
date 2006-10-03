package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class SearchPolymorphicObjects extends AbstractSearchObjects implements AutoCompleteSearchService {

    public List<DomainObject> run(Class type, String value, int limit, Map<String, String> arguments) {
        Collection<DomainObject> objects = RootDomainObject.readAllDomainObjectsAux(type);

        return super.process(objects, value, limit, arguments);
    }
}
