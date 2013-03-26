package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixframework.DomainObject;

public class SearchPolymorphicObjects extends AbstractSearchObjects implements AutoCompleteSearchService {

    @Override
    public Collection run(Class type, String value, int limit, Map<String, String> arguments) {
        Collection<DomainObject> objects = RootDomainObject.readAllDomainObjectsAux(type);

        return super.process(objects, value, limit, arguments);
    }
}
